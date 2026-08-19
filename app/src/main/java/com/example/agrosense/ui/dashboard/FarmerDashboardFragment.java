package com.example.agrosense.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FarmerDashboardFragment extends Fragment {

    private TextView tvGreeting, tvTotalFarms, tvTotalFields, tvActiveCrops, tvHealthScore;
    private RecyclerView rvRecentActivity;
    private SessionManager sessionManager;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_dashboard, container, false);

        sessionManager = new SessionManager(requireContext());
        db = AgroSenseDatabase.getInstance(requireContext());

        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvTotalFarms = view.findViewById(R.id.tvTotalFarms);
        tvTotalFields = view.findViewById(R.id.tvTotalFields);
        tvActiveCrops = view.findViewById(R.id.tvActiveCrops);
        tvHealthScore = view.findViewById(R.id.tvHealthScore);
        rvRecentActivity = view.findViewById(R.id.rvRecentActivity);

        rvRecentActivity.setLayoutManager(new LinearLayoutManager(requireContext()));
        setupDummyActivity();

        view.findViewById(R.id.btnAddFarm).setOnClickListener(v -> {
            if (getActivity() instanceof com.example.agrosense.MainActivity) {
                ((com.example.agrosense.MainActivity) getActivity()).loadFragment(new com.example.agrosense.ui.farm.AddFarmFragment(), true);
            }
        });

        view.findViewById(R.id.btnAddField).setOnClickListener(v -> {
            if (getActivity() instanceof com.example.agrosense.MainActivity) {
                ((com.example.agrosense.MainActivity) getActivity()).loadFragment(new com.example.agrosense.ui.field.AddFieldFragment(), true);
            }
        });

        view.findViewById(R.id.btnViewCrops).setOnClickListener(v -> {
            if (getActivity() instanceof com.example.agrosense.MainActivity) {
                ((com.example.agrosense.MainActivity) getActivity()).loadFragment(new com.example.agrosense.ui.crop.CropListFragment(), true);
            }
        });

        view.findViewById(R.id.btnScanCrop).setOnClickListener(v -> {
            if (getActivity() instanceof com.example.agrosense.MainActivity) {
                ((com.example.agrosense.MainActivity) getActivity()).loadFragment(new com.example.agrosense.ui.scanner.DiseaseScannerFragment(), true);
            }
        });

        String greeting = "Hello, " + sessionManager.getUserName();
        tvGreeting.setText(greeting);

        loadStats();

        return view;
    }

    private void setupDummyActivity() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            List<com.example.agrosense.data.entity.Prediction> predictions = db.predictionDao().getRecentPredictions(5);
            requireActivity().runOnUiThread(() -> {
                if (predictions.isEmpty()) {
                    // Show some welcome items if no scans yet
                    List<RecentActivityAdapter.ActivityItem> items = new ArrayList<>();
                    items.add(new RecentActivityAdapter.ActivityItem("AgroSense AI v1.0", "Welcome to the future of farming", "Now"));
                    rvRecentActivity.setAdapter(new RecentActivityAdapter(items));
                } else {
                    List<RecentActivityAdapter.ActivityItem> items = new ArrayList<>();
                    for (com.example.agrosense.data.entity.Prediction p : predictions) {
                        String title = p.isHealthy ? "Healthy Crop Scan" : "Infection Detected";
                        String details = "Scan #" + p.id;
                        String time = "Recent"; // Simplified
                        items.add(new RecentActivityAdapter.ActivityItem(title, details, time));
                    }
                    rvRecentActivity.setAdapter(new RecentActivityAdapter(items));
                }
            });
        });
    }

    private void loadStats() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            int farms = db.farmDao().countFarmsByUser(userId);
            int fields = db.fieldDao().countFieldsByUser(userId);
            int crops = db.cropDao().countActiveCropsByUser(userId);
            
            requireActivity().runOnUiThread(() -> {
                tvTotalFarms.setText(String.valueOf(farms));
                tvTotalFields.setText(String.valueOf(fields));
                tvActiveCrops.setText(String.valueOf(crops));
                // Health score logic can be added later
            });
        });
    }
}
