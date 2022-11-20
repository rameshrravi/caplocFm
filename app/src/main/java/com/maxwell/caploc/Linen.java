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
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Linen extends AppCompatActivity {

    EditText sentbox,pickbox,sentbox1,pickbox1,sentbox2,pickbox2,sentbox3,pickbox3,sentbox4,pickbox4,sentbox5,pickbox5,sentbox6,pickbox6,sentbox7,pickbox7,sentbox8,pickbox8,sentbox9,pickbox9,sentbox10,pickbox10,sentbox11,pickbox11,sentbox12,pickbox12,sentbox13,pickbox13;
    TextView submit;

    String  Sentbox,Pickbox,Sentbox1,Pickbox1,Sentbox2,Pickbox2,Sentbox3,Pickbox3,Sentbox4,Pickbox4,Sentbox5,Pickbox5,Sentbox6,Pickbox6,Sentbox7,Pickbox7,Sentbox8,Pickbox8,Sentbox9,Pickbox9,Sentbox10,Pickbox10,Sentbox11,Pickbox11,Sentbox12,Pickbox12,Sentbox13,Pickbox13;
    SharedPreferences.Editor editor ;
    SharedPreferences preferences;
    String token,roomtypeid,roomno,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linen);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();
        token=preferences.getString(StringConstants.token,"");
        userid=preferences.getString(StringConstants.prefuserId,"");

        roomtypeid = getIntent().getStringExtra("roomtypeid");

        roomno = getIntent().getStringExtra("roomno");


        sentbox  =(EditText)findViewById(R.id.sentbox);
        pickbox = (EditText)findViewById(R.id.pickbox);
        sentbox1  =(EditText)findViewById(R.id.avibaleqty_edit1);
        pickbox1 = (EditText)findViewById(R.id.pickbox1);
        sentbox2  =(EditText)findViewById(R.id.avibaleqty_edit2);
        pickbox2 = (EditText)findViewById(R.id.pickbox2);
        sentbox3  =(EditText)findViewById(R.id.avibaleqty_edit3);
        pickbox3 = (EditText)findViewById(R.id.pickbox3);
        sentbox4  =(EditText)findViewById(R.id.avibaleqty_edit4);
        pickbox4 = (EditText)findViewById(R.id.pickbox4);
   //     sentbox5  =(EditText)findViewById(R.id.avibaleqty_edit5);
   //     pickbox5 = (EditText)findViewById(R.id.pickbox5);
        sentbox6  =(EditText)findViewById(R.id.avibaleqty_edit6);
        pickbox6 = (EditText)findViewById(R.id.pickbox6);
        sentbox7  =(EditText)findViewById(R.id.avibaleqty_edit7);
        pickbox7 = (EditText)findViewById(R.id.pickbox7);
        sentbox8  =(EditText)findViewById(R.id.avibaleqty_edit8);
        pickbox8 = (EditText)findViewById(R.id.pickbox8);
        sentbox9  =(EditText)findViewById(R.id.avibaleqty_edit9);
        pickbox9 = (EditText)findViewById(R.id.pickbox9);
        sentbox10  =(EditText)findViewById(R.id.avibaleqty_edit10);
        pickbox10 = (EditText)findViewById(R.id.pickbox10);
        sentbox11  =(EditText)findViewById(R.id.avibaleqty_edit11);
        pickbox11 = (EditText)findViewById(R.id.pickbox11);
        sentbox12  =(EditText)findViewById(R.id.avibaleqty_edit12);
        pickbox12 = (EditText)findViewById(R.id.pickbox12);
        sentbox13  =(EditText)findViewById(R.id.avibaleqty_edit13);
        pickbox13 = (EditText)findViewById(R.id.pickbox13);

        submit = (TextView)findViewById(R.id.text_submit);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();
            }
        });







