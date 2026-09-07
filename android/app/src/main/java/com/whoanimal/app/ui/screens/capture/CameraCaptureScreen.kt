package com.whoanimal.app.ui.screens.capture

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.OrientationEventListener
import android.view.Surface as AndroidSurface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.whoanimal.app.R
import com.whoanimal.app.domain.identification.IdentificationService
import com.whoanimal.app.domain.model.IdentificationResultContract
import com.whoanimal.app.domain.model.ObservationContract
import com.whoanimal.app.ui.theme.ForestGreenPrimary
import com.whoanimal.app.ui.theme.SageAccent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.util.UUID

/**
 * Pantalla de Captura en Vivo con CameraX y Hardening de Hardware (WHO-018B / WHO-024).
 *
 * Características de endurecimiento de hardware y accesibilidad:
 * 1. Desvinculación limpia de CameraProvider en [DisposableEffect] para prevenir fugas de recursos.
 * 2. Soporte dinámico de rotación física mediante [OrientationEventListener].
 * 3. Verificación preventiva de disponibilidad física de cámara ([ProcessCameraProvider.hasCamera]).
 * 4. Fallback resiliente con botón explícito de captura simulada ante fallo o ausencia de sensor.
 * 5. Objetivos táctiles mínimos de 48dp y semántica completa para TalkBack/lectores de pantalla.
 */
@Composable
fun CameraCaptureScreen(
    onNavigateToResult: (IdentificationResultContract) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    identificationService: IdentificationService = remember { IdentificationService() },
    onNavigateToResultWithPhoto: ((IdentificationResultContract, String) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var permissionRequestedOnce by remember { mutableStateOf(false) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var isProcessingCapture by remember { mutableStateOf(false) }
    var captureErrorMessage by remember { mutableStateOf<String?>(null) }
    var isCameraBound by remember { mutableStateOf(false) }
    var isCameraHardwareAvailable by remember { mutableStateOf(true) }
    var activeCameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    // Desvinculación limpia del ciclo de vida al salir de la pantalla
    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                activeCameraProvider?.unbindAll()
            } catch (_: Exception) {
                // Silencioso en desmontaje
            }
        }
    }

    // Escucha de rotación de hardware para ajustar targetRotation en ImageCapture
    val orientationEventListener = remember {
        object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                val targetRotation = when (orientation) {
                    in 45..134 -> AndroidSurface.ROTATION_270
                    in 135..224 -> AndroidSurface.ROTATION_180
                    in 225..314 -> AndroidSurface.ROTATION_90
                    else -> AndroidSurface.ROTATION_0
                }
                imageCapture?.targetRotation = targetRotation
            }
        }
    }

    DisposableEffect(orientationEventListener) {
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable()
        }
        onDispose {
            orientationEventListener.disable()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        permissionRequestedOnce = true
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            !hasCameraPermission -> {
                // Estado: Permiso Denegado / No Concedido
                CameraPermissionDeniedView(
                    onGrantPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    onNavigateBack = onNavigateBack
                )
            }

            !isCameraHardwareAvailable -> {
                // Estado: Sensor de Cámara No Disponible / Fallo de Hardware
                CameraUnavailableView(
                    isProcessing = isProcessingCapture,
                    onSimulateCapture = {
                        if (!isProcessingCapture) {
                            isProcessingCapture = true
                            captureErrorMessage = null
                            coroutineScope.launch {
                                executeSimulatedCapture(
                                    context = context,
                                    identificationService = identificationService,
                                    onSuccess = { result, photoPath ->
                                        isProcessingCapture = false
                                        if (onNavigateToResultWithPhoto != null) {
                                            onNavigateToResultWithPhoto(result, photoPath)
                                        } else {
                                            onNavigateToResult(result)
                                        }
                                    },
                                    onError = { err ->
                                        isProcessingCapture = false
                                        captureErrorMessage = err
                                    }
                                )
                            }
                        }
                    },
                    onNavigateBack = onNavigateBack
                )
            }

            else -> {
                // Estado Normal: Vista Previa y Captura CameraX
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({
                            try {
                                val cameraProvider = cameraProviderFuture.get()
                                activeCameraProvider = cameraProvider

                                val hasBack = cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
                                val hasFront = cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)

                                if (!hasBack && !hasFront) {
                                    isCameraHardwareAvailable = false
                                    isCameraBound = false
                                    return@addListener
                                }

                                val cameraSelector = if (hasBack) {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                }

                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                                val capture = ImageCapture.Builder()
                                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                    .setTargetRotation(previewView.display?.rotation ?: AndroidSurface.ROTATION_0)
                                    .build()

                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    capture
                                )

                                imageCapture = capture
                                isCameraBound = true
                            } catch (e: Exception) {
                                isCameraHardwareAvailable = false
                                captureErrorMessage = context.getString(R.string.camera_error_init, e.message ?: "")
                            }
                        }, ContextCompat.getMainExecutor(ctx))

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Retícula de enfoque zoológico (decorativa)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .border(
                                width = 2.dp,
                                color = SageAccent.copy(alpha = 0.65f),
                                shape = RoundedCornerShape(24.dp)
                            )
                    )
                }

                // Barra superior con botón volver (mínimo 48dp) e indicador de estado
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = Color.White
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color.Green, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.camera_live_view),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = Color.White
                            )
                        }
                    }

                    // Espaciador balanceador con target mínimo de 48dp
                    Spacer(modifier = Modifier.size(48.dp))
                }

                // Panel inferior con botón de captura grande
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.camera_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de disparo grande (76dp > 48dp)
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val shutterDesc = if (isProcessingCapture) {
                            stringResource(R.string.camera_capturing_in_progress)
                        } else {
                            stringResource(R.string.camera_shutter_description)
                        }

                        IconButton(
                            onClick = {
                                if (!isProcessingCapture) {
                                    isProcessingCapture = true
                                    captureErrorMessage = null

                                    val captureUseCase = imageCapture
                                    if (captureUseCase != null) {
                                        val photoFile = File(
                                            context.cacheDir,
                                            "observation_${UUID.randomUUID()}.jpg"
                                        )
                                        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                                        captureUseCase.takePicture(
                                            outputOptions,
                                            ContextCompat.getMainExecutor(context),
                                            object : ImageCapture.OnImageSavedCallback {
                                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                                    coroutineScope.launch {
                                                        processCapturedPhoto(
                                                            context = context,
                                                            photoFile = photoFile,
                                                            identificationService = identificationService,
                                                            onSuccess = { result, photoPath ->
                                                                isProcessingCapture = false
                                                                if (onNavigateToResultWithPhoto != null) {
                                                                    onNavigateToResultWithPhoto(result, photoPath)
                                                                } else {
                                                                    onNavigateToResult(result)
                                                                }
                                                            },
                                                            onError = { err ->
                                                                isProcessingCapture = false
                                                                captureErrorMessage = err
                                                            }
                                                        )
                                                    }
                                                }

                                                override fun onError(exception: ImageCaptureException) {
                                                    isProcessingCapture = false
                                                    captureErrorMessage = context.getString(
                                                        R.string.camera_error_capture_failed,
                                                        exception.message ?: ""
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        // Fallback seguro si la cámara física no responde
                                        coroutineScope.launch {
                                            executeSimulatedCapture(
                                                context = context,
                                                identificationService = identificationService,
                                                onSuccess = { result, photoPath ->
                                                    isProcessingCapture = false
                                                    if (onNavigateToResultWithPhoto != null) {
                                                        onNavigateToResultWithPhoto(result, photoPath)
                                                    } else {
                                                        onNavigateToResult(result)
                                                    }
                                                },
                                                onError = { err ->
                                                    isProcessingCapture = false
                                                    captureErrorMessage = err
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                            enabled = !isProcessingCapture,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(ForestGreenPrimary, CircleShape)
                                .semantics {
                                    role = Role.Button
                                    contentDescription = shutterDesc
                                }
                        ) {
                            if (isProcessingCapture) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(32.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null, // Ya comunicado en el IconButton semantics
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Overlay de error con LiveRegion para notificación inmediata en lectores de pantalla
        if (captureErrorMessage != null) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp, start = 20.dp, end = 20.dp)
                    .semantics { liveRegion = LiveRegionMode.Assertive }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(R.string.error_icon_description),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = captureErrorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Estado accesible cuando los permisos de cámara no han sido concedidos.
 */
@Composable
private fun CameraPermissionDeniedView(
    onGrantPermission: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(ForestGreenPrimary.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(46.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.camera_permission_title),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.camera_permission_rationale),
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onGrantPermission,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.camera_grant_permission_action))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onNavigateBack,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp)
        ) {
            Text(stringResource(R.string.camera_back_to_home_action))
        }
    }
}

/**
 * Estado accesible cuando el dispositivo no cuenta con sensor de cámara compatible o falló el hardware.
 */
@Composable
private fun CameraUnavailableView(
    isProcessing: Boolean,
    onSimulateCapture: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.NoPhotography,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(46.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.camera_unavailable_title),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.camera_unavailable_body),
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSimulateCapture,
            enabled = !isProcessing,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.camera_capturing_in_progress))
            } else {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.camera_simulated_capture_action))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onNavigateBack,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp)
        ) {
            Text(stringResource(R.string.action_back))
        }
    }
}

