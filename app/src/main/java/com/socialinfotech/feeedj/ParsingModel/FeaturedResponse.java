package com.socialinfotech.feeedj.ParsingModel;

import java.util.List;

/**
 * Created by Social Infotech on 6/27/2016.
 */
public class FeaturedResponse {

    /**
     * CompanyId : 64
     * Featured : false
     * CompanyName : @Danube
     * WebSite : Danubeco.com
     * PhoneNumber : +966126545557
     * CompanyName_Ar : الدانوب
     * CompanyDescription : حساب الدانوب هايبرماركت على فيييدز. تابعونا لمعرفة جديد العروض الحصرية.
     * TwitterHandle : null
     * CompanyHeaderPhotoCoord : 2 39 529 333.00359712230215
     * CompanyHeaderPhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_9be03116-549f-4da8-a08b-67cc8ff0643ddaaecd7acb7d3acb73d412bb4281f1a0_w570_h0.jpg
     * CompanyProfilePhotoCoord : 529.7231833910034 335.70934256055363 174.39446366782008 174.39446366782008
     * CompanyProfilePhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_9165d40f-fa0e-4714-972b-f13f2dde22abDanubLogo.png
     * Offers : []
     * CompanyTags : null
     * NumberOfSubscribers : 164
     * IsFollowed : true
     * NumberOfOffers : 0
     * ObjectState : 0
     */

    private int CompanyId;
    private boolean Featured;
    private String CompanyName;
    private String WebSite;
    private String PhoneNumber;
    private String CompanyName_Ar;
    private String CompanyDescription;
    private Object TwitterHandle;
    private String CompanyHeaderPhotoCoord;
    private String CompanyHeaderPhoto;
    private String CompanyProfilePhotoCoord;
    private String CompanyProfilePhoto;
    private Object CompanyTags;
    private int NumberOfSubscribers;
    private boolean IsFollowed;
    private int NumberOfOffers;
    private int ObjectState;
    private List<?> Offers;

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int CompanyId) {
        this.CompanyId = CompanyId;
    }

    public boolean isFeatured() {
        return Featured;
    }

    public void setFeatured(boolean Featured) {
        this.Featured = Featured;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getWebSite() {
        return WebSite;
    }

    public void setWebSite(String WebSite) {
        this.WebSite = WebSite;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getCompanyName_Ar() {
        return CompanyName_Ar;
    }

    public void setCompanyName_Ar(String CompanyName_Ar) {
        this.CompanyName_Ar = CompanyName_Ar;
    }

    public String getCompanyDescription() {
        return CompanyDescription;
    }

    public void setCompanyDescription(String CompanyDescription) {
        this.CompanyDescription = CompanyDescription;
    }

    public Object getTwitterHandle() {
        return TwitterHandle;
    }

    public void setTwitterHandle(Object TwitterHandle) {
        this.TwitterHandle = TwitterHandle;
    }

    public String getCompanyHeaderPhotoCoord() {
        return CompanyHeaderPhotoCoord;
    }

    public void setCompanyHeaderPhotoCoord(String CompanyHeaderPhotoCoord) {
        this.CompanyHeaderPhotoCoord = CompanyHeaderPhotoCoord;
    }

    public String getCompanyHeaderPhoto() {
        return CompanyHeaderPhoto;
    }

    public void setCompanyHeaderPhoto(String CompanyHeaderPhoto) {
        this.CompanyHeaderPhoto = CompanyHeaderPhoto;
    }

    public String getCompanyProfilePhotoCoord() {
        return CompanyProfilePhotoCoord;
    }

    public void setCompanyProfilePhotoCoord(String CompanyProfilePhotoCoord) {
        this.CompanyProfilePhotoCoord = CompanyProfilePhotoCoord;
    }

    public String getCompanyProfilePhoto() {
        return CompanyProfilePhoto;
    }

    public void setCompanyProfilePhoto(String CompanyProfilePhoto) {
        this.CompanyProfilePhoto = CompanyProfilePhoto;
    }

    public Object getCompanyTags() {
        return CompanyTags;
    }

    public void setCompanyTags(Object CompanyTags) {
        this.CompanyTags = CompanyTags;
    }

    public int getNumberOfSubscribers() {
        return NumberOfSubscribers;
    }

    public void setNumberOfSubscribers(int NumberOfSubscribers) {
        this.NumberOfSubscribers = NumberOfSubscribers;
    }

    public boolean isIsFollowed() {
        return IsFollowed;
    }

    public void setIsFollowed(boolean IsFollowed) {
        this.IsFollowed = IsFollowed;
    }

    public int getNumberOfOffers() {
        return NumberOfOffers;
    }

    public void setNumberOfOffers(int NumberOfOffers) {
        this.NumberOfOffers = NumberOfOffers;
    }

    public int getObjectState() {
        return ObjectState;
    }

    public void setObjectState(int ObjectState) {
        this.ObjectState = ObjectState;
    }

    public List<?> getOffers() {
        return Offers;
    }

    public void setOffers(List<?> Offers) {
        this.Offers = Offers;
    }
}
