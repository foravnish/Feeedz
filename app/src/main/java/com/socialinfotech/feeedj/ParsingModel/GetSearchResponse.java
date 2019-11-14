package com.socialinfotech.feeedj.ParsingModel;

import java.util.List;

/**
 * Created by Social Infotech on 6/20/2016.
 */
public class GetSearchResponse {

    /**
     * OfferId : 1282
     * CreationDate : 2016-06-12T11:50:50.11
     * Featured : false
     * OfferTitle : اشتري #هواوي بي ٨ ب١٢٩٩ ريال!
     * Attachment : null
     * OfferImage : http://feeedz.co/upload/459beaef-2656-4446-8b8a-a825744d265cCkv0Q5mWYAAvmVm.png
     * OfferImageCoord : null
     * OfferTimeEnd : 2016-06-27T03:00:00
     * CompanyTagId : 4
     * CompanyTag : null
     * OfferDescription : ما فيه معلومات زيادة عن العرض حالياً..
     * CompanyId : 48
     * Company : {"CompanyId":48,"Featured":false,"CompanyName":"@Axiom","WebSite":"ksa.ِِِAxiomtelecom.com/ar","PhoneNumber":"920029466","CompanyName_Ar":"اكسيوم","CompanyDescription":"صفحة اكسيوم تليكوم.. أكبرَ شركة موزِّعة، وأبرز وكيل مُعتمد، بالمنطقة لأهمِّ الهواتف النقالة ذات العلامات التجارية العالمية","TwitterHandle":null,"CompanyHeaderPhotoCoord":"190.3114186851211 10.657439446366781 682.0413247652001 429.3425605536331","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_4e14e8b1-87e3-46df-955d-14eb43f5e5e6CHiPYE8XAAAQOfV.png","CompanyProfilePhotoCoord":"25 30 245 245","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_07e64f16-ca0e-48cb-9e8d-d37ef0cdc81bqErYw4qV.png","Offers":[],"CompanyTags":null,"NumberOfSubscribers":115,"IsFollowed":false,"NumberOfOffers":1,"ObjectState":0}
     * OfferTags : [{"Offers":[{"OfferId":1343,"CreationDate":"2016-06-19T12:09:00.987","Featured":false,"OfferTitle":"استمتع بتقسيط مشترياتك بدون هامش ربح عند استخدام بطاقتك الائتمانية من البنك السعودي الهولندي #اكسيوم","Attachment":null,"OfferImage":"http://feeedz.co/upload/6c33dcd3-91a0-4819-8a8d-d5a684e26d69ClPDWchXIAAAIzu.jpg","OfferImageCoord":null,"OfferTimeEnd":"2016-07-22T22:00:00","CompanyTagId":4,"CompanyTag":null,"OfferDescription":"ما فيه معلومات زيادة عن العرض حالياً..","CompanyId":16,"Company":{"CompanyId":16,"Featured":true,"CompanyName":"@SHB","WebSite":"shb.com.sa","PhoneNumber":"920013323","CompanyName_Ar":"البنك السعودي الهولندي","CompanyDescription":"الحساب الرسمي للبنك السعودي الهولندي","TwitterHandle":null,"CompanyHeaderPhotoCoord":"0 0 419.3828571428571 264","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_c8aa11ed-c825-4a97-9de7-5860e6f10edd_00000000-0000-0000-0000-000000000000_c96caa24-89d3-49cf-b4bd-713fdb45687953f61_7776.jpg","CompanyProfilePhotoCoord":"0 0 600 600","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_d462010f-93e6-49eb-84fc-a3edb07698f9_00000000-0000-0000-0000-000000000000_3d767b08-8061-4a32-8761-0db08f5fecebbank.jpg","Offers":[],"CompanyTags":null,"NumberOfSubscribers":31,"IsFollowed":false,"NumberOfOffers":2,"ObjectState":0},"OfferTags":[],"OfferRatings":[],"OfferPannerImages":null,"OfferConditions":"<div style=\"font-family:'GEThameen-DemiBold',Glober,tahoma; font-size:23px; direction:rtl; color:#757474\">لمزيد من المعلومات عن العرض, تواصل مع الشركة عن طريق معلومات التواصل اللي موجودة تحت..<\/div>","Likes":0,"ObjectState":0}],"TagId":1205,"TagName":"axiom","ObjectState":0}]
     * OfferRatings : []
     * OfferPannerImages : null
     * OfferConditions : <div style="font-family:'GEThameen-DemiBold',Glober,tahoma; font-size:23px; direction:rtl; color:#757474">لمزيد من المعلومات عن العرض, تواصل مع الشركة عن طريق معلومات التواصل اللي موجودة تحت..</div>
     * Likes : 0
     * ObjectState : 0
     */

