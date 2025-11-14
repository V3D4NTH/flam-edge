# 🔥 flam-edge

Real-time edge detection viewer built using Android, OpenCV (C++), OpenGL ES, and TypeScript. 


### [Watch Demo Video](https://drive.google.com/file/d/1GGffBxzmYvPBxSYEqVdE7NuMpNOwKNMm/view?usp=sharing)

### [View Architecture](architecture.md)



-----

### Core Features

- [x] Camera feed integration (Camera2 API)
- [x] Canny edge detection (OpenCV C++)
- [x] JNI bridge (Java ↔ C++)
- [x] OpenGL ES 2.0 rendering
- [x] Real-time performance (10-15+ FPS)
- [x] TypeScript web viewer
- [x] Toggle raw/processed views
- [x] FPS counter display

### Bonus Features

- [x] Processing time display
- [x] Aspect ratio correction
- [x] Proper error handling
- [x] Multiple architecture support

### Documentation

- [x] README.md with features
- [x] Setup instructions
- [x] Screenshots included
- [x] Demo video added


## Tech Stack
- Android SDK (Java/Kotlin)
- NDK (Native Development Kit)
- OpenCV 4.x (C++)
- OpenGL ES 2.0+
- JNI (Java ↔ C++ communication)
- TypeScript (Web viewer)

### Devices Tested
- Samsung Galaxy A23
- Samsung Galaxy A55


### Prerequisites
- Android Studio 2025.2.1 or newer
- NDK r21 or newer
- CMake 3.10+
- OpenCV 4.x Android SDK
- Node.js 16+ and TypeScript
- USB cable for debugging

------------------------

###  Installation Steps

#### 1. Clone the Repository
```bash
git clone https://github.com/V3D4NTH/flam-edge.git
cd flam-edge
```

#### 2. Download OpenCV Android SDK

1. Download from: https://opencv.org/releases/
2. Extract to: `C:\Users\YourUsername\GitHub\OpenCV-android-sdk\`
3. Verify structure:
```
   OpenCV-android-sdk/
   └── sdk/
       ├── native/
       │   └── jni/
       └── java/
```

#### 3. Import OpenCV Module in Android Studio

1. Open project in Android Studio
2. **File** → **New** → **Import Module**
3. Navigate to: `OpenCV-android-sdk/sdk/`
4. Select the `sdk` folder
5. Name it: `opencv`
6. Click **Finish**

#### 4. Configure CMakeLists.txt Path

Open `app/src/main/cpp/CMakeLists.txt` and update line 12:
```cmake
set(OpenCV_DIR "C:/Users/YourUsername/GitHub/OpenCV-android-sdk/sdk/native/jni")
```

Replace `YourUsername` with your actual username!

#### 5. Sync Gradle

1. Click **File** → **Sync Project with Gradle Files**
2. Wait for sync to complete (~2-5 minutes)
3. Check for errors in Build tab

---

###  Running the Android App

#### Step 1: Enable USB Debugging on Your Phone

**For Samsung/Most Android:**
1. Go to **Settings** → **About phone**
2. Tap **Build number** 7 times
3. Go back to **Settings** → **Developer options**
4. Enable **USB debugging**

#### Step 2: Connect Device

1. Connect phone via USB cable
2. Unlock phone
3. Allow USB debugging when prompted
4. Check "Always allow from this computer"

#### Step 3: Verify Connection
```bash
adb devices
```

You should see your device listed!

#### Step 4: Run the App

1. In Android Studio, click the **green Run button** ▶️
2. Select your device from dropdown
3. App will install and launch automatically
4. Grant camera permission when prompted

---

###  Running the Web Viewer

#### Step 1: Install Dependencies
```bash
cd web
npm install
```

#### Step 2: Build TypeScript
```bash
npm run build
```

#### Step 3: Start Web Server
```bash
npm run serve
```

#### Step 4: Open in Browser

Navigate to: `http://localhost:8080`

**Features:**
-  **Load Image:** Upload processed frames from Android
-  **Generate Sample:** View demo edge detection
-  **Stats Display:** FPS, resolution, processing time

---

###  Using the App

#### Controls

| Action | Effect |
|--------|--------|
| **Tap "Show Processed"** | Toggle edge detection ON |
| **Tap "Show Raw Feed"** | Toggle edge detection OFF |
| **Point at objects** | See white edges on black background |
| **Cover camera** | Observe FPS changes |

#### What to Expect

**Edge Detection ON:**
- Black background
- White edges outlining objects
- Best results with high-contrast scenes
- 10-30 FPS depending on device

**Edge Detection OFF:**
- Grayscale camera feed
- 30 FPS stable performance

---

###  Troubleshooting

#### Build Fails with "OpenCV not found"

**Solution:**
1. Verify OpenCV path in `CMakeLists.txt`
2. Check that `sdk/native/jni` folder exists
3. Clean and rebuild: **Build** → **Clean Project** → **Rebuild Project**

#### App Crashes on Launch

**Solution:**
1. Check Logcat for error messages
2. Verify camera permission granted
3. Ensure OpenCV module imported
4. Check `implementation(project(":opencv"))` in `app/build.gradle.kts`

#### Device Not Detected

