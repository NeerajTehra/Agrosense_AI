package com.example.agrosense.ui.field;

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
import com.example.agrosense.data.entity.Field;
import com.example.agrosense.data.entity.Crop;
import com.example.agrosense.ui.crop.CropAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FieldDetailsFragment extends Fragment {

    private long fieldId;
    private TextView tvFieldName, tvCropType, tvArea, tvHealthStatus;
    private RecyclerView rvCrops;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static FieldDetailsFragment newInstance(long fieldId) {
        FieldDetailsFragment fragment = new FieldDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("field_id", fieldId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fieldId = getArguments().getLong("field_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_details, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());

        tvFieldName = view.findViewById(R.id.tvFieldName);
        tvCropType = view.findViewById(R.id.tvCropType);
        tvArea = view.findViewById(R.id.tvArea);
        tvHealthStatus = view.findViewById(R.id.tvHealthStatus);
        rvCrops = view.findViewById(R.id.rvCrops);

        rvCrops.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadFieldDetails();

        return view;
    }

    private void loadFieldDetails() {
        executorService.execute(() -> {
            Field field = db.fieldDao().getFieldById(fieldId);
            List<Crop> crops = db.cropDao().getCropsByField(fieldId);

            requireActivity().runOnUiThread(() -> {
                if (field != null) {
                    tvFieldName.setText(field.name);
                    tvCropType.setText("Crop: " + field.cropType);
                    tvArea.setText(field.area + " Ac");
                    // Health status logic
                    
                    rvCrops.setAdapter(new CropAdapter(crops, crop -> {
                        // Navigate to Crop Details
                    }));
                }
            });
        });
    }
}
