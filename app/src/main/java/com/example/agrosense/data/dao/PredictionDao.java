package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.agrosense.data.entity.Prediction;

import java.util.List;

@Dao
public interface PredictionDao {
    @Insert
    long insert(Prediction prediction);

    @Query("SELECT * FROM predictions ORDER BY timestamp DESC")
    List<Prediction> getAllPredictions();

    @Query("SELECT * FROM predictions WHERE crop_id = :cropId ORDER BY timestamp DESC")
    List<Prediction> getPredictionsByCrop(long cropId);

    @Query("SELECT * FROM predictions WHERE disease_id = :diseaseId")
    List<Prediction> getPredictionsByDisease(long diseaseId);

    @Query("SELECT * FROM predictions WHERE severity = :severity")
    List<Prediction> getPredictionsBySeverity(String severity);

    @Query("SELECT COUNT(*) FROM predictions")
    int getPredictionCount();

    @Query("SELECT COUNT(*) FROM predictions WHERE is_healthy = 0")
    int getInfectionCount();

    @Query("SELECT * FROM predictions WHERE timestamp >= :startTime AND timestamp <= :endTime")
    List<Prediction> getPredictionsInDateRange(long startTime, long endTime);

    @Query("SELECT * FROM predictions ORDER BY timestamp DESC LIMIT :limit")
    List<Prediction> getRecentPredictions(int limit);

    @Query("SELECT * FROM predictions WHERE id = :id")
    Prediction getPredictionById(long id);

    @Query("SELECT p.* FROM predictions p JOIN crops c ON p.crop_id = c.id JOIN fields fi ON c.field_id = fi.id JOIN farms fa ON fi.farm_id = fa.id WHERE fa.user_id = :userId ORDER BY p.timestamp DESC")
    List<Prediction> getPredictionsByUser(long userId);
}
