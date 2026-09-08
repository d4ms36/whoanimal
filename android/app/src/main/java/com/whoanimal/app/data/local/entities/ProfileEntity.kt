package com.whoanimal.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.whoanimal.app.domain.model.ExplorerProfile

/**
 * Entidad Room para la tabla `profiles`.
 *
 * Mapea la información de perfil local asegurando persistencia offline
 * y soporte para consulta indexada del perfil activo.
 */
@Entity(
    tableName = "profiles",
    indices = [
        Index(value = ["is_active"])
    ]
)
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "profile_id")
    val profileId: String,

    @ColumnInfo(name = "explorer_name")
    val explorerName: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "last_opened_at")
    val lastOpenedAt: Long,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,

    @ColumnInfo(name = "lore_edits_used", defaultValue = "0")
    val loreEditsUsed: Int = 0,

    @ColumnInfo(name = "password_hash")
    val passwordHash: String? = null
) {
    fun toDomain(): ExplorerProfile = ExplorerProfile(
        profileId = profileId,
        explorerName = explorerName,
        createdAt = createdAt,
        lastOpenedAt = lastOpenedAt,
        isActive = isActive,
        loreEditsUsed = loreEditsUsed,
        passwordHash = passwordHash
    )

    companion object {
        fun fromDomain(profile: ExplorerProfile): ProfileEntity = ProfileEntity(
            profileId = profile.profileId,
            explorerName = profile.explorerName,
            createdAt = profile.createdAt,
            lastOpenedAt = profile.lastOpenedAt,
            isActive = profile.isActive,
            loreEditsUsed = profile.loreEditsUsed,
            passwordHash = profile.passwordHash
        )
    }
}
