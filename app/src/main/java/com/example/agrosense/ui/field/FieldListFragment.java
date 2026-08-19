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
import com.example.agrosense.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FieldListFragment extends Fragment {

    private RecyclerView rvFields;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddField;
    private AgroSenseDatabase db;
    private SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_list, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        rvFields = view.findViewById(R.id.rvFields);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        fabAddField = view.findViewById(R.id.fabAddField);

        rvFields.setLayoutManager(new LinearLayoutManager(requireContext()));

        fabAddField.setOnClickListener(v -> {
            if (getActivity() instanceof com.example.agrosense.MainActivity) {
                ((com.example.agrosense.MainActivity) getActivity()).loadFragment(new AddFieldFragment(), true);
            }
        });

        loadFields();

        return view;
    }

    private void loadFields() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            // This is a bit simplified, ideally query fields by userId directly
            // For now, let's get all fields if admin, or fields for user's farms
            List<Field> allFields = new ArrayList<>();
            List<com.example.agrosense.data.entity.Farm> userFarms = db.farmDao().getFarmsByUser(userId);
            for (com.example.agrosense.data.entity.Farm farm : userFarms) {
                allFields.addAll(db.fieldDao().getFieldsByFarm(farm.id));
            }

            requireActivity().runOnUiThread(() -> {
                if (allFields.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    rvFields.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    rvFields.setVisibility(View.VISIBLE);
                    rvFields.setAdapter(new FieldAdapter(allFields, field -> {
                        if (getActivity() instanceof com.example.agrosense.MainActivity) {
                            ((com.example.agrosense.MainActivity) getActivity()).loadFragment(FieldDetailsFragment.newInstance(field.id), true);
                        }
                    }));
                }
            });
        });
    }
}
