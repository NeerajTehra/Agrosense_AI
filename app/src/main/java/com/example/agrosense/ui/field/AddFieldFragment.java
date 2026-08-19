package com.example.agrosense.ui.field;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.Farm;
import com.example.agrosense.data.entity.Field;
import com.example.agrosense.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddFieldFragment extends Fragment {

    private Spinner spinnerFarm;
    private TextInputEditText etFieldName, etCropType, etArea;
    private Button btnSaveField;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Farm> userFarms = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_field, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        spinnerFarm = view.findViewById(R.id.spinnerFarm);
        etFieldName = view.findViewById(R.id.etFieldName);
        etCropType = view.findViewById(R.id.etCropType);
        etArea = view.findViewById(R.id.etArea);
        btnSaveField = view.findViewById(R.id.btnSaveField);

        loadFarms();

        btnSaveField.setOnClickListener(v -> saveField());

        return view;
    }

    private void loadFarms() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            userFarms = db.farmDao().getFarmsByUser(userId);
            List<String> farmNames = new ArrayList<>();
            for (Farm farm : userFarms) {
                farmNames.add(farm.name);
            }

            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, farmNames);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerFarm.setAdapter(adapter);
            });
        });
    }

    private void saveField() {
        if (userFarms.isEmpty()) {
            Toast.makeText(requireContext(), "Add a farm first", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedFarmIndex = spinnerFarm.getSelectedItemPosition();
        if (selectedFarmIndex < 0) return;

        long farmId = userFarms.get(selectedFarmIndex).id;
        String name = etFieldName.getText().toString().trim();
        String cropType = etCropType.getText().toString().trim();
        String areaStr = etArea.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(cropType) || TextUtils.isEmpty(areaStr)) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double area = Double.parseDouble(areaStr);

        executorService.execute(() -> {
            Field field = new Field();
            field.farmId = farmId;
            field.name = name;
            field.cropType = cropType;
            field.area = area;

            db.fieldDao().insert(field);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Field saved successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        });
    }
}
