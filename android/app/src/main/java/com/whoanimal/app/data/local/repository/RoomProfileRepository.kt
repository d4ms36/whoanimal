package com.whoanimal.app.data.local.repository

import com.whoanimal.app.data.local.dao.ProfileDao
import com.whoanimal.app.data.local.entities.ProfileEntity
import com.whoanimal.app.domain.model.ExplorerProfile
import com.whoanimal.app.domain.repository.InvalidProfileException
import com.whoanimal.app.domain.repository.LoreConstants
import com.whoanimal.app.domain.repository.ProfileRepository
import com.whoanimal.app.domain.repository.ProfileValidationResult
import com.whoanimal.app.domain.repository.AuthUtil
import java.util.UUID

/**
 * Implementación de [ProfileRepository] respaldada por Room / SQLite.
 *
 * Aplica reglas de negocio:
 * - Validación estricta del nombre (no vacío, sin solo espacios, 2..30 caracteres).
 * - Garantía de un único perfil activo en Alpha 0.1.
 * - Registro de timestamps de creación y última apertura.
 * - Cuota global de 3 ediciones de Lore por cuenta (DEC-041).
 */
class RoomProfileRepository(
    private val profileDao: ProfileDao
) : ProfileRepository {

    override suspend fun getActiveProfile(): ExplorerProfile? {
        return profileDao.getActiveProfile()?.toDomain()
    }

    override suspend fun hasActiveProfile(): Boolean {
        return profileDao.countActiveProfiles() > 0
    }

    override suspend fun createProfile(name: String): ExplorerProfile {
        val validation = validateName(name)
        if (validation is ProfileValidationResult.Invalid) {
            throw InvalidProfileException(validation.reason)
        }

        val trimmedName = name.trim()
        val now = System.currentTimeMillis()
        val profile = ExplorerProfile(
            profileId = UUID.randomUUID().toString(),
            explorerName = trimmedName,
            createdAt = now,
            lastOpenedAt = now,
            isActive = true
        )

        val entity = ProfileEntity.fromDomain(profile)
        profileDao.setActiveProfile(entity)
        return profile
    }

    override suspend fun updateLastOpened(profileId: String) {
        val now = System.currentTimeMillis()
        profileDao.updateLastOpened(profileId, now)
    }

    override fun validateName(name: String): ProfileValidationResult {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return ProfileValidationResult.Invalid("El nombre de explorador no puede estar vacío.")
        }
        if (trimmed.length < 2) {
            return ProfileValidationResult.Invalid("El nombre debe tener al menos 2 caracteres.")
        }
        if (trimmed.length > 30) {
            return ProfileValidationResult.Invalid("El nombre no puede superar los 30 caracteres.")
        }
        return ProfileValidationResult.Valid
    }

    override suspend fun getLoreEditsUsed(): Int {
        return profileDao.getActiveProfileLoreEditsUsed() ?: 0
    }

    override suspend fun getRemainingLoreEdits(): Int {
        val used = getLoreEditsUsed()
        return (LoreConstants.MAX_EDITS_PER_ACCOUNT - used).coerceAtLeast(0)
    }

    override suspend fun canEditLore(): Boolean {
        return getRemainingLoreEdits() > 0
    }

    override suspend fun bootstrapAdminIfNeeded() {
        if (profileDao.countProfiles() == 0) {
            val now = System.currentTimeMillis()
            val adminProfile = ExplorerProfile(
                profileId = UUID.randomUUID().toString(),
                explorerName = "admin",
                createdAt = now,
                lastOpenedAt = now,
                isActive = false, // Must login manually
                passwordHash = AuthUtil.hash("1234")
            )
            profileDao.insertProfile(ProfileEntity.fromDomain(adminProfile))
        }
    }

    override suspend fun authenticateOrCreateProfile(name: String, password: String?): ExplorerProfile {
        val existingEntity = profileDao.getProfileByName(name.trim())
        if (existingEntity != null) {
            // Authenticación
            if (existingEntity.passwordHash != null) {
                if (password == null || AuthUtil.hash(password) != existingEntity.passwordHash) {
                    throw InvalidProfileException("Contraseña incorrecta.")
                }
            }
            // Activar el perfil existente
            val activeEntity = existingEntity.copy(isActive = true)
            profileDao.setActiveProfile(activeEntity)
            return activeEntity.toDomain()
        } else {
            // Creación normal (si no existe, lo crea. Si escribió contraseña, se la guardamos)
            val validation = validateName(name)
            if (validation is ProfileValidationResult.Invalid) {
                throw InvalidProfileException(validation.reason)
            }
            val trimmedName = name.trim()
            val now = System.currentTimeMillis()
            val profile = ExplorerProfile(
                profileId = UUID.randomUUID().toString(),
                explorerName = trimmedName,
                createdAt = now,
                lastOpenedAt = now,
                isActive = true,
                passwordHash = password?.let { AuthUtil.hash(it) }
            )
            profileDao.setActiveProfile(ProfileEntity.fromDomain(profile))
            return profile
        }
    }

    override suspend fun consumeLoreEdit(): Boolean {
        val active = profileDao.getActiveProfile() ?: return false
        if (active.loreEditsUsed >= LoreConstants.MAX_EDITS_PER_ACCOUNT) {
            return false
        }
        val updatedRows = profileDao.incrementLoreEdits(active.profileId)
        return updatedRows > 0
    }
}
