package com.sophie.miller.portablecloset.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "clothing_items")
public class ClothingItem implements Parcelable {
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

    protected ClothingItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        picture_address = in.readString();
        image = in.createByteArray();
        color = in.readInt();
        style = in.readInt();
        size = in.readString();
        note = in.readString();
    }

    public static final Creator<ClothingItem> CREATOR = new Creator<ClothingItem>() {
        @Override
        public ClothingItem createFromParcel(Parcel in) {
            return new ClothingItem(in);
        }

        @Override
        public ClothingItem[] newArray(int size) {
            return new ClothingItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(picture_address);
        dest.writeByteArray(image);
        dest.writeInt(color);
        dest.writeInt(style);
        dest.writeString(size);
        dest.writeString(note);
    }
}
