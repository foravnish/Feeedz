package com.socialinfotech.feeedj.ParsingModel;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Social Infotech on 6/13/2016.
 */
public interface FeeedjAPIClass {
    String API = "http://feeedz.co";

//    @FormUrlEncoded
//    @POST("/register")
//    void RegisterUser(@FieldMap Map<String, String> options, Callback<RegisterResponce> response);
//
//    @Headers("Cache-Control: no-cache")
//
//    @Headers({
//            "Cache-Control: no-cache"
//    })

    @GET("/api/Offers")
    Call<GetAllOffersResponse[]> getAllOffers();

    @GET("/api/Offers/OffersOfSubscribedToCompanies")
    Call<GetAllOffersResponse[]> getSubsribeOffer();

    @GET("/api/CompanyTags/{id}")
    Call<CompnayTagListingResponce> getCompanyList(@Path("id") int offset);


    @GET("/api/Companies/SimilarCompanies/{id}")
    Call<GetSimilarCompaniesResponse[]> getSimilarCompanies(@Path("id") int offset);

//    @GET("/api/offers/HashtagsList")
//    void getHorizontalList(Callback<GethorizontalResponse[]> response);

    @GET("/api/Offers/LatestFeaturedOffers")
    Call<GetSearchResponse> getFeaturedOffer();

    @GET("/api/Companies/{id}")
    Call<GetCompnayDetailResponse> getCompanyDetails(@Path("id") int offset);

    @FormUrlEncoded
    @POST("/api/offers/offerlikes/{offer_id}")
    Call<JsonObject> reportOfferLikes(@Path("offer_id") int offerId, @Field("rating") float rating);

    @POST("/api/offers/OfferViews/{offer_id}")
    Call<JsonObject> reportOfferViewed(@Path("offer_id") int offerId);


    @POST("/api/offers/OfferImages/{offer_id}")
    Call<JsonObject> reportOfferImageViewed(@Path("offer_id") int offerId);

    @POST("/api/offers/OfferCall/{offer_id}")
    Call<JsonObject> reportOfferCalled(@Path("offer_id") int offerId);

    @POST("/api/offers/OfferLocation/{offer_id}")
    Call<JsonObject> reportOfferLocation(@Path("offer_id") int offerId);

    @POST("/api/offers/OfferShare/{offer_id}")
    Call<JsonObject> reportOfferShared(@Path("offer_id") int offerId);

    @POST("/api/OfferTags")
    Call<GetSearchResponse[]> getSearchResult(@Body JsonObject params);

    @POST("api/Notifications/SubscribeCompany")
    Call<JsonObject> subscribeToCompanyNotifications(@Body JsonObject params);

    @POST("api/Notifications/UnsubscribeCompany")
    Call<JsonObject> unsubscribeFromCompanyNotifications(@Body JsonObject params);


    @POST("api/Statistics/CompanyCall")
    Call<JsonObject> reportStatisticsCompanyCall(@Body JsonObject params);

    @POST("api/Statistics/CompanyShare")
    Call<JsonObject> reportStatisticsCompanyShare(@Body JsonObject params);

    @POST("api/Statistics/CompanyLocation")
    Call<JsonObject> reportStatisticsCompanyLocation(@Body JsonObject params);

    @POST("api/Statistics/WorkingHours")
    Call<JsonObject> reportStatisticsCompanyWorkingHours(@Body JsonObject params);

    @POST("api/Statistics/Email")
    Call<JsonObject> reportStatisticsCompanyEmail(@Body JsonObject params);

    @POST("/api/Account/v3/NotificationRegister")
    Call<NotificationResponse> registerForNotification(@Body JsonObject params);

    @GET("/api/Offers/{id}")
    void getSpecificOffer(@Path("id") int offset, Callback<GetSpecificOfferResponse> response);

    //    @FormUrlEncoded
    @Headers({"Content-Type: application/json"})

    @POST("/api/account/v2/register")
    Call<RegistrationResponse> Registration(@Body JsonObject params);

