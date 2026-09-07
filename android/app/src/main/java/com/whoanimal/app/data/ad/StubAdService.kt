package com.whoanimal.app.data.ad

import com.whoanimal.app.domain.ad.AdPlacement
import com.whoanimal.app.domain.ad.AdService
import com.whoanimal.app.domain.ad.AdShowResult
import com.whoanimal.app.domain.ad.FrequencyPolicy
import com.whoanimal.app.domain.ad.RewardResult

/**
 * Implementación Stub/Fake determinista de [AdService] para desarrollo, testing y modo offline.
 *
 * Cumple con todas las restricciones éticas y técnicas de WHO Animal:
 * - 100% offline (sin llamadas de red ni dependencias externas).
 * - Protección absoluta de zonas críticas (CAPTURE, IDENTIFICATION, etc.).
 * - Configurable para simular disponibilidad, rechazo, fallo o cancelación.
 */
class StubAdService(
    private val frequencyPolicy: FrequencyPolicy = FrequencyPolicy()
) : AdService {

    var simulateBannerAvailable: Boolean = true
    var simulateInterstitialAvailable: Boolean = true
    var simulateRewardedAvailable: Boolean = true
    var simulateRewardGranted: Boolean = true
    var simulateFailure: Boolean = false
    var defaultRewardType: String = "lore_edit_bonus"

    private var adsGloballyEnabled: Boolean = true

    override fun isBannerAvailable(placement: AdPlacement): Boolean {
        if (!adsGloballyEnabled) return false
        if (placement.isProtected) return false
        if (simulateFailure) return false
        return simulateBannerAvailable
    }

    override fun isInterstitialAvailable(placement: AdPlacement): Boolean {
        if (!adsGloballyEnabled) return false
        if (placement.isProtected) return false
        if (simulateFailure) return false
        if (!simulateInterstitialAvailable) return false

        return frequencyPolicy.canShowInterstitial(placement, System.currentTimeMillis())
    }

    override fun isRewardedAvailable(placement: AdPlacement): Boolean {
        if (!adsGloballyEnabled) return false
        if (placement.isProtected) return false
        if (simulateFailure) return false
        return simulateRewardedAvailable
    }

    override fun showInterstitial(
        placement: AdPlacement,
        onClosed: () -> Unit
    ): AdShowResult {
        if (!adsGloballyEnabled) {
            onClosed()
            return AdShowResult.PolicyBlocked("Ads are globally disabled")
        }

        if (placement.isProtected) {
            onClosed()
            return AdShowResult.PolicyBlocked("Placement '${placement.placementName}' is a protected zone")
        }

        if (simulateFailure) {
            onClosed()
            return AdShowResult.Failed("Simulated interstitial display failure")
        }

        val now = System.currentTimeMillis()
        if (!frequencyPolicy.canShowInterstitial(placement, now)) {
            onClosed()
            return AdShowResult.PolicyBlocked("Frequency policy blocked interstitial")
        }

        if (!simulateInterstitialAvailable) {
            onClosed()
            return AdShowResult.NotAvailable
        }

        // Registrar impresión en la política de frecuencia
        frequencyPolicy.recordImpression(placement, now)
        onClosed()
        return AdShowResult.Success
    }

    override fun showRewarded(
        placement: AdPlacement,
        onResult: (RewardResult) -> Unit
    ) {
        if (!adsGloballyEnabled) {
            onResult(RewardResult.PolicyBlocked("Ads are globally disabled"))
            return
        }

        if (placement.isProtected) {
            onResult(RewardResult.PolicyBlocked("Placement '${placement.placementName}' is a protected zone"))
            return
        }

        if (simulateFailure) {
            onResult(RewardResult.Failed("Simulated rewarded ad failure"))
            return
        }

        if (!simulateRewardedAvailable) {
            onResult(RewardResult.Unavailable)
            return
        }

        if (simulateRewardGranted) {
            onResult(RewardResult.Granted(rewardType = defaultRewardType, amount = 1))
        } else {
            onResult(RewardResult.DismissedWithoutReward)
        }
    }

    override fun getFrequencyPolicy(): FrequencyPolicy = frequencyPolicy

    override fun isAdsEnabled(): Boolean = adsGloballyEnabled

    override fun setAdsEnabled(enabled: Boolean) {
        adsGloballyEnabled = enabled
        frequencyPolicy.isEnabled = enabled
    }
}
