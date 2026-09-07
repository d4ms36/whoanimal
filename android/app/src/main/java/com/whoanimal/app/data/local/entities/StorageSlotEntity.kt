package com.whoanimal.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.whoanimal.app.domain.repository.StorageSlotRecord

/**
 * Entidad Room para el mapeo de ubicación de cartas en la colección Alpha (10 contenedores x 30 slots).
 *
 * Reglas de integridad:
 * - cardId es clave primaria y clave foránea referenciando a cards con borrado en cascada (CASCADE).
 * - (containerIndex, slotIndex) cuenta con un índice ÚNICO (dos cartas no pueden ocupar el mismo slot).
 */
@Entity(
    tableName = "storage_slots",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["cardId"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["containerIndex", "slotIndex"], unique = true)
    ]
)
data class StorageSlotEntity(
    @PrimaryKey
    val cardId: String,
    @ColumnInfo(name = "containerIndex")
    val containerIndex: Int,
    @ColumnInfo(name = "slotIndex")
    val slotIndex: Int,
    @ColumnInfo(name = "assignedAt")
    val assignedAt: String
) {
    fun toRecord(): StorageSlotRecord {
        return StorageSlotRecord(
            cardId = cardId,
            containerIndex = containerIndex,
            slotIndex = slotIndex,
            assignedAt = assignedAt
        )
    }

    companion object {
        fun fromRecord(record: StorageSlotRecord): StorageSlotEntity {
            return StorageSlotEntity(
                cardId = record.cardId,
                containerIndex = record.containerIndex,
                slotIndex = record.slotIndex,
                assignedAt = record.assignedAt
            )
        }
    }
}
