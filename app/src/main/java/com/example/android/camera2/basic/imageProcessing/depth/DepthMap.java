package com.example.android.camera2.basic.imageProcessing.depth;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.api.client.util.DateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.sql.Timestamp;
import java.time.Instant;
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
    float median = 0;
    Integer numPoints = 0;
    List<Float> listOfRanges = new ArrayList<Float>();
    public static int WIDTH = 240;
    public static int HEIGHT = 180;


    public DepthInformationObj createDepthMapFromDepth16(Image image) {
        //WIDTH = image.getWidth() / 2;
        //HEIGHT = image.getHeight() / 2;
        Map<String, PixelData> map = parseDepth16IntoDistanceMap(image);
        Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        Image.Plane plane = image.getPlanes()[0];

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        avg = sum / numPoints;
        median = calcMedian(listOfRanges);


        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                PixelData pixelInfo = map.get(x + "_" + y);
                float z = pixelInfo.distance;
                if (z > 0) {

                    float normalized_range = (float) (z - minz) / (maxz - minz);
                    //int colour = (int) (normalized_range * 10) * 255;
                    float depthRangeMax = normalized_range; //z / maxz;
                    int c;
                    if (depthRangeMax > 1) {
                        int colour = (int) (depthRangeMax * 255);
                        c = Color.rgb(colour, 0, 0);
                    } else {
                        int colour = (int) (depthRangeMax * 255);
                        c = Color.rgb(0, (int) (colour), 0);
                    }
                    bitmap.setPixel(x, y, c);
                }
            }
        }


        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        DepthInformationObj depthInformationObj = new DepthInformationObj(rotatedBitmap, (int) minz, (int) maxz, avg, null, median);
        writeToFile(map, rotatedBitmap);
        resetValues();
        return depthInformationObj;
    }

    private void resetValues() {
        maxz = -1000;
        minz = 9999;
        sum = 0;
        avg = -1000;
        median = 0;
        listOfRanges = new ArrayList<Float>();
        numPoints = 0;
    }

    private Map<String, PixelData> parseDepth16IntoDistanceMap(Image image) {

        Map<String, PixelData> map = new HashMap();
        Image.Plane plane = image.getPlanes()[0];
        //ByteBuffer buffer = plane.getBuffer().order(ByteOrder.LITTLE_ENDIAN);
        int stride = plane.getRowStride();
        ShortBuffer buffer = plane.getBuffer().order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();

        for (short y = 0; y < HEIGHT; y++) {
            for (short x = 0; x < WIDTH; x++) {
                // Parse the data. Format is [depth|confidence]
                //short depthSample = buffer.get((y / 2) * stride + x);
                short depthSample = buffer.get((x * 2) + (stride * y));
                short depthRange = (short) (depthSample & 0x1F);
                short depthConfidence = (short) ((depthSample >> 13) & 0x7);
                float depthPercentage = depthConfidence == 0 ? 1.f : (depthConfidence - 1) / 7.f;
                if (depthRange > maxz && depthRange < 6500) {
                    maxz = depthRange;
                }
                sum = sum + depthRange;
                numPoints++;
                listOfRanges.add((float) depthRange);
                if (depthRange < minz && depthRange > 0) {
                    minz = depthRange;
                }
                map.put(x + "_" + y, new PixelData(x, y, depthRange, depthPercentage));

            }
        }
        return map;
    }

    private class PixelData {
        short x;
        short y;
        int distance;
        float percantage;

        PixelData(short x, short y, int distance, float percentage) {
            this.x = x;
            this.y = y;
            this.distance = distance;
            this.percantage = percentage;

        }
    }

    private float calcMedian(List<Float> depthsRanges) {
        Collections.sort(depthsRanges);
        float median;
        if (depthsRanges.size() % 2 == 0)
            median = ((float) depthsRanges.get(depthsRanges.size() / 2) + (float) depthsRanges.get(depthsRanges.size() / 2 - 1)) / 2;
        else
            median = (float) depthsRanges.get(depthsRanges.size() / 2);
        return median;
    }


    public void writeToFile(Map<String, PixelData> map, Bitmap bitmap) {
        List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();
        String json = "";
        try {
            for (Map.Entry<String, PixelData> data : map.entrySet()) {
                if (data.getValue() != null && data.getValue().x == bitmap.getWidth() / 2) {
                    Map entry = new HashMap<String, String>();
                    entry.put("Distanz", Integer.valueOf(data.getValue().distance).toString());
                    entry.put("Prozent", Float.valueOf(data.getValue().percantage).toString());
                    entry.put("X", Short.valueOf(data.getValue().x).toString());
                    entry.put("Y", Short.valueOf(data.getValue().y).toString());
                    listOfMaps.add(entry);
                }
            }
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(listOfMaps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Querschnitt mitte X-Achse: ", json);
        return;
    }


}
