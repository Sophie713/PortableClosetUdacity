package com.sophie.miller.portablecloset.objects;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "styles")
public class Style {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public Style(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Style(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
