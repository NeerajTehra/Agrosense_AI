package com.example.agrosense.ui.crop;

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
import com.example.agrosense.data.entity.Crop;
import com.example.agrosense.data.entity.Field;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CropDetailsFragment extends Fragment {

    private long cropId;
    private TextView tvCropVariety, tvStatus, tvPlantingDate, tvFieldName;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public static CropDetailsFragment newInstance(long cropId) {
        CropDetailsFragment fragment = new CropDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("crop_id", cropId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropId = getArguments().getLong("crop_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_details, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());

        tvCropVariety = view.findViewById(R.id.tvCropVariety);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvPlantingDate = view.findViewById(R.id.tvPlantingDate);
        tvFieldName = view.findViewById(R.id.tvFieldName);

        loadCropDetails();

        return view;
    }

    private void loadCropDetails() {
        executorService.execute(() -> {
            Crop crop = db.cropDao().getCropById(cropId);
            Field field = crop != null ? db.fieldDao().getFieldById(crop.fieldId) : null;

            requireActivity().runOnUiThread(() -> {
                if (crop != null) {
                    tvCropVariety.setText(crop.variety);
                    tvStatus.setText(crop.status);
                    tvPlantingDate.setText(dateFormat.format(new Date(crop.plantingDate)));
                    if (field != null) {
                        tvFieldName.setText(field.name);
                    }
                }
            });
        });
    }
}
