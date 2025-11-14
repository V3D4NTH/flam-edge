# 🏗 flam-edge Architecture

## System Overview

flam-edge is a real-time computer vision application that captures camera frames, processes them using OpenCV's Canny edge detection algorithm via JNI, and renders the output using OpenGL ES 2.0.

---

##  Architecture Diagram
```
┌─────────────────────────────────────────────────────────────────┐
│                         Android Application                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                     MainActivity.kt                       │  │
│  │  - UI Management                                          │  │
│  │  - Permission Handling                                    │  │
│  │  - Component Orchestration                                │  │
│  └────────────┬──────────────────────┬──────────────────────┘  │
│               │                      │                          │
│               ▼                      ▼                          │
│  ┌────────────────────┐  ┌─────────────────────┐              │
│  │  CameraManager.kt  │  │ OpenGLRenderer.kt   │              │
│  │  - Camera2 API     │  │ - OpenGL ES 2.0     │              │
│  │  - Frame Capture   │  │ - Texture Rendering │              │
│  │  - ImageReader     │  │ - Shader Programs   │              │
│  └─────────┬──────────┘  └─────────▲───────────┘              │
│            │                        │                          │
│            │ Frame Data             │ Processed Frame          │
│            ▼                        │                          │
│  ┌──────────────────────────────────┴──────────────────────┐  │
│  │                    JNI Bridge                            │  │
│  │               (native-lib.cpp)                           │  │
│  │  - Java ↔ C++ Communication                             │  │
│  │  - Memory Management                                     │  │
│  │  - Error Handling                                        │  │
│  └─────────────────────┬────────────────────────────────────┘  │
└────────────────────────┼───────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Native C++ Layer                              │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              OpenCVProcessor.cpp                          │  │
│  │  - Gaussian Blur (5x5 kernel, σ=1.5)                    │  │
│  │  - Canny Edge Detection (threshold: 50, 150)            │  │
│  │  - Mat Operations                                        │  │
│  └──────────────────────┬───────────────────────────────────┘  │
│                         │                                       │
│                         ▼                                       │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                OpenCV 4.12.0 Library                      │  │
│  │  - Image Processing Algorithms                            │  │
│  │  - Matrix Operations                                      │  │
│  │  - Computer Vision Functions                              │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    TypeScript Web Viewer                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    index.ts                               │  │
│  │  - Frame Display                                          │  │
│  │  - Statistics Visualization                               │  │
│  │  - Image Upload & Processing                             │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

##  Data Flow

### Frame Processing Pipeline

1. **Capture** (CameraManager.kt)
    - Camera2 API captures frame in YUV_420_888 format
    - ImageReader extracts Y-plane (grayscale) at 640x480
    - FPS calculation performed

2. **Transfer** (JNI Bridge)
    - Java ByteArray converted to C++ unsigned char*
    - Memory-efficient zero-copy where possible
    - Bounds checking and validation

3. **Process** (OpenCVProcessor.cpp)
    - Create cv::Mat from raw data
    - Apply Gaussian blur (noise reduction)
    - Execute Canny edge detection
    - Return processed frame

4. **Render** (OpenGLRenderer.kt)
    - Convert processed data to OpenGL texture
    - Update texture in GPU memory
    - Render full-screen quad with texture
    - Maintain aspect ratio with letterboxing

5. **Display**
    - GLSurfaceView presents frame
    - Real-time updates at 10-30 FPS
    - Toggle between raw/processed views

---

##  Technology Stack

### Android Layer
- **Language:** Kotlin
- **Min SDK:** API 24 (Android 7.0)
- **Target SDK:** API 35 (Android 15)
- **Camera:** Camera2 API
- **Graphics:** OpenGL ES 2.0
- **UI:** ConstraintLayout, Material Components

### Native Layer
- **Language:** C++ 14
- **Build System:** CMake 3.22.1
- **NDK:** r21+
- **OpenCV:** 4.12.0 (C++)
- **ABIs:** arm64-v8a, armeabi-v7a, x86, x86_64

### Web Layer
- **Language:** TypeScript 5.3.3
- **Build Tool:** Webpack 5
- **Runtime:** Node.js 16+
- **Package Manager:** npm

---

## Performance Characteristics

### Metrics
- **Frame Rate:** 10-30 FPS (device dependent)
- **Processing Time:** 20-40ms per frame (640x480)
- **Memory Usage:** ~50-80MB
- **Latency:** <100ms end-to-end

### Optimizations
- Zero-copy memory transfer where possible
- GPU-accelerated rendering via OpenGL
- Background thread for camera operations
- Efficient buffer management
- Native code for compute-intensive tasks

---

## 🔐 Security & Permissions

### Required Permissions
- `android.permission.CAMERA` - Camera access
- `android.permission.INTERNET` - Web viewer (bonus feature)

### Best Practices
- Runtime permission requests
- Graceful permission denial handling
- Resource cleanup on lifecycle events
- Thread-safe operations

---

## 🧩 Key Design Decisions

### 1. Camera2 API over Camera1
- **Reason:** Modern API with better control
- **Benefits:** Manual focus/exposure, better performance
- **Trade-off:** More complex but more powerful

### 2. GLSurfaceView over TextureView
- **Reason:** Hardware-accelerated rendering
- **Benefits:** Better performance, lower latency
- **Trade-off:** Less flexible but faster

### 3. Grayscale Processing
- **Reason:** Canny works on single-channel images
- **Benefits:** Faster processing, lower memory
- **Trade-off:** No color but meets requirements

### 4. JNI for OpenCV
- **Reason:** Performance-critical operations
- **Benefits:** ~10x faster than Java
- **Trade-off:** More complex but necessary

---

## 📦 Component Responsibilities

### MainActivity.kt
- Application entry point
- UI initialization and event handling
- Permission management
- Component lifecycle coordination

### CameraManager.kt
- Camera device management
- Frame capture and delivery
- FPS calculation
- Background thread management

### OpenGLRenderer.kt
- OpenGL context management
- Shader compilation and linking
- Texture creation and updates
- Rendering pipeline execution

### native-lib.cpp (JNI Bridge)
- Java/Kotlin ↔ C++ interface
- Type conversion and marshalling
- Error handling and logging

### OpenCVProcessor.cpp
- Image processing algorithms
- OpenCV API integration
- Performance-critical computations

---

## 🔍 Error Handling Strategy

### Layers of Protection
1. **Java Layer:** Try-catch blocks, null checks
2. **JNI Layer:** Pointer validation, size checks
3. **C++ Layer:** Exception handling, fallback modes
4. **OpenGL Layer:** Error state checks, shader validation

### Failure Modes
- **Camera failure:** Display error, request restart
- **OpenCV error:** Passthrough raw frame
- **OpenGL error:** Log and continue with last frame
- **Permission denial:** Show explanation, exit gracefully



---

##  Testing Strategy

### Unit Tests
- JNI bridge functionality
- OpenCV processor algorithms
- Camera manager state machine

### Integration Tests
- End-to-end frame pipeline
- OpenGL rendering
- Permission handling

### Manual Tests
- Various lighting conditions
- Different devices
- Edge cases (rotation, background, etc.)

---
