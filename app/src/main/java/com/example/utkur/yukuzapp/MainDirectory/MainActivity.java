package com.example.utkur.yukuzapp.MainDirectory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.HomeForDriver;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson.HomeForPerson;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson.PersonProfile;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.google.gson.JsonObject;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String TAG = "Main Activity";
    @BindView(R.id.btn_test)
    Button BtnTest;
    @BindView(R.id.working_fragment_location)
    FrameLayout main_fragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (!Personal.IS_DRIVER) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        getSupportActionBar().setTitle("Person");
                        Fragment fr = new HomeForPerson();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.working_fragment_location, fr).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        getSupportActionBar().setTitle(R.string.title_dashboard);
                        return true;
                    case R.id.navigation_notifications:
                        getSupportActionBar().setTitle(R.string.title_notifications);
                        return true;
                }
            } else
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        getSupportActionBar().setTitle("Driver");
                        Fragment fr = new HomeForDriver();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.working_fragment_location, fr).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        getSupportActionBar().setTitle(R.string.title_dashboard);
                        return true;
                    case R.id.navigation_notifications:
                        getSupportActionBar().setTitle(R.string.title_notifications);
                        return true;
                }
            return false;

        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainmenu_profile:
                Intent intent = new Intent(this, PersonProfile.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//                Log.d(TAG, "Refreshed token: " + refreshedToken);
            }
        });
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initialize();
    }

    public String getCredentials() {
        String json = "";
        return json;
    }

    public void initialize() {
        getSupportActionBar().setTitle((!Personal.IS_DRIVER) ? "Person" : "Driver");
        Fragment fr = null;
        if (!Personal.IS_DRIVER)
            fr = new HomeForPerson();
        else
            fr = new HomeForDriver();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.working_fragment_location, fr).commit();

    }
}
