package com.example.agrosense.ui.scanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.agrosense.MainActivity;
import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.Disease;
import com.example.agrosense.data.entity.Prediction;
import com.example.agrosense.ml.PredictionResult;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiseaseResultFragment extends Fragment {

    private long cropId;
    private String diseaseName, severity;
    private double confidence;
    private boolean isHealthy;
    private Bitmap bitmap;

    private TextView tvStatus, tvDiseaseName, tvConfidence, tvSeverity, tvSymptoms, tvTreatment, tvCause, tvPrevention;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static DiseaseResultFragment newInstance(long cropId, PredictionResult result, Bitmap bitmap) {
        DiseaseResultFragment fragment = new DiseaseResultFragment();
        Bundle args = new Bundle();
        args.putLong("crop_id", cropId);
        args.putString("disease_name", result.getDiseaseName());
        args.putString("plant_name", result.getPlantName());
        args.putString("cause", result.getCause());
        args.putDouble("confidence", result.getConfidence());
        args.putString("severity", result.getSeverity());
        args.putBoolean("is_healthy", result.isHealthy());
        args.putParcelable("bitmap", bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    private String plantName, cause;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropId = getArguments().getLong("crop_id");
            diseaseName = getArguments().getString("disease_name");
            plantName = getArguments().getString("plant_name");
            cause = getArguments().getString("cause");
            confidence = getArguments().getDouble("confidence");
            severity = getArguments().getString("severity");
            isHealthy = getArguments().getBoolean("is_healthy");
            bitmap = getArguments().getParcelable("bitmap");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_result, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());

        tvStatus = view.findViewById(R.id.tvStatus);
        tvDiseaseName = view.findViewById(R.id.tvDiseaseName);
        tvConfidence = view.findViewById(R.id.tvConfidence);
        tvSeverity = view.findViewById(R.id.tvSeverity);
        tvSymptoms = view.findViewById(R.id.tvSymptoms);
        tvTreatment = view.findViewById(R.id.tvTreatment);
        tvCause = view.findViewById(R.id.tvCause);
        tvPrevention = view.findViewById(R.id.tvPrevention);

        view.findViewById(R.id.btnDone).setOnClickListener(v -> saveAndFinish());
        view.findViewById(R.id.btnHistory).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new PredictionHistoryFragment(), true);
            }
        });
        view.findViewById(R.id.btnAskAI).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "AgroSense AI Assistant coming soon!", Toast.LENGTH_SHORT).show();
        });

        displayResult();
        loadDiseaseDetails();

        return view;
    }

    private void displayResult() {
        tvDiseaseName.setText(diseaseName + " (" + plantName + ")");
        tvConfidence.setText(String.format("%.1f%%", confidence * 100));
        tvSeverity.setText(severity);
        tvCause.setText(cause);

        if (isHealthy) {
            tvStatus.setText("CROP APPEARS HEALTHY");
            tvStatus.setBackgroundResource(R.color.leaf_green);
            tvSeverity.setTextColor(ContextCompat.getColor(requireContext(), R.color.leaf_green));
        } else {
            tvStatus.setText("DISEASE DETECTED");
            tvStatus.setBackgroundResource(R.color.error_red);
            tvSeverity.setTextColor(ContextCompat.getColor(requireContext(), R.color.earth_brown));
        }
    }

    private void loadDiseaseDetails() {
        executorService.execute(() -> {
            Disease disease = db.diseaseDao().getDiseaseByName(diseaseName);
            requireActivity().runOnUiThread(() -> {
                if (disease != null) {
                    tvSymptoms.setText(disease.symptoms);
                    tvTreatment.setText(disease.treatmentRecommendation);
                    tvPrevention.setText(disease.prevention);
                } else if (isHealthy) {
                    tvSymptoms.setText("No negative symptoms observed.");
                    tvTreatment.setText("Continue regular monitoring and maintenance.");
                    tvPrevention.setText("Maintain field hygiene and proper irrigation.");
                } else {
                    tvSymptoms.setText("Detailed symptoms not available.");
                    tvTreatment.setText("Consult an expert for specific treatment.");
                    tvPrevention.setText("Avoid spreading suspected infected material.");
                }
            });
        });
    }

    private void saveAndFinish() {
        executorService.execute(() -> {
            Disease disease = db.diseaseDao().getDiseaseByName(diseaseName);
            
            Prediction p = new Prediction();
            p.cropId = cropId;
            p.diseaseId = disease != null ? disease.id : null;
            p.confidence = confidence;
            p.severity = severity;
            p.isHealthy = isHealthy;
            p.timestamp = new Date().getTime();
            p.detectedPlant = plantName;
            p.cause = cause;
            // p.imagePath = ... (would save bitmap to file here)

            db.predictionDao().insert(p);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Prediction saved", Toast.LENGTH_SHORT).show();
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToHome();
                }
            });
        });
    }
}
