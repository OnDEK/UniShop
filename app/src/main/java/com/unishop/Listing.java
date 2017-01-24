package com.unishop;
import java.lang.Double;
/**
 * Created by kaosp on 1/23/17.
 */

public class Listing {

    String title;
    double[] bids = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    String description;
    String imageURL;

    Listing() {

    }
    Listing(String title, double zeroBid, double oneBid, double twoBid, double threeBid,
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
