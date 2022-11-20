package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    Window window;
    CardView roomChecklist, supervisorchecklist,damaged,linen;
    BarChart barChart;
    ImageView logout;
    // variable for our bar data set.
    BarDataSet barDataSet1, barDataSet2, barDataSet3;

    // array list for storing entries.
    ArrayList barEntries;

    // creating a string array for displaying days.

    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String user_appmenu,user_username,user_firstname,dirty1,cleaned1,unattend1,dirty2,cleaned2,unattend2,dirty3,cleaned3,unattend3,date1,date2,date3;
    TextView username,firstname;

    RelativeLayout relativeLayout,superreative;
    String Username,Passowrd,android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_dashboard);
        roomChecklist = (CardView) findViewById(R.id.room_checklist);
        linen = (CardView) findViewById(R.id.linen);
        damaged = (CardView)findViewById(R.id.damaged_checklist);
        username=(TextView)findViewById(R.id.username);
        firstname = (TextView)findViewById(R.id.firstname);

        MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);

        editor = preferences.edit();

        user_appmenu=preferences.getString(StringConstants.prefAppMenu,"");
        user_firstname=preferences.getString(StringConstants.prefFirstNme,"");
        user_username=preferences.getString(StringConstants.prefUserName,"");

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

//        date1=preferences.getString(StringConstants.date1,"");
//        date2=preferences.getString(StringConstants.date2,"");
//        date3=preferences.getString(StringConstants.date3,"");

        Username=preferences.getString(StringConstants.Adminusername,"");
        Passowrd=preferences.getString(StringConstants.Adminpassword,"");

        relativeLayout = (RelativeLayout)findViewById(R.id.relative);
        superreative = (RelativeLayout)findViewById(R.id.relative_super);
        System.out.println("getappmenu"+user_appmenu);
  //      username.setText(user_username);
        firstname.setText(user_firstname);

        if (user_appmenu.equals("Supervisor,")){
            relativeLayout.setVisibility(View.GONE);
            superreative.setVisibility(View.VISIBLE);
        }else if (user_appmenu.equals("House keeping,")){
            superreative.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }else{
            relativeLayout.setVisibility(View.VISIBLE);
            superreative.setVisibility(View.VISIBLE);
        }

        String[] days = new String[]{"date1","date2", "date3"};

        System.out.println("Appmenu"+user_appmenu);

        logout = (ImageView)findViewById(R.id.logoutt);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDailog();
            }
        });

        roomChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, CheckList.class);
                intent.putExtra("damage","no");
                intent.putExtra("type","Housekeeping");
                startActivity(intent);
            }
        });

        linen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, CheckList.class);
                intent.putExtra("damage","no");
                intent.putExtra("type","Linen");
                startActivity(intent);
            }
        });


        damaged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, CheckList.class);
                intent.putExtra("damage","damage");
                intent.putExtra("type","no");
                startActivity(intent);
            }
        });
        supervisorchecklist = (CardView) findViewById(R.id.supervisor_checklist);

        supervisorchecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, CheckList.class);
                intent.putExtra("damage","no");
                intent.putExtra("type","Supervisor");
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation1);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:

                        return true;
                    case R.id.About_Us:

                        startActivity(new Intent(getApplicationContext(), AboutUs.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:

                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });


        barChart = findViewById(R.id.idBarChart);

        // creating a new bar data set.
        barDataSet1 = new BarDataSet(getBarEntriesOne(), "Dirty");
        barDataSet1.setColor(Color.rgb(141,79,128));
        barDataSet2 = new BarDataSet(getBarEntriesTwo(), "Unattended");
        barDataSet2.setColor(Color.rgb(254,167,176));
        barDataSet3 = new BarDataSet(getBarEntriesThree(), "Cleaned");
        barDataSet3.setColor(Color.rgb(12,200,31));
