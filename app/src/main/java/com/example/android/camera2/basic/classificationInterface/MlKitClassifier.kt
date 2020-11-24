package com.example.android.camera2.basic.classificationInterface

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MlKitClassifier : ImageAnalysis.Analyzer {

        @SuppressLint("UnsafeExperimentalUsageError")
        fun analyze(mediaImage: Image, orientation: Int) {
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, 0)
                val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                labeler.process(image)
                        .addOnSuccessListener { labels ->
                            // Task completed successfully
                            // ...
                            Log.d("Class Success", labels.toString())
                        }
                        .addOnFailureListener { e ->
                            // Task failed with an exception
                            // ...
                            Log.d("Class Faliure", e.toString())
                        }
                // Pass image to an ML Kit Vision API
                // ...
            }
        }

    override fun analyze(image: ImageProxy) {
        TODO("Not yet implemented")
    }

}