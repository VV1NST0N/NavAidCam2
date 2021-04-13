package com.example.android.camera2.basic.imageProcessing.objectClassification;

import com.google.api.services.vision.v1.model.LocalizedObjectAnnotation;

public class ObjectMetaInformation {

    private LocalizedObjectAnnotation localizedObjectAnnotation;
    private Double angle;
    private String objectLocalString;
    private Double score;
    ObjectMetaInformation(Double angle, String objectLocalString, LocalizedObjectAnnotation annotateImageResponse){
        this.angle = angle;
        this.localizedObjectAnnotation = annotateImageResponse;
        this.objectLocalString = objectLocalString;
    }

    public LocalizedObjectAnnotation getLocalizedObjectAnnotation() {
        return localizedObjectAnnotation;
    }

    public void setLocalizedObjectAnnotation(LocalizedObjectAnnotation localizedObjectAnnotation) {
        this.localizedObjectAnnotation = localizedObjectAnnotation;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public String getObjectLocalString() {
        return objectLocalString;
    }

    public void setObjectLocalString(String objectLocalString) {
        this.objectLocalString = objectLocalString;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
