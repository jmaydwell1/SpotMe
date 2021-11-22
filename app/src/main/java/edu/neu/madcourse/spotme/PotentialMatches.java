package edu.neu.madcourse.spotme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class PotentialMatches extends AppCompatActivity {

    ArrayList<userModel> userModelsList = new ArrayList<>();

    int[] userProfileImgs = {R.drawable.woman0, R.drawable.man1, R.drawable.man2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.potential_matches);

        RecyclerView recyclerView = findViewById(R.id.swRecyclerView);

        setUpUserModels();

        recyclerViewAdapter_sw adapter = new recyclerViewAdapter_sw(this, userModelsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpUserModels(){
        String[] usersFullName = getResources().getStringArray(R.array.user_fullNames_all);

        for (int i = 0; i<usersFullName.length; i++){
            userModelsList.add(new userModel(usersFullName[i], userProfileImgs[i]));
        }
    }
}
