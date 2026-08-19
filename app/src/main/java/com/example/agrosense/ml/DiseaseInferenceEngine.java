package com.example.agrosense.ml;

import android.graphics.Bitmap;

public interface DiseaseInferenceEngine {
    PredictionResult runInference(Bitmap bitmap);
}
