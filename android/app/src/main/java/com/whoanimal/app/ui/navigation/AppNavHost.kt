package com.whoanimal.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomProfileRepository
import com.whoanimal.app.domain.model.IdentificationResultContract
import com.whoanimal.app.domain.repository.ProfileRepository
import com.whoanimal.app.ui.screens.capture.CameraCaptureScreen
import com.whoanimal.app.ui.screens.capture.IdentificationResultScreen
import com.whoanimal.app.ui.screens.collection.CollectionPlaceholderScreen
import com.whoanimal.app.ui.screens.home.HomeScreen
import com.whoanimal.app.ui.screens.profile.CreateProfileScreen
import com.whoanimal.app.ui.screens.splash.SplashScreen
import com.whoanimal.app.ui.screens.welcome.WelcomeScreen

import androidx.compose.runtime.rememberCoroutineScope
import com.whoanimal.app.data.local.repository.RoomCollectionStorageRepository
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.ui.screens.explore.ExploreScreen
import com.whoanimal.app.domain.model.DecisionStatus
import com.whoanimal.app.domain.model.IdentificationDecisionContract
import com.whoanimal.app.domain.model.ObservationContract
import com.whoanimal.app.domain.repository.CollectionStorageRepository
import com.whoanimal.app.data.ad.StubAdService
import com.whoanimal.app.domain.ad.AdService
import com.whoanimal.app.domain.service.CardGeneratorService
import com.whoanimal.app.ui.screens.card.CardPresentationMode
import com.whoanimal.app.ui.screens.card.CardPresentationScreen
import com.whoanimal.app.ui.screens.collection.CollectionScreen
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestination.Splash.route,
    profileRepository: ProfileRepository? = null,
    storageRepository: CollectionStorageRepository? = null,
    cardGenerator: CardGeneratorService? = null,
    adService: AdService? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val effectiveProfileRepository = profileRepository ?: remember(context) {
        RoomProfileRepository(WhoAnimalDatabase.getInstance(context).profileDao())
    }

    val effectiveStorageRepository = storageRepository ?: remember(context) {
        val db = WhoAnimalDatabase.getInstance(context)
        RoomCollectionStorageRepository(db.cardDao(), db.storageSlotDao())
    }

    val effectiveCardGenerator = cardGenerator ?: remember { CardGeneratorService() }

    val effectiveAdService = adService ?: remember { StubAdService() }

    var currentIdentificationResult by remember { mutableStateOf<IdentificationResultContract?>(null) }
    var currentCapturedImagePath by remember { mutableStateOf<String?>(null) }
    var currentCardToReview by remember { mutableStateOf<AnimalCardContract?>(null) }
    var currentPresentationMode by remember { mutableStateOf(CardPresentationMode.NEW_CARD_REVIEW) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestination.Splash.route) {
            SplashScreen(
                profileRepository = effectiveProfileRepository,
                onNavigateToHome = {
                    navController.navigate(NavDestination.Home.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToWelcome = {
                    navController.navigate(NavDestination.Welcome.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestination.Welcome.route) {
            WelcomeScreen(
                onStartExpedition = {
                    navController.navigate(NavDestination.CreateProfile.route)
                }
            )
        }

        composable(NavDestination.CreateProfile.route) {
            CreateProfileScreen(
                profileRepository = effectiveProfileRepository,
                onProfileCreated = {
                    navController.navigate(NavDestination.Home.route) {
                        popUpTo(NavDestination.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestination.Home.route) {
            HomeScreen(
                onNavigateToCapture = { navController.navigate(NavDestination.Capture.route) },
                onNavigateToExplore = { navController.navigate(NavDestination.Explore.route) },
                onNavigateToCollection = { navController.navigate(NavDestination.Collection.route) },
                profileRepository = effectiveProfileRepository,
                adService = effectiveAdService
            )
        }

        composable(NavDestination.Explore.route) {
            ExploreScreen()
        }

        composable(NavDestination.Capture.route) {
            CameraCaptureScreen(
                onNavigateToResult = { result ->
                    currentIdentificationResult = result
                    navController.navigate(NavDestination.IdentificationResult.route)
                },
                onNavigateToResultWithPhoto = { result, photoPath ->
                    currentIdentificationResult = result
                    currentCapturedImagePath = photoPath
                    navController.navigate(NavDestination.IdentificationResult.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }




        composable(NavDestination.IdentificationResult.route) {
            IdentificationResultScreen(
                result = currentIdentificationResult,
                onAcceptDecision = { result ->
                    val topCandidate = result.topCandidate
                    if (topCandidate != null) {
                        val decision = IdentificationDecisionContract(
                            decisionId = UUID.randomUUID().toString(),
                            identificationId = result.identificationId,
                            animalId = topCandidate.animalId,
                            status = DecisionStatus.ACCEPTED,
                            decidedAt = Instant.now().toString()
                        )
                        val (_, generatedCard) = effectiveCardGenerator.assembleFromDecision(
                            decision = decision,
                            observation = ObservationContract(
                                observationId = result.observationId,
                                createdAt = result.createdAt,
                                imagePath = currentCapturedImagePath ?: ""
                            ),
                            confidence = topCandidate.confidence,
                            displayLocation = "Reserva Natural Protegida"
                        )
                        currentCardToReview = generatedCard
                        currentPresentationMode = CardPresentationMode.NEW_CARD_REVIEW
                        navController.navigate(NavDestination.CardReview.route)
                    }
                },
                onDiscardDecision = {
                    currentIdentificationResult = null
                    currentCapturedImagePath = null
                    navController.popBackStack(NavDestination.Home.route, inclusive = false)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestination.CardReview.route) {
            var remainingEdits by remember { mutableIntStateOf(3) }

            LaunchedEffect(Unit) {
                remainingEdits = effectiveProfileRepository.getRemainingLoreEdits()
            }

            CardPresentationScreen(
                card = currentCardToReview,
                mode = currentPresentationMode,
                remainingLoreEdits = remainingEdits,
                onUpdateLore = { cardId, newLore ->
                    var success = false
                    if (effectiveProfileRepository.canEditLore()) {
                        val updated = effectiveStorageRepository.updateCardLore(cardId, newLore)
                        if (updated) {
                            effectiveProfileRepository.consumeLoreEdit()
                            remainingEdits = effectiveProfileRepository.getRemainingLoreEdits()
                            currentCardToReview = currentCardToReview?.copy(personalLore = newLore)
                            success = true
                        }
                    }
                    success
                },
                onReleasePersistedCard = { cardId ->
                    effectiveStorageRepository.deleteCardAndFreeSlot(cardId)
                    currentCardToReview = null
                    navController.popBackStack()
                },
                onSaveCard = { cardToSave ->
                    scope.launch {
                        try {
                            effectiveStorageRepository.autoAssignSlot(cardToSave)
                        } catch (_: Exception) {
                            // Si ya existe o hay error, la navegación continúa de forma segura
                        }
                        currentCardToReview = null
                        currentIdentificationResult = null
                        currentCapturedImagePath = null
                        navController.navigate(NavDestination.Collection.route) {
                            popUpTo(NavDestination.Home.route) { inclusive = false }
                        }
                    }
                },
                onReleaseCard = {
                    currentCardToReview = null
                    currentIdentificationResult = null
                    currentCapturedImagePath = null
                    navController.popBackStack(NavDestination.Home.route, inclusive = false)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestination.Collection.route) {
            CollectionScreen(
                storageRepository = effectiveStorageRepository,
                onSelectCard = { card ->
                    currentCardToReview = card
                    currentPresentationMode = CardPresentationMode.PERSISTED_CARD
                    navController.navigate(NavDestination.CardReview.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}


