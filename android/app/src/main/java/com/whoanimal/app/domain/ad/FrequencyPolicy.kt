package com.whoanimal.app.domain.ad

/**
 * Política desacoplada de frecuencia para anuncios intersticiales.
 *
 * Reglas clave:
 * 1. Zonas protegidas nunca permiten intersticiales (bloqueo categórico).
 * 2. Respeta un período de enfriamiento (cooldown) entre impresiones sucesivas.
 * 3. Limita el número máximo de impresiones por sesión.
 * 4. Permite desactivación global (`isEnabled = false`).
 */
data class FrequencyPolicy(
    val cooldownMillis: Long = 60_000L,
    val maxImpressionsPerSession: Int = 5,
    var isEnabled: Boolean = true
) {
    private var lastImpressionTimestamp: Long = 0L
    private var sessionImpressionCount: Int = 0

    /**
     * Evalúa si es válido mostrar un anuncio intersticial en el placement y timestamp dados.
     */
    @Synchronized
    fun canShowInterstitial(placement: AdPlacement, currentTimeMillis: Long): Boolean {
        if (!isEnabled) return false
        if (placement.isProtected) return false
        if (sessionImpressionCount >= maxImpressionsPerSession) return false

        val elapsed = currentTimeMillis - lastImpressionTimestamp
        if (lastImpressionTimestamp > 0L && elapsed < cooldownMillis) {
            return false
        }

        return true
    }

    /**
     * Registra una impresión consumada.
     */
    @Synchronized
    fun recordImpression(placement: AdPlacement, currentTimeMillis: Long) {
        if (placement.isProtected) return
        lastImpressionTimestamp = currentTimeMillis
        sessionImpressionCount++
    }

    /**
     * Retorna el número de impresiones consumidas en la sesión actual.
     */
    @Synchronized
    fun getSessionImpressionCount(): Int = sessionImpressionCount

    /**
     * Retorna el timestamp de la última impresión consumada.
     */
    @Synchronized
    fun getLastImpressionTimestamp(): Long = lastImpressionTimestamp

    /**
     * Reinicia el contador de sesión y cooldown (útil para pruebas y reinicio de ciclo).
     */
    @Synchronized
    fun resetSession() {
        lastImpressionTimestamp = 0L
        sessionImpressionCount = 0
    }
}
