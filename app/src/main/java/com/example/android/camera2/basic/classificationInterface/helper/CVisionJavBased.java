package com.example.android.camera2.basic.classificationInterface.helper;

import android.util.Log;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CVisionJavBased {

    public String getAnalzedResponse(ByteString data){
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<AnnotateImageRequest>();
            Image img = Image.newBuilder().setContent(data).build();
            Feature feat = Feature.newBuilder().setType(com.google.cloud.vision.v1.Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    Log.d("Cloud Vision ERROR", res.getError().getMessage());
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return "ERROR";
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    Log.d("CVISION: ", annotation
                            .getAllFields().entrySet().toString());
                }
            }
            return "ERROR";
        }catch (Exception e){
            Log.d("Cloud Vision ERROR", e.getMessage());
            return "ERROR";
        }
    }
}
