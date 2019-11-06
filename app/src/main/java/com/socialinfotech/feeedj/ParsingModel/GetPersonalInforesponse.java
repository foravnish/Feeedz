package com.socialinfotech.feeedj.ParsingModel;

/**
 * Created by Social Infotech on 6/27/2016.
 */
public class GetPersonalInforesponse {

    /**
     * Username : 99d2522802254a8
     * Email : 99d2522802254a8
     * HasRegistered : true
     * LoginProvider : null
     * NumberOfSubscribtions : 4
     */

    private String Username;
    private String Email;
    private boolean HasRegistered;
    private Object LoginProvider;
    private int NumberOfSubscribtions;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public boolean isHasRegistered() {
        return HasRegistered;
    }

    public void setHasRegistered(boolean HasRegistered) {
        this.HasRegistered = HasRegistered;
    }

    public Object getLoginProvider() {
        return LoginProvider;
    }

    public void setLoginProvider(Object LoginProvider) {
        this.LoginProvider = LoginProvider;
    }

    public int getNumberOfSubscribtions() {
        return NumberOfSubscribtions;
    }

    public void setNumberOfSubscribtions(int NumberOfSubscribtions) {
        this.NumberOfSubscribtions = NumberOfSubscribtions;
    }
}
