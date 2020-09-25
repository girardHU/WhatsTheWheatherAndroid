package com.example.whattheweather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {
    @Query("SELECT * FROM city")
    List<City> getAll();

    @Query("SELECT * FROM city WHERE uid = :uid")
    City loadById(int uid);

    @Query("SELECT * FROM city WHERE name = :name")
    City findByName(String name);

    @Query("SELECT * FROM city WHERE current=1")
    City getCurrentCity();

    @Query("UPDATE city SET current=1 WHERE name = :name")
    void changeCurrentState(String name);

    @Query("UPDATE city SET current=0")
    void resetAllCurrentState();

    @Query("UPDATE city SET current=0 WHERE current=1")
    void resetCurrentValue();

    @Query("DELETE FROM city WHERE name= :name")
    void deleteByName(String name);

    @Insert
    void insertAll(City... cities);

    @Delete
    void delete(City city);

    @Query("DELETE FROM city")
    void deleteAll();
}