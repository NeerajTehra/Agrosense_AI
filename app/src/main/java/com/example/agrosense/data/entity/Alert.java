package com.example.agrosense.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "alerts",
        foreignKeys = @ForeignKey(entity = Field.class,
                parentColumns = "id",
                childColumns = "field_id",
                onDelete = ForeignKey.CASCADE))
public class Alert {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "field_id")
    public Long fieldId; // Optional

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "level")
    public String level; // INFO, WARNING, CRITICAL

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "is_read")
    public boolean isRead;
}
