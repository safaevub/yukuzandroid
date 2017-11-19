package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver;

import android.app.DialogFragment;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Adapters.CarsArrayAdapter;
import com.example.utkur.yukuzapp.Adapters.CustomAdapter;
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

    @BindView(R.id.alert_text)
    public TextView alert_text;
    public static List<Car> cars;

    @BindView(R.id.add_car_btn)
    public FloatingActionButton btn_add_car;

    private String TAG = "Fill Driver Blank";
    public static CarsArrayAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_driver_mode_blank);
        ButterKnife.bind(this);

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
        cars = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
        String token = preferences.getString(Personal.ID_TOKEN, "null");

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

        btn_add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment p = new CreateCarPopup("Add A New Car", 1);
                p.show(getFragmentManager(), "create car popup");
            }
        });
    }

    String path = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
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
        getMenuInflater().inflate(R.menu.profile_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings_save:
                break;
            default:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
