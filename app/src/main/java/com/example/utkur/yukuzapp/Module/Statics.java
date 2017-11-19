package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 10/14/2017.
 */

public class Statics {
    public static String serverURL = "http://192.168.1.133:8000/";
    public static String serverURL2 = "http://muhammadjon.pythonanywhere.com/";
    public static int pref_code = 0;
    public static int RESULT_LOAD_IMAGE = 1000;
    public static int RESULT_PERMISSION_LOCATION = 1001;
    public static int RESULT_PERMISSION_COARSE = 1002;

    public static class URL {
        public static String getToeknURL = serverURL + "api-gettoken/";
        public static String load_image_url = serverURL + "images/";

        public static class REST {
            public static String register = serverURL + "auth/register/";
            public static String add_person = serverURL + "auth/personlist/";
            public static String get_creds = serverURL + "auth/get_user/";
            public static String get_currency_types = serverURL + "rest/get_price_class/";
            public static String get_unpicked_orders = serverURL + "rest/posts/?id=1";
            public static String get_vehicle_type = serverURL + "rest/vtypelist/";
            public static String create_orders = serverURL + "rest/posts/";
            public static String update_order = serverURL + "rest/post_update/";
            public static String add_car = serverURL + "rest/car_view/";
            public static String cars_list = serverURL + "rest/car_view/";
        }
    }
}
