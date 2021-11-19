package edu.neu.madcourse.spotme;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapter_sw extends RecyclerView.Adapter<recyclerViewAdapter_sw.MyViewHolder> {
    Context context;
    ArrayList<userModel> userModels;

    public recyclerViewAdapter_sw(Context context, ArrayList<userModel> userModels){
        this.context = context;
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public recyclerViewAdapter_sw.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row_sw, parent, false);
        return new recyclerViewAdapter_sw.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapter_sw.MyViewHolder holder, int position) {
        holder.textView.setText(userModels.get(position).getFullName());
        holder.imageView.setImageResource(userModels.get(position).getUserImgInt());

    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.userImg);
            textView = itemView.findViewById(R.id.fullName);

        }
    }
}

