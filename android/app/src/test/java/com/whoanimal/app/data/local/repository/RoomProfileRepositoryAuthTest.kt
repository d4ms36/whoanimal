package com.whoanimal.app.data.local.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.domain.repository.AuthUtil
import com.whoanimal.app.domain.repository.InvalidProfileException
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
class RoomProfileRepositoryAuthTest {

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

    @Test
    fun bootstrapAdminIfNeeded_createsAdminWhenNoUsersExist(): Unit = runBlocking {
        repository.bootstrapAdminIfNeeded()
        
        val dao = db.profileDao()
        assertEquals(1, dao.countProfiles())
        
        val adminEntity = dao.getProfileByName("admin")
        assertNotNull(adminEntity)
        assertEquals("admin", adminEntity?.explorerName)
        assertEquals(false, adminEntity?.isActive)
        assertEquals(AuthUtil.hash("1234"), adminEntity?.passwordHash)
    }

    @Test
    fun bootstrapAdminIfNeeded_doesNothingIfUsersExist(): Unit = runBlocking {
        repository.createProfile("some_user")
        
        repository.bootstrapAdminIfNeeded()
        
        val dao = db.profileDao()
        assertEquals(1, dao.countProfiles())
        assertNull(dao.getProfileByName("admin"))
    }

    @Test
    fun authenticateOrCreateProfile_withValidPassword_activatesProfile(): Unit = runBlocking {
        repository.bootstrapAdminIfNeeded()
        val dao = db.profileDao()
        val admin = dao.getProfileByName("admin")
        assertNotNull(admin)
        assertFalse(admin!!.isActive)
        
        val result = repository.authenticateOrCreateProfile("admin", "1234")
        
        assertEquals("admin", result.explorerName)
        
        val activeAdmin = dao.getProfileByName("admin")
        assertTrue(activeAdmin!!.isActive)
        assertEquals(admin.profileId, repository.getActiveProfile()?.profileId)
    }

    @Test(expected = InvalidProfileException::class)
    fun authenticateOrCreateProfile_withInvalidPassword_throwsException(): Unit = runBlocking {
        repository.bootstrapAdminIfNeeded()
        repository.authenticateOrCreateProfile("admin", "wrong_password")
    }

    @Test
    fun authenticateOrCreateProfile_createsNewProfileIfNotFound(): Unit = runBlocking {
        val result = repository.authenticateOrCreateProfile("new_user", "mypass")
        
        assertEquals("new_user", result.explorerName)
        assertTrue(result.isActive)
        assertEquals(AuthUtil.hash("mypass"), result.passwordHash)
        
        val dao = db.profileDao()
        val active = dao.getActiveProfile()
        assertNotNull(active)
        assertEquals("new_user", active?.explorerName)
    }
}
