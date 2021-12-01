package edu.neu.madcourse.spotme;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.spotme.database.models.Match;
import edu.neu.madcourse.spotme.database.models.PotentialMatch;
import io.grpc.Context;

public class MatchesRViewAdapter extends FirestoreRecyclerAdapter<Match, MatchesRViewAdapter.MyViewHolder> {

    private List<MessageMatchModel> matchList;
    private FirebaseStorage storage;
    private StorageReference profilePictureStorage;


    //constructor
    public MatchesRViewAdapter(@NonNull FirestoreRecyclerOptions<Match> options) {
        super(options);
        storage = FirebaseStorage.getInstance();
    }

    @Override
    // this is where you inflate the layout and give the look to our rows
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.matched_r_view_row, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    // this is where we assign values to each of our rows as they come back
    // on the screen and this is dependent on where it is on the R.view
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Match model) {

//        holder.name_of_match.setText("this is a new person :)");
//        holder.date_of_match.setText("this is a new date i swear!");

//        String name = matchList.get(position).getNameOfMatch();
//        String date = matchList.get(position).getDateOfMatch();
//        int matchPic = matchList.get(position).getMatchIcon();
//        int messageButton = matchList.get(position).getMessageIcon();


//        holder.setData(name, date, matchPic, messageButton);
        Log.d("!!!NAME: ", model.getName());
//        updateHolderData(holder, model.getName());
        holder.name_of_match.setText(model.getName());
//        holder.setData(model.getName(), model.getDate());
    }

//    @Override
//    // how many items do you really have
//    public int getItemCount() {
//        return matchList.size();
//    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        // kind of like on create
        // grabs the info from our row layout/card

        TextView name_of_match;
//        TextView date_of_match;
//        ImageView message_icon, match_icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_of_match = itemView.findViewById(R.id.nameOfMatch);
//            date_of_match = itemView.findViewById(R.id.dateOfMatch);
//            message_icon = itemView.findViewById(R.id.messageIcon);
//            match_icon = itemView.findViewById(R.id.matchIcon);

        }

//        public void setData(String name, String date) {
//
//            name_of_match.setText(name);
//            date_of_match.setText(date);
////            message_icon.setImageResource(messageButton);
////            match_icon.setImageResource(matchPic);
//
//        }
    }

//    public void updateHolderData(MyViewHolder holder, String name) {
//
//        holder.name_of_match.setText(name);
////        date_of_match.setText(date);
////            message_icon.setImageResource(messageButton);
////            match_icon.setImageResource(matchPic);
//
//    }

}
