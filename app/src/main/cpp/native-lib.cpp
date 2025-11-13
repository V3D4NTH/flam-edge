#include <jni.h>
#include <string>
#include <android/log.h>
#include "OpenCVProcessor.h"

#define LOG_TAG "NativeLib"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

/**
 * Process image using OpenCV Canny edge detection
 *
 * @param env JNI environment
 * @param thiz Java object reference
 * @param imageData Input image byte array (grayscale)
 * @param width Image width
 * @param height Image height
 * @return Processed image byte array
 */
JNIEXPORT jbyteArray JNICALL
Java_com_vedanth_flamedge_MainActivity_processImageNative(
        JNIEnv *env,
        jobject thiz,
        jbyteArray imageData,
        jint width,
        jint height) {

    try {
        // Get input data
        jbyte *inputBytes = env->GetByteArrayElements(imageData, nullptr);
        jsize inputSize = env->GetArrayLength(imageData);

        if (inputBytes == nullptr) {
            LOGE("Failed to get input byte array");
            return nullptr;
        }

        // Convert jbyte to unsigned char
        unsigned char *inputData = reinterpret_cast<unsigned char *>(inputBytes);

        // Create output buffer
        unsigned char *outputData = new unsigned char[inputSize];

        // Process with OpenCV
        OpenCVProcessor::processImage(inputData, outputData, width, height);

        // Create output Java byte array
        jbyteArray outputArray = env->NewByteArray(inputSize);
        env->SetByteArrayRegion(outputArray, 0, inputSize,
                                reinterpret_cast<jbyte *>(outputData));

        // Cleanup
        env->ReleaseByteArrayElements(imageData, inputBytes, JNI_ABORT);
        delete[] outputData;

        return outputArray;

    } catch (const std::exception &e) {
        LOGE("Exception in processImageNative: %s", e.what());
        return nullptr;
    }
}

/**
 * Test function to verify JNI is working
 */
JNIEXPORT jstring JNICALL
Java_com_vedanth_flamedge_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++ with OpenCV!";
    return env->NewStringUTF(hello.c_str());
}

} // extern "C"