package com.example.agrosense.ui.farm;

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
import com.example.agrosense.data.entity.Farm;
import com.example.agrosense.data.entity.Field;
import com.example.agrosense.ui.field.FieldAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FarmDetailsFragment extends Fragment {

    private long farmId;
    private TextView tvFarmName, tvLocation, tvTotalArea, tvFieldCount;
    private RecyclerView rvFields;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static FarmDetailsFragment newInstance(long farmId) {
        FarmDetailsFragment fragment = new FarmDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("farm_id", farmId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            farmId = getArguments().getLong("farm_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_details, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());

        tvFarmName = view.findViewById(R.id.tvFarmName);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvTotalArea = view.findViewById(R.id.tvTotalArea);
        tvFieldCount = view.findViewById(R.id.tvFieldCount);
        rvFields = view.findViewById(R.id.rvFields);

        rvFields.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadFarmDetails();

        return view;
    }

    private void loadFarmDetails() {
        executorService.execute(() -> {
            Farm farm = db.farmDao().getFarmById(farmId);
            List<Field> fields = db.fieldDao().getFieldsByFarm(farmId);

            requireActivity().runOnUiThread(() -> {
                if (farm != null) {
                    tvFarmName.setText(farm.name);
                    tvLocation.setText(farm.location);
                    tvTotalArea.setText(farm.totalArea + " Ac");
                    tvFieldCount.setText(String.valueOf(fields.size()));
                    
                    rvFields.setAdapter(new FieldAdapter(fields, field -> {
                        // Navigate to Field Details
                    }));
                }
            });
        });
    }
}
