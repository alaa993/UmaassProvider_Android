package net.umaass_providers.app.ui;


import android.app.Application;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.Industry;
import net.umaass_providers.app.data.remote.models.Login;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.ChangeLanguageModel;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.ui.components.RoundCornerButton;
import net.umaass_providers.app.ui.login.Authentication;
import net.umaass_providers.app.utils.LocaleUtils;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.Arrays;
import java.util.List;


public class ActivityWelcome extends BaseActivity {

    RoundCornerButton btnRegister;
    RoundCornerButton btnLogin;
    RoundCornerButton btnLang;
    private final int REQUESR_LOG = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        readView();
        functionView();
        initViewModel();
    }

    @Override
    public void readView() {
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnLang = findViewById(R.id.btnlanguege1);


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void functionView() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.PhoneBuilder().build()))
                                .setTheme(R.style.AppTheme).build(), REQUESR_LOG);
                //startActivity(new Intent(ActivityWelcome.this, Authentication.class));

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ActivityWelcome.this, Authentication.class));
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.PhoneBuilder().build()))
                                .setTheme(R.style.AppTheme).build(), REQUESR_LOG);

            }
        });
        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanguageDialog();
            }
        });
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
               LocaleUtils.setLocale(getBaseContext(), Preference.getLanguage());
                dialogInterface.dismiss();
                if (getApplication() != null) {
                    Intent intent = new Intent(getApplication(), ActivityWelcome.class);
                    startActivity(intent);
                    finish();
                }
            }
        })
                .setTitle(Utils.getString(R.string.choose_language))
                .setCancelable(true)
                .show();

    }
    private void checkLogin(final String access_token,String phoneNumber) {
        showLoading();
        Repository.getInstance().getLogin(access_token,phoneNumber, new CallBack<Api<Login>>() {
            @Override
            public void onSuccess(Api<Login> s) {
                super.onSuccess(s);
//                hideLoading();
                Preference.setToken(s.getData().getToken());
                checkNeedInit();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                if (e.getCode() == 412) {
                    intentToRegister(access_token,phoneNumber);
                } else {
                    G.toast(Utils.getString(R.string.try_again));
                    finish();
                }

            }
        });
    }

    private void checkNeedInit() {
        Repository.getInstance().getAllIndustries(true, new CallBack<Api<List<Industry>>>() {
            @Override
            public void onSuccess(Api<List<Industry>> listApi) {
                super.onSuccess(listApi);
                if (listApi == null || listApi.getData() == null || listApi.getData().size() == 0) {
                    intentToInit();
                } else {
                    Preference.setActiveIndustryId(listApi.getData().get(0).getId() + "");
                    intentToMain();
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                G.toast(Utils.getString(R.string.try_again));
                finish();
            }
        });
    }

    private void intentToMain() {
        ActivityUtils.finishAllActivitiesExceptNewest();
        Intent intent = new Intent(ActivityWelcome.this, ActivityMain.class);
        startActivity(intent);
        finish();
    }

    private void intentToInit() {
        ActivityUtils.finishAllActivitiesExceptNewest();
        Intent intent = new Intent(ActivityWelcome.this, ActivityIndustryCreator.class);
        startActivity(intent);
        finish();
    }

    private void intentToRegister(String access_token,String phoneNumber) {
        ActivityUtils.finishAllActivitiesExceptNewest();
        Intent intent = new Intent(ActivityWelcome.this, ActivityRegister.class);
        intent.putExtra("access_token", access_token);
        intent.putExtra("phone", phoneNumber);
        startActivity(intent);
        finish();
    }

    ProgressDialog progressDialog;

    private void showLoading() {
        progressDialog = new ProgressDialog(ActivityUtils.getTopActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.pls_wait));
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
             //   progressDialog.dismiss();
            }
        }
    }
    private void getData() {
        showLoading();
        Repository.getInstance().aboutUs(Preference.getLanguage(), new CallBack<Api<String>>() {
            @Override
            public void onSuccess(Api<String> stringApi) {
                super.onSuccess(stringApi);
                hideLoading();


            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESR_LOG) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                if (!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()) {
                    checkLogin(FirebaseAuth.getInstance().getCurrentUser().getUid(),FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    return;
                } else {
                    if (response == null) {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Toast.makeText(this, "NO internet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        Toast.makeText(this, "Unkonw erorrs", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    moveToRegisterActivity();
                }
            }
        }
    }


}
