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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import com.whoanimal.app.domain.repository.LoreConstants
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
import androidx.compose.ui.res.stringResource
import com.whoanimal.app.R
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.AnimalProfileContract
import java.io.File

enum class CardPresentationMode {
    NEW_CARD_REVIEW,
    PERSISTED_CARD
}

/**
 * WHO-018C & WHO-018E — Card Presentation & Interactive Review / Reopening.
 *
 * Presentación interactiva de la carta de animal con giro 3D (Front <-> Back).
 * Soporta dos modos operacionales:
 * - NEW_CARD_REVIEW: Muestra acciones de decisión (Guardar / Liberar) tras una captura.
 * - PERSISTED_CARD: Muestra inspección de carta reabierta desde el Baúl sin duplicar guardados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPresentationScreen(
    card: AnimalCardContract?,
    profile: AnimalProfileContract? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    mode: CardPresentationMode = CardPresentationMode.NEW_CARD_REVIEW,
    remainingLoreEdits: Int = 3,
    onSaveCard: (AnimalCardContract) -> Unit = {},
    onReleaseCard: () -> Unit = {},
    onUpdateLore: suspend (cardId: String, newLore: String) -> Boolean = { _, _ -> false },
    onReleasePersistedCard: suspend (cardId: String) -> Unit = {},
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var displayedCard by remember(card) { mutableStateOf(card) }
    var currentRemainingEdits by remember(remainingLoreEdits) { mutableIntStateOf(remainingLoreEdits) }
    var isFlipped by remember { mutableStateOf(false) }
    var isProcessingAction by remember { mutableStateOf(false) }
    var showReleaseDialog by remember { mutableStateOf(false) }
    var showPersistedReleaseDialog by remember { mutableStateOf(false) }
    var showLoreEditorDialog by remember { mutableStateOf(false) }
    var showLoreLimitNotice by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Si no se proveyó profile explícito, se consulta en el catálogo oficial
    val effectiveProfile = profile ?: displayedCard?.let { OfficialStarterCatalog.findById(it.animalId) }

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
                            text = if (mode == CardPresentationMode.PERSISTED_CARD) stringResource(R.string.card_mode_persisted_title) else stringResource(R.string.card_mode_review_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isFlipped) stringResource(R.string.card_mode_flipped_subtitle) else stringResource(R.string.card_mode_front_subtitle),
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
                            contentDescription = stringResource(R.string.action_back)
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
                            contentDescription = stringResource(R.string.card_flip_action_description),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (displayedCard != null && errorMessage == null && !isLoading) {
                if (mode == CardPresentationMode.NEW_CARD_REVIEW) {
                    CardActionBar(
                        isFlipped = isFlipped,
                        isProcessing = isProcessingAction,
                        onToggleFlip = { isFlipped = !isFlipped },
                        onSave = {
                            if (!isProcessingAction) {
                                isProcessingAction = true
                                onSaveCard(displayedCard!!)
                            }
                        },
                        onOpenReleaseDialog = {
                            if (!isProcessingAction) {
                                showReleaseDialog = true
                            }
                        }
                    )
                } else {
                    PersistedCardActionBar(
                        isFlipped = isFlipped,
                        remainingEdits = currentRemainingEdits,
                        onToggleFlip = { isFlipped = !isFlipped },
                        onOpenLoreEditor = {
                            if (currentRemainingEdits <= 0) {
                                showLoreLimitNotice = true
                            } else {
                                showLoreEditorDialog = true
                            }
                        },
                        onOpenReleaseDialog = {
                            showPersistedReleaseDialog = true
                        },
                        onNavigateBack = onNavigateBack
                    )
                }
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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.card_generating),
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
                                text = stringResource(R.string.card_load_error_title),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = errorMessage ?: stringResource(R.string.card_load_error_fallback),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onNavigateBack,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text(stringResource(R.string.action_return))
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
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isFlipped) stringResource(R.string.card_hint_reverso) else stringResource(R.string.card_hint_anverso),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        val flipContentDescription = if (isFlipped) {
                            stringResource(R.string.card_flip_hint_back, effectiveProfile?.commonName.orEmpty())
                        } else {
                            stringResource(R.string.card_flip_hint_front, effectiveProfile?.commonName.orEmpty())
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
                                .semantics {
                                    role = Role.Button
                                    contentDescription = flipContentDescription
                                }
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
                                    card = displayedCard!!,
                                    profile = effectiveProfile,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                // Back Face con compensación de espejo (180deg)
                                CardBackFace(
                                    card = displayedCard!!,
                                    profile = effectiveProfile,
                                    mode = mode,
                                    onOpenLoreEditor = {
                                        if (currentRemainingEdits <= 0) {
                                            showLoreLimitNotice = true
                                        } else {
                                            showLoreEditorDialog = true
                                        }
                                    },
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

    // Diálogo de confirmación para liberación ética de fauna (NEW_CARD_REVIEW)
    if (showReleaseDialog) {
        AlertDialog(
            onDismissRequest = { showReleaseDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.NaturePeople,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text(stringResource(R.string.card_release_dialog_title)) },
            text = {
                Text(stringResource(R.string.card_release_dialog_body))
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
                    Text(stringResource(R.string.card_release_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { showReleaseDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }

    // Diálogo de confirmación para liberación/eliminación de carta persistida (PERSISTED_CARD)
    if (showPersistedReleaseDialog) {
        AlertDialog(
            onDismissRequest = { showPersistedReleaseDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text(stringResource(R.string.card_persisted_release_dialog_title)) },
            text = { Text(stringResource(R.string.card_persisted_release_dialog_body)) },
            confirmButton = {
                Button(
                    onClick = {
                        showPersistedReleaseDialog = false
                        val cardId = displayedCard?.cardId ?: return@Button
                        coroutineScope.launch {
                            isProcessingAction = true
                            onReleasePersistedCard(cardId)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("confirm_release_persisted_button")
                ) {
                    Text(stringResource(R.string.card_action_release))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showPersistedReleaseDialog = false },
                    modifier = Modifier.testTag("cancel_release_persisted_button")
                ) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }

    // Diálogo modal de edición interactiva de Lore (DEC-041)
    if (showLoreEditorDialog && displayedCard != null) {
        LoreEditDialog(
            initialLore = displayedCard?.personalLore ?: "",
            remainingEdits = currentRemainingEdits,
            onDismiss = { showLoreEditorDialog = false },
            onSave = { newLore ->
                val trimmed = newLore.trim()
                val existing = (displayedCard?.personalLore ?: "").trim()
                if (trimmed == existing) {
                    // Sin cambios reales: no consume edición de cuota
                    showLoreEditorDialog = false
                } else {
                    coroutineScope.launch {
                        val cardId = displayedCard?.cardId ?: return@launch
                        val success = onUpdateLore(cardId, trimmed)
                        if (success) {
                            displayedCard = displayedCard?.copy(personalLore = trimmed)
                            currentRemainingEdits = (currentRemainingEdits - 1).coerceAtLeast(0)
                            showLoreEditorDialog = false
                        }
                    }
                }
            }
        )
    }

    // Aviso informativo cuando el límite de 3 ediciones ha sido alcanzado (DEC-041)
    if (showLoreLimitNotice) {
        AlertDialog(
            onDismissRequest = { showLoreLimitNotice = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text(stringResource(R.string.lore_limit_reached)) },
            text = { Text(stringResource(R.string.lore_limit_reached_message)) },
            confirmButton = {
                TextButton(onClick = { showLoreLimitNotice = false }) {
                    Text(stringResource(R.string.action_return))
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
    val commonName = profile?.commonName ?: stringResource(R.string.unknown_species)
    val scientificName = profile?.scientificName ?: "Incertae sedis"
    val isRare = card.rarity.equals("RARE", ignoreCase = true) || (profile?.isRareSpecies == true)

    // Outer collectible frame
    Surface(
        modifier = modifier
            .testTag("card_front_face")
            .border(
                width = if (isRare) 3.dp else 2.dp,
                brush = if (isRare) Brush.linearGradient(listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.primary))
                        else androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 8.dp
    ) {
        // Inner card content (Passepartout / Paper)
        Surface(
            modifier = Modifier.padding(6.dp),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha=0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header (Number & Rarity)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Specimen Number Pill
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha=0.3f))
                    ) {
                        Text(
                            text = "S-${card.specimenNumber.toString().padStart(3, '0')}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Rarity Indicator
                    if (isRare) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha=0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.rarity_rare).uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Artwork Frame
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                    shadowElevation = 2.dp
                ) {
                    val localBitmap = remember(card.imagePath) {
                        card.imagePath?.let { path ->
                            val file = File(path)
                            if (file.exists() && file.length() > 0) {
                                try {
                                    BitmapFactory.decodeFile(path)?.asImageBitmap()
                                } catch (_: Exception) { null }
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
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pets,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Info Footer
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = commonName.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = scientificName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Decorative bottom line
                    HorizontalDivider(
                        modifier = Modifier.width(40.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun CardBackFace(
    card: AnimalCardContract,
    profile: AnimalProfileContract?,
    mode: CardPresentationMode = CardPresentationMode.NEW_CARD_REVIEW,
    onOpenLoreEditor: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isRare = card.rarity.equals("RARE", ignoreCase = true) || (profile?.isRareSpecies == true)

    Surface(
        modifier = modifier
            .testTag("card_back_face")
            .border(
                width = if (isRare) 3.dp else 2.dp,
                brush = if (isRare) Brush.linearGradient(listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.primary))
                        else androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 8.dp
    ) {
        // Inner card content with pattern
        Surface(
            modifier = Modifier.padding(6.dp),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.secondary,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary.copy(alpha=0.3f))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // Background Pattern simulation
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(4) {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            repeat(3) {
                                Icon(
                                    imageVector = Icons.Default.NaturePeople,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.08f),
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }
                }
                
                // Central Logo or Emblem
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shadowElevation = 4.dp
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
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

@Composable
private fun mapConservationStatus(code: String): String {
    return when (code.uppercase()) {
        "LC" -> stringResource(R.string.conservation_lc)
        "NT" -> stringResource(R.string.conservation_nt)
        "VU" -> stringResource(R.string.conservation_vu)
        "EN" -> stringResource(R.string.conservation_en)
        "CR" -> stringResource(R.string.conservation_cr)
        "EW" -> stringResource(R.string.conservation_ew)
        "EX" -> stringResource(R.string.conservation_ex)
        "NE" -> stringResource(R.string.conservation_ne)
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
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("action_release_button")
            ) {
                Icon(
                    imageVector = Icons.Default.NaturePeople,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.card_action_release), maxLines = 1)
            }

            // Botón Voltear (accesibilidad y control explícito)
            OutlinedButton(
                onClick = onToggleFlip,
                enabled = !isProcessing,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("action_flip_button")
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isFlipped) stringResource(R.string.card_action_show_front) else stringResource(R.string.card_action_show_back), maxLines = 1)
            }

            // Botón Guardar en Baúl (persistencia a colección)
            Button(
                onClick = onSave,
                enabled = !isProcessing,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1.3f)
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("action_save_button")
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.action_saving))
                } else {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.action_save), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Barra de inspección para cartas ya persistidas reabiertas desde el Baúl.
 * Soporta edición de Lore (DEC-041), liberación segura del slot, volteo y navegación de regreso.
 */
