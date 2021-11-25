package edu.neu.madcourse.spotme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class PotentialMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PotentialMatchAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private Query query;

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

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void populatePotentialMatches() {
        FirestoreRecyclerOptions<PotentialMatch> options = new FirestoreRecyclerOptions.Builder<PotentialMatch>()
                .setQuery(query, PotentialMatch.class)
                .build();
        adapter = new PotentialMatchAdapter(options);
        recyclerView.setAdapter(adapter);
    }

}
