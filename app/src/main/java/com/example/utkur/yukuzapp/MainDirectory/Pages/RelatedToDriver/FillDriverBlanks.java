package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.Adapters.CarsArrayAdapter;
import com.example.utkur.yukuzapp.Adapters.CustomAdapter;
import com.example.utkur.yukuzapp.External.DoAction;
import com.example.utkur.yukuzapp.MainDirectory.MainActivityV2;
import com.example.utkur.yukuzapp.Manifest;
import com.example.utkur.yukuzapp.Module.Car;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.Module.VehicleType;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.utkur.yukuzapp.Module.Statics.RESULT_LOAD_IMAGE;

/**
 * Created by Muhammadjon on 11/14/2017.
 */

public class FillDriverBlanks extends AppCompatActivity {

    @BindView(R.id.driver_cars_list)
    ListView cars_list_view;

    @BindView(R.id.driver_cert_img_title)
    TextView driver_cert_img_title_view;

    @BindView(R.id.driver_cert_upload)
    Button btn_upload_image;

    @BindView(R.id.driver_description)
    TextView driver_description;

    @BindView(R.id.alert_text)
    public TextView alert_text;
    public static List<Car> cars;

    @BindView(R.id.add_car_btn)
    public FloatingActionButton btn_add_car;

    private String TAG = "Fill Driver Blank";
    public static CarsArrayAdapter adapter;

    public Car car = Car.getInstance();
    String path = "";
    SharedPreferences preferences;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings_save:

