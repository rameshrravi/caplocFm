package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maxwell.caploc.DatabaseHelper.ChecklistControler;
import com.maxwell.caploc.DatabaseHelper.ObjectChecklist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkistlevel_new extends AppCompatActivity {

    Window window;

    TextView camera;
    private static final int pic_id = 123;

    boolean check = false;
    String roomtypeid,cateID,availableQuantity,checkList,roomtype,roomno,checkListid,completed="yes",asset="",token;
    SharedPreferences.Editor editor ;
    SharedPreferences preferences;
    String userid,checkid,scan,type;
    RecyclerView recyclerView;
    CheckListLeveModel checkListLeveModel;
    List<CheckListLeveModel> checklistmodellist=new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    List<CheckListLeveModel> datamodel;
    ChecklistControler database;
    CheckListAdapter recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_checkistlevel_new);

        roomtypeid = getIntent().getStringExtra("roomtypeid");
        roomtype = getIntent().getStringExtra("room");
        roomno = getIntent().getStringExtra("roomno");
        scan = getIntent().getStringExtra("scan");
        type = getIntent().getStringExtra("type");
        cateID = getIntent().getStringExtra("cateID");

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();
        userid=preferences.getString(StringConstants.prefuserId,"");
        token=preferences.getString(StringConstants.token,"");
        recyclerView = (RecyclerView)findViewById(R.id.checklist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        System.out.println("type"+type);
        System.out.println("getid"+roomtype);System.out.println("getid"+roomno);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    finish();
                }
            }
        });
        //getLogin();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation1);
        bottomNavigationView.setSelectedItemId(R.id.home);
        getLogin();
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

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if (requestCode == pic_id) {
//            // BitMap is data structure of image file which store the image in memory
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            // Set the image in imageview for display
//            image.setImageBitmap(photo);
//        }
//    }

    public void getLogin(){
        final ProgressDialog pDialog=new ProgressDialog(Checkistlevel_new.this);
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

                        System.out.println("getid"+roomtypeid);

                        JSONArray specializationsAray=jsonObject.getJSONArray("response");
                        if (specializationsAray.length()>0) {
                            JSONObject object = specializationsAray.getJSONObject(0);
                            if(object.has("checklist_questions")){
                                JSONArray userArray=object.getJSONArray("checklist_questions");

                                checklistmodellist=new ArrayList<>();

                                boolean deleteSuccessful = new ChecklistControler(Checkistlevel_new.this).deleteallrecords();
                                for(int i=0;i<userArray.length();i++){
                                    JSONObject doctorsObject=userArray.getJSONObject(i);

                                    System.out.println("getidss"+doctorsObject.getString("checkList"));
                                    checkListLeveModel=new CheckListLeveModel();
                                    checkListLeveModel.setId(doctorsObject.getString("slno"));
                                    checkListLeveModel.setCheckList(doctorsObject.getString("checkList"));
                                    checkListLeveModel.setAvailableQuantity(doctorsObject.getString("availableQuantity"));

                                    //      checklistmodellist.add(checkListLeveModel);
                                    ObjectChecklist objectItemSetting = new ObjectChecklist();
                                    objectItemSetting.checklistid= doctorsObject.getString("id");
                                    objectItemSetting.checklist= doctorsObject.getString("checkList");
                                    objectItemSetting.availableqty= doctorsObject.getString("availableQuantity");
                                    objectItemSetting.status= "No";

                                    boolean createSuccessful = new ChecklistControler(Checkistlevel_new.this).create(objectItemSetting);
                                    if(createSuccessful)
                                    {

                                        readRecords();
                                    }

                                }
                            }
                        }


//                        mAdapter=new Checkistlevel_new.CheckListAdapter(Checkistlevel_new.this,checklistmodellist);
//                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext());
//                        recyclerView.setLayoutManager(horizontalLayoutManager1);
//                        recyclerView.setAdapter(mAdapter);
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
        })  {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "category_wise_checklist_question_details");
                MyData.put("token", token);
                MyData.put("category_id",  cateID);
                MyData.put("type",  type);

              /*  MyData.put("method", "checklist_question_details");
                MyData.put("token", token);
                MyData.put("room_type_id",  roomtypeid);
                MyData.put("type",  type);*/

                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }


    public class CheckListAdapter extends RecyclerView.Adapter<Checkistlevel_new.CheckListAdapter.ViewHolder>{

        List<CheckListLeveModel> checklistmodellist;
        Context context;



        public CheckListAdapter(Context mcontext, List<CheckListLeveModel> checklistmodellist){
            this.context=mcontext;
            this.checklistmodellist =checklistmodellist;

        }

        @NonNull
        @Override
        public Checkistlevel_new.CheckListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem= layoutInflater.inflate(R.layout.checlistevel_input, viewGroup, false);
            Checkistlevel_new.CheckListAdapter.ViewHolder viewHolder = new Checkistlevel_new.CheckListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final Checkistlevel_new.CheckListAdapter.ViewHolder viewHolder, final int i) {
            final CheckListLeveModel doctorsModel= checklistmodellist.get(i);

            viewHolder.avibaleqty_edit.setText(doctorsModel.getAvailableQuantity());
            viewHolder.actualqty.setText("Actual Quantity  "+doctorsModel.getAvailableQuantity());
            viewHolder.listroom.setText(doctorsModel.getSno() +" . "+ doctorsModel.getCheckList());
            viewHolder.yes.setBackground(getDrawable(R.drawable.button_radius1));
            String comments = viewHolder.commnets.getText().toString();
            viewHolder.avibaleqty_edit.setSelection(viewHolder.avibaleqty_edit.getText().length());

            asset = "";
            if (type.equals("Supervisor")){

                viewHolder.avaiaqty.setVisibility(View.VISIBLE);
                viewHolder.actualqty.setVisibility(View.VISIBLE);
                viewHolder.asset.setVisibility(View.VISIBLE);
                viewHolder.linear.setVisibility(View.VISIBLE);
            }else {
                viewHolder.avaiaqty.setVisibility(View.GONE);
                viewHolder.asset.setVisibility(View.GONE);
                viewHolder.actualqty.setVisibility(View.GONE);
                viewHolder.linear.setVisibility(View.GONE);
            }

            viewHolder.yes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    viewHolder.no.setBackground(getDrawable(R.drawable.button_radius));
                    viewHolder.yes.setBackground(getDrawable(R.drawable.button_radius1));
                    completed="yes";
                }
            });

            viewHolder.no.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    viewHolder.yes.setBackground(getDrawable(R.drawable.button_radius));
                    viewHolder.no.setBackground(getDrawable(R.drawable.button_radius1));

                    completed="no";

                }
            });

            viewHolder.damaged.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    viewHolder.missing.setBackground(getDrawable(R.drawable.button_radius));
                    viewHolder.damaged.setBackground(getDrawable(R.drawable.button_radius1));
                    asset="damaged";
                }
            });

            viewHolder.missing.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    viewHolder.damaged.setBackground(getDrawable(R.drawable.button_radius));
                    viewHolder.missing.setBackground(getDrawable(R.drawable.button_radius1));
                    asset = "missing";

                }
            });


            //  viewHolder.tv_name.setText(planModel.getName());
            viewHolder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type.equals("Supervisor")){


                        final ProgressDialog pDialog=new ProgressDialog(Checkistlevel_new.this);
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
                                        if(responseArray.length()>0) {
                                            JSONObject object = responseArray.getJSONObject(0);
                                            if(object.has("status")) {
                                                String status = object.getString("status");
                                                if(status.equals("success")){
                                                    ObjectChecklist objectExpenseSetting = new ObjectChecklist();
                                                    objectExpenseSetting.checklistid = doctorsModel.getId();
                                                    objectExpenseSetting.checklist = doctorsModel.getCheckList();
                                                    objectExpenseSetting.availableqty = doctorsModel.getAvailableQuantity();
                                                    objectExpenseSetting.status = "Yes";
                                                    final ChecklistControler tableControllerItem = new ChecklistControler(Checkistlevel_new.this);
                                                    boolean updateSuccessful = tableControllerItem.update(objectExpenseSetting);

                                                    if(updateSuccessful){
                                                        boolean deleteSuccessful = new ChecklistControler(Checkistlevel_new.this).delete1(Integer.parseInt(doctorsModel.getId()));
                                                        readRecords();
                                                        List<ObjectChecklist> recordsList = new ChecklistControler(Checkistlevel_new.this).read();
                                                        if (recordsList.size() ==0) {
                                                            showAlertDialogyes("Successfully inserted up");
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            showAlertDialog("Invalid");
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
                                //    getLogin();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> MyData = new HashMap<String, String>();

                                MyData.put("token", token);
                                MyData.put("method", "update_checklist_answer");
                                MyData.put("checklist_type",  type);
                                MyData.put("enterby_id",  userid);
                                MyData.put("entered_time",  currentTime);
                                MyData.put("entered_date",  currentDate);
                                MyData.put("comments", viewHolder.commnets.getText().toString());
                                MyData.put("asset_problem", asset);
                                MyData.put("completed",  completed);
                                MyData.put("available_qty",  viewHolder.avibaleqty_edit.getText().toString());
                                MyData.put("checklist_id",  doctorsModel.getId());
                                MyData.put("scantype",  scan);
                                MyData.put("room_no",roomno);
                                MyData.put("room_type",  roomtypeid);

                                System.out.println("19 "+token);
                                System.out.println("type"+type);
                                System.out.println("17 "+userid);
                                System.out.println("15 "+currentTime);
                                System.out.println("14 "+currentDate);
                                System.out.println("11 "+viewHolder.commnets.getText().toString());
                                System.out.println("13 "+completed);
                                System.out.println("17 "+viewHolder.avibaleqty_edit.getText().toString());
                                System.out.println("18 "+scan);
                                System.out.println("12 "+roomno);
                                System.out.println("112 "+roomtypeid);
                                return MyData;
                            }
                        };

                        requestQueue.add(MyStringRequest);
                    }else {

                        final ProgressDialog pDialog=new ProgressDialog(Checkistlevel_new.this);
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
                                        if(responseArray.length()>0) {
                                            JSONObject object = responseArray.getJSONObject(0);
                                            if(object.has("status")) {
                                                String status = object.getString("status");
                                                if(status.equals("success")){
                                                    ObjectChecklist objectExpenseSetting = new ObjectChecklist();
                                                    objectExpenseSetting.checklistid = doctorsModel.getId();
                                                    objectExpenseSetting.checklist = doctorsModel.getCheckList();
                                                    objectExpenseSetting.availableqty = doctorsModel.getAvailableQuantity();
                                                    objectExpenseSetting.status = "Yes";
                                                    final ChecklistControler tableControllerItem = new ChecklistControler(Checkistlevel_new.this);
                                                    boolean updateSuccessful = tableControllerItem.update(objectExpenseSetting);

                                                    if(updateSuccessful){
                                                        boolean deleteSuccessful = new ChecklistControler(Checkistlevel_new.this).delete1(Integer.parseInt(doctorsModel.getId()));
                                                        readRecords();
                                                        List<ObjectChecklist> recordsList = new ChecklistControler(Checkistlevel_new.this).read();
                                                        if (recordsList.size() ==0) {
                                                            showAlertDialogyes("Successfully inserted up");
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            showAlertDialog("Invalid");
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
                                //    getLogin();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> MyData = new HashMap<String, String>();

                                MyData.put("token", token);
                                MyData.put("method", "update_checklist_answer");
                                MyData.put("checklist_type",  type);
                                MyData.put("enterby_id",  userid);
                                MyData.put("entered_time",  currentTime);
                                MyData.put("entered_date",  currentDate);
                                MyData.put("comments", viewHolder.commnets.getText().toString());
                                MyData.put("asset_problem", "-");
                                MyData.put("completed",  completed);
                                MyData.put("available_qty",  "-");
                                MyData.put("checklist_id",  doctorsModel.getId());
                                MyData.put("scantype",  scan);
                                MyData.put("room_no",roomno);
                                MyData.put("room_type",  roomtypeid);

                                System.out.println("19 "+token);
                                System.out.println("18 "+type);
                                System.out.println("17 "+userid);
                                System.out.println("15 "+currentTime);
                                System.out.println("14 "+currentDate);
                                System.out.println("11 "+viewHolder.commnets.getText().toString());
                                System.out.println("13 "+completed);
                                System.out.println("17 "+viewHolder.avibaleqty_edit.getText().toString());
                                System.out.println("18 "+scan);
                                System.out.println("12 "+roomno);
                                System.out.println("112 "+roomtypeid);
                                return MyData;
                            }
                        };

                        requestQueue.add(MyStringRequest);
                    }



                }
            });

        }


        @Override
        public int getItemCount() {
            return checklistmodellist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView next,actualqty,listroom,camera,asset;
            Button damaged,missing,yes,no;
            CardView cardView;
            ImageView image;
            EditText avibaleqty_edit,commnets;

            LinearLayout avaiaqty,linear;

            public ViewHolder(View itemView) {
                super(itemView);

                this.avibaleqty_edit = (EditText)itemView.findViewById(R.id.avibaleqty_edit);
                this.camera = (TextView)itemView.findViewById(R.id.camerauploaded);
                this.image = (ImageView)itemView.findViewById(R.id.cam_img);
                this.yes  =(Button)itemView.findViewById(R.id.yes);
                this.no  =(Button)itemView.findViewById(R.id.no);
                this.damaged  =(Button)itemView.findViewById(R.id.damaged);
                this.missing  =(Button)itemView.findViewById(R.id.missing);
                this.actualqty = (TextView)itemView.findViewById(R.id.actualqty);
                this.listroom = (TextView)itemView.findViewById(R.id.listroom);
                this.next = (TextView)itemView.findViewById(R.id.text_finish);
                this.commnets =(EditText)itemView.findViewById(R.id.comments);
                this.commnets =(EditText)itemView.findViewById(R.id.comments);
                this.avaiaqty =(LinearLayout) itemView.findViewById(R.id.avaiqty);
                this.linear =(LinearLayout)itemView.findViewById(R.id.linear);
                this.asset =(TextView) itemView.findViewById(R.id.asset);

            }
        }

    }

    public void showAlertDialogyes(String message){

        Intent intent = new Intent(Checkistlevel_new.this,Overall.class);
        intent.putExtra("room",roomtype);
        intent.putExtra("roomno",roomno);
        intent.putExtra("roomtypeid",roomtypeid);
        startActivity(intent);


        /*final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        boolean deleteSuccessful = new ChecklistControler(Checkistlevel_new.this).deleteallrecords();

                        Intent intent = new Intent(Checkistlevel_new.this,Overall.class);
                        intent.putExtra("room",roomtype);
                        intent.putExtra("roomno",roomno);
                        intent.putExtra("roomtypeid",roomtypeid);
                        startActivity(intent);
                        dialog.dismiss();
                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
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

    @Override
    public void onBackPressed() {
        boolean deleteSuccessful = new ChecklistControler(Checkistlevel_new.this).deleteallrecords();
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
//
    public void readRecords()
    {
        datamodel =new ArrayList<CheckListLeveModel>();

        database = new ChecklistControler(Checkistlevel_new.this);

        datamodel=  database.getitem();
        recycler =new Checkistlevel_new.CheckListAdapter(Checkistlevel_new.this,datamodel);

        Log.i("HIteshdata",""+datamodel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
        //Its for divider for recycler view

        //       recyclerView.addItemDecoration(new DividerItemDecoration(Product_setting.this, 0));
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(Product_setting.this, LinearLayoutManager.VERTICAL, 0));
    }

}