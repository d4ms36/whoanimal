package com.whoanimal.app.domain.model

/**
 * Modelo de dominio para el perfil de usuario/explorador local en Alpha.
 *
 * Contiene únicamente los atributos estrictamente requeridos para la sesión
 * local en dispositivo, sin contraseñas, credenciales remotas ni telemetría.
 */
data class ExplorerProfile(
    val profileId: String,
    val explorerName: String,
    val createdAt: Long,
    val lastOpenedAt: Long,
    val isActive: Boolean = true
)
