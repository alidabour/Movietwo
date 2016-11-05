package com.example.ali.movietwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MovieRecycleAdapter.Callback {
    boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.detail_container)!=null){
            mTwoPane=true;
            if(savedInstanceState ==null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container,new DetailMovieFragment())
                        .commit();
            }
        }
        else {
            mTwoPane=false;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up addToFavBt, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(Movie movie) {
        if(mTwoPane){
            Bundle agrs = new Bundle();
            agrs.putString("id", movie.getId());
            agrs.putString("title", movie.getTitle());
            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(agrs);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container,fragment)
                    .commit();
        }else {
           Intent intent = new Intent(this,DetailMovie.class)
                   .putExtra("id", movie.getId());
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("poster_path", movie.getPoster_path());
            intent.putExtra("backdrop_path", movie.getBackdrop_path());
            intent.putExtra("overview", movie.getOverview());
            intent.putExtra("release_date", movie.getRelaseDate());
            Log.v("Test", "PosterPAthMMainActibvit" + movie.getPoster_path());
            startActivity(intent);
        }

    }
}
