package com.example.utkur.yukuzapp.MainDirectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.EntranceActivity;
import com.example.utkur.yukuzapp.External.DoAction;
import com.example.utkur.yukuzapp.MainDirectory.CreateOrder.Deliveries;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson.PersonProfile;
import com.example.utkur.yukuzapp.Module.CurrencyType;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.Module.VehicleType;
import com.example.utkur.yukuzapp.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.utkur.yukuzapp.Module.Statics.RESULT_PERMISSION_LOCATION;

/**
 * Created by Muhammadjon on 11/9/2017.
 */

@SuppressLint("Registered")
public class MainActivityV2 extends AppCompatActivity {
    String TAG = "MAIN Activity";
    Deliveries fr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);
        if (DoAction.isNetworkAvailable(getBaseContext())) {
            initialize();
            SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
            String token = preferences.getString(Personal.ID_TOKEN, "null");
            if (token.equals("null")) {
                Intent intent = new Intent(MainActivityV2.this, EntranceActivity.class);
                startActivity(intent);
                finish();
            }

            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, RESULT_PERMISSION_LOCATION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Log.d(TAG, "asking permission");

                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_PERMISSION_LOCATION);
                    }
                }
            }


            fr = new Deliveries();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.working_fragment_location1, fr).commit();
        } else
            Toast.makeText(this, "No Internet Connection is available", Toast.LENGTH_SHORT).show();

        String fb_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onCreate: " + fb_token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.RESULT_PERMISSION_COARSE && resultCode == RESULT_OK) {
            Log.d(TAG, "coarse permission is granted");
        }
    }

    public static List<CurrencyType> currencyTypeList = new ArrayList<>();
    public static List<VehicleType> vehicleTypeList = new ArrayList<>();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        @SuppressLint("WrongConstant") SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
        switch (item.getItemId()) {
            case R.id.mainmenu_profile:
                Intent intent = new Intent(this, PersonProfile.class);
                startActivity(intent);
                break;
            case R.id.mainmenu_log_out:
                preferences.edit().remove(Personal.ID_TOKEN).apply();
                Toast.makeText(getBaseContext(), "Logged out", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, EntranceActivity.class);
                startActivity(intent);
                finish();
            case R.id.mainmenu_update_list:
                Deliveries.postOrders.clear();
                fr.getView().findViewById(R.id.progress_circle).setVisibility(View.VISIBLE);
                Ion.with(fr.getContext())
                        .load(Statics.URL.REST.get_unpicked_orders)
                        .setHeader("Authorization", "Token " + preferences.getString(Personal.ID_TOKEN, "null"))
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                try {
                                    if (result != null) {
                                        Log.d("result", "onCompleted: " + result);
                                        for (int i = 0; i < result.size(); i++) {
                                            PostOrder order = PostOrder.getOrderByJsonObject(result.get(i).getAsJsonObject());
                                            Deliveries.postOrders.add(order);
                                            fr.unpickedRequestsAdapter.notifyDataSetChanged();
                                        }
                                        if (Deliveries.postOrders.isEmpty())
                                            fr.no_post_text_alert.setVisibility(View.VISIBLE);
                                        else fr.no_post_text_alert.setVisibility(View.GONE);
                                        fr.getView().findViewById(R.id.progress_circle).setVisibility(View.GONE);
                                        Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(fr.getContext(), "Something went wrong or check your internet connection", Toast.LENGTH_SHORT).show();
                                } catch (Exception ex) {
                                    Toast.makeText(fr.getContext(), e.getMessage() + "-" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            default:
                break;
        }
        return true;
    }

    public void initialize() {
        getSupportActionBar().setTitle((!Personal.IS_DRIVER) ? "Person" : "Driver");

        Ion.with(getBaseContext())
                .load(Statics.URL.REST.get_currency_types)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.d(TAG, "onCompleted: " + result);
                        if (result != null)
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject object = result.get(i).getAsJsonObject();

                                Log.d(TAG, "onCompleted: " + object.getAsJsonObject("fields").get("title").getAsString());
                                currencyTypeList.add(new CurrencyType(object.get("pk").getAsInt(),
                                        object.getAsJsonObject("fields").get("sign").getAsString(),
                                        object.getAsJsonObject("fields").get("title").getAsString()));
                            }
                    }
                });
        Ion.with(getBaseContext())
                .load(Statics.URL.REST.get_vehicle_type)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for (int i = 0; i < result.size(); i++) {
                            JsonObject obj = result.get(i).getAsJsonObject();
                            vehicleTypeList.add(new VehicleType(obj.get("id").getAsInt(), obj.get("title").getAsString(), obj.get("description").getAsString()));
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }
}
