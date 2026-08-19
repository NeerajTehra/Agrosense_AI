package com.example.agrosense.ml;

public class PredictionResult {
    private final String diseaseName;
    private final String plantName;
    private final String cause;
    private final double confidence;
    private final String severity;
    private final boolean isHealthy;

    public PredictionResult(String plantName, String diseaseName, String cause, double confidence, String severity, boolean isHealthy) {
        this.plantName = plantName;
        this.diseaseName = diseaseName;
        this.cause = cause;
        this.confidence = confidence;
        this.severity = severity;
        this.isHealthy = isHealthy;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getCause() {
        return cause;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getSeverity() {
        return severity;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    @Override
    public String toString() {
        return "PredictionResult{" +
                "diseaseName='" + diseaseName + '\'' +
                ", confidence=" + confidence +
                ", severity='" + severity + '\'' +
                ", isHealthy=" + isHealthy +
                '}';
    }
}
