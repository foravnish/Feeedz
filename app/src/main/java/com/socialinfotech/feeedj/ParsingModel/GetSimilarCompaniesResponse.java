package com.socialinfotech.feeedj.ParsingModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSimilarCompaniesResponse {

    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("CompanyName_Ar")
    @Expose
    private String companyNameAr;
    @SerializedName("IdentifyingTagId")
    @Expose
    private Integer identifyingTagId;
    @SerializedName("CompanyProfilePhotoCoord")
    @Expose
    private String companyProfilePhotoCoord;
    @SerializedName("CompanyProfilePhoto")
    @Expose
    private String companyProfilePhoto;
    @SerializedName("IsFollowed")
    @Expose
    private Boolean isFollowed;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNameAr() {
        return companyNameAr;
    }

    public void setCompanyNameAr(String companyNameAr) {
        this.companyNameAr = companyNameAr;
    }

    public Integer getIdentifyingTagId() {
        return identifyingTagId;
    }

    public void setIdentifyingTagId(Integer identifyingTagId) {
        this.identifyingTagId = identifyingTagId;
    }

    public String getCompanyProfilePhotoCoord() {
        return companyProfilePhotoCoord;
    }

    public void setCompanyProfilePhotoCoord(String companyProfilePhotoCoord) {
        this.companyProfilePhotoCoord = companyProfilePhotoCoord;
    }

    public String getCompanyProfilePhoto() {
        return companyProfilePhoto;
    }

    public void setCompanyProfilePhoto(String companyProfilePhoto) {
        this.companyProfilePhoto = companyProfilePhoto;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }


}
