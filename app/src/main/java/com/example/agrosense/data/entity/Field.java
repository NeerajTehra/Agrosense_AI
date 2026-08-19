package com.example.agrosense.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "fields",
        foreignKeys = @ForeignKey(entity = Farm.class,
                parentColumns = "id",
                childColumns = "farm_id",
                onDelete = ForeignKey.CASCADE))
public class Field {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "farm_id")
    public long farmId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "crop_type")
    public String cropType;

    @ColumnInfo(name = "area")
    public double area;
}
