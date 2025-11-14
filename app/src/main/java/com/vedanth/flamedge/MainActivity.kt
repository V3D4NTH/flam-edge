package com.vedanth.flamedge

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var cameraManager: CameraManager
    private lateinit var glRenderer: OpenGLRenderer
    private lateinit var fpsTextView: TextView
    private lateinit var toggleButton: Button

    private var isProcessingEnabled = true
    private val CAMERA_PERMISSION_REQUEST = 100

    companion object {
        private const val TAG = "MainActivity"

        // Load native libraries
        init {
            System.loadLibrary("opencv_java4")  // Load OpenCV FIRST!
            System.loadLibrary("native-lib")    // Then our library
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        glSurfaceView = findViewById(R.id.glSurfaceView)
        fpsTextView = findViewById(R.id.fpsTextView)
        toggleButton = findViewById(R.id.toggleButton)

        // Setup GLSurfaceView
        glSurfaceView.setEGLContextClientVersion(2)
        glRenderer = OpenGLRenderer()
        glSurfaceView.setRenderer(glRenderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        // Setup button click listener
        toggleButton.setOnClickListener {
            isProcessingEnabled = !isProcessingEnabled
            toggleButton.text = if (isProcessingEnabled) "Show Raw Feed" else "Show Processed"
            Toast.makeText(this,
                if (isProcessingEnabled) "Edge Detection ON" else "Edge Detection OFF",
                Toast.LENGTH_SHORT).show()
        }

        // Check camera permission
        if (checkCameraPermission()) {
            initializeCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera()
            } else {
                Toast.makeText(this, "Camera permission required!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun initializeCamera() {
        try {
            // Initialize camera manager
            cameraManager = CameraManager(this) { imageData, width, height, fps ->
                // Process frame
                processFrame(imageData, width, height, fps)
            }

            cameraManager.openCamera()

        } catch (e: Exception) {
            Log.e(TAG, "Error initializing camera: ${e.message}")
            Toast.makeText(this, "Failed to initialize camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processFrame(imageData: ByteArray, width: Int, height: Int, fps: Float) {
        try {
            val startTime = System.currentTimeMillis()

            // Call native processing
            val processedData = if (isProcessingEnabled) {
                Log.d(TAG, "Processing enabled - calling native code")
                processImageNative(imageData, width, height)
            } else {
                Log.d(TAG, "Processing disabled - using raw data")
                imageData // Return raw data
            }

            Log.d(TAG, "Input size: ${imageData.size}, Output size: ${processedData?.size ?: 0}")

            val processingTime = System.currentTimeMillis() - startTime

            // Update OpenGL texture
            glRenderer.updateTexture(processedData, width, height)
            glSurfaceView.requestRender()  // Request a render!

            // Update FPS display
            runOnUiThread {
                fpsTextView.text = "FPS: %.1f | Processing: %dms".format(fps, processingTime)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error processing frame: ${e.message}")
        }
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
        if (::cameraManager.isInitialized) {
            cameraManager.closeCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
        if (checkCameraPermission() && ::cameraManager.isInitialized) {
            cameraManager.openCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraManager.isInitialized) {
            cameraManager.closeCamera()
        }
    }

    // Native method declarations
    private external fun processImageNative(imageData: ByteArray, width: Int, height: Int): ByteArray
}