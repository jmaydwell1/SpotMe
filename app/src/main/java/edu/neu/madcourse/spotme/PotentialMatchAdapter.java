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

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.Period;

import edu.neu.madcourse.spotme.database.firestore.Firestore;
import edu.neu.madcourse.spotme.database.models.Match;
import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class PotentialMatchAdapter extends FirestoreRecyclerAdapter<PotentialMatch, PotentialMatchAdapter.PotentialMatchHolder> {
    private Dialog dialogTest;
    private TextView dialogNameTv;
    private TextView dialogGenderAgeTv;
    private ShapeableImageView dialogPictureIv;
    private LocalDate today;
    private FirebaseStorage storage;
    private StorageReference profilePictureStorage;
    private FirebaseFirestore db;
    private String loginId;

    public PotentialMatchAdapter(@NonNull FirestoreRecyclerOptions<PotentialMatch> options, String loginId){
        super(options);
        this.today = LocalDate.now();
        this.storage = FirebaseStorage.getInstance();
        this.loginId = loginId;
        this.db = FirebaseFirestore.getInstance();
        Log.d("CURRENT USER CONSTRUCTION: ", loginId);
    }

    @Override
    public PotentialMatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.potential_match_card, parent, false);
        final PotentialMatchHolder viewHolder = new PotentialMatchHolder(view);

        initializeBigCard(viewHolder);
        setBigCardListener(viewHolder);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull PotentialMatchHolder holder, int position, @NonNull PotentialMatch model) {
        String genderAge = model.getGender() + ", " + calculateAge(model.getDob());
        String picturePath = "profile_pictures/" + model.getPicture();
        profilePictureStorage = storage.getReference().child(picturePath);
        updatePotentialMatchCard(holder, model.getName(), genderAge, profilePictureStorage);
        updateBigCard(model.getName(), genderAge);

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void checkIfUsersMatch(int position) {
        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
        String userBLoginId = snapshot.getId();

        Log.d("CURRENT USER: ", loginId);
        Log.d("OTHER USER: ", userBLoginId);

        DocumentReference docRef = Firestore.readFromDBSubCollection(db, "matches", userBLoginId, "swiped", loginId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                writeMatchToDB(position, document.exists(), document);
            } else {
                Log.d("CheckIfUsersMatch", "get failed with ", task.getException());
            }
        });
    }

    private void writeMatchToDB(int position, boolean exist, DocumentSnapshot document) {
        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
        String userBLoginId = snapshot.getId();
        String name = snapshot.getString("name");
        String picture = snapshot.getString("picture");
        String date = formatTodayDate();

        Log.d("CURRENT USER: ", loginId);
        Log.d("OTHER USER: ", userBLoginId);

        final Match[] matchData = new Match[2];
        if (exist) {
            Log.d("CheckIfUsersMatch", "DocumentSnapshot data: " + document.getData());
            String userBName = document.getString("name");
            String userBPicture = document.getString("picture");
            matchData[0] = new Match(name, picture, date, true);
            matchData[1] = new Match(userBName, userBPicture, date, true);
            Firestore.writeToDBSubCollection(db, "matches", loginId, "swiped", userBLoginId, matchData[0]);
            Firestore.writeToDBSubCollection(db, "matches", userBLoginId, "swiped", loginId, matchData[1]);
            // TODO send a notification

        } else {
            Log.d("CheckIfUsersMatch", "No such document");
            matchData[0] = new Match(name, picture, date, false);
            Firestore.writeToDBSubCollection(db, "matches", loginId, "swiped", userBLoginId, matchData[0]);
        }
    }

    private String formatTodayDate() {
        LocalDate today = LocalDate.now();
        String formattedDate = today.getMonthValue() + "/" + today.getDayOfMonth() + "/" + today.getYear();
        return formattedDate;
    }

    public class PotentialMatchHolder extends RecyclerView.ViewHolder{
        TextView holderNameTv;
        TextView holderGenderAgeTv;
        ShapeableImageView holderPictureIv;
        CardView potentialMatchCard;

        public PotentialMatchHolder(@NonNull View itemView) {
            super(itemView);
            holderNameTv = itemView.findViewById(R.id.potential_match_name);
            holderGenderAgeTv = itemView.findViewById(R.id.potential_match_genderage_tv);
            holderPictureIv = itemView.findViewById(R.id.potential_match_picture);
            potentialMatchCard = itemView.findViewById(R.id.potential_match_card);
        }
    }

    private void updatePotentialMatchCard(PotentialMatchHolder holder, String name, String genderAge, StorageReference profilePictureStorage) {
        holder.holderNameTv.setText(name);
        holder.holderGenderAgeTv.setText(genderAge);
        Glide.with(holder.holderPictureIv.getContext()).load(profilePictureStorage).into(holder.holderPictureIv);
    }

    private void updateBigCard(String name, String genderAgeText) {
        dialogNameTv = dialogTest.findViewById(R.id.potential_big_name);
        dialogGenderAgeTv = dialogTest.findViewById(R.id.potential_big_gender_age);
        dialogPictureIv = dialogTest.findViewById(R.id.potential_big_picture);

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

    private void initializeBigCard(PotentialMatchHolder viewHolder) {
        dialogTest = new Dialog(viewHolder.potentialMatchCard.getContext());
        dialogTest.setContentView(R.layout.potential_buddy_dialog);
        dialogTest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogNameTv = dialogTest.findViewById(R.id.potential_big_name);
        dialogGenderAgeTv = dialogTest.findViewById(R.id.potential_big_gender_age);
        dialogPictureIv = dialogTest.findViewById(R.id.potential_big_picture);
    }

    private void setBigCardListener(PotentialMatchHolder viewHolder) {
        viewHolder.potentialMatchCard.setOnClickListener(itemView -> {
            dialogNameTv.setText(viewHolder.holderNameTv.getText());
            dialogGenderAgeTv.setText(viewHolder.holderGenderAgeTv.getText());
            dialogPictureIv.setImageDrawable(viewHolder.holderPictureIv.getDrawable());
            dialogTest.show();
        });
    }

}

