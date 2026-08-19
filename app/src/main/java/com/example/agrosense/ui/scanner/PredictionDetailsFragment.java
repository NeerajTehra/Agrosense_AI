package com.example.agrosense.ui.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.Disease;
import com.example.agrosense.data.entity.Prediction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PredictionDetailsFragment extends Fragment {

    private long predictionId;
    private TextView tvDiseaseName, tvDate, tvConfidence, tvSeverity, tvSymptoms, tvTreatment, tvCause;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

    public static PredictionDetailsFragment newInstance(long predictionId) {
        PredictionDetailsFragment fragment = new PredictionDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("prediction_id", predictionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            predictionId = getArguments().getLong("prediction_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prediction_details, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());

        tvDiseaseName = view.findViewById(R.id.tvDiseaseName);
        tvDate = view.findViewById(R.id.tvDate);
        tvConfidence = view.findViewById(R.id.tvConfidence);
        tvSeverity = view.findViewById(R.id.tvSeverity);
        tvSymptoms = view.findViewById(R.id.tvSymptoms);
        tvTreatment = view.findViewById(R.id.tvTreatment);
        tvCause = view.findViewById(R.id.tvCause);

        loadDetails();

        return view;
    }

    private void loadDetails() {
        executorService.execute(() -> {
            Prediction p = db.predictionDao().getPredictionById(predictionId);
            Disease d = (p != null && p.diseaseId != null) ? db.diseaseDao().getDiseaseById(p.diseaseId) : null;

            requireActivity().runOnUiThread(() -> {
                if (p != null) {
                    tvDate.setText(dateFormat.format(new Date(p.timestamp)));
                    tvConfidence.setText(String.format(Locale.getDefault(), "%.1f%%", p.confidence * 100));
                    tvSeverity.setText(p.severity);
                    tvCause.setText(p.cause != null ? p.cause : "N/A");
                    
                    String displayName = p.isHealthy ? "Healthy Crop" : (d != null ? d.getName() : "Infection Detected");
                    if (p.detectedPlant != null) {
                        displayName += " (" + p.detectedPlant + ")";
                    }
                    tvDiseaseName.setText(displayName);
                    
                    if (p.isHealthy) {
                        tvSymptoms.setText("No symptoms detected.");
                        tvTreatment.setText("Regular maintenance.");
                    } else if (d != null) {
                        tvSymptoms.setText(d.getSymptoms());
                        tvTreatment.setText(d.getTreatment());
                    } else {
                        tvSymptoms.setText("Information unavailable.");
                        tvTreatment.setText("Consult an expert.");
                    }
                }
            });
        });
    }
}
