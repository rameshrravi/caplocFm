package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Roomtype_New extends AppCompatActivity {

    Window window;
    String id,roomtype;
    RecyclerView recycerrromtype;
    RoomTpeModel checkListModel;
    List<RoomTpeModel> checklistmodellist=new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    TextView roomtypenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_roomtype_new);

        id = getIntent().getStringExtra("roomtypeid");
        roomtype = getIntent().getStringExtra("room");
        System.out.println("getid"+id);
        recycerrromtype = (RecyclerView)findViewById(R.id.recycerroomtype);
        roomtypenew = (TextView)findViewById(R.id.roomtype);
        roomtypenew.setText(roomtype);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.back_arrow);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog("Do you want to back?");
            }
        });
        getRoomtype();

    }

    public void getRoomtype(){
        final ProgressDialog pDialog=new ProgressDialog(Roomtype_New.this);
        pDialog.setMessage("Loading..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Roomtype_New.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.AdminmainUrl+"room_number_details.php?roomTypeId="+id , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response",response);

                try {

                    JSONObject jsonObject=new JSONObject(response.trim());

                    if(jsonObject.has("room_type")){

                        JSONArray doctorsArray=jsonObject.getJSONArray("room_type");
                        checklistmodellist=new ArrayList<>();
                        for(int i=0;i<doctorsArray.length();i++){
                            JSONObject doctorsObject=doctorsArray.getJSONObject(i);
                            checkListModel=new RoomTpeModel();
                            checkListModel.setId(doctorsObject.getString("id"));
                            checkListModel.setRoomNo(doctorsObject.getString("roomNo"));

                            checklistmodellist.add(checkListModel);

                        }

                        mAdapter=new Roomtype_New.RoomAdapter(Roomtype_New.this,checklistmodellist);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                                getApplicationContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                        );
                        recycerrromtype.setLayoutManager(mLayoutManager);
                        recycerrromtype.setAdapter(mAdapter);
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
                    getRoomtype();
            }
        }) ;

        requestQueue.add(MyStringRequest);

    }


    public class RoomAdapter extends RecyclerView.Adapter<Roomtype_New.RoomAdapter.ViewHolder>{

        List<RoomTpeModel> doctorsModelList;
        Context context;



        public RoomAdapter(Context mcontext, List<RoomTpeModel> doctorsModelList){
            this.context=mcontext;
            this.doctorsModelList =doctorsModelList;

        }

        @NonNull
        @Override
        public Roomtype_New.RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem= layoutInflater.inflate(R.layout.roomtype_input, viewGroup, false);
            Roomtype_New.RoomAdapter.ViewHolder viewHolder = new Roomtype_New.RoomAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final Roomtype_New.RoomAdapter.ViewHolder viewHolder, final int i) {
            final RoomTpeModel doctorsModel= doctorsModelList.get(i);

            viewHolder.roomno.setText(doctorsModel.getRoomNo());


            //viewHolder.iv_doctor.setImageResource(doctorsModel.getImage());
            Glide.with(context)
                    .load(doctorsModel.getRoomNo())

                    .fitCenter()
                    .crossFade()
                    .dontAnimate();



            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i=new Intent(getApplicationContext(),Roomscan_new.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("roomtypeid",id);
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
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to back?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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