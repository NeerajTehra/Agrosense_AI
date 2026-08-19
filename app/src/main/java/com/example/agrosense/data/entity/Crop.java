package com.example.agrosense.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "crops",
        foreignKeys = @ForeignKey(entity = Field.class,
                parentColumns = "id",
                childColumns = "field_id",
                onDelete = ForeignKey.CASCADE))
public class Crop {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "field_id")
    public long fieldId;

    @ColumnInfo(name = "variety")
    public String variety;

    @ColumnInfo(name = "planting_date")
    public long plantingDate;

    @ColumnInfo(name = "status")
    public String status; // e.g., "GROWING", "HARVESTED"
}
