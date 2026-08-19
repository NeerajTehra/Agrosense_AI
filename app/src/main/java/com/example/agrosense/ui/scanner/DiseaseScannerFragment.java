package com.example.agrosense.ui.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.agrosense.MainActivity;
import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.Crop;
import com.example.agrosense.data.entity.Farm;
import com.example.agrosense.data.entity.Field;
import com.example.agrosense.utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiseaseScannerFragment extends Fragment {

    private Spinner spinnerFarm, spinnerField, spinnerCrop;
    private ImageView ivPreview;
    private View placeholderLayout;
    private Bitmap selectedBitmap;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private List<Farm> farms = new ArrayList<>();
    private List<Field> fields = new ArrayList<>();
    private List<Crop> crops = new ArrayList<>();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    selectedBitmap = (Bitmap) extras.get("data");
                    showPreview();
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        showPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_scanner, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        spinnerFarm = view.findViewById(R.id.spinnerFarm);
        spinnerField = view.findViewById(R.id.spinnerField);
        spinnerCrop = view.findViewById(R.id.spinnerCrop);
        ivPreview = view.findViewById(R.id.ivPreview);
        placeholderLayout = view.findViewById(R.id.placeholder_layout);

        view.findViewById(R.id.btnCamera).setOnClickListener(v -> checkPermissionAndOpenCamera());
        view.findViewById(R.id.btnGallery).setOnClickListener(v -> openGallery());
        view.findViewById(R.id.btnAnalyze).setOnClickListener(v -> analyzeCrop());

        setupSpinners();
        loadFarms();

        return view;
    }

    private void setupSpinners() {
        spinnerFarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadFields(farms.get(position).id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadCrops(fields.get(position).id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadFarms() {
        executorService.execute(() -> {
            farms = db.farmDao().getFarmsByUser(sessionManager.getUserId());
            requireActivity().runOnUiThread(() -> {
                List<String> names = new ArrayList<>();
                for (Farm f : farms) names.add(f.name);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, names);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerFarm.setAdapter(adapter);
            });
        });
    }

    private void loadFields(long farmId) {
        executorService.execute(() -> {
            fields = db.fieldDao().getFieldsByFarm(farmId);
            requireActivity().runOnUiThread(() -> {
                List<String> names = new ArrayList<>();
                for (Field f : fields) names.add(f.name);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, names);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerField.setAdapter(adapter);
            });
        });
    }

    private void loadCrops(long fieldId) {
        executorService.execute(() -> {
            crops = db.cropDao().getCropsByField(fieldId);
            requireActivity().runOnUiThread(() -> {
                List<String> names = new ArrayList<>();
                for (Crop c : crops) names.add(c.variety);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, names);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerCrop.setAdapter(adapter);
            });
        });
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePictureIntent);
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhoto);
    }

    private void showPreview() {
        if (selectedBitmap != null) {
            ivPreview.setImageBitmap(selectedBitmap);
            placeholderLayout.setVisibility(View.GONE);
        }
    }

    private void analyzeCrop() {
        if (selectedBitmap == null) {
            Toast.makeText(requireContext(), "Please select or capture a crop image", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPos = spinnerCrop.getSelectedItemPosition();
        if (selectedPos < 0 || selectedPos >= crops.size()) {
            Toast.makeText(requireContext(), "Please select a valid crop", Toast.LENGTH_SHORT).show();
            return;
        }

        long cropId = crops.get(selectedPos).id;
        
        // Navigate to result fragment with data
        if (getActivity() instanceof MainActivity) {
            // Passing bitmap is not ideal for large images, but for demo it's fine. 
            // Better: save to temp file and pass path.
            ((MainActivity) getActivity()).loadFragment(AnalysisProgressFragment.newInstance(cropId, selectedBitmap), true);
        }
    }
}
