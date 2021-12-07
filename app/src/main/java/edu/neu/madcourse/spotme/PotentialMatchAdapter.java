package edu.neu.madcourse.spotme;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.Match;
import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class PotentialMatchAdapter extends RecyclerView.Adapter<PotentialMatchAdapter.PotentialMatchHolder> {

    Context context;
    ArrayList<PotentialMatch> potentialMatchArrayList;

    private Dialog potentialMatchDialog;
    private TextView dialogNameTv, dialogGenderAgeTv, dialogDistanceTv;
    private ShapeableImageView dialogPictureIv;
    private ImageView dialogSoccerIv, dialogPingPongIv, dialogYogaIv, dialogSkiIv, dialogSwimmingIv, dialogRunningIv;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference profilePictureStorage;
    private String loginId, userLatitude, userLongitude;
    private LocalDate today;

    public PotentialMatchAdapter(Context context, ArrayList<PotentialMatch> potentialMatchArrayList, String loginId, String userLatitude, String userLongitude) {
        this.context = context;
        this.potentialMatchArrayList = potentialMatchArrayList;
        this.today = LocalDate.now();
        this.storage = FirebaseStorage.getInstance();
        this.loginId = loginId;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PotentialMatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.potential_match_card, parent, false);
        PotentialMatchHolder viewHolder = new PotentialMatchHolder(v);
        initializeBigCard(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PotentialMatchHolder holder, int position) {
        PotentialMatch potentialMatch = potentialMatchArrayList.get(position);
        String genderAge = potentialMatch.getGender() + ", " + calculateAge(potentialMatch.getDob());
        Double distance = Math.round(Utils.distance(userLatitude, userLongitude, potentialMatch.getLatitude(), potentialMatch.getLongitude(), "M") * 100) / 100.0;
        String distanceText = distance + " miles away";
        String picturePath = "profile_pictures/" + potentialMatch.getPicture();
        profilePictureStorage = storage.getReference().child(picturePath);
        List<String> sports = potentialMatch.getSports();
        Log.d("onbindviewHolder", loginId + sports.toString());
        updatePotentialMatchCard(holder, potentialMatch.getName(), genderAge, profilePictureStorage, sports, distanceText);
        setBigCardListener(holder);
    }

    @Override
    public int getItemCount() {
        return potentialMatchArrayList.size();
    }

    public static class PotentialMatchHolder extends RecyclerView.ViewHolder {
        TextView holderNameTv, holderGenderAgeTv, holderDistanceTv;
        ShapeableImageView holderPictureIv;
        CardView potentialMatchCard;
        ImageView holderSoccerIv, holderPingPongIv, holderYogaIv, holderSkiIv, holderSwimmingIv, holderRunningIv;

        public PotentialMatchHolder(@NonNull View itemView) {
            super(itemView);
            holderNameTv = itemView.findViewById(R.id.potential_match_name);
            holderGenderAgeTv = itemView.findViewById(R.id.potential_match_genderage_tv);
            holderDistanceTv = itemView.findViewById(R.id.potential_match_distance_tv);
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

    private void updatePotentialMatchCard(PotentialMatchHolder holder, String name, String genderAge, StorageReference profilePictureStorage, List<String> sports, String distanceText) {
        holder.holderNameTv.setText(name);
        holder.holderGenderAgeTv.setText(genderAge);
        holder.holderDistanceTv.setText(distanceText);
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
            }
        }
        holder.updateSportsIconVisibility(soccer, pingpong, yoga, ski, swimming, running);
    }

    private int calculateAge(String date) {
        // "MM/DD/YY"
        String[] dateArray = date.split("/");
        String year = Integer.parseInt(dateArray[2]) > 50 ? ("19" + dateArray[2]) : ("20" + dateArray[2]);
        LocalDate birthday = LocalDate.of(Integer.parseInt(year), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
        Period p = Period.between(birthday, today);
        return p.getYears();
    }

    private void initializeBigCard(PotentialMatchHolder viewHolder) {
        potentialMatchDialog = new Dialog(viewHolder.potentialMatchCard.getContext());
        potentialMatchDialog.setContentView(R.layout.potential_buddy_dialog);
        potentialMatchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogFindViewIds();
    }

    private void dialogFindViewIds() {
        dialogNameTv = potentialMatchDialog.findViewById(R.id.potential_big_name);
        dialogPictureIv = potentialMatchDialog.findViewById(R.id.potential_big_picture);
        dialogGenderAgeTv = potentialMatchDialog.findViewById(R.id.potential_big_gender_age);
        dialogDistanceTv = potentialMatchDialog.findViewById(R.id.potential_big_distance);
        dialogSoccerIv = potentialMatchDialog.findViewById(R.id.potential_big_soccer_icon);
        dialogPingPongIv = potentialMatchDialog.findViewById(R.id.potential_big_pingpong_icon);
        dialogYogaIv = potentialMatchDialog.findViewById(R.id.potential_big_yoga_icon);
        dialogSkiIv = potentialMatchDialog.findViewById(R.id.potential_big_ski_icon);
        dialogSwimmingIv = potentialMatchDialog.findViewById(R.id.potential_big_swimming_icon);
        dialogRunningIv = potentialMatchDialog.findViewById(R.id.potential_big_running_icon);
    }

    private void setBigCardListener(PotentialMatchHolder viewHolder) {
        viewHolder.potentialMatchCard.setOnClickListener(itemView -> {
            dialogNameTv.setText(viewHolder.holderNameTv.getText());
            dialogGenderAgeTv.setText(viewHolder.holderGenderAgeTv.getText());
            dialogDistanceTv.setText(viewHolder.holderDistanceTv.getText());
            dialogPictureIv.setImageDrawable(viewHolder.holderPictureIv.getDrawable());
            setDialogSportIcons(viewHolder);
            potentialMatchDialog.show();
        });
    }

    private void setDialogSportIcons(PotentialMatchHolder viewHolder) {
        dialogSoccerIv.setVisibility(viewHolder.holderSoccerIv.getVisibility());
        dialogPingPongIv.setVisibility(viewHolder.holderPingPongIv.getVisibility());
        dialogYogaIv.setVisibility(viewHolder.holderYogaIv.getVisibility());
        dialogSkiIv.setVisibility(viewHolder.holderSkiIv.getVisibility());
        dialogSwimmingIv.setVisibility(viewHolder.holderSwimmingIv.getVisibility());
        dialogRunningIv.setVisibility(viewHolder.holderRunningIv.getVisibility());
    }

    public void checkIfUsersMatch(int position, PotentialMatch deletedMatch) {
        PotentialMatch userB = deletedMatch;
        String userBLoginId = userB.getEmail();

        Log.d("checkIf - CURRENT USER: ", loginId);
        Log.d("checkIf - USER B: ", userBLoginId);

        DocumentReference docRef = Firestore.readFromDBSubCollection(db, "matches", userBLoginId, "swiped", loginId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                writeMatchToDB(deletedMatch, document.exists());
            } else {
                Log.d("Check If Users Match", "get failed with ", task.getException());
            }
        });
    }

    private void writeMatchToDB(PotentialMatch deletedMatch, boolean exist) {
        PotentialMatch userB = deletedMatch;
        String userBLoginId = userB.getEmail();
        String name = userB.getName();
        String picture = userB.getPicture();
        String date = formatTodayDate();

        Log.d("writeToDB - CURRENT USER: ", loginId);
        Log.d("writeToDB - USER B: ", userBLoginId);

        final Match[] matchData = new Match[2];
        if (exist) {
            Log.d("Write Match to DB", "YES USERS MATCH");
            String userBName = deletedMatch.getName();
            String userBPicture = deletedMatch.getPicture();
            matchData[0] = new Match(name, picture, date, true);
            matchData[1] = new Match(userBName, userBPicture, date, true);
            Firestore.writeToDBSubCollection(db, "matches", loginId, "swiped", userBLoginId, matchData[0]);
            Firestore.writeToDBSubCollection(db, "matches", userBLoginId, "swiped", loginId, matchData[1]);
            // TODO send a notification

        } else {
            Log.d("Write Match to DB", "USERS DON'T MATCH");
            matchData[0] = new Match(name, picture, date, false);
            Firestore.writeToDBSubCollection(db, "matches", loginId, "swiped", userBLoginId, matchData[0]);
        }
    }

    private String formatTodayDate() {
        LocalDate today = LocalDate.now();
        return today.getMonthValue() + "/" + today.getDayOfMonth() + "/" + today.getYear();
    }
}