//
//            if(!Pickbox.isEmpty()){
//                if(!Sentbox1.isEmpty()){
//                    if(!Pickbox1.isEmpty()){
//                        if(!Sentbox2.isEmpty()){
//                            if(!Pickbox2.isEmpty()){
//                                if(!Sentbox3.isEmpty()){
//                                    if(!Pickbox3.isEmpty()){
//                                        if(!Sentbox4.isEmpty()){
//                                            if(!Pickbox4.isEmpty()){
//                                                if(!Sentbox6.isEmpty()){
//                                                    if(!Pickbox6.isEmpty()){
//                                                        if(!Sentbox7.isEmpty()){
//                                                            if(!Pickbox7.isEmpty()){
//                                                                if(!Sentbox8.isEmpty()){
//                                                                    if(!Pickbox8.isEmpty()){
//                                                                        if(!Sentbox9.isEmpty()){
//                                                                            if(!Pickbox9.isEmpty()){
//                                                                                if(!Sentbox10.isEmpty()){
//                                                                                    if(!Pickbox10.isEmpty()){
//                                                                                        if(!Sentbox11.isEmpty()){
//                                                                                            if(!Pickbox11.isEmpty()){
//                                                                                                if(!Sentbox12.isEmpty()){
//                                                                                                    if(!Pickbox12.isEmpty()){
//                                                                                                        if(!Sentbox13.isEmpty()){
//                                                                                                            if(!Pickbox13.isEmpty()){
//
//
//                                                                                                            }else {
//                                                                                                                showAlertDialog("Please enter Queen Bed Spread Pickup");
//                                                                                                            }
//                                                                                                        }else {
//                                                                                                            showAlertDialog("Please enter Queen Bed Spread sent");
//                                                                                                        }
//                                                                                                    }else {
//                                                                                                        showAlertDialog("Please enter Crib Sheet Pickup");
//                                                                                                    }
//                                                                                                }else {
//                                                                                                    showAlertDialog("Please enter Crib Sheet sent");
//                                                                                                }
//                                                                                            }else {
//                                                                                                showAlertDialog("Please enter Single Bed Spread Pickup");
//                                                                                            }
//                                                                                        }else {
//                                                                                            showAlertDialog("Please enter Single Bed Spread sent");
//                                                                                        }
//                                                                                    }else {
//                                                                                        showAlertDialog("Please enter Double Bed Spread Pickup");
//                                                                                    }
//                                                                                }else {
//                                                                                    showAlertDialog("Please enter Double Bed Spread sent");
//                                                                                }
//                                                                            }else {
//                                                                                showAlertDialog("Please enter King Blanket Pickup");
//                                                                            }
//                                                                        }else {
//                                                                            showAlertDialog("Please enter King Blanket sent");
//                                                                        }
//                                                                    }else {
//                                                                        showAlertDialog("Please enter Double Blanket Pickup");
//                                                                    }
//                                                                }else {
//                                                                    showAlertDialog("Please enter Double Blanket sent");
//                                                                }
//                                                            }else {
//                                                                showAlertDialog("Please enter King Pillow Pickup");
//                                                            }
//                                                        }else {
//                                                            showAlertDialog("Please enter King Pillow sent");
//                                                        }
//                                                    }else {
//                                                        showAlertDialog("Please enter Double Pillow Pickup");
//                                                    }
//                                                }else {
//                                                    showAlertDialog("Please enter Double Pillow sent");
//                                                }
//
//                                            }else {
//                                                showAlertDialog("Please enter Pillow Case Pickup");
//                                            }
//                                        }else {
//                                            showAlertDialog("Please enter Pillow Case sent");
//                                        }
//                                    }else {
//                                        showAlertDialog("Please enter Bath Maths Pickup");
//                                    }
//                                }else {
//                                    showAlertDialog("Please enter Bath Maths sent");
//                                }
//                            }else {
//                                showAlertDialog("Please enter Wash Cloths Pickup");
//                            }
//                        }else {
//                            showAlertDialog("Please enter Wash Cloths sent");
//                        }
//                    }else {
//                        showAlertDialog("Please enter Hand Towels Pickup");
//                    }
//                }else {
//                    showAlertDialog("Please enter Hand Towels sent");
//                }
//            }else {
//                showAlertDialog("Please enter Bath Towels Pickup");
//            }
//        }else {
//            showAlertDialog("Please enter Bath Towels sent");
//        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sentbox=sentbox.getText().toString().trim();
                Pickbox=pickbox.getText().toString().trim();
                Sentbox1=sentbox1.getText().toString().trim();
                Pickbox1=pickbox1.getText().toString().trim();
                Sentbox2=sentbox2.getText().toString().trim();
                Pickbox2=pickbox2.getText().toString().trim();
                Sentbox3=sentbox3.getText().toString().trim();
                Pickbox3=pickbox3.getText().toString().trim();
                Sentbox4=sentbox4.getText().toString().trim();
                Pickbox4=pickbox4.getText().toString().trim();
