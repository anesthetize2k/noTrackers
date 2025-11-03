# noTrackers - Installation Guide

## Quick Start

### Option 1: Build with Android Studio (Easiest)
1. **Download and install Android Studio** from https://developer.android.com/studio
2. **Open this project** in Android Studio
3. **Wait for Gradle sync** to complete
4. **Build the APK**: Go to Build → Build Bundle(s) / APK(s) → Build APK(s)
5. **Find your APK** in `app/build/outputs/apk/debug/app-debug.apk`

### Option 2: Build with Command Line
1. **Install Java JDK 8+** from https://adoptium.net/
2. **Install Android Studio** (includes Android SDK)
3. **Set environment variables**:
   - `JAVA_HOME` = path to your JDK installation
   - `ANDROID_HOME` = path to your Android SDK (usually in Android Studio installation)
4. **Run the build script**: Double-click `build.bat` or run `.\gradlew.bat assembleDebug`

## Installing on Your Phone

1. **Enable Unknown Sources**:
   - Go to Settings → Security → Unknown Sources (Android 7 and below)
   - Or Settings → Apps → Special Access → Install Unknown Apps (Android 8+)

2. **Transfer the APK** to your phone (via USB, email, cloud storage, etc.)

3. **Install the APK**:
   - Open the APK file on your phone
   - Tap "Install" when prompted
   - Grant any necessary permissions

## How to Use

1. **Share any URL** from any app (browser, social media, etc.)
2. **Select "noTrackers"** from the share menu
3. **The app will clean the URL** by removing tracking parameters
4. **Choose your action**:
   - **Copy**: Saves the cleaned URL to clipboard
   - **Share**: Opens the regular share menu with the cleaned URL

## What Gets Removed

The app removes tracking parameters like:
- `utm_source`, `utm_medium`, `utm_campaign` (Google Analytics)
- `fbclid` (Facebook)
- `gclid` (Google Ads)
- `msclkid` (Microsoft)
- `twclid` (Twitter)
- `igshid` (Instagram)
- And many more...

## Example

**Before**: `https://example.com/page?utm_source=facebook&utm_campaign=summer&fbclid=123456`
**After**: `https://example.com/page`

## Troubleshooting

### Build Issues
- **Java not found**: Install Java JDK and set JAVA_HOME
- **Android SDK not found**: Install Android Studio and set ANDROID_HOME
- **Gradle sync fails**: Check internet connection and try again

### Installation Issues
- **"Unknown sources" blocked**: Enable it in Android settings
- **"App not installed"**: Check if you have enough storage space
- **"Parse error"**: The APK might be corrupted, try rebuilding

### App Not Appearing in Share Menu
- **Restart your phone** after installation
- **Check app permissions** in Settings → Apps → noTrackers
- **Try sharing from different apps** (browser, social media, etc.)

## Privacy & Security

- ✅ **No data collection**: App doesn't send any data anywhere
- ✅ **No internet required**: Works completely offline
- ✅ **Local processing**: All URL cleaning happens on your device
- ✅ **Open source**: You can review all the code
- ✅ **No ads**: Completely ad-free

## Support

If you encounter any issues:
1. Check this guide first
2. Make sure you have the latest version
3. Try reinstalling the app
4. Check that your Android version is 5.0+ (API level 21+)

---

**Enjoy your privacy-enhanced sharing!** 🛡️
