package net.umaass_providers.app.data.remote;


import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Appointment;
import net.umaass_providers.app.data.remote.models.AppointmentDetail;
import net.umaass_providers.app.data.remote.models.Category;
import net.umaass_providers.app.data.remote.models.City;
import net.umaass_providers.app.data.remote.models.Comment;
import net.umaass_providers.app.data.remote.models.Country;
import net.umaass_providers.app.data.remote.models.Customer;
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
import net.umaass_providers.app.data.remote.models.WorkingHoursItem;
import net.umaass_providers.app.models.ChangeLanguageModel;
import net.umaass_providers.app.models.ModelIntroducer;
import net.umaass_providers.app.models.NewIndustry;
import net.umaass_providers.app.models.UpdateAppointmentTime;
import net.umaass_providers.app.models.UpdateIndustry;

import java.util.List;

import retrofit2.http.Path;

public interface RemoteSource {

    void getLogin(String access_token,String phoneNumber,
                  CallBack<Api<Login>> callBack);

    void register(String access_token, String name, String email, String birthday, String gender,String phoneNumber, String introduceCode,
                  CallBack<Api<Register>> callBack);

    void getAllIndustries(boolean mine, CallBack<Api<List<Industry>>> callBack);

    void getAllCategories(String lang,CallBack<Api<List<Category>>> callBack);

    void getCitys(String id, CallBack<Api<List<City>>> callBack);

    void getAllAppointments(String user_id,int page_number, String status, String start_date, String end_date, CallBack<Api<List<Appointment>>> callBack);

    void getProfile(CallBack<Api<Profile>> callBack);

    void createStaff(String industry_id, String name, String phone, String email, String role, List<Integer> permissions, CallBack<Api<DefualtResponse>> callBack);


    void deleteStaff(String staff_id, CallBack<Api<DefualtResponse>> callBack);

    void updateStaff(String staff_id,
                     String userName,
                     String phone,
                     String email,
                     String role,
                     List<Integer> permissions,
                     CallBack<Api<DefualtResponse>> callBack);

    void uploadAvatar(String imagePath, CallBack<Api<DefualtResponse>> callBack);

    void createService(
            String industry_id,
            String title,
            String duration,
            String price,
            String TypeTime,
            String Typeprice,
            String notes_for_the_customer,
            CallBack<Api<List<ServiceResult>>> callBack
                      );

    void updateService(
            String service_id,
            String title,
            String duration,
            String price,
            String notes_for_the_customer,
            String TypeTime,
            String Typeprice,
            CallBack<Api<DefualtResponse>> callBack
                      );


    void deleteService(String service_id, CallBack<Api<DefualtResponse>> callBack);


    void createIndustry(NewIndustry newIndustry,
                        CallBack<Api<IndustryResult>> callBack);


    void uploadAppointmentFiles(String appointment_id_, String image_, CallBack<Api<DefualtResponse>> callBack);

    void uploadImageIndustry(
            String industry_id,
            String manner,
            String image,
            CallBack<Api<DefualtResponse>> callBack);

    void deleteImageIndustry(String id, CallBack<Api<DefualtResponse>> callBack);


    void updateProfile(
            String name,
            String email,
            String desc,
            String birthday,
            String gender,
            String language,
            String device_type,
            String device_id,
            String device_token,
            CallBack<Api<DefualtResponse>> callBack);


    void showIndustry(
            String industry_id,
            CallBack<Api<ShowIndustry>> callBack);

    void updateWorkingHours(
            String industry_id,
            List<WorkingHoursItem> workingHours,
            CallBack<Api<DefualtResponse>> callBack);

    void getSuggestionsAppointment(
            String appointment_id,
            CallBack<Api<List<Suggestion>>> callBack);

    void updateAppointment(
            String appointment_id,
            UpdateAppointmentTime new_date_time,
            CallBack<Api<DefualtResponse>> callBack);


    void updateIndustry(
            UpdateIndustry updateIndustry,
            CallBack<Api<IndustryResult>> callBack);

    void getAppointmentDetail(
            String appointment_id,
            CallBack<Api<AppointmentDetail>> callBack);

    void deleteAppointment(
            String appointment_id,
            CallBack<Api<DefualtResponse>> callBack);

    void getComments(
            String provider_id,
            int page,
            CallBack<Api<List<Comment>>> callBack);

    void aboutUs(String lang,CallBack<Api<String>> callBack);

    void getRules(String lang,CallBack<Api<String>> callBack);

    void getCustomers(
            String name,
            int page_number,
            CallBack<Api<List<Customer>>> callBack);


    void  deleteAppointmentImage(
            @Path("appointment_id") String appointment_id,
            @Path("image_id") String image_id , CallBack<Api<DefualtResponse>> callBack);

    void suggestDoctor(SuggestModel model, CallBack<DefualtResponse> callBack);

    void getListIntroducer(CallBack<Api<List<ModelIntroducer>>> callBack);

    void addDoctorToGroup(String mobile, CallBack<Api> callBack);

    void deleteDoctorGroup(String userId, CallBack<Api> callBack);

    void changeLanguage(ChangeLanguageModel model, CallBack<Api<DefualtResponse>> callBack);

    void getCountries(CallBack<Api<List<Country>>> callBack);

    void statusReferal(String status, String id, CallBack<Api<DefualtResponse>> callBack);

    void getReferalData(CallBack<Api<List<ReferalModel>>> callBack);

    void getNotifications(String lang, CallBack<Api<List<NotificationsModel>>> callBack);

    void getUnreadNotifications(CallBack<Api<List<NotificationsModel>>> callBack);

    void changeToReadNotifications(String id, CallBack<Api<DefualtResponse>> callBack);
}
