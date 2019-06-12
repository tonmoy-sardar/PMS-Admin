package com.pmsadmin.networkUtils;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.pmsadmin.apilist.ApiList.ADDSITE;
//import static com.pmsadmin.apilist.ApiList.ADD_CONTRACT_HYDRO;
import static com.pmsadmin.apilist.ApiList.ADD_CONTRACT_VENDOR;
import static com.pmsadmin.apilist.ApiList.ADD_HYDROLOGICAL;
import static com.pmsadmin.apilist.ApiList.ADD_HYDROLOGICAL_DOC;
import static com.pmsadmin.apilist.ApiList.ADD_SITE_PHOTOSF;
import static com.pmsadmin.apilist.ApiList.GET_ALL_UNIT;
import static com.pmsadmin.apilist.ApiList.GET_MACHINERY_TYPE_;
import static com.pmsadmin.apilist.ApiList.MACHINERY_TYPE_ADD;
import static com.pmsadmin.apilist.ApiList.MACHINERY_TYPE_ADD_DOC;
import static com.pmsadmin.apilist.ApiList.MATERIAL_ADD;
import static com.pmsadmin.apilist.ApiList.TENDER_SURVEY_RESOURCE_ESTABLISHMENT_DOCUMENT_ADD;
import static com.pmsadmin.apilist.ApiList.TENDER_SURVEY_SITE_PHOTOS_EDIT;
import static com.pmsadmin.apilist.ApiList.APPROVALLIST;
import static com.pmsadmin.apilist.ApiList.ATTANDANCE_JUSTIFICATION;
import static com.pmsadmin.apilist.ApiList.ATTANDENCEADD;
import static com.pmsadmin.apilist.ApiList.ATTANDENCELOGOUT;
import static com.pmsadmin.apilist.ApiList.ATTENDANCELISTING;
import static com.pmsadmin.apilist.ApiList.ATTENDANCELOCATIONUPDATE;
import static com.pmsadmin.apilist.ApiList.ATTENDENCEEDIT;
import static com.pmsadmin.apilist.ApiList.ATTENDENCE_DEVIATION_LIST;
import static com.pmsadmin.apilist.ApiList.CHANGEPASSWORD;
import static com.pmsadmin.apilist.ApiList.CONTRACT_VENDOR_DOC_ADD;
import static com.pmsadmin.apilist.ApiList.CRUSHER_LIST;
import static com.pmsadmin.apilist.ApiList.DEVIATION_JUSTIFICATION;
import static com.pmsadmin.apilist.ApiList.EMPLOYEELIST;
import static com.pmsadmin.apilist.ApiList.ESTABLISHMENT_ADD;
import static com.pmsadmin.apilist.ApiList.FORGOT;
import static com.pmsadmin.apilist.ApiList.GETSITETYPE;
import static com.pmsadmin.apilist.ApiList.GET_CONTRACT_VENDOR;
import static com.pmsadmin.apilist.ApiList.GET_RESOURCE_ESTABLISHMENT;
import static com.pmsadmin.apilist.ApiList.GET_RESOURCE_HYDRO;
import static com.pmsadmin.apilist.ApiList.GET_SITE_PHOTO_LIST;
import static com.pmsadmin.apilist.ApiList.LEAVEAPPLY;
import static com.pmsadmin.apilist.ApiList.LEAVEAPPLYLIST;
import static com.pmsadmin.apilist.ApiList.LEAVEHISTORY;
import static com.pmsadmin.apilist.ApiList.LOGIN;
import static com.pmsadmin.apilist.ApiList.LOGOUT;
import static com.pmsadmin.apilist.ApiList.LOGOUT_API;
import static com.pmsadmin.apilist.ApiList.MARKERGET;
import static com.pmsadmin.apilist.ApiList.PROJECT_LIST;
import static com.pmsadmin.apilist.ApiList.RAW_MATERIALS_LIST;
import static com.pmsadmin.apilist.ApiList.REPORTLISTING;
import static com.pmsadmin.apilist.ApiList.TENDERS_ADD;
import static com.pmsadmin.apilist.ApiList.TENDER_SURVEY_LOCATION_ADD;
import static com.pmsadmin.apilist.ApiList.TENDER_SURVEY_LOCATION_LIST;
import static com.pmsadmin.apilist.ApiList.machineries_wp_list;
import static com.pmsadmin.apilist.ApiList.pms_execution_daily_progress_add;
import static com.pmsadmin.apilist.ApiList.pms_execution_daily_progress_pandm_add;
import static com.pmsadmin.apilist.ApiList.pms_execution_labour_progress_add;
import static com.pmsadmin.apilist.ApiList.project_site_management_site_add;
import static com.pmsadmin.apilist.ApiList.projects_details_by_project_site_id;
import static com.pmsadmin.apilist.ApiList.unit_add;

