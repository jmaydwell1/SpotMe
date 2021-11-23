package edu.neu.madcourse.spotme;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.time.LocalDate;
import java.time.Period;

import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class recyclerViewAdapter_sw extends FirestoreRecyclerAdapter<PotentialMatch, recyclerViewAdapter_sw.MyViewHolder> {
//    Context context;
//    ArrayList<userModel> userModels;
    Dialog dialogTest;
    TextView dialogNameTv;
    TextView dialogGenderAgeTv;
    ShapeableImageView dialogPictureIv;
    LocalDate today;


    public recyclerViewAdapter_sw(@NonNull FirestoreRecyclerOptions<PotentialMatch> options){
        super(options);
        today = LocalDate.now();
//        this.context = context;
//        this.userModels = userModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_row_sw, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        dialogTest = new Dialog(viewHolder.potentialMatchCard.getContext());
        dialogTest.setContentView(R.layout.potential_buddy_dialog);
        dialogTest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogNameTv = (TextView) dialogTest.findViewById(R.id.potential_big_name);
        dialogGenderAgeTv = (TextView) dialogTest.findViewById(R.id.potential_big_gender_age);
        dialogPictureIv = (ShapeableImageView) dialogTest.findViewById(R.id.potential_big_picture);

        viewHolder.potentialMatchCard.setOnClickListener(itemView -> {
            dialogNameTv.setText(viewHolder.holderNameTv.getText());
            dialogGenderAgeTv.setText(viewHolder.holderGenderAgeTv.getText());
            dialogTest.show();
        });

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
        String gender = model.getGender();
        String birthday = model.getDob();
        int userAge = calculateAge(birthday);
        String genderAge = gender + ", " + userAge;

        holder.holderNameTv.setText(name);
        holder.holderGenderAgeTv.setText(genderAge);

        onBindDialogUpdate(name, genderAge);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

//        ImageView imageView;
        TextView holderNameTv;
        TextView holderGenderAgeTv;
        ShapeableImageView holderPictureIv;
        CardView potentialMatchCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            holderNameTv = itemView.findViewById(R.id.potential_match_name);
            holderGenderAgeTv = itemView.findViewById(R.id.potential_match_genderage_tv);
            holderPictureIv = itemView.findViewById(R.id.potential_match_picture);
            potentialMatchCard = itemView.findViewById(R.id.potential_match_card);
        }
    }

    private void onBindDialogUpdate(String name, String genderAgeText) {
        dialogNameTv = (TextView) dialogTest.findViewById(R.id.potential_big_name);
        dialogGenderAgeTv = (TextView) dialogTest.findViewById(R.id.potential_big_gender_age);
        dialogPictureIv = (ShapeableImageView) dialogTest.findViewById(R.id.potential_big_picture);

        dialogNameTv.setText(name);
        dialogGenderAgeTv.setText(genderAgeText);
    }

    private int calculateAge(String date) {
        // "MM/DD/YY"
        String[] dateArray = date.split("/");
        String year = Integer.parseInt(dateArray[2]) > 50 ? ("19" + dateArray[2]) : ("20" + dateArray[2]);
        LocalDate birthday = LocalDate.of(Integer.parseInt(year), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
        Period p = Period.between(birthday, today);
        return p.getYears();
    }

}

