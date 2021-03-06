package com.example.utkur.yukuzapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Authentication.LoginActivity;
import com.example.utkur.yukuzapp.Authentication.Registration.RegisterActivity;

public class EntranceActivity extends AppCompatActivity {
    Button login_btn;
    TextView reg_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        login_btn = (Button) findViewById(R.id.login_with_acc_buttonXML);
        reg_text = (TextView) findViewById(R.id.register_text);

        reg_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_int = new Intent(EntranceActivity.this, RegisterActivity.class);
                startActivity(reg_int);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_int = new Intent(EntranceActivity.this, LoginActivity.class);
                startActivity(log_int);
            }
        });

    }
}
