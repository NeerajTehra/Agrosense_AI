package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agrosense.data.entity.Disease;

import java.util.List;

@Dao
public interface DiseaseDao {
    @Insert
    long insert(Disease disease);

    @Update
    void update(Disease disease);

    @Delete
    void delete(Disease disease);

    @Query("SELECT * FROM diseases")
    List<Disease> getAllDiseases();

    @Query("SELECT * FROM diseases WHERE name = :name LIMIT 1")
    Disease getDiseaseByName(String name);

    @Query("SELECT * FROM diseases WHERE id = :id")
    Disease getDiseaseById(long id);
}
