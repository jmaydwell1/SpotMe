package edu.neu.madcourse.spotme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.spotme.database.models.PotentialMatch;

public class PotentialMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PotentialMatchAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private Query query;
    private Query preferenceQuery;
    private SharedPreferences sharedPreferences;
    private String loginId;

    private Integer preferenceDistance, preferenceMinAge, preferenceMaxAge;
    private List<String> preferenceGenders, preferenceSports;

    private String preferenceMinDOB, preferenceMaxDOB;

    private static String SHARED_PREF_NAME = "SpotMeSP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.potential_matches);
        recyclerView = findViewById(R.id.swRecyclerView);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", "empty");

        // Preferences hard coded
        preferenceSports = new ArrayList<>(Arrays.asList("Soccer", "Ski"));
        preferenceGenders = new ArrayList<>(Arrays.asList("Female"));

        // Pull data from Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
//        query = firebaseFirestore.collection("users").whereNotEqualTo("email", loginId); // figure out a way to do this other than not equal
        query = firebaseFirestore.collection("users").orderBy("name").startAt("T"); // for distance
//        preferenceQuery = query.whereGreaterThanOrEqualTo("dob", preferenceMinDOB).whereLessThanOrEqualTo("dob", preferenceMaxDOB);
        preferenceQuery = query.whereArrayContainsAny("sports", preferenceSports);
        if (preferenceGenders.size() == 1) {
            preferenceQuery = preferenceQuery.whereEqualTo("gender", preferenceGenders.get(0));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        populatePotentialMatches();
        onSwipeLeftConfig();
        onSwipeRightConfig();
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
                .setQuery(preferenceQuery, PotentialMatch.class)
                .build();
        adapter = new PotentialMatchAdapter(options, loginId);
        recyclerView.setAdapter(adapter);
    }

    private void onSwipeLeftConfig() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.notifyItemMoved(viewHolder.getLayoutPosition(), adapter.getItemCount() - 1);
                recyclerView.scrollToPosition(0);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void onSwipeRightConfig() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.checkIfUsersMatch(viewHolder.getLayoutPosition());
                adapter.notifyItemMoved(viewHolder.getLayoutPosition(), adapter.getItemCount() - 1);
                recyclerView.scrollToPosition(0);
            }
        }).attachToRecyclerView(recyclerView);
    }

}