                Ion.with(getBaseContext())
                        .load(Statics.URL.REST.driver_create)
                        .setHeader("Authorization", "token " + token)
                        .setMultipartFile("driver_license", new File(path))
                        .setMultipartParameter("description", driver_description.getText().toString())
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Log.d(TAG, "onCompleted: " + result);
                                try {
                                    result.get("description").getAsString();
                                    preferences.edit().putString(Personal.DRIVER_MODE, String.valueOf(Personal.Settings.ON_DRIVER_MODE)).apply();
                                } catch (Exception ex) {
                                    Toast.makeText(FillDriverBlanks.this, "Could not enable driver mode", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            default:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String token;
    int purpose = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_driver_mode_blank);
        ButterKnife.bind(this);
        purpose = getIntent().getExtras().getInt("purpose");
        preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
        token = preferences.getString(Personal.ID_TOKEN, "null");
        if (purpose == 1) {
            getSupportActionBar().setTitle("" + getIntent().getExtras().getString("username"));
            Ion.with(getBaseContext())
                    .load(Statics.URL.REST.driver_get)
                    .setHeader("Authorization", "token " + token)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onCompleted(Exception e, final JsonObject result) {
                            try {
                                Log.d(TAG, "onCompleted: " + result);
                                driver_cert_img_title_view.setText(result.get("image_title").getAsString());
                                driver_description.setText(result.get("descr").getAsString());
                                btn_upload_image.setText("View");
                                driver_description.setTextColor(getColor(R.color.black));
                                driver_cert_img_title_view.setEnabled(false);
                                driver_description.setEnabled(false);
                                btn_upload_image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Dialog d = new Dialog(FillDriverBlanks.this);
                                        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        ImageView v = new ImageView(getBaseContext());
                                        Picasso.with(getBaseContext()).load(Statics.URL.base_url + result.get("image").getAsString()).into(v);
                                        LinearLayout l = new LinearLayout(getBaseContext());
                                        l.setOrientation(LinearLayout.VERTICAL);

                                        TextView title = new TextView(getBaseContext());
                                        title.setText(result.get("image_title").getAsString());
                                        title.setTextColor(getColor(R.color.black));
                                        title.setGravity(Gravity.CENTER);
                                        title.setPadding(8, 8, 8, 8);
                                        l.addView(title);
                                        l.addView(v);

                                        d.setContentView(l);
                                        d.setCancelable(true);
                                        d.show();
                                    }
                                });
                            } catch (Exception ex) {

                            }
                        }
                    });

        } else if (purpose == 2) {
            btn_upload_image.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "not granted");
                        ActivityCompat.requestPermissions(FillDriverBlanks.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, RESULT_LOAD_IMAGE);
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Log.d(TAG, "asking permission");
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }
                    } else {
                        Log.d(TAG, "granted");
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                }
            });
        }
        cars = new ArrayList<>();
        token = preferences.getString(Personal.ID_TOKEN, "null");

        Ion.with(getApplicationContext())
                .load(Statics.URL.REST.cars_list)
                .setHeader("Authorization", "Token " + token)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        for (int i = 0; i < result.getAsJsonArray().size(); i++) {
                            JsonObject object = result.get(i).getAsJsonObject();
                            Car c = Car.getInstance();
                            c.setTitle(object.get("title").getAsString());
                            c.setNumber(object.get("number").getAsString());
                            c.setMin_kg(object.get("min_kg").getAsInt());
                            c.setMax_kg(object.get("max_kg").getAsInt());
                            c.setCar_type(VehicleType.getTitle(MainActivityV2.vehicleTypeList, object.get("car_type").getAsInt()));
                            cars.add(c);
                        }
                        adapter.notifyDataSetChanged();
                        if (cars.size() <= 0) {
                            alert_text.setVisibility(View.VISIBLE);
                        } else {
                            alert_text.setVisibility(View.GONE);
                        }
                    }
                });
        adapter = new CarsArrayAdapter(getApplicationContext(), R.layout.cars_list_item, cars);
        cars_list_view.setAdapter(adapter);
        cars_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog d = new Dialog(FillDriverBlanks.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LinearLayout linear = DoAction.deletePopupLayout(FillDriverBlanks.this);
                d.setContentView(linear);
                d.setCancelable(true);
                d.show();
                @SuppressLint("ResourceType") Button t = d.findViewById(101);
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("number", cars.get(i).getNumber());
                        Ion.with(FillDriverBlanks.this)
                                .load("DELETE", Statics.URL.REST.delete_car)
                                .setHeader("Authorization", "token " + token)
                                .setJsonObjectBody(obj)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            Log.d(TAG, "onCompleted: " + result);
                                            if (result.get("success").getAsBoolean()) {
                                                Toast.makeText(FillDriverBlanks.this, "Your car " + cars.get(i).getTitle() + " deleted", Toast.LENGTH_SHORT).show();
                                                cars.remove(i);
                                                adapter.notifyDataSetChanged();
                                                d.cancel();
                                            } else {
                                                Toast.makeText(FillDriverBlanks.this, "Your car " + cars.get(i).getTitle() + " could not be deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception ex) {
                                            Log.d(TAG, "onCompleted: " + ex.getMessage() + " second: " + e.getMessage());
                                        }
                                    }
                                });
                    }
                });
                return false;
            }
        });
        btn_add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(FillDriverBlanks.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.fragment_add_car_driver);
                d.setCancelable(true);

                final Spinner s = d.findViewById(R.id.car_type_spinner1);
                final TextView car_title = d.findViewById(R.id.car_create_title);
                final TextView car_number = d.findViewById(R.id.car_create_number);
                final TextView car_min_kg = d.findViewById(R.id.car_min_kg);
                final TextView car_max_kg = d.findViewById(R.id.car_max_kg);
                final Button btn_create = d.findViewById(R.id.create_car_btn);
                final Button btn_cancel = d.findViewById(R.id.back_car_btn);
                final ProgressBar progress_bar_create_car = d.findViewById(R.id.progress_bar_create_car);

                CustomAdapter adapter_vehicle = new CustomAdapter(d.getContext(), MainActivityV2.vehicleTypeList, 2);
                s.setAdapter(adapter_vehicle);
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        VehicleType type = (VehicleType) adapterView.getAdapter().getItem(i);
                        car.setCar_type(type);
                        Log.d(TAG, "onItemSelected: pressed");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.cancel();
                    }
                });
                btn_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        car.setTitle(car_title.getText().toString());
                        car.setMax_kg(Integer.parseInt(car_max_kg.getText().toString()));
                        car.setMin_kg(Integer.parseInt(car_min_kg.getText().toString()));
                        car.setNumber(car_number.getText().toString());
                        try {
                            progress_bar_create_car.setVisibility(View.VISIBLE);
                            SharedPreferences preferences = d.getContext().getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
                            String token = preferences.getString(Personal.ID_TOKEN, "null");
                            Ion.with(d.getContext())
                                    .load(Statics.URL.REST.add_car)
                                    .setHeader("Authorization", "Token " + token)
                                    .setJsonObjectBody(Car.getCreateCarJsonObject(car))
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            try {
                                                Log.d(TAG, "onCompleted: " + result);
                                                if (result.contains(car.getNumber())) {
                                                    d.cancel();
                                                    Toast.makeText(d.getContext(), "Car Created", Toast.LENGTH_SHORT).show();
                                                    cars.add(car);
                                                    FillDriverBlanks.adapter.notifyDataSetChanged();
                                                    progress_bar_create_car.setVisibility(View.GONE);
                                                } else {
                                                    Toast.makeText(d.getContext(), "Not Created, Try again", Toast.LENGTH_SHORT).show();
                                                    progress_bar_create_car.setVisibility(View.GONE);
                                                }
//                                                Toast.makeText(d.getContext(), "Null object", Toast.LENGTH_SHORT).show();
                                            } catch (Exception ex) {
                                                Log.d(TAG, "onCompleted: " + ex.getMessage());
                                            }

                                        }

                                    });
                            Log.d(TAG, "onClick: " + car.toString());
                            Log.d(TAG, "onClick: car created");
                        } catch (Exception e) {
                            Log.d(TAG, "car not created");
                        }
                    }

                });
                d.show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data && purpose == 2) {
            Uri selectedImage = data.getData();
            Log.d(TAG, "onActivityResult: " + selectedImage);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);
            driver_cert_img_title_view.setText(path);
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (purpose == 2) {
            getMenuInflater().inflate(R.menu.profile_settings_menu, menu);
            return true;
        }
        return false;
    }


}