**Solution:**
1. Enable USB debugging on phone
2. Try different USB port
3. Run: `adb kill-server` then `adb start-server`
4. Check USB mode is set to "File Transfer" not "Charge only"

#### Web Viewer Build Fails

**Solution:**
1. Delete `node_modules` folder
2. Run: `npm install` again
3. Ensure Node.js 16+ installed
4. Check `package.json` exists

#### Low FPS / Laggy

**Solution:**
- Close other apps running in background
- Reduce camera resolution (modify `IMAGE_WIDTH/HEIGHT` in CameraManager)
- Test on a more powerful device
- Normal: 10-15 FPS on mid-range, 20-30 FPS on flagship devices

---

### Project Structure
```
flam-edge/
├── app/                          # Android application
│   ├── src/main/
│   │   ├── java/com/vedanth/flamedge/
│   │   │   ├── MainActivity.kt              # Main UI & orchestration
│   │   │   ├── CameraManager.kt             # Camera2 API handler
│   │   │   └── OpenGLRenderer.kt            # OpenGL ES renderer
│   │   ├── cpp/
│   │   │   ├── native-lib.cpp               # JNI bridge
│   │   │   ├── OpenCVProcessor.cpp          # Edge detection
│   │   │   ├── OpenCVProcessor.h            # Header file
│   │   │   └── CMakeLists.txt               # Native build config
│   │   ├── jniLibs/                         # OpenCV .so files
│   │   │   ├── arm64-v8a/
│   │   │   ├── armeabi-v7a/
│   │   │   ├── x86/
│   │   │   └── x86_64/
│   │   ├── res/                             # Android resources
│   │   │   └── layout/
│   │   │       └── activity_main.xml        # UI layout
│   │   └── AndroidManifest.xml              # App configuration
│   └── build.gradle.kts                     # App build config
├── web/                          # TypeScript web viewer
│   ├── src/
│   │   └── index.ts                         # TypeScript logic
│   ├── dist/                                # Built JavaScript
│   ├── index.html                           # Web UI
│   ├── package.json                         # npm config
│   ├── tsconfig.json                        # TypeScript config
│   └── webpack.config.js                    # Bundler config
├── opencv/                       # OpenCV module (imported)
├── screenshots/                  # App screenshots
├── README.md                     # Main documentation
├── ARCHITECTURE.md               # System architecture
└── build.gradle.kts              # Project build config
```



----------------------------------

## Dev Logs

### 13/11/25 16:50 IST

I will now be adding the codebase used for my first successful build. I am trying to have the edge-detection take place in real-time rather than having it delayed using frame-by-frame processing. Maybe it isn't rendering just yet due to latency issues or missing libraries.

Below is what is working so far:

✅ App opens successfully
✅ Camera permission handling
✅ Camera feed displaying (30 FPS)
✅ Toggle button working (Bonus)
✅ FPS counter working
✅ UI layout complete
✅ JNI bridge compiled
✅ Project structure set up

What isn't working:

❌ OpenCV edge detection (Don't know why. Probably due to missing native libraries)

#### Output

<img width="350" height="870" alt="image" src="https://github.com/user-attachments/assets/cede2c9c-8f65-422f-916d-7f7295be147b" />
<img width="355" height="885" alt="image" src="https://github.com/user-attachments/assets/c9484ee5-4fe6-4448-beba-2c695f60f4c6" />
<img width="357" height="884" alt="image" src="https://github.com/user-attachments/assets/d01f69ee-ad44-40f5-973a-69974522b2e1" />
<img width="356" height="892" alt="image" src="https://github.com/user-attachments/assets/192c0641-049e-41dc-af82-451b86c0dc10" />


-----------

### 13/11/25 17:45 IST

I changed my Git credentials from my former work account to my personal account as I noticed that it shows that my former work account is one of the contributors. Please note that it has been changed now as reflected in my latest commit made in MainActivity.kt

-----------

### 14/11/25 11:40 IST

Fixed OpenGL rendering. Added logging. Removed TextureView.

#### Output

<img width="354" height="878" alt="image" src="https://github.com/user-attachments/assets/811f2341-3382-48ca-85da-06d5412f3180" />

<img width="351" height="905" alt="image" src="https://github.com/user-attachments/assets/95698e3d-1765-4cce-b77d-dabf36015457" />



-----------


### 14/11/25 12:35 IST

Edge Detection works on mobile app. 

#### Output

<img width="355" height="688" alt="image" src="https://github.com/user-attachments/assets/f7c12185-4370-46a0-bde2-2297b271a8f6" />

<img width="368" height="718" alt="image" src="https://github.com/user-attachments/assets/c4114c8c-13f2-4f5d-9e05-843515243a85" />

<img width="349" height="694" alt="image" src="https://github.com/user-attachments/assets/b958c9be-e124-48b2-aa2d-a85c7259bb6d" />

--------------------

### 14/11/25 13:28 IST

TS Web Viewer works. Also changed aspect ratio of mobile app to 4:3 to normalise view and avoid compression.

#### Output

<img width="3206" height="1844" alt="image" src="https://github.com/user-attachments/assets/c8351751-1731-48e7-8802-4ffea73d1b50" />

<img width="3210" height="1876" alt="image" src="https://github.com/user-attachments/assets/86be4f8b-32e6-4ce7-a6ba-a31f155d2476" />



