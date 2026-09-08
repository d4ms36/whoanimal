# Configuración del Emulador Android para WHO Animal

Este documento detalla el entorno oficial y el procedimiento utilizado para ejecutar y validar el **Technical Smoke Test** de la versión Beta de WHO Animal sin depender de un dispositivo físico.

## 1. Requisitos
- **Java:** OpenJDK 25.0.3 (o compatible 64-Bit Server VM).
- **Android SDK:** Instalado localmente (ej: `C:\Users\Dams\AppData\Local\Android\Sdk`).
- **Platform Tools:** Proveen `adb` (Android Debug Bridge).
- **Android CLI:** `cmdline-tools` y `emulator` (disponibles mediante `sdkmanager` y `avdmanager`).

## 2. Crear el AVD
El dispositivo virtual oficial para la fase Beta se denomina `whoanimal-beta`.
- **API:** 34 (Android 14)
- **ABI:** x86_64
- **Google APIs:** Incluidas.

*Comando de creación (ejemplo referencial):*
```powershell
avdmanager create avd -n whoanimal-beta -k "system-images;android-34;google_apis;x86_64" --device "pixel_4"
```

## 3. Arrancar el emulador
El emulador se lanza de forma *headless* (sin ventana) utilizando el acelerador SwiftShader.
```powershell
emulator.exe -avd whoanimal-beta -no-audio -no-window -no-snapshot -gpu swiftshader_indirect
```
*(Nota: Para entornos con UI gráfica, omita `-no-window`).*

## 4. Verificar ADB
Una vez arrancado, compruebe que el dispositivo está conectado:
```powershell
adb devices
```
Debería aparecer listado (ej: `emulator-5554 device`).

## 5. Instalar APK
Construya el APK de release o recupérelo del directorio de compilación, y luego instálelo:
```powershell
adb install -r android/app/build/outputs/apk/release/app-release.apk
```
Debe retornar `Success`.

## 6. Lanzar la aplicación
Puede iniciar el flujo principal de WHO Animal mediante `monkey` o `am start`:
```powershell
adb shell am start -n com.whoanimal.app/com.whoanimal.app.MainActivity
```

## 7. Logcat
Para inspeccionar posibles crashes o el flujo interno de la app (ej: inferencia de modelos), mantenga abierta una terminal con:
```powershell
adb logcat -s "WHO_Animal"
```

## 8. Capturas (Dumping UI)
Para verificar programáticamente el estado de la UI (por ejemplo, validar si el botón "Save" está visible):
```powershell
adb shell uiautomator dump
adb pull /sdcard/window_dump.xml
```

## 9. Limitaciones del Emulador
La validación en emulador es útil para **Smoke Tests** técnicos y de navegación, pero tiene limitaciones físicas estrictas. Los siguientes escenarios **requieren un teléfono físico**:
- **Cámara Física:** El emulador inyecta un stream gráfico artificial, inútil para probar el auto-focus o la resolución.
- **Calidad de Identificación:** La precisión biológica del modelo de Machine Learning requiere fotos de la naturaleza, con variaciones de iluminación reales que el emulador no puede proveer.
- **Sensores:** Giroscopio, GPS u otros sensores contextuales.

## 10. Flujo Recomendado
1. Build (`.\gradlew.bat assembleRelease`).
2. Start Emulator (`emulator -avd whoanimal-beta ...`).
3. Install (`adb install -r ...`).
4. Launch (`adb shell am start ...`).
5. Run Smoke Test manual o automatizado.
