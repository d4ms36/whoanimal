package com.whoanimal.app.ui.card

import android.graphics.BitmapFactory
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.AnimalProfileContract
import com.whoanimal.app.domain.model.TaxonomyContract
import com.whoanimal.app.ui.theme.WhoAnimalTheme
import java.io.File
import com.whoanimal.app.ui.theme.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.BorderStroke

// ─────────────────────────────────────────────────────────────────────────────
// WHO-003B-R2 — SpecimenCard: DESIGN LOCK IMPLEMENTATION
//
// Implementación fiel al Design Lock de WHO-003B-R1.
// NO modificar la apariencia sin aprobación del Director/PM.
//
// JERARQUÍA VISUAL (inmutable):
//  1. Animal (fotografía, ~60% del área)
//  2. Rareza / Valor (frame material)
//  3. Identidad de captura (nombre común — Serif)
//  4. Información científica (nombre científico — Monospace, max 2 líneas)
//  5. RPG Ligero (stats, rank hexagonal, skills dots)
//
// DESIGN LOCK:
//  - Explorer como único ADN visual base.
//  - Photo ratio: 60% (fijo, NO 75%).
//  - Hero Silhouette: condicional (sólo si foregroundBitmap != null).
//  - Thumbnail: simplificado (sin stats, sin científico, sin rank text).
//  - Prisma: holografía sutil y controlada.
// ─────────────────────────────────────────────────────────────────────────────

// ExplorerColors removed; using semantic color tokens defined in Color.kt

/**
 * SpecimenCard — Composable principal de la carta coleccionable de WHO Animal.
 *
 * @param card              Contrato de la carta (datos de colección y rareza).
 * @param profile           Perfil zoológico del espécimen (nombre, taxonomía, stats).
 * @param backgroundBitmap  Imagen principal del espécimen (puede ser null → placeholder).
 * @param foregroundBitmap  Alpha mask recortada del espécimen para Hero Silhouette.
 *                          Si es null → Standard Specimen (igualmente válido y premium).
 * @param isThumbnail       Si true, renderiza versión simplificada 96×134dp.
 * @param modifier          Modifier externo.
 */
