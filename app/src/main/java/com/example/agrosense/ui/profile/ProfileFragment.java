package com.example.agrosense.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrosense.R;
import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.User;
import com.example.agrosense.utils.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvRole;
    private Button btnLogout, btnEditProfile;
    private SessionManager sessionManager;
    private AgroSenseDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db = AgroSenseDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvRole = view.findViewById(R.id.tvRole);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        loadUserData();

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new android.content.Intent(requireContext(), com.example.agrosense.ui.auth.LoginActivity.class));
            requireActivity().finishAffinity();
        });

        btnEditProfile.setOnClickListener(v -> Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show());

        return view;
    }

    private void loadUserData() {
        long userId = sessionManager.getUserId();
        executorService.execute(() -> {
            User user = db.userDao().getUserById(userId);
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    tvName.setText(user.fullName);
                    tvEmail.setText(user.email);
                    tvPhone.setText(user.phoneNumber);
                    tvRole.setText(user.role);
                });
            }
        });
    }
}
