package edu.neu.madcourse.spotme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainMatchMessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<MessageMatchModel> matchList;
    MatchesRViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_matches);

       initialDate();
       initialRView();

    }

    private void initialRView() {
        recyclerView = findViewById(R.id.matchedRView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MatchesRViewAdapter(matchList);
        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    private void initialDate() {

        matchList = new ArrayList<>();

        matchList.add(new MessageMatchModel("Adam Smith", "10-10-1000",
                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_one_24));

        matchList.add(new MessageMatchModel("Human Being", "20-20-2000",
                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_one_24));

        matchList.add(new MessageMatchModel("Another Being", "11-11-1111",
                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_two_24));

        matchList.add(new MessageMatchModel("Yup Human", "90-09-1590",
                R.drawable.ic_baseline_message_24, R.drawable.ic_baseline_looks_3_24));

    }


}