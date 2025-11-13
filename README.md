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

Output So Far:

<img width="350" height="870" alt="image" src="https://github.com/user-attachments/assets/cede2c9c-8f65-422f-916d-7f7295be147b" />
<img width="355" height="885" alt="image" src="https://github.com/user-attachments/assets/c9484ee5-4fe6-4448-beba-2c695f60f4c6" />
<img width="357" height="884" alt="image" src="https://github.com/user-attachments/assets/d01f69ee-ad44-40f5-973a-69974522b2e1" />
<img width="356" height="892" alt="image" src="https://github.com/user-attachments/assets/192c0641-049e-41dc-af82-451b86c0dc10" />





