package com.whoanimal.app.ui.screens.card

import android.graphics.BitmapFactory
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.AnimalProfileContract
import com.whoanimal.app.ui.theme.AmberWarning
import com.whoanimal.app.ui.theme.ForestGreenPrimary
import com.whoanimal.app.ui.theme.SageAccent
import com.whoanimal.app.ui.theme.SoftCardBorder
import java.io.File

/**
 * WHO-018C — Card Presentation & Interactive Review.
 *
 * Presentación interactiva de la carta de animal con giro 3D (Front <-> Back).
 * Separa de manera estricta la información zoológica científica del Lore/observación personal.
 * Consume exclusivamente contratos inmutables existentes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPresentationScreen(
    card: AnimalCardContract?,
    profile: AnimalProfileContract? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSaveCard: (AnimalCardContract) -> Unit,
    onReleaseCard: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }
    var isProcessingAction by remember { mutableStateOf(false) }
    var showReleaseDialog by remember { mutableStateOf(false) }

    // Si no se proveyó profile explícito, se consulta en el catálogo oficial
    val effectiveProfile = profile ?: card?.let { OfficialStarterCatalog.findById(it.animalId) }

    // Animación 3D de rotación sobre el eje Y
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing),
        label = "Card3DFlipRotation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Inspección de Carta",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isFlipped) "Reverso • Ficha Científica & Lore" else "Anverso • Coleccionable",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { if (!isProcessingAction) onNavigateBack() },
                        enabled = !isProcessingAction
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isFlipped = !isFlipped },
                        modifier = Modifier.testTag("action_flip_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = "Voltear Carta",
                            tint = ForestGreenPrimary
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (card != null && errorMessage == null && !isLoading) {
                CardActionBar(
                    isFlipped = isFlipped,
                    isProcessing = isProcessingAction,
                    onToggleFlip = { isFlipped = !isFlipped },
                    onSave = {
                        if (!isProcessingAction) {
                            isProcessingAction = true
                            onSaveCard(card)
                        }
                    },
                    onOpenReleaseDialog = {
                        if (!isProcessingAction) {
                            showReleaseDialog = true
                        }
                    }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = ForestGreenPrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Generando presentación de carta...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                errorMessage != null || card == null -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .testTag("card_error_view")
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No se pudo cargar la carta",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = errorMessage ?: "Los datos de la carta no se encuentran disponibles.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onNavigateBack,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Regresar")
                            }
                        }
                    }
                }

                else -> {
                    // Contenedor principal de la carta 3D
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Hint sutil superior
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { isFlipped = !isFlipped }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraAndroid,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = ForestGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isFlipped) "Mostrando Reverso • Toca para volver al Anverso" else "Mostrando Anverso • Toca la carta para voltear",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = ForestGreenPrimary
                            )
                        }

                        // Caja de la carta con proyección 3D
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .graphicsLayer {
                                    rotationY = rotation
                                    cameraDistance = 14f * density
                                }
                                .testTag("card_flip_container")
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if (!isProcessingAction) {
                                        isFlipped = !isFlipped
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotation <= 90f) {
                                // Front Face
                                CardFrontFace(
                                    card = card,
                                    profile = effectiveProfile,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                // Back Face con compensación de espejo (180deg)
                                CardBackFace(
                                    card = card,
                                    profile = effectiveProfile,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer { rotationY = 180f }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para liberación ética de fauna
    if (showReleaseDialog) {
        AlertDialog(
            onDismissRequest = { showReleaseDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.NaturePeople,
                    contentDescription = null,
                    tint = ForestGreenPrimary
                )
            },
            title = { Text("¿Liberar captura de fauna?") },
            text = {
                Text(
                    "Esta acción descartará el registro sin almacenarlo en tu baúl de colección. El ejemplar continuará libre en su ecosistema."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showReleaseDialog = false
                        isProcessingAction = true
                        onReleaseCard()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Sí, Liberar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReleaseDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * ANVERSO DE LA CARTA (CARD FRONT)
 *
 * Jerarquía visual:
 * 1. Animal (fotografía real o arte zoológico).
 * 2. Identidad (nombre común y científico).
 * 3. Estética coleccionable (rareza, edición, serial, rango).
 * 4. Metadatos secundarios.
 */
