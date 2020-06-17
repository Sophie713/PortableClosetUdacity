package com.sophie.miller.portablecloset.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class ClothesFilter implements Parcelable {

    private String sizeFilter = "";
    private int styleFilter = -1;
    private int colorFilter = -1;

    public ClothesFilter(String sizeFilter, int styleFilter, int colorFilter) {
        this.colorFilter = colorFilter;
        this.styleFilter = styleFilter;
        this.sizeFilter = sizeFilter;
    }

    public ClothesFilter() {
    }

    protected ClothesFilter(Parcel in) {
        sizeFilter = in.readString();
        styleFilter = in.readInt();
        colorFilter = in.readInt();
    }

    public static final Creator<ClothesFilter> CREATOR = new Creator<ClothesFilter>() {
        @Override
        public ClothesFilter createFromParcel(Parcel in) {
            return new ClothesFilter(in);
        }

        @Override
        public ClothesFilter[] newArray(int size) {
            return new ClothesFilter[size];
        }
    };

    public int getColorFilter() {
        return colorFilter;
    }

    public void setColorFilter(int colorFilter) {
        this.colorFilter = colorFilter;
    }

    public int getStyleFilter() {
        return styleFilter;
    }

    public void setStyleFilter(int styleFilter) {
        this.styleFilter = styleFilter;
    }

    public String getSizeFilter() {
        return sizeFilter;
    }

    public void setSizeFilter(String sizeFilter) {
        this.sizeFilter = sizeFilter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sizeFilter);
        dest.writeInt(styleFilter);
        dest.writeInt(colorFilter);
    }
}
