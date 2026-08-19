package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agrosense.data.entity.Treatment;

import java.util.List;

@Dao
public interface TreatmentDao {
    @Insert
    long insert(Treatment treatment);

    @Update
    void update(Treatment treatment);

    @Delete
    void delete(Treatment treatment);

    @Query("SELECT * FROM treatments WHERE field_id = :fieldId ORDER BY date_applied DESC")
    List<Treatment> getTreatmentsByField(long fieldId);
}
