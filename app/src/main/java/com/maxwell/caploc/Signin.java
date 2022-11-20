package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity {

    Window window;
    ImageView signin;
    EditText username,password;
    String Username="",Passowrd="",android_id="",token="";
    SharedPreferences.Editor editor ;
    SharedPreferences preferences;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_signin);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        signin = (ImageView) findViewById(R.id.signin);
        username = (EditText)findViewById(R.id.edit_usernme);
        password = (EditText)findViewById(R.id.edit_password);
        forgot = (TextView) findViewById(R.id.edit_forgot);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Forgot_password.class);
                startActivity(i);
                finish();
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    Username=username.getText().toString().trim();
                    Passowrd=password.getText().toString().trim();
                    if(!Username.isEmpty()){
                        if(!Passowrd.isEmpty()){

                            getLogin();


                        }else {
                            showAlertDialog("Please enter Password");
                        }
                    }else {
                        showAlertDialog("Please enter Username");
                    }
                }
                return false;
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Username=username.getText().toString().trim();
                Passowrd=password.getText().toString().trim();
                if(!Username.isEmpty()){
                    if(!Passowrd.isEmpty()){

                        getLogin();


                    }else {
                        showAlertDialog("Please enter Password");
                    }
                }else {
                    showAlertDialog("Please enter Username");
                }
            }
        });

    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void getLogin(){

        editor.putString(StringConstants.Adminusername,Username);
        editor.putString(StringConstants.Adminpassword,Passowrd);
        editor.apply();
        editor.commit();

        final ProgressDialog pDialog=new ProgressDialog(Signin.this);
        pDialog.setMessage("Loading..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.AdminmainUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response",response);

                try {

                    JSONObject jsonObject=new JSONObject(response.trim());

                    if(jsonObject.has("response")){

                        JSONArray responseArray=jsonObject.getJSONArray("response");

                        if(responseArray.length()>0){
                            JSONObject object=responseArray.getJSONObject(0);
                            if(object.has("status")){
                                String status = object.getString("status");
                                if(status.equals("success")){

                                    editor.putString(StringConstants.token,object.getString("token"));
                                    editor.putString(StringConstants.prefuserId,object.getString("id"));
                                    editor.putString(StringConstants.prefFirstNme,object.getString("firstName"));
                                    editor.putString(StringConstants.prefUserName,object.getString("lastName"));
                                    editor.putString(StringConstants.prefAppMenu,object.getString("appmenu"));
                                    editor.putString(StringConstants.PhoneNumber,object.getString("phoneNumber"));
                                    editor.putString(StringConstants.ShiftType,object.getString("shiftType"));

                                    editor.putBoolean("isLogin",true);

                                    editor.apply();
                                    editor.commit();

                                    Intent i=new Intent(getApplicationContext(),Dashboard.class);
                                    startActivity(i);
                                    finish();
                                }else {
                                    showAlertDialog(object.getString("message"));
                                }
                            }else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage= StringConstants.ErrorMessage(error);
                if(errorMessage.equals("Connection TimeOut! Please check your internet connection."))
                    getLogin();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", Username);
                MyData.put("password", Passowrd);
                MyData.put("fcm_token", android_id);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                MyData.put("method", "login");

                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }
//    public void getFirst(){
//
//
//        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl +"userName="+Username+"&newPassword="+Passowrd, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //This code is executed if the server responds, whether or not the response contains data.
//                //The String 'response' contains the server's response.
//                Log.d("Response",response);
//
//                try {
//
//                    JSONObject jsonObject=new JSONObject(response.trim());
//
//                    if(jsonObject.has("error")){
//                        String result=jsonObject.getString("error");
//
//                        if(result.equals("false")){
//
//                            JSONArray listArray1=jsonObject.getJSONArray("2");
//                            JSONArray listArray2=jsonObject.getJSONArray("3");
//                            JSONArray listArray3=jsonObject.getJSONArray("4");
//                            if(listArray1.length()>0) {
//                                JSONObject listObject = listArray.getJSONObject(0);
//                                if (listObject.has("date")) {
//
//                                    editor.putString(StringConstants.date1,listObject.getString("date"));
//                                    System.out.println("date"+listObject.getString("date"));
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("dirty")) {
//                                    editor.putString(StringConstants.dirty1,listObject.getString("dirty"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("cleaned")) {
//                                    editor.putString(StringConstants.cleaned1,listObject.getString("cleaned"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("unattended")) {
//                                    editor.putString(StringConstants.unattened1,listObject.getString("unattended"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                            }
//                            if(listArray2.length()>0) {
//                                JSONObject listObject = listArray.getJSONObject(0);
//                                if (listObject.has("date")) {
//
//                                    editor.putString(StringConstants.date2,listObject.getString("date"));
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("dirty")) {
//                                    editor.putString(StringConstants.dirty2,listObject.getString("dirty"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("cleaned")) {
//                                    editor.putString(StringConstants.cleaned2,listObject.getString("cleaned"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("unattended")) {
//                                    editor.putString(StringConstants.unattened2,listObject.getString("unattended"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                            }
//                            if(listArray3.length()>0) {
//                                JSONObject listObject = listArray.getJSONObject(0);
//                                if (listObject.has("date")) {
//
//                                    editor.putString(StringConstants.date3,listObject.getString("date"));
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("dirty")) {
//                                    editor.putString(StringConstants.dirty3,listObject.getString("dirty"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("cleaned")) {
//                                    editor.putString(StringConstants.cleaned3,listObject.getString("cleaned"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("unattended")) {
//                                    editor.putString(StringConstants.unattened3,listObject.getString("unattended"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                            }
//                            if(listArray.length()>0) {
//                                JSONObject listObject = listArray.getJSONObject(0);
//                                if (listObject.has("id")) {
//
//                                    editor.putString(StringConstants.prefuserId,listObject.getString("id"));
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("userName")) {
//                                    editor.putString(StringConstants.prefUserName,listObject.getString("userName"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }
//                                if (listObject.has("firstName")) {
//                                    editor.putString(StringConstants.prefFirstNme,listObject.getString("firstName"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }if (listObject.has("lastName")) {
//                                    editor.putString(StringConstants.LastName,listObject.getString("lastName"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }if (listObject.has("phoneNumber")) {
//                                    editor.putString(StringConstants.PhoneNumber,listObject.getString("phoneNumber"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }if (listObject.has("shiftType")) {
//                                    editor.putString(StringConstants.ShiftType,listObject.getString("shiftType"));
//
//                                    editor.apply();
//                                    editor.commit();
//
//                                }if (listObject.has("appmenu")) {
//                                    editor.putString(StringConstants.prefAppMenu,listObject.getString("appmenu"));
//
//                                    editor.apply();
//                                    editor.commit();
//                                    Intent i=new Intent(getApplicationContext(),Dashboard.class);
//                                    startActivity(i);
//                                    finish();
//                                }
//
//                            }
//
//                        }else {
//                            showAlertDialog("Invalid username & password");
//                        }
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                pDialog.dismiss();
//
//            }
//        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //This code is executed if there is an error.
//                pDialog.dismiss();
//                String errorMessage= StringConstants.ErrorMessage(error);
//                if(errorMessage.equals("Connection TimeOut! Please check your internet connection."))
//                    getLogin();
//            }
//        }) ;
//
//        requestQueue.add(MyStringRequest);
//
//    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showAlertDialog1(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}