package com.lypeer.ipcclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lypeer on 16-7-20.
 */
public class ParcelableTest implements Parcelable{

    private String name;
    private int price;

    protected ParcelableTest(Parcel in) {
        name = in.readString();
        price = in.readInt();
    }

    public static final Creator<ParcelableTest> CREATOR = new Creator<ParcelableTest>() {
        @Override
        public ParcelableTest createFromParcel(Parcel in) {
            return new ParcelableTest(in);
        }

        @Override
        public ParcelableTest[] newArray(int size) {
            return new ParcelableTest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(price);
    }
}
