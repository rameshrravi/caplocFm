package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Overall extends AppCompatActivity {

    Button dirty,cleaned,unattend,vaccant,turnover,stayover;
    String roomlevel,status,roomtype,roomno,id;
    TextView textsubmit;
    SharedPreferences.Editor editor ;
    SharedPreferences preferences;
    String userid,token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall);

        dirty  =(Button)findViewById(R.id.dirty);
        cleaned  =(Button)findViewById(R.id.cleaned);
        unattend  =(Button)findViewById(R.id.unattended);
        vaccant  =(Button)findViewById(R.id.Vaccant);
        turnover  =(Button)findViewById(R.id.turnover);
        stayover  =(Button)findViewById(R.id.stayover);
        textsubmit = (TextView) findViewById(R.id.text_submit);

        roomtype = getIntent().getStringExtra("room");
        roomno = getIntent().getStringExtra("roomno");
        id = getIntent().getStringExtra("roomtypeid");


        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();
        token=preferences.getString(StringConstants.token,"");
        userid=preferences.getString(StringConstants.prefuserId,"");
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        textsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getnext();
            }
        });
        dirty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cleaned.setBackground(getDrawable(R.drawable.button_radius));
                unattend.setBackground(getDrawable(R.drawable.button_radius));
                dirty.setBackground(getDrawable(R.drawable.button_radius1));
                roomlevel ="Dirty";
            }
        });

        cleaned.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                cleaned.setBackground(getDrawable(R.drawable.button_radius1));
                unattend.setBackground(getDrawable(R.drawable.button_radius));
                dirty.setBackground(getDrawable(R.drawable.button_radius));
                roomlevel ="cleaned";

            }
        });
        unattend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                unattend.setBackground(getDrawable(R.drawable.button_radius1));
                cleaned.setBackground(getDrawable(R.drawable.button_radius));
                dirty.setBackground(getDrawable(R.drawable.button_radius));
                roomlevel ="unattend";
            }
        });

        vaccant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stayover.setBackground(getDrawable(R.drawable.button_radius));
                turnover.setBackground(getDrawable(R.drawable.button_radius));
                vaccant.setBackground(getDrawable(R.drawable.button_radius1));
                status ="vaccant";
            }
        });

        turnover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                turnover.setBackground(getDrawable(R.drawable.button_radius1));
                vaccant.setBackground(getDrawable(R.drawable.button_radius));
                stayover.setBackground(getDrawable(R.drawable.button_radius));
                status ="turnover";

            }
        });
        stayover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stayover.setBackground(getDrawable(R.drawable.button_radius1));
                vaccant.setBackground(getDrawable(R.drawable.button_radius));
                turnover.setBackground(getDrawable(R.drawable.button_radius));
                status ="stayover";
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

    public void getnext(){
        final ProgressDialog pDialog=new ProgressDialog(Overall.this);
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

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.AdminmainUrl, new Response.Listener<String>() {
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
                                    showAlertDialogyes("Successfully inserted");
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
                    getnext();
            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("token", token);
                MyData.put("method", "update_overall_room_status");
                MyData.put("room_no",roomno);
                MyData.put("room_type",  id);
                MyData.put("enterby_id",  userid);
                MyData.put("entered_time",  currentTime);
                MyData.put("entered_date",  currentDate);
                MyData.put("room_level", roomlevel);
                MyData.put("room_status",  status);

                System.out.println("19 "+token);
                System.out.println("17 "+userid);
                System.out.println("15 "+currentTime);
                System.out.println("14 "+currentDate);
                System.out.println("11 "+id);
                System.out.println("13 "+status);
                System.out.println("12 "+roomno);
                System.out.println("112 "+roomlevel);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

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

    public void showAlertDialogyes(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        Intent intent = new Intent(Overall.this,Dashboard.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
        startActivity(intent);
        finish();
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