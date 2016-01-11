package com.example.ali.movietwo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class DetailMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
//        if(savedInstanceState==null){
//            Bundle arg = new Bundle();
//            arg.putString("json",DetailMovieFragment.jsonArrayCB);
//            DetailMovieFragment fragment = new DetailMovieFragment();
//            fragment.setArguments(arg);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.detail_container,new DetailMovieFragment())
//                    .commit();
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
