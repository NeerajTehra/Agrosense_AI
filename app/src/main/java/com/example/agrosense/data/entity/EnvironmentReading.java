package com.example.agrosense.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "environment_readings",
        foreignKeys = @ForeignKey(entity = Field.class,
                parentColumns = "id",
                childColumns = "field_id",
                onDelete = ForeignKey.CASCADE))
public class EnvironmentReading {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "field_id")
    public long fieldId;

    @ColumnInfo(name = "temperature")
    public double temperature;

    @ColumnInfo(name = "humidity")
    public double humidity;

    @ColumnInfo(name = "soil_moisture")
    public double soilMoisture;

    @ColumnInfo(name = "timestamp")
    public long timestamp;
}
