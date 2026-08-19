package com.example.agrosense.ml;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Real TensorFlow Lite Inference Engine.
 * 
 * NOTE: As of Phase 4, a verified trained model was not found in the reference repository.
 * This class remains as a clean integration point for when a valid .tflite model is available.
 */
public class RealTFLiteInferenceEngine implements DiseaseInferenceEngine {

    private final Context context;

    public RealTFLiteInferenceEngine(Context context) {
        this.context = context;
    }

    @Override
    public PredictionResult runInference(Bitmap bitmap) {
        // TODO: Implement actual TFLite inference logic:
        // 1. Load .tflite model from assets
        // 2. Preprocess Bitmap (resize to model input size, e.g., 224x224)
        // 3. Normalize pixels
        // 4. Run interpreter
        // 5. Map output to labels
        
        // For now, this is a placeholder. 
        // Use DemoInferenceEngine until a verified model is provided.
        throw new UnsupportedOperationException("Real TFLite model not configured. Use DemoInferenceEngine.");
    }
}
