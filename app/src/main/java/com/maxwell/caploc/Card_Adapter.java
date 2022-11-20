//package com.maxwell.caploc;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//
//public class Card_Adapter extends ArrayAdapter<Card_Adapter> {
//
//    public Card_Adapter(@NonNull Context context, ArrayList<Card_Adapter> courseModelArrayList) {
//        super(context, 0, courseModelArrayList);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        View listitemView = convertView;
//        if (listitemView == null) {
//            // Layout Inflater inflates each item to be displayed in GridView.
//            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
//        }
//
//        Card_Adapter courseModel = getItem(position);
//        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
//        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);
//
//        courseTV.setText(courseModel.getCourse_name());
//        courseIV.setImageResource(courseModel.getImgid());
//        return listitemView;
//    }
//}