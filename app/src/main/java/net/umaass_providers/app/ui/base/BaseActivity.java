package net.umaass_providers.app.ui.base;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.utils.LocaleUtils;
import net.umaass_providers.app.utils.NotificationCenter;
import net.umaass_providers.app.utils.Utils;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        Context x = LocaleUtils.setLocale(newBase, Preference.getLanguage());
        super.attachBaseContext(ViewPumpContextWrapper.wrap(x));
    }

    //=====================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enhance);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void readView() {

    }

    public void functionView() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

    }

    public void initViewModel() {

    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Utils.getString(R.string.your_gps_seems_to_be_disabled))
               .setCancelable(false)
               .setPositiveButton(Utils.getString(R.string.yes), new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id) {
                       //  startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                       enableLocationSettings();
                   }
               })
               .setNegativeButton(Utils.getString(R.string.no), new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id) {
                       dialog.cancel();
                   }
               });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                                                         .setInterval(1000)
                                                         .setFastestInterval(1000)
                                                         .setNumUpdates(1)
                                                         .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    // startUpdatingLocation(...);
                })
                .addOnFailureListener(this, ex -> {
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(BaseActivity.this, 100);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (100 == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                NotificationCenter.getInstance().postNotificationName(NotificationCenter.GPSEnable);
            } else {
                NotificationCenter.getInstance().postNotificationName(NotificationCenter.GPSDisable);
                G.toast(Utils.getString(R.string.gps_is_off));
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }

    }


}//END
