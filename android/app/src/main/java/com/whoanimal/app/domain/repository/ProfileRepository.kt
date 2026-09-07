package com.whoanimal.app.domain.repository

import com.whoanimal.app.domain.model.ExplorerProfile

sealed class ProfileValidationResult {
    object Valid : ProfileValidationResult()
    data class Invalid(val reason: String) : ProfileValidationResult()
}

class InvalidProfileException(message: String) : IllegalArgumentException(message)

/**
 * Contrato del repositorio de perfiles locales para Alpha.
 *
 * Desacopla la lógica de perfil/sesión local del motor de persistencia (Room/SQLite).
 */
interface ProfileRepository {
    /**
     * Obtiene el perfil actualmente activo en el dispositivo, o null si no existe.
     */
    suspend fun getActiveProfile(): ExplorerProfile?

    /**
     * Determina si existe una sesión/perfil activo en el almacenamiento local.
     */
    suspend fun hasActiveProfile(): Boolean

    /**
     * Crea y persiste un nuevo perfil local, estableciéndolo como el único activo.
     * Lanza [InvalidProfileException] si el nombre no supera las validaciones.
     */
    suspend fun createProfile(name: String): ExplorerProfile

    /**
     * Actualiza la marca de tiempo de última apertura para el perfil indicado.
     */
    suspend fun updateLastOpened(profileId: String)

    /**
     * Valida si un nombre de explorador cumple con los criterios mínimos de Alpha.
     */
    fun validateName(name: String): ProfileValidationResult

    /**
     * Devuelve el número de ediciones de Lore utilizadas por la cuenta activa (DEC-041).
     */
    suspend fun getLoreEditsUsed(): Int

    /**
     * Devuelve las ediciones de Lore restantes para la cuenta activa (máximo 3 según DEC-041).
     */
    suspend fun getRemainingLoreEdits(): Int

    /**
     * Determina si la cuenta activa aún puede editar el Lore de una carta.
     */
    suspend fun canEditLore(): Boolean

    /**
     * Consume una edición de Lore de la cuenta activa.
     * Devuelve true si la edición fue consumida con éxito, o false si se alcanzó el límite de 3.
     */
    suspend fun consumeLoreEdit(): Boolean
}

object LoreConstants {
    const val MAX_EDITS_PER_ACCOUNT = 3
    const val MAX_LORE_LENGTH = 300
}
