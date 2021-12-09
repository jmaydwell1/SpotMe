package edu.neu.madcourse.spotme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import edu.neu.madcourse.spotme.database.models.Match;

public class MainMatchMessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<MessageMatchModel> matchList;
    MatchesRViewAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    Query query;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_matches);
        recyclerView = findViewById(R.id.matchedRView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("matches").document("tiffanymarthin@gmail.com").collection("swiped").whereEqualTo("match", true);
       initialRView();

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.matches);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.potentialMatches:
                        startActivity(new Intent(getApplicationContext(), PotentialMatchesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.matches:
                        return true;

//                    case R.id.sports:
//                        startActivity(new Intent(getApplicationContext(), ProfileBuilder.class));
//                        overridePendingTransition(0, 0);


                    case R.id.preferences:
                        startActivity(new Intent(getApplicationContext(), Preference.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void initialRView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        FirestoreRecyclerOptions<Match> options = new FirestoreRecyclerOptions.Builder<Match>()
                .setQuery(query, Match.class)
                .build();
        adapter = new MatchesRViewAdapter(options);
        recyclerView.setAdapter(adapter);
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

//    private void initialDate() {
//
//        matchList = new ArrayList<>();
//
//        matchList.add(new MessageMatchModel("Adam Smith", "10-10-1000",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_one_24));
//
//        matchList.add(new MessageMatchModel("Human Being", "20-20-2000",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_one_24));
//
//        matchList.add(new MessageMatchModel("Another Being", "11-11-1111",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_two_24));
//
//        matchList.add(new MessageMatchModel("Yup Human", "90-09-1590",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_3_24));
//
//        matchList.add(new MessageMatchModel("Adam Smith", "10-10-1000",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_one_24));
//
//        matchList.add(new MessageMatchModel("Human Being", "20-20-2000",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_one_24));
//
//        matchList.add(new MessageMatchModel("Another Being", "11-11-1111",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_two_24));
//
//        matchList.add(new MessageMatchModel("Yup Human", "90-09-1590",
//                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_3_24));
//
//    }

}