package com.example.agrosense.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agrosense.data.entity.Crop;

import java.util.List;

@Dao
public interface CropDao {
    @Insert
    long insert(Crop crop);

    @Update
    void update(Crop crop);

    @Delete
    void delete(Crop crop);

    @Query("SELECT * FROM crops WHERE field_id = :fieldId")
    List<Crop> getCropsByField(long fieldId);

    @Query("SELECT * FROM crops WHERE id = :id")
    Crop getCropById(long id);

    @Query("SELECT COUNT(*) FROM crops c JOIN fields fi ON c.field_id = fi.id JOIN farms fa ON fi.farm_id = fa.id WHERE fa.user_id = :userId AND c.status = 'GROWING'")
    int countActiveCropsByUser(long userId);
}
