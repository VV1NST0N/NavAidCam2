package com.example.android.camera2.basic.classificationInterface.helper;

import com.example.android.camera2.basic.CameraActivity;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import java.io.IOException;

public class InitializerFactory {

    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    public static VisionRequestInitializer getInitializer(String CLOUD_VISION_API_KEY, CameraActivity cameraActivity){

        return new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
            /**
             * We override this so we can inject important identifying fields into the HTTP
             * headers. This enables use of a restricted cloud platform API key.
             */
            @Override
            protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                    throws IOException {
                super.initializeVisionRequest(visionRequest);
                String packageName = cameraActivity.getPackageName();
                visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                String sig = PackageManagerUtils.getSignature(cameraActivity.getPACKAGE_MANAGER(), packageName);

                visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
            }
        };
    }
}
