package com.example.agrosense.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.agrosense.R;
import com.example.agrosense.ui.auth.LoginActivity;
import com.example.agrosense.utils.OnboardingManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private MaterialButton btnNext;
    private OnboardingManager onboardingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Force Light Mode for Onboarding
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        onboardingManager = new OnboardingManager(this);

        viewPager = findViewById(R.id.viewPagerOnboarding);
        btnNext = findViewById(R.id.btnNext);
        TextView tvSkip = findViewById(R.id.tvSkip);
        TabLayout tabLayout = findViewById(R.id.tabIndicator);

        // Apply Window Insets for Responsive Layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onboarding_root), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, insets.top, 0, insets.bottom);
            return windowInsets;
        });

        List<OnboardingAdapter.OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingAdapter.OnboardingItem(
                R.drawable.agrosense_logo,
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_subtitle_1)
        ));
        items.add(new OnboardingAdapter.OnboardingItem(
                R.drawable.agrosense_logo,
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_subtitle_2)
        ));
        items.add(new OnboardingAdapter.OnboardingItem(
                R.drawable.agrosense_logo,
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_subtitle_3)
        ));
        items.add(new OnboardingAdapter.OnboardingItem(
                R.drawable.agrosense_logo,
                getString(R.string.onboarding_title_4),
                getString(R.string.onboarding_subtitle_4)
        ));

        OnboardingAdapter adapter = new OnboardingAdapter(items);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Indicators are handled by tabBackground selector in XML
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == items.size() - 1) {
                    btnNext.setText(R.string.btn_get_started);
                } else {
                    btnNext.setText(R.string.btn_next);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < items.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                completeOnboarding();
            }
        });

        tvSkip.setOnClickListener(v -> completeOnboarding());
    }

    private void completeOnboarding() {
        onboardingManager.setOnboardingCompleted();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
