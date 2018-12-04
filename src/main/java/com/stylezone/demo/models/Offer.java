package com.stylezone.demo.models;

public class Offer {
    private int offerId;
    private String offerName;
    private String offerContent;
    private String offerStart;
    private String offerEnd;

    public Offer() {
    }

    public Offer(String offerName, String offerContent, String offerStart, String offerEnd) {
        this.offerName = offerName;
        this.offerContent = offerContent;
        this.offerStart = offerStart;
        this.offerEnd = offerEnd;
    }

    public Offer(int offerId, String offerName, String offerContent, String offerStart, String offerEnd) {
        this.offerId = offerId;
        this.offerName = offerName;
        this.offerContent = offerContent;
        this.offerStart = offerStart;
        this.offerEnd = offerEnd;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferContent() {
        return offerContent;
    }

    public void setOfferContent(String offerContent) {
        this.offerContent = offerContent;
    }

    public String getOfferStart() {
        return offerStart;
    }

    public void setOfferStart(String offerStart) {
        this.offerStart = offerStart;
    }

    public String getOfferEnd() {
        return offerEnd;
    }

    public void setOfferEnd(String offerEnd) {
        this.offerEnd = offerEnd;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "offerId=" + offerId +
                ", offerName='" + offerName + '\'' +
                ", offerContent='" + offerContent + '\'' +
                ", offerStart='" + offerStart + '\'' +
                ", offerEnd='" + offerEnd + '\'' +
                '}';
    }
}
