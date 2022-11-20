package com.maxwell.caploc;

import static com.maxwell.caploc.StringConstants.token;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checklist_new extends AppCompatActivity {


    Window window;
    //   GridLayout Queenroom;
    RecyclerView listofroom;
    SharedPreferences.Editor editor ;
    SharedPreferences preferences;
    CheckListModel checkListModel;
    List<CheckListModel> checklistmodellist=new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    RoomTpeModel checkListModel1;
    List<RoomTpeModel> checklistmodellist1=new ArrayList<>();
    private RecyclerView.Adapter mAdapter1;
    String roomtypeid,roomtype,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_checklist_new);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();
        token=preferences.getString(StringConstants.token,"");
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog("Do you want to back?");
            }
        });

        listofroom = (RecyclerView)findViewById(R.id.listofrooms);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation1);
        bottomNavigationView.setSelectedItemId(R.id.home);

        getDoctorsByLocation();
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
    public void getDoctorsByLocation(){
        final ProgressDialog pDialog=new ProgressDialog(Checklist_new.this);
        pDialog.setMessage("Loading..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Checklist_new.this);
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
                            if(object.has("room_list")){
                                JSONArray userArray=object.getJSONArray("room_list");

                                checklistmodellist=new ArrayList<>();
                                for(int i=0;i<userArray.length();i++){
                                    JSONObject doctorsObject=userArray.getJSONObject(i);
                                    checkListModel=new CheckListModel();
                                    checkListModel.setId(doctorsObject.getString("id"));
                                    checkListModel.setRoom(doctorsObject.getString("room"));

                                    checklistmodellist.add(checkListModel);

                                }
                            }

                        }

                        mAdapter=new Checklist_new.DoctorsAdapter1(Checklist_new.this,checklistmodellist);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                        listofroom.setLayoutManager(horizontalLayoutManager1);
                        listofroom.setAdapter(mAdapter);
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
                    getDoctorsByLocation();
            }
        })   {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "room_type_list");
                MyData.put("token", token);


                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public class DoctorsAdapter1 extends RecyclerView.Adapter<Checklist_new.DoctorsAdapter1.ViewHolder>{

        List<CheckListModel> doctorsModelList;
        Context context;



        public DoctorsAdapter1(Context mcontext, List<CheckListModel> doctorsModelList){
            this.context=mcontext;
            this.doctorsModelList =doctorsModelList;

        }

        @NonNull
        @Override
        public Checklist_new.DoctorsAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem= layoutInflater.inflate(R.layout.listofroom_input, viewGroup, false);
            Checklist_new.DoctorsAdapter1.ViewHolder viewHolder = new Checklist_new.DoctorsAdapter1.ViewHolder(listItem);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final Checklist_new.DoctorsAdapter1.ViewHolder viewHolder, final int i) {
            final CheckListModel doctorsModel= doctorsModelList.get(i);

            viewHolder.queenroom.setText(doctorsModel.getRoom());


            viewHolder.recyclerView.setVisibility(View.GONE);
            viewHolder.queenroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    roomtypeid  = doctorsModel.getId();
                    roomtype = doctorsModel.getRoom();
                    int visible = viewHolder.recyclerView.getVisibility();
                    if (visible==View.VISIBLE){
                        viewHolder.recyclerView.setVisibility(View.GONE);
                    }else {
                        viewHolder.recyclerView.setVisibility(View.VISIBLE);
                    }

                }
            });


            RequestQueue requestQueue = Volley.newRequestQueue(Checklist_new.this);
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
                            if (doctorsArray.length()>0) {
                                JSONObject object = doctorsArray.getJSONObject(0);
                                if (object.has("room_list")) {
                                    JSONArray userArray = object.getJSONArray("room_list");
                                    checklistmodellist1=new ArrayList<>();
                                    //       ArrayList<RoomTpeModel> courseModelArrayList = new ArrayList<RoomTpeModel>();
                                    for(int i=0;i<userArray.length();i++){
                                        JSONObject doctorsObject=userArray.getJSONObject(i);
                                        checkListModel1=new RoomTpeModel();
                                        checkListModel1.setId(doctorsObject.getString("id"));
                                        checkListModel1.setRoomNo(doctorsObject.getString("roomNo"));
                                        checkListModel1.setColour(doctorsObject.getString("color"));

                                        checklistmodellist1.add(checkListModel1);

                                    }
                                }
                            }

                            mAdapter1=new Checklist_new.RoomAdapter(Checklist_new.this,checklistmodellist1);
                            //                 LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                            StaggeredGridLayoutManager staggeredGridLayoutManager= new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);

                            viewHolder.recyclerView.setLayoutManager(staggeredGridLayoutManager);
                            viewHolder.recyclerView.setAdapter(mAdapter1);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.

                    String errorMessage=StringConstants.ErrorMessage(error);
                    if(errorMessage.equals("Connection TimeOut! Please check your internet connection."))
                        getDoctorsByLocation();
                }
            })  {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("method", "room_number_list");
                    MyData.put("token", token);
                    MyData.put("room_type_id",  doctorsModel.getId());


                    return MyData;
                }
            };

            requestQueue.add(MyStringRequest);
