package com.example.android.camera2.basic.imageProcessing.depth;

import android.graphics.Bitmap;

public class DepthInformationObj {

    private Bitmap depthMap;
    private Integer minZ;
    private Integer maxZ;
    private Float avg;
    private Float boundingAvg;
    private Float median;

    public DepthInformationObj(Bitmap depthMap, Integer minZ, Integer maxZ, Float avg, Float boundingAvg, float median){
        this.depthMap = depthMap;
        this.maxZ = maxZ;
        this.minZ = minZ;
        this.avg = avg;
        this.boundingAvg = boundingAvg;
        this.median = median;
    }


    public Bitmap getDepthMap() {
        return depthMap;
    }

    public void setDepthMap(Bitmap depthMap) {
        this.depthMap = depthMap;
    }

    public Integer getMinZ() {
        return minZ;
    }

    public void setMinZ(Integer minZ) {
        this.minZ = minZ;
    }

    public Integer getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(Integer maxZ) {
        this.maxZ = maxZ;
    }

    public Float getAvg() {
        return avg;
    }

    public void setAvg(Float avg) {
        this.avg = avg;
    }

    public Float getBoundingAvg() {
        return boundingAvg;
    }

    public void setBoundingAvg(Float boundingAvg) {
        this.boundingAvg = boundingAvg;
    }

    public Float getMedian() {
        return median;
    }

    public void setMedian(Float median) {
        this.median = median;
    }
}
