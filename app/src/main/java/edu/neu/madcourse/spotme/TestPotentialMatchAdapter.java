package edu.neu.madcourse.spotme;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class TestPotentialMatchAdapter extends RecyclerView.Adapter<TestPotentialMatchAdapter.TestPMViewHolder> {

    Context context;
    ArrayList<PotentialMatch> potentialMatchArrayList;

    private Dialog dialogTest;
    private TextView dialogNameTv;
    private TextView dialogGenderAgeTv;
    private ShapeableImageView dialogPictureIv;
    private ImageView dialogSoccerIv, dialogPingPongIv, dialogYogaIv, dialogSkiIv, dialogSwimmingIv, dialogRunningIv;
    private LocalDate today;
    private FirebaseStorage storage;
    private StorageReference profilePictureStorage;
    private FirebaseFirestore db;
    private String loginId;

    public TestPotentialMatchAdapter(Context context, ArrayList<PotentialMatch> potentialMatchArrayList, String loginId) {
        this.context = context;
        this.potentialMatchArrayList = potentialMatchArrayList;
        this.today = LocalDate.now();
        this.storage = FirebaseStorage.getInstance();
        this.loginId = loginId;
    }

    @NonNull
    @Override
    public TestPotentialMatchAdapter.TestPMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.potential_match_card, parent, false);
        TestPMViewHolder viewHolder = new TestPMViewHolder(v);
        initializeBigCard(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestPotentialMatchAdapter.TestPMViewHolder holder, int position) {
        PotentialMatch potentialMatch = potentialMatchArrayList.get(position);
        String genderAge = potentialMatch.getGender() + ", " + calculateAge(potentialMatch.getDob());
        String picturePath = "profile_pictures/" + potentialMatch.getPicture();
        profilePictureStorage = storage.getReference().child(picturePath);
        List<String> sports = potentialMatch.getSports();
        updatePotentialMatchCard(holder, potentialMatch.getName(), genderAge, profilePictureStorage, sports);
        setBigCardListener(holder);
    }

    @Override
    public int getItemCount() {
        return potentialMatchArrayList.size();
    }

    public static class TestPMViewHolder extends RecyclerView.ViewHolder {
        TextView holderNameTv;
        TextView holderGenderAgeTv;
        ShapeableImageView holderPictureIv;
        CardView potentialMatchCard;
        ImageView holderSoccerIv, holderPingPongIv, holderYogaIv, holderSkiIv, holderSwimmingIv, holderRunningIv;

        public TestPMViewHolder(@NonNull View itemView) {
            super(itemView);
            holderNameTv = itemView.findViewById(R.id.potential_match_name);
            holderGenderAgeTv = itemView.findViewById(R.id.potential_match_genderage_tv);
            holderPictureIv = itemView.findViewById(R.id.potential_match_picture);
            potentialMatchCard = itemView.findViewById(R.id.potential_match_card);

            holderSoccerIv = itemView.findViewById(R.id.potential_match_soccer_icon);
            holderPingPongIv = itemView.findViewById(R.id.potential_match_pingpong_icon);
            holderYogaIv = itemView.findViewById(R.id.potential_match_yoga_icon);
            holderSkiIv = itemView.findViewById(R.id.potential_match_ski_icon);
            holderSwimmingIv = itemView.findViewById(R.id.potential_match_swimming_icon);
            holderRunningIv = itemView.findViewById(R.id.potential_match_running_icon);
        }

        public void updateSportsIconVisibility(boolean soccer, boolean pingpong, boolean yoga, boolean ski, boolean swimming, boolean running) {
            if (soccer) holderSoccerIv.setVisibility(View.VISIBLE);
            if (pingpong) holderPingPongIv.setVisibility(View.VISIBLE);
            if (yoga) holderYogaIv.setVisibility(View.VISIBLE);
            if (ski) holderSkiIv.setVisibility(View.VISIBLE);
            if (swimming) holderSwimmingIv.setVisibility(View.VISIBLE);
            if (running) holderRunningIv.setVisibility(View.VISIBLE);
        }
    }

    private void updatePotentialMatchCard(TestPotentialMatchAdapter.TestPMViewHolder holder, String name, String genderAge, StorageReference profilePictureStorage, List<String> sports) {
        holder.holderNameTv.setText(name);
        holder.holderGenderAgeTv.setText(genderAge);
        Glide.with(holder.holderPictureIv.getContext()).load(profilePictureStorage).into(holder.holderPictureIv);

        boolean soccer = false, pingpong = false, yoga = false, ski = false, swimming = false, running = false;

        for (String sport : sports) {
            switch (sport) {
                case "Soccer":
                    soccer = true;
                    continue;
                case "Ping Pong":
                    pingpong = true;
                    continue;
                case "Yoga":
                    yoga = true;
                    continue;
                case "Ski":
                    ski = true;
                    continue;
                case "Swimming":
                    swimming = true;
                    continue;
                case "Running":
                    running = true;
                    continue;
            }
        }
        holder.updateSportsIconVisibility(soccer, pingpong, yoga, ski, swimming, running);
    }

    private void updateBigCard(String name, String genderAgeText) {
        dialogFindViewIds();

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

    private void initializeBigCard(TestPotentialMatchAdapter.TestPMViewHolder viewHolder) {
        dialogTest = new Dialog(viewHolder.potentialMatchCard.getContext());
        dialogTest.setContentView(R.layout.potential_buddy_dialog);
        dialogTest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogFindViewIds();
    }

    private void dialogFindViewIds() {
        dialogNameTv = dialogTest.findViewById(R.id.potential_big_name);
        dialogGenderAgeTv = dialogTest.findViewById(R.id.potential_big_gender_age);
        dialogPictureIv = dialogTest.findViewById(R.id.potential_big_picture);
        dialogSoccerIv = dialogTest.findViewById(R.id.potential_big_soccer_icon);
        dialogPingPongIv = dialogTest.findViewById(R.id.potential_big_pingpong_icon);
        dialogYogaIv = dialogTest.findViewById(R.id.potential_big_yoga_icon);
        dialogSkiIv = dialogTest.findViewById(R.id.potential_big_ski_icon);
        dialogSwimmingIv = dialogTest.findViewById(R.id.potential_big_swimming_icon);
        dialogRunningIv = dialogTest.findViewById(R.id.potential_big_running_icon);
    }

    private void setBigCardListener(TestPotentialMatchAdapter.TestPMViewHolder viewHolder) {
        viewHolder.potentialMatchCard.setOnClickListener(itemView -> {
            dialogNameTv.setText(viewHolder.holderNameTv.getText());
            dialogGenderAgeTv.setText(viewHolder.holderGenderAgeTv.getText());
            dialogPictureIv.setImageDrawable(viewHolder.holderPictureIv.getDrawable());
            setDialogSportIcons(viewHolder);
            dialogTest.show();
        });
    }

    private void setDialogSportIcons(TestPotentialMatchAdapter.TestPMViewHolder viewHolder) {
        dialogSoccerIv.setVisibility(viewHolder.holderSoccerIv.getVisibility());
        dialogPingPongIv.setVisibility(viewHolder.holderPingPongIv.getVisibility());
        dialogYogaIv.setVisibility(viewHolder.holderYogaIv.getVisibility());
        dialogSkiIv.setVisibility(viewHolder.holderSkiIv.getVisibility());
        dialogSwimmingIv.setVisibility(viewHolder.holderSwimmingIv.getVisibility());
        dialogRunningIv.setVisibility(viewHolder.holderRunningIv.getVisibility());
    }
}
