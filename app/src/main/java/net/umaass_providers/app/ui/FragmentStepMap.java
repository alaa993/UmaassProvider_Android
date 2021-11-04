package net.umaass_providers.app.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.models.NewIndustry;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.utils.NotificationCenter;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.helper.PermissionHelper;

public class FragmentStepMap extends BaseFragment implements BlockingStep, NotificationCenter.NotificationCenterDelegate {

    private NewIndustry newIndustry;
    private Toolbar toolbar;
    private ImageView btnSave;
    private boolean inStep = false;

    public void setNewIndustry(NewIndustry newIndustry) {
        this.newIndustry = newIndustry;
    }

    public void setInStep(boolean inStep) {
        this.inStep = inStep;
    }


    @Override
    public int getViewLayout() {
        return R.layout.fragment_step_map;
    }

    @Override
    public void readView() {
        super.readView();
        toolbar = baseView.findViewById(R.id.toolbar);
        btnSave = baseView.findViewById(R.id.btnSave);
    }

    @Override
    public void functionView() {
        super.functionView();
        toolbar.setVisibility(inStep ? View.GONE : View.VISIBLE);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (!inStep) {
            initilizeMap();
        }

    }

    private GoogleMap googleMap;
    private LatLng selectedLocation;

    private void initilizeMap() {
        if (googleMap == null && getActivity() != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        FragmentStepMap.this.googleMap = googleMap;
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.getUiSettings().setZoomGesturesEnabled(true);
                        initLocation();
                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                googleMap.clear();
                                selectedLocation = latLng;
                                googleMap.addMarker(new MarkerOptions().position(latLng).title(Utils.getString(R.string.location)));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            }
                        });

                    }
                });
            }
        }
    }

    private boolean isGpsEnable() {
        LocationManager locationManager = (LocationManager) G.getInstance().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void initLocation() {
        PermissionHelper.requestLocation(new PermissionHelper.OnPermissionGrantedListener() {
            @Override
            public void onPermissionGranted() {
                if (isGpsEnable()) {
                    direction();
                } else {
                    if (getActivity() != null) {
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        baseActivity.buildAlertMessageNoGps();
                    }
                }
            }
        });


    }

    private void direction() {
        if (googleMap != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity() != null) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            }
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError verificationError = null;
        if (selectedLocation == null) {
            verificationError = new VerificationError(Utils.getString(R.string.please_select_a_location));
        } else {
            if (newIndustry != null) {
                newIndustry.lat = selectedLocation.latitude;
                newIndustry.lng = selectedLocation.longitude;
            }
        }
        return verificationError;
    }

    @Override
    public void onSelected() {
        initilizeMap();
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        G.toast(error.getErrorMessage());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.GPSEnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.GPSEnable);
    }


    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.GPSEnable) {
            direction();
        }
    }
}
