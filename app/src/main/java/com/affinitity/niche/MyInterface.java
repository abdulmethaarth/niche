package com.affinitity.niche;


import com.affinitity.niche.response.UpdateResponse;
import com.affinitity.niche.ui.categprylist.CategoryResponse;
import com.affinitity.niche.ui.mainhome.MainhomeModel;
import com.affinitity.niche.ui.pdf.PdfResponse;
import com.affinitity.niche.ui.videoportion.VideoModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MyInterface {

    @GET("api/fetchCategory.php")
  // Call<CategoryResponse> getCategory();

  Call<CategoryResponse> getCategory(@Query("id") String ID);


    @GET("api/Api.php?apicall=fetchUser")
Call<SpashResponse> getUserData(@Query("id") String ID);


    @FormUrlEncoded
    @POST("api/uploadRetrofit.php")
    Call<String> getUserLogin(
            @Field("name") String name,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("api/Api.php?apicall=signupnew")
    Call<SignUpUser> signupUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("mobile") String mobile
    );

    @Multipart
    @POST("api/Api.php?apicall=uploadUserImage")
    Call<MyResponse> uploadImage(@Part MultipartBody.Part file, @Part("file") @Query("id") String ID);

    @Multipart
    @POST("api/uploadRetrofit.php")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST("api/Api.php?apicall=updateUserDetails")
    Call<UpdateResponse> insertdata(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Query("id") String ID

    );

    @GET("api/Api.php?apicall=fetchUser")
    Call<settingResponse> getUserDetails(@Query("id") String ID);

//    @POST("Api.php?apicall=upload")
//    Call<MyResponse> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("desc") RequestBody desc);



    @GET("api/Api.php?apicall=pdf")
    Call<PdfResponse> getPDF();


    @GET("api/home.php")
    Call<MainhomeModel>getHome(@Query("userid") String userid);


    @GET("api/Api.php?apicall=fetchVideo")
    Call<VideoModel> getVideo(@Query("id") String ID);
}
