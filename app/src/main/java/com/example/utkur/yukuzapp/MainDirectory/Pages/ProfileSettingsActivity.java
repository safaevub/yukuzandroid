package com.example.utkur.yukuzapp.MainDirectory.Pages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.FillDriverBlanks;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.FillDriverBlanks_ViewBinding;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 11/12/2017.
 */

public class ProfileSettingsActivity extends AppCompatActivity {
    private String TAG = "Profile Settings";
    String first_name, last_name;
    @BindView(R.id.settings_first_name)
    AutoCompleteTextView text_first_name;
    @BindView(R.id.settings_last_name)
    AutoCompleteTextView text_last_name;
    @BindView(R.id.settings_push_notification)
    Switch push_notif;
    @BindView(R.id.settings_driver_enable)
    Switch is_driver;
    private boolean driver_mode = false;
    SharedPreferences preferences;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            driver_mode = preferences.getBoolean(Personal.DRIVER_ACTIVE, false);
            boolean b = preferences.getBoolean(Personal.NOTIF_MODE, false);
            push_notif.setChecked(b);
        } catch (Exception ex) {
            Log.d(TAG, "on_resume_error: " + ex.getMessage());
        }
        is_driver.setChecked(driver_mode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        ButterKnife.bind(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
        driver_mode = Boolean.parseBoolean(preferences.getString(Personal.DRIVER_MODE, "false"));
        Log.d(TAG, "onCreate: " + driver_mode);
        is_driver.setChecked(driver_mode);

        first_name = getIntent().getExtras().getString("fn");
        last_name = getIntent().getExtras().getString("ln");

        text_first_name.setText(first_name);
        text_last_name.setText(last_name);
        push_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    preferences.edit().putBoolean(Personal.NOTIF_MODE, Personal.Settings.ON_PUSH_NOTIFICATION).apply();
                else
                    preferences.edit().putBoolean(Personal.NOTIF_MODE, !Personal.Settings.ON_PUSH_NOTIFICATION).apply();

            }
        });
        is_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_driver.isChecked()) {
                    boolean is_driver_mode = Boolean.parseBoolean(preferences.getString(Personal.DRIVER_MODE, "false"));
                    if (!is_driver_mode) {
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(ProfileSettingsActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(ProfileSettingsActivity.this);
                        }
                        builder.setTitle("Be Driver")
                                .setMessage("You are about to move to driver mode. You have to fill some blanks to be a driver")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), FillDriverBlanks.class);
                                        intent.putExtra("prupose", 2);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else
                        toggle_driver_mode(getBaseContext(), Personal.getToken(preferences), preferences);

                } else {
                    toggle_driver_mode(getBaseContext(), Personal.getToken(preferences), preferences);
                }
            }
        });
    }

    private void toggle_driver_mode(Context context, String token, final SharedPreferences preferences) {
        Ion.with(context)
                .load("PATCH", Statics.URL.REST.driver_active_toggle)
                .setHeader("Authorization", "Token " + token)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (result.get("success").getAsBoolean()) {
                                preferences.edit().putBoolean(Personal.DRIVER_ACTIVE, result.get("driver_mode").getAsBoolean()).apply();
                                Log.d(TAG, "driver_mode: " + result.get("driver_mode").getAsBoolean());
                            }
                        } catch (Exception ex) {
                            Log.d(TAG, "onCompleted: " + ex.getMessage());
                        }
                    }
                });
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
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

                break;
            default:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
