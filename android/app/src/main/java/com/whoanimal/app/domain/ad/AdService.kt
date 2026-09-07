package com.whoanimal.app.domain.ad

/**
 * Contrato de dominio desacoplado para servicios publicitarios en WHO Animal.
 *
 * Principio rector:
 * Si AdService no está disponible, falla o se desactiva, WHO Animal debe continuar
 * operando con total normalidad. El Core Loop jamás debe importar clases de un SDK de publicidad.
 */
interface AdService {

    /**
     * Consulta si un banner publicitario está disponible para el placement solicitado.
     * Retorna false inmediatamente si el placement es una zona protegida.
     */
    fun isBannerAvailable(placement: AdPlacement): Boolean

    /**
     * Consulta si un anuncio Interstitial está disponible y cumple la política de frecuencia.
     * Retorna false inmediatamente si el placement es una zona protegida.
     */
    fun isInterstitialAvailable(placement: AdPlacement): Boolean

    /**
     * Consulta si un anuncio bonificado (Rewarded) está disponible para exhibición opt-in.
     */
    fun isRewardedAvailable(placement: AdPlacement): Boolean

    /**
     * Solicita mostrar un anuncio Interstitial.
     * Ejecuta [onClosed] al finalizar o descartar, y devuelve el resultado detallado.
     */
    fun showInterstitial(
        placement: AdPlacement,
        onClosed: () -> Unit = {}
    ): AdShowResult

    /**
     * Solicita mostrar un anuncio bonificado (Rewarded).
     * Comunica el resultado explícito ([RewardResult.Granted], [RewardResult.DismissedWithoutReward], etc.)
     * a través del callback [onResult].
     */
    fun showRewarded(
        placement: AdPlacement,
        onResult: (RewardResult) -> Unit
    )

    /**
     * Proporciona acceso a la política de frecuencia activa.
     */
    fun getFrequencyPolicy(): FrequencyPolicy

    /**
     * Indica si la publicidad está globalmente habilitada en el servicio.
     */
    fun isAdsEnabled(): Boolean

    /**
     * Activa o desactiva globalmente la exhibición de anuncios.
     */
    fun setAdsEnabled(enabled: Boolean)
}
