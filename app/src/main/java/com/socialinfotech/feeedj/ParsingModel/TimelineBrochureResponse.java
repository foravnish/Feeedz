package com.socialinfotech.feeedj.ParsingModel;

/**
 * Created by fi8er1 on 23/10/2017.
 */

public class TimelineBrochureResponse {

    private String CompanyProfilePhoto;
    private String CompanyName_Ar;
    private int OfferId;
    private String BrochureBadge;
    private String AttachmentHTML;
    private String OfferEndType;
    private String OfferLocation;
    private String phoneNumber;
    private String OfferTimeEnd;
    private int offerRating;
    private int Type;
    private String OfferImage;
    private String OfferTitle;
    private int CompanyTagId;
    private int CompanyId;

    private boolean CompanyVerified;
    private String CompanyName;


    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public void setOfferId(int OfferId) {
        this.OfferId = OfferId;
    }

    public int getOfferId() {
        return OfferId;
    }

    public String getOfferImage() {
        return OfferImage;
    }

    public void setOfferImage(String OfferImage) {
        this.OfferImage = OfferImage;
    }

    public void setCompanyProfilePhoto(String CompanyProfilePhoto) {
        this.CompanyProfilePhoto = CompanyProfilePhoto;
    }

    public String getCompanyProfilePhoto() {
        return CompanyProfilePhoto;
    }

    public void setCompanyName_Ar(String CompanyName_Ar) {
        this.CompanyName_Ar = CompanyName_Ar;
    }

    public String getCompanyName_Ar() {
        return CompanyName_Ar;
    }

    public void setBrochureBadge(String BrochureBadge) {
        this.BrochureBadge = BrochureBadge;
    }

    public String getBrochureBadge() {
        return BrochureBadge;
    }

    public void setAttachmentHTML(String AttachmentHTML) {
        this.AttachmentHTML = AttachmentHTML;
    }

    public String getAttachmentHTML() {
        return AttachmentHTML;
    }


    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }


    public Boolean isCompanyVerified() {
        return CompanyVerified;
    }


    public String getOfferLocation() {
        return OfferLocation;
    }

    public void setOfferLocation(String OfferLocation) {
        this.OfferLocation = OfferLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public String getOfferTimeEnd() {
        return OfferTimeEnd;
    }

    public void setOfferTimeEnd(String OfferTimeEnd) {
        this.OfferTimeEnd = OfferTimeEnd;
    }

    public String getOfferTitle() {
        return OfferTitle;
    }

    public void setOfferTitle(String OfferTitle) {
        this.OfferTitle = OfferTitle;
    }

    public int getOfferRating() {
        return offerRating;
    }

    public void setOfferRating(int offerRating) {
        this.offerRating = offerRating;
    }

    public String getOfferEndType() {
        return OfferEndType;
    }

    public void setOfferEndType(String OfferEndType) {
        this.OfferEndType = OfferEndType;
    }


    public int getCompanyTagId() {
        return CompanyTagId;
    }

    public void setCompanyTagId(int CompanyTagId) {
        this.CompanyTagId = CompanyTagId;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int CompanyId) {
        this.CompanyId = CompanyId;
    }

}