package com.example.android.camera2.basic.imageProcessing.depth;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.util.Log;

import com.example.android.camera2.basic.imageProcessing.objectClassification.DepthInformationObj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepthMap {

    private short maxz = -1000;
    private short minz = 9999;

    float sum = 0;
    float avg = -1000;
    // idea: if stride states
    int boundingBoxSum = 0;
    float boundingBoxAvg = -1000;
    float median = 0;
    Integer numPoints = 0;
    List<Float> arrayOfRanges = new ArrayList<Float>();

    Map<String, Integer> test_Method(Image image, Image.Plane plane) {
        Map<String, Integer> test = new HashMap();
        ShortBuffer buffer = plane.getBuffer().order(ByteOrder.nativeOrder()).asShortBuffer();
        try{
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int byteIndex = x * plane.getPixelStride() + y * plane.getRowStride();
                    int depthSample =  buffer.get(byteIndex)& 0xFFFF;
                    int depthRange = (depthSample & 0x1FFF);
                    int depthConfidence = ((depthSample >> 13) & 0x7);

                    test.put(x + "_" + y +"_" + "range",depthRange);
                    test.put(x + "_" + y +"_" + "conf",depthConfidence);


                }
            }

        }catch (Exception e){
            Log.d("Error catched" , e.getLocalizedMessage() + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return test;
    }

    public DepthInformationObj checkImageDepth(Image image) {
        Map<String, PixelData> map = parseTof(image);
        //TODO refator OOP
        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Image.Plane plane = image.getPlanes()[0];

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        avg = sum / numPoints;
        median = calcMedian(arrayOfRanges);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                PixelData pixelInfo = map.get(x + "_" + y);
                float z = pixelInfo.distance;
                if (z > 0) {

                    float normalized_range = (int)(z-minz)/(maxz-minz);
                    //int colour = (int) (normalized_range * 10) * 255;
                    float depthRangeMax = z / maxz;
                    if(depthRangeMax > 1){
                        depthRangeMax = 1;
                    }
                     int colour = (int) ( depthRangeMax * 255);

                    int c = Color.rgb(0,(int) (colour), 0);
                    bitmap.setPixel(x, y, c);
                }
            }
        }


        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        DepthInformationObj depthInformationObj = new DepthInformationObj(rotatedBitmap, (int) minz, (int) maxz, avg, boundingBoxAvg, median);
        resetValues();
        return depthInformationObj;
    }

    public void resetValues(){
        maxz = -1000;
        minz = 9999;
        sum = 0;
        avg = -1000;
        boundingBoxSum = 0;
        boundingBoxAvg = -1000;
        median = 0;
        arrayOfRanges = new ArrayList<Float>();
        numPoints = 0;
    }

    public Map<String, PixelData> parseTof(Image imgTOF) {

        Map<String, PixelData> map = new HashMap();

        // Buffers for storing TOF output

        Image.Plane plane = imgTOF.getPlanes()[0];
        ByteBuffer shortDepthBuffer = plane.getBuffer().order(ByteOrder.nativeOrder());

        int stride = plane.getRowStride();
        int offset = 0;


        int i = 0;

        for (short y = 0; y < imgTOF.getHeight(); y++) {
            for (short x = 0; x < imgTOF.getWidth(); x++) {
                // Parse the data. Format is [depth|confidence]
                int depthSample = shortDepthBuffer.get( (y / 2) * stride + x);
                short depthSampleShort = (short) depthSample;
                short depthRange = (short) (depthSampleShort & 0x1FFF);
                short depthConfidence = (short) ((depthSampleShort >> 13) & 0x7);
                float depthPercentage = depthConfidence == 0 ? 1.f : (depthConfidence - 1) / 7.f;
                if (depthRange > maxz && depthRange < 8000) {
                    maxz = depthRange;
                }
                sum = sum + depthRange;
                numPoints++;
                arrayOfRanges.add((float) depthRange);
                if (depthRange < minz && depthRange > 0) {
                    minz = depthRange;
                }

                // Store data in buffer
                map.put(x + "_" + y, new PixelData(x, y, depthRange, depthPercentage));
                i++;
            }
            offset += imgTOF.getWidth();
        }
        return map;
    }

    class PixelData {
        short x;
        short y;
        float distance;
        float percantage;

        PixelData(short x, short y, float distance, float percentage) {
            this.x = x;
            this.y = y;
            this.distance = distance;
            this.percantage = percentage;

        }
    }

    public float calcMedian(List<Float> depthsRanges) {
        Collections.sort(depthsRanges);
        float median;
        if (depthsRanges.size() % 2 == 0)
            median = ((float) depthsRanges.get(depthsRanges.size() / 2) + (float) depthsRanges.get(depthsRanges.size() / 2 - 1)) / 2;
        else
            median = (float) depthsRanges.get(depthsRanges.size() / 2);
        return median;
    }
}
