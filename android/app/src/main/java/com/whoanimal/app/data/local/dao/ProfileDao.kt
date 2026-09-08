package com.whoanimal.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.whoanimal.app.data.local.entities.ProfileEntity

/**
 * Data Access Object (DAO) para operaciones sobre perfiles locales.
 */
@Dao
interface ProfileDao {

    @Query("SELECT * FROM profiles WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveProfile(): ProfileEntity?

    @Query("SELECT COUNT(*) FROM profiles WHERE is_active = 1")
    suspend fun countActiveProfiles(): Int

    @Query("SELECT * FROM profiles WHERE explorer_name = :name LIMIT 1")
    suspend fun getProfileByName(name: String): ProfileEntity?

    @Query("SELECT COUNT(*) FROM profiles")
    suspend fun countProfiles(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("UPDATE profiles SET is_active = 0 WHERE is_active = 1")
    suspend fun deactivateAllProfiles()

    @Query("UPDATE profiles SET last_opened_at = :timestamp WHERE profile_id = :profileId")
    suspend fun updateLastOpened(profileId: String, timestamp: Long)

    @Query("DELETE FROM profiles")
    suspend fun deleteAllProfiles()

    @Query("UPDATE profiles SET lore_edits_used = lore_edits_used + 1 WHERE profile_id = :profileId AND lore_edits_used < 3")
    suspend fun incrementLoreEdits(profileId: String): Int

    @Query("SELECT lore_edits_used FROM profiles WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveProfileLoreEditsUsed(): Int?

    /**
     * Garantiza de forma atómica que solo un perfil esté activo a la vez.
     */
    @Transaction
    suspend fun setActiveProfile(profile: ProfileEntity) {
        deactivateAllProfiles()
        insertProfile(profile)
    }
}
