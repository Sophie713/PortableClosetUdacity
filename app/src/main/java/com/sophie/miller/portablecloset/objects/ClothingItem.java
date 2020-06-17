package com.sophie.miller.portablecloset.objects;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "clothing_items")
public class ClothingItem {
    //primary key
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name = "";
    private String picture_address = "";
    private byte[] image;
    private int color = -1;
    private int style = -1;
    private String size = "";
    private String note = "";


    public ClothingItem(int id, String name, byte[] image, int color, int style, String size, String note) {
        this.id = id;
        this.name = name;
        this.image = image;
        //this.picture_address = picture_address;
        this.color = color;
        this.style = style;
        this.size = size;
        this.note = note;
    }

    @Ignore
    public ClothingItem(String name, byte[] image, int color, int style, String size, String note) {
        this.name = name;
        this.image = image;
        //this.picture_address = picture_address;
        this.color = color;
        this.style = style;
        this.size = size;
        this.note = note;
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

    public String getPicture_address() {
        return picture_address;
    }

    public void setPicture_address(String picture_address) {
        this.picture_address = picture_address;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
