package com.example.android.camera2.basic.imageProcessing.depth;

public class DepthStatus {
    private static String depthId = null;
    private static Integer detphFormat = null;


    public static String getDepthId() {
        return depthId;
    }

    public static void setDepthId(String depthId) {
        DepthStatus.depthId = depthId;
    }

    public static Integer getDetphFormat() {
        return detphFormat;
    }

    public static void setDetphFormat(Integer detphFormat) {
        DepthStatus.detphFormat = detphFormat;
    }
}
