package com.whoanimal.app.domain.ad

/**
 * Representa los puntos semánticos de exhibición (placements) dentro de WHO Animal.
 *
 * Conforme a PRODUCT_RULES.md (Regla 8) y PRODUCT_VISION.md:
 * - La publicidad está estrictamente prohibida en zonas críticas de la experiencia:
 *   cámara, identificación, revelación de carta, revisión, lectura científica y combates.
 */
enum class AdPlacement(
    val placementName: String,
    val isProtected: Boolean
) {
    // =========================================================================
    // ZONAS PERMITIDAS (Secondary & Transition Surfaces)
    // =========================================================================
    HOME("home_screen", false),
    COLLECTION("collection_vault", false),
    SHOP("shop_cosmetics", false),
    PROFILE("profile_screen", false),
    REWARDS("rewards_hub", false),

    // =========================================================================
    // ZONAS PROTEGIDAS (Prohibición estricta de publicidad)
    // =========================================================================
    CAPTURE("capture_camera", true),
    IDENTIFICATION("identification_processing", true),
    CARD_REVEAL("card_reveal_animation", true),
    CARD_REVIEW("card_inspection", true),
    ENCYCLOPEDIA_READING("encyclopedia_reading", true),
    COMBAT_PVP("combat_pvp_duel", true);

    companion object {
        /**
         * Devuelve true si el placement indicado pertenece a las zonas protegidas libres de anuncios.
         */
        fun isZoneProtected(placement: AdPlacement): Boolean = placement.isProtected
    }
}
