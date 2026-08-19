package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agrosense.data.entity.Field;

import java.util.List;

@Dao
public interface FieldDao {
    @Insert
    long insert(Field field);

    @Update
    void update(Field field);

    @Delete
    void delete(Field field);

    @Query("SELECT * FROM fields WHERE farm_id = :farmId")
    List<Field> getFieldsByFarm(long farmId);

    @Query("SELECT COUNT(*) FROM fields")
    int getFieldCount();

    @Query("SELECT SUM(area) FROM fields")
    double getTotalArea();

    @Query("SELECT COUNT(*) FROM fields f JOIN farms fa ON f.farm_id = fa.id WHERE fa.user_id = :userId")
    int countFieldsByUser(long userId);

    @Query("SELECT * FROM fields WHERE id = :id")
    Field getFieldById(long id);

    @Query("SELECT COUNT(*) FROM fields WHERE farm_id = :farmId")
    int countFieldsByFarm(long farmId);
}
