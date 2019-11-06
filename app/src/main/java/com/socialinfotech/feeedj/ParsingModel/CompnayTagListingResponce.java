package com.socialinfotech.feeedj.ParsingModel;

import java.util.List;

/**
 * Created by Social Infotech on 6/13/2016.
 */
public class CompnayTagListingResponce {

    /**
     * CompanyTagImage : http://feeedz.co/upload/tele@2x.png
     * Companies : [{"CompanyId":5,"Featured":true,"CompanyName":"@Mobily","WebSite":"www.mobily.com.sa","PhoneNumber":"0560101100","CompanyName_Ar":"موبايلي","CompanyDescription":"موبايلي شركة الاتصالات الرائدة والأسرع نموا في المملكة العربية","TwitterHandle":null,"CompanyHeaderPhotoCoord":"3.804498269896194 21.558823529411764 693.0129115175482 436.24913494809687","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_cd53871b-56d5-4d19-b38c-b8cb554aacb9_00000000-0000-0000-0000-000000000000_271bb263-d7a3-44ff-a036-5e51ac5fc21b3651452.jpg","CompanyProfilePhotoCoord":"71.62629757785467 60.2076124567474 482.6989619377162 482.6989619377162","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_d4f8b2cd-53e9-4e8f-a050-cd8e7271262b_00000000-0000-0000-0000-000000000000_86d5db11-72c1-4bc0-86f9-8353a66a2bfbmobily.jpg","Offers":null,"CompanyTags":[],"NumberOfSubscribers":149,"IsFollowed":false,"NumberOfOffers":1,"ObjectState":0},{"CompanyId":33,"Featured":false,"CompanyName":"@STC","WebSite":"www.stc.com.sa","PhoneNumber":"902","CompanyName_Ar":"الإتصالات السعودية","CompanyDescription":"تابع حساب Stc على فيييدز لمعرفة المزيد عن منتجاتنا وعروضنا..","TwitterHandle":null,"CompanyHeaderPhotoCoord":"0 30.1038062283737 395.7686604053386 249.1349480968858","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_a10d4a45-5960-49f8-8a17-396b5aff4fdcCKI8zioWEAAA3Mp.jpg","CompanyProfilePhotoCoord":"368.1660899653979 171.62629757785464 871.9723183391003 871.9723183391003","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_67258437-d87e-4652-9c19-8fa3584c3bcfSTC-logo.png","Offers":null,"CompanyTags":[],"NumberOfSubscribers":110,"IsFollowed":false,"NumberOfOffers":1,"ObjectState":0},{"CompanyId":35,"Featured":false,"CompanyName":"@VirginMobile","WebSite":"www.virginmobile.sa","PhoneNumber":"+966 570 001 789","CompanyName_Ar":" ڤيرجن موبايل","CompanyDescription":"أهلاً بكم في حساب شركة ڤيرجن موبايل السعودية على فيييدز..","TwitterHandle":null,"CompanyHeaderPhotoCoord":"0 321.7993079584775 998.2698961937717 628.407308755072","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_f6ad3188-124e-4ede-9fbd-2cd0d6c8c4b8CJOnVTpVAAA-X20.jpg","CompanyProfilePhotoCoord":"205.53633217993078 134.53287197231833 313.9100346020761 313.9100346020761","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_e0520a6f-f017-48b8-8715-2f436be7374dYS906Dtl.png","Offers":null,"CompanyTags":[],"NumberOfSubscribers":53,"IsFollowed":false,"NumberOfOffers":3,"ObjectState":0},{"CompanyId":36,"Featured":false,"CompanyName":"@Lebara","WebSite":"www.lebara.sa","PhoneNumber":"+966 57 600 1755","CompanyName_Ar":"ليبارا","CompanyDescription":"مرحباً بكم في ليبارا، شركة الاتصالات الجديدة في السعودية.","TwitterHandle":null,"CompanyHeaderPhotoCoord":"0 363.1833910034602 1024 644.6043165467626","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_22509aea-7da3-4325-bb0c-14b6a0b92ccdCJu5w1TUsAAHF5C.jpg","CompanyProfilePhotoCoord":"326.439446366782 153.07612456747404 409.4325259515571 409.4325259515571","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_75fa1a74-78d8-4c92-afa6-6f32c3a70133LebaraLogo.png","Offers":null,"CompanyTags":[],"NumberOfSubscribers":47,"IsFollowed":false,"NumberOfOffers":0,"ObjectState":0},{"CompanyId":45,"Featured":false,"CompanyName":"@Zain","WebSite":"sa.Zain.com","PhoneNumber":"+966590000959","CompanyName_Ar":"زين","CompanyDescription":"الصفحة الرئيسية لزين المملكة العربية السعودية على تطبيق فيييدز. ","TwitterHandle":null,"CompanyHeaderPhotoCoord":"36.53979238754326 34.325259515570934 592.773504695996 373.14878892733566","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_adc46757-6999-4dfa-a866-7467b134d1e5zain-branch.jpg","CompanyProfilePhotoCoord":"393.65916955017303 215.81314878892735 345.70069204152253 345.70069204152253","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_46360184-a572-4c3f-8774-d77f1218c139ZainLogo.png","Offers":null,"CompanyTags":[],"NumberOfSubscribers":75,"IsFollowed":false,"NumberOfOffers":2,"ObjectState":0}]
     * TagId : 1
     * TagName : Tele
     * ObjectState : 0
     */

