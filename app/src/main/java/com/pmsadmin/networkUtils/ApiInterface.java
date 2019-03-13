package com.pmsadmin.networkUtils;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static com.pmsadmin.apilist.ApiList.ATTANDENCEADD;
import static com.pmsadmin.apilist.ApiList.ATTANDENCELOGOUT;
import static com.pmsadmin.apilist.ApiList.ATTENDANCELISTING;
import static com.pmsadmin.apilist.ApiList.ATTENDANCELOCATIONUPDATE;
import static com.pmsadmin.apilist.ApiList.CHANGEPASSWORD;
import static com.pmsadmin.apilist.ApiList.FORGOT;
import static com.pmsadmin.apilist.ApiList.LOGIN;
import static com.pmsadmin.apilist.ApiList.LOGOUT;

public interface ApiInterface {

    @POST(LOGIN)
    Call<ResponseBody> call_loginApi(@Body JsonObject object);

    @POST(FORGOT)
    Call<ResponseBody> call_forgotPasswordApi(@Body JsonObject object);

    @PUT(CHANGEPASSWORD)
    Call<ResponseBody> call_changePasswordApi(@Header("Authorization") String Bearer,
                                              @Body JsonObject object);

    @GET(LOGOUT)
    Call<ResponseBody> call_logoutApi(@Header("Authorization") String Bearer);

    @POST(ATTANDENCEADD)
    Call<ResponseBody> call_attendanceAddApi(@Header("Authorization") String Bearer,
                                             @Body JsonObject object);

    @GET(ATTENDANCELISTING)
    Call<ResponseBody> call_attendanceListingApi(@Header("Authorization") String Bearer);

    @PUT(ATTANDENCELOGOUT)
    Call<ResponseBody> call_attendanceLogoutApi(@Header("Authorization") String Bearer,
                                                @Path("attendance_id") String attendance_id,
                                                @Body JsonObject object);

    @POST(ATTENDANCELOCATIONUPDATE)
    Call<ResponseBody> call_attendanceLocationUpdateApi(@Header("Authorization") String Bearer,
                                             @Body JsonObject object);
}
