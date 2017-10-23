package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import com.example.utkur.yukuzapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 10/23/2017.
 */

public class PersonProfile extends AppCompatActivity {
    @BindView(R.id.tabHost)
    public TabHost tabHost;

    //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        tabHost.setup();
        getSupportActionBar().setTitle("Mukhammadjon Tokhirov");
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
    }
}
