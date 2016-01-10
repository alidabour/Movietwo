package com.example.ali.movietwo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DetailMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        if(savedInstanceState==null){
            Bundle arg = new Bundle();
            arg.putString("json",DetailMovieFragment.jsonArrayCB);
            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(arg);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container,new DetailMovieFragment())
                    .commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
