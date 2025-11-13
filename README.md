# flam-edge

Real-time edge detection viewer built using Android, OpenCV (C++), OpenGL ES, and TypeScript.



## Tech Stack
- Android SDK (Java/Kotlin)
- NDK (Native Development Kit)
- OpenCV 4.x (C++)
- OpenGL ES 2.0+
- JNI (Java ↔ C++ communication)
- TypeScript (Web viewer)







## Device Tested
- OnePlus 11R (Android 15)





### Prerequisites
- Android Studio 2025.2.1 or newer  
- NDK r21 or newer  
- CMake 3.10+  
- OpenCV 4.x Android SDK  
- Node.js 16+ and TypeScript

----------------------------------

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




