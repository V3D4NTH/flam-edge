#include "OpenCVProcessor.h"
#include <opencv2/opencv.hpp>
#include <android/log.h>

#define LOG_TAG "OpenCVProcessor"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

void OpenCVProcessor::processImage(const unsigned char *inputData,
                                   unsigned char *outputData,
                                   int width,
                                   int height) {
    try {
        // Create OpenCV Mat from input data (grayscale)
        cv::Mat inputMat(height, width, CV_8UC1, (void *)inputData);

        // Apply Canny edge detection
        cv::Mat edges;
        cv::Canny(inputMat, edges, 50, 150);

        // Optional: Apply Gaussian blur before edge detection for better results
        // cv::Mat blurred;
        // cv::GaussianBlur(inputMat, blurred, cv::Size(5, 5), 1.5);
        // cv::Canny(blurred, edges, 50, 150);

        // Copy result to output
        memcpy(outputData, edges.data, width * height);

        LOGI("Processed frame: %dx%d", width, height);

    } catch (const cv::Exception &e) {
        LOGE("OpenCV exception: %s", e.what());
        // On error, copy input to output (passthrough)
        memcpy(outputData, inputData, width * height);
    }
}

void OpenCVProcessor::applyCannyEdgeDetection(const unsigned char *inputData,
                                              unsigned char *outputData,
                                              int width,
                                              int height,
                                              double threshold1,
                                              double threshold2) {
    try {
        // Create Mat from input
        cv::Mat inputMat(height, width, CV_8UC1, (void *)inputData);
        cv::Mat edges;

        // Apply Gaussian blur to reduce noise
        cv::Mat blurred;
        cv::GaussianBlur(inputMat, blurred, cv::Size(5, 5), 1.5);

        // Apply Canny edge detection
        cv::Canny(blurred, edges, threshold1, threshold2);

        // Copy result
        memcpy(outputData, edges.data, width * height);

    } catch (const cv::Exception &e) {
        LOGE("Canny edge detection error: %s", e.what());
        memcpy(outputData, inputData, width * height);
    }
}

void OpenCVProcessor::convertToGrayscale(const unsigned char *inputData,
                                         unsigned char *outputData,
                                         int width,
                                         int height) {
    try {
        // Assuming input is already grayscale (YUV format from camera)
        // Just copy the data
        memcpy(outputData, inputData, width * height);

        // If input were RGB/BGR, we would use:
        // cv::Mat inputMat(height, width, CV_8UC3, (void *)inputData);
        // cv::Mat grayMat;
        // cv::cvtColor(inputMat, grayMat, cv::COLOR_BGR2GRAY);
        // memcpy(outputData, grayMat.data, width * height);

    } catch (const cv::Exception &e) {
        LOGE("Grayscale conversion error: %s", e.what());
        memcpy(outputData, inputData, width * height);
    }
}