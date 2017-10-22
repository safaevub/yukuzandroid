package com.example.utkur.yukuzapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RegisterActivity_2 extends AppCompatActivity {
    ImageButton close;
    ImageButton next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pg_2);

        close = (ImageButton) findViewById(R.id.number_close_buttonXML);
        next = (ImageButton) findViewById(R.id.number_next_stepXML);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nx = new Intent(RegisterActivity_2.this, MainActivity.class);
                startActivity(nx);
                finish();
            }
        });
    }
}
