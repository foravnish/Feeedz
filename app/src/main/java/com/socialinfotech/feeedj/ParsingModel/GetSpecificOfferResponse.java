package com.socialinfotech.feeedj.ParsingModel;

import java.util.List;

/**
 * Created by Social Infotech on 6/20/2016.
 */
public class GetSpecificOfferResponse {


    /**
     * OfferId : 1325
     * CreationDate : 2016-06-16T11:30:43.257
     * Featured : false
     * OfferTitle : عروض شهر #رمضان من #توكيلات_الجزيرة تشمل موديلات لينكون 2016 مع صيانة مجانية 5 سنوات
     * Attachment : null
     * OfferImage : http://feeedz.co/upload/9787e762-6b6e-42da-be84-57d4bd891a2aClAk_bDWYAArIqY.jpg
     * OfferImageCoord : null
     * OfferTimeEnd : 2016-07-06T00:00:00
     * CompanyTagId : 6
     * CompanyTag : null
     * OfferDescription : ما فيه معلومات زيادة عن العرض حالياً..
     * CompanyId : 41
     * Company : {"CompanyId":41,"Featured":false,"CompanyName":"@AlJazirahFord","WebSite":"aljazirahvehicles.com","PhoneNumber":"920025678","CompanyName_Ar":"توكيلات الجزيرة","CompanyDescription":"تابعونا لمعرفة أفضل العروض على سيارات فورد و لينكون!","TwitterHandle":null,"CompanyHeaderPhotoCoord":"30.519031141868513 4.359861591695502 562.7335640138408 354.2387543252595","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_0f3c197d-5652-42eb-9856-cdf5b25d2aa4Ford-Lincoln-logos.jpg","CompanyProfilePhotoCoord":"305.7352941176471 149.87024221453288 577.5 577.5","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_ed38561a-e6d8-4712-9d61-46e1260cc0b9AljazirahLogo.png","Offers":[],"CompanyTags":null,"NumberOfSubscribers":61,"IsFollowed":false,"NumberOfOffers":4,"ObjectState":0}
     * OfferTags : []
     * OfferRatings : []
     * OfferPannerImages : [{"OfferPannerImageId":1368,"OfferPannerImagePathCoord":null,"OfferPannerImagePath":"http://feeedz.co/upload/9787e762-6b6e-42da-be84-57d4bd891a2aClAk_bDWYAArIqY.jpg","OfferId":1325,"ObjectState":0}]
     * OfferConditions : <div style="font-family:'GEThameen-DemiBold',Glober,tahoma; font-size:23px; direction:rtl; color:#757474">لمزيد من المعلومات عن العرض, تواصل مع الشركة عن طريق معلومات التواصل اللي موجودة تحت..</div>
     * Likes : 0
     * ObjectState : 0
     */

    private int OfferId;
    private String CreationDate;
    private boolean Featured;
    private String OfferTitle;
    private Object Attachment;
    private Object AttachmentHTML;
    private String OfferImage;
    private Object OfferImageCoord;
    private String OfferTimeEnd;
    private int CompanyTagId;
    private Object CompanyTag;
    private String OfferDescription;
    private int CompanyId;
    /**
     * CompanyId : 41
     * Featured : false
     * CompanyName : @AlJazirahFord
     * WebSite : aljazirahvehicles.com
     * PhoneNumber : 920025678
     * CompanyName_Ar : توكيلات الجزيرة
     * CompanyDescription : تابعونا لمعرفة أفضل العروض على سيارات فورد و لينكون!
     * TwitterHandle : null
     * CompanyHeaderPhotoCoord : 30.519031141868513 4.359861591695502 562.7335640138408 354.2387543252595
     * CompanyHeaderPhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_0f3c197d-5652-42eb-9856-cdf5b25d2aa4Ford-Lincoln-logos.jpg
     * CompanyProfilePhotoCoord : 305.7352941176471 149.87024221453288 577.5 577.5
     * CompanyProfilePhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_ed38561a-e6d8-4712-9d61-46e1260cc0b9AljazirahLogo.png
     * Offers : []
     * CompanyTags : null
     * NumberOfSubscribers : 61
     * IsFollowed : false
     * NumberOfOffers : 4
     * ObjectState : 0
     */

    private CompanyBean Company;
    private String OfferConditions;
    private int Likes;
    private int ObjectState;
    private List<?> OfferTags;
    private List<?> OfferRatings;
    /**
     * OfferPannerImageId : 1368
     * OfferPannerImagePathCoord : null
     * OfferPannerImagePath : http://feeedz.co/upload/9787e762-6b6e-42da-be84-57d4bd891a2aClAk_bDWYAArIqY.jpg
     * OfferId : 1325
     * ObjectState : 0
     */

