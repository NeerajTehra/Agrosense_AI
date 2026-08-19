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
import com.example.agrosense.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FarmListFragment extends Fragment {

    private RecyclerView rvFarms;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddFarm;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_list, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        rvFarms = view.findViewById(R.id.rvFarms);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        fabAddFarm = view.findViewById(R.id.fabAddFarm);

        rvFarms.setLayoutManager(new LinearLayoutManager(requireContext()));

        fabAddFarm.setOnClickListener(v -> {
            if (getActivity() instanceof com.example.agrosense.MainActivity) {
                ((com.example.agrosense.MainActivity) getActivity()).loadFragment(new AddFarmFragment(), true);
            }
        });

        loadFarms();

        return view;
    }

    private void loadFarms() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            List<Farm> farms = db.farmDao().getFarmsByUser(userId);
            requireActivity().runOnUiThread(() -> {
                if (farms.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    rvFarms.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    rvFarms.setVisibility(View.VISIBLE);
                    rvFarms.setAdapter(new FarmAdapter(farms, farm -> {
                        if (getActivity() instanceof com.example.agrosense.MainActivity) {
                            ((com.example.agrosense.MainActivity) getActivity()).loadFragment(FarmDetailsFragment.newInstance(farm.id), true);
                        }
                    }));
                }
            });
        });
    }
}
