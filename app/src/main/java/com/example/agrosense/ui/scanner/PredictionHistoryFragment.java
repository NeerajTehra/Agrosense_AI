package com.example.agrosense.ui.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.Prediction;
import com.example.agrosense.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PredictionHistoryFragment extends Fragment {

    private RecyclerView rvPredictions;
    private TextView tvEmptyState;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prediction_history, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        rvPredictions = view.findViewById(R.id.rvPredictions);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        rvPredictions.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadHistory();

        return view;
    }

    private void loadHistory() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            List<Prediction> predictions = db.predictionDao().getPredictionsByUser(userId);
            requireActivity().runOnUiThread(() -> {
                if (predictions.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    rvPredictions.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    rvPredictions.setVisibility(View.VISIBLE);
                    rvPredictions.setAdapter(new PredictionAdapter(predictions, prediction -> {
                        if (getActivity() instanceof com.example.agrosense.MainActivity) {
                            ((com.example.agrosense.MainActivity) getActivity()).loadFragment(PredictionDetailsFragment.newInstance(prediction.id), true);
                        }
                    }));
                }
            });
        });
    }
}
