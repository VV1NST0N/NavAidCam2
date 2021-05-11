package com.example.android.camera2.basic.utils.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.util.Log;

import com.example.android.camera2.basic.fragments.CameraFragment;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class BitmapUtil {
    public static Bitmap getBitmapFromJPG(String absolutePath){
        Bitmap map = BitmapFactory.decodeFile(absolutePath);
        return map;
    }
}
