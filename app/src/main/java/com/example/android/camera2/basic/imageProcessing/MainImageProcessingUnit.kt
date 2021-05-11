package com.example.android.camera2.basic.imageProcessing

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import com.example.android.camera2.basic.CameraActivity
import com.example.android.camera2.basic.fragments.CameraFragment
import com.example.android.camera2.basic.imageProcessing.bitmapManipulation.BitmapDrawing
import com.example.android.camera2.basic.imageProcessing.objectClassification.CloudVision
import com.example.android.camera2.basic.imageProcessing.objectClassification.Constants
import com.example.android.camera2.basic.imageProcessing.objectClassification.ImageClassificationObj
import com.example.android.camera2.basic.utils.helper.BitmapUtil
import com.google.api.services.vision.v1.model.AnnotateImageResponse
import java.io.File

class MainImageProcessingUnit {
    var bitmapProcesser: BitmapDrawing = BitmapDrawing()

    fun classifyImage(resultFile: File): Bitmap? {

        var bitMap = BitmapUtil.getBitmapFromJPG(resultFile.absolutePath)
        val matrix = Matrix()
        matrix.setRotate(90f)
        bitMap = Bitmap.createBitmap(bitMap, 0, 0, bitMap.width, bitMap.height, matrix, true)

        var cloudVision: CloudVision = CloudVision(bitMap, CameraActivity.PACKAGE_NAME, CameraActivity.PACKAGE_MANAGER as PackageManager)
        ImageClassificationObj.setVision(cloudVision)
        var label: MutableList<AnnotateImageResponse>? = cloudVision.performAnalyze(mode = Constants.LABEL)
        var localization: MutableList<AnnotateImageResponse>? = cloudVision.performAnalyze(mode = Constants.OBJECT)
        var labels: MutableList<String> = mutableListOf()
        var read = false
        if (label != null) {
            for (result in label!!) {
                for (label in result.labelAnnotations) {
                    if ((label.description.equals("Text") || label.description.equals("Font") || label.description.equals("Signage") || label.description.equals("Advertising") || label.description.equals("Sign")) && read == false) {
                        var textRecognitionResult: MutableList<AnnotateImageResponse>? = cloudVision.performAnalyze(mode = Constants.TEXT)
                        ImageClassificationObj.setTextRecognition(textRecognitionResult)
                        read = true
                    }
                    labels.add("Object: ${label.description} Score: ${label.score} \n")
                }
            }
        } else {
            labels.add("Keine Labels erkannt")
        }
        // TODO use EXIF information
        if (localization != null) {
            bitMap = bitmapProcesser.processObjectOrientations(bitMap, localization)
            bitMap = bitmapProcesser.drawHighestAccurracyObjectsInfoOnImage(bitMap, localization)
        }

        ImageClassificationObj.setLabels(labels)
        ImageClassificationObj.setBitmap(bitMap)

        return bitMap
    }

    fun reciveDepth(result: CameraFragment.Companion.CombinedCaptureResult): Bitmap? {
        var depthInformationObj = bitmapProcesser.createDepthImage(result.image)
        ImageClassificationObj.setDepthInformationObj(depthInformationObj)
        return depthInformationObj?.depthMap
    }

    fun reciveCombinedBitmap(): Bitmap {
        var combinedBitmap = bitmapProcesser.mergeDepthAndPicture()!!
        ImageClassificationObj.setCombinedBitmap(combinedBitmap)
        return combinedBitmap
    }


}