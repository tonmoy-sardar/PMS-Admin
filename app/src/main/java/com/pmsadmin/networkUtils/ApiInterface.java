package com.pmsadmin.networkUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.pmsadmin.apilist.ApiList.LOGIN;

public interface ApiInterface {
    @FormUrlEncoded
    @POST(LOGIN)
    Call<ResponseBody> call_loginApi(@Field("username") String username,
                                     @Field("password") String password);
}
