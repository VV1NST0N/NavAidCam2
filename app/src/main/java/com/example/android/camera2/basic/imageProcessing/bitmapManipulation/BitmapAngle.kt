package com.example.android.camera2.basic.imageProcessing.bitmapManipulation

import android.graphics.*
import android.util.Log
import com.google.api.services.vision.v1.model.NormalizedVertex
import java.text.DecimalFormat
import kotlin.math.acos
import kotlin.math.sqrt

class BitmapAngle {

    fun drawVector(xCenter: Float, yCenter: Float, xMid: Float, yMid: Float, xObject: Float, yObject: Float, mutableBitmap: Bitmap): Bitmap {
        val canvas = Canvas(mutableBitmap)
        Paint().apply {
            color = Color.RED
            strokeWidth = 20F
            style = Paint.Style.STROKE
            val wallpath = Path()
            wallpath.reset()
            wallpath.moveTo((xCenter), (yCenter))
            wallpath.lineTo(xMid, yMid)

            wallpath.moveTo((xCenter), (yCenter))
            wallpath.lineTo(xObject, (yObject))
            canvas.drawPath(
                    wallpath,
                    this
            )
        }
        return mutableBitmap
    }

    fun calcDegrees(xCenter: Float, yCenter: Float, xMid: Float, yMid: Float, xObject: Float, yObject: Float): MutableList<String> {
        var distCenterMid = sqrt((yMid - yCenter) * (yMid - yCenter) + (xMid - xCenter) * (xMid - xCenter)).toBigDecimal()
        var distCenterObject = sqrt((yObject - yCenter) * (yObject - yCenter) + (xObject - xCenter) * (xObject - xCenter)).toBigDecimal()
        var distMidObject = sqrt((yObject - yMid) * (yObject - yMid) + (xObject - xMid) * (xObject - xMid)).toBigDecimal()
        var angle = Math.toDegrees(acos((((distCenterMid * distCenterMid) + (distCenterObject * distCenterObject) - (distMidObject * distMidObject)) / (2.toBigDecimal() * distCenterMid * distCenterObject)).toDouble()))
        Log.i("INFO: ", "Angle with $angle degrees found.")
        if(xObject < xMid){
            angle = 360 - angle
        }
        var displayLocal = ""
        when (angle.toInt()) {
            in 0..44 -> displayLocal = "Oben"
            in 45..67 -> displayLocal = "Oben Rechts"
            in 68..134 -> displayLocal = "Rechts"
            in 135..157 -> displayLocal = "Unten Rechts"
            in 158..224 -> displayLocal = "Unten"
            in 225..246 -> displayLocal = "Unten Links"
            in 247..314 -> displayLocal = "Links"
            in 315..336 -> displayLocal = "Oben Links"
            in 337..360 -> displayLocal = "Oben"
        }
        return mutableListOf(angle.toString(), displayLocal)
    }

    fun calculateImagePositions(local: MutableList<NormalizedVertex>, mutableBitmap: Bitmap): MutableList<Float> {
        var centerObjectX = 0F
        var centerObjectY = 0F
        if (local[0].x < local[1].x) {
            centerObjectX = local[0].x * mutableBitmap.width + (local[1].x * mutableBitmap.width - local[0].x * mutableBitmap.width) / 2
        } else if (local[0].x < local[2].x) {
            centerObjectX = local[0].x * mutableBitmap.width + (local[2].x * mutableBitmap.width - local[0].x * mutableBitmap.width) / 2
        } else if (local[0].x > local[1].x) {
            centerObjectX = local[1].x * mutableBitmap.width + (local[0].x * mutableBitmap.width - local[0].x * mutableBitmap.width) / 2
        } else if (local[0].x > local[2].x) {
            centerObjectX = local[2].x * mutableBitmap.width + (local[0].x * mutableBitmap.width - local[0].x * mutableBitmap.width) / 2
        }
        if (local[0].y < local[1].y) {
            centerObjectY = local[0].y * mutableBitmap.height + (local[1].y * mutableBitmap.height - local[0].y * mutableBitmap.height) / 2
        } else if (local[0].y < local[2].y) {
            centerObjectY = local[0].y * mutableBitmap.height + (local[2].y * mutableBitmap.height - local[0].y * mutableBitmap.height) / 2
        } else if (local[0].y > local[1].y) {
            centerObjectY = local[1].y * mutableBitmap.height + (local[0].y * mutableBitmap.height - local[0].y * mutableBitmap.height) / 2
        } else if (local[0].y > local[2].y) {
            centerObjectY = local[2].y * mutableBitmap.height + (local[0].y * mutableBitmap.height - local[0].y * mutableBitmap.height) / 2
        }
        var df = DecimalFormat("0.000")
        var xCenter = mutableBitmap.width / 2F
        var yCenter = mutableBitmap.height / 2F

        var xObject = centerObjectX
        var yObject = centerObjectY
        var xMid = mutableBitmap.width / 2F
        var yMid = 0f

        return mutableListOf<Float>(xCenter, yCenter, xMid, yMid, xObject, yObject)
    }
}