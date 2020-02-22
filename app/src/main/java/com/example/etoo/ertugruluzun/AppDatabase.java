package com.example.etoo.ertugruluzun;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/*
I created this class by following this subject
http://tugbaustundag.com/room-kutuphanesinin-kullanimi/
 */


@Database(entities = {Earthquake.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract EarthquakeDao earthquakeDao();

}
