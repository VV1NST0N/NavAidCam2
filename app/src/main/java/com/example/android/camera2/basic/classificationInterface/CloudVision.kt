package com.example.android.camera2.basic.classificationInterface

import android.os.AsyncTask
import android.util.Log
import com.example.android.camera2.basic.CameraActivity
import com.example.android.camera2.basic.classificationInterface.helper.InitializerFactory
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.model.*
import java.io.IOException


class CloudVision(main: CameraActivity, image: Image, mode: String) : AsyncTask<Object, Void, String>() {
    private var main: CameraActivity? = null
    private var CLOUD_VISION_API_KEY = ""
    private var image: Image? = null
    private var mode: String? = null


    private val ANDROID_CERT_HEADER = "X-Android-Cert"
    private val ANDROID_PACKAGE_HEADER = "X-Android-Package"

    init {
        this.main = main
        this.image = image
        this.mode = mode
    }

    override fun doInBackground(vararg p0: Object?): String? {
        try {
            Log.i("starting process: ", " Cloud Vision Access")
            var httpTransport: HttpTransport = AndroidHttp.newCompatibleTransport()
            var jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

            var builder: Vision.Builder = Vision.Builder(httpTransport, jsonFactory, null)
            var requestInitializer = InitializerFactory.getInitializer(CLOUD_VISION_API_KEY, main)
            builder.setVisionRequestInitializer(requestInitializer)
            var vision: Vision = builder.build()
            var batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
            batchAnnotateImagesRequest.setRequests(listOf(addImageRequest()))

            try{
                var annotateRequest : Vision.Images.Annotate = vision.images().annotate(batchAnnotateImagesRequest)

                // Due to a bug: requests to Vision API containing large images fail when GZipped.
                annotateRequest.disableGZipContent = true
                val response : BatchAnnotateImagesResponse = annotateRequest.execute()
                return  convertResponseToString(response)
            }catch ( e: IOException) {
                Log.d("CloudVision", "failed to make API request because of other IOException " +
                        e.message);
            }
        } catch (e: Exception) {
            Log.e("cloud acces ERROR", e.toString())
        }
        return null!!
    }

    private fun convertResponseToString(response: BatchAnnotateImagesResponse): String? {
        // TODO: whooh this is bad. rework everything to not be hardcoded.
        if (response.responses[0].isEmpty()) {
            Log.i("cloud", "empty Response")
            return Constants.NORESPONSE
        }
        Log.i("cloud", "fullResponse: $response")
        //TODO If result is "product" then ignore the shit.

        // Todo Have to include more alternatives, top one is often bad. I think photo quality
        // has alot to do with it. (Not sure)

        // TODO: If lower than [treshold] (e.g. 0.75) notify user that this is incorrect and tell him to focus and hold camera steady.
        if (mode == Constants.LABEL) {
            if (response.responses[0].labelAnnotations[0].description == "Product" || response.responses[0].labelAnnotations[0].description == "product") {
                return response.responses[0].labelAnnotations[1].description
            }
        } else if (mode == Constants.LOGO) {
            Log.i("cloud", "Logo response: " + response.responses[0].logoAnnotations[0].description)
            return response.responses[0].logoAnnotations[0].description
        } else if (mode == Constants.LANDMARK) {
            Log.i("cloud", "Landmark response: " + response.responses[0].landmarkAnnotations[0].description)
            return response.responses[0].landmarkAnnotations[0].description
        }
        return response.responses[0].labelAnnotations[0].description
    }

    fun addImageRequest(): AnnotateImageRequest {
        var request: AnnotateImageRequest = AnnotateImageRequest()
        /*var base64EncodedImage: Image = Image()
        var byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes)*/
        request.image = image
        // adding Features
        // TODO experiment
        var feature = Feature()
        feature.type = mode
        feature.maxResults = 2
        request.features = listOf(feature)
        return request
    }

}

