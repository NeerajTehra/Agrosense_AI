package com.example.agrosense.ml;

public class RiskEngine {

    /**
     * Calculates a risk score from 0-100.
     * Formula: (SeverityWeight * 0.5) + (ConfidenceWeight * 0.3) + (EnvWeight * 0.2)
     */
    public static int calculateRiskScore(String severity, double confidence, double temperature, double humidity) {
        double severityWeight = SeverityEstimator.getSeverityValue(severity) * 25.0; // 1->25, 4->100
        double confidenceWeight = confidence * 100.0;

        // Environmental risk: High humidity (>80%) and moderate temp (20-30°C) often increase disease risk
        double envWeight = 0;
        if (humidity > 80) envWeight += 50;
        if (temperature > 20 && temperature < 30) envWeight += 50;

        double finalScore = (severityWeight * 0.5) + (confidenceWeight * 0.3) + (envWeight * 0.2);

        return (int) Math.min(100, Math.max(0, finalScore));
    }

    public static String getRiskLevel(int score) {
        if (score >= 80) return "CRITICAL";
        if (score >= 60) return "HIGH";
        if (score >= 40) return "MODERATE";
        return "LOW";
    }
}
