package com.example.agrosense.ml;

import android.graphics.Bitmap;

import java.util.Random;

public class DemoInferenceEngine implements DiseaseInferenceEngine {

    private static final String[] DISEASES = {
            "Late Blight", "Early Blight", "Septoria Leaf Spot",
            "Bacterial Spot", "Target Spot", "Yellow Leaf Curl Virus"
    };

    @Override
    public PredictionResult runInference(Bitmap bitmap) {
        // Simulated deterministic delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        boolean healthy = random.nextDouble() > 0.8;

        if (healthy) {
            return new PredictionResult("Unknown", "Healthy", "None", 0.95, "NONE", true);
        } else {
            String disease = DISEASES[random.nextInt(DISEASES.length)];
            double confidence = 0.6 + (random.nextDouble() * 0.35);

            // Severity mapping based on confidence for demo purposes
            String severity;
            if (confidence > 0.9) severity = "HIGH";
            else if (confidence > 0.75) severity = "MODERATE";
            else severity = "LOW";

            return new PredictionResult("Tomato", disease, "Fungal Infection", confidence, severity, false);
        }
    }
}
