package com.whoanimal.app.domain.ad

/**
 * Resultado de una solicitud para mostrar un anuncio Interstitial.
 */
sealed interface AdShowResult {
    data object Success : AdShowResult
    data object Dismissed : AdShowResult
    data object NotAvailable : AdShowResult
    data class PolicyBlocked(val reason: String) : AdShowResult
    data class Failed(val reason: String) : AdShowResult

    val isSuccessful: Boolean
        get() = this is Success || this is Dismissed
}

/**
 * Resultado de la visualización de un anuncio bonificado (Rewarded Ad).
 *
 * Principio ético: Existe una separación explícita entre "ad viewed" y "reward granted".
 * La recompensa nunca debe asumirse simplemente porque se solicitó un anuncio.
 */
sealed interface RewardResult {
    /**
     * Recompensa otorgada de forma verificada y completa.
     */
    data class Granted(
        val rewardType: String,
        val amount: Int = 1
    ) : RewardResult

    /**
     * El usuario canceló o cerró el anuncio antes de completarlo; no corresponde recompensa.
     */
    data object DismissedWithoutReward : RewardResult

    /**
     * No hay anuncio disponible en este momento.
     */
    data object Unavailable : RewardResult

    /**
     * Solicitud bloqueada por políticas de frecuencia, zona protegida o límites de sesión.
     */
    data class PolicyBlocked(val reason: String) : RewardResult

    /**
     * Fallo técnico en la carga o reproducción.
     */
    data class Failed(val reason: String) : RewardResult

    val isRewardGranted: Boolean
        get() = this is Granted
}

/**
 * Estado de exhibición de un Banner publicitario.
 */
sealed interface BannerState {
    data object Visible : BannerState
    data object Hidden : BannerState
    data object NotAvailable : BannerState
    data class Failed(val reason: String) : BannerState
}
