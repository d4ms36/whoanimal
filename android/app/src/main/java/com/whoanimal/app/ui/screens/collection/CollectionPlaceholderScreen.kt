package com.whoanimal.app.ui.screens.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomCollectionStorageRepository
import com.whoanimal.app.domain.boundary.StorageCapacityInfo
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.VerificationStatus
import com.whoanimal.app.domain.repository.StorageConstants
import com.whoanimal.app.ui.theme.EmeraldSecondary
import com.whoanimal.app.ui.theme.ForestGreenPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionPlaceholderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val database = remember { WhoAnimalDatabase.getInstance(context) }
    val storageRepository = remember {
        RoomCollectionStorageRepository(database.cardDao(), database.storageSlotDao())
    }

    var capacityInfo by remember {
        mutableStateOf(StorageCapacityInfo(10, 30, 300, 0))
    }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var lastInsertedCardId by remember { mutableStateOf<String?>(null) }

    suspend fun refreshCapacity() {
        val updated = withContext(Dispatchers.IO) {
            storageRepository.getStorageCapacity()
        }
        capacityInfo = updated
    }

    LaunchedEffect(Unit) {
        refreshCapacity()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Álbum & Contenedores") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(EmeraldSecondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CollectionsBookmark,
                    contentDescription = null,
                    tint = EmeraldSecondary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(
                text = "Persistencia Local de Colección",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "Capacidad canónica Alpha 0.1: 10 contenedores x 30 cartas (300 cartas totales).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Tarjeta de Estado de Almacenamiento con Room
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Ocupación: ${capacityInfo.occupiedSlots} / ${capacityInfo.totalCapacity} cartas",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = "Persistencia activa en SQLite mediante Room. Las cartas y sus ubicaciones sobreviven al reinicio de la aplicación.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Grid visual de los 10 contenedores
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (row in 0 until 5) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (col in 0 until 2) {
                                    val containerIndex = row * 2 + col + 1
                                    ContainerSlotPreview(
                                        containerNumber = containerIndex,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Mensaje de estado de operación
            if (statusMessage != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ForestGreenPrimary.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = statusMessage!!,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = ForestGreenPrimary,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Acciones de demostración de persistencia
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val uniqueCardId = UUID.randomUUID().toString()
                            val uniqueCaptureId = UUID.randomUUID().toString()
                            val species = OfficialStarterCatalog.allSpecies.random()

                            val demoCard = AnimalCardContract(
                                cardId = uniqueCardId,
                                animalId = species.animalId,
                                specimenNumber = capacityInfo.occupiedSlots + 1,
                                generation = "genesis",
                                issuedAt = Instant.now().toString(),
                                populationAtIssuance = capacityInfo.occupiedSlots + 1,
                                rarity = "COMMON",
                                captureId = uniqueCaptureId,
                                serial = "WA-${species.commonName.take(3).uppercase()}-${UUID.randomUUID().toString().take(4).uppercase()}",
                                verificationStatus = VerificationStatus.UNVERIFIED,
                                identificationMethod = "DETERMINISTIC_ALPHA"
                            )

                            try {
                                val slot = withContext(Dispatchers.IO) {
                                    storageRepository.autoAssignSlot(demoCard)
                                }
                                lastInsertedCardId = uniqueCardId
                                statusMessage = "Carta guardada en Contenedor ${slot.containerIndex}, Slot ${slot.slotIndex}."
                                refreshCapacity()
                            } catch (e: Exception) {
                                statusMessage = "Error al guardar: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Guardar Carta Demo", style = MaterialTheme.typography.labelMedium)
                }

                if (lastInsertedCardId != null) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                try {
                                    val deleted = withContext(Dispatchers.IO) {
                                        storageRepository.deleteCardAndFreeSlot(lastInsertedCardId!!)
                                    }
                                    if (deleted) {
                                        statusMessage = "Carta eliminada y slot liberado correctamente."
                                        lastInsertedCardId = null
                                        refreshCapacity()
                                    }
                                } catch (e: Exception) {
                                    statusMessage = "Error al eliminar: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Liberar Slot", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Regresar a Home")
            }
        }
    }
}

@Composable
private fun ContainerSlotPreview(
    containerNumber: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = RoundedCornerShape(10.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "C-$containerNumber",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = ForestGreenPrimary
            )
            Text(
                text = "Max 30",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
