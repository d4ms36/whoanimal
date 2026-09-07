package com.whoanimal.app.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whoanimal.app.R
import com.whoanimal.app.domain.repository.ProfileRepository
import com.whoanimal.app.ui.theme.ForestGreenPrimary
import kotlinx.coroutines.delay

/**
 * Pantalla de Splash inicial de WHO Animal.
 *
 * Responsabilidad:
 * - Mostrar identidad visual y lema del producto.
 * - Verificar si existe un perfil de explorador local previamente creado.
 * - Navegar a [HomeScreen] si existe perfil; o a [WelcomeScreen] si es la primera apertura.
 */
@Composable
fun SplashScreen(
    profileRepository: ProfileRepository,
    onNavigateToHome: () -> Unit,
    onNavigateToWelcome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isChecking by remember { mutableStateOf(true) }
    var checkError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            delay(500)
            val active = profileRepository.getActiveProfile()
            if (active != null) {
                profileRepository.updateLastOpened(active.profileId)
                onNavigateToHome()
            } else {
                onNavigateToWelcome()
            }
        } catch (e: Exception) {
            isChecking = false
            checkError = e.message ?: "No se pudo comprobar la sesión local."
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // Icono emblemático con contenedor circular suave
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .background(
                        color = ForestGreenPrimary.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = stringResource(R.string.splash_logo_description),
                    modifier = Modifier.size(58.dp),
                    tint = ForestGreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.app_name).uppercase(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                ),
                color = ForestGreenPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.tagline),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ForestGreenPrimary.copy(alpha = 0.06f),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.splash_alpha_banner),
                    style = MaterialTheme.typography.labelSmall,
                    color = ForestGreenPrimary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            if (isChecking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = ForestGreenPrimary,
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.splash_loading),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            } else if (checkError != null) {
                Text(
                    text = checkError ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isChecking = true
                        checkError = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreenPrimary
                    )
                ) {
                    Text(stringResource(R.string.action_retry))
                }
            }
        }
    }
}
