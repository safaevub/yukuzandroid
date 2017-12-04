package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 10/14/2017.
 */

public class Statics {
    //    public static String serverURL1 = "http://165.246.221.212:8000/";
    public static String serverURL2 = "http://192.168.1.133:8000/";
    public static String serverURL = "http://muhammadjon.pythonanywhere.com/";
    public static int pref_code = 0;
    public static int RESULT_LOAD_IMAGE = 1000;
    public static int RESULT_PERMISSION_LOCATION = 1001;
    public static int RESULT_PERMISSION_COARSE = 1002;

    public static class URL {
        public static String getToeknURL = serverURL + "api-gettoken/";
        public static String load_image_url = serverURL + "images/";
        public static String base_url = "http://muhammadjon.pythonanywhere.com";

        public static class REST {
            public static String register = serverURL + "auth/register/";
            public static String add_person = serverURL + "auth/personlist/";
            public static String get_creds = serverURL + "auth/get_user/";
            public static String get_currency_types = serverURL + "rest/get_price_class/";
            public static String get_order = serverURL + "rest/posts/?id=1";
            public static String get_unpicked_orders = serverURL + "rest/posts/?id=1";
            public static String get_unpicked_driver_related_orders = serverURL + "rest/posts/?id=2";
            public static String get_picked_driver_related_orders = serverURL + "rest/posts/?id=3";
            public static String get_vehicle_type = serverURL + "rest/vtypelist/";
            public static String create_orders = serverURL + "rest/posts/";
            public static String update_order = serverURL + "rest/post_update/";
            public static String add_car = serverURL + "rest/car_view/";
            public static String delete_car = serverURL + "rest/car_view/";
            public static String cars_list = serverURL + "rest/car_view/";
            public static String driver_get = serverURL + "auth/driver_create/";
            public static String driver_create = serverURL + "auth/driver_create/";
            public static String driver_active_toggle = serverURL + "auth/driver_create/";
            public static String initialize = serverURL + "rest/initialize/";
            public static String create_device = serverURL + "firebase/devices/";
            public static String pick_order = serverURL + "rest/picked_orders/";
            public static String picked_orders_for_person = serverURL + "rest/picked_orders/?id=2";
            public static String accept_request_of_a_driver = serverURL + "rest/accept_request/";
        }
    }
}
