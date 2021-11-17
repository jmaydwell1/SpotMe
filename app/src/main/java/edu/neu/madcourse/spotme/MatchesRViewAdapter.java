package edu.neu.madcourse.spotme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MatchesRViewAdapter extends RecyclerView.Adapter<MatchesRViewAdapter.MyViewHolder> {

    private List<MessageMatchModel> matchList;


    public MatchesRViewAdapter(List<MessageMatchModel>matchList) {
        this.matchList = matchList;
    }


    @NonNull
    @Override
    // this is where you inflate the layout and give the look to our rows
    public MatchesRViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.matched_r_view_row,
                parent, false);

        return new MatchesRViewAdapter.MyViewHolder(view);
    }

    @Override
    // this is where we assign values to each of our rows as they come back
    // on the screen and this is dependent on where it is on the R.view
    public void onBindViewHolder(@NonNull MatchesRViewAdapter.MyViewHolder holder, int position) {

        holder.name_of_match.setText("this is a new person :)");
        holder.date_of_match.setText("this is a new date i swear!");

        String name = matchList.get(position).getNameOfMatch();
        String date = matchList.get(position).getDateOfMatch();
        int matchPic = matchList.get(position).getMatchIcon();
        int messageButton = matchList.get(position).getMessageIcon();


        holder.setData(name, date, matchPic, messageButton);
    }

    @Override
    // how many items do you really have
    public int getItemCount() {
        return matchList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // kind of like on create
        // grabs the info from our row layout/card

        TextView name_of_match, date_of_match;
        ImageView message_icon, match_icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_of_match = itemView.findViewById(R.id.nameOfMatch);
            date_of_match = itemView.findViewById(R.id.dateOfMatch);
            message_icon = itemView.findViewById(R.id.messageIcon);
            match_icon = itemView.findViewById(R.id.matchIcon);

        }

        public void setData(String name, String date, int matchPic, int messageButton) {

            name_of_match.setText(name);
            date_of_match.setText(date);
            message_icon.setImageResource(messageButton);
            match_icon.setImageResource(matchPic);

        }
    }

}
