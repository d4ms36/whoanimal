# WHO Animal - Beta Release Pipeline & Internal Testing

## 1. Generación del Keystore (Firma de Release)

Para firmar la aplicación y poder subir el AAB a Google Play Console, se requiere un keystore. **Este keystore NUNCA debe incluirse en el repositorio.**

### Creación local del Keystore

Ejecuta el siguiente comando en tu terminal para generar un nuevo keystore local:

```bash
keytool -genkey -v -keystore release.keystore -alias whoanimal_alias -keyalg RSA -keysize 2048 -validity 10000
```

Sigue las instrucciones en pantalla para establecer contraseñas e información del desarrollador.

### Archivo `keystore.properties`

Crea un archivo llamado `keystore.properties` en la raíz del proyecto (`android/keystore.properties`) y configura tus credenciales:

```properties
storeFile=../release.keystore
storePassword=tu_password_del_keystore
keyAlias=whoanimal_alias
keyPassword=tu_password_del_alias
```

*Nota: La configuración Gradle cuenta con un fallback a la firma de `debug` si `keystore.properties` no es encontrado. Sin embargo, para builds válidos en Google Play, es obligatorio configurarlo.*

## 2. Generación del Android App Bundle (AAB)

Para compilar la versión optimizada con R8 y lista para Play Console:

```bash
cd android
.\gradlew.bat clean bundleRelease
```

El artefacto generado (`app-release.aab`) se encontrará en:
`android/app/build/outputs/bundle/release/`

## 3. Checklist de Validación y Subida a Play Console

Antes de crear un nuevo release interno, verifica:

- [ ] ¿Los Unit Tests pasan? (`.\gradlew.bat testDebugUnitTest` y Python tests).
- [ ] ¿La aplicación se compiló con R8/ProGuard sin errores? (`assembleRelease`).
- [ ] ¿El archivo `keystore.properties` está excluido del repositorio? (revisa `.gitignore`).
- [ ] ¿El `versionCode` fue incrementado en `build.gradle.kts`?

### Pasos de subida:
1. Accede a tu cuenta de Google Play Console.
2. Navega a **Pruebas internas**.
3. Sube el archivo `.aab`.
4. Añade las notas de la versión.
5. Inicia el despliegue para los testers.

## 4. Obtener el Hash SHA-256

Si necesitas verificar las firmas o configurar integraciones que dependen de Google Play App Signing:

```bash
keytool -list -v -keystore release.keystore
```
Copia la huella digital del certificado SHA-256.

## 5. Rollback

En caso de fallo crítico en la versión Beta:
1. Revertir el código a la etiqueta (tag) anterior en `main`.
2. Generar un nuevo AAB incrementando el `versionCode` +1 respecto a la versión fallida.
3. Subir el nuevo bundle a Play Console deteniendo (Halt) el despliegue de la versión rota.