//            viewHolder.queenroom.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent i=new Intent(getApplicationContext(),Roomtype_New.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("roomtypeid",doctorsModel.getId());
//                    i.putExtra("room",doctorsModel.getRoom());
//
//                    startActivity(i);
//                }
//            });

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
            return doctorsModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView queenroom;
            LinearLayout layoutContainer;
            RecyclerView recyclerView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.queenroom = (TextView) itemView.findViewById(R.id.queenroom);
                this.recyclerView = (RecyclerView) itemView.findViewById(R.id.recycerroomtype);



            }
        }

    }
    public class RoomAdapter extends RecyclerView.Adapter<Checklist_new.RoomAdapter.ViewHolder>{

        List<RoomTpeModel> doctorsModelList;
        Context context;



        public RoomAdapter(Context mcontext, List<RoomTpeModel> doctorsModelList){
            this.context=mcontext;
            this.doctorsModelList =doctorsModelList;

        }

        @NonNull
        @Override
        public Checklist_new.RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem= layoutInflater.inflate(R.layout.roomtype_input, viewGroup, false);
            Checklist_new.RoomAdapter.ViewHolder viewHolder = new Checklist_new.RoomAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final Checklist_new.RoomAdapter.ViewHolder viewHolder, final int i) {
            final RoomTpeModel doctorsModel= doctorsModelList.get(i);

            viewHolder.roomno.setText(doctorsModel.getRoomNo());

            if (doctorsModel.getColour().equals("Red")){
                viewHolder.cardView.setBackgroundColor(Color.RED);

            }else if(doctorsModel.getColour().equals("Green")){
                viewHolder.cardView.setBackgroundColor(Color.GREEN);
            }else{
                viewHolder.cardView.setBackgroundColor(Color.rgb(225,115,29));
            }

            //viewHolder.iv_doctor.setImageResource(doctorsModel.getImage());
            Glide.with(context)
                    .load(doctorsModel.getRoomNo())

                    .fitCenter()
                    .crossFade()
                    .dontAnimate();



            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //        Intent i=new Intent(getApplicationContext(),RoomScan.class);
                    Intent i=new Intent(getApplicationContext(),Roomscan_new.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("roomtypeid",roomtypeid);
                    i.putExtra("room",roomtype);
                    i.putExtra("roomno",doctorsModel.getId());
                    startActivity(i);
                }
            });
            //  viewHolder.tv_name.setText(planModel.getName());


        }


        @Override
        public int getItemCount() {
            return doctorsModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView roomno,tv_service;
            CardView cardView;

            LinearLayout layoutContainer;

            public ViewHolder(View itemView) {
                super(itemView);
                this.roomno = (TextView) itemView.findViewById(R.id.roomno);
                this.cardView = (CardView) itemView.findViewById(R.id.cardview);


            }
        }

    }

    public class CourseGVAdapter extends ArrayAdapter<RoomTpeModel> {

        public CourseGVAdapter(@NonNull Context context, ArrayList<RoomTpeModel> courseModelArrayList) {
            super(context, 0, courseModelArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View listitemView = convertView;
            if (listitemView == null) {
                // Layout Inflater inflates each item to be displayed in GridView.
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.roomtype_input, parent, false);
            }

            RoomTpeModel courseModel = getItem(position);
            TextView courseTV = listitemView.findViewById(R.id.roomno);
            CardView cardview = listitemView.findViewById(R.id.cardview);

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent i=new Intent(getApplicationContext(),Roomscan_new.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("roomtypeid",roomtypeid);
                        i.putExtra("room",roomtype);
                        i.putExtra("roomno",courseModel.getId());
                        startActivity(i);


                    //        Intent i=new Intent(getApplicationContext(),RoomScan.class);

                }
            });


            if (courseModel.getColour().equals("Red")){
                cardview.setBackgroundColor(Color.RED);

            }else if(courseModel.getColour().equals("Green")){
                cardview.setBackgroundColor(Color.GREEN);
            }else{
                cardview.setBackgroundColor(Color.rgb(225,115,29));
            }
            courseTV.setText(courseModel.getRoomNo());

            return listitemView;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
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