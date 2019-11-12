package com.socialinfotech.feeedj.ParsingModel;

import java.util.List;

/**
 * Created by Social Infotech on 6/13/2016.
 */
public class GetAllOffersResponse {

    /**
     * OfferId : 1299
     * Attachment : null
     * CompanyId : 35
     * CompanyTagId : 4
     * CreationDate : 2016-06-12T12:39:28.333
     * Featured : false
     * OfferConditions : <div style="font-family:'GEThameen-DemiBold',Glober,tahoma; font-size:23px; direction:rtl; color:#757474">لمزيد من المعلومات عن العرض, تواصل مع الشركة عن طريق معلومات التواصل اللي موجودة تحت..</div>
     * OfferDescription : ما فيه معلومات زيادة عن العرض حالياً..
     * OfferImage : http://feeedz.co/upload/db18674b-f7b6-417a-9134-d6adfb9093c4CknlYwqXIAAXN2O.jpg
     * OfferImageCoord : null
     * OfferTimeEnd : 2016-06-27T09:00:00
     * OfferTitle : شرّفونا في الويكند وافتتحوا معانا أوفر العروض الرمضانية..
     * Company : {"CompanyId":35,"CompanyName":"@VirginMobile","CompanyName_Ar":" ڤيرجن موبايل","CompanyProfilePhoto":"http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_e0520a6f-f017-48b8-8715-2f436be7374dYS906Dtl.png"}
     */

    private int OfferId;
    private String Attachment;
    private String AttachmentHTML;
    private int CompanyId;
    private int CompanyTagId;
    private String CreationDate;
    private boolean Featured;
    private int offerRating;
    private int Type;
    private String OfferEndType;
    private String OfferConditions;
    private String OfferDescription;
    private String OfferImage;
    private String OfferImageCoord;
    private String OfferTimeEnd;
    private String OfferTitle;
    private String OfferLocation;
    private String phoneNumber;
    private String BrochureBadge;
    private boolean IsMultiple;
    private List<OffersImagesBeans> OfferImages;
    /**
     * CompanyId : 35
     * CompanyName : @VirginMobile
     * CompanyName_Ar :  ڤيرجن موبايل
     * CompanyProfilePhoto : http://feeedz.co/upload/_00000000-0000-0000-0000-000000000000_e0520a6f-f017-48b8-8715-2f436be7374dYS906Dtl.png
     */

    private CompanyBean Company;

    public int getOfferId() {
        return OfferId;
    }

    public void setOfferId(int OfferId) {
        this.OfferId = OfferId;
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

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int CompanyId) {
        this.CompanyId = CompanyId;
    }

    public int getCompanyTagId() {
        return CompanyTagId;
    }

    public void setCompanyTagId(int CompanyTagId) {
        this.CompanyTagId = CompanyTagId;
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


    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
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

    public String getOfferConditions() {
        return OfferConditions;
    }

    public void setOfferConditions(String OfferConditions) {
        this.OfferConditions = OfferConditions;
    }

    public String getOfferDescription() {
        return OfferDescription;
    }

    public void setOfferDescription(String OfferDescription) {
        this.OfferDescription = OfferDescription;
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

    public void setBrochureBadge(String BrochureBadge) {
        this.BrochureBadge = BrochureBadge;
    }

    public String getBrochureBadge() {
        return BrochureBadge;
    }

    public void setMultiple(Boolean multiple) {
        IsMultiple = multiple;
    }

    public Boolean getMultiple() {
        return IsMultiple;
    }

    public CompanyBean getCompany() {
        return Company;
    }

    public void setCompany(CompanyBean Company) {
        this.Company = Company;
    }

    public static class CompanyBean {
        private int CompanyId;
        private String CompanyName;
        private String CompanyName_Ar;
        private boolean CompanyVerified;
        private String CompanyProfilePhoto;

        public int getCompanyId() {
            return CompanyId;
        }

        public void setCompanyId(int CompanyId) {
            this.CompanyId = CompanyId;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String CompanyName) {
            this.CompanyName = CompanyName;
        }

        public String getCompanyName_Ar() {
            return CompanyName_Ar;
        }

        public void setCompanyName_Ar(String CompanyName_Ar) {
            this.CompanyName_Ar = CompanyName_Ar;
        }

        public String getCompanyProfilePhoto() {
            return CompanyProfilePhoto;
        }

        public Boolean isCompanyVerified() {
            return CompanyVerified;
        }

        public void setCompanyVerified(boolean CompanyVerified) {
            this.CompanyVerified = CompanyVerified;
        }

        public void setCompanyProfilePhoto(String CompanyProfilePhoto) {
            this.CompanyProfilePhoto = CompanyProfilePhoto;
        }
    }

    public class OffersImagesBeans {
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

    public List<OffersImagesBeans> getOfferImages() {
        return OfferImages;
    }

    public void setOfferImages(List<OffersImagesBeans> offerImages) {
        OfferImages = offerImages;
    }
}
