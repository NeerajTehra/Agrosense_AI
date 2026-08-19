package com.example.agrosense.ui.crop;

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
import com.example.agrosense.data.entity.Crop;
import com.example.agrosense.data.entity.Farm;
import com.example.agrosense.data.entity.Field;
import com.example.agrosense.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CropListFragment extends Fragment {

    private RecyclerView rvCrops;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddCrop;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_list, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        rvCrops = view.findViewById(R.id.rvCrops);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        fabAddCrop = view.findViewById(R.id.fabAddCrop);

        rvCrops.setLayoutManager(new LinearLayoutManager(requireContext()));

        fabAddCrop.setOnClickListener(v -> {
            if (getActivity() instanceof com.example.agrosense.MainActivity) {
                ((com.example.agrosense.MainActivity) getActivity()).loadFragment(new AddCropFragment(), true);
            }
        });

        loadCrops();

        return view;
    }

    private void loadCrops() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            List<Crop> allCrops = new ArrayList<>();
            List<Farm> userFarms = db.farmDao().getFarmsByUser(userId);
            for (Farm farm : userFarms) {
                List<Field> fields = db.fieldDao().getFieldsByFarm(farm.id);
                for (Field field : fields) {
                    allCrops.addAll(db.cropDao().getCropsByField(field.id));
                }
            }

            requireActivity().runOnUiThread(() -> {
                if (allCrops.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    rvCrops.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    rvCrops.setVisibility(View.VISIBLE);
                    rvCrops.setAdapter(new CropAdapter(allCrops, crop -> {
                        if (getActivity() instanceof com.example.agrosense.MainActivity) {
                            ((com.example.agrosense.MainActivity) getActivity()).loadFragment(CropDetailsFragment.newInstance(crop.id), true);
                        }
                    }));
                }
            });
        });
    }
}
