package com.unishop;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.Double;
/**
 * Created by kaosp on 1/23/17.
 */

public class Listing implements Parcelable{

    public String title;
    public double[] bids = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    public String[] bidderID = {"", "", "", "", "", ""};
    public String description;
    public String imageURL;


    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeDouble(bids[0]);
        out.writeDouble(bids[1]);
        out.writeDouble(bids[2]);
        out.writeDouble(bids[3]);
        out.writeDouble(bids[4]);
        out.writeDouble(bids[5]);
        out.writeString(bidderID[0]);
        out.writeString(bidderID[1]);
        out.writeString(bidderID[2]);
        out.writeString(bidderID[3]);
        out.writeString(bidderID[4]);
        out.writeString(bidderID[5]);
        out.writeString(description);
        out.writeString(imageURL);
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Listing> CREATOR = new Parcelable.Creator<Listing>() {
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };
    private Listing(Parcel in) {
        title = in.readString();
        bids[0] = in.readDouble();
        bids[1] = in.readDouble();
        bids[2] = in.readDouble();
        bids[3] = in.readDouble();
        bids[4] = in.readDouble();
        bids[5] = in.readDouble();
        bidderID[0] = in.readString();
        bidderID[1] = in.readString();
        bidderID[2] = in.readString();
        bidderID[3] = in.readString();
        bidderID[4] = in.readString();
        bidderID[5] = in.readString();
        description = in.readString();
        imageURL = in.readString();
    }

    public Listing(String title, double zeroBid, double oneBid, double twoBid, double threeBid,
            double fourBid, double fiveBid, String description, String imageURL) {

        this.title = title;

        this.bids[0] = zeroBid;
        this.bids[1] = oneBid;
        this.bids[2] = twoBid;
        this.bids[3] = threeBid;
        this.bids[4] = fourBid;
        this.bids[5] = fiveBid;


        this.description = description;
        this.imageURL = imageURL;
    }
    public void setZeroStarID(String id) {
        this.bidderID[0] = id;
    }
    public String getZeroStarID() {
        return this.bidderID[0];
    }
    public void setOneStarID(String id) {
        this.bidderID[0] = id;
    }
    public String getOneStarID() {
        return this.bidderID[0];
    }
    public void setTwoStarID(String id) {
        this.bidderID[0] = id;
    }
    public String getTwoStarID() {
        return this.bidderID[0];
    }
    public void setThreeStarID(String id) {
        this.bidderID[0] = id;
    }
    public String getThreeStarID() {
        return this.bidderID[0];
    }
    public void setFourStarID(String id) {
        this.bidderID[0] = id;
    }
    public String getFourStarID() {
        return this.bidderID[0];
    }
    public void setFiveStarID(String id) {
        this.bidderID[0] = id;
    }
    public String getFiveStarID() {
        return this.bidderID[0];
    }
    public void setZeroStarBid(double d) {
        bids[0] = d;
    }
    public double getZeroStarBid() {
        return bids[0];
    }
    public void setOneStarBid(double d) {
        bids[1] = d;
    }
    public double getOneStarBid() {
        return bids[1];
    }
    public void setTwoStarBid(double d) {
        bids[2] = d;
    }
    public double getTwoStarBid() {
        return bids[2];
    }
    public void setThreeStarBid(double d) {
        bids[3] = d;
    }
    public double getThreeStarBid() {
        return bids[3];
    }
    public void setFourStarBid(double d) {
        bids[4] = d;
    }
    public double getFourStarBid() {
        return bids[4];
    }
    public void setFiveStarBid(double d) {
        bids[5] = d;
    }
    public double getFiveStarBid() {
        return bids[5];
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
