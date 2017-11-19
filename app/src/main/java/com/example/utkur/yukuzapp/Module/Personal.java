package com.example.utkur.yukuzapp.Module;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Muhammadjon on 10/23/2017.
 */

public class Personal {
    public static boolean IS_DRIVER = false;
    public static String SHARED_PREF_CODE = "user";
    public static String ID_TOKEN = "token";

    public static String getToken(SharedPreferences preferences) {
        return preferences.getString(Personal.ID_TOKEN, "null");
    }

    public static class Settings {
        public static boolean ON_PUSH_NOTIFICATION = false;
        public static boolean ON_DRIVER_MODE = false;
    }
}
