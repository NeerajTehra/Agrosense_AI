package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agrosense.data.entity.Farm;

import java.util.List;

@Dao
public interface FarmDao {
    @Insert
    long insert(Farm farm);

    @Update
    void update(Farm farm);

    @Delete
    void delete(Farm farm);

    @Query("SELECT * FROM farms WHERE user_id = :userId")
    List<Farm> getFarmsByUser(long userId);

    @Query("SELECT * FROM farms WHERE id = :id")
    Farm getFarmById(long id);

    @Query("SELECT COUNT(*) FROM farms WHERE user_id = :userId")
    int countFarmsByUser(long userId);

    @Query("SELECT COUNT(*) FROM farms")
    int countAllFarms();
}
