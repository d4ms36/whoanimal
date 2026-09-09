package com.whoanimal.app.ui.card

import androidx.compose.ui.graphics.Color

/**
 * WHO-003B-R2 — DESIGN LOCK: Card Rarity Material System
 *
 * Materiales físicos para el sistema de rareza de WHO Animal.
 * Cada rareza se siente como un material real: cobre, oro, obsidiana, etc.
 * Referencia: docs/TOKENS.md y docs/design-lab/styles.css
 *
 * NO modificar estos tokens sin aprobación del Design Lock de WHO-003B-R1.
 */

enum class SpecimenRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    PRISMA;

    companion object {
        fun fromString(value: String): SpecimenRarity = when (value.uppercase()) {
            "UNCOMMON"  -> UNCOMMON
            "RARE"      -> RARE
            "EPIC"      -> EPIC
            "LEGENDARY" -> LEGENDARY
            "PRISMA"    -> PRISMA
            else        -> COMMON
        }
    }
}

/**
 * Los tres colores (base, highlight, shadow) mapean directamente a los tokens CSS del laboratorio:
 *   --rarity-base, --rarity-highlight, --rarity-shadow
 */
data class RarityMaterial(
    val base: Color,
    val highlight: Color,
    val shadow: Color,
    val isPrisma: Boolean = false
)

object RarityMaterials {
    // Common (Titanio Mate)
    val COMMON = RarityMaterial(
        base      = Color(0xFF8c857b),
        highlight = Color(0xFFb0a99f),
        shadow    = Color(0xFF59534c)
    )
    // Uncommon (Verde Natural Tratado)
    val UNCOMMON = RarityMaterial(
        base      = Color(0xFF4a6b53),
        highlight = Color(0xFF6e9479),
        shadow    = Color(0xFF283d2e)
    )
    // Rare (Cobre Bruñido)
    val RARE = RarityMaterial(
        base      = Color(0xFFa65e3a),
        highlight = Color(0xFFd98a62),
        shadow    = Color(0xFF5c2f1a)
    )
    // Epic (Piedra Oscura / Obsidiana)
    val EPIC = RarityMaterial(
        base      = Color(0xFF36323b),
        highlight = Color(0xFF5b5563),
        shadow    = Color(0xFF18161a)
    )
    // Legendary (Oro Premium)
    val LEGENDARY = RarityMaterial(
        base      = Color(0xFFcca329),
        highlight = Color(0xFFffe680),
        shadow    = Color(0xFF664f00)
    )
    // Prisma (Iridiscente Sutil — tono neutro base, animación de brillo se añade encima)
    val PRISMA = RarityMaterial(
        base      = Color(0xFFa8a8a8),
        highlight = Color(0xFFffffff),
        shadow    = Color(0xFF555555),
        isPrisma  = true
    )

    fun forRarity(rarity: SpecimenRarity): RarityMaterial = when (rarity) {
        SpecimenRarity.COMMON    -> COMMON
        SpecimenRarity.UNCOMMON  -> UNCOMMON
        SpecimenRarity.RARE      -> RARE
        SpecimenRarity.EPIC      -> EPIC
        SpecimenRarity.LEGENDARY -> LEGENDARY
        SpecimenRarity.PRISMA    -> PRISMA
    }
}
