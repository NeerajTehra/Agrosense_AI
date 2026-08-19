package com.example.agrosense.ml;

public class SeverityEstimator {

    /**
     * Estimates severity level based on confidence score and metadata.
     * LOW: 0 - 0.7
     * MODERATE: 0.7 - 0.85
     * HIGH: 0.85 - 0.95
     * CRITICAL: 0.95 - 1.0
     */
    public static String estimateSeverity(double confidence) {
        if (confidence >= 0.95) {
            return "CRITICAL";
        } else if (confidence >= 0.85) {
            return "HIGH";
        } else if (confidence >= 0.70) {
            return "MODERATE";
        } else {
            return "LOW";
        }
    }

    public static int getSeverityValue(String severity) {
        switch (severity) {
            case "CRITICAL": return 4;
            case "HIGH": return 3;
            case "MODERATE": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }
}
