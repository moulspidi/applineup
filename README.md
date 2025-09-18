# FVBIB Lineup (Android, Kotlin)

Three screens: Home, Entrenadores (generate QR), Árbitros (scan QR), Instrucciones.

## Build (Android Studio recommended)
- Open folder in **Android Studio Giraffe+**
- Use JDK **17**
- Build → Make Project → Run

## CLI (if you have gradle installed)
```bash
gradle clean assembleDebug
# APK => app/build/outputs/apk/debug/app-debug.apk
```
(If you want to use `./gradlew`, generate a Gradle wrapper in Android Studio: *Tools → Gradle → Gradle Wrapper*).
