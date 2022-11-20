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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import java.util.List;

public class CheckListLevel extends AppCompatActivity {

    Window window;
    TextView next,camera,listroom;
    private static final int pic_id = 123;
    ImageView image;
    Button yes,no,damaged,missing;
    String roomtypeid,availableQuantity,checkList,roomtype,roomno,checkListid,completed="yes",asset="";
    EditText commnets;
    SharedPreferences.Editor editor ;
    SharedPreferences preferences;
    String userid,scan;

    RecyclerView recyclerView;
    CheckListLeveModel checkListLeveModel;
    List<CheckListLeveModel> checklistmodellist=new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    List<CheckListLeveModel> datamodel;
    ChecklistControler database;
    CheckListLevel.CheckListAdapter recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_check_list_level);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        userid=preferences.getString(StringConstants.prefuserId,"");

        roomtypeid = getIntent().getStringExtra("roomtypeid");
        roomtype = getIntent().getStringExtra("room");
        roomno = getIntent().getStringExtra("roomno");
        scan = getIntent().getStringExtra("scan");

        System.out.println("roomid"+roomtypeid);
        recyclerView = (RecyclerView)findViewById(R.id.checklist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getLogin();


        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog1("Do you want to back?");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            image.setImageBitmap(photo);
        }
    }

    public void getLogin(){
        final ProgressDialog pDialog=new ProgressDialog(CheckListLevel.this);
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

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.AdminmainUrl+"checklist_questions_view.php?type=Housekeeping&roomtypeID="+roomtypeid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response",response);

                try {

                    JSONObject jsonObject=new JSONObject(response.trim());

                    if(jsonObject.has("checklist_questions")){

                        JSONArray doctorsArray=jsonObject.getJSONArray("checklist_questions");
                        boolean deleteSuccessful = new ChecklistControler(CheckListLevel.this).deleteallrecords();
                        checklistmodellist=new ArrayList<>();
                        for(int i=0;i<doctorsArray.length();i++){
                            JSONObject doctorsObject=doctorsArray.getJSONObject(i);

                            System.out.println("getidss"+doctorsObject.getString("checkList"));
                            checkListLeveModel=new CheckListLeveModel();
                            checkListLeveModel.setId(doctorsObject.getString("s.no"));
                            checkListLeveModel.setCheckList(doctorsObject.getString("checkList"));
                            checkListLeveModel.setAvailableQuantity(doctorsObject.getString("availableQuantity"));

                            ObjectChecklist objectItemSetting = new ObjectChecklist();
                            objectItemSetting.checklistid= doctorsObject.getString("s.no");
                            objectItemSetting.checklist= doctorsObject.getString("checkList");
                            objectItemSetting.availableqty= doctorsObject.getString("availableQuantity");
                            objectItemSetting.status= "No";

                            boolean createSuccessful = new ChecklistControler(CheckListLevel.this).create(objectItemSetting);
                            if(createSuccessful)
                            {

                                readRecords();
                            }

                        }

//                        mAdapter=new CheckListLevel.CheckListAdapter(CheckListLevel.this,checklistmodellist);
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
        }) ;

        requestQueue.add(MyStringRequest);

    }

