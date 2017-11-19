package com.example.utkur.yukuzapp.MainDirectory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.EntranceActivity;
import com.example.utkur.yukuzapp.External.DoAction;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson.HomeForPerson;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson.PersonProfile;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
//import com.facebook.FacebookSdk;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String TAG = "Main Activity";
    //    @BindView(R.id.btn_test)
//    Button BtnTest;
//    @BindView(R.id.btn_test2)
//    Button BtnTest2;
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
//                        getSupportActionBar().setTitle(R.string.title_delivery);
//                        fr = new Deliveries();
//                        ft = getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.working_fragment_location, fr).commit();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportActionBar().setTitle(R.string.title_notifications);
                        return true;
                }
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
            case R.id.mainmenu_log_out:
                @SuppressLint("WrongConstant") SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
                preferences.edit().remove(Personal.ID_TOKEN).apply();
                Toast.makeText(getBaseContext(), "Logged out", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, EntranceActivity.class);
                startActivity(intent);
                finish();
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
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        if (DoAction.isNetworkAvailable(getBaseContext())) {
            SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
            String token = preferences.getString(Personal.ID_TOKEN, "null");
            if (token.equals("null")) {
                Intent intent = new Intent(MainActivity.this, EntranceActivity.class);
                startActivity(intent);
                finish();
            }
            if (!Personal.IS_DRIVER) {
                ButterKnife.bind(this);
//                BtnTest.setOnClickListener(new View.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.M)
//                    @Override
//                    public void onClick(View view) {
//                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//                Log.d(TAG, "Refreshed token: " + refreshedToken);
//                Intent i = new Intent(getBaseContext(), RecyclerViewTest.class);
//                startActivity(i);
//                        Log.d(TAG, "Refreshed token: ");
//                        Intent i = new Intent(getApplicationContext(), AddressSelectorActivity.class);
//                        startActivity(i);
//
//                    }
//
//                });
                mTextMessage = (TextView) findViewById(R.id.message);
                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setSelectedItemId(R.id.navigation_home);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//                BtnTest2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                    }
//                });
            } else {
                Toast.makeText(this, "check your network connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
