package com.example.agrosense.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.agrosense.data.dao.AlertDao;
import com.example.agrosense.data.dao.CropDao;
import com.example.agrosense.data.dao.DiseaseDao;
import com.example.agrosense.data.dao.EnvironmentReadingDao;
import com.example.agrosense.data.dao.FarmDao;
import com.example.agrosense.data.dao.FieldDao;
import com.example.agrosense.data.dao.PredictionDao;
import com.example.agrosense.data.dao.TreatmentDao;
import com.example.agrosense.data.dao.UserDao;
import com.example.agrosense.data.entity.Alert;
import com.example.agrosense.data.entity.Crop;
import com.example.agrosense.data.entity.Disease;
import com.example.agrosense.data.entity.EnvironmentReading;
import com.example.agrosense.data.entity.Farm;
import com.example.agrosense.data.entity.Field;
import com.example.agrosense.data.entity.Prediction;
import com.example.agrosense.data.entity.Treatment;
import com.example.agrosense.data.entity.User;

@Database(entities = {
        User.class, Farm.class, Field.class, Crop.class,
        Disease.class, Prediction.class, Treatment.class,
        EnvironmentReading.class, Alert.class
}, version = 1, exportSchema = false)
public abstract class AgroSenseDatabase extends RoomDatabase {

    private static volatile AgroSenseDatabase instance;

    public abstract UserDao userDao();
    public abstract FarmDao farmDao();
    public abstract FieldDao fieldDao();
    public abstract CropDao cropDao();
    public abstract DiseaseDao diseaseDao();
    public abstract PredictionDao predictionDao();
    public abstract TreatmentDao treatmentDao();
    public abstract EnvironmentReadingDao environmentReadingDao();
    public abstract AlertDao alertDao();

    public static AgroSenseDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AgroSenseDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AgroSenseDatabase.class, "agrosense_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
