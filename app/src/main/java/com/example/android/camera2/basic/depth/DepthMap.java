package com.example.android.camera2.basic.depth;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepthMap {

    public static Bitmap checkImageDepth3(Image image){
        Bitmap b = Bitmap.createBitmap(image.getWidth(),image.getHeight(), Bitmap.Config.ARGB_8888);

        Image.Plane plane = image.getPlanes()[0];
        ShortBuffer shortDepthBuffer = plane.getBuffer().asShortBuffer();
        ArrayList<Short> pixel = new ArrayList<Short>();
        while (shortDepthBuffer.hasRemaining())
        {
            pixel.add(shortDepthBuffer.get());
        }

        int stride = plane.getRowStride();
        int offset = 0;
        float[] output = new float[image.getWidth() * image.getHeight()];
        float maxz = -1000;
        Map<String, Float> test = new HashMap();
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++) {
                int depthSample = pixel.get((y / 2) * stride + x);
                int depthRange = (depthSample & 0x1FFF);
                int depthConfidence = ((depthSample >> 13) & 0x7);
                float depthPercentage = depthConfidence == 0 ? 1.f : (depthConfidence - 1) / 7.f;
                if(depthRange > maxz)
                {
                    maxz = depthRange;
                }
                test.put(x + "_" + y, (float) depthRange);
                output[(y / 2) * stride + x] = depthRange;
            }

            offset += image.getWidth();
        }


        for(int x =0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight()-100; y++)
            {
                float z = test.get(x+ "_" + y);
                if(z > 0) {
                    int colour = (int) (z / maxz * 255);
                    int c = Color.rgb(colour, colour, colour);
                    b.setPixel(x, y, c);
                }
            }
        }

        return b;
    }

}