//                Sentbox5=sentbox5.getText().toString().trim();
                //              Pickbox5=pickbox5.getText().toString().trim();
                Sentbox6=sentbox6.getText().toString().trim();
                Pickbox6=pickbox6.getText().toString().trim();
                Sentbox7=sentbox7.getText().toString().trim();
                Pickbox7=pickbox7.getText().toString().trim();
                Sentbox8=sentbox8.getText().toString().trim();
                Pickbox8=pickbox8.getText().toString().trim();
                Sentbox9=sentbox9.getText().toString().trim();
                Pickbox9=pickbox9.getText().toString().trim();
                Sentbox10=sentbox10.getText().toString().trim();
                Pickbox10=pickbox10.getText().toString().trim();
                Sentbox11=sentbox11.getText().toString().trim();
                Pickbox11=pickbox11.getText().toString().trim();
                Sentbox12=sentbox12.getText().toString().trim();
                Pickbox12=pickbox12.getText().toString().trim();
                Sentbox13=sentbox13.getText().toString().trim();
                Pickbox13=pickbox13.getText().toString().trim();

                if(Sentbox.isEmpty()) {
                    Sentbox = "0";
                }else{
                    Sentbox=sentbox.getText().toString().trim();
                }

                if(Pickbox.isEmpty()) {
                    Pickbox = "0";
                }else {
                    Pickbox=pickbox.getText().toString().trim();
                }

                if(Sentbox1.isEmpty()) {
                    Sentbox1 = "0";
                }else {
                    Sentbox1=sentbox1.getText().toString().trim();
                }

                if(Pickbox1.isEmpty()) {
                    Pickbox1 = "0";
                }else {
                    Pickbox1=pickbox1.getText().toString().trim();
                }

                if(Sentbox2.isEmpty()) {
                    Sentbox2 = "0";
                }else{
                    Sentbox2=sentbox2.getText().toString().trim();
                }

                if(Pickbox2.isEmpty()) {
                    Pickbox2 = "0";
                }else{
                    Pickbox2=pickbox2.getText().toString().trim();
                }

                if(Sentbox3.isEmpty()) {
                    Sentbox3 = "0";
                }else{
                    Sentbox3=sentbox3.getText().toString().trim();
                }

                if(Pickbox3.isEmpty()) {
                    Pickbox3 = "0";
                }else{
                    Pickbox3=pickbox3.getText().toString().trim();
                }


                if(Sentbox4.isEmpty()) {
                    Sentbox4 = "0";
                }else {
                    Sentbox4=sentbox4.getText().toString().trim();
                }


                if(Pickbox4.isEmpty()) {
                    Pickbox4 = "0";
                }else {
                    Pickbox4=pickbox4.getText().toString().trim();
                }

                if(Sentbox6.isEmpty()) {
                    Sentbox6 = "0";
                }else{
                    Sentbox6=sentbox6.getText().toString().trim();
                }
                if(Pickbox6.isEmpty()) {
                    Pickbox6 = "0";
                }else{
                    Pickbox6=pickbox6.getText().toString().trim();
                }

                if(Sentbox7.isEmpty()) {
                    Sentbox7 = "0";
                }else{
                    Sentbox7=sentbox7.getText().toString().trim();
                }

                if(Pickbox7.isEmpty()) {
                    Pickbox7 = "0";
                }else {
                    Pickbox7=pickbox7.getText().toString().trim();
                }

                if(Sentbox8.isEmpty()) {
                    Sentbox8 = "0";
                }else{
                    Sentbox8=sentbox8.getText().toString().trim();
                }

                if(Pickbox8.isEmpty()) {
                    Pickbox8 = "0";
                }else{
                    Pickbox8=pickbox8.getText().toString().trim();
                }

                if(Sentbox9.isEmpty()) {
                    Sentbox9 = "0";
                }else {
                    Sentbox9=sentbox9.getText().toString().trim();
                }
                if(Pickbox9.isEmpty()) {
                    Pickbox9 = "0";
                }else{
                    Pickbox9=pickbox9.getText().toString().trim();
                }
                if(Sentbox10.isEmpty()) {
                    Sentbox10 = "0";
                }else{
                    Sentbox10=sentbox10.getText().toString().trim();
                }
                if(Pickbox10.isEmpty()) {
                    Pickbox10 = "0";
                }else{
                    Pickbox10=pickbox10.getText().toString().trim();
                }
                if(Sentbox11.isEmpty()) {
                    Sentbox11 = "0";
                }else{
                    Sentbox11=sentbox11.getText().toString().trim();
                }

                if(Pickbox11.isEmpty()) {
                    Pickbox11 = "0";
                }
                else{
                    Pickbox11=pickbox11.getText().toString().trim();
                }

                if(Sentbox12.isEmpty()) {
                    Sentbox12 = "0";
                }else {
                    Sentbox12=sentbox12.getText().toString().trim();
                }

                if(Pickbox12.isEmpty()) {
                    Pickbox12 = "0";
                }else{
                    Pickbox12=pickbox12.getText().toString().trim();
                }

                if(Sentbox13.isEmpty()) {
                    Sentbox13 = "0";
                }else{
                    Sentbox13=sentbox13.getText().toString().trim();
                }

                if(Pickbox13.isEmpty()) {
                    Pickbox13 = "0";
                }else {
                    Pickbox13=pickbox13.getText().toString().trim();
                }


                Submit();