//    public void getnext(){
//        final ProgressDialog pDialog=new ProgressDialog(CheckListLevel.this);
//        pDialog.setMessage("Loading..");
//        pDialog.setCancelable(false);
//        pDialog.setTitle("");
//        pDialog.show();
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.getCache().clear();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        final String currentDate = sdf.format(new Date());
//        DateFormat df = new SimpleDateFormat("HH:mm:ss");
//        final String currentTime = df.format(Calendar.getInstance().getTime());
//
//        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, "https://demo9.maxwellserver.com/Clients/caplocfm/api/get_checklist_details.php?room_type="+roomtypeid+"&room_no="+roomno+"&scantype=ManualScan"+"&checklist_id="+checkListid+"&available_qty=0"+"&completed="+completed+"&asset_problem="+asset+"&comments="+commnets.getText().toString()+"&entered_date="+currentDate+"&entered_time="+currentTime+"&enterby_id="+userid, new Response.Listener<String>() {
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
//                            showAlertDialogyes("Successfully inserted up");
//
//                        }else {
//                            showAlertDialog("Invalid");
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
//                if(errorMessage.equals("Connection TimeOut! Please check your internet connection."));
//
//            }
//        }) ;
//
//        requestQueue.add(MyStringRequest);
//
//    }


    public class CheckListAdapter extends RecyclerView.Adapter<CheckListLevel.CheckListAdapter.ViewHolder>{

        List<CheckListLeveModel> checklistmodellist;
        Context context;



        public CheckListAdapter(Context mcontext, List<CheckListLeveModel> checklistmodellist){
            this.context=mcontext;
            this.checklistmodellist =checklistmodellist;

        }

        @NonNull
        @Override
        public CheckListLevel.CheckListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem= layoutInflater.inflate(R.layout.checlistevel_input, viewGroup, false);
            CheckListLevel.CheckListAdapter.ViewHolder viewHolder = new CheckListLevel.CheckListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final CheckListLevel.CheckListAdapter.ViewHolder viewHolder, final int i) {
            final CheckListLeveModel doctorsModel= checklistmodellist.get(i);

//            viewHolder.avibaleqty_edit.setText(doctorsModel.getAvailableQuantity());
        //    viewHolder.availableqty.setText("Actual Quantity - "+doctorsModel.getAvailableQuantity());
            viewHolder.listroom.setText(doctorsModel.getId()+" . "+doctorsModel.getCheckList());
            viewHolder.yes.setBackground(getDrawable(R.drawable.button_radius1));
            String comments = viewHolder.commnets.getText().toString();
            asset = "";


            viewHolder.avibaleqty_edit.setVisibility(View.GONE);
            viewHolder.actualqty.setVisibility(View.GONE);
            viewHolder.availableqty.setVisibility(View.GONE);

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

                    final ProgressDialog pDialog=new ProgressDialog(CheckListLevel.this);
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

                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.AdminmainUrl+"get_checklist_details.php?room_type="+roomtypeid+"&room_no="+roomno+"&scantype="+scan+"&checklist_id="+doctorsModel.getId()+"&checklist_type=Housekeeping"+"&available_qty=0"+"&completed="+completed+"&asset_problem="+asset+"&comments="+viewHolder.commnets.getText().toString()+"&entered_date="+currentDate+"&entered_time="+currentTime+"&enterby_id="+userid, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.
                            Log.d("Response",response);

                            try {

                                JSONObject jsonObject=new JSONObject(response.trim());

                                if(jsonObject.has("error")){
                                    String result=jsonObject.getString("error");

                                    if(result.equals("false")){
                                        ObjectChecklist objectExpenseSetting = new ObjectChecklist();
                                        objectExpenseSetting.checklistid = doctorsModel.getId();
                                        objectExpenseSetting.checklist = doctorsModel.getCheckList();
                                        objectExpenseSetting.availableqty = doctorsModel.getAvailableQuantity();
                                        objectExpenseSetting.status = "Yes";
                                        final ChecklistControler tableControllerItem = new ChecklistControler(CheckListLevel.this);
                                        boolean updateSuccessful = tableControllerItem.update(objectExpenseSetting);

                                        if(updateSuccessful){
                                            boolean deleteSuccessful = new ChecklistControler(CheckListLevel.this).delete1(Integer.parseInt(doctorsModel.getId()));
                                            readRecords();
                                            List<ObjectChecklist> recordsList = new ChecklistControler(CheckListLevel.this).read();
                                            if (recordsList.size() ==0) {
                                                showAlertDialogyes("Successfully inserted up");
                                            }

                                        }
                                    }else {
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
                    }) ;

                    requestQueue.add(MyStringRequest);


                }
            });

        }


        @Override
        public int getItemCount() {
            return checklistmodellist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView next,actualqty,listroom,camera,availableqty;
            Button damaged,missing,yes,no;
            CardView cardView;
            ImageView image;
            EditText avibaleqty_edit,commnets;

            LinearLayout layoutContainer;

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
                this.availableqty =(TextView)itemView.findViewById(R.id.availableqty);



            }
        }

    }
    public void showAlertDialogyes(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        boolean deleteSuccessful = new ChecklistControler(CheckListLevel.this).deleteallrecords();
                        Intent intent = new Intent(CheckListLevel.this,Overall.class);
                        intent.putExtra("room",roomtype);
                        intent.putExtra("roomno",roomno);
                        intent.putExtra("roomtypeid",roomtypeid);
                        startActivity(intent);
                        dialog.dismiss();
                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to back?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean deleteSuccessful = new ChecklistControler(CheckListLevel.this).deleteallrecords();
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();
                //  dialog.dismiss();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    public void showAlertDialog1(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        boolean deleteSuccessful = new ChecklistControler(CheckListLevel.this).deleteallrecords();
                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void readRecords()
    {
        datamodel =new ArrayList<CheckListLeveModel>();

        database = new ChecklistControler(CheckListLevel.this);

        datamodel=  database.getitem();
        recycler =new CheckListLevel.CheckListAdapter(CheckListLevel.this,datamodel);

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