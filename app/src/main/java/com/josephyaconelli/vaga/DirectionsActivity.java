package com.josephyaconelli.vaga;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.josephyaconelli.vaga.com.josephyaconelli.vaga.DirectionsAdapter;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.Directions;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Route;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Step;

import java.util.NoSuchElementException;

public class DirectionsActivity extends AppCompatActivity {

    RecyclerView mDirectionsRecyclerView;
    DirectionsAdapter mDirectionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        mDirectionsRecyclerView = findViewById(R.id.directions_rv);
        mDirectionsAdapter = new DirectionsAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDirectionsRecyclerView.setLayoutManager(layoutManager);

        mDirectionsRecyclerView.setHasFixedSize(true);

        mDirectionsAdapter = new DirectionsAdapter();

        mDirectionsRecyclerView.setAdapter(mDirectionsAdapter);


        try {

            Route route = Directions.getRoute();
            mDirectionsAdapter.setSteps(route.steps.toArray(new Step[route.steps.size()]));

        } catch (NoSuchElementException e){
            Toast error = Toast.makeText(this, "Something went wrong... so sorry!", Toast.LENGTH_LONG);
            error.show();

        }



    }
}
