package com.example.utkur.yukuzapp.Authentication.Registration;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity_1 extends Fragment implements RegCommunicator {

    @BindView(R.id.user_email_textXML)
    EditText email;

    @BindView(R.id.user_first_textXML)
    EditText first_name;

    @BindView(R.id.user_last_textXML)
    EditText last_name;

    @BindView(R.id.user_password_textXML)
    EditText password;

    @BindView(R.id.user_password_textXML1)
    EditText password_repeated;

    @BindView(R.id.register_make_visibleXML)
    CheckBox visible;
    private String TAG = "Registration page 1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_pg_1, container, false);
        ButterKnife.bind(this, view);
        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible.isChecked()) {
                    // show password
                    password.setTransformationMethod(null);
                    password_repeated.setTransformationMethod(null);

                } else {
                    // hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_repeated.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        password_repeated.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!password.getText().toString().equals(password_repeated.getText().toString())) {
                    password.setTextColor(Color.RED);
                    password_repeated.setTextColor(Color.RED);
                } else {
                    password.setTextColor(Color.BLACK);
                    password_repeated.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }


    @Override
    public boolean onSubmit() {
        return !(email.getText().toString().equals("") || first_name.
                getText().toString().equals("") || last_name.getText().toString().equals("")
                || password.getText().toString().equals("") || password_repeated.getText().toString().equals(""));

    }
}
