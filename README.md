# noTrackers Android App

A simple Android app that removes tracking parameters from URLs when sharing content.

## Features

- Appears in the Android share menu for any text/URL content
- Automatically removes common tracking parameters (utm_*, fbclid, gclid, etc.)
- Clean, simple interface with Copy and Share buttons
- Protects your privacy by removing tracking data from shared URLs

## How to Build and Install

### Prerequisites

1. **Install Java Development Kit (JDK) 8 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Or use OpenJDK: https://adoptium.net/
   - Make sure JAVA_HOME is set in your environment variables

2. **Install Android Studio**
   - Download from: https://developer.android.com/studio
   - This includes the Android SDK and build tools

### Building the APK

#### Option 1: Using Android Studio (Recommended)
1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to this folder and open it
4. Wait for Gradle sync to complete
5. Go to Build → Build Bundle(s) / APK(s) → Build APK(s)
6. The APK will be created in `app/build/outputs/apk/debug/`

#### Option 2: Using Command Line
1. Open Command Prompt or PowerShell in this directory
2. Run: `.\gradlew.bat assembleDebug`
3. The APK will be created in `app/build/outputs/apk/debug/`

### Installing the APK

1. Enable "Unknown sources" or "Install from unknown sources" in your Android device settings
2. Transfer the APK file to your Android device
3. Open the APK file on your device to install
4. Grant necessary permissions when prompted

## How to Use

1. When you want to share a URL from any app (browser, social media, etc.)
2. Tap the "Share" button
3. Select "noTrackers" from the share menu
4. The app will automatically clean the URL by removing tracking parameters
5. You can then:
   - **Copy** the cleaned URL to clipboard
   - **Share** the cleaned URL using your regular sharing apps (WhatsApp, Signal, etc.)

## Supported Tracking Parameters

The app removes common tracking parameters including:
- Google Analytics: utm_source, utm_medium, utm_campaign, utm_term, utm_content
- Facebook: fbclid
- Google Ads: gclid
- Microsoft: msclkid
- Twitter: twclid
- LinkedIn: li_fat_id
- Instagram: igshid, igsh
- Mailchimp: mc_cid, mc_eid
- And many more...

## Project Structure

```
noTrackers/
├── AndroidManifest.xml          # App configuration and share intent handling
├── app/
│   ├── build.gradle             # App-level build configuration
│   ├── src/main/
│   │   ├── java/com/notrackers/app/
│   │   │   └── MainActivity.java # Main app logic
│   │   └── res/
│   │       ├── layout/
│   │       │   └── activity_main.xml # UI layout
│   │       └── values/
│   │           └── strings.xml   # App strings
│   └── proguard-rules.pro       # Code obfuscation rules
├── build.gradle                 # Project-level build configuration
├── settings.gradle              # Project settings
├── gradle.properties            # Gradle properties
└── gradlew.bat                  # Gradle wrapper for Windows
```

## Customization

Tracking parameter removal is handled by the `UrlCleaner` class, which supports comprehensive global and domain-specific rules. Modify `UrlCleaner.java` to customize tracking parameter removal behavior.

## Privacy

This app:
- Does not collect any data
- Does not require internet access
- Processes URLs locally on your device
- Only removes tracking parameters, preserving the core URL functionality

## License

MIT License

Copyright (c) 2024 noTrackers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.