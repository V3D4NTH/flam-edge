#ifndef FLAM_EDGE_OPENCVPROCESSOR_H
#define FLAM_EDGE_OPENCVPROCESSOR_H

class OpenCVProcessor {
public:
    /**
     * Process grayscale image with Canny edge detection
     * 
     * @param inputData Input image data (grayscale)
     * @param outputData Output processed image data
     * @param width Image width
     * @param height Image height
     */
    static void processImage(const unsigned char *inputData,
                            unsigned char *outputData,
                            int width,
                            int height);
    
    /**
     * Apply Canny edge detection
     * 
     * @param inputData Input grayscale image
     * @param outputData Output edge-detected image
     * @param width Image width
     * @param height Image height
     * @param threshold1 First threshold for hysteresis
     * @param threshold2 Second threshold for hysteresis
     */
    static void applyCannyEdgeDetection(const unsigned char *inputData,
                                       unsigned char *outputData,
                                       int width,
                                       int height,
                                       double threshold1 = 50.0,
                                       double threshold2 = 150.0);
    
    /**
     * Convert to grayscale (if needed)
     * 
     * @param inputData Input image data
     * @param outputData Output grayscale image
     * @param width Image width
     * @param height Image height
     */
    static void convertToGrayscale(const unsigned char *inputData,
                                   unsigned char *outputData,
                                   int width,
                                   int height);
};

#endif //FLAM_EDGE_OPENCVPROCESSOR_H