//        TooltipCompat.setTooltipText(barChart, "one");
        // below line is to add bar data set to our bar data.
        BarData data = new BarData(barDataSet1, barDataSet2, barDataSet3);

        // after adding data to our bar data we
        // are setting that data to our bar chart.
        barChart.setData(data);

        // below line is to remove description
        // label of our bar chart.
        barChart.getDescription().setEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = barChart.getXAxis();

        // below line is to set value formatter to our x-axis and
        // we are adding our days to our x axis.
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        // below line is to set center axis
        // labels to our bar chart.
        xAxis.setCenterAxisLabels(true);

        // below line is to set position
        // to our x-axis to bottom.
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // below line is to set granularity
        // to our x axis labels.
        xAxis.setGranularity(1);

        // below line is to enable
        // granularity to our x axis.
        xAxis.setGranularityEnabled(true);

        // below line is to make our
        // bar chart as draggable.
        barChart.setDragEnabled(true);

        // below line is to make visible
        // range for our bar chart.
        barChart.setVisibleXRangeMaximum(3);

        // below line is to add bar
        // space to our chart.
        float barSpace = 0.1f;

        // below line is use to add group
        // spacing to our bar chart.
        float groupSpace = 0.5f;

        // we are setting width of
        // bar in below line.
        data.setBarWidth(0.15f);

        data.setValueFormatter(formatter);
        // below line is to set minimum
        // axis to our chart.
        barChart.getXAxis().setAxisMinimum(0);

        // below line is to
        // animate our chart.
        barChart.animate();

        // below line is to group bars
        // and add spacing to it.
        barChart.groupBars(0, groupSpace, barSpace);

        // below line is to invalidate
        // our bar chart.
        barChart.invalidate();
        CustomMarkerView mv = new CustomMarkerView(Dashboard.this, R.layout.custom_marker);
        barChart.setMarkerView(mv);


    }

    public void logoutDailog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Dashboard.this);

        builder.setMessage("Do you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editor.remove(StringConstants.prefuserId);
                        editor.remove(StringConstants.prefUserName);
                        editor.remove(StringConstants.prefAppMenu);

                        editor.remove("isLogin");
                        editor.apply();
                        editor.commit();
                        Intent intent = new Intent(Dashboard.this, Signin.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public void getLogin(){

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        requestQueue.getCache().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());


        requestQueue.getCache().clear();
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
                            if(object.has("date_wise_list")){
                                JSONArray userArray=object.getJSONArray("date_wise_list");


                                for(int i=0;i<userArray.length();i++){
                                    JSONObject doctorsObject=userArray.getJSONObject(i);

                                    String cleaned = doctorsObject.getString("cleaned");



                                }
                            }

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
             //   pDialog.dismiss();

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
       //         pDialog.dismiss();
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
    // array list for first set
    private ArrayList<BarEntry> getBarEntriesOne() {

        // creating a new array list
        barEntries = new ArrayList<>();


        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        requestQueue.getCache().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());


        requestQueue.getCache().clear();
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
                            if(object.has("date_wise_list")){
                                JSONArray userArray=object.getJSONArray("date_wise_list");


                                for(int i=0;i<userArray.length();i++){
                                    JSONObject doctorsObject=userArray.getJSONObject(i);

                                    String cleaned = doctorsObject.getString("cleaned");
                                    System.out.println("ceaned"+cleaned);

                                    barEntries.add(new BarEntry(1, Integer.parseInt(cleaned)));
                                    barEntries.add(new BarEntry(2, Integer.parseInt(cleaned)));
                                    barEntries.add(new BarEntry(3, Integer.parseInt(cleaned)));
                                }
                            }

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //   pDialog.dismiss();

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                //         pDialog.dismiss();
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

        return barEntries;
    }

    // array list for second set.
    private ArrayList<BarEntry> getBarEntriesTwo() {
//        unattend1=preferences.getString(StringConstants.unattened1,"");
//        unattend2=preferences.getString(StringConstants.unattened2,"");
//        unattend3=preferences.getString(StringConstants.unattened3,"");
        // creating a new array list
        barEntries = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntries.add(new BarEntry(1, 1));
        barEntries.add(new BarEntry(2, 2));
        barEntries.add(new BarEntry(3, 3));

        return barEntries;
    }

    private ArrayList<BarEntry> getBarEntriesThree() {
//        cleaned1=preferences.getString(StringConstants.cleaned1,"");
//        cleaned2=preferences.getString(StringConstants.cleaned2,"");
//        cleaned3=preferences.getString(StringConstants.cleaned3,"");
        // creating a new array list
        barEntries = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntries.add(new BarEntry(1, 2));
        barEntries.add(new BarEntry(2, 3));
        barEntries.add(new BarEntry(3, 4));

        return barEntries;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}