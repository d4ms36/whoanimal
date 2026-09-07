package com.whoanimal.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.whoanimal.app.data.local.entities.StorageSlotEntity

@Dao
interface StorageSlotDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(slot: StorageSlotEntity)

    @Query("SELECT * FROM storage_slots WHERE cardId = :cardId LIMIT 1")
    suspend fun getByCardId(cardId: String): StorageSlotEntity?

    @Query("SELECT * FROM storage_slots WHERE containerIndex = :containerIndex AND slotIndex = :slotIndex LIMIT 1")
    suspend fun getByLocation(containerIndex: Int, slotIndex: Int): StorageSlotEntity?

    @Query("SELECT * FROM storage_slots WHERE containerIndex = :containerIndex ORDER BY slotIndex ASC")
    suspend fun getByContainer(containerIndex: Int): List<StorageSlotEntity>

    @Query("SELECT * FROM storage_slots ORDER BY containerIndex ASC, slotIndex ASC")
    suspend fun getAll(): List<StorageSlotEntity>

    @Query("DELETE FROM storage_slots WHERE cardId = :cardId")
    suspend fun deleteByCardId(cardId: String): Int

    @Query("DELETE FROM storage_slots WHERE containerIndex = :containerIndex AND slotIndex = :slotIndex")
    suspend fun deleteByLocation(containerIndex: Int, slotIndex: Int): Int

    @Query("SELECT COUNT(*) FROM storage_slots")
    suspend fun getOccupiedCount(): Int
}
