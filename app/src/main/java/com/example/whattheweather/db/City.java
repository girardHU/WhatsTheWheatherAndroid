package com.example.whattheweather.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class City {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "current")
    public boolean current;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "country")
    public String country;

    public City(boolean current, long timestamp, String name, String country) {
        this.current = current;
        this.timestamp = timestamp;
        this.name = name;
        this.country = country;
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
        return sdf.format(new Date(timestamp));
    }

    public String getDebugString() {
        return "UID: " + uid + " Current: " + current + " Timestamp: " + getDateString() + " Name: " + name + " country: " + country;
    }

    public String getName() {
        return name;
    }

    public Boolean getCurrent() {
        return current;
    }

    public String getCountry() {
        return country;
    }

    public void changeVersion(int migration_1, int migration_2) {
        final Migration MIGRATION_1_2 = new Migration(migration_1, migration_2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("DROP TABLE city");
            }
        };
    }
}
