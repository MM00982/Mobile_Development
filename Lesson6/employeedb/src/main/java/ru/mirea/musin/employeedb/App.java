package ru.mirea.musin.employeedb;

import android.app.Application;

import androidx.room.Room;

import ru.mirea.musin.employeedb.data.AppDatabase;

/** Singleton-инициализатор базы. */
public class App extends Application {

    private static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        database = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "superhero.db")
                .allowMainThreadQueries()   // только для учебных примеров
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
