package com.bittech.manga.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector.FaceDetectorOptions
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult

class FaceDetectionHelper(
    context: Context,
    private val onSuccess: (FaceDetectorResult) -> Unit,
    private val onFailure: (Throwable) -> Unit
) {

    private val detector: FaceDetector

    init {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("models/face_detector_short_range.tflite")
            .build()


        val faceDetectorOptions = FaceDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.LIVE_STREAM)
            .setMinDetectionConfidence(0.5f)
            .setResultListener { result: FaceDetectorResult, input: MPImage ->
                Log.d("FaceDetection", "Faces detected: ${result.detections().size}")
            }
            .setErrorListener { e ->
                Log.e("FaceDetection", "Face detection error", e)
            }
            .build()

        detector = FaceDetector.createFromOptions(context, faceDetectorOptions)
    }

    fun detectFaces(image: MPImage, timestampMs: Long) {
        detector.detectAsync(image, timestampMs)
    }

    fun imageProxyToMpImage(imageProxy: ImageProxy): MPImage {
        val bitmap = imageProxyToBitmap(imageProxy)
        return BitmapImageBuilder(bitmap).build()
    }

    private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
        val yBuffer = imageProxy.planes[0].buffer
        val uBuffer = imageProxy.planes[1].buffer
        val vBuffer = imageProxy.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(
            nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null
        )

        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val yuv = out.toByteArray()
        return android.graphics.BitmapFactory.decodeByteArray(yuv, 0, yuv.size)
    }
}
