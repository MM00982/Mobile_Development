package ru.mirea.musin.employeedb.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Hero.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HeroDao heroDao();
}
