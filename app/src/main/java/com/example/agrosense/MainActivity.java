package com.example.agrosense;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.agrosense.ui.auth.LoginActivity;
import com.example.agrosense.ui.dashboard.AdminDashboardFragment;
import com.example.agrosense.ui.dashboard.ComingSoonFragment;
import com.example.agrosense.ui.dashboard.FarmerDashboardFragment;
import com.example.agrosense.ui.dashboard.FieldWorkerDashboardFragment;
import com.example.agrosense.ui.farm.FarmListFragment;
import com.example.agrosense.ui.field.FieldListFragment;
import com.example.agrosense.ui.scanner.DiseaseScannerFragment;
import com.example.agrosense.ui.scanner.PredictionHistoryFragment;
import com.example.agrosense.ui.profile.ProfileFragment;
import com.example.agrosense.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        // Default fragment
        if (savedInstanceState == null) {
            loadFragment(getDashboardFragment(), false);
        }
    }

    private void setupBottomNavigation() {
        String role = sessionManager.getUserRole();
        int menuResId;

        switch (role) {
            case "ADMIN":
                menuResId = R.menu.menu_admin;
                break;
            case "FIELD_WORKER":
                menuResId = R.menu.menu_field_worker;
                break;
            case "FARMER":
            default:
                menuResId = R.menu.menu_farmer;
                break;
        }

        bottomNavigation.getMenu().clear();
        bottomNavigation.inflateMenu(menuResId);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(getDashboardFragment(), false);
                return true;
            } else if (id == R.id.nav_farms) {
                loadFragment(new FarmListFragment(), false);
                return true;
            } else if (id == R.id.nav_fields) {
                loadFragment(new FieldListFragment(), false);
                return true;
            } else if (id == R.id.nav_scanner) {
                loadFragment(new DiseaseScannerFragment(), false);
                return true;
            } else if (id == R.id.nav_history) {
                loadFragment(new PredictionHistoryFragment(), false);
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment(), false);
                return true;
            } else if (id == R.id.nav_analytics || id == R.id.nav_alerts || id == R.id.nav_users || id == R.id.nav_reports) {
                loadFragment(new ComingSoonFragment(), false);
                return true;
            }
            return false;
        });
    }

    private Fragment getDashboardFragment() {
        String role = sessionManager.getUserRole();
        switch (role) {
            case "ADMIN":
                return new AdminDashboardFragment();
            case "FIELD_WORKER":
                return new FieldWorkerDashboardFragment();
            case "FARMER":
            default:
                return new FarmerDashboardFragment();
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void navigateToHome() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
