package com.example.utkur.yukuzapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Utkur on 10/6/2017.
 */

public class LoginActivity extends AppCompatActivity {
    CheckBox vis;
    ImageButton bc;
    EditText password;
    TextView forgotpass;

        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
            bc = (ImageButton) findViewById(R.id.back_button);
            vis = (CheckBox) findViewById(R.id.login_make_visibleXML);
            password = (EditText) findViewById(R.id.login_passwordXMl);
            forgotpass = (TextView) findViewById(R.id.forgot_passwordXML);

            forgotpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent fg = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(fg);
                }
            });
            bc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                                        finish();
                }
            });
            vis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // checkbox status is changed from un_check to checked.
                    if (!isChecked) {
                        // show password
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        // hide password
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                }
            });


    }
}
