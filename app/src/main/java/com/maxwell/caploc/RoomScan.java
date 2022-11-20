package com.maxwell.caploc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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


public class RoomScan extends AppCompatActivity {

    Window window;
    CardView manualscan,qrscan;
    String roomtypeid,roomtype,roomno;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String qrvalue,type,android_id,Username,Passowrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_room_scan);
        roomtypeid = getIntent().getStringExtra("roomtypeid");
        roomtype = getIntent().getStringExtra("room");
        roomno = getIntent().getStringExtra("roomno");
        type = getIntent().getStringExtra("type");

        manualscan = (CardView)findViewById(R.id.manualscan);
        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        qrvalue=preferences.getString(StringConstants.qrvalue,"");
        Username=preferences.getString(StringConstants.Adminusername,"");
        Passowrd=preferences.getString(StringConstants.Adminpassword,"");

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        manualscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(RoomScan.this,Checkistlevel_new.class);
                intent.putExtra("roomtypeid",roomtypeid);
                intent.putExtra("room",roomtype);
                intent.putExtra("roomno",roomno);
                intent.putExtra("scan","Manual");
                intent.putExtra("type",type);
                startActivity(intent);*/

                Intent intent = new Intent(RoomScan.this,CategoryListActivity.class);
                intent.putExtra("roomtypeid",roomtypeid);
                intent.putExtra("room",roomtype);
                intent.putExtra("roomno",roomno);
                intent.putExtra("scan","Manual");
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CheckList.class);
                startActivity(intent);
                finish();

            }
        });
        qrscan = (CardView)findViewById(R.id.qrscan);

        qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(RoomScan.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
//                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();
            }
        });



        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation1);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.About_Us:

                        startActivity(new Intent(getApplicationContext(),AboutUs.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:

                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        // we need to create the object
//        // of IntentIntegrator class
//        // which is the class of QR library
//        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
//        intentIntegrator.setPrompt("Scan a barcode or QR Code");
//        intentIntegrator.setOrientationLocked(true);
//        intentIntegrator.initiateScan();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }else{

                RequestQueue requestQueue = Volley.newRequestQueue(RoomScan.this);
                requestQueue.getCache().clear();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String currentDate = sdf.format(new Date());
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                final String currentTime = df.format(Calendar.getInstance().getTime());
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.AdminmainUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        Log.d("Response",response);

                        try {

                            JSONObject jsonObject=new JSONObject(response.trim());

                            if(jsonObject.has("response")){

                                JSONArray doctorsArray=jsonObject.getJSONArray("response");
                                if (doctorsArray.length()>0){
                                    JSONObject object=doctorsArray.getJSONObject(0);
                                    if(object.has("qrlist_list")){
                                        JSONArray userArray=object.getJSONArray("qrlist_list");


                                        for(int i=0;i<userArray.length();i++){
                                            JSONObject doctorsObject=userArray.getJSONObject(i);

                                            String qr_value = doctorsObject.getString("qr_value");

                                            if (intentResult.getContents().equals(qr_value)){
                                                Intent intent = new Intent(RoomScan.this,Checkistlevel_new.class);
                                                intent.putExtra("roomtypeid",roomtypeid);
                                                intent.putExtra("room",roomtype);
                                                intent.putExtra("roomno",roomno);
                                                intent.putExtra("scan","QR");
                                                intent.putExtra("type","QR");
                                                startActivity(intent);
                                                System.out.println("qrvalue"+intentResult.getContents());
                                            }else{
                                            }


                                        }
                                    }

                                }


                            }


                        } catch (JSONException e) {
                            Toast.makeText(getBaseContext(), "QRValue is Invalid", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.

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


                //    Toast.makeText(getBaseContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onBackPressed() {

                Intent intent = new Intent(getApplicationContext(),CheckList.class);
                startActivity(intent);
                finish();


    }

    public void showAlertDialog(String message){
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