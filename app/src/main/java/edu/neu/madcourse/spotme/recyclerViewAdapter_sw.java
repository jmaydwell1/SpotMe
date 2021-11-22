package edu.neu.madcourse.spotme;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class recyclerViewAdapter_sw extends FirestoreRecyclerAdapter<PotentialMatch, recyclerViewAdapter_sw.MyViewHolder> {
//    Context context;
//    ArrayList<userModel> userModels;
    Dialog dialogTest;

    public recyclerViewAdapter_sw(@NonNull FirestoreRecyclerOptions<PotentialMatch> options){
        super(options);
//        this.context = context;
//        this.userModels = userModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_row_sw, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        dialogTest = new Dialog(parent.getContext());
        dialogTest.setContentView(R.layout.potential_buddy_dialog);
        dialogTest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewHolder.potentialMatchCard.setOnClickListener(itemView -> dialogTest.show());

        return viewHolder;
    }

//    @Override
//    public void onBindViewHolder(@NonNull recyclerViewAdapter_sw.MyViewHolder holder, int position) {
//        holder.textView.setText(userModels.get(position).getFullName());
//        holder.imageView.setImageResource(userModels.get(position).getUserImgInt());
//
//    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull PotentialMatch model) {
        String name = model.getName();
        Log.d("name: ", name);
        holder.textView.setText(name);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

//        ImageView imageView;
        TextView textView;
        CardView potentialMatchCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

//            imageView = itemView.findViewById(R.id.userImg);
            textView = itemView.findViewById(R.id.fullName);
            potentialMatchCard = itemView.findViewById(R.id.potential_match_card);

        }
    }
}

