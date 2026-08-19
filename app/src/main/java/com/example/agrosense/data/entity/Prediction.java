package com.example.agrosense.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "predictions",
        foreignKeys = {
                @ForeignKey(entity = Crop.class,
                        parentColumns = "id",
                        childColumns = "crop_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Disease.class,
                        parentColumns = "id",
                        childColumns = "disease_id",
                        onDelete = ForeignKey.SET_NULL)
        })
public class Prediction {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "crop_id")
    public long cropId;

    @ColumnInfo(name = "disease_id")
    public Long diseaseId; // Can be null if healthy

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "confidence")
    public double confidence;

    @ColumnInfo(name = "severity")
    public String severity; // LOW, MODERATE, HIGH, CRITICAL

    @ColumnInfo(name = "image_path")
    public String imagePath;

    @ColumnInfo(name = "is_healthy")
    public boolean isHealthy;

    @ColumnInfo(name = "detected_plant")
    public String detectedPlant; // e.g., "Tomato"

    @ColumnInfo(name = "cause")
    public String cause;
}
