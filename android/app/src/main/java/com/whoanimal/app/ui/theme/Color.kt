package com.whoanimal.app.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta Oficial DMC para WHO Animal (WHO-028/029)
val DmcDarkOrangeSpice = Color(0xFFC85312) // #C85312 - DMC 720
val DmcLightWedgwood = Color(0xFF4C8CA8)   // #4C8CA8 - DMC 518
val DmcLightGrayGreen = Color(0xFFA7B7B4)  // #A7B7B4 - DMC 927
val DmcVeryLightTerraCotta = Color(0xFFD89B86) // #D89B86 - DMC 758
val DmcUltraDarkBeigeGray = Color(0xFF7D6653) // #7D6653 - DMC 3790
val DmcVeryDarkBrownGray = Color(0xFF3E352B) // #3E352B - DMC 3021

// Variantes Adicionales de la paleta para soporte Material 3
val DmcCreamBackground = Color(0xFFF7F5F0) // Fondo crema cálido para Light Theme
val DmcCardPaper = Color(0xFFFAF9F6)       // Color para el interior de las cartas
val DmcWhite = Color(0xFFFFFFFF)
val DmcErrorRed = Color(0xFFBA1A1A)
val DmcErrorDark = Color(0xFFFFB4AB)
val DmcSoftCardBorder = Color(0xFFE2E2E2)


// Semantic Color Tokens – Light Theme
val Primary = DmcDarkOrangeSpice
val Secondary = DmcLightWedgwood
val Surface = DmcCreamBackground
val SurfaceVariant = DmcLightGrayGreen
val Background = DmcCreamBackground
val CardSurface = DmcCardPaper
val Border = DmcSoftCardBorder
val TextPrimary = DmcVeryDarkBrownGray
val TextSecondary = DmcVeryLightTerraCotta
val Success = Color(0xFF4CAF50) // Green
val Warning = Color(0xFFFFC107) // Amber
val Error = DmcErrorRed
val PrimaryOn = DmcWhite
val SecondaryOn = DmcWhite
val ExplorerMark = Color(0x4D000000)
val ExplorerDarkMark = Color(0x33FFFFFF)


// Semantic Color Tokens – Dark Theme (using darker variants)
val DarkPrimary = DmcDarkOrangeSpice
val DarkSecondary = DmcLightWedgwood
val DarkSurface = DmcUltraDarkBeigeGray
val DarkSurfaceVariant = DmcVeryDarkBrownGray
val DarkBackground = DmcUltraDarkBeigeGray
val DarkCardSurface = DmcVeryDarkBrownGray
val DarkBorder = DmcSoftCardBorder
val DarkTextPrimary = DmcWhite
val DarkTextSecondary = DmcLightGrayGreen
val DarkSuccess = Color(0xFF81C784)
val DarkWarning = Color(0xFFFFD54F)
val DarkError = DmcErrorDark