    @POST("/api/account/v3/skip")
    Call<RegistrationResponse> Skip();

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded"})

    @POST("/Token")
    Call<RegistrationResponse> Login(@FieldMap Map<String, String> options);

    @POST("/api/account/v2/registerexternal")
    Call<RegistrationResponse> LoginExternal(@Body JsonObject params);

    @Headers({"Content-Type: application/json"})

    @POST("/api/account/v2/reset")
    Call<ResponseBody> ForgotPassword(@Body JsonObject params);

    @POST("/api/account/v2/changepassword")
    Call<ResponseBody> ChangePassword(@Body JsonObject params);

    @POST("/api/Companies/Subscribe")
    Call<Void> Subscribe(@Query("CompanyId") String company_id);

    @POST("/api/Companies/UnSubscribe")
    Call<Void> UnSubscribe(@Query("CompanyId") String company_id);

    @GET("/api/Account/UserInfo")
    Call<GetPersonalInforesponse> GetPersonalInfo();

    @GET("/api/offers/topsearchedwords")
    Call<GetFrequentSearchPhrasesResponse[]> GetFrequentSearchPhrase();

    @GET("/api/Companies")
    Call<FeaturedResponse[]> getFeatured();

    @GET("/api/offers/Brochures")
    Call<TimelineBrochureResponse[]> getTimelineBrochures();

//    UnSubscribe
//    @GET("/Request_Points")
//    void PaidHistory(Callback<PaidHistoryResponce> response);
//
//    @FormUrlEncoded
//    @POST("/goal")
//    void setGoal(@FieldMap Map<String, String> options, Callback<SetGoalResponse> response);
//
//    @FormUrlEncoded
//    @PUT("/goal")
//    void GoalNotAchived(@Query("id") int offset, @Field("Walk_Steps_achieve_goal") String type, Callback<GoalNotAchivedResponce> response);
//
//    @FormUrlEncoded
//    @POST("/point_history")
//    void GoalAchived(@FieldMap Map<String, String> options, Callback<GoalAchivedResponce> response);
//
//    @FormUrlEncoded
//    @POST("/mail")
//    void Sentemail(@FieldMap Map<String, String> options, Callback<EmailVerificationResponce> response);
//
//    @FormUrlEncoded
//    @POST("/password")
//    void ChangePassword(@FieldMap Map<String, String> options, Callback<ChangePasswordResponce> response);
//
//    @GET("/butto")
//    void ButtoPoints(Callback<ButtoPonintsResponce> response);
//
//
//    @FormUrlEncoded
//    @POST("/butto_play")
//    void PlayButto(@FieldMap Map<String, String> options, Callback<ButtoPlayResponce> response);
//
//
//    @GET("/butto_play")
//    void LastWeekyButto(@Query("type") String offset, Callback<LastWeekButtoResponce> response);
//
//    @GET("/bcom_post")
//    void BecomPopular(@Query("type") String offset, Callback<BeComePopularResponce> response);
//
//    @GET("/bcom_post")
//    void BecomALL(@Query("type") String offset, Callback<BecomAllResponce> response);
//
//    @GET("/bcom_post")
//    void MyBecom(@Query("type") String offset, Callback<MyBecomResponce> response);
//
//    @FormUrlEncoded
//    @PUT("/user")
//    void ChangeUserDetails(@FieldMap Map<String, String> options, Callback<ChangePasswordResponce> response);
//
//    @FormUrlEncoded
//    @POST("/login")
//    void Login(@FieldMap Map<String, String> options, Callback<LoginServiceResponce> response);
//
//    @GET("/goal")
//    void GoalHistory(Callback<WalkHistoryResponce> response);
//
//    @FormUrlEncoded
//    @POST("/like")
//    void Unlike(@Field("type") String type, @Field("Bcom_Post_id") int Bcom_Post_id, Callback<UnilkeResponce> response);
//
//    @FormUrlEncoded
//    @POST("/read")
//    void ReadPost(@Field("Bcom_Post_id") int Bcom_Post_id, Callback<UnilkeResponce> response);
//
//
//    @GET("/comment")
//    void GetCooment(@Query("Bcom_Post_id") int offset, Callback<GETPostCommentResponce> response);
//
//    @DELETE("/bcom_post")
//    void DeletePost(@Query("id") int type, Callback<UnilkeResponce> response);
//
//    @Multipart
//    @POST("/upload")
//    Observable<ImageUploadResponce> getImageName(@Part("upload") ProgressedTypedFile attachments);
//
//    @FormUrlEncoded
//    @POST("/bcom_post")
//    void MakeBcomPost(@FieldMap Map<String, String> options, Callback<MakeBcomPostResponce> response);
//
//    @FormUrlEncoded
//    @PUT("/user_nickname")
//    void RestNickname(@FieldMap Map<String, String> options, Callback<UnilkeResponce> response);
//
//    @FormUrlEncoded
//    @PUT("/user_email")
//    void ResetEmail(@FieldMap Map<String, String> options, Callback<UnilkeResponce> response);
//
//    @GET("/user")
//    void GetUser(@Query("id") int offset, Callback<UserDataResponce> response);
//
//    @GET("/usercheck")
//    void checkNickname(@Query("Users_nick_name") String offset, Callback<UnilkeResponce> response);
//
//    @GET("/usercheck")
//    void checkEmail(@Query("Users_email") String offset, Callback<UnilkeResponce> response);
//
//    @GET("/usercheck")
//    void checkPhonenumber(@Query("Users_mobile") String offset, Callback<UnilkeResponce> response);
//
//    @GET("/user_next")
//    void checkUser(@Query("Users_nick_name") String nickname, @Query("Users_email") String email, @Query("Users_mobile") String mobile, Callback<UnilkeResponce> response);
//
//    @FormUrlEncoded
//    @POST("/comment")
//    void GiveComment(@Field("Bcom_Post_id") int type, @Field("Bcom_Post_Comment_desc") String Bcom_Post_id, Callback<UnilkeResponce> response);
//
//    @FormUrlEncoded
//    @POST("/Contact_Us")
//    void ContactUS(@Field("Contact_Us_title") String type, @Field("Contact_Us_desc") String Bcom_Post_id, Callback<UnilkeResponce> response);
//    @FormUrlEncoded
//    @POST("/delete")
//    void DeleteReson(@Field("Bcom_Post_Delete_reason") String type, @Field("Bcom_Post_id") int id, Callback<UnilkeResponce> response);
//
//    @FormUrlEncoded
//    @POST("/mail_password")
//    void FindPassword(@Field("Users_email") String Users_email, Callback<UnilkeResponce> response);
//
//    @FormUrlEncoded
//    @POST("/Request_Points")
//    void RequestPoints(@Field("Request_Points_RPoints") int Request_Points_RPoints, @Field("Request_Points_ENumber") String Request_Points_ENumber, Callback<UnilkeResponce> response);
//
//    @FormUrlEncoded
//    @POST("/butto_winner_getPoints")
//    void butto_winner_getPoints(@Field("Winner_Point") String Winner_Point, @Field("Winner_Paid") String Winner_Paid, @Field("Butto_Win_Id") int Butto_Win_Id, Callback<ButtoWinnerGetPointResponce> response);
//
//    @FormUrlEncoded
//    @PUT("/user")
//    void UpdateUser(@FieldMap Map<String, String> options, Callback<UserUpdate> response);
//
//    @FormUrlEncoded
//    @POST("/Comment_Delete_Request")
//    void DeleteComment(@Field("Bcom_Post_Comment_id") int Request_Points_RPoints, Callback<UnilkeResponce> response);
//
//    @DELETE("/Comment_Delete_Request")
//    void CoomentDelte(@Query("id") int type, Callback<UnilkeResponce> response);
//
//    @GET("/faq")
//    void FAQ(Callback<FAQResponce> response);
//
//    @GET("/Contact_Us")
//    void Contact_Us(Callback<ContactUsResponce> response);
//
//    @GET("/notice")
//    void Notice(Callback<NoticeRespoce> response);
}