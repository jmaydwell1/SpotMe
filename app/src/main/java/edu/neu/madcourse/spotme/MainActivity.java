package edu.neu.madcourse.spotme;

import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ColorSpace.Model> mModelList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new RecyclerViewAdapter(getListData());
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<ColorSpace.Model> getListData() {
        mModelList = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            mModelList.add(new ColorSpace.Model("TextView " + i));
        }
        return mModelList;
    }
}