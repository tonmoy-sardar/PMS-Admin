package com.pmsadmin.networkUtils;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import static com.pmsadmin.apilist.ApiList.CHANGEPASSWORD;
import static com.pmsadmin.apilist.ApiList.FORGOT;
import static com.pmsadmin.apilist.ApiList.LOGIN;

public interface ApiInterface {

    @POST(LOGIN)
    Call<ResponseBody> call_loginApi(@Body JsonObject object);

    @POST(FORGOT)
    Call<ResponseBody> call_forgotPasswordApi(@Body JsonObject object);

    @PUT(CHANGEPASSWORD)
    Call<ResponseBody> call_changePasswordApi(@Header("Authorization") String Bearer,
                                              @Body JsonObject object);
}
