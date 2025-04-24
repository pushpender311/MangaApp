package com.bittech.manga.ui.face


import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import java.util.concurrent.Executors

@Composable
fun FaceRecognitionScreen(
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    var faces by remember { mutableStateOf(emptyList<RectF>()) }
    var frameBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isFaceInside by remember { mutableStateOf(false) }


    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(
                context = context,
                lifecycleOwner = lifecycleOwner,
                onFrameCaptured = { bitmap ->
                    frameBitmap = bitmap
                },
                onFacesDetected = { detectedFaces ->
                    faces = detectedFaces
                    isFaceInside = detectedFaces.any { face ->
                        REFERENCE_RECT.contains(face.centerX(), face.centerY())
                    }
                }
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                frameBitmap?.let { bmp ->
                    drawImage(bmp.asImageBitmap())

                    // Draw Reference Rectangle
                    drawRect(
                        color = if (isFaceInside) Color.Green else Color.Red,
                        topLeft = androidx.compose.ui.geometry.Offset(REFERENCE_RECT.left, REFERENCE_RECT.top),
                        size = androidx.compose.ui.geometry.Size(
                            REFERENCE_RECT.width(),
                            REFERENCE_RECT.height()
                        ),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
                    )

                    // Optional: Draw detected faces
                    faces.forEach { rect ->
                        drawRect(
                            color = Color.Yellow,
                            topLeft = androidx.compose.ui.geometry.Offset(rect.left, rect.top),
                            size = androidx.compose.ui.geometry.Size(rect.width(), rect.height()),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                        )
                    }
                }
            }
        }
    }
}

private val REFERENCE_RECT = RectF(300f, 600f, 800f, 1300f) // Adjust this according to your design

@Composable
private fun CameraPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onFrameCaptured: (Bitmap) -> Unit,
    onFacesDetected: (List<RectF>) -> Unit
) {
    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder()
            .setTargetResolution(Size(1280, 720))
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        val faceDetector = FaceDetector.createFromOptions(
            context,
            FaceDetector.FaceDetectorOptions.builder()
                .setBaseOptions(
                    BaseOptions.builder()
                        .setModelAssetPath("models/face_detection_short_range.tflite")
                        .build()
                )
                .setMinDetectionConfidence(0.5f)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener { result: FaceDetectorResult, input: MPImage ->
                    onFacesDetected(result.detections().map { detection ->
                        val bbox = detection.boundingBox()
                        RectF(bbox.left.toFloat(), bbox.top.toFloat(), bbox.right.toFloat(), bbox.bottom.toFloat())
                    })
                }
                .setErrorListener { e ->
                    Log.e("FaceDetection", "Error: ", e)
                }
                .build()
        )

        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analyzer ->
                analyzer.setAnalyzer(cameraExecutor) { imageProxy ->
                    val bitmap = imageProxy.toBitmap()
                    bitmap?.let { bmp ->
                        onFrameCaptured(bmp)
                        val mpImage = BitmapImageBuilder(bmp).build()
                        faceDetector.detectAsync(mpImage, System.currentTimeMillis())
                    }
                    imageProxy.close()
                }
            }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, analysis)
        } catch (e: Exception) {
            Log.e("FaceDetection", "Camera binding failed", e)
        }
    }

    AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
}

private fun ImageProxy.toBitmap(): Bitmap? {
    val planeProxy = planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}