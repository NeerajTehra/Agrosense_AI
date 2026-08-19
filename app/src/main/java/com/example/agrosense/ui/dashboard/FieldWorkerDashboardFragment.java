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

public class FieldWorkerDashboardFragment extends Fragment {

    private TextView tvAssignedFields;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_worker_dashboard, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        tvAssignedFields = view.findViewById(R.id.tvAssignedFields);

        loadStats();

        return view;
    }

    private void loadStats() {
        executorService.execute(() -> {
            int count = db.fieldDao().getFieldCount();
            requireActivity().runOnUiThread(() -> tvAssignedFields.setText(String.valueOf(count)));
        });
    }
}