@Composable
fun SpecimenCard(
    card: AnimalCardContract,
    profile: AnimalProfileContract?,
    backgroundBitmap: ImageBitmap? = null,
    foregroundBitmap: ImageBitmap? = null,
    isThumbnail: Boolean = false,
    modifier: Modifier = Modifier
) {
    val rarity   = SpecimenRarity.fromString(card.rarity)
    val material = RarityMaterials.forRarity(rarity)

    val commonName     = profile?.commonName ?: "Espécimen Desconocido"
    val scientificName = profile?.scientificName ?: "Incertae sedis"

    val frameCorner   = if (isThumbnail) 8.dp  else 16.dp
    val innerCorner   = if (isThumbnail) 5.dp  else 10.dp
    val framePadding  = if (isThumbnail) 4.dp  else 8.dp

    Box(modifier = modifier.testTag(if (isThumbnail) "specimen_card_thumbnail" else "specimen_card_main")) {

        // ── OUTER FRAME (rarity material gradient) ──────────────────────────
        Surface(
            shape       = RoundedCornerShape(frameCorner),
            color       = Color.Transparent,
            shadowElevation = if (isThumbnail) 4.dp else 16.dp,
            modifier    = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (rarity == SpecimenRarity.LEGENDARY)
                            Brush.linearGradient(
                                colors  = listOf(material.highlight, material.base, material.shadow, material.highlight),
                                start   = Offset(0f, 0f),
                                end     = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        else
                            Brush.linearGradient(
                                colors = listOf(material.highlight, material.base, material.shadow),
                                start  = Offset(0f, 0f),
                                end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            ),
                        shape = RoundedCornerShape(frameCorner)
                    )
                    .padding(framePadding)
            ) {

                // ── INNER CARD (warm paper) ────────────────────────────────
                Surface(
                    shape  = RoundedCornerShape(innerCorner),
                    color  = Surface,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        // ── HERO PHOTO AREA (~60% weight) ─────────────────
                        Box(modifier = Modifier.weight(0.60f).fillMaxWidth()) {

                            // Background photo
                            if (backgroundBitmap != null) {
                                Image(
                                    bitmap       = backgroundBitmap,
                                    contentDescription = commonName,
                                    contentScale = ContentScale.Crop,
                                    modifier     = Modifier.fillMaxSize()
                                )
                            } else {
                                // Placeholder cuando no hay imagen
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFFD8D3CC)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector  = Icons.Default.Pets,
                                        contentDescription = null,
                                        tint         = Color(0xFF9E9890),
                                        modifier     = Modifier.size(if (isThumbnail) 24.dp else 56.dp)
                                    )
                                }
                            }

                            // Foreground silhouette (Hero Enhancement — condicional)
                            if (foregroundBitmap != null && !isThumbnail) {
                                Image(
                                    bitmap       = foregroundBitmap,
                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight,
                                    modifier     = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            translationY = -16.dp.toPx() // Rompe ligeramente el marco superior
                                            clip         = false
                                        }
                                )
                            }

                            if (!isThumbnail) {
                                // Explorer Mark — esquina superior izquierda
                                ExplorerMarkBadge(
                                    modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
                                )

                                // Rank Hexagon — esquina superior derecha
                                RankHexBadge(
                                    rank     = card.rank,
                                    material = material,
                                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
                                )
                            }

                            // Gradient fade at the bottom of the photo (bridge to info)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Surface)
                                        )
                                    )
                            )
                        }

                        // ── TRIANGLE SEPARATOR ────────────────────────────
                        if (!isThumbnail) {
                            Box(modifier = Modifier.fillMaxWidth().height(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp, 10.dp)
                                        .align(Alignment.Center)
                                        .clip(TriangleShape)
                                        .background(
                                            Surface
                                        )
                                )
                            }
                        }

                        // ── INFO PANEL (40% weight) ────────────────────────
                        Column(
                            modifier = Modifier
                                .weight(0.40f)
                                .fillMaxWidth()
                                .padding(
                                    horizontal = if (isThumbnail) 6.dp else 16.dp,
                                    vertical   = if (isThumbnail) 4.dp else 12.dp
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Common name — Serif, large, dominant
                                Text(
                                    text      = commonName,
                                    fontFamily= FontFamily.Serif,
                                    fontWeight= FontWeight.Bold,
                                    fontSize  = if (isThumbnail) 10.sp else 22.sp,
                                    color     = TextPrimary,
                                    textAlign = TextAlign.Center,
                                    maxLines  = if (isThumbnail) 2 else 1,
                                    overflow  = TextOverflow.Ellipsis,
                                    lineHeight= if (isThumbnail) 12.sp else 26.sp,
                                    modifier  = Modifier.testTag("card_common_name")
                                )

                                if (!isThumbnail) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    // Scientific name — Monospace, max 2 líneas (Design Lock)
                                    Text(
                                        text      = scientificName,
                                        fontFamily= FontFamily.Monospace,
                                        fontWeight= FontWeight.Normal,
                                        fontSize  = 11.sp,
                                        lineHeight= 15.sp,
                                        color     = TextSecondary,
                                        textAlign = TextAlign.Center,
                                        maxLines  = 2,
                                        overflow  = TextOverflow.Ellipsis,
                                        letterSpacing = 0.5.sp,
                                        modifier  = Modifier.testTag("card_scientific_name")
                                    )
                                }
                            }

                            // Stats biológicos (ocultos en thumbnail)
                            if (!isThumbnail && profile != null) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    HorizontalDivider(
                                        color    = Border,
                                        thickness= 1.dp,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                    AnatomicalStatsRow(profile = profile)
                                    HorizontalDivider(
                                        color    = Border,
                                        thickness= 1.dp,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }
                            }

                            // RPG Skills (ocultos en thumbnail)
                            if (!isThumbnail) {
                                SkillDotsRow(
                                    rarity   = rarity,
                                    material = material
                                )
                            }
                        }
                    }
                }
            }

            // Rarity thumbnail mini-indicator (top-right corner on thumbnail)
            if (isThumbnail) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .background(color = material.base, shape = CircleShape)
                )
            }
        }

        // ── PRISMA HOLOGRAPHIC OVERLAY (controlado y sutil) ─────────────────
        if (rarity == SpecimenRarity.PRISMA && !isThumbnail) {
            PrismaShineOverlay(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SUB-COMPONENTES INTERNOS
// ─────────────────────────────────────────────────────────────────────────────

/** Explorer Mark: círculo con símbolo de brújula en esquina superior izquierda. */
@Composable
private fun ExplorerMarkBadge(modifier: Modifier = Modifier) {
    Surface(
        shape      = CircleShape,
        color      = if (isSystemInDarkTheme()) ExplorerDarkMark else ExplorerMark,
        border = BorderStroke(1.dp, Border),
        modifier   = modifier.size(30.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text      = "✵",
                fontSize  = 16.sp,
                color     = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

/** Rank Hexagonal: indicador de rango con gradiente de rareza. */
@Composable
private fun RankHexBadge(
    rank: Int,
    material: RarityMaterial,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 34.dp, height = 38.dp)
            .clip(HexagonShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(material.highlight, material.base)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = rank.coerceIn(1, 99).toString(),
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Black,
            fontSize   = 14.sp,
            color      = Color.White,
            modifier   = Modifier.testTag("card_rank_badge")
        )
    }
}

/** Fila de estadísticas anatómicas (Peso, Longitud, Hábitat). */
@Composable
private fun AnatomicalStatsRow(
    profile: AnimalProfileContract,
    modifier: Modifier = Modifier
) {
    Row(
        modifier            = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        profile.weightKg?.let { w ->
            StatItem(
                icon  = "W",
                value = if (w >= 1000) "${(w / 1000).toInt()}t" else "${w.toInt()}kg"
            )
        }
        profile.sizeCm?.let { s ->
            StatItem(
                icon  = "L",
                value = if (s >= 100) "${s / 100}m" else "${s}cm"
            )
        }
        profile.habitat?.let { h ->
            StatItem(
                icon  = "H",
                value = h.take(10)
            )
        }
        // Fallback si no hay stats para no dejar la fila vacía
        if (profile.weightKg == null && profile.sizeCm == null && profile.habitat == null) {
            StatItem(icon = "SP", value = "#${profile.animalId.take(4).uppercase()}")
        }
    }
}

@Composable
private fun StatItem(icon: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text       = icon,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize   = 9.sp,
            color      = TextSecondary,
            letterSpacing = 0.5.sp
        )
        Text(
            text       = value,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize   = 12.sp,
            color      = TextPrimary
        )
    }
}

/** Skills dots: indicadores RPG mínimos (3 puntos con color de rareza). */
@Composable
private fun SkillDotsRow(
    rarity: SpecimenRarity,
    material: RarityMaterial,
    modifier: Modifier = Modifier
) {
    val dotCount = when (rarity) {
        SpecimenRarity.COMMON    -> 1
        SpecimenRarity.UNCOMMON  -> 2
        SpecimenRarity.RARE      -> 3
        SpecimenRarity.EPIC      -> 4
        SpecimenRarity.LEGENDARY -> 5
        SpecimenRarity.PRISMA    -> 6
    }
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        repeat(dotCount) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(color = material.base, shape = CircleShape)
            )
        }
    }
}