public interface ApiInterface {

//    @POST(LOGIN)
//    Call<ResponseBody> call_loginApi(@Body JsonObject object);

    @POST(LOGIN)
    Call<ResponseBody> call_loginApi(  @Header("Content-Type") String Content_type,@Body JsonObject object);

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

    /*@GET(REPORTLISTING)
    Call<ResponseBody> call_reportListingApi(@Header("Authorization") String Bearer,
                                             @Header("Content-Type") String Content_type,
                                             @Query("page") String page);*/

    @GET(REPORTLISTING)
    Call<ResponseBody> call_reportListingApi(@Header("Authorization") String Bearer,
                                             @Header("Content-Type") String Content_type,
                                             @Query("user_project") String user_project,
                                             @Query("page") String page);

    @GET(APPROVALLIST)
    Call<ResponseBody> call_approvalListingApi(@Header("Authorization") String Bearer,
                                               @Query("page") String page);

    @PUT(ATTANDENCELOGOUT)
    Call<ResponseBody> call_attendanceLogoutApi(@Header("Authorization") String Bearer,
                                                @Path("attendance_id") String attendance_id,
                                                @Body JsonObject object);


    @PUT(LOGOUT_API)
    Call<ResponseBody> callLogoutApi(@Header("Authorization") String Bearer,
                                     @Path("attendance_id") String attendance_id,
                                     @Body JsonObject object);



