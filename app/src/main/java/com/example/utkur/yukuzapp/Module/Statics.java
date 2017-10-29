package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 10/14/2017.
 */

public class Statics {
    public static String serverURL = "http://192.168.1.133:8000/";
    public static int pref_code = 1000;
    public static int RESULT_LOAD_IMAGE = 1000;

    public static class URL {
        public static String getToeknURL = serverURL + "api-gettoken/";
        public static String load_image_url = serverURL + "images/";
        public static class REST {
            public static String register = serverURL + "rest/register/";
            public static String add_person = serverURL + "rest/personlist/";
            public static String get_creds = serverURL + "rest/get_user/";
        }
    }
}
