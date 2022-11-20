package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maxwell.caploc.DatabaseHelper.ChecklistControler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Damageditem_Image extends AppCompatActivity {

    DamagedimgeModel damagedimgeModel;
    List<DamagedimgeModel> damagedimgeModelList=new ArrayList<>();
    private RecyclerView.Adapter mAdapter2;
    RecyclerView recyclerViewImage;
    String roomtypeid;
    Window window;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 101;
    String Category,assetid,roomnoid,token;
    SharedPreferences.Editor editor ;
    SharedPreferences preferences;
    private String pictureImagePath = "";
    final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_damageditem_image);

        roomtypeid = getIntent().getStringExtra("roomtypeid");
        roomnoid = getIntent().getStringExtra("roomno");
        System.out.println("roomid"+roomtypeid);
        recyclerViewImage=(RecyclerView)findViewById(R.id.recyclerimage);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();
        token=preferences.getString(StringConstants.token,"");
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        getImageview();

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
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
//    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_CAMERA_PERMISSION_CODE)
//        {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 101);
//            }
//            else
//            {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Uri u = data.getData();
//            Intent selfiSrc = new Intent(this, Damageditem_Submit.class);
//            selfiSrc.putExtra("img", photo);
//            selfiSrc.putExtra("cat", Category);
//            selfiSrc.putExtra("assetid", assetid);
//            selfiSrc.putExtra("roomnoid", roomnoid);
//            startActivity(selfiSrc);
////            imageView.setImageBitmap(photo);
//        }
//    }
//

    public void getImageview(){
        final ProgressDialog pDialog=new ProgressDialog(Damageditem_Image.this);
        pDialog.setMessage("Loading..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String   currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());
        RequestQueue requestQueue = Volley.newRequestQueue(Damageditem_Image.this);
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

                        JSONArray specializationsAray=jsonObject.getJSONArray("response");
                        if (specializationsAray.length()>0){
                            JSONObject object=specializationsAray.getJSONObject(0);
                            if(object.has("asset_list")){
                                JSONArray userArray=object.getJSONArray("asset_list");

                                damagedimgeModelList=new ArrayList<>();
                                for(int i=0;i<userArray.length();i++){
                                    damagedimgeModel=new DamagedimgeModel();
                                    JSONObject specializationObject=userArray.getJSONObject(i);

                                    damagedimgeModel.setId(specializationObject.getString("id"));
                                    damagedimgeModel.setName(specializationObject.getString("asset_Name"));
                                    damagedimgeModel.setImageUrl(specializationObject.getString("image"));

                                    damagedimgeModelList.add(damagedimgeModel);

                                }
                            }

                        }
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                                getApplicationContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                        );

                        mAdapter2=new DamageditemAdapter(Damageditem_Image.this,damagedimgeModelList);
                        recyclerViewImage.setLayoutManager(mLayoutManager);
                        recyclerViewImage.setAdapter(mAdapter2);

                    }

                    pDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage=StringConstants.ErrorMessage(error);
                if(errorMessage.equals("Connection TimeOut! Please check your internet connection."))
                    getImageview();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("token", token);
                MyData.put("method", "asset_details_by_roomtype");
                MyData.put("room_type_id", roomtypeid);


                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public class DamageditemAdapter extends RecyclerView.Adapter<DamageditemAdapter.ViewHolder>{

        List<DamagedimgeModel> specializationModelList;
        Context context;



        public DamageditemAdapter(Context mcontext, List<DamagedimgeModel> specializationModelList){
            this.context=mcontext;
            this.specializationModelList =specializationModelList;

        }

        @NonNull
        @Override
        public DamageditemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem= layoutInflater.inflate(R.layout.layout_damgeditemimg_row, viewGroup, false);
            DamageditemAdapter.ViewHolder viewHolder = new DamageditemAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final DamageditemAdapter.ViewHolder viewHolder, final int i) {
            final DamagedimgeModel doctorsModel= specializationModelList.get(i);

            viewHolder.tv_name.setText(doctorsModel.getName());
            viewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category = doctorsModel.getName();
                    assetid  = doctorsModel.getId();
                    cameraIntent();
                }
            });
            viewHolder.iv_doctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category = doctorsModel.getName();
                    assetid  = doctorsModel.getId();
                    cameraIntent();
                }
            });

            //  viewHolder.iv_doctor.setImageResource(doctorsModel.getImage());
            Glide.with(context)
                    .load(doctorsModel.getImageUrl())
                    .thumbnail(Glide.with(context).load(R.drawable.load))
                    .fitCenter()
                    .crossFade()
                    .dontAnimate()
                    .into(viewHolder.iv_doctor);

//            viewHolder.layoutContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i=new Intent(context,Damageditem_Image.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("Specialization",doctorsModel.getName());
//                    i.putExtra("SpecializationID",doctorsModel.getId());
//                    context.startActivity(i);
//                }
//            });

            //  viewHolder.tv_name.setText(planModel.getName());


        }

        public void showAlertDialog(String message){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        public int getItemCount() {
            return specializationModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_name,tv_service;
            ImageView iv_doctor;
            LinearLayout layoutContainer;


            public ViewHolder(View itemView) {
                super(itemView);
                this.tv_name = (TextView) itemView.findViewById(R.id.text_name);

                this.iv_doctor=(ImageView)itemView.findViewById(R.id.image);

                this.layoutContainer = (LinearLayout) itemView.findViewById(R.id.layout_container);



            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cameraIntent()
    {
        if(checkPermission()){
            openBackCamera();
        }else {
            requestPermission();
        }
    }
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(Damageditem_Image.this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(
                Damageditem_Image.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(
                Damageditem_Image.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }
    private void openBackCamera() {

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        String pictureFile = "ENFORCE_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pictureImagePath = image.getAbsolutePath();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            //  startActivityForResult(cameraIntent, 1);
            if (image != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        image);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 1);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(
                new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
        // requestPermissions( new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0 && (grantResults[0]
                        + grantResults[1]
                        + grantResults[2]
                        == PackageManager.PERMISSION_GRANTED
                )) {
                    //  Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    openBackCamera();
                    // main logic
                } else {
                    //   Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(
                                Damageditem_Image.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(
                                Damageditem_Image.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermission();

                        }
                    }
                }
                break;

            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK) {

            if (requestCode == 1) {
                File imgFile = new  File(pictureImagePath);

                if(imgFile.exists()){

                    Uri selectedImageUri = Uri.fromFile(imgFile);

                    Intent selfiSrc = new Intent(this, Damageditem_Submit.class);
                    selfiSrc.putExtra("img", selectedImageUri);
                    selfiSrc.putExtra("cat", Category);
                    selfiSrc.putExtra("assetid", assetid);
                    selfiSrc.putExtra("roomnoid", roomnoid);
                    selfiSrc.putExtra("roomtypeid", roomtypeid);
                    startActivity(selfiSrc);


                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
        startActivity(intent);
        finish();
    }

}