/** Prisma shine overlay: brillo holográfico sutil animado (Design Lock: controlado). */
@Composable
private fun PrismaShineOverlay(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "PrismaShine")
    val offset by transition.animateFloat(
        initialValue   = -1f,
        targetValue    = 2f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PrismaShineOffset"
    )

    Box(
        modifier = modifier
            .graphicsLayer { alpha = 0.25f }     // muy sutil — Design Lock
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xAAFFFFFF),
                        Color.Transparent
                    ),
                    start = Offset(offset * 400f - 200f, 0f),
                    end   = Offset(offset * 400f + 100f, Float.POSITIVE_INFINITY)
                )
            )
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// GEOMETRIC SHAPES
// ─────────────────────────────────────────────────────────────────────────────

/** Hexagon clip-path para el Rank badge. */
private val HexagonShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    moveTo(w * 0.5f, 0f)
    lineTo(w,        h * 0.25f)
    lineTo(w,        h * 0.75f)
    lineTo(w * 0.5f, h)
    lineTo(0f,       h * 0.75f)
    lineTo(0f,       h * 0.25f)
    close()
}

/** Triangle para el separador entre foto e info panel. */
private val TriangleShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, size.height)
    lineTo(0f,              0f)
    lineTo(size.width,      0f)
    close()
}