@Composable
private fun PersistedCardActionBar(
    isFlipped: Boolean,
    remainingEdits: Int,
    onToggleFlip: () -> Unit,
    onOpenLoreEditor: () -> Unit,
    onOpenReleaseDialog: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Surface(
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fila 1: Acciones contextuales (Editar Lore y Liberar Carta)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onOpenLoreEditor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("action_edit_lore_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.action_edit_lore),
                        maxLines = 1,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onOpenReleaseDialog,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("action_release_persisted_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.card_action_release),
                        maxLines = 1,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Fila 2: Navegación y Volteo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("action_back_to_collection_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.card_action_back_to_storage), maxLines = 1)
                }

                Button(
                    onClick = onToggleFlip,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("action_flip_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isFlipped) stringResource(R.string.card_action_view_front) else stringResource(R.string.card_action_view_back),
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Diálogo modal para edición de Historia Personal (Lore) con cuota DEC-041 y límite de 300 caracteres.
 */
@Composable
fun LoreEditDialog(
    initialLore: String,
    remainingEdits: Int,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf(initialLore) }
    val maxChars = LoreConstants.MAX_LORE_LENGTH

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = stringResource(R.string.lore_editor_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.lore_edits_remaining, remainingEdits),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        if (it.length <= maxChars) {
                            text = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.lore_editor_hint),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lore_input_field"),
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = stringResource(R.string.lore_char_counter, text.length),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (text.length >= maxChars) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.testTag("lore_char_counter_text")
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.card_lore_disclaimer),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontStyle = FontStyle.Italic,
                        fontSize = 11.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(text) },
                enabled = text.length <= maxChars,
                modifier = Modifier.testTag("action_save_lore_edit"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.action_save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("action_cancel_lore_edit")
            ) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

