package com.example.android.camera2.basic.imageProcessing.objectClassification;

import android.graphics.Bitmap;

import com.example.android.camera2.basic.imageProcessing.depth.DepthStatus;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.LocalizedObjectAnnotation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageClassificationObj {

    private static CloudVision vision = null;
    private static List<String> labels = null;
    private static Map<UUID, ObjectMetaInformation> objectMetaInformationMap = null;
    private static Bitmap bitmap = null;
    private static Bitmap combinedBitmap = null;
    private static Integer exifOrientation = null;
    private static DepthInformationObj depthInformationObj = null;
    private static List<AnnotateImageResponse> textRecognition = null;

    public static List<String> getLabels() {
        return labels;
    }

    public static void setLabels(List<String> labels) {
        ImageClassificationObj.labels = labels;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        ImageClassificationObj.bitmap = bitmap;
    }

    public static List<AnnotateImageResponse> getTextRecognition() {
        return textRecognition;
    }

    public static void setTextRecognition(List<AnnotateImageResponse> textRecognition) {
        ImageClassificationObj.textRecognition = textRecognition;
    }

    public static CloudVision getVision() {
        return vision;
    }

    public static void setVision(CloudVision vision) {
        ImageClassificationObj.vision = vision;
    }

    public static Integer getExifOrientation() {
        return exifOrientation;
    }

    public static void setExifOrientation(Integer exifOrientation) {
        ImageClassificationObj.exifOrientation = exifOrientation;
    }

    public static void addToMetaInformationMap(Double angle, String objectLocalString, LocalizedObjectAnnotation annotateImageResponse) {
        if (objectMetaInformationMap == null) {
            objectMetaInformationMap = new LinkedHashMap<UUID, ObjectMetaInformation>();
        }
        objectMetaInformationMap.put(UUID.randomUUID(), new ObjectMetaInformation(angle, objectLocalString, annotateImageResponse));
    }

    public static Map<UUID, ObjectMetaInformation> getImageClassificationObjMap() {
        return objectMetaInformationMap;
    }

    public static Bitmap getCombinedBitmap() {
        return combinedBitmap;
    }

    public static void setCombinedBitmap(Bitmap combinedBitmap) {
        ImageClassificationObj.combinedBitmap = combinedBitmap;
    }

    public static DepthInformationObj getDepthInformationObj() {
        return depthInformationObj;
    }

    public static void setDepthInformationObj(DepthInformationObj depthInformationObj) {
        ImageClassificationObj.depthInformationObj = depthInformationObj;
    }

    public static void clean(Integer format) {
        if (bitmap != null && depthInformationObj != null) {
            bitmap = null;
            labels = null;
            textRecognition = null;
            exifOrientation = null;
            combinedBitmap = null;
            depthInformationObj = null;
            objectMetaInformationMap = null;
        } else if (bitmap != null && !format.equals(DepthStatus.getDetphFormat())) {
            bitmap = null;
            labels = null;
            textRecognition = null;
            exifOrientation = null;
            combinedBitmap = null;
            depthInformationObj = null;
            objectMetaInformationMap = null;
        } else if (depthInformationObj != null && format.equals(DepthStatus.getDetphFormat())) {
            depthInformationObj = null;
        }
    }

}
