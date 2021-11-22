package edu.neu.madcourse.spotme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class PotentialMatches extends AppCompatActivity {

    private RecyclerView recyclerView;
    private recyclerViewAdapter_sw adapter;
    private Query db;

    ArrayList<userModel> userModelsList = new ArrayList<>();

    int[] userProfileImgs = {R.drawable.woman0, R.drawable.man1, R.drawable.man2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.potential_matches);

        recyclerView = findViewById(R.id.swRecyclerView);

//        setUpUserModels();

        // Pull from FirebaseDatabase
        db = FirebaseFirestore.getInstance().collection("users");

//        adapter = new recyclerViewAdapter_sw(this, userModelsList);
//        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        populatePotentialMatches();
    }

    private void setUpUserModels(){
        String[] usersFullName = getResources().getStringArray(R.array.user_fullNames_all);

        for (int i = 0; i<usersFullName.length; i++){
            userModelsList.add(new userModel(usersFullName[i], userProfileImgs[i]));
        }
    }

    private void populatePotentialMatches() {
        FirestoreRecyclerOptions<PotentialMatch> options = new FirestoreRecyclerOptions.Builder<PotentialMatch>().setQuery(db, PotentialMatch.class).build();
        adapter = new recyclerViewAdapter_sw(options);
        recyclerView.setAdapter(adapter);
    }
}
