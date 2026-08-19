package com.example.agrosense.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "treatments",
        foreignKeys = @ForeignKey(entity = Field.class,
                parentColumns = "id",
                childColumns = "field_id",
                onDelete = ForeignKey.CASCADE))
public class Treatment {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "field_id")
    public long fieldId;

    @ColumnInfo(name = "action")
    public String action;

    @ColumnInfo(name = "date_applied")
    public long dateApplied;

    @ColumnInfo(name = "notes")
    public String notes;

    @ColumnInfo(name = "cost")
    public double cost;
}
