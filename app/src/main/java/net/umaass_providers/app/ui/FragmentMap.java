package net.umaass_providers.app.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.models.PointMark;
import net.umaass_providers.app.ui.adapter.AdapterCategory;
import net.umaass_providers.app.ui.base.BaseFragment;

public class FragmentMap extends BaseFragment implements OnMapReadyCallback {

    private RecyclerView recyclerViewCategory;
    private AdapterCategory adapterCategory;
    private MapView mapView;
    private GoogleMap map;
    private ClusterManager<PointMark> clusterManager;
    private static double LAT_X = 31.321779;
    private static double LAT_Y = 48.670841;


    @Override
    public int getViewLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerViewCategory = baseView.findViewById(R.id.recyclerViewCategory);
        mapView = baseView.findViewById(R.id.mapView);
    }


    @Override
    public void functionView() {
        super.functionView();
        adapterCategory = new AdapterCategory();
        LinearLayoutManager categoryManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(categoryManager);
        recyclerViewCategory.setAdapter(adapterCategory);
        initMap(null);
    }

    private void initMap(Bundle savedInstanceState) {
        MapView mapView = baseView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                                                       android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                map.setMyLocationEnabled(true);
                return false;
            }
        });

        clusterManager = new ClusterManager<>(getContext(), map);
        clusterManager.setAnimation(true);
        // clusterManager.setRenderer(new MyClusterRenderer(getActivity(), map, clusterManager));
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        // map.setOnInfoWindowClickListener(clusterManager);

        notifyPoint();

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PointMark>() {
            @Override
            public boolean onClusterClick(Cluster<PointMark> cluster) {
                float zoom = map.getCameraPosition().zoom + 2;
                goToLocation(cluster.getPosition(), zoom);
                return true;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PointMark>() {
            @Override
            public boolean onClusterItemClick(PointMark pointMark) {
                if (getActivity() != null) {
                    /*ItemBottomDialogFragment itemBottomDialogFragment = new ItemBottomDialogFragment(getActivity());
                    itemBottomDialogFragment.setCodeMelk(pointMark.getMelkCode());
                    itemBottomDialogFragment.setIdTypeNoeKarbari(pointMark.getTypeNoekarbari());
                    itemBottomDialogFragment.show();*/
                }
                return false;
            }
        });

        G.log("ZOOM max", map.getMaxZoomLevel() + "");
        G.log("ZOOM min", map.getMinZoomLevel() + "");
        goToDefultLocation();

    }

    private void notifyPoint() {
        if (clusterManager == null) {
            return;
        }
       /* if (map != null) {
            map.clear();
        }*/
        clusterManager.clearItems();
        /*for (PointMark pointMark : pointMarks) {
            clusterManager.addItem(pointMark);
        }*/
        clusterManager.cluster();
        goToDefultLocation();
    }


    private void goToDefultLocation() {
        goToLocation(new LatLng(LAT_X, LAT_Y), 12.5f);
    }

    private void goToLocation(LatLng latLng, float zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //  map.setMinZoomPreference(12);
        //  map.setMaxZoomPreference(21);
        //  map.setLatLngBoundsForCameraTarget(toBounds(latLng, 30000));
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }


}
