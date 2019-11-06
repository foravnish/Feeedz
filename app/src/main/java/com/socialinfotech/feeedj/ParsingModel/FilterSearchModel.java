package com.socialinfotech.feeedj.ParsingModel;

/**
 * Created by Piyush Poriya on 7/19/2016.
 */
public class FilterSearchModel {

    int CompanyId;
    String CompanyName;
    String CompanyName_Ar;
    String CompanyProfilePhoto;

    public FilterSearchModel(int companyId, String companyName, String companyName_Ar, String companyProfilePhoto) {
        CompanyId = companyId;
        CompanyName = companyName;
        CompanyName_Ar = companyName_Ar;
        CompanyProfilePhoto = companyProfilePhoto;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyName_Ar() {
        return CompanyName_Ar;
    }

    public void setCompanyName_Ar(String companyName_Ar) {
        CompanyName_Ar = companyName_Ar;
    }

    public String getCompanyProfilePhoto() {
        return CompanyProfilePhoto;
    }

    public void setCompanyProfilePhoto(String companyProfilePhoto) {
        CompanyProfilePhoto = companyProfilePhoto;
    }
}
