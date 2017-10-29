package com.example.utkur.yukuzapp.Authentication.Registration;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_next_stepXML)
    ImageButton next_step;
    private String TAG = "Registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

//        terms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent term = new Intent(RegisterActivity.this, Term_And_ConditionActivity.class);
//           startActivity(term);
//            }
//        });
        final RegisterActivity_1 reg1 = new RegisterActivity_1();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.register_frame, reg1).commit();


//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reg1.onSubmit()) {
                    ///////////////
                    if (reg1.password.getText().toString().equals(reg1.password_repeated.getText().toString())) {
                        final JsonObject object = new JsonObject();
                        final String username = reg1.email.getText().toString().split("@")[0];
                        object.addProperty("username", username);
                        object.addProperty("email", reg1.email.getText().toString());
                        object.addProperty("first_name", reg1.first_name.getText().toString());
                        object.addProperty("last_name", reg1.last_name.getText().toString());
                        object.addProperty("password", reg1.password.getText().toString());
                        Ion.with(getBaseContext())
                                .load(Statics.URL.REST.register  + "1/")
                                .setJsonObjectBody(object)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        Log.d(TAG, "input: " + object.toString());
                                        if (result != null) {
                                            JsonObject obj = new JsonObject();
                                            obj.addProperty("username", username);
                                            obj.addProperty("password", reg1.password.getText().toString());
                                            Ion.with(getBaseContext()).load(Statics.URL.getToeknURL)
                                                    .setJsonObjectBody(obj)
                                                    .asJsonObject()
                                                    .setCallback(new FutureCallback<JsonObject>() {
                                                        @Override
                                                        public void onCompleted(Exception e, JsonObject result) {
                                                            @SuppressLint("WrongConstant") SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
                                                            preferences.edit().putString(Personal.ID_TOKEN, result.get("token").getAsString()).apply();
                                                            Log.d(TAG, "" + result.toString());
                                                            Fragment fr = new RegisterActivity_2();
                                                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                                            ft.replace(R.id.register_frame, fr).commit();
                                                            next_step.setVisibility(View.GONE);
                                                            Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        } else {
                                            e.printStackTrace();
                                            Toast.makeText(getBaseContext(), "check your network status", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(getBaseContext(), "passwords did not matched", Toast.LENGTH_SHORT).show();
//                        reg1.password.setTextColor(Color.RED);
//                        reg1.password_repeated.setTextColor(Color.RED);
                    }
                    ///////////////

                } else
                    Toast.makeText(getBaseContext(), "Fill up all blanks", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
