package com.indraneel.beerinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO container for Beer objects
 */

public class Beer implements Parcelable {

    private int mId;
    private String mName;
    private String mTagline;
    private Double mAbv;
    private Double mIbu;
    private Double mEbc;
    private String mDescription;
    private String mFirstBrewed;
    private List<String> mFoodPairing;
    private String mBrewersTips;

    public Beer() {

    }

    public Beer(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mTagline = in.readString();
        this.mAbv = in.readDouble();
        this.mIbu = in.readDouble();
        this.mEbc = in.readDouble();
        this.mDescription = in.readString();
        this.mFirstBrewed = in.readString();
        this.mFoodPairing = new ArrayList<>();
        in.readList( this.mFoodPairing, null);
        this.mBrewersTips = in.readString();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String tagline) {
        this.mTagline = tagline;
    }

    public double getAbv() {
        return mAbv;
    }

    public void setAbv(double abv) {
        this.mAbv = abv;
    }

    public double getIbu() {
        return mIbu;
    }

    public void setIbu(double ibu) {
        this.mIbu = ibu;
    }

    public double getEbc() {
        return mEbc;
    }

    public void setEbc(double ebc) {
        this.mEbc = ebc;
    }


    public String getBrewersTips() {
        return mBrewersTips;
    }

    public void setBrewersTips(String brewersTips) {
        this.mBrewersTips = brewersTips;
    }

    public List<String> getFoodPairing() {
        return mFoodPairing;
    }

    public void setFoodPairing(List<String> foodPairing) {
        this.mFoodPairing = foodPairing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeString(mTagline);
        parcel.writeDouble(mAbv);
        parcel.writeDouble(mIbu);
        parcel.writeDouble(mEbc);
        parcel.writeString(mDescription);
        parcel.writeString(mFirstBrewed);
        parcel.writeList(mFoodPairing);
        parcel.writeString(mBrewersTips);
    }

    public static final Parcelable.Creator<Beer> CREATOR = new Parcelable.Creator<Beer>() {

        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        public Beer[] newArray(int size) {
            return new Beer[size];
        }
    };

    public String getFirstBrewed() {
        return mFirstBrewed;
    }

    public void setFirstBrewed(String firstBrewed) {
        this.mFirstBrewed = firstBrewed;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
