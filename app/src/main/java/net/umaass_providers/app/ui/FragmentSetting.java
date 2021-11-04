package net.umaass_providers.app.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;

import com.squareup.picasso.Picasso;

import net.umaass_providers.app.BuildConfig;
import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Avatar;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.NotificationsModel;
import net.umaass_providers.app.data.remote.models.Profile;
import net.umaass_providers.app.data.remote.models.ShowIndustry;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.ChangeLanguageModel;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.utils.CircleImageView;
import net.umaass_providers.app.utils.LocaleUtils;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentSetting extends BaseFragment {
    TextView btnWorkingHours;
    TextView btnStaff;
    TextView btnServices;
    TextView btnGallery;
    TextView txtName;
    TextView txtDesc;
    TextView txtAddress;
    TextView txtPhone;
    TextView btnLocation;
    TextView btnAboutUs;
    TextView btnSetting;
    TextView btnShare;
    TextView btnLogout;
    TextView btnContactUs;
    TextView btnEditIndustry;
    TextView btnComment;
    TextView btnLanguege;
    CircleImageView imgProfile;
    RatingBar ratingBar;
    private TextView btn_suggestion;
    TextView btn_Introducer_code;
    TextView txtIntroducer;
    private TextView btnNotify;
    private TextView tvCount;


    @Override
    public int getViewLayout() {
        return R.layout.fragment_setting;
    }


    @Override
    public void readView() {
        super.readView();
        btnWorkingHours = baseView.findViewById(R.id.btnWorkingHours);
        btnStaff = baseView.findViewById(R.id.btnStaff);
        btnServices = baseView.findViewById(R.id.btnServices);
        txtDesc = baseView.findViewById(R.id.txtDesc);
        btnComment = baseView.findViewById(R.id.btnComment);
        txtPhone = baseView.findViewById(R.id.txtPhone);
        btnLocation = baseView.findViewById(R.id.btnLocation);
        txtAddress = baseView.findViewById(R.id.txtAddress);
        btnGallery = baseView.findViewById(R.id.btnGallery);
        imgProfile = baseView.findViewById(R.id.imgProfile);
        btnSetting = baseView.findViewById(R.id.btnSetting);
        btnLanguege = baseView.findViewById(R.id.btnLanguege);
        btnAboutUs = baseView.findViewById(R.id.btnAboutUs);
        btnLogout = baseView.findViewById(R.id.btnLogout);
        txtName = baseView.findViewById(R.id.txtName);
        ratingBar = baseView.findViewById(R.id.ratingBar);
        btnContactUs = baseView.findViewById(R.id.btnContactUs);
        btnEditIndustry = baseView.findViewById(R.id.btnEditIndustry);
        btnShare = baseView.findViewById(R.id.btnShare);
        btn_suggestion = baseView.findViewById(R.id.btn_suggestion);
        btn_Introducer_code = baseView.findViewById(R.id.btn_Introducer_code);
        txtIntroducer = baseView.findViewById(R.id.txtIntroducer);
        btnNotify = baseView.findViewById(R.id.btnNotifications);
        tvCount = baseView.findViewById(R.id.tvCount);

    }


    @Override
    public void functionView() {
        super.functionView();

        btn_Introducer_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityMyGroup.class);
                startActivity(intent);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildConfig.DEBUG) {
                    startActivity(new Intent(getActivity(), ActivityIndustryCreator.class));
                }
            }
        });

        btnLanguege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanguageDialog();
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityComments.class);
                intent.putExtra("id", Preference.getActiveStaffId());
                startActivity(intent);
            }
        });

        txtName.setText(Preference.getFirstName());
        txtPhone.setText(Preference.getPhone());
        Picasso.get()
               .load(Preference.getImage())
               .placeholder(R.drawable.profile)
               .error(R.drawable.profile)
               .into(imgProfile);

        btnWorkingHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null) {
                    mfragmentNavigation.pushFragment(new FragmentStepHours());
                }

            }
        });

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ActivityNotifications.class);
                startActivity(intent);
            }
        });

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null) {
                    mfragmentNavigation.pushFragment(new FragmentContactUs());
                }

            }
        });
        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null) {
                    mfragmentNavigation.pushFragment(new FragmentStepStaff());
                }
            }
        });
        btnLocation.setVisibility(View.GONE);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null) {
                    mfragmentNavigation.pushFragment(new FragmentStepMap());
                }
            }
        });

        btn_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySuggestion.class);
                startActivity(intent);

            }
        });
        btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null) {
                    mfragmentNavigation.pushFragment(new FragmentStepService());
                }
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null) {
                    mfragmentNavigation.pushFragment(new FragmentGallery());
                }
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ActivityEditProfile.class), 100);
            }
        });
        btnEditIndustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ActivityEditIndustry.class), 200);
            }
        });
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null) {
                    mfragmentNavigation.pushFragment(new FragmentAboutUs());
                }
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        //ratingBar.setVisibility(View.GONE);
        ratingBar.setRating(Preference.getRate());
        getProfile();
        getRatingBarData();
        getUnreadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnreadData();
    }

    private void share() {
        try {
            Activity activity = ActivityUtils.getTopActivity();
            if (activity == null) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Check out " + Utils.getString(R.string.app_name) + " at:\n\n");
            builder.append("http://onelink.to/6acjfz");

            ShareCompat.IntentBuilder.from(activity)
                                     .setType("text/plain")
                                     .setChooserTitle("Share to")
                                     .setText(builder)
                                     .startChooser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPop() {
        super.onPop();
        if (G.changeProfile) {
            G.changeProfile = false;
            getProfile();
        }
        if (G.changeIndustry) {
            G.changeIndustry = false;
            getRatingBarData();
        }

    }

    private void getUnreadData() {

        Repository.getInstance().getUnreadNotifications(new CallBack<Api<List<NotificationsModel>>>() {
            @Override
            public void onSuccess(Api<List<NotificationsModel>> listApi) {
                super.onSuccess(listApi);
                List<NotificationsModel> lists = new ArrayList<>();
                for (int i = 0; i < listApi.getData().size(); i++) {
                    if (listApi.getData().get(i).getApp().equals("Provider")){
                        lists.add(listApi.getData().get(i));
                    }
                }
                tvCount.setText(lists.size() + "");
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
            }
        });
    }


    private void dialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityUtils.getTopActivity());
        builder1.setMessage(Utils.getString(R.string.are_you_sure_for_logout));
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                Utils.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Preference.logOut();
                        startActivity(new Intent(getActivity(), ActivityWelcome.class));
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                });

        builder1.setNegativeButton(
                Utils.getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void showLanguageDialog() {

        final String[] selectedLanguage = {"English"};
        String[] arrayServiceTypes;
        arrayServiceTypes = new String[4];
        arrayServiceTypes[0] = "English";
        arrayServiceTypes[1] = "عربى";
        arrayServiceTypes[2] = "Kurdî";
        arrayServiceTypes[3] = "Türkçesi";

        AlertDialog.Builder materialBuilder = new AlertDialog.Builder(ActivityUtils.getTopActivity());
        materialBuilder.setSingleChoiceItems(arrayServiceTypes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedLanguage[0] = arrayServiceTypes[i];
                if (selectedLanguage[0].equals("English")) {
                    Preference.setLanguage("en");
                }
                if (selectedLanguage[0].equals("عربى")) {
                    Preference.setLanguage("ar");
                }
                if (selectedLanguage[0].equals("Kurdî")) {
                    Preference.setLanguage("ku");
                }if (selectedLanguage[0].equals("Türkçesi")) {
                    Preference.setLanguage("tr");
                }
                String androidId = Settings.Secure.getString(G.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);

                ChangeLanguageModel changeLanguageModel = new ChangeLanguageModel();
                changeLanguageModel.setLanguage(Preference.getLanguage().toUpperCase());
                changeLanguageModel.setDevice_id("provider_" + androidId);
                Repository.getInstance()
                        .changeLanguage(changeLanguageModel, new CallBack<Api<DefualtResponse>>() {
                            @Override
                            public void onSuccess(Api<DefualtResponse> api) {
                                super.onSuccess(api);

                            }

                            @Override
                            public void onFail(RequestException e) {
                                super.onFail(e);

                            }
                        });
                LocaleUtils.setLocale(getContext(), Preference.getLanguage());
                dialogInterface.dismiss();
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        })
                .setTitle(Utils.getString(R.string.choose_language))
                .setCancelable(true)
                .show();

    }


    private void getProfile() {

        Repository.getInstance().getProfile(new CallBack<Api<Profile>>() {
            @Override
            public void onSuccess(Api<Profile> profileApi) {
                super.onSuccess(profileApi);
                Profile profile = profileApi.getData();
                Preference.setPhone(profile.getPhone());
                Preference.setGender(profile.getGender());
                Preference.setEmail(profile.getEmail());
                Preference.setIdUser(profile.getId() + "");
                Preference.setFirstName(profile.getName());
                txtName.setText(profile.getName());
                txtPhone.setText(profile.getPhone());
                txtIntroducer.setText(getString(R.string.Introducer_code)+" : "+profile.getIntroduce_code());


                Avatar avatar = profile.getAvatar();
                if (avatar != null) {
                    Preference.setImage(avatar.getUrlMd());
                    Picasso.get()
                           .load(avatar.getUrlMd())
                           .placeholder(R.drawable.profile)
                           .error(R.drawable.profile)
                           .into(imgProfile);
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
            }
        });
    }


    private void getRatingBarData() {
        Repository.getInstance().showIndustry(Preference.getActiveIndustryId(), new CallBack<Api<ShowIndustry>>() {
            @Override
            public void onSuccess(Api<ShowIndustry> listApi) {
                super.onSuccess(listApi);
                if (listApi != null) {
                    if (listApi.getData() != null) {
                        ShowIndustry industry = listApi.getData();
                        if (industry.getStaff().size() > 0) {
                            Preference.setRate(industry.getStaff().get(0).getRate());
                            Preference.setActiveStaffId(industry.getStaff().get(0).getId() + "");
                            ratingBar.setRating(Preference.getRate());
                        }
                        txtDesc.setText(industry.getDescription());
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            getProfile();
        } else if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            getRatingBarData();
        }
    }
}