// ─────────────────────────────────────────────────────────────────────────────
// PREVIEWS — Design Lock Validation (WHO-003B-R2)
// ─────────────────────────────────────────────────────────────────────────────

private fun mockCard(rarity: String = "COMMON", rank: Int = 1) = AnimalCardContract(
    cardId      = "card-001",
    animalId    = "wolf-001",
    specimenNumber = 42,
    rarity      = rarity,
    captureId   = "cap-001",
    serial      = "WHO-SER-001",
    issuedAt    = "2026-09-08",
    populationAtIssuance = 1200,
    rank        = rank
)

private fun mockProfile(
    commonName: String     = "Lobo Gris",
    scientificName: String = "Canis lupus"
) = AnimalProfileContract(
    animalId       = "wolf-001",
    scientificName = scientificName,
    commonName     = commonName,
    taxonomy       = TaxonomyContract(
        phylum    = "Chordata",
        className = "Mammalia",
        order     = "Carnivora",
        family    = "Canidae",
        genus     = "Canis",
        species   = "Canis lupus"
    ),
    habitat     = "Bosque",
    weightKg    = 40.0,
    sizeCm      = 120
)

@Preview(name = "Standard — Common — Corto", widthDp = 300, heightDp = 430)
@Composable
private fun PreviewCommonShort() {
    WhoAnimalTheme {
        SpecimenCard(card = mockCard("COMMON"), profile = mockProfile())
    }
}

@Preview(name = "Standard — Rare — Científico Largo", widthDp = 300, heightDp = 430)
@Composable
private fun PreviewRareLongScientific() {
    WhoAnimalTheme {
        SpecimenCard(
            card    = mockCard("RARE", rank = 5),
            profile = mockProfile(
                commonName     = "Lobo Gris Occidental",
                scientificName = "Canis lupus familiaris var. extraordinarius"
            )
        )
    }
}

@Preview(name = "Epic", widthDp = 300, heightDp = 430)
@Composable
private fun PreviewEpic() {
    WhoAnimalTheme {
        SpecimenCard(card = mockCard("EPIC", rank = 12), profile = mockProfile("Águila Real", "Aquila chrysaetos"))
    }
}

@Preview(name = "Legendary", widthDp = 300, heightDp = 430)
@Composable
private fun PreviewLegendary() {
    WhoAnimalTheme {
        SpecimenCard(card = mockCard("LEGENDARY", rank = 25), profile = mockProfile("Oso Pardo", "Ursus arctos horribilis"))
    }
}

@Preview(name = "Prisma", widthDp = 300, heightDp = 430)
@Composable
private fun PreviewPrisma() {
    WhoAnimalTheme {
        SpecimenCard(card = mockCard("PRISMA", rank = 50), profile = mockProfile("Zorro Ártico", "Vulpes lagopus"))
    }
}

@Preview(name = "Thumbnail — Common", widthDp = 96, heightDp = 134)
@Composable
private fun PreviewThumbnailCommon() {
    WhoAnimalTheme {
        SpecimenCard(card = mockCard("COMMON"), profile = mockProfile(), isThumbnail = true)
    }
}

@Preview(name = "Thumbnail — Legendary", widthDp = 96, heightDp = 134)
@Composable
private fun PreviewThumbnailLegendary() {
    WhoAnimalTheme {
        SpecimenCard(card = mockCard("LEGENDARY", rank = 25), profile = mockProfile(), isThumbnail = true)
    }
}

@Preview(name = "Sin imagen — Uncommon", widthDp = 300, heightDp = 430)
@Composable
private fun PreviewNoImage() {
    WhoAnimalTheme {
        SpecimenCard(
            card              = mockCard("UNCOMMON"),
            profile           = mockProfile(),
            backgroundBitmap  = null,
            foregroundBitmap  = null
        )
    }
}
