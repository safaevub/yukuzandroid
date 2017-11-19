package com.example.utkur.yukuzapp.MainDirectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.utkur.yukuzapp.Module.Statics.RESULT_PERMISSION_LOCATION;

public class AddressSelectorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @BindView(R.id.map_view)
    MapView map_view;
    private String TAG = "MAP";
    String LatLang = "";
    String return_address = "";
    private LatLng myLocation;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            LatLng p = mMap.getProjection().fromScreenLocation(new Point((int) event.getX(), (int) event.getY()));
            Log.d(TAG, "onTouchEvent: " + p.latitude + " " + p.longitude);
            mMap.addMarker(new MarkerOptions().position(p).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
            Log.d(TAG, "onTouchEvent: ");
            return true;
        }
        Log.d(TAG, "event not one: ");
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selector);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        if (map_view != null) {
            map_view.onCreate(null);
            map_view.onResume();
            map_view.getMapAsync(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_menu_ok:
                Intent intent = new Intent();
                intent.putExtra("address", return_address);
                intent.putExtra("location", LatLang);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.RESULT_PERMISSION_LOCATION && resultCode == RESULT_OK) {
            Log.d(TAG, "location permission is granted");
            if (map_view != null) {
                map_view.onCreate(null);
                map_view.onResume();
                map_view.getMapAsync(this);
            }
        }
        if (requestCode == Statics.RESULT_PERMISSION_COARSE && resultCode == RESULT_OK) {
            Log.d(TAG, "coarse permission is granted");
        }
    }

    public void goToPlace(LatLng location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }

    public LatLng getMyLocation(GoogleMap map, LocationManager manager) {
        Criteria criteria = new Criteria();
        String provider = manager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        Location myLocation = manager.getLastKnownLocation(provider);
        return new LatLng((myLocation != null ? myLocation.getLatitude() : 0), (myLocation != null ? myLocation.getLongitude() : 0));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(googleMap.getMapType()).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(true);
        MapsInitializer.initialize(getBaseContext());

        // Add a marker in Sydney and move the camera
        LatLng lng = getMyLocation(mMap, (LocationManager) getSystemService(LOCATION_SERVICE));
        myLocation = lng;
        String address = "Current Location";
        try {
            address = getAddress(lng, getBaseContext());
            LatLang = lng.latitude + "/" + lng.longitude;
            return_address = address;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(lng).title(address));
        goToPlace(lng);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                String address = "Current Location";
                try {
                    address = getAddress(latLng, getBaseContext());
                    LatLang = String.format("%f/%f", latLng.latitude, latLng.longitude);

                    return_address = address;
                    Log.d(TAG, "onMapClick: " + LatLang);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMap.addMarker(new MarkerOptions().position(latLng).title(address));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        });
    }

    public static String getAddress(LatLng latLng, Context context) throws IOException {
        Geocoder geocoder;
        List<Address> addressList;
        geocoder = new Geocoder(context, Locale.getDefault());
        addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (addressList != null && addressList.size() > 0) {
            try {
                String address = addressList.get(0).getAddressLine(0);
                String city = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String country = addressList.get(0).getCountryName();
                String postCode = addressList.get(0).getPostalCode();
                String knownName = addressList.get(0).getFeatureName();
                return address + " " + postCode + country;
            } catch (Exception ex) {
                return "null address location";
            }
        }
        return "";
    }
}
