# 🔥 flam-edge

Real-time edge detection viewer built using Android, OpenCV (C++), OpenGL ES, and TypeScript.



## Tech Stack
- Android SDK (Java/Kotlin)
- NDK (Native Development Kit)
- OpenCV 4.x (C++)
- OpenGL ES 2.0+
- JNI (Java ↔ C++ communication)
- TypeScript (Web viewer)







## Device Tested
- Samsung Galaxy A23
- Samsung Galaxy A55





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

Demo video: [Watch Demo Video](https://drive.google.com/file/d/1GGffBxzmYvPBxSYEqVdE7NuMpNOwKNMm/view?usp=sharing)


