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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.EntranceActivity;
import com.example.utkur.yukuzapp.External.DoAction;
import com.example.utkur.yukuzapp.MainDirectory.CreateOrder.UnpickedOrdersFragment;
import com.example.utkur.yukuzapp.MainDirectory.Pages.ProfileSettingsActivity;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.FillDriverBlanks;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.utkur.yukuzapp.Module.Statics.RESULT_PERMISSION_LOCATION;

/**
 * Created by Muhammadjon on 11/9/2017.
 */

@SuppressLint("Registered")
public class MainActivityV2 extends AppCompatActivity {
    String TAG = "MAIN Activity";
    UnpickedOrdersFragment fragment_unpicked_orders;

    SharedPreferences preferences;
    private Toolbar toolbar;

    NavigationView nav_view;
    CircleImageView imageView;
    TextView username;
    TextView usernumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav_view = findViewById(R.id.nav_view);
        View v = nav_view.getHeaderView(0);

        navViewMenuItemSelected();
        imageView = v.findViewById(R.id.profile_avatar1);
        username = v.findViewById(R.id.user_name);
        usernumber = v.findViewById(R.id.user_number);
//        getSupportActionBar().setIcon(getDrawable(R.drawable.ic_menu_black_24dp));
        preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
        if (DoAction.isNetworkAvailable(getBaseContext())) {
            initialize();
            final String token = preferences.getString(Personal.ID_TOKEN, "null");
            final Intent intent = new Intent(MainActivityV2.this, EntranceActivity.class);
            if (token.equals("null")) {
                startActivity(intent);
                finish();
            } else {
                Ion.with(getBaseContext())
                        .load(Statics.URL.REST.initialize)
                        .setHeader("Authorization", "token " + token)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                get_user_credentials();

                                try {
                                    Log.d(TAG, "onCompleted: " + result);
                                    preferences.edit().putString(Personal.DRIVER_MODE, result.get("driver").getAsString())
                                            .putBoolean(Personal.DRIVER_ACTIVE, result.get("driver_active").getAsBoolean()).apply();
                                    String fb_token = FirebaseInstanceId.getInstance().getToken();
                                    Log.d(TAG, "onCreate: " + fb_token);
                                    nav_view.getMenu().getItem(1).setVisible(result.get("driver_active").getAsBoolean());
                                    @SuppressLint("DefaultLocale") String version = String.format("%d, %s, %s", Build.VERSION.SDK_INT, Build.MANUFACTURER, Build.MODEL);
                                    JsonObject obj = Personal.getCreateDeviceJsonObject(fb_token, result.get("driver").getAsBoolean(), version, "1");
                                    Ion.with(getBaseContext())
                                            .load(Statics.URL.REST.create_device)
                                            .setHeader("Authorization", "token " + token)
                                            .setJsonObjectBody(obj)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    if (result != null) {
                                                        try {
                                                            Log.d(TAG, "onCompleted: " + result);
                                                            if (result.get("created").getAsBoolean()) {
                                                                Log.d(TAG, "your device is created: " + result);
                                                            }
                                                        } catch (Exception ex) {
                                                            Log.d(TAG, "onCompleted: " + ex.getMessage());
                                                        }
                                                    }
                                                }
                                            });
                                } catch (Exception ex) {
                                    Log.d(TAG, "not_finished: " + ex.getMessage());
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
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


            fragment_unpicked_orders = new UnpickedOrdersFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.working_fragment_location1, fragment_unpicked_orders).commit();
        } else
            Toast.makeText(this, "No Internet Connection is available", Toast.LENGTH_SHORT).show();
    }

    private void navViewMenuItemSelected() {
        nav_view.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.driver_menu_settings:
                        Log.d(TAG, "onMenuItemClick: go to settings");
                        Intent i = new Intent(getBaseContext(), ProfileSettingsActivity.class);
                        i.putExtra("fn", fn);
                        i.putExtra("ln", ln);
                        startActivity(i);
                        break;
                    default:
                        Log.d(TAG, "onMenuItemClick: " + menuItem.getTitle());

                        break;
                }
                return true;
            }
        });
        nav_view.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_posts_un_picked:
                        fragment_unpicked_orders = new UnpickedOrdersFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.working_fragment_location1, fragment_unpicked_orders).commit();
                        break;
                    case R.id.nav_posts_picked:
                        break;
                    default:
                        Log.d(TAG, "onMenuItemClick: " + menuItem.getTitle());
                        break;

                }
                return true;
            }
        });
        nav_view.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.driver_menu_field_profile:
                        Intent i = new Intent(MainActivityV2.this, FillDriverBlanks.class);
                        i.putExtra("purpose", 1);
                        i.putExtra("username", fn + " " + ln);
                        startActivity(i);
                        break;
                    default:
                        Log.d(TAG, "onMenuItemClick: " + menuItem.getItemId());
                        break;
                }
                return true;
            }
        });
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
    protected void onResume() {
        super.onResume();
        nav_view.getMenu().getItem(1).setVisible(Personal.getIsDriverActive(getBaseContext()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
        Intent intent;
        switch (item.getItemId()) {
            case R.id.mainmenu_log_out:
                preferences.edit().remove(Personal.ID_TOKEN).apply();
                Toast.makeText(getBaseContext(), "Logged out", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, EntranceActivity.class);
                startActivity(intent);
                finish();
            case R.id.mainmenu_update_list:
                UnpickedOrdersFragment.postOrders.clear();
                fragment_unpicked_orders.getView().findViewById(R.id.progress_circle).setVisibility(View.VISIBLE);
                Ion.with(fragment_unpicked_orders.getContext())
                        .load(Statics.URL.REST.get_unpicked_orders)
                        .setHeader("Authorization", "Token " + Personal.getToken(getBaseContext()))
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                try {
                                    if (result != null) {
                                        Log.d("result", "onCompleted: " + result);
                                        for (int i = 0; i < result.size(); i++) {
                                            PostOrder order = PostOrder.getOrderByJsonObject(result.get(i).getAsJsonObject());
                                            UnpickedOrdersFragment.postOrders.add(order);
                                            fragment_unpicked_orders.unpickedRequestsAdapter.notifyDataSetChanged();
                                        }
                                        if (UnpickedOrdersFragment.postOrders.isEmpty())
                                            fragment_unpicked_orders.no_post_text_alert.setVisibility(View.VISIBLE);
                                        else
                                            fragment_unpicked_orders.no_post_text_alert.setVisibility(View.GONE);
                                        fragment_unpicked_orders.getView().findViewById(R.id.progress_circle).setVisibility(View.GONE);
                                        Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(fragment_unpicked_orders.getContext(), "Something went wrong or check your internet connection", Toast.LENGTH_SHORT).show();
                                } catch (Exception ex) {
                                    Toast.makeText(fragment_unpicked_orders.getContext(), e.getMessage() + "-" + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
//        getSupportActionBar().setTitle((!Personal.IS_DRIVER) ? "Person" : "Driver");
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

    String fn;
    String ln;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    private void get_user_credentials() {
        Ion.with(getBaseContext())
                .load(Statics.URL.REST.get_creds)
                .setHeader("Authorization", "Token " + getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code).getString(Personal.ID_TOKEN, "null"))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            try {
                                Log.d(TAG, "Got_creds" + result.toString());
                                Picasso.with(getBaseContext())
                                        .load(Statics.URL.load_image_url + result.get("image").getAsString())
                                        .into(imageView);
                                fn = result.get("first_name").getAsString();
                                ln = result.get("last_name").getAsString();
                                username.setText(fn + " " + ln);
//                                getSupportActionBar().setTitle(result.get("first_name").getAsString() + " " + result.get("last_name").getAsString());
                                usernumber.setText("+" + result.get("phone").getAsString());
                            } catch (Exception ex) {
                                Log.d(TAG, "onCompleted: " + ex.getMessage());
                            }
                        }
                    }
                });
    }
}
