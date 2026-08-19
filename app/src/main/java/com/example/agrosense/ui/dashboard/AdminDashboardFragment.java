package com.example.agrosense.ui.dashboard;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminDashboardFragment extends Fragment {

    private TextView tvTotalUsers, tvTotalFarms;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());

        tvTotalUsers = view.findViewById(R.id.tvTotalUsers);
        tvTotalFarms = view.findViewById(R.id.tvTotalFarms);

        loadStats();

        return view;
    }

    private void loadStats() {
        executorService.execute(() -> {
            int users = db.userDao().countAllUsers();
            int farms = db.farmDao().countAllFarms();

            requireActivity().runOnUiThread(() -> {
                tvTotalUsers.setText(String.valueOf(users));
                tvTotalFarms.setText(String.valueOf(farms));
            });
        });
    }
}
