package net.umaass_providers.app.data;


import android.annotation.SuppressLint;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.RemoteRepository;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Appointment;
import net.umaass_providers.app.data.remote.models.AppointmentDetail;
import net.umaass_providers.app.data.remote.models.Avatar;
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
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.ChangeLanguageModel;
import net.umaass_providers.app.models.ModelIntroducer;
import net.umaass_providers.app.models.NewIndustry;
import net.umaass_providers.app.models.UpdateAppointmentTime;
import net.umaass_providers.app.models.UpdateIndustry;

import java.util.List;


public class Repository implements RepositoryMethod {

    private static Repository INSTANCE;
    private RemoteRepository remoteRepository;

    public static Repository getInstance() {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository();
                }
            }
        }
        return INSTANCE;
    }

    private Repository() {
        this.remoteRepository = RemoteRepository.getInstance();
        sendToken();
    }

    private void sendToken() {
        if (Preference.isLogin()) {
            getProfile(null);
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        @SuppressLint("HardwareIds")
                        String androidId = Settings.Secure.getString(G.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
                        String token = task.getResult().getToken();
                        updateProfile(null, Preference.getEmail(), null, null,
                                Preference.getGender() + "",
                                Preference.getLanguage().toUpperCase(),
                                "android",
                                "provider_" + androidId, token, new CallBack<Api<DefualtResponse>>() {
                                    @Override
                                    public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                                        super.onSuccess(defualtResponseApi);
                                    }
                                });
                    }
                }
            });
        }
    }

    @Override
    public void getLogin(String access_token, String phoneNumber, CallBack<Api<Login>> callBack) {
        remoteRepository.getLogin(access_token, phoneNumber, new CallBack<Api<Login>>() {
            @Override
            public void onSuccess(Api<Login> loginApi) {
                super.onSuccess(loginApi);
                sendToken();
                if (callBack != null) {
                    callBack.onSuccess(loginApi);
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                if (callBack != null) {
                    callBack.onFail(e);
                }
            }
        });
    }

    @Override
    public void register(String access_token, String name, String email, String birthday, String gender, String phoneNumber, String introducerCode, CallBack<Api<Register>> callBack) {
        remoteRepository.register(access_token, name, email, birthday, gender, phoneNumber, introducerCode, new CallBack<Api<Register>>() {
            @Override
            public void onSuccess(Api<Register> registerApi) {
                super.onSuccess(registerApi);
                sendToken();
                if (callBack != null) {
                    callBack.onSuccess(registerApi);
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                if (callBack != null) {
                    callBack.onFail(e);
                }
            }
        });
    }

    @Override
    public void getAllIndustries(boolean mine, CallBack<Api<List<Industry>>> callBack) {
        remoteRepository.getAllIndustries(mine, callBack);
    }

    @Override
    public void getAllCategories(String lang, CallBack<Api<List<Category>>> callBack) {
        remoteRepository.getAllCategories(lang, callBack);
    }

    @Override
    public void getCitys(String id, CallBack<Api<List<City>>> callBack) {
        remoteRepository.getCitys(id, callBack);
    }

    @Override
    public void getCountries(CallBack<Api<List<Country>>> callBack) {
        remoteRepository.getCountries(callBack);
    }

    @Override
    public void getAllAppointments(String user_id, int page_number, String status, String start_date, String end_date, CallBack<Api<List<Appointment>>> callBack) {
        remoteRepository.getAllAppointments(user_id, page_number, status, start_date, end_date, callBack);
    }

    @Override
    public void getProfile(CallBack<Api<Profile>> callBack) {
        remoteRepository.getProfile(new CallBack<Api<Profile>>() {
            @Override
            public void onSuccess(Api<Profile> profileApi) {
                super.onSuccess(profileApi);
                Profile profile = profileApi.getData();
                Preference.setPhone(profile.getPhone());
                Preference.setGender(profile.getGender());
                Preference.setEmail(profile.getEmail());
                Preference.setIdUser(profile.getId() + "");
                Preference.setFirstName(profile.getName());
                Avatar avatar = profile.getAvatar();
                if (avatar != null) {
                    Preference.setImage(avatar.getUrlMd());
                }
                if (callBack != null) {

                    callBack.onSuccess(profileApi);
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                if (callBack != null) {
                    callBack.onFail(e);
                }
            }
        });
    }


    @Override
    public void createStaff(String industry_id, String name, String phone, String email, String role, List<Integer> permissions, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.createStaff(industry_id, name, phone, email, role, permissions, callBack);
    }

    @Override
    public void deleteStaff(String staff_id, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.deleteStaff(staff_id, callBack);
    }

    @Override
    public void updateStaff(String staff_id, String userName, String phone, String email, String role, List<Integer> permissions, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.updateStaff(staff_id, userName, phone, email, role, permissions, callBack);
    }

    @Override
    public void uploadAvatar(String imagePath, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.uploadAvatar(imagePath, callBack);
    }

    @Override
    public void createService(String industry_id, String title, String duration, String price, String notes_for_the_customer,String TypeTime,String Typeprice, CallBack<Api<List<ServiceResult>>> callBack) {
        remoteRepository.createService(industry_id, title, duration, price, notes_for_the_customer,TypeTime,Typeprice, callBack);
    }

    @Override
    public void updateService(String service_id, String title, String duration, String price, String notes_for_the_customer,String TypeTime,String TypePrice, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.updateService(service_id, title, duration, price, notes_for_the_customer,TypeTime,TypePrice, callBack);
    }

    @Override
    public void deleteService(String service_id, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.deleteService(service_id, callBack);
    }

    @Override
    public void createIndustry(NewIndustry newIndustry, CallBack<Api<IndustryResult>> callBack) {
        remoteRepository.createIndustry(newIndustry, callBack);
    }

    @Override
    public void uploadImageIndustry(String industry_id, String manner, String image, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.uploadImageIndustry(industry_id, manner, image, callBack);
    }

    @Override
    public void deleteImageIndustry(String id, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.deleteImageIndustry(id, callBack);
    }

    @Override
    public void updateProfile(String name, String email, String desc, String birthday, String gender, String language, String device_type,
                              String device_id,
                              String device_token, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.updateProfile(name, email, desc, birthday, gender, language, device_type, device_id, device_token, callBack);
    }

    @Override
    public void showIndustry(String industry_id, CallBack<Api<ShowIndustry>> callBack) {
        remoteRepository.showIndustry(industry_id, callBack);
    }

    @Override
    public void updateWorkingHours(String industry_id, List<WorkingHoursItem> workingHours, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.updateWorkingHours(industry_id, workingHours, callBack);
    }

    @Override
    public void getSuggestionsAppointment(String industry_id, CallBack<Api<List<Suggestion>>> callBack) {
        remoteRepository.getSuggestionsAppointment(industry_id, callBack);
    }

    @Override
    public void updateAppointment(String appointment_id, UpdateAppointmentTime new_date_time, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.updateAppointment(appointment_id, new_date_time, callBack);
    }

    @Override
    public void getAppointmentDetail(String appointment_id, CallBack<Api<AppointmentDetail>> callBack) {
        remoteRepository.getAppointmentDetail(appointment_id, callBack);
    }

    @Override
    public void deleteAppointment(String appointment_id, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.deleteAppointment(appointment_id, callBack);
    }

    @Override
    public void getComments(String provider_id, int page, CallBack<Api<List<Comment>>> callBack) {
        remoteRepository.getComments(provider_id, page, callBack);
    }

    @Override
    public void aboutUs(String lang, CallBack<Api<String>> callBack) {
        remoteRepository.aboutUs(lang, callBack);
    }

    @Override
    public void getRules(String lang, CallBack<Api<String>> callBack) {
        remoteRepository.getRules(lang, callBack);
    }

    @Override
    public void updateIndustry(UpdateIndustry updateIndustry, CallBack<Api<IndustryResult>> callBack) {
        remoteRepository.updateIndustry(updateIndustry, callBack);
    }

    @Override
    public void getCustomers(String name, int page_number, CallBack<Api<List<Customer>>> callBack) {
        remoteRepository.getCustomers(name, page_number, callBack);
    }

    @Override
    public void uploadAppointmentFiles(String appointment_id_, String image_, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.uploadAppointmentFiles(appointment_id_, image_, callBack);
    }

    @Override
    public void deleteAppointmentImage(String appointment_id, String image_id, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.deleteAppointmentImage(appointment_id, image_id, callBack);
    }

    @Override
    public void suggestDoctor(SuggestModel model, CallBack<DefualtResponse> callBack) {
        remoteRepository.suggestDoctor(model, callBack);
    }

    @Override
    public void getListIntroducer(CallBack<Api<List<ModelIntroducer>>> callBack) {
        remoteRepository.getListIntroducer(callBack);
    }

    @Override
    public void addDoctorToGroup(String mobile, CallBack<Api> callBack) {
        remoteRepository.addDoctorToGroup(mobile, callBack);
    }

    @Override
    public void deleteDoctorGroup(String userId, CallBack<Api> callBack) {
        remoteRepository.deleteDoctorGroup(userId, callBack);
    }

    @Override
    public void changeLanguage(ChangeLanguageModel model, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.changeLanguage(model, callBack);
    }

    @Override
    public void statusReferal(String status, String id, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.statusReferal(status, id, callBack);
    }

    @Override
    public void getReferalData(CallBack<Api<List<ReferalModel>>> callBack) {
        remoteRepository.getReferalData(callBack);
    }


    @Override
    public void getNotifications(String lang, CallBack<Api<List<NotificationsModel>>> callBack) {
        remoteRepository.getNotifications(lang, callBack);
    }

    @Override
    public void getUnreadNotifications(CallBack<Api<List<NotificationsModel>>> callBack) {
        remoteRepository.getUnreadNotifications(callBack);
    }

    @Override
    public void changeToReadNotifications(String id, CallBack<Api<DefualtResponse>> callBack) {
        remoteRepository.changeToReadNotifications(id, callBack);
    }
}
