package com.maxwell.caploc;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class StringConstants {

    public static String mainUrl="https://fm.caploc.io/api/get_login.php?";
    public static String AdminmainUrl="https://fm.caploc.io/mapiv1/";
    public static String productsUrl="products.php?";
    public static String allProductsUrl="categoryproducts.php?";
    public static String allCategoriesUrl="categorylist.php?";
    public static String categoryUrl="category_orderlist.php?";
    public static String updatedDateUrl="updated_date.php?";
    public static String registrationUrl="customer_register.php?";
    public static String loginUrl="login.php?";
    public static String appSupportUrl="app_support.php?";
    public static String placeOrderUrl="order_details.php?";
    public static String orderListUrl="my_order.php?";
    public static String addAddressUrl="add_address.php?";
    public static String updateAddressUrl="update_address.php?";
    public static String getAddressUrl="getaddress.php?";
    public static String couponsListUrl="coupon_list.php?";
    public static String updateProfile="update_profile.php?";
    public static String offerUrl="applyoffer.php?";
    public static String notificationUrl="notification_list.php?";
    public static String giftcardUrl="giftcard.php?";
    public static String popupUrl="dailypopup.php?";
    public static String termsOfServiceUrl="terms_and_conditions.php?";
    public static String privacyUrl="privacy_policy.php?";
    public static String faqUrl="faq.php?";
    public static String forgotPasswordUrl="forget_password.php?";
    public static String customerCareNumber="customer_service_contact_details.php?";


    public static String prefMySharedPreference="BigstorePreference";
    public static String prefuserId="UserID";
    public static String Checklistid="Checklistid";
    public static String PhoneNumber="phoneNumber";
    public static String ShiftType="shiftType";
    public static String LastName="lastName";
    public static String Adminusername="Adminuser";
    public static String Adminpassword="Adminpassword";
    public static String prefUserName="UserName";
    public static String prefFirstNme="FirstName";
    public static String prefAppMenu="Appmenu";
    public static String dirty1="Dirty1";
    public static String cleaned1="cleaned1";
    public static String unattened1="unattended1";
    public static String date1="date1";
    public static String dirty2="dirty2";
    public static String cleaned2="cleaned2";
    public static String unattened2="unattended2";
    public static String date2="date2";
    public static String dirty3="dirty3";
    public static String cleaned3="cleaned3";
    public static String unattened3="unattended3";
    public static String date3="date3";
    public static String qrvalue="qr_value";
    public static String token="token";


    public static String ErrorMessage(VolleyError volleyError) {
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
//Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
//Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        return message;
    }

}
