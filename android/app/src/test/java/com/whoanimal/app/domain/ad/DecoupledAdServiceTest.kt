package com.whoanimal.app.domain.ad

import com.whoanimal.app.data.ad.NoOpAdService
import com.whoanimal.app.data.ad.StubAdService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DecoupledAdServiceTest {

    private lateinit var stubAdService: StubAdService
    private lateinit var noOpAdService: NoOpAdService

    @Before
    fun setup() {
        stubAdService = StubAdService()
        noOpAdService = NoOpAdService()
    }

    @Test
    fun `isBannerAvailable returns false for PROTECTED zone`() {
        assertFalse(stubAdService.isBannerAvailable(AdPlacement.CAPTURE))
    }

    @Test
    fun `isBannerAvailable returns true for ALLOWED zone`() {
        assertTrue(stubAdService.isBannerAvailable(AdPlacement.HOME))
    }

    @Test
    fun `showInterstitial blocked by FrequencyPolicy`() {
        // First show succeeds
        val result1 = stubAdService.showInterstitial(AdPlacement.HOME)
        assertTrue(result1 is AdShowResult.Success)

        // Second show fails due to frequency cap
        val result2 = stubAdService.showInterstitial(AdPlacement.HOME)
        assertTrue(result2 is AdShowResult.PolicyBlocked)
    }

    @Test
    fun `showInterstitial success updates frequency policy timestamp`() {
        stubAdService.showInterstitial(AdPlacement.HOME)
        val timestamp = stubAdService.getFrequencyPolicy().getLastImpressionTimestamp()
        assertTrue(timestamp > 0)
    }

    @Test
    fun `showRewarded returns RewardResult Granted when configured`() {
        var result: RewardResult? = null
        stubAdService.simulateRewardGranted = true
        stubAdService.showRewarded(AdPlacement.REWARDS) { res ->
            result = res
        }
        assertTrue(result is RewardResult.Granted)
    }

    @Test
    fun `showRewarded returns DismissedWithoutReward when cancelled`() {
        var result: RewardResult? = null
        stubAdService.simulateRewardGranted = false
        stubAdService.showRewarded(AdPlacement.REWARDS) { res ->
            result = res
        }
        assertTrue(result is RewardResult.DismissedWithoutReward)
    }

    @Test
    fun `NoOpAdService always returns unavailable`() {
        assertFalse(noOpAdService.isBannerAvailable(AdPlacement.HOME))
        assertFalse(noOpAdService.isInterstitialAvailable(AdPlacement.HOME))
        assertFalse(noOpAdService.isRewardedAvailable(AdPlacement.HOME))
    }

    @Test
    fun `NoOpAdService blocks interstitial ad`() {
        var closed = false
        val result = noOpAdService.showInterstitial(AdPlacement.HOME) { closed = true }
        assertTrue(result is AdShowResult.NotAvailable)
        assertTrue(closed)
    }

    @Test
    fun `isAdsEnabled false completely blocks ads`() {
        stubAdService.setAdsEnabled(false)
        assertFalse(stubAdService.isBannerAvailable(AdPlacement.HOME))
        
        val result = stubAdService.showInterstitial(AdPlacement.HOME)
        assertTrue(result is AdShowResult.PolicyBlocked)
    }

    @Test
    fun `simulateFailure true simulates failure`() {
        stubAdService.simulateFailure = true
        assertFalse(stubAdService.isBannerAvailable(AdPlacement.HOME))
        
        val result = stubAdService.showInterstitial(AdPlacement.HOME)
        assertTrue(result is AdShowResult.Failed)
    }

    @Test
    fun `protected zone blocks rewarded ad`() {
        var result: RewardResult? = null
        stubAdService.showRewarded(AdPlacement.CAPTURE) { res ->
            result = res
        }
        assertTrue(result is RewardResult.PolicyBlocked)
    }

    @Test
    fun `protected zone blocks interstitial ad`() {
        val result = stubAdService.showInterstitial(AdPlacement.CAPTURE)
        assertTrue(result is AdShowResult.PolicyBlocked)
    }
}
