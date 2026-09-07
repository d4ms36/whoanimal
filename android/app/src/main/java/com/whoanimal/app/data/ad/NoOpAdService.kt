package com.whoanimal.app.data.ad

import com.whoanimal.app.domain.ad.AdPlacement
import com.whoanimal.app.domain.ad.AdService
import com.whoanimal.app.domain.ad.AdShowResult
import com.whoanimal.app.domain.ad.FrequencyPolicy
import com.whoanimal.app.domain.ad.RewardResult

/**
 * Implementación no operativa (No-Op) de [AdService].
 *
 * Utilizada como salvaguarda fail-safe:
 * Si ningún proveedor está disponible, el sistema cae silenciosamente a esta implementación,
 * garantizando que el Core Loop continúe ejecutándose sin crashes, sin bloqueos y sin interrupciones.
 */
class NoOpAdService : AdService {

    private val disabledPolicy = FrequencyPolicy(isEnabled = false)

    override fun isBannerAvailable(placement: AdPlacement): Boolean = false

    override fun isInterstitialAvailable(placement: AdPlacement): Boolean = false

    override fun isRewardedAvailable(placement: AdPlacement): Boolean = false

    override fun showInterstitial(
        placement: AdPlacement,
        onClosed: () -> Unit
    ): AdShowResult {
        onClosed()
        return AdShowResult.NotAvailable
    }

    override fun showRewarded(
        placement: AdPlacement,
        onResult: (RewardResult) -> Unit
    ) {
        onResult(RewardResult.Unavailable)
    }

    override fun getFrequencyPolicy(): FrequencyPolicy = disabledPolicy

    override fun isAdsEnabled(): Boolean = false

    override fun setAdsEnabled(enabled: Boolean) {
        // No-op: siempre deshabilitado
    }
}
