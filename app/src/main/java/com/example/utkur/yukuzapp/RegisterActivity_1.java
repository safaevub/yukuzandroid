package com.example.utkur.yukuzapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegisterActivity_1 extends AppCompatActivity {
    CheckBox visible;
    EditText user_password;
    ImageButton back;
    ImageButton next_step;
    TextView terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);

        terms = (TextView) findViewById(R.id.term_conditionsXML);
        back = (ImageButton) findViewById(R.id.close_buttonXML);
        next_step = (ImageButton) findViewById(R.id.register_next_stepXML);
        visible= (CheckBox) findViewById(R.id.register_make_visibleXML);
        user_password = (EditText) findViewById(R.id.user_password_textXML);


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent term = new Intent(RegisterActivity_1.this, Term_And_ConditionActivity.class);
           startActivity(term);
            }
        });
        visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    // show password
                    user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    user_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main_int = new Intent(RegisterActivity_1.this, MainActivity.class);
                startActivity(main_int);
            }
        });
        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(RegisterActivity_1.this,RegisterActivity_2.class);
                startActivity(next);
            }
        });
    }
}
