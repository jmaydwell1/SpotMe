package edu.neu.madcourse.spotme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class PotentialMatches extends AppCompatActivity {

    private RecyclerView recyclerView;
    private recyclerViewAdapter_sw adapter;
    private FirebaseFirestore firebaseFirestore;
    private Query query;

    ArrayList<userModel> userModelsList = new ArrayList<>();

    int[] userProfileImgs = {R.drawable.woman0, R.drawable.man1, R.drawable.man2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.potential_matches);
        recyclerView = findViewById(R.id.swRecyclerView);

        // Pull data from Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("users");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        populatePotentialMatches();
    }

//    private void setUpUserModels(){
//        String[] usersFullName = getResources().getStringArray(R.array.user_fullNames_all);
//
//        for (int i = 0; i<usersFullName.length; i++){
//            userModelsList.add(new userModel(usersFullName[i], userProfileImgs[i]));
//        }
//    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    private void populatePotentialMatches() {
        FirestoreRecyclerOptions<PotentialMatch> options = new FirestoreRecyclerOptions.Builder<PotentialMatch>()
                .setQuery(query, PotentialMatch.class)
                .build();
        adapter = new recyclerViewAdapter_sw(options);
        recyclerView.setAdapter(adapter);
    }

}
