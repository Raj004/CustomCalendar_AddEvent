package com.example.rajshekhar.calendarwithevents_sql.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Event.class},version = 1)
 public abstract class EventDB extends RoomDatabase {
    public abstract EventDao eventDao();


 }
