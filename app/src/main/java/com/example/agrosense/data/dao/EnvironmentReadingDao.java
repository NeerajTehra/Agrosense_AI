package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.agrosense.data.entity.EnvironmentReading;

import java.util.List;

@Dao
public interface EnvironmentReadingDao {
    @Insert
    long insert(EnvironmentReading reading);

    @Query("SELECT * FROM environment_readings WHERE field_id = :fieldId ORDER BY timestamp DESC")
    List<EnvironmentReading> getReadingsByField(long fieldId);

    @Query("SELECT * FROM environment_readings WHERE field_id = :fieldId AND timestamp >= :startTime ORDER BY timestamp ASC")
    List<EnvironmentReading> getRecentReadings(long fieldId, long startTime);
}
