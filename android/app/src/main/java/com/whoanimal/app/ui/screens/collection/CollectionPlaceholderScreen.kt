package com.whoanimal.app.ui.screens.collection

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomCollectionStorageRepository
import com.whoanimal.app.domain.boundary.StorageCapacityInfo
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.repository.CollectionStorageRepository
import com.whoanimal.app.ui.theme.ForestGreenPrimary
import com.whoanimal.app.ui.theme.SageAccent
import com.whoanimal.app.ui.theme.SoftCardBorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * WHO-018E — Collection Grid & Card Reopening.
 *
 * Pantalla principal del Baúl de Colección de WHO Animal:
 * BAÚL -> CONTENEDORES -> GRID DE CARTAS -> SELECCIONAR CARTA -> REOPEN (CardPresentationScreen)
 *
 * Muestra los contenedores del álbum, el grid de cartas guardadas por contenedor y
 * permite seleccionar cualquier carta persistida para reabrirla fielmente en su estado original.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    onNavigateBack: () -> Unit,
    onSelectCard: (AnimalCardContract) -> Unit,
    modifier: Modifier = Modifier,
    storageRepository: CollectionStorageRepository? = null,
    initialContainerIndex: Int = 1
) {
    val context = LocalContext.current
    val effectiveStorageRepository = storageRepository ?: remember(context) {
        val database = WhoAnimalDatabase.getInstance(context)
        RoomCollectionStorageRepository(database.cardDao(), database.storageSlotDao())
    }

    var selectedContainerIndex by remember { mutableIntStateOf(initialContainerIndex) }
    var capacityInfo by remember {
        mutableStateOf(StorageCapacityInfo(10, 30, 300, 0))
    }
    var cardsInContainer by remember {
        mutableStateOf<List<Pair<Int, AnimalCardContract>>>(emptyList())
    }
    var isLoadingContainer by remember { mutableStateOf(true) }

    // Carga la información de capacidad general y las cartas del contenedor seleccionado
    LaunchedEffect(selectedContainerIndex) {
        isLoadingContainer = true
        withContext(Dispatchers.IO) {
            try {
                capacityInfo = effectiveStorageRepository.getStorageCapacity()
                cardsInContainer = effectiveStorageRepository.getCardsInContainer(selectedContainerIndex)
            } catch (_: Exception) {
                cardsInContainer = emptyList()
            }
        }
        isLoadingContainer = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Baúl de Colección",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${capacityInfo.occupiedSlots} de ${capacityInfo.totalCapacity} cartas guardadas",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Home"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Selector horizontal de contenedores
            ContainerSelectorBar(
                totalContainers = capacityInfo.totalContainers,
                selectedContainer = selectedContainerIndex,
                onSelectContainer = { newIndex ->
                    selectedContainerIndex = newIndex
                }
            )

            // Header del contenedor activo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Contenedor $selectedContainerIndex",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ForestGreenPrimary.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "${cardsInContainer.size} / ${capacityInfo.slotsPerContainer} slots",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = ForestGreenPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Grid de cartas o estado vacío
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoadingContainer -> {
                        CircularProgressIndicator(color = ForestGreenPrimary)
                    }

                    cardsInContainer.isEmpty() -> {
                        EmptyContainerView(
                            containerIndex = selectedContainerIndex,
                            modifier = Modifier.padding(24.dp)
                        )
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("collection_cards_grid")
                        ) {
                            items(cardsInContainer, key = { it.second.cardId }) { (slotIndex, card) ->
                                CollectionCardItem(
                                    slotIndex = slotIndex,
                                    card = card,
                                    onCardClick = { onSelectCard(card) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Delegación retrocompatible para mantener compatibilidad con [CollectionPlaceholderScreen].
 */
@Composable
fun CollectionPlaceholderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    storageRepository: CollectionStorageRepository? = null,
    onSelectCard: ((AnimalCardContract) -> Unit)? = null
) {
    CollectionScreen(
        onNavigateBack = onNavigateBack,
        onSelectCard = onSelectCard ?: {},
        storageRepository = storageRepository,
        modifier = modifier
    )
}

/**
 * Barra selectora con desplazamiento horizontal para los contenedores del Baúl.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContainerSelectorBar(
    totalContainers: Int,
    selectedContainer: Int,
    onSelectContainer: (Int) -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (c in 1..totalContainers) {
                val isSelected = c == selectedContainer
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectContainer(c) },
                    label = {
                        Text(
                            text = "C-$c",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreenPrimary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("container_tab_$c")
                )
            }
        }
    }
}

/**
 * Tarjeta individual dentro del grid de colección.
 */
@Composable
private fun CollectionCardItem(
    slotIndex: Int,
    card: AnimalCardContract,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = remember(card.animalId) { OfficialStarterCatalog.findById(card.animalId) }
    val commonName = profile?.commonName ?: "Espécimen #${card.specimenNumber}"
    val scientificName = profile?.scientificName ?: "Incertae sedis"
    val isRare = card.rarity.equals("RARE", ignoreCase = true) || (profile?.isRareSpecies == true)

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

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isRare) 2.dp else 1.dp,
                brush = if (isRare) Brush.linearGradient(listOf(SageAccent, ForestGreenPrimary))
                else Brush.linearGradient(listOf(SoftCardBorder, SoftCardBorder)),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick)
            .testTag("collection_card_${card.cardId}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header del item: Slot y Rareza
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "S-$slotIndex",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (isRare) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SageAccent.copy(alpha = 0.2f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ForestGreenPrimary,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "RARA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = ForestGreenPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Thumbnail visual
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                SageAccent.copy(alpha = 0.18f),
                                ForestGreenPrimary.copy(alpha = 0.08f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (localBitmap != null) {
                    Image(
                        bitmap = localBitmap,
                        contentDescription = commonName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SageAccent.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Textos de identidad
            Text(
                text = commonName,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = scientificName,
                style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Estado visual cuando el contenedor seleccionado no tiene cartas almacenadas.
 */
@Composable
private fun EmptyContainerView(
    containerIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(ForestGreenPrimary.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Inbox,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Contenedor $containerIndex Vacío",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "No tienes cartas archivadas en este contenedor. Los ejemplares capturados y guardados se almacenarán aquí.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}
