package com.ljd.aidl.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ljd-PC on 2016/2/16.
 */
public class ComputerEntity implements Parcelable{

    public int computerId;     //id
    public String brand;       //品牌
    public String model;       //型号

    public ComputerEntity(int computerId, String brand, String model) {
        this.brand = brand;
        this.computerId = computerId;
        this.model = model;
    }

    protected ComputerEntity(Parcel in) {
        computerId = in.readInt();
        brand = in.readString();
        model = in.readString();
    }

    public static final Creator<ComputerEntity> CREATOR = new Creator<ComputerEntity>() {
        @Override
        public ComputerEntity createFromParcel(Parcel in) {
            return new ComputerEntity(in);
        }

        @Override
        public ComputerEntity[] newArray(int size) {
            return new ComputerEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(computerId);
        dest.writeString(brand);
        dest.writeString(model);
    }
}
