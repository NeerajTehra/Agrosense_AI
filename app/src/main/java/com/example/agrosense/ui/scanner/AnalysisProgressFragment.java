package com.example.agrosense.ui.scanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrosense.MainActivity;
import com.example.agrosense.R;
import com.example.agrosense.ml.DemoInferenceEngine;
import com.example.agrosense.ml.DiseaseInferenceEngine;
import com.example.agrosense.ml.PredictionResult;
import com.example.agrosense.ui.scanner.DiseaseResultFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalysisProgressFragment extends Fragment {

    private long cropId;
    private Bitmap bitmap;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static AnalysisProgressFragment newInstance(long cropId, Bitmap bitmap) {
        AnalysisProgressFragment fragment = new AnalysisProgressFragment();
        Bundle args = new Bundle();
        args.putLong("crop_id", cropId);
        args.putParcelable("bitmap", bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropId = getArguments().getLong("crop_id");
            bitmap = getArguments().getParcelable("bitmap");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analysis_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        runInference();
    }

    private void runInference() {
        executorService.execute(() -> {
            // Using DemoInferenceEngine as requested for Phase 4 if real model is unavailable
            DiseaseInferenceEngine engine = new DemoInferenceEngine();
            PredictionResult result = engine.runInference(bitmap);

            mainHandler.post(() -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).loadFragment(
                            DiseaseResultFragment.newInstance(cropId, result, bitmap), true);
                }
            });
        });
    }
}