//                if(!Sentbox.isEmpty()){
//                    Sentbox = "0";
//                    if(!Pickbox.isEmpty()){
//                        if(!Sentbox1.isEmpty()){
//                            if(!Pickbox1.isEmpty()){
//                                if(!Sentbox2.isEmpty()){
//                                    if(!Pickbox2.isEmpty()){
//                                        if(!Sentbox3.isEmpty()){
//                                            if(!Pickbox3.isEmpty()){
//                                                if(!Sentbox4.isEmpty()){
//                                                    if(!Pickbox4.isEmpty()){
//                                                                if(!Sentbox6.isEmpty()){
//                                                                    if(!Pickbox6.isEmpty()){
//                                                                        if(!Sentbox7.isEmpty()){
//                                                                            if(!Pickbox7.isEmpty()){
//                                                                                if(!Sentbox8.isEmpty()){
//                                                                                    if(!Pickbox8.isEmpty()){
//                                                                                        if(!Sentbox9.isEmpty()){
//                                                                                            if(!Pickbox9.isEmpty()){
//                                                                                                if(!Sentbox10.isEmpty()){
//                                                                                                    if(!Pickbox10.isEmpty()){
//                                                                                                        if(!Sentbox11.isEmpty()){
//                                                                                                            if(!Pickbox11.isEmpty()){
//                                                                                                                if(!Sentbox12.isEmpty()){
//                                                                                                                    if(!Pickbox12.isEmpty()){
//                                                                                                                        if(!Sentbox13.isEmpty()){
//                                                                                                                            if(!Pickbox13.isEmpty()){
//
//
//                                                                                                                            }else {
//                                                                                                                                showAlertDialog("Please enter Queen Bed Spread Pickup");
//                                                                                                                            }
//                                                                                                                        }else {
//                                                                                                                            showAlertDialog("Please enter Queen Bed Spread sent");
//                                                                                                                        }
//                                                                                                                    }else {
//                                                                                                                        showAlertDialog("Please enter Crib Sheet Pickup");
//                                                                                                                    }
//                                                                                                                }else {
//                                                                                                                    showAlertDialog("Please enter Crib Sheet sent");
//                                                                                                                }
//                                                                                                            }else {
//                                                                                                                showAlertDialog("Please enter Single Bed Spread Pickup");
//                                                                                                            }
//                                                                                                        }else {
//                                                                                                            showAlertDialog("Please enter Single Bed Spread sent");
//                                                                                                        }
//                                                                                                    }else {
//                                                                                                        showAlertDialog("Please enter Double Bed Spread Pickup");
//                                                                                                    }
//                                                                                                }else {
//                                                                                                    showAlertDialog("Please enter Double Bed Spread sent");
//                                                                                                }
//                                                                                            }else {
//                                                                                                showAlertDialog("Please enter King Blanket Pickup");
//                                                                                            }
//                                                                                        }else {
//                                                                                            showAlertDialog("Please enter King Blanket sent");
//                                                                                        }
//                                                                                    }else {
//                                                                                        showAlertDialog("Please enter Double Blanket Pickup");
//                                                                                    }
//                                                                                }else {
//                                                                                    showAlertDialog("Please enter Double Blanket sent");
//                                                                                }
//                                                                            }else {
//                                                                                showAlertDialog("Please enter King Pillow Pickup");
//                                                                            }
//                                                                        }else {
//                                                                            showAlertDialog("Please enter King Pillow sent");
//                                                                        }
//                                                                    }else {
//                                                                        showAlertDialog("Please enter Double Pillow Pickup");
//                                                                    }
//                                                                }else {
//                                                                    showAlertDialog("Please enter Double Pillow sent");
//                                                                }
//
//                                                    }else {
//                                                        showAlertDialog("Please enter Pillow Case Pickup");
//                                                    }
//                                                }else {
//                                                    showAlertDialog("Please enter Pillow Case sent");
//                                                }
//                                            }else {
//                                                showAlertDialog("Please enter Bath Maths Pickup");
//                                            }
//                                        }else {
//                                            showAlertDialog("Please enter Bath Maths sent");
//                                        }
//                                    }else {
//                                        showAlertDialog("Please enter Wash Cloths Pickup");
//                                    }
//                                }else {
//                                    showAlertDialog("Please enter Wash Cloths sent");
//                                }
//                            }else {
//                                showAlertDialog("Please enter Hand Towels Pickup");
//                            }
//                        }else {
//                            showAlertDialog("Please enter Hand Towels sent");
//                        }
//                    }else {
//                        showAlertDialog("Please enter Bath Towels Pickup");
//                    }
//                }else {
//                    showAlertDialog("Please enter Bath Towels sent");
//                }
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation1);

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

    public void showAlertDialogIntent(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), CheckList.class);
                        i.putExtra("damage","no");
                        i.putExtra("type","Linen");
                        startActivity(i);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void Submit(){



        final ProgressDialog pDialog=new ProgressDialog(Linen.this);
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

                        if(responseArray.length()>0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if(object.has("status")) {
                                String status = object.getString("status");
                                if(status.equals("success")){
                                    showAlertDialogIntent("Submit Successful");

                                }else {
                                    showAlertDialog("Something went wrong");
                                }
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
                if(errorMessage.equals("Connection TimeOut! Please check your internet connection."));


            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("method", "update_linen_sent_pickup_details");
                MyData.put("token", token);
                MyData.put("room_type_id", roomtypeid);
                MyData.put("room_id", roomno);
                MyData.put("bath_towels_sent", Sentbox);
                MyData.put("hand_towels_sent", Sentbox1);
                MyData.put("wash_cloths_sent", Sentbox2);
                MyData.put("bath_maths_sent", Sentbox3);
                MyData.put("pillow_case_sent", Sentbox4);
                MyData.put("entered_by", userid);
                MyData.put("double_pillow_sent", Sentbox6);
                MyData.put("king_pillow_sent", Sentbox7);
                MyData.put("double_blanket_sent", Sentbox8);
                MyData.put("king_blanket_sent", Sentbox9);
                MyData.put("double_bed_spread_sent", Sentbox10);
                MyData.put("single_bed_spread_sent", Sentbox11);

                MyData.put("crib_sheet_sent", Sentbox12);
                MyData.put("queen_bed_spread_sent", Sentbox13);
                MyData.put("bath_towels_picked_up", Pickbox);
                MyData.put("hand_towels_picked_up", Pickbox1);
                MyData.put("wash_cloths_picked_up", Pickbox2);
                MyData.put("bath_maths_picked_up", Pickbox3);
                MyData.put("pillow_case_picked_up", Pickbox4);
                MyData.put("queen_bed_spread_picked_up", Pickbox13);
                MyData.put("double_pillow_picked_up", Pickbox6);
                MyData.put("king_pillow_picked_up", Pickbox7);
                MyData.put("double_blanket_picked_up", Pickbox8);
                MyData.put("king_blanket_picked_up", Pickbox9);

                MyData.put("double_bed_spread_picked_up", Pickbox10);
                MyData.put("single_bed_spread_picked_up", Pickbox11);
                MyData.put("crib_sheet_picked_up", Pickbox12);
                MyData.put("entered_by_date", currentDate);
                MyData.put("enteredby_time", currentTime);

                System.out.println("pickbox1 "+Sentbox);
                System.out.println("pickbox2 "+Sentbox1);
                System.out.println("pickbox3 "+Pickbox1);
                System.out.println("pickbox4 "+Pickbox2);
                System.out.println("pickbox5 "+Sentbox3);
                System.out.println("pickbox6 "+Pickbox3);
                System.out.println("pickbox7 "+Pickbox4);
                System.out.println("pickbox8 "+Sentbox4);


                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),CheckList.class);
        intent.putExtra("damage","no");
        intent.putExtra("type","Linen");
        startActivity(intent);
        finish();
    }


}