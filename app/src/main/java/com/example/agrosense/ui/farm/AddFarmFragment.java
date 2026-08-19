package com.example.agrosense.ui.farm;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.Farm;
import com.example.agrosense.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddFarmFragment extends Fragment {

    private TextInputEditText etFarmName, etLocation, etArea;
    private Button btnSaveFarm;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_farm, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        etFarmName = view.findViewById(R.id.etFarmName);
        etLocation = view.findViewById(R.id.etLocation);
        etArea = view.findViewById(R.id.etArea);
        btnSaveFarm = view.findViewById(R.id.btnSaveFarm);

        btnSaveFarm.setOnClickListener(v -> saveFarm());

        return view;
    }

    private void saveFarm() {
        String name = etFarmName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String areaStr = etArea.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(areaStr)) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double area = Double.parseDouble(areaStr);
        long userId = sessionManager.getUserId();

        executorService.execute(() -> {
            Farm farm = new Farm();
            farm.name = name;
            farm.location = location;
            farm.totalArea = area;
            farm.userId = userId;

            db.farmDao().insert(farm);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Farm saved successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        });
    }
}
