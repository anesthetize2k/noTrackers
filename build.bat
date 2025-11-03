@echo off
echo Building noTrackers Android App...
echo.

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 8 or higher from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo or https://adoptium.net/
    echo.
    echo After installing Java, restart your command prompt and try again.
    pause
    exit /b 1
)

REM Check if Android SDK is available
if not defined ANDROID_HOME (
    echo WARNING: ANDROID_HOME is not set
    echo This might cause build issues. Please install Android Studio
    echo and set ANDROID_HOME environment variable.
    echo.
)

echo Java found. Building APK...
echo.

REM Build the APK
gradlew.bat assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo SUCCESS: APK built successfully!
    echo Location: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo To install on your Android device:
    echo 1. Enable "Unknown sources" in Android settings
    echo 2. Transfer the APK to your device
    echo 3. Open the APK file to install
    echo.
) else (
    echo.
    echo ERROR: Build failed
    echo Please check the error messages above.
    echo.
    echo Common solutions:
    echo - Install Android Studio and Android SDK
    echo - Set ANDROID_HOME environment variable
    echo - Update build tools in Android Studio
    echo.
)

pause
