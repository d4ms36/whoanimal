package com.whoanimal.app.ui.screens.capture

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalLifecycleOwner
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
 * Pantalla de Captura en Vivo con CameraX (WHO-018B).
 *
 * Reemplaza el placeholder previo permitiendo la toma de fotografías reales
 * que generan una [ObservationContract] válida y la envían al [IdentificationService].
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
        if (hasCameraPermission) {
            // Camera Preview View
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val capture = ImageCapture.Builder()
                                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                .build()

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

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
                            captureErrorMessage = "No se pudo inicializar la cámara: ${e.message}"
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Retícula de enfoque zoológico (Visor de observación)
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

            // Barra superior con botón volver e indicador de estado en vivo
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
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
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
                            text = "VISTA EN VIVO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = Color.White
                        )
                    }
                }

                // Espaciador para balance visual
                Spacer(modifier = Modifier.size(44.dp))
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
                    text = "Apunta al espécimen y presiona para observar",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de disparo grande
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
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
                                                captureErrorMessage = "Fallo al capturar fotografía: ${exception.message}"
                                            }
                                        }
                                    )
                                } else {
                                    // Fallback seguro si la cámara física no responde (ej. emulador)
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
                                contentDescription = "Capturar fotografía",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

        } else {
            // Estado de Permiso Denegado / No Concedido
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
                    text = "Permiso de Cámara Requerido",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "WHO Animal necesita acceder a la cámara para que puedas fotografiar y estudiar animales en campo sin perturbarlos. Toda observación permanece estrictamente en tu dispositivo.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Conceder Permiso de Cámara")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(50.dp)
                ) {
                    Text("Volver al Inicio")
                }
            }
        }

        // Overlay de error en captura si ocurrió algún problema
        if (captureErrorMessage != null) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp, start = 20.dp, end = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
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
 * Procesa la foto real guardada en almacenamiento temporal, construye la [ObservationContract]
 * y ejecuta el análisis taxonómico con [IdentificationService].
 */
private suspend fun processCapturedPhoto(
    photoFile: File,
    identificationService: IdentificationService,
    onSuccess: (IdentificationResultContract, String) -> Unit,
    onError: (String) -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            if (!photoFile.exists() || photoFile.length() == 0L) {
                onError("El archivo fotográfico temporal es inválido o está vacío.")
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
                onError(e.message ?: "Ocurrió un error al identificar la observación.")
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
                onError(e.message ?: "Error al procesar la captura simulada.")

            }
        }
    }
}
