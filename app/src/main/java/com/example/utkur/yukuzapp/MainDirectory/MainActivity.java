package com.example.utkur.yukuzapp.MainDirectory;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.utkur.yukuzapp.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String TAG = "Main Activity";
    @BindView(R.id.btn_test)
    Button BtnTest;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };
    private void LogIn(){
        mTextMessage.setText(R.string.title_home);
        JsonObject obj = new JsonObject();
        try {
            obj.addProperty("username", "muhammadjon");
            obj.addProperty("password", "Pa$$w0rd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri.Builder bulider = new Uri.Builder();
        bulider.appendPath("http://192.168.1.133:8000/api-gettoken/");
        Ion.with(getBaseContext())
                .load("http://192.168.1.133:8000/api-gettoken/")
                .setJsonObjectBody(obj)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d(TAG, "output " + result);
                        if (result == null) {
                            Log.d(TAG, "error: " + e);
                        }

                    }
                }).tryGetException();
    }
    private void Register(){

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
                LogIn();
            }
        });
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
