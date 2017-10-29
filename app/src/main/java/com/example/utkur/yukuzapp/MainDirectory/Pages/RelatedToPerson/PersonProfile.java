package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Muhammadjon on 10/23/2017.
 */

public class PersonProfile extends AppCompatActivity {
    @BindView(R.id.tabHost)
    public TabHost tabHost;
    @BindView(R.id.profile_avatar)
    public CircleImageView imageView;
    @BindView(R.id.user_name)
    public TextView textView;

    //about
    @BindView(R.id.ssn_text)
    public TextView ssn;
    @BindView(R.id.phone_number_text)
    public TextView phone_number;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        tabHost.setup();
        TabHost.TabSpec spec = tabHost.newTabSpec("Posts");
        spec.setContent(R.id.frame_posts);
        spec.setIndicator("Posts");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("About");
        spec.setContent(R.id.frame_about);
        spec.setIndicator("About");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Settings");
        spec.setContent(R.id.frame_settins);
        spec.setIndicator("Settings");
        tabHost.addTab(spec);
        Log.d(TAG, "onCreate: " + getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code).getString(Personal.ID_TOKEN, "null"));
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
                                Log.d(TAG, "onCompleted: " + result.toString());
                                Picasso.with(getBaseContext())
                                        .load(Statics.URL.load_image_url + result.get("image").getAsString())
                                        .into(imageView);
                                textView.setText(result.get("first_name").getAsString() + " " + result.get("last_name").getAsString());
                                getSupportActionBar().setTitle(result.get("first_name").getAsString() + " " + result.get("last_name").getAsString());
                                ssn.setText("SSN: " + result.get("ssn").getAsString());
                                phone_number.setText("Phone Number: +" + result.get("phone").getAsString());
                            }catch (Exception ex){
                                Log.d(TAG, "onCompleted: "+ex.getMessage());
                            }
                        }
                    }
                });
    }

    private String TAG = "ProfilePerson";
}