    @PUT(DEVIATION_JUSTIFICATION)
    Call<ResponseBody> call_deviation_justification(@Header("Authorization") String Bearer,
                                                    @Path("deviation_id") String deviation_id,
                                                    @Body JsonObject object);
    @PUT(ATTANDANCE_JUSTIFICATION)
    Call<ResponseBody> call_attandance_justification(@Header("Authorization") String Bearer,
                                                     @Path("attandance_id") Integer attandance_id,
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

    @GET(PROJECT_LIST)
    Call<ResponseBody> call_projectList(@Header("Authorization") String Bearer);

    @POST(LEAVEAPPLY)
    Call<ResponseBody> call_leaveApplyApi(@Header("Authorization") String Bearer,
                                          @Header("Content-Type") String Content_type,
                                          @Body JsonObject object);

    /*@POST(TENDER_SURVEY_LOCATION_ADD)
    Call<ResponseBody> call_leaveApplyApi(@Header("Authorization") String Bearer,
                                          @Header("Content-Type") String Content_type,
                                          @Body JsonObject object);*/

    @GET(LEAVEAPPLYLIST)
    Call<ResponseBody> call_leaveListApi(@Header("Authorization") String Bearer,
                                         @Header("Content-Type") String Content_type,
                                         @Query("page") String page);

    /*@GET(LEAVEHISTORY)
    Call<ResponseBody> call_leaveHistoryApi(@Header("Authorization") String Bearer,
                                            @Header("Content-Type") String Content_type,
                                            @Path("employee_id") String employee_id);*/

    @GET(LEAVEHISTORY)
    Call<ResponseBody> call_leaveHistoryApi(@Header("Authorization") String Bearer,
                                            @Header("Content-Type") String Content_type,
                                            @Path("employee_id") String employee_id,
                                            @Query("page") String page);

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

    @GET(TENDERS_ADD)
    Call<ResponseBody> call_tenders_add(@Header("Authorization") String Bearer,
                                        @Header("Content-Type") String Content_type);


    @POST(TENDER_SURVEY_LOCATION_ADD)
    Call<ResponseBody> call_tender_survey_location_add(@Header("Authorization") String Bearer,
                                                       @Header("Content-Type") String Content_type,
                                                       @Body JsonObject object);



    @GET(project_site_management_site_add)
    Call<ResponseBody> call_projectlistApi(@Header("Authorization") String Bearer);

    @POST(pms_execution_daily_progress_pandm_add)
    Call<ResponseBody> pms_execution_daily_progress_pandm_add(@Header("Authorization") String Bearer,@Header("Content-Type") String Content_type,@Body JsonObject object);

    @GET(machineries_wp_list)
    Call<ResponseBody> call_machineries_wp_list(@Header("Authorization") String Bearer);

    @GET(projects_details_by_project_site_id)
    Call<ResponseBody> call_projectid(@Header("Authorization") String Bearer,@Header("Content-Type") String Content_type,@Query("site_location") String site_location);

    @POST(pms_execution_labour_progress_add)
    Call<ResponseBody> pms_execution_labour_progress_add(@Header("Authorization") String Bearer,
                                                         @Header("Content-Type") String Content_type,
                                                         @Body JsonObject object);

    @GET(unit_add)
    Call<ResponseBody> call_unit_add(@Header("Authorization") String Bearer);

    @POST(pms_execution_daily_progress_add)
    Call<ResponseBody> call_ppms_execution_daily_progress_add(@Header("Authorization") String Bearer,
                                                              @Header("Content-Type") String Content_type,
                                                              @Body JsonObject object);

    @GET(RAW_MATERIALS_LIST)
    Call<ResponseBody> call_raw_materials_listing(@Header("Authorization") String Bearer,
                                        @Header("Content-Type") String Content_type);

    @GET(CRUSHER_LIST)
    Call<ResponseBody> call_crusher_list_api(@Header("Authorization") String Bearer,
                                        @Header("Content-Type") String Content_type);

    @POST(ESTABLISHMENT_ADD)
    Call<ResponseBody> call_establishment_add(@Header("Authorization") String Bearer,
                                                         @Header("Content-Type") String Content_type,
                                                         @Body JsonObject object);

    @POST(ADD_HYDROLOGICAL)
    Call<ResponseBody> call_add_hydrological(@Header("Authorization") String Bearer,
                                                         @Header("Content-Type") String Content_type,
                                                         @Body JsonObject object);

    @POST(MATERIAL_ADD)
    Call<ResponseBody> call_add_materials(@Header("Authorization") String Bearer,
                                                         @Header("Content-Type") String Content_type,
                                                         @Body JsonObject object);

    @POST(MATERIAL_ADD)
    Call<ResponseBody> call_add_material(@Header("Authorization") String Bearer,
                                                         @Header("Content-Type") String Content_type,
                                                         @Body JSONObject object);

    @POST(ADD_CONTRACT_VENDOR)
    Call<ResponseBody> call_add_contract_vendor(@Header("Authorization") String Bearer,
                                                         @Header("Content-Type") String Content_type,
                                                         @Body JsonObject object);


    @POST(MACHINERY_TYPE_ADD)
    Call<ResponseBody> call_add_machinery(@Header("Authorization") String Bearer,
                                                         @Header("Content-Type") String Content_type,
                                                         @Body JsonObject object);
    @Multipart
    @POST(ADD_SITE_PHOTOSF)
    Call<ResponseBody> call_add_site_photos(@Part MultipartBody.Part file,
                                            @Part("tender") RequestBody tender,
                                            @Part("latitude") RequestBody latitude,
                                            @Part("longitude") RequestBody longitude,
                                            @Part("address") RequestBody address,
                                            @Part("additional_notes") RequestBody additional_notes,
                                            @Part("document_name") RequestBody document_name);
    @Multipart
    @POST(CONTRACT_VENDOR_DOC_ADD)
    Call<ResponseBody> call_add_contract_vendor_w_doc(@Part MultipartBody.Part file,
                                                       @Part("tender") RequestBody tender,
                                                       @Part("module_id") RequestBody module_id,
                                                       @Part("document_name") RequestBody document_name);

    @Multipart
    @POST(MACHINERY_TYPE_ADD_DOC)
    Call<ResponseBody> call_add_machinery_doc(@Part MultipartBody.Part file,
                                                       @Part("tender") RequestBody tender,
                                                       @Part("module_id") RequestBody module_id,
                                                       @Part("document_name") RequestBody document_name);

    @Multipart
    @POST(ADD_HYDROLOGICAL_DOC)
    Call<ResponseBody> call_add_Hydro_doc(@Part MultipartBody.Part file,
                                                       @Part("tender") RequestBody tender,
                                                       @Part("module_id") RequestBody module_id,
                                                       @Part("document_name") RequestBody document_name);




    @Multipart
    @PUT(TENDER_SURVEY_SITE_PHOTOS_EDIT)
    Call<ResponseBody> call_site_photos_edit(@Path("id") Integer id,
                                             @Part MultipartBody.Part file,
                                            @Part("latitude") RequestBody latitude,
                                            @Part("longitude") RequestBody longitude,
                                            @Part("address") RequestBody address,
                                            @Part("additional_notes") RequestBody additional_notes,
                                            @Part("document_name") RequestBody document_name);


    @Multipart
    @POST(TENDER_SURVEY_RESOURCE_ESTABLISHMENT_DOCUMENT_ADD)
    Call<ResponseBody> call_add_establishment_document(@Part MultipartBody.Part file,
                                            @Part("tender") RequestBody tender,
                                            @Part("module_id") RequestBody module_id,
                                            @Part("document_name") RequestBody document_name);



    @GET(TENDER_SURVEY_LOCATION_LIST)
    Call<ResponseBody> call_tender_survey_location_list(@Header("Authorization") String Bearer,
                                                        @Path("tender_id") String tender_id);


    @GET(GET_SITE_PHOTO_LIST)
    Call<ResponseBody> call_get_site_photo_list(@Header("Authorization") String Bearer,
                                                        @Path("tender_id") String tender_id);



    @GET(GET_RESOURCE_ESTABLISHMENT)
    Call<ResponseBody> call_get_establishment(@Header("Authorization") String Bearer,
                                              @Header("Content-Type") String Content_type,
                                              @Query("tender") Integer tender);

    @GET(GET_RESOURCE_HYDRO)
    Call<ResponseBody> call_get_hydro(@Header("Authorization") String Bearer,
                                              @Header("Content-Type") String Content_type,
                                              @Query("tender") Integer tender);

    @GET(GET_CONTRACT_VENDOR)
    Call<ResponseBody> call_get_contract_vendor(@Header("Authorization") String Bearer,
                                              @Header("Content-Type") String Content_type,
                                              @Query("tender") Integer tender);

    @GET(GET_MACHINERY_TYPE_)
    Call<ResponseBody> call_get_machinery(@Header("Authorization") String Bearer,
                                              @Header("Content-Type") String Content_type,
                                              @Query("tender") Integer tender);

    @GET(GET_ALL_UNIT)
    Call<ResponseBody> call_get_unit(@Header("Authorization") String Bearer,
                                              @Header("Content-Type") String Content_type);




}


