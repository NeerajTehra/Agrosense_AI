package com.example.agrosense.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class OnboardingManager {
    private static final String PREF_NAME = "AgroSenseOnboarding";
    private static final String KEY_HAS_SEEN_ONBOARDING = "hasSeenOnboarding";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public OnboardingManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public boolean hasSeenOnboarding() {
        return pref.getBoolean(KEY_HAS_SEEN_ONBOARDING, false);
    }

    public void setOnboardingCompleted() {
        editor.putBoolean(KEY_HAS_SEEN_ONBOARDING, true);
        editor.commit();
    }
}
