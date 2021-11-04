package net.umaass_providers.app.ui;


import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Industry;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.utils.LocaleUtils;
import net.umaass_providers.app.utils.Utils;

import java.util.List;


public class ActivitySplash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LocaleUtils.setLocale(ActivitySplash.this, Preference.getLanguage());
        readView();
        functionView();
        initViewModel();
    }

    @Override
    public void readView() {
        super.readView();
    }

    @Override
    public void functionView() {
        super.functionView();
        YoYo.with(Techniques.BounceIn)
            .duration(1000)
            .onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(Animator animator) {
                    goNextStep();
                }
            })
            .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.BounceIn)
            .duration(1000)
            .onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(Animator animator) {
                }
            })
            .playOn(findViewById(R.id.txtName));
    }

   /* private void showLanguageDialog() {
        final String[] selectedLanguage = {"English"};
        String[] arrayServiceTypes;
        arrayServiceTypes = new String[3];
        arrayServiceTypes[0] = "English";
        arrayServiceTypes[1] = "عربى";
        arrayServiceTypes[2] = "Kurdî";
        if (arrayServiceTypes.length > 0) {
            AlertDialog.Builder materialBuilder = new AlertDialog.Builder(this);
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
                    }
                    LocaleUtils.setLocale(ActivitySplash.this, Preference.getLanguage());
                    startActivity(new Intent(ActivitySplash.this, ActivityWelcome.class));
                    finish();
                }
            })
                           .setTitle(Utils.getString(R.string.choose_language))
                           .setCancelable(false)
                           .show();

        }
    }*/

    private void goNextStep() {
        if (Preference.isLogin()) {
            Repository.getInstance().getAllIndustries(true, new CallBack<Api<List<Industry>>>() {
                @Override
                public void onSuccess(Api<List<Industry>> listApi) {
                    super.onSuccess(listApi);
                    if (listApi != null) {
                        if (listApi.getData() != null) {
                            if (listApi.getData().size() > 0) {
                                Industry industry = listApi.getData().get(0);
                                Preference.setActiveIndustryId(String.valueOf(industry.getId()));
                                Preference.setActiveStaffId(String.valueOf(industry.getId()));
                                startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
                                finish();
                            } else {
                                startActivity(new Intent(ActivitySplash.this, ActivityIndustryCreator.class));
                                finish();
                            }
                        } else {
                            G.toast(Utils.getString(R.string.try_again));
                        }
                    } else {
                        G.toast(Utils.getString(R.string.try_again));
                    }
                }

                @Override
                public void onFail(RequestException e) {
                    super.onFail(e);
                    G.toast(Utils.getString(R.string.try_again));
                }
            });
        } else {
            startActivity(new Intent(ActivitySplash.this, ActivityWelcome.class));
            finish();
        }
    }
}