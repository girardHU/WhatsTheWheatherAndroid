package com.example.whattheweather.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {City.class}, version = 2)
public abstract class CityDatabase extends RoomDatabase {
    public abstract CityDao cityDao();

    private static volatile CityDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 1;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    public static CityDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (CityDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CityDatabase.class, "city_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}