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
import retrofit2.http.Query;

import static com.pmsadmin.apilist.ApiList.ADDSITE;
import static com.pmsadmin.apilist.ApiList.APPROVALLIST;
import static com.pmsadmin.apilist.ApiList.ATTANDENCEADD;
import static com.pmsadmin.apilist.ApiList.ATTANDENCELOGOUT;
import static com.pmsadmin.apilist.ApiList.ATTENDANCELISTING;
import static com.pmsadmin.apilist.ApiList.ATTENDANCELOCATIONUPDATE;
import static com.pmsadmin.apilist.ApiList.ATTENDENCEEDIT;
import static com.pmsadmin.apilist.ApiList.ATTENDENCE_DEVIATION_LIST;
import static com.pmsadmin.apilist.ApiList.CHANGEPASSWORD;
import static com.pmsadmin.apilist.ApiList.DEVIATION_JUSTIFICATION;
import static com.pmsadmin.apilist.ApiList.EMPLOYEELIST;
import static com.pmsadmin.apilist.ApiList.FORGOT;
import static com.pmsadmin.apilist.ApiList.GETSITETYPE;
import static com.pmsadmin.apilist.ApiList.LEAVEAPPLY;
import static com.pmsadmin.apilist.ApiList.LEAVEAPPLYLIST;
import static com.pmsadmin.apilist.ApiList.LEAVEHISTORY;
import static com.pmsadmin.apilist.ApiList.LOGIN;
import static com.pmsadmin.apilist.ApiList.LOGOUT;
import static com.pmsadmin.apilist.ApiList.MARKERGET;
import static com.pmsadmin.apilist.ApiList.REPORTLISTING;

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
    Call<ResponseBody> call_attendanceListingApi(@Header("Authorization") String Bearer,
                                                 @Path("employee_id") String employee_id);

    @GET(REPORTLISTING)
    Call<ResponseBody> call_reportListingApi(@Header("Authorization") String Bearer,
                                             @Header("Content-Type") String Content_type,
                                             @Query("page") String page);

    @GET(APPROVALLIST)
    Call<ResponseBody> call_approvalListingApi(@Header("Authorization") String Bearer,
                                               @Query("page") String page);

    @PUT(ATTANDENCELOGOUT)
    Call<ResponseBody> call_attendanceLogoutApi(@Header("Authorization") String Bearer,
                                                @Path("attendance_id") String attendance_id,
                                                @Body JsonObject object);

    @PUT(DEVIATION_JUSTIFICATION)
    Call<ResponseBody> call_deviation_justification(@Header("Authorization") String Bearer,
                                                @Path("deviation_id") String deviation_id,
                                                @Body JsonObject object);

    @PUT(ATTENDENCEEDIT)
    Call<ResponseBody> call_attendanceEditApi(@Header("Authorization") String Bearer,
                                              @Path("attendance_id") String attendance_id,
                                              @Body JsonObject object);

    @POST(ATTENDANCELOCATIONUPDATE)
    Call<ResponseBody> call_attendanceLocationUpdateApi(@Header("Authorization") String Bearer,
                                                        @Body JsonObject object);

    @GET(EMPLOYEELIST)
    Call<ResponseBody> call_employeeListApi(@Header("Authorization") String Bearer,
                                            @Path("employee_id") String employee_id);

    @POST(LEAVEAPPLY)
    Call<ResponseBody> call_leaveApplyApi(@Header("Authorization") String Bearer,
                                          @Header("Content-Type") String Content_type,
                                          @Body JsonObject object);

    @GET(LEAVEAPPLYLIST)
    Call<ResponseBody> call_leaveListApi(@Header("Authorization") String Bearer,
                                         @Header("Content-Type") String Content_type,
                                         @Query("page") String page);

    @GET(LEAVEHISTORY)
    Call<ResponseBody> call_leaveHistoryApi(@Header("Authorization") String Bearer,
                                            @Header("Content-Type") String Content_type,
                                            @Path("employee_id") String employee_id);

    @GET(GETSITETYPE)
    Call<ResponseBody> call_siteTypeApi(@Header("Authorization") String Bearer);

    @POST(ADDSITE)
    Call<ResponseBody> call_siteAddApi(@Header("Authorization") String Bearer,
                                          @Header("Content-Type") String Content_type,
                                          @Body JsonObject object);

    @GET(MARKERGET)
    Call<ResponseBody> call_markerGetApi(@Header("Authorization") String Bearer,
                                         @Query("attendance_id") String attendance_id);

    @GET(ATTENDENCE_DEVIATION_LIST)
    Call<ResponseBody> call_deviation_listApi(@Header("Authorization") String Bearer,
                                            @Header("Content-Type") String Content_type,
                                              @Query("attandance") Integer attendence_id);
}
