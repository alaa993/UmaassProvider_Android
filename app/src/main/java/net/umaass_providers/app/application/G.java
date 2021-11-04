package net.umaass_providers.app.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import net.umaass_providers.app.R;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.UtilsLib;

import java.io.File;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

import static net.umaass_providers.app.application.BuildVars.DEBUG_VERSION;

public class G extends Application {

    public static Context context;
    private static G instance;
    public static SharedPreferences sharedPref;
    public static String temporary_Token = null;

    private static final String PREFS_NAME = "umaass";
    public static final Handler HANDLER = new Handler();
    public static final Boolean DEBUG = DEBUG_VERSION;
    public static final String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DIR_APP = DIR_SDCARD + "/umaass";
    public static final String DIR_DOWNLOAD = DIR_APP + "/Download";
    public static boolean changeProfile = false;
    public static boolean changeFile = false;
    public static boolean changeIndustry = false;
    public static boolean changeList = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        new File(DIR_APP).mkdirs();
        new File(DIR_DOWNLOAD).mkdirs();

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ViewPump.init(ViewPump.builder()
                              .addInterceptor(new CalligraphyInterceptor(
                                      new CalligraphyConfig.Builder()
                                              .setFontAttrId(R.attr.fontPath)
                                              .setDefaultFontPath("fonts/Aller_Rg.ttf")
                                              .build()))
                              .build());


        UtilsLib.init(this);

    }

    public static G getInstance() {
        return instance;
    }


    public static void log(String tag, String body) {
        if (DEBUG) {
            Log.i(tag, body);
        }
    }


    public static void toast(final String body) {
        Utils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, body, Toast.LENGTH_LONG).show();
            }
        });

    }


}