    private String CompanyTagImage;
    private int TagId;
    private String TagName;
    private int ObjectState;
    /**
     * CompanyId : 5
     * Featured : true
     * CompanyName : @Mobily
     * WebSite : www.mobily.com.sa
     * PhoneNumber : 0560101100
     * CompanyName_Ar : موبايلي
     * CompanyDescription : موبايلي شركة الاتصالات الرائدة والأسرع نموا في المملكة العربية
     * TwitterHandle : null
     * CompanyHeaderPhotoCoord : 3.804498269896194 21.558823529411764 693.0129115175482 436.24913494809687
     * CompanyHeaderPhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_cd53871b-56d5-4d19-b38c-b8cb554aacb9_00000000-0000-0000-0000-000000000000_271bb263-d7a3-44ff-a036-5e51ac5fc21b3651452.jpg
     * CompanyProfilePhotoCoord : 71.62629757785467 60.2076124567474 482.6989619377162 482.6989619377162
     * CompanyProfilePhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_d4f8b2cd-53e9-4e8f-a050-cd8e7271262b_00000000-0000-0000-0000-000000000000_86d5db11-72c1-4bc0-86f9-8353a66a2bfbmobily.jpg
     * Offers : null
     * CompanyTags : []
     * NumberOfSubscribers : 149
     * IsFollowed : false
     * NumberOfOffers : 1
     * ObjectState : 0
     */

    private List<CompaniesBean> Companies;

    public String getCompanyTagImage() {
        return CompanyTagImage;
    }

    public void setCompanyTagImage(String CompanyTagImage) {
        this.CompanyTagImage = CompanyTagImage;
    }

    public int getTagId() {
        return TagId;
    }

    public void setTagId(int TagId) {
        this.TagId = TagId;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public int getObjectState() {
        return ObjectState;
    }

    public void setObjectState(int ObjectState) {
        this.ObjectState = ObjectState;
    }

    public List<CompaniesBean> getCompanies() {
        return Companies;
    }

    public void setCompanies(List<CompaniesBean> Companies) {
        this.Companies = Companies;
    }

    public static class CompaniesBean {
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
        private Object Offers;
        private int NumberOfSubscribers;
        private boolean IsFollowed;
        private int NumberOfOffers;
        private int ObjectState;
        private List<?> CompanyTags;

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

        public Object getOffers() {
            return Offers;
        }

        public void setOffers(Object Offers) {
            this.Offers = Offers;
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

        public List<?> getCompanyTags() {
            return CompanyTags;
        }

        public void setCompanyTags(List<?> CompanyTags) {
            this.CompanyTags = CompanyTags;
        }
    }
}
