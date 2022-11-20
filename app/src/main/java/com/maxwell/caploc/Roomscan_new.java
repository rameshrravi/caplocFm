package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Roomscan_new extends AppCompatActivity {

    Window window;
    CardView qrscan,manualscan;
    String roomtypeid,roomtype,roomno;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String qrvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_roomscan_new);
        roomtypeid = getIntent().getStringExtra("roomtypeid");
        roomtype = getIntent().getStringExtra("room");
        roomno = getIntent().getStringExtra("roomno");
        System.out.println("getid"+roomtypeid);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        qrvalue=preferences.getString(StringConstants.qrvalue,"");

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CheckList.class);
                startActivity(intent);
                finish();

            }
        });

        manualscan = (CardView)findViewById(R.id.manualscan);
        manualscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Roomscan_new.this,Checkistlevel_new.class);
                intent.putExtra("roomtypeid",roomtypeid);
                intent.putExtra("room",roomtype);
                intent.putExtra("roomno",roomno);
                intent.putExtra("scan","Manual");
                startActivity(intent);
            }
        });
        qrscan = (CardView)findViewById(R.id.qrscan);
        qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(Roomscan_new.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
//                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setOrientationLocked(true);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else if(intentResult.getContents().equals(qrvalue)){
                Intent intent = new Intent(Roomscan_new.this,Checkistlevel_new.class);
                intent.putExtra("roomtypeid",roomtypeid);
                intent.putExtra("room",roomtype);
                intent.putExtra("roomno",roomno);
                intent.putExtra("scan","QR");
                startActivity(intent);
                System.out.println("qrvalue"+intentResult.getContents());
            //    Toast.makeText(getBaseContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getBaseContext(), "QR is Invalid", Toast.LENGTH_SHORT).show();

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