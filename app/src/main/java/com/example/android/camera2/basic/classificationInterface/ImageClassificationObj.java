package com.example.android.camera2.basic.classificationInterface;

import android.graphics.Bitmap;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageClassificationObj {

    private static CloudVision vision = null;
    private static List<String> labels = null;

    // TODO neue Metainformation in neuem Objekt mit Objektinformationen zusammenfassen
    //------------------------------------------------------------------
    //private static Map<UUID, ImageObjectInformation> = null
    private static List<AnnotateImageResponse> localization= null;
    private static Integer angle = null;
    private static String objectLocalString = null;
    //------------------------------------------------------------------

    private Map<UUID, ImageClassificationObj> imageClassificationObjMap;

    private static Map<String, Integer> anglesMap = null;
    private static Map<String, String> angleDescription = null;

    // TODO image Objekt
    private static Bitmap bitmap= null;
    private static Integer exifOrientation = null;

    private static List<AnnotateImageResponse> textRecognition= null;



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


    public static List<AnnotateImageResponse> getTextRecognition() {
        return textRecognition;
    }

    public static void setTextRecognition(List<AnnotateImageResponse> textRecognition) {
        ImageClassificationObj.textRecognition = textRecognition;
    }



    public static void clean() {
        bitmap = null;
        labels = null;
        localization = null;
        textRecognition = null;
        angle = null;
        objectLocalString = null;
        angleDescription = null;
        anglesMap = null;
        exifOrientation = null;
    }

    public static CloudVision getVision() {
        return vision;
    }

    public static void setVision(CloudVision vision) {
        ImageClassificationObj.vision = vision;
    }

    public static Integer getAngle() {
        return angle;
    }

    public static void setAngle(Integer angle) {
        ImageClassificationObj.angle = angle;
    }

    public static String getObjectLocalString() {
        return objectLocalString;
    }

    public static void setObjectLocalString(String objectLocalString) {
        ImageClassificationObj.objectLocalString = objectLocalString;
    }

    public static Map<String, Integer> getAnglesMap() {
        return anglesMap;
    }

    public static void setAngleMap(String name, Integer angle) {
        if (anglesMap == null){
            anglesMap = new LinkedHashMap<String, Integer>(){{
                put(name, angle);
            }};
        }else {
            anglesMap.put(name, angle);
        }
    }

    public static Map<String, String> getAngleDescription() {
        return angleDescription;
    }

    public static void setAngleDesc(String name, String objectLocalString) {
        if (angleDescription == null){
            angleDescription = new LinkedHashMap<String, String>(){{
                put(name, objectLocalString);
            }};
        }else {
            angleDescription.put(name, objectLocalString);
        }
    }

    public static Integer getExifOrientation() {
        return exifOrientation;
    }

    public static void setExifOrientation(Integer exifOrientation) {
        ImageClassificationObj.exifOrientation = exifOrientation;
    }
}