/**
 * Procesa la foto real guardada en almacenamiento temporal, construye la [ObservationContract]
 * y ejecuta el análisis taxonómico con [IdentificationService].
 */
private suspend fun processCapturedPhoto(
    context: Context,
    photoFile: File,
    identificationService: IdentificationService,
    onSuccess: (IdentificationResultContract, String) -> Unit,
    onError: (String) -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            if (!photoFile.exists() || photoFile.length() == 0L) {
                onError(context.getString(R.string.camera_error_invalid_file))
                return@withContext
            }

            val observation = ObservationContract(
                observationId = UUID.randomUUID().toString(),
                createdAt = Instant.now().toString(),
                imagePath = photoFile.absolutePath
            )

            val result = identificationService.identify(observation)
            withContext(Dispatchers.Main) {
                onSuccess(result, photoFile.absolutePath)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e.message ?: context.getString(R.string.camera_error_identification))
            }
        }
    }
}

/**
 * Fallback de captura fotográfica simulada para entornos sin sensor físico de cámara (ej. emulador).
 */
private suspend fun executeSimulatedCapture(
    context: Context,
    identificationService: IdentificationService,
    onSuccess: (IdentificationResultContract, String) -> Unit,
    onError: (String) -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            val fallbackFile = File(context.cacheDir, "simulated_observation_${UUID.randomUUID()}.jpg")
            if (!fallbackFile.exists()) {
                fallbackFile.writeBytes(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte())) // JPEG header dummy
            }

            val observation = ObservationContract(
                observationId = UUID.randomUUID().toString(),
                createdAt = Instant.now().toString(),
                imagePath = fallbackFile.absolutePath
            )

            val result = identificationService.identify(observation)
            withContext(Dispatchers.Main) {
                onSuccess(result, fallbackFile.absolutePath)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e.message ?: context.getString(R.string.camera_error_simulated))
            }
        }
    }
}
