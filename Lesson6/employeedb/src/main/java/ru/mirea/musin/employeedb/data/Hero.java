package ru.mirea.musin.employeedb.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hero")
public class Hero {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String superPower;
    public String universe;
    public int strengthLevel;

    public Hero() { }

    public Hero(String name, String superPower, String universe, int strengthLevel) {
        this.name = name;
        this.superPower = superPower;
        this.universe = universe;
        this.strengthLevel = strengthLevel;
    }
}
