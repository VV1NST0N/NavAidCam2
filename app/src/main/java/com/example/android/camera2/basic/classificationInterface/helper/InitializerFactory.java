package com.example.android.camera2.basic.classificationInterface.helper;

import android.content.pm.PackageManager;
import android.media.Image;

import com.example.android.camera2.basic.CameraActivity;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class InitializerFactory {

    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    public static VisionRequestInitializer getInitializer(String CLOUD_VISION_API_KEY, PackageManager manager, String packageName){

        return new VisionRequestInitializer(CLOUD_VISION_API_KEY) {

            @Override
            protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                    throws IOException {
                super.initializeVisionRequest(visionRequest);
                visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);
                String sig = PackageManagerUtils.getSignature(manager, packageName);
                visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
            }
        };
    }



    public com.google.api.services.vision.v1.model.Image convertToGoogleImage(Image image) {
        //TODO fix this
        ///val source: ImageDecoder.Source = ImageDecoder.createSource(file)

        ByteBuffer buffer  = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        com.google.api.services.vision.v1.model.Image imageClass = new com.google.api.services.vision.v1.model.Image();
        com.google.api.services.vision.v1.model.Image newImage  = imageClass.encodeContent(bytes);

        return newImage;
    }
}
