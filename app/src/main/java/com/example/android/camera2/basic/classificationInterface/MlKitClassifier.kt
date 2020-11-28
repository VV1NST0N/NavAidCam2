package com.example.android.camera2.basic.classificationInterface

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.PredefinedCategory
import java.util.*

class MlKitClassifier {

    @SuppressLint("UnsafeExperimentalUsageError")
    fun analyzeImage(mediaImage: Image, orientation: Int): MutableList<String> {
        val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()  // Optional
                .build()
        val objectDetector = ObjectDetection.getClient(options)
        var results: MutableList<String> = mutableListOf<String>()

        if (mediaImage != null) {
            var image = InputImage.fromMediaImage(mediaImage, 0)
            var labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image).addOnSuccessListener { lables ->
                // Task completed successfully
                // ...
                lables.forEach { label ->
                    val text = label.text
                    val index = label.index
                    val confidence = label.confidence

                    var stringResult = "Objekt: $text Wahrscheinlichkeit: $confidence"
                    results.add(stringResult)
                    Log.d("ML Kit: ", text)
                    Log.d("ML Kit: ", confidence.toString())
                    Log.d("ML Kit: ", index.toString())
                }
            }.addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                results.add("ERROR")
                Log.d("Class Faliure", e.message)
            }
            // Pass image to an ML Kit Vision API
            // ...
        }

    return results
}

}