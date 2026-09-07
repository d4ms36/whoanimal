package com.whoanimal.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.whoanimal.app.data.local.entities.CardEntity

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(card: CardEntity)

    @Query("SELECT * FROM cards WHERE cardId = :cardId LIMIT 1")
    suspend fun getById(cardId: String): CardEntity?

    @Query("SELECT * FROM cards WHERE captureId = :captureId LIMIT 1")
    suspend fun getByCaptureId(captureId: String): CardEntity?

    @Query("SELECT * FROM cards ORDER BY specimenNumber ASC, issuedAt DESC")
    suspend fun getAll(): List<CardEntity>

    @Query("DELETE FROM cards WHERE cardId = :cardId")
    suspend fun deleteById(cardId: String): Int

    @Query("SELECT COUNT(*) FROM cards WHERE cardId = :cardId")
    suspend fun countById(cardId: String): Int

    @Query("SELECT COUNT(*) FROM cards WHERE captureId = :captureId")
    suspend fun countByCaptureId(captureId: String): Int

    @Query("SELECT COUNT(*) FROM cards")
    suspend fun countAll(): Int

    @Query("UPDATE cards SET personalLore = :lore WHERE cardId = :cardId")
    suspend fun updatePersonalLore(cardId: String, lore: String?): Int
}