    private List<OfferPannerImagesBean> OfferPannerImages;

    public int getOfferId() {
        return OfferId;
    }

    public void setOfferId(int OfferId) {
        this.OfferId = OfferId;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String CreationDate) {
        this.CreationDate = CreationDate;
    }

    public boolean isFeatured() {
        return Featured;
    }

    public void setFeatured(boolean Featured) {
        this.Featured = Featured;
    }

    public String getOfferTitle() {
        return OfferTitle;
    }

    public void setOfferTitle(String OfferTitle) {
        this.OfferTitle = OfferTitle;
    }

    public Object getAttachment() {
        return Attachment;
    }

    public void setAttachment(Object Attachment) {
        this.Attachment = Attachment;
    }

    public Object getAttachmentHTML() {
        return AttachmentHTML;
    }

    public void setAttachmentHTML(Object AttachmentHTML) {
        this.AttachmentHTML = AttachmentHTML;
    }

    public String getOfferImage() {
        return OfferImage;
    }

    public void setOfferImage(String OfferImage) {
        this.OfferImage = OfferImage;
    }

    public Object getOfferImageCoord() {
        return OfferImageCoord;
    }

    public void setOfferImageCoord(Object OfferImageCoord) {
        this.OfferImageCoord = OfferImageCoord;
    }

    public String getOfferTimeEnd() {
        return OfferTimeEnd;
    }

    public void setOfferTimeEnd(String OfferTimeEnd) {
        this.OfferTimeEnd = OfferTimeEnd;
    }

    public int getCompanyTagId() {
        return CompanyTagId;
    }

    public void setCompanyTagId(int CompanyTagId) {
        this.CompanyTagId = CompanyTagId;
    }

    public Object getCompanyTag() {
        return CompanyTag;
    }

    public void setCompanyTag(Object CompanyTag) {
        this.CompanyTag = CompanyTag;
    }

    public String getOfferDescription() {
        return OfferDescription;
    }

    public void setOfferDescription(String OfferDescription) {
        this.OfferDescription = OfferDescription;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int CompanyId) {
        this.CompanyId = CompanyId;
    }

    public CompanyBean getCompany() {
        return Company;
    }

    public void setCompany(CompanyBean Company) {
        this.Company = Company;
    }

    public String getOfferConditions() {
        return OfferConditions;
    }

    public void setOfferConditions(String OfferConditions) {
        this.OfferConditions = OfferConditions;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int Likes) {
        this.Likes = Likes;
    }

    public int getObjectState() {
        return ObjectState;
    }

    public void setObjectState(int ObjectState) {
        this.ObjectState = ObjectState;
    }

    public List<?> getOfferTags() {
        return OfferTags;
    }

    public void setOfferTags(List<?> OfferTags) {
        this.OfferTags = OfferTags;
    }

    public List<?> getOfferRatings() {
        return OfferRatings;
    }

    public void setOfferRatings(List<?> OfferRatings) {
        this.OfferRatings = OfferRatings;
    }

    public List<OfferPannerImagesBean> getOfferPannerImages() {
        return OfferPannerImages;
    }

    public void setOfferPannerImages(List<OfferPannerImagesBean> OfferPannerImages) {
        this.OfferPannerImages = OfferPannerImages;
    }

    public static class CompanyBean {
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

    public static class OfferPannerImagesBean {
        private int OfferPannerImageId;
        private Object OfferPannerImagePathCoord;
        private String OfferPannerImagePath;
        private int OfferId;
        private int ObjectState;

        public int getOfferPannerImageId() {
            return OfferPannerImageId;
        }

        public void setOfferPannerImageId(int OfferPannerImageId) {
            this.OfferPannerImageId = OfferPannerImageId;
        }

        public Object getOfferPannerImagePathCoord() {
            return OfferPannerImagePathCoord;
        }

        public void setOfferPannerImagePathCoord(Object OfferPannerImagePathCoord) {
            this.OfferPannerImagePathCoord = OfferPannerImagePathCoord;
        }

        public String getOfferPannerImagePath() {
            return OfferPannerImagePath;
        }

        public void setOfferPannerImagePath(String OfferPannerImagePath) {
            this.OfferPannerImagePath = OfferPannerImagePath;
        }

        public int getOfferId() {
            return OfferId;
        }

        public void setOfferId(int OfferId) {
            this.OfferId = OfferId;
        }

        public int getObjectState() {
            return ObjectState;
        }

        public void setObjectState(int ObjectState) {
            this.ObjectState = ObjectState;
        }
    }
}
