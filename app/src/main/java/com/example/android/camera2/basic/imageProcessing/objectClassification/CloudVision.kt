package com.example.android.camera2.basic.imageProcessing.objectClassification

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import com.example.android.camera2.basic.CameraActivity
import com.example.android.camera2.basic.utils.helper.InitializerFactory
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.services.vision.v1.model.*
import com.google.cloud.translate.TranslateOptions
import java.io.ByteArrayOutputStream
import java.io.IOException


class CloudVision(image: Bitmap, PACKAGE_NAME: String, PACKAGE_MANAGER: PackageManager) {

    private var CLOUD_VISION_API_KEY = "AIzaSyDfoRQ6HR6jCUwQT0_ef4Ls21OuIelEMAI"
    private lateinit var image: Bitmap
    private lateinit var mode: String
    private lateinit var cameraActivity: CameraActivity
    private lateinit var PACKAGE_NAME: String
    private lateinit var PACKAGE_MANAGER: PackageManager

    init {
        this.PACKAGE_MANAGER = PACKAGE_MANAGER
        this.PACKAGE_NAME = PACKAGE_NAME
        this.image = image
    }

    fun performAnalyze(mode: String): MutableList<AnnotateImageResponse>? {
        try {
            this.mode = mode
            val httpTransport = AndroidHttp.newCompatibleTransport()
            val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
            val builder = Vision.Builder(httpTransport, jsonFactory, null)
            var requestInitializer: VisionRequestInitializer = InitializerFactory.getInitializer(CLOUD_VISION_API_KEY, PACKAGE_MANAGER, PACKAGE_NAME)

            builder.setVisionRequestInitializer(requestInitializer)
            val visionRequest = builder.build()
            Log.i("cloud", "Vision built")
            val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
            batchAnnotateImagesRequest.requests = listOf(addImageRequest())
            // Add the list of one thing to the request
            Log.i("cloud", "first try completed")
            try {
                val annotateRequest = visionRequest.images().annotate(batchAnnotateImagesRequest)
                annotateRequest.disableGZipContent = true
                Log.d("CloudVision", "created Cloud Vision request object, sending request")
                var response = annotateRequest.execute()
                return convertResponseToString(response)
            } catch (e: GoogleJsonResponseException) {
                Log.d("CloudVision", "failed to make API request because " + e.content)
            } catch (e: IOException) {
                Log.d("CloudVision", "failed to make API request because of other IOException " +
                        e.message)
            }
            return null
        } catch (e: Exception) {
            Log.i("cloud", e.toString())
        }
        return null
    }

    fun translateString(text: String): String? {
        try {
            Log.i("cloud", "Translation built")
            var translate: com.google.cloud.translate.Translate? = TranslateOptions.newBuilder().setApiKey(CLOUD_VISION_API_KEY).build().service
            try {
                var translation = translate?.translate(text, com.google.cloud.translate.Translate.TranslateOption.sourceLanguage("en"), com.google.cloud.translate.Translate.TranslateOption.targetLanguage("de"))
                //annotateRequest.setFields(text)
                return translation?.translatedText
            } catch (e: Exception) {
                Log.i("cloud", e.toString())
            }
        } catch (e: java.lang.Exception) {
            Log.e("ERROR in Translation request", e.toString())
        }
        return null
    }


    private fun convertResponseToString(response: BatchAnnotateImagesResponse): MutableList<AnnotateImageResponse>? {
        if (response.responses[0].isEmpty()) {
            Log.i("cloud", "empty Response")
            return null
        }
        Log.i("cloud", "fullResponse: $response")

        if (mode == Constants.LABEL) {
            if (response.responses[0].labelAnnotations[0].description == "Product" || response.responses[0].labelAnnotations[0].description == "product") {
                return response.responses
            }
        } else if (mode == Constants.LOGO) {
            Log.i("cloud", "Logo response: " + response.responses[0].logoAnnotations[0].description)
            return response.responses
        } else if (mode == Constants.LANDMARK) {
            Log.i("cloud", "Landmark response: " + response.responses[0].landmarkAnnotations[0].description)
            return response.responses
        }
        return response.responses
    }


    fun addImageRequest(): AnnotateImageRequest {
        var test: Image

        var request: AnnotateImageRequest = AnnotateImageRequest()
        var base64EncodedImage: Image = Image()
        var byteArrayOutputStream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        base64EncodedImage.encodeContent(imageBytes)
        request.image = base64EncodedImage
        var feature = Feature()
        feature.type = mode
        feature.maxResults = 2
        request.features = listOf(feature)
        return request
    }

}



