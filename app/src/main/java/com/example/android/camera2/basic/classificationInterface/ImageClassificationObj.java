package com.example.android.camera2.basic.classificationInterface;

import android.graphics.Bitmap;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;

import java.util.List;

public class ImageClassificationObj {
    private static List<String> labels;
    private static List<AnnotateImageResponse> localization;
    private static Bitmap bitmap;

    public static List<String> getLabels() {
        return labels;
    }

    public static void setLabels(List<String> labels) {
        ImageClassificationObj.labels = labels;
    }

    public static List<AnnotateImageResponse> getLocalization() {
        return localization;
    }

    public static void setLocalization(List<AnnotateImageResponse> localization) {
        ImageClassificationObj.localization = localization;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        ImageClassificationObj.bitmap = bitmap;
    }


}
