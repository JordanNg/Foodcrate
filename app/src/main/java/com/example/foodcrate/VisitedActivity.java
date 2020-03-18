package com.example.foodcrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.foodcrate.data.YelpItem;

import java.util.List;

public class VisitedActivity extends AppCompatActivity {

    private SavedYelpItemsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);

        RecyclerView visitedRV = findViewById(R.id.rv_visited);
        visitedRV.setLayoutManager(new LinearLayoutManager(this));
        visitedRV.setHasFixedSize(true);

        final VisitedAdapter adapter = new VisitedAdapter();
        visitedRV.setAdapter(adapter);

        mViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(SavedYelpItemsViewModel.class);

        mViewModel.getAllYelpItems().observe(this, new Observer<List<YelpItem>>() {
            @Override
            public void onChanged(List<YelpItem> yelpItems) {
                adapter.updateSearchResults(yelpItems);
            }
        });
    }
}
