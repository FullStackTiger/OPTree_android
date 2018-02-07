package com.tecocraft.optree.rest;

import com.tecocraft.optree.model.Ads.AdsModel;
import com.tecocraft.optree.model.ConfirmModel;
import com.tecocraft.optree.model.SignupModel;
import com.tecocraft.optree.model.category.CategoryModel;
import com.tecocraft.optree.model.details.CodeDetailsModel;
import com.tecocraft.optree.model.favourite.GetFavourite;
import com.tecocraft.optree.model.favourite.addFavourite;
import com.tecocraft.optree.model.login.LoginModel;
import com.tecocraft.optree.model.name.SearchNameModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("api/codes")
    Call<SearchNameModel> getCodes();

    @FormUrlEncoded
    @POST("api/signup")
    Call<SignupModel> signUp(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/code")
    Call<CodeDetailsModel> codeDetails(@Field("userid") String userid, @Field("codeName") String codeName);

    @FormUrlEncoded
    @POST("api/add_favitem")
    Call<addFavourite> addFavourite(@Field("userid") String userid, @Field("cname") String codeName, @Field("description") String description);


    @FormUrlEncoded
    @POST("api/del_favitem")
    Call<addFavourite> removeFavourite(@Field("userid") String userid, @Field("cname") String codeName);

    @FormUrlEncoded
    @POST("api/get_favlist")
    Call<GetFavourite> getFavourite(@Field("userid") String userid);

    @GET("api/get_images")
    Call<AdsModel> getADS();

    @GET("api/treedata")
    Call<CategoryModel> getCategory();

    @FormUrlEncoded
    @POST("api/forgot_password")
    Call<addFavourite> forgetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("api/confirm")
    Call<ConfirmModel> confirmCode(@Field("email") String email, @Field("password") String password, @Field("confirm_code") String confirm_code);

}