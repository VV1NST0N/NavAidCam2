package com.example.android.camera2.basic.imageProcessing.bitmapManipulation

import android.graphics.*
import android.media.Image
import com.example.android.camera2.basic.imageProcessing.depth.DepthMap
import com.example.android.camera2.basic.imageProcessing.objectClassification.DepthInformationObj
import com.example.android.camera2.basic.imageProcessing.objectClassification.ImageClassificationObj
import com.google.api.services.vision.v1.model.AnnotateImageResponse


class BitmapDrawing {

    private var angleCalculation : BitmapAngle = BitmapAngle()
    private var boundingBox: BitmapBoundingBox = BitmapBoundingBox()
    private var depthProcessor = DepthMap()


    fun processObjectOrientations(bitmap: Bitmap, location: MutableList<AnnotateImageResponse>?): Bitmap? {
       var mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        // TODO rotate Image
        // TODO check why we take only first entry from list
        var localObjs = location?.get(0)?.localizedObjectAnnotations

        for (obj in localObjs!!) {
            mutableBitmap = boundingBox.drawRectangleAroundObject(obj.boundingPoly.normalizedVertices, mutableBitmap)!!
            var listOfPositions = angleCalculation.calculateImagePositions(obj.boundingPoly.normalizedVertices, mutableBitmap)
            var angleResultList = angleCalculation.calcDegrees(listOfPositions[0], listOfPositions[1], listOfPositions[2], listOfPositions[3], listOfPositions[4], listOfPositions[5])
            mutableBitmap = angleCalculation.drawVector(listOfPositions[0], listOfPositions[1], listOfPositions[2], listOfPositions[3], listOfPositions[4], listOfPositions[5], mutableBitmap)
            // TODO add to MetaInformation
            ImageClassificationObj.addToMetaInformationMap(angleResultList[0].toDouble(), angleResultList[1], obj)

        }
        return mutableBitmap
    }



    fun createDepthImage(image: Image): DepthInformationObj? {
        return drawDepthInfoOnImage(depthProcessor.checkImageDepth(image))
        //return depthProcessor.checkImageDepth(image)
    }

    fun drawDepthInfoOnImage(depthInformation: DepthInformationObj): DepthInformationObj {
        var bitmap = depthInformation.depthMap
        val canvas = Canvas(bitmap)
        Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            this.color = Color.RED
            this.textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("Max: ${depthInformation.maxZ}mm Min: ${depthInformation.minZ}mm",20f,20f,this)
            canvas.drawText("Avg: ${depthInformation.avg}mm Median: ${depthInformation.median}",20f,bitmap.height-20f,this)
        }
        depthInformation.depthMap = bitmap
        return depthInformation
    }

    fun mergeDepthAndPicture(): Bitmap? {
        // TODO find a way to use this
        //val metrics: DisplayMetrics = getBaseContext().getResources().getDisplayMetrics()
        //val width = metrics.widthPixels
        //val height = metrics.heightPixels
        var bitmap = ImageClassificationObj.getBitmap()
        var depthBitmap = ImageClassificationObj.getDepthInformationObj().depthMap
        var height = bitmap.height
        var width = bitmap.width
        var combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        combinedBitmap = combinedBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(combinedBitmap)
        val dest1 = Rect(0, 0, width, height) // left,top,right,bottom
        canvas.drawBitmap(bitmap!!, null, dest1, null)
        val dest2 = Rect(0, height - 400 / 2, width, height)
        canvas.drawBitmap(depthBitmap!!, null, dest2, null)
        return combinedBitmap
    }

}