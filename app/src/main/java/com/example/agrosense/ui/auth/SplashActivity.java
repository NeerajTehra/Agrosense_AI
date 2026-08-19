package com.example.agrosense.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agrosense.MainActivity;
import com.example.agrosense.R;
import com.example.agrosense.ui.onboarding.OnboardingActivity;
import com.example.agrosense.utils.DemoDataSeeder;
import com.example.agrosense.utils.OnboardingManager;
import com.example.agrosense.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // UI Element References
        LinearLayout brandContainer = findViewById(R.id.brand_container);
        android.view.View logo = findViewById(R.id.splash_logo);
        LinearLayout loadingContainer = findViewById(R.id.loading_container);

        // Load and Start Animations
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_scale);
        Animation fadeUpAnim = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in);
        
        logo.startAnimation(logoAnim);
        brandContainer.startAnimation(fadeUpAnim);
        loadingContainer.startAnimation(fadeUpAnim);

        // Seed Demo Data
        DemoDataSeeder.seedDiseases(this);

        // Navigation Logic
        new Handler().postDelayed(() -> {
            OnboardingManager onboardingManager = new OnboardingManager(SplashActivity.this);
            SessionManager sessionManager = new SessionManager(SplashActivity.this);

            if (!onboardingManager.hasSeenOnboarding()) {
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            } else if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 2200);
    }
}
