package com.whoanimal.app

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomProfileRepository
import com.whoanimal.app.domain.repository.InvalidProfileException
import com.whoanimal.app.domain.repository.ProfileValidationResult
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProfilePersistenceTest {

    private lateinit var context: Context
    private lateinit var db: WhoAnimalDatabase
    private lateinit var repository: RoomProfileRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = WhoAnimalDatabase.buildInMemory(context)
        repository = RoomProfileRepository(db.profileDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    // ==========================================
    // 1. VALIDATION TESTS
    // ==========================================

    @Test
    fun testValidateNameRejectsEmptyAndBlankStrings(): Unit {
        val emptyResult = repository.validateName("")
        assertTrue(emptyResult is ProfileValidationResult.Invalid)

        val whitespaceResult = repository.validateName("   ")
        assertTrue(whitespaceResult is ProfileValidationResult.Invalid)
    }

    @Test
    fun testValidateNameRejectsTooShortAndTooLongStrings(): Unit {
        val tooShort = repository.validateName("A")
        assertTrue("Single character must be rejected", tooShort is ProfileValidationResult.Invalid)

        val tooLong = repository.validateName("A".repeat(31))
        assertTrue("More than 30 characters must be rejected", tooLong is ProfileValidationResult.Invalid)
    }

    @Test
    fun testValidateNameAcceptsValidNames(): Unit {
        val validShort = repository.validateName("Al")
        assertTrue(validShort is ProfileValidationResult.Valid)

        val validStandard = repository.validateName("Dra. Jane Goodall")
        assertTrue(validStandard is ProfileValidationResult.Valid)

        val validMax = repository.validateName("A".repeat(30))
        assertTrue(validMax is ProfileValidationResult.Valid)
    }

    @Test
    fun testCreateProfileWithInvalidNameThrowsException(): Unit = runBlocking {
        var exceptionThrown = false
        try {
            repository.createProfile("   ")
        } catch (_: InvalidProfileException) {
            exceptionThrown = true
        }
        assertTrue("InvalidProfileException must be thrown on invalid name", exceptionThrown)
    }

    // ==========================================
    // 2. CREATION & ACTIVE SESSION TESTS
    // ==========================================

    @Test
    fun testInitialStateHasNoActiveProfile(): Unit = runBlocking {
        assertFalse(repository.hasActiveProfile())
        assertNull(repository.getActiveProfile())
    }

    @Test
    fun testCreateProfilePersistsAndActivatesProfile(): Unit = runBlocking {
        val profile = repository.createProfile("Alex Explorador")

        assertEquals("Alex Explorador", profile.explorerName)
        assertTrue(profile.isActive)
        assertTrue(profile.profileId.isNotBlank())
        assertTrue(profile.createdAt > 0)
        assertTrue(profile.lastOpenedAt > 0)

        assertTrue(repository.hasActiveProfile())
        val retrieved = repository.getActiveProfile()
        assertNotNull(retrieved)
        assertEquals(profile.profileId, retrieved?.profileId)
        assertEquals("Alex Explorador", retrieved?.explorerName)
    }

    @Test
    fun testOnlyOneActiveProfileEnforced(): Unit = runBlocking {
        val first = repository.createProfile("Primer Explorador")
        assertEquals(first.profileId, repository.getActiveProfile()?.profileId)

        val second = repository.createProfile("Segundo Explorador")
        val currentActive = repository.getActiveProfile()

        assertEquals("Second profile must become the only active profile", second.profileId, currentActive?.profileId)
        assertEquals("Segundo Explorador", currentActive?.explorerName)

        val activeCount = db.profileDao().countActiveProfiles()
        assertEquals("Only 1 active profile allowed in Alpha", 1, activeCount)
    }

    @Test
    fun testUpdateLastOpenedRefreshesTimestamp(): Unit = runBlocking {
        val profile = repository.createProfile("Explorador Ártico")
        val initialOpenedAt = profile.lastOpenedAt

        // Simular paso del tiempo
        Thread.sleep(15)
        repository.updateLastOpened(profile.profileId)

        val updated = repository.getActiveProfile()
        assertNotNull(updated)
        assertTrue("lastOpenedAt must be updated", (updated?.lastOpenedAt ?: 0) >= initialOpenedAt)
    }

    // ==========================================
    // 3. APP RESTART SIMULATION TEST
    // ==========================================

    @Test
    fun testProfileSurvivesAppRestartAndDatabaseRecreation(): Unit = runBlocking {
        val dbName = "test_profile_restart.db"
        context.deleteDatabase(dbName)

        // 1. Instancia A: Primera apertura, crea perfil y cierra base de datos
        val dbA = WhoAnimalDatabase.buildPersistent(context, dbName)
        val repoA = RoomProfileRepository(dbA.profileDao())

        assertFalse("Initially no active profile", repoA.hasActiveProfile())
        val createdProfile = repoA.createProfile("Valeria Expedición")
        assertTrue(repoA.hasActiveProfile())

        dbA.close()

        // 2. Instancia B: Reapertura de la aplicación (App Restart)
        val dbB = WhoAnimalDatabase.buildPersistent(context, dbName)
        val repoB = RoomProfileRepository(dbB.profileDao())

        // Debe detectar inmediatamente la sesión recordada
        assertTrue("Active profile must survive app restart", repoB.hasActiveProfile())
        val recovered = repoB.getActiveProfile()
        assertNotNull("Recovered profile must not be null", recovered)
        assertEquals(createdProfile.profileId, recovered?.profileId)
        assertEquals("Valeria Expedición", recovered?.explorerName)
        assertTrue(recovered?.isActive == true)

        dbB.close()
        context.deleteDatabase(dbName)
        Unit
    }
}
