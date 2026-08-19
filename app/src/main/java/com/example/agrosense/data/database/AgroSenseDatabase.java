package com.example.agrosense.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.agrosense.data.dao.*;
import com.example.agrosense.data.entity.*;

@Database(
        entities = {
                User.class,
                Farm.class,
                Field.class,
                Crop.class,
                Disease.class,
                Prediction.class,
                Alert.class,
                EnvironmentReading.class,
                Treatment.class
        },
        version = 2, // <-- Version 1 se badha kar 2 karein
        exportSchema = false
)
public abstract class AgroSenseDatabase extends RoomDatabase {

    private static volatile AgroSenseDatabase instance;

    public abstract UserDao userDao();
    public abstract FarmDao farmDao();
    public abstract FieldDao fieldDao();
    public abstract CropDao cropDao();
    public abstract DiseaseDao diseaseDao();
    public abstract PredictionDao predictionDao();
    public abstract AlertDao alertDao();
    public abstract EnvironmentReadingDao environmentReadingDao();
    public abstract TreatmentDao treatmentDao();

    public static synchronized AgroSenseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AgroSenseDatabase.class,
                            "agrosense_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}