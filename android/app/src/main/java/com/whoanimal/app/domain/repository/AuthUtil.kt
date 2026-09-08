package com.whoanimal.app.domain.repository

import java.security.MessageDigest

/**
 * Utilidad básica de autenticación para Beta Bootstrap.
 *
 * NOTA: Esta implementación de hashing SHA-256 sin salt es el mínimo coherente
 * para no almacenar contraseñas en texto plano en la fase Beta.
 * NO es apto para un sistema de producción real con cuentas en la nube.
 */
object AuthUtil {
    fun hash(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
