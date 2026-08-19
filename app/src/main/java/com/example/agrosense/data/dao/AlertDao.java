package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agrosense.data.entity.Alert;

import java.util.List;

@Dao
public interface AlertDao {
    @Insert
    long insert(Alert alert);

    @Update
    void update(Alert alert);

    @Query("SELECT * FROM alerts ORDER BY timestamp DESC")
    List<Alert> getAllAlerts();

    @Query("SELECT * FROM alerts WHERE is_read = 0")
    List<Alert> getUnreadAlerts();

    @Query("SELECT COUNT(*) FROM alerts WHERE is_read = 0")
    int getUnreadCount();

    @Query("SELECT * FROM alerts ORDER BY timestamp DESC LIMIT :limit")
    List<Alert> getRecentAlerts(int limit);
}
