package com.example.etoo.ertugruluzun;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EarthquakeDao {

    @Query("SELECT * FROM earhthquake")
    List<Earthquake> getAllEarthquakes();

    @Query("DELETE FROM Earhthquake")
    void deleteAllArchive();

    @Insert
    void insertAll(Earthquake... earthquakes);

    @Delete
    void deleteSingleEarthquake(Earthquake earthquake);



}
