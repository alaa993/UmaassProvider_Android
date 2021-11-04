package net.umaass_providers.app.data.remote.service;


import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Appointment;
import net.umaass_providers.app.data.remote.models.AppointmentDetail;
import net.umaass_providers.app.data.remote.models.Category;
import net.umaass_providers.app.data.remote.models.City;
import net.umaass_providers.app.data.remote.models.Comment;
import net.umaass_providers.app.data.remote.models.Country;
import net.umaass_providers.app.data.remote.models.Customer;
import net.umaass_providers.app.data.remote.models.CustomerAppointment;
import net.umaass_providers.app.data.remote.models.CustomerAppointmentDetail;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.Industry;
import net.umaass_providers.app.data.remote.models.IndustryResult;
import net.umaass_providers.app.data.remote.models.Login;
import net.umaass_providers.app.data.remote.models.NotificationsModel;
import net.umaass_providers.app.data.remote.models.Profile;
import net.umaass_providers.app.data.remote.models.ReferalModel;
import net.umaass_providers.app.data.remote.models.Register;
import net.umaass_providers.app.data.remote.models.ServiceResult;
import net.umaass_providers.app.data.remote.models.ShowIndustry;
import net.umaass_providers.app.data.remote.models.SuggestModel;
import net.umaass_providers.app.data.remote.models.Suggestion;
import net.umaass_providers.app.models.ChangeLanguageModel;
import net.umaass_providers.app.models.ModelIntroducer;
import net.umaass_providers.app.models.NewIndustryForSend;
import net.umaass_providers.app.models.NewService;
import net.umaass_providers.app.models.NewStaff;
import net.umaass_providers.app.models.UpdateAppointmentTime;
import net.umaass_providers.app.models.UpdateIndustry;
import net.umaass_providers.app.models.UpdateWorkingHours;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {

    @FormUrlEncoded
    @POST("/api/login")
    Call<Api<Login>> getLogin(@Field("access_token") String access_token,
                              @Field("phone") String phoneNumber);

    @FormUrlEncoded
    @POST("/api/register")
    Call<Api<Register>> register(@Field("access_token") String access_token,
                                 @Field("name") String name,
                                 @Field("email") String email,
                                 @Field("birthdate") String birthday,
                                 @Field("gender") String gender,
                                 @Field("phone") String phoneNumber,
                                 @Field("introduce_code") String introducerCode);


    @GET("/api/profile")
    Call<Api<Profile>> getProfile();

    @GET("/api/cities")
    Call<Api<List<City>>> getCitys(@Query("province_id") String province_id);


    @FormUrlEncoded
    @PUT("/api/profile")
    Call<Api<DefualtResponse>> updateProfile(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("description") String desc,
                                             @Field("birthdate") String birthdate,
                                             @Field("gender") String gender,
                                             @Field("language") String language,
                                             @Field("device_type") String device_type,
                                             @Field("device_id") String device_id,
                                             @Field("device_token") String device_token,
                                             @Field("current_password") String current_password,
                                             @Field("new_password") String new_password,
                                             @Field("new_password_confirmation") String new_password_confirmation);

    @Multipart
    @POST("/api/uploadAvatar")
    Call<Api<DefualtResponse>> uploadAvatar(
            @Part MultipartBody.Part avatar
                                           );

    @DELETE("/api/deleteAvatar")
    Call<Api<DefualtResponse>> deleteAvatar();


    @POST("/api/staff")
    Call<Api<DefualtResponse>> createStaff(@Body List<NewStaff> newStaffs
                                          );

    @PATCH("/api/staff/{staff_id}")
    Call<Api<DefualtResponse>> updateStaff(
            @Path("staff_id") String staff_id,
            @Body NewStaff newStaffs
                                          );

    @DELETE("/api/staff/{staff_id}")
    Call<Api<DefualtResponse>> deleteStaff(@Path("staff_id") String staff_id);

    @POST("/api/services")
    Call<Api<List<ServiceResult>>> createService(@Body List<NewService> newService);

    @FormUrlEncoded
    @PUT("/api/services/{service_id}")
    Call<Api<DefualtResponse>> updateService(
            @Path("service_id") String service_id,
            @Field("title") String title,
            @Field("duration") String duration,
            @Field("price") String price,
            @Field("notes_for_the_customer") String description,
            @Field("TypeTime") String TypeTime,
            @Field("TypePrice") String TypePrice
                                         );

    @DELETE("/api/services/{service_id}")
    Call<Api<DefualtResponse>> deleteService(@Path("service_id") String service_id);

    @GET("/api/industries")
    Call<Api<List<Industry>>> getAllIndustries(
            @Query("mine") boolean mine
                                              );

    @POST("/api/industries")
    Call<Api<IndustryResult>> createIndustry(@Body NewIndustryForSend newIndustryForSend
                                            );

    @GET("/api/industries/{industry_id}")
    Call<Api<ShowIndustry>> showIndustry(
            @Path("industry_id") String industry_id
                                        );

    @PUT("/api/industries/{industry_id}")
    Call<Api<IndustryResult>> updateIndustry(
            @Path("industry_id") String industry_id,
            @Body UpdateIndustry updateIndustry);



    @Multipart
    @POST("/api/industries/uploadImage")
    Call<Api<DefualtResponse>> uploadImageIndustry(
            @Part("industry_id") RequestBody industry_id,
            @Part("manner") RequestBody manner,
            @Part MultipartBody.Part image
                                                  );

    @Multipart
    @POST("/api/appointments/{appointment_id}/image")
    Call<Api<DefualtResponse>> uploadFileAppointment(
            @Path("appointment_id") String appointment_id,
            @Part MultipartBody.Part image);

    @DELETE("/api/industries/deleteImage")
    Call<Api<DefualtResponse>> deleteImageIndustry(@Query("id") String id);


    @GET("/api/categories")
    Call<Api<List<Category>>> getAllCategories(@Query("lang") String lang);

    @GET("/api/page/about")
    Call<Api<String>> aboutUs(@Query("lang") String lang);

    @GET("/api/page/rules")
    Call<Api<String>> getRules(@Query("lang") String lang);

    @GET("/api/appointments")
    Call<Api<List<Appointment>>> getAllAppointments(
            @Query("user_id") String user_id,
            @Query("status") String status,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date,
            @Query("page") int page_number);

    @GET("/api/customers")
    Call<Api<List<Customer>>> getCustomers(
            @Query("name") String name,
            @Query("page") int page_number);

    @DELETE("/api/appointments/{appointment_id}/image/{image_id}")
    Call<Api<DefualtResponse>> deleteAppointmentImage(
            @Path("appointment_id") String appointment_id,
            @Path("image_id") String image_id);


    @GET("/api/appointments/{appointment_id}")
    Call<Api<AppointmentDetail>> getAppointmentDetail(
            @Path("appointment_id") String appointment_id
                                                     );


    @PUT("/api/appointments/{appointment_id}")
    Call<Api<DefualtResponse>> updateAppointment(
            @Path("appointment_id") String appointment_id,
            @Body UpdateAppointmentTime new_date_time
                                                );


    @GET("/api/appointments/{appointment_id}/suggestions")
    Call<Api<List<Suggestion>>> getSuggestionsAppointment(@Path("appointment_id") String appointment_id);

    @DELETE("/api/customer/appointments/{appointment_id}")
    Call<Api<DefualtResponse>> deleteAppointment(@Path("appointment_id") String appointment_id);

    @GET("/api/customer/appointments")
    Call<Api<List<CustomerAppointment>>> getAllCustomerAppointments(
            @Field("status") String status
                                                                   );

    @GET("/api/customer/appointments/{appointment_id}")
    Call<Api<CustomerAppointmentDetail>> getCustomerAppointmentDetail(
            @Path("appointment_id") String appointment_id,
            @Field("service_id") String service_id,
            @Field("client_name") String client_name,
            @Field("client_phone") String client_phone
                                                                     );


    @PUT("/api/industries/{industry_id}/workingHours")
    Call<Api<DefualtResponse>> updateWorkingHours(
            @Path("industry_id") String industry_id,
            @Body UpdateWorkingHours workingHours);

    @GET("/api/customer/providers/{provider_id}/comments")
    Call<Api<List<Comment>>> getComments(@Path("provider_id") String provider_id, @Query("page") int page_number);

    @POST("/api/suggest-provider")
    Call<DefualtResponse> suggest(
            @Body SuggestModel model);

    @GET("/api/children")
    Call<Api<List<ModelIntroducer>>> getListIntroducer();

    @FormUrlEncoded
    @PATCH("/api/referal/add")
    Call<Api> addDoctorTGroup(@Field("mobile") String mobile);

    @FormUrlEncoded
    @PATCH("/api/referal/remove")
    Call<Api> deleteDoctorGroup(@Field("user_id") String user_id);

    @FormUrlEncoded
    @PATCH("/api/referal/{id}/status")
    Call<Api<DefualtResponse>> statusReferal(@Field("status") String status, @Path("id") String id);

    @PATCH("/api/user/language")
    Call<Api<DefualtResponse>> changeLanguage(@Body ChangeLanguageModel model);

    @GET("/api/provinces")
    Call<Api<List<Country>>> getCountries();

    @GET("/api/referal")
    Call<Api<List<ReferalModel>>> getReferals();

    @GET("/api/notifications")
    Call<Api<List<NotificationsModel>>> getNotifications(@Query("lang") String lang);

    @GET("/api/notifications")
    Call<Api<List<NotificationsModel>>> getUnreadNotifications(@Query("unread") String unread);

    @PATCH("api/notifications/{id}")
    Call<Api<DefualtResponse>> changeToReadNotifications(@Path("id") String id);
}
