package com.example.agrosense.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "diseases")
public class Disease {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "symptoms")
    public String symptoms;

    @ColumnInfo(name = "causative_agent")
    public String causativeAgent;

    @ColumnInfo(name = "treatment_recommendation")
    public String treatmentRecommendation;

    @ColumnInfo(name = "prevention")
    public String prevention;

    @ColumnInfo(name = "crop_applicability")
    public String cropApplicability; // e.g., "Tomato, Potato"
}
