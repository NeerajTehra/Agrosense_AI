package com.example.agrosense.ui.crop;

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
import com.example.agrosense.data.entity.Crop;
import com.example.agrosense.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCropFragment extends Fragment {

    private Spinner spinnerField;
    private TextInputEditText etVariety;
    private Button btnSaveCrop;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Field> userFields = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_crop, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        spinnerField = view.findViewById(R.id.spinnerField);
        etVariety = view.findViewById(R.id.etVariety);
        btnSaveCrop = view.findViewById(R.id.btnSaveCrop);

        loadFields();

        btnSaveCrop.setOnClickListener(v -> saveCrop());

        return view;
    }

    private void loadFields() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            List<Farm> userFarms = db.farmDao().getFarmsByUser(userId);
            userFields.clear();
            List<String> fieldNames = new ArrayList<>();
            for (Farm farm : userFarms) {
                List<Field> fields = db.fieldDao().getFieldsByFarm(farm.id);
                for (Field field : fields) {
                    userFields.add(field);
                    fieldNames.add(field.name + " (" + farm.name + ")");
                }
            }

            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, fieldNames);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerField.setAdapter(adapter);
            });
        });
    }

    private void saveCrop() {
        if (userFields.isEmpty()) {
            Toast.makeText(requireContext(), "Add a field first", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedFieldIndex = spinnerField.getSelectedItemPosition();
        if (selectedFieldIndex < 0) return;

        long fieldId = userFields.get(selectedFieldIndex).id;
        String variety = etVariety.getText().toString().trim();

        if (TextUtils.isEmpty(variety)) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            Crop crop = new Crop();
            crop.fieldId = fieldId;
            crop.variety = variety;
            crop.plantingDate = new Date().getTime();
            crop.status = "GROWING";

            db.cropDao().insert(crop);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Crop saved successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        });
    }
}
