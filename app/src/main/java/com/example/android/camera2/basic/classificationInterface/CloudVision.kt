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
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.services.vision.v1.model.*
import java.io.IOException


class CloudVision(main: CameraActivity, image: Image, mode: String) : AsyncTask<Object, Void, String>() {
    private var main: CameraActivity? = null
    private var CLOUD_VISION_API_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDhSGNBv7BdZe1\\ncwiAAQ6t5xToCdwBCtLu1y7As/rP+wa+qQl37J8SrAL5l8kmj/7XpGh1bcRjp9uA\\nDyiCqFImVTtGqenm+qOy+TQaZNYkskSvA3yVW2HMoOhXItka4C5nrPSdyQTM2OGw\\npjbvsyrKMv9kUh4Bx5yPVgevvr7OTFC8V1P5yT6UQ543eLRlWkvjfL1YYGDNbgVf\\n4dzEqY8KRDQw6LIL2r0paliKP7TQV85PF70wlNgRDZTnV87jeMPZjbcZBRLE535I\\nPlHyKiEAlYMMai6WRw/6xq4VUG/dEE+LWCFYY69bER/qAAVTX6LHfWZdwJ29EGpu\\nkXDk5vsVAgMBAAECggEAGwQIvENUrSeR9FT2PjWnMRlGdr9yAkTcG3tpLuBPjjBZ\\n4LtbnxH3cu2IpbM27Jil9mb1thAaPEjj2ACAMPmQDFLnk16D/tHwD5lGfUUkn8Jw\\nIhyhuMN9MnijUfLzO7bxQosP68NsYd+v46g73AgOKA0+475C/iz2MYoKGtsI41lD\\nVIxp+zQoxxfsxwShdVMUSmHlDWXJ0GpjO4ayE5PfC9y/Jfh2vXJmt+RNtNPhbDN0\\n7+YnvPbFj2zeSLMazNDNhU8KJ7jkVK0y5zeMhhKvuyZKWaGuY1opZa1aOI0ao5rt\\nO1CA8xvBDSOb/d23WxwvFch67jKZP8s2yVZFnI0vywKBgQDhNdgsy4Zsk6axKDYw\\nQ7mlcR+ZAr8raq/AdcdBHJIVP5RzY6M+XIqfNKWcFL5x+r4DmXABMbfx1QyI36Mg\\nIX4+Ojwz09kMrDGNxncTWMuQJpLOSbmNvq9Gm+Tr/7bZ8YmZibjqGsLYP4qKZbv3\\n4z7h5mt0QFwz5MwqtQlwaTwlWwKBgQDeQCdcAAVySWq8b8bOiiB/nmrlrf4R4wb9\\naFw79F2uWnljsCZgiMp6wjTv/eCjKZg7qrnofUW9Bm8q1BZ0cFFF/07QYjfada0W\\ns8Z9EpXwObfq4saQui0sRzZ1UdH6p5dd3hDMMVpxc4ML7T2zF3Ul+VovGkDhIjGo\\nt+6uLfycTwKBgQCJNv9BO7fpS4TSh4eMnJbt4CC6X1wOne/7OUdvunKfE5/lNh3u\\nDwA+xBrrIBBw7a78Dm6Zq6tBYudCNc/z8bQzQdQQV9D00a1XjkZauU09xOLJYU32\\nuOmeAbnWuHS2EV4e+DR8HlX836oPbLC79e8IQBXUPKpwy8RBeRAJN3T35QKBgHSP\\nStUqQbD9phfru2Vo9cBYkhGhHeW9nlXanLzo3RTq6E0K/iWUuDSHlAHlsSGBWBC6\\n6kNvJ9sJ+9WHY7tviIBgdLI/QLG3E68bW9cOn0pcywNKKf+PVM+rDXmcDrcZm/4j\\nz8V3gMqNXUYtBzXc8JiY3N5lM2+fYlHtHSWGgrxrAoGAYl8eQxBJaNAhvGeAgSip\\nUCZrp/a5CzaWv+XWqySKPQrmY8qXaLK2u69j8K1tZSkjAx3CbfkMdQ66wROkAK/o\\n1C0x8YYzFotnkTqqFkKEXP0nxZ9KCkwTGlUxb6Wu9DxyzFWg2/Pr1zTUj1OFJcm+\\n6Vu87JtvQBVkvGloc4MK2V0="
    private var image: Image? = null
    private var mode: String? = null


    private val ANDROID_CERT_HEADER = "X-Android-Cert"
    private val ANDROID_PACKAGE_HEADER = "X-Android-Package"

    init {
        this.main = main
        this.image = image
        this.mode = mode
        Log.d("starting process: ", "Init")
    }

    fun performAnalyze(): String? {
        try {
            Log.d("starting process: ", " Cloud Vision Access")
            var httpTransport: HttpTransport = AndroidHttp.newCompatibleTransport()
            var jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

            var builder: Vision.Builder = Vision.Builder(httpTransport, jsonFactory, null)
            var requestInitializer = VisionRequestInitializer(CLOUD_VISION_API_KEY) // TODO repair later InitializerFactory.getInitializer(CLOUD_VISION_API_KEY, main)
            builder.setVisionRequestInitializer(requestInitializer)
            var vision: Vision = builder.build()
            var batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
            batchAnnotateImagesRequest.setRequests(listOf(addImageRequest()))
            Log.d( "Cloudvision", "we reach this here?")
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

    override fun doInBackground(vararg p0: Object?): String? {
        try {
            Log.d("starting process: ", " Cloud Vision Access")
            var httpTransport: HttpTransport = AndroidHttp.newCompatibleTransport()
            var jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

            var builder: Vision.Builder = Vision.Builder(httpTransport, jsonFactory, null)
            var requestInitializer = VisionRequestInitializer(CLOUD_VISION_API_KEY) // TODO repair later InitializerFactory.getInitializer(CLOUD_VISION_API_KEY, main)
            builder.setVisionRequestInitializer(requestInitializer)
            var vision: Vision = builder.build()
            var batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
            batchAnnotateImagesRequest.setRequests(listOf(addImageRequest()))
            Log.d( "Cloudvision", "we reach this here?")
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

