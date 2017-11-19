package com.example.utkur.yukuzapp.MainDirectory.Pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.FillDriverBlanks;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.FillDriverBlanks_ViewBinding;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        first_name = getIntent().getExtras().getString("fn");
        last_name = getIntent().getExtras().getString("ln");

        text_first_name.setText(first_name);
        text_last_name.setText(last_name);
        push_notif.setChecked(Personal.Settings.ON_PUSH_NOTIFICATION);
        is_driver.setChecked(Personal.Settings.ON_DRIVER_MODE);

        is_driver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
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
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
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
