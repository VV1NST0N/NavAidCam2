package com.example.android.camera2.basic.imageProcessing.bitmapManipulation

import android.graphics.*
import com.google.api.services.vision.v1.model.NormalizedVertex

class BitmapBoundingBox {

    fun drawRectangleAroundObject(local: MutableList<NormalizedVertex>, mutableBitmap: Bitmap): Bitmap {
        val canvas = Canvas(mutableBitmap)
        Paint().apply {
            color = Color.RED
            isAntiAlias = true
            strokeWidth = 20f
            style = Paint.Style.STROKE
            val wallpath = Path()
            wallpath.reset()
            wallpath.moveTo(local[0].x * mutableBitmap.width, local[0].y * mutableBitmap.height)
            wallpath.lineTo(local[1].x * mutableBitmap.width, local[1].y * mutableBitmap.height)
            wallpath.lineTo(local[2].x * mutableBitmap.width, local[2].y * mutableBitmap.height)
            wallpath.lineTo(local[3].x * mutableBitmap.width, local[3].y * mutableBitmap.height)
            wallpath.lineTo(local[0].x * mutableBitmap.width, local[0].y * mutableBitmap.height)
            canvas.drawPath(
                    wallpath,
                    this
            )
        }
        return mutableBitmap
    }
}