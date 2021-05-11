package com.example.android.camera2.basic.utils.helper;

import android.content.pm.PackageManager;
import android.media.Image;

import com.google.api.services.translate.TranslateRequest;
import com.google.api.services.translate.TranslateRequestInitializer;
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
}
