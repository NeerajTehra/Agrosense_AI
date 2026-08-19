package com.example.agrosense.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "diseases")
public class Disease {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private String cropApplicability;
    private String symptoms;
    private String cause;
    private String treatment;
    private String prevention;

    public Disease() {}

    public Disease(String name, String cropApplicability, String symptoms, String cause, String treatment, String prevention) {
        this.name = name;
        this.cropApplicability = cropApplicability;
        this.symptoms = symptoms;
        this.cause = cause;
        this.treatment = treatment;
        this.prevention = prevention;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCropApplicability() { return cropApplicability; }
    public void setCropApplicability(String cropApplicability) { this.cropApplicability = cropApplicability; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getCause() { return cause; }
    public void setCause(String cause) { this.cause = cause; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getPrevention() { return prevention; }
    public void setPrevention(String prevention) { this.prevention = prevention; }
}