@Composable
fun CardFrontFace(
    card: AnimalCardContract,
    profile: AnimalProfileContract?,
    modifier: Modifier = Modifier
) {
    val commonName = profile?.commonName ?: "Especie Desconocida"
    val scientificName = profile?.scientificName ?: "Incertae sedis"
    val taxonomy = profile?.taxonomy
    val taxonomyBreadcrumb = listOfNotNull(
        taxonomy?.className,
        taxonomy?.order,
        taxonomy?.family
    ).joinToString(" • ")

    val isRare = card.rarity.equals("RARE", ignoreCase = true) || (profile?.isRareSpecies == true)

    Card(
        modifier = modifier
            .border(
                width = if (isRare) 2.dp else 1.dp,
                brush = if (isRare) Brush.linearGradient(listOf(SageAccent, ForestGreenPrimary))
                else Brush.linearGradient(listOf(SoftCardBorder, SoftCardBorder)),
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("card_front_face"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Specimen number, Rarity capsule, Edition
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ForestGreenPrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "#${card.specimenNumber.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = ForestGreenPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isRare) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SageAccent.copy(alpha = 0.2f),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = ForestGreenPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "RARA",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = ForestGreenPrimary
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "COMÚN",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    card.edition?.let { ed ->
                        Text(
                            text = ed,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Artwork / Imagen principal del animal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                SageAccent.copy(alpha = 0.2f),
                                ForestGreenPrimary.copy(alpha = 0.08f)
                            )
                        )
                    )
                    .border(1.dp, SoftCardBorder, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                val localBitmap = remember(card.imagePath) {
                    card.imagePath?.let { path ->
                        val file = File(path)
                        if (file.exists() && file.length() > 0) {
                            try {
                                BitmapFactory.decodeFile(path)?.asImageBitmap()
                            } catch (_: Exception) {
                                null
                            }
                        } else null
                    }
                }

                if (localBitmap != null) {
                    Image(
                        bitmap = localBitmap,
                        contentDescription = commonName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Placeholder visual elegante y temático de fauna
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(SageAccent.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pets,
                                contentDescription = null,
                                tint = ForestGreenPrimary,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Expedición Zoológica",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ForestGreenPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Identidad biológica
            Column(modifier = Modifier.fillMaxWidth()) {
                if (taxonomyBreadcrumb.isNotBlank()) {
                    Text(
                        text = taxonomyBreadcrumb,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = ForestGreenPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Text(
                    text = commonName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = scientificName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = SoftCardBorder)
            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Serial de colección visual & Rango
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = card.serial,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 10.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Rango ${card.rank}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * REVERSO DE LA CARTA (CARD BACK)
 *
 * Separación estricta de dos pilares conceptuales:
 * A. INFORMACIÓN CIENTÍFICA (Factual, biológica, taxonómica, educativa).
 * B. OBSERVACIÓN PERSONAL & LORE (Avistamiento, fecha, ubicación generalizada y narrativa).
 */
@Composable
fun CardBackFace(
    card: AnimalCardContract,
    profile: AnimalProfileContract?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, SoftCardBorder, RoundedCornerShape(20.dp))
            .testTag("card_back_face"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header del Reverso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = null,
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FICHA BIOLÓGICA",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = ForestGreenPrimary
                    )
                }

                Text(
                    text = "WHO Animal Core",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
            }

            // ==========================================
            // SECCIÓN A: INFORMACIÓN CIENTÍFICA FACTUAL
            // ==========================================
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ForestGreenPrimary.copy(alpha = 0.04f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.12f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "INFORMACIÓN CIENTÍFICA",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = ForestGreenPrimary
                    )

                    // Taxonomía formal
                    profile?.taxonomy?.let { tax ->
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            TaxonomyFactRow("Clase", tax.className)
                            TaxonomyFactRow("Orden", tax.order)
                            TaxonomyFactRow("Familia", tax.family)
                            TaxonomyFactRow("Género", tax.genus)
                            TaxonomyFactRow("Especie", tax.species)
                        }
                    }

                    // Estado de conservación
                    profile?.conservationStatus?.let { status ->
                        FactItemRow("Estado de conservación", mapConservationStatus(status))
                    }

                    // Hábitat
                    profile?.habitat?.let { habitat ->
                        FactItemRow("Hábitat", habitat)
                    }

                    // Dieta
                    profile?.diet?.let { diet ->
                        FactItemRow("Dieta", diet)
                    }

                    // Ciclo de actividad
                    profile?.activityCycle?.let { cycle ->
                        FactItemRow("Ciclo de actividad", cycle)
                    }

                    // Dimensiones y Longevidad si existen
                    val details = listOfNotNull(
                        profile?.lifespanYears?.let { "$it años aprox." },
                        profile?.sizeCm?.let { "$it cm" },
                        profile?.weightKg?.let { "$it kg" }
                    )
                    if (details.isNotEmpty()) {
                        FactItemRow("Biometría estimada", details.joinToString(" • "))
                    }

                    // Curiosidad zoológica
                    profile?.curiosity?.let { cur ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SageAccent.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = ForestGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = cur,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Advertencia preventiva responsable (nunca alarmista)
                    profile?.dangerLevel?.let { danger ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberWarning.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = AmberWarning,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = danger,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // ==========================================
            // SECCIÓN B: OBSERVACIÓN PERSONAL & LORE
            // ==========================================
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "OBSERVACIÓN PERSONAL",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FactItemRow("Fecha de registro", card.issuedAt.take(19).replace('T', ' '))

                    // Ubicación generalizada que protege fauna y privacidad
                    FactItemRow("Zona de avistamiento", card.displayLocation)

                    // Método de verificación y confianza
                    val confidenceText = card.identificationConfidence?.let {
                        "${(it * 100).toInt()}%"
                    } ?: "Sin calcular"
                    FactItemRow("Método", "${card.identificationMethod} ($confidenceText)")

                    // Lore narrativo debidamente separado y etiquetado
                    card.personalLore?.let { lore ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = SageAccent
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Lore de Expedición (Narrativa de juego • No es hecho científico)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 10.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lore,
                                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FactItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TaxonomyFactRow(rank: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = rank,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun mapConservationStatus(code: String): String {
    return when (code.uppercase()) {
        "LC" -> "Preocupación Menor (LC)"
        "NT" -> "Casi Amenazada (NT)"
        "VU" -> "Vulnerable (VU)"
        "EN" -> "En Peligro (EN)"
        "CR" -> "En Peligro Crítico (CR)"
        "EW" -> "Extinta en Estado Silvestre (EW)"
        "EX" -> "Extinta (EX)"
        "NE" -> "No Evaluada (NE)"
        else -> code
    }
}

/**
 * Barra de acciones inferior: Guardar en Baúl, Voltear Carta, Liberar Captura.
 */
@Composable
private fun CardActionBar(
    isFlipped: Boolean,
    isProcessing: Boolean,
    onToggleFlip: () -> Unit,
    onSave: () -> Unit,
    onOpenReleaseDialog: () -> Unit
) {
    Surface(
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Liberar (descartar captura)
            OutlinedButton(
                onClick = onOpenReleaseDialog,
                enabled = !isProcessing,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .weight(0.9f)
                    .testTag("action_release_button")
            ) {
                Icon(
                    imageVector = Icons.Default.NaturePeople,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Liberar", maxLines = 1)
            }

            // Botón Voltear (accesibilidad y control explícito)
            OutlinedButton(
                onClick = onToggleFlip,
                enabled = !isProcessing,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("action_flip_button")
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isFlipped) "Anverso" else "Reverso", maxLines = 1)
            }

            // Botón Guardar en Baúl (persistencia a colección)
            Button(
                onClick = onSave,
                enabled = !isProcessing,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                modifier = Modifier
                    .weight(1.3f)
                    .testTag("action_save_button")
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Guardando...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Guardar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
