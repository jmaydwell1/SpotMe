package edu.neu.madcourse.spotme;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.neu.madcourse.spotme.database.models.Match;
import edu.neu.madcourse.spotme.database.models.PotentialMatch;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PotentialMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<PotentialMatch> potentialMatches;
    private ArrayList<String> matchesId;
    private PotentialMatchAdapter adapter;
    private FirebaseFirestore db;
    private String loginId, userALatitude, userALongitude;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;

    private Integer preferenceDistance, preferenceMinAge, preferenceMaxAge;
    private List<String> preferenceGenders, preferenceSports;
    private LocalDate today;

    private static final String TAG = "PotentialMatchesActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.potential_matches);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setProgressTintList(ColorStateList.valueOf(Color.GRAY));

        recyclerView = findViewById(R.id.swRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String SHARED_PREF_NAME = "SpotMeSP";
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", "empty");
        // default is Northeastern University location
        userALatitude = sharedPreferences.getString("userLatitude", "42.478951");
        userALongitude = sharedPreferences.getString("userLongitude", "-71.189247");
        Log.d(TAG, userALatitude);
        Log.d(TAG, userALongitude);

        preferenceDistance = 100;
        preferenceSports = new ArrayList<>(Arrays.asList("Swimming", "Ping Pong", "Soccer"));
        preferenceGenders = new ArrayList<>(Arrays.asList("Female", "Male"));
        preferenceMinAge = 0;
        preferenceMaxAge = 30;

        today = LocalDate.now();
        db = FirebaseFirestore.getInstance();
        matchesId = new ArrayList<>();
        potentialMatches = new ArrayList<>();
        matchesListener();
//        potentialMatchesListener();

        adapter = new PotentialMatchAdapter(PotentialMatchesActivity.this, potentialMatches, loginId, userALatitude, userALongitude);
        recyclerView.setAdapter(adapter);
        onSwipeConfig();
    }

    private void potentialMatchesListener() {
        db.collection("users").whereNotEqualTo("email", loginId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Log.e("Firestore data potential match error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                // filter potential matches here
                                PotentialMatch potentialMatch = dc.getDocument().toObject(PotentialMatch.class);
                                Log.d("potential match", potentialMatch.getEmail());
                                Log.d("potential match", potentialMatch.getPhone());
                                Log.d("potential match", "lat: " + potentialMatch.getLatitude());
                                Log.d("potential match", "long: " + potentialMatch.getLongitude());
                                if (notMatchedYet(potentialMatch.getEmail()) && matchPreferences(potentialMatch)) {
                                    potentialMatches.add(potentialMatch);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }

    private void matchesListener() {
        db.collection("matches").document(loginId).collection("swiped")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Log.e("Firestore data potential match error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                // get matched users here
                                Match match = dc.getDocument().toObject(Match.class);
                                if (match.isMatch()) {
                                    Log.d("matches listener", match.getName());
                                    matchesId.add(dc.getDocument().getId());
                                }
                            }
                        }
                        Log.d("matches size", String.valueOf(matchesId.size()));
                        potentialMatchesListener();
                    }
                });
    }

    private void onSwipeConfig() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        potentialMatches.remove(position);
                        adapter.notifyItemRemoved(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        PotentialMatch deletedMatch = potentialMatches.get(position);
                        adapter.checkIfUsersMatch(position, deletedMatch);
                        potentialMatches.remove(position);
                        adapter.notifyItemRemoved(position);
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(PotentialMatchesActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_check_box_24)
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_forever_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, true);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private boolean notMatchedYet(String potentialMatchEmail) {
        return !matchesId.contains(potentialMatchEmail);
    }

    private boolean matchPreferences(PotentialMatch potentialMatch) {
        return gendersFilter(potentialMatch.getGender())
                && ageFilter(potentialMatch.getDob())
                && sportsFilter(potentialMatch.getSports())
                && withinDistance(potentialMatch.getLatitude(), potentialMatch.getLongitude());
    }

    private boolean ageFilter(String dob) {
        int userAge = calculateAge(dob);
        return userAge >= preferenceMinAge && userAge <= preferenceMaxAge;
    }

    private int calculateAge(String date) {
        // "MM/DD/YY"
        String[] dateArray = date.split("/");
        String year = Integer.parseInt(dateArray[2]) > 50 ? ("19" + dateArray[2]) : ("20" + dateArray[2]);
        LocalDate birthday = LocalDate.of(Integer.parseInt(year), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
        Period p = Period.between(birthday, today);
        return p.getYears();
    }


    private boolean sportsFilter(List<String> sports) {
        return !Collections.disjoint(sports, preferenceSports);
    }

    private boolean gendersFilter(String gender) {
        return preferenceGenders.contains(gender);
    }

    private boolean withinDistance(String userBLatitude, String userBLongitude) {
        Log.d(TAG, "userBLat: " + userBLatitude);
        Log.d(TAG, "userBLon: " + userBLongitude);
        double distance = Utils.distance(userALatitude, userALongitude, userBLatitude, userBLongitude, "M");
        final double MARGIN_OF_ERROR = 0.2;
        double difference = preferenceDistance - Math.abs(distance);
        Log.d(TAG, "distance: ");
        return Math.abs(difference) >= MARGIN_OF_ERROR;
    }
}