    private int OfferId;
    private String CreationDate;
    private boolean Featured;
    private int offerRating;
    private boolean CompanyVerified;
    private String OfferEndType;
    private int Type;
    private String OfferTitle;
    private String Attachment;
    private String AttachmentHTML;
    private String OfferImage;
    private String OfferImageCoord;
    private String OfferTimeEnd;
    private String OfferLocation;
    private String phoneNumber;
    private int CompanyTagId;
    private Object CompanyTag;
    private String OfferDescription;
    private int CompanyId;
    private boolean IsMultiple;
    private List<OffersImagesBeansCat> OfferImages;

    /**
     * CompanyId : 48
     * Featured : false
     * CompanyName : @Axiom
     * WebSite : ksa.ِِِAxiomtelecom.com/ar
     * PhoneNumber : 920029466
     * CompanyName_Ar : اكسيوم
     * CompanyDescription : صفحة اكسيوم تليكوم.. أكبرَ شركة موزِّعة، وأبرز وكيل مُعتمد، بالمنطقة لأهمِّ الهواتف النقالة ذات العلامات التجارية العالمية
     * TwitterHandle : null
     * CompanyHeaderPhotoCoord : 190.3114186851211 10.657439446366781 682.0413247652001 429.3425605536331
     * CompanyHeaderPhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_4e14e8b1-87e3-46df-955d-14eb43f5e5e6CHiPYE8XAAAQOfV.png
     * CompanyProfilePhotoCoord : 25 30 245 245
     * CompanyProfilePhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_07e64f16-ca0e-48cb-9e8d-d37ef0cdc81bqErYw4qV.png
     * Offers : []
     * CompanyTags : null
     * NumberOfSubscribers : 115
     * IsFollowed : false
     * NumberOfOffers : 1
     * ObjectState : 0
     */

    private CompanyBean Company;
    private Object OfferPannerImages;
    private String OfferConditions;
    private int Likes;
    private int ObjectState;
    /**
     * Offers : [{"OfferId":1343,"CreationDate":"2016-06-19T12:09:00.987","Featured":false,"OfferTitle":"استمتع بتقسيط مشترياتك بدون هامش ربح عند استخدام بطاقتك الائتمانية من البنك السعودي الهولندي #اكسيوم","Attachment":null,"OfferImage":"http://feeedz.co/upload/6c33dcd3-91a0-4819-8a8d-d5a684e26d69ClPDWchXIAAAIzu.jpg","OfferImageCoord":null,"OfferTimeEnd":"2016-07-22T22:00:00","CompanyTagId":4,"CompanyTag":null,"OfferDescription":"ما فيه معلومات زيادة عن العرض حالياً..","CompanyId":16,"Company":{"CompanyId":16,"Featured":true,"CompanyName":"@SHB","WebSite":"shb.com.sa","PhoneNumber":"920013323","CompanyName_Ar":"البنك السعودي الهولندي","CompanyDescription":"الحساب الرسمي للبنك السعودي الهولندي","TwitterHandle":null,"CompanyHeaderPhotoCoord":"0 0 419.3828571428571 264","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_c8aa11ed-c825-4a97-9de7-5860e6f10edd_00000000-0000-0000-0000-000000000000_c96caa24-89d3-49cf-b4bd-713fdb45687953f61_7776.jpg","CompanyProfilePhotoCoord":"0 0 600 600","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_d462010f-93e6-49eb-84fc-a3edb07698f9_00000000-0000-0000-0000-000000000000_3d767b08-8061-4a32-8761-0db08f5fecebbank.jpg","Offers":[],"CompanyTags":null,"NumberOfSubscribers":31,"IsFollowed":false,"NumberOfOffers":2,"ObjectState":0},"OfferTags":[],"OfferRatings":[],"OfferPannerImages":null,"OfferConditions":"<div style=\"font-family:'GEThameen-DemiBold',Glober,tahoma; font-size:23px; direction:rtl; color:#757474\">لمزيد من المعلومات عن العرض, تواصل مع الشركة عن طريق معلومات التواصل اللي موجودة تحت..<\/div>","Likes":0,"ObjectState":0}]
     * TagId : 1205
     * TagName : axiom
     * ObjectState : 0
     */

    private List<OfferTagsBean> OfferTags;
    private List<?> OfferRatings;

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

    public int getFeatureType() {
        return Type;
    }

    public void setFeatureType(int Type) {
        this.Type = Type;
    }

    public int getOfferRating() {
        return offerRating;
    }

    public void setOfferRating(int offerRating) {
        this.offerRating = offerRating;
    }

    public String getOfferTitle() {
        return OfferTitle;
    }

    public void setOfferTitle(String OfferTitle) {
        this.OfferTitle = OfferTitle;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String Attachment) {
        this.Attachment = Attachment;
    }

    public String getAttachmentHTML() {
        return AttachmentHTML;
    }

    public void setAttachmentHTML(String AttachmentHTML) {
        this.AttachmentHTML = AttachmentHTML;
    }

    public String getOfferImage() {
        return OfferImage;
    }

    public void setOfferImage(String OfferImage) {
        this.OfferImage = OfferImage;
    }

    public String getOfferImageCoord() {
        return OfferImageCoord;
    }

    public void setOfferImageCoord(String OfferImageCoord) {
        this.OfferImageCoord = OfferImageCoord;
    }

    public Boolean isCompanyVerified() {
        return CompanyVerified;
    }

    public void setCompanyVerified(boolean CompanyVerified) {
        this.CompanyVerified = CompanyVerified;
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

    public String getOfferEndType() {
        return OfferEndType;
    }

    public void setOfferEndType(String OfferEndType) {
        this.OfferEndType = OfferEndType;
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

    public Object getOfferPannerImages() {
        return OfferPannerImages;
    }

    public void setOfferPannerImages(Object OfferPannerImages) {
        this.OfferPannerImages = OfferPannerImages;
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

    public List<OfferTagsBean> getOfferTags() {
        return OfferTags;
    }

    public void setOfferTags(List<OfferTagsBean> OfferTags) {
        this.OfferTags = OfferTags;
    }

    public List<?> getOfferRatings() {
        return OfferRatings;
    }

    public void setOfferRatings(List<?> OfferRatings) {
        this.OfferRatings = OfferRatings;
    }


    public void setMultiple(Boolean multiple) {
        IsMultiple = multiple;
    }

    public Boolean getMultiple() {
        return IsMultiple;
    }


     public class OffersImagesBeansCat {
        private String image;
        private String coord;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCoord() {
            return coord;
        }

        public void setCoord(String coord) {
            this.coord = coord;
        }
    }

    public List<OffersImagesBeansCat> getOfferImages() {
        return OfferImages;
    }

    public void setOfferImages(List<OffersImagesBeansCat> offerImages) {
        OfferImages = offerImages;
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

    public static class OfferTagsBean {
        private int TagId;
        private String TagName;
        private int ObjectState;
        /**
         * OfferId : 1343
         * CreationDate : 2016-06-19T12:09:00.987
         * Featured : false
         * OfferTitle : استمتع بتقسيط مشترياتك بدون هامش ربح عند استخدام بطاقتك الائتمانية من البنك السعودي الهولندي #اكسيوم
         * Attachment : null
         * OfferImage : http://feeedz.co/upload/6c33dcd3-91a0-4819-8a8d-d5a684e26d69ClPDWchXIAAAIzu.jpg
         * OfferImageCoord : null
         * OfferTimeEnd : 2016-07-22T22:00:00
         * CompanyTagId : 4
         * CompanyTag : null
         * OfferDescription : ما فيه معلومات زيادة عن العرض حالياً..
         * CompanyId : 16
         * Company : {"CompanyId":16,"Featured":true,"CompanyName":"@SHB","WebSite":"shb.com.sa","PhoneNumber":"920013323","CompanyName_Ar":"البنك السعودي الهولندي","CompanyDescription":"الحساب الرسمي للبنك السعودي الهولندي","TwitterHandle":null,"CompanyHeaderPhotoCoord":"0 0 419.3828571428571 264","CompanyHeaderPhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_c8aa11ed-c825-4a97-9de7-5860e6f10edd_00000000-0000-0000-0000-000000000000_c96caa24-89d3-49cf-b4bd-713fdb45687953f61_7776.jpg","CompanyProfilePhotoCoord":"0 0 600 600","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_d462010f-93e6-49eb-84fc-a3edb07698f9_00000000-0000-0000-0000-000000000000_3d767b08-8061-4a32-8761-0db08f5fecebbank.jpg","Offers":[],"CompanyTags":null,"NumberOfSubscribers":31,"IsFollowed":false,"NumberOfOffers":2,"ObjectState":0}
         * OfferTags : []
         * OfferRatings : []
         * OfferPannerImages : null
         * OfferConditions : <div style="font-family:'GEThameen-DemiBold',Glober,tahoma; font-size:23px; direction:rtl; color:#757474">لمزيد من المعلومات عن العرض, تواصل مع الشركة عن طريق معلومات التواصل اللي موجودة تحت..</div>
         * Likes : 0
         * ObjectState : 0
         */

        private List<OffersBean> Offers;

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

        public List<OffersBean> getOffers() {
            return Offers;
        }

        public void setOffers(List<OffersBean> Offers) {
            this.Offers = Offers;
        }

        public static class OffersBean {
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
             * CompanyId : 16
             * Featured : true
             * CompanyName : @SHB
             * WebSite : shb.com.sa
             * PhoneNumber : 920013323
             * CompanyName_Ar : البنك السعودي الهولندي
             * CompanyDescription : الحساب الرسمي للبنك السعودي الهولندي
             * TwitterHandle : null
             * CompanyHeaderPhotoCoord : 0 0 419.3828571428571 264
             * CompanyHeaderPhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_c8aa11ed-c825-4a97-9de7-5860e6f10edd_00000000-0000-0000-0000-000000000000_c96caa24-89d3-49cf-b4bd-713fdb45687953f61_7776.jpg
             * CompanyProfilePhotoCoord : 0 0 600 600
             * CompanyProfilePhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_d462010f-93e6-49eb-84fc-a3edb07698f9_00000000-0000-0000-0000-000000000000_3d767b08-8061-4a32-8761-0db08f5fecebbank.jpg
             * Offers : []
             * CompanyTags : null
             * NumberOfSubscribers : 31
             * IsFollowed : false
             * NumberOfOffers : 2
             * ObjectState : 0
             */

            private CompanyBean Company;
            private Object OfferPannerImages;
            private String OfferConditions;
            private int Likes;
            private int ObjectState;
            private List<?> OfferTags;
            private List<?> OfferRatings;

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

            public Object getOfferPannerImages() {
                return OfferPannerImages;
            }

            public void setOfferPannerImages(Object OfferPannerImages) {
                this.OfferPannerImages = OfferPannerImages;
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

            public class OffersImagesBeansFeeds {
                private String image;
                private String coord;

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getCoord() {
                    return coord;
                }

                public void setCoord(String coord) {
                    this.coord = coord;
                }
            }


        }


    }




}
