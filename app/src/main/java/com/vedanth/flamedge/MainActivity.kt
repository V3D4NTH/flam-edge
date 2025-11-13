package com.vedanth.flamedge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var textureView: TextureView
    private lateinit var cameraManager: CameraManager
    private lateinit var glRenderer: OpenGLRenderer
    private lateinit var fpsTextView: TextView
    private lateinit var toggleButton: Button

    private var isProcessingEnabled = true
    private val CAMERA_PERMISSION_REQUEST = 100

    companion object {
        private const val TAG = "MainActivity"
        // test comment to remove vedanth4gd creds
        // Load native library
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        textureView = findViewById(R.id.textureView)
        fpsTextView = findViewById(R.id.fpsTextView)
        toggleButton = findViewById(R.id.toggleButton)

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
            // Initialize OpenGL renderer
            glRenderer = OpenGLRenderer(textureView)

            // Initialize camera manager
            cameraManager = CameraManager(this, textureView) { imageData, width, height, fps ->
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
                processImageNative(imageData, width, height)
            } else {
                imageData // Return raw data
            }

            val processingTime = System.currentTimeMillis() - startTime

            // Update OpenGL texture
            glRenderer.updateTexture(processedData, width, height)

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
        cameraManager.closeCamera()
    }

    override fun onResume() {
        super.onResume()
        if (checkCameraPermission() && ::cameraManager.isInitialized) {
            cameraManager.openCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraManager.isInitialized) {
            cameraManager.closeCamera()
        }
        if (::glRenderer.isInitialized) {
            glRenderer.release()
        }
    }

    // Native method declarations
    private external fun processImageNative(imageData: ByteArray, width: Int, height: Int): ByteArray
}