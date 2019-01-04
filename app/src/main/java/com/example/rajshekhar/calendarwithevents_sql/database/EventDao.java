package com.example.rajshekhar.calendarwithevents_sql.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM tbl_event")
    List<Event>getAllEvents();

    @Insert
    void insertEvent(Event event);


    @Insert
    void insertEvent(List<Event>events);

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);



}
