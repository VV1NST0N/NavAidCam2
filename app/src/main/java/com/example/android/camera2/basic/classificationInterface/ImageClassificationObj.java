package com.example.android.camera2.basic.classificationInterface;

import android.graphics.Bitmap;

import com.example.android.camera2.basic.fragments.CameraFragment;

import java.util.List;

public class ImageClassificationObj {
    private static CameraFragment.Companion.CombinedCaptureResult captureResult;

    public static CameraFragment.Companion.CombinedCaptureResult getCaptureResult() {
        return captureResult;
    }

    public static void setCaptureResult(CameraFragment.Companion.CombinedCaptureResult captureResult) {
        ImageClassificationObj.captureResult = captureResult;
    }
}
