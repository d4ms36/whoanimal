package com.whoanimal.app.ui.screens.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whoanimal.app.domain.repository.ProfileRepository
import com.whoanimal.app.domain.repository.ProfileValidationResult
import com.whoanimal.app.ui.theme.ForestGreenPrimary
import kotlinx.coroutines.launch

/**
 * Pantalla de registro del perfil local de explorador (Create Profile).
 */
@Composable
fun CreateProfileScreen(
    profileRepository: ProfileRepository,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    var explorerName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val submitProfile: () -> Unit = {
        keyboardController?.hide()
        val validation = profileRepository.validateName(explorerName)
        if (validation is ProfileValidationResult.Invalid) {
            errorMessage = validation.reason
        } else {
            errorMessage = null
            isLoading = true
            coroutineScope.launch {
                try {
                    profileRepository.createProfile(explorerName)
                    isLoading = false
                    onProfileCreated()
                } catch (e: Exception) {
                    isLoading = false
                    errorMessage = e.message ?: "Ocurrió un error al crear el perfil."
                }
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Emblema de perfil de campo
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(
                        color = ForestGreenPrimary.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = "Badge de Explorador",
                    modifier = Modifier.size(50.dp),
                    tint = ForestGreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tu Perfil de Explorador",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Elige el nombre con el que firmarás tus observaciones zoológicas y registrarás las cartas de tu colección.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = explorerName,
                onValueChange = { input ->
                    if (input.length <= 30) {
                        explorerName = input
                        if (errorMessage != null) {
                            val v = profileRepository.validateName(input)
                            errorMessage = if (v is ProfileValidationResult.Invalid) v.reason else null
                        }
                    }
                },
                label = { Text("Nombre de Explorador") },
                placeholder = { Text("Ej. Dra. Carmen Silva") },
                singleLine = true,
                isError = errorMessage != null,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = errorMessage ?: "Mínimo 2 caracteres",
                            color = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${explorerName.trim().length}/30",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                trailingIcon = {
                    if (errorMessage != null) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Error de validación",
                            tint = MaterialTheme.colorScheme.error
                        )
                    } else if (explorerName.trim().length >= 2) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Nombre válido",
                            tint = ForestGreenPrimary
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { submitProfile() }
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ForestGreenPrimary,
                    focusedLabelColor = ForestGreenPrimary,
                    cursorColor = ForestGreenPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = { submitProfile() },
                enabled = !isLoading && explorerName.trim().isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForestGreenPrimary,
                    disabledContainerColor = ForestGreenPrimary.copy(alpha = 0.4f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Creando perfil...",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                } else {
                    Text(
                        text = "Entrar a WHO Animal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
