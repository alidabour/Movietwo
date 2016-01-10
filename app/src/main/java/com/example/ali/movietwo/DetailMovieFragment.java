package com.example.ali.movietwo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment {
    View view;
    String poster_path;
    String movieName ;
    String releaseDate;
    String overview;
    String voteAverage;
    String id;
    String videos[];
    String name[];
    JSONObject child;
    JSONObject childR;
     static String jsonArrayCB=null;
    private CustomBtnAdapter videoAdapter;
    private ArrayAdapter<String> reviewAdapter;
    Button button;
    ListView listView;
    public DetailMovieFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            jsonArrayCB=arguments.getString("json");

        }
        view=inflater.inflate(R.layout.fragment_detail_movie, container, false);
        final Intent intent=getActivity().getIntent();
//        videoAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.btn_list_item,
//                R.id.btn_tv,
//                new ArrayList<String>()
//        );

         listView = (ListView) view.findViewById(R.id.btn_LV);


//        Button button;//=(Button) view.findViewById(R.id.btn_tv);
//        button= (Button) listView.getChildAt(0);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videos[0])));
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Button button=(Button) view.findViewById(R.id.btn_tv);
//                final int x=position;
//               button= (Button) listView.getChildAt(position);
//                       button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videos[position])));
//                    }
//                });
//               // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videos[position])));
//
//            }
//        });
        videoAdapter =new CustomBtnAdapter(getActivity(),new ArrayList<String>());


        reviewAdapter = new ArrayAdapter<String>(getActivity(),R.layout.review_list_item,R.id.review_tv,new ArrayList<String>());
        ListView listView1 = (ListView) view.findViewById(R.id.review_LV);
        listView1.setAdapter(reviewAdapter);
        button=(Button) view.findViewById(R.id.favBN);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String paths = sharedPreferences.getString("path", "");
                int total;
                total=sharedPreferences.getInt("total",0);

                try {
                    paths+=new JSONObject(jsonArrayCB);
                    editor.putString("path",paths +",");
                    ++total;
                    editor.putInt("total",total);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();

                Log.v("Path",paths );
                Log.v("Total", String.valueOf(total));

            }
        });
        if (intent!=null && intent.hasExtra("json")){
            jsonArrayCB=intent.getStringExtra("json");
        }
        if(jsonArrayCB!=null) {
            Log.v("Linear","True");
            try {
                JSONObject obj = new JSONObject(jsonArrayCB);
                id = obj.optString("id");
                poster_path = "http://image.tmdb.org/t/p/w185/";
                poster_path += obj.optString("poster_path").toString();
                Picasso.with(getActivity()).load(poster_path).into((ImageView) view.findViewById(R.id.image_detail));
                movieName = obj.optString("original_title").toString();
                TextView orTV =
                        ((TextView) view.findViewById(R.id.movieName));
                orTV.setText(movieName);
                releaseDate = obj.optString("release_date").toString();
                releaseDate = releaseDate.substring(0, 4);
                Log.v("Release", releaseDate);
                TextView releaseTV = ((TextView) view.findViewById(R.id.releaseDate));
                releaseTV.setText(releaseDate);
                overview = obj.optString("overview").toString();
                TextView overViewTV = (TextView) view.findViewById(R.id.overviewTV);
                overViewTV.setText(overview);
                voteAverage = obj.optString("vote_average").toString();
                voteAverage += "/10";
                TextView voteAverageTV = (TextView) view.findViewById(R.id.voteAverageTV);
                voteAverageTV.setText(voteAverage);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.detailMovie);
            linearLayout.setVisibility(View.INVISIBLE);
        }

//        else if(intent==null){
//            movieName="Try again";
//            TextView orTV =
//                    ((TextView) view.findViewById(R.id.movieName));
//            orTV.setText(movieName);
//        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        FetchVideos fetchVideos = new FetchVideos();
        fetchVideos.execute(id);
        FetchReviews fetchReviews =new FetchReviews();
        fetchReviews.execute(id);

     //   Log.v("Youtube",videos[0]);
    }

    public class FetchVideos extends AsyncTask<String,Void , String[][]>{
        public String[][] getDataFromJson (String videoStr) throws JSONException{
            JSONObject jsonObject=new JSONObject(videoStr);
            JSONArray jsonArray=jsonObject.optJSONArray("results");

            String[][] resultStrs = new String[jsonArray.length()][2];
            for(int i=0 ; i<jsonArray.length(); i++){
                child=jsonArray.getJSONObject(i);
                resultStrs[i][0]="http://www.youtube.com/watch?v=";
                resultStrs[i][0]+=child.optString("key").toString();
                resultStrs[i][1]=child.optString("name").toString();
            }
            return resultStrs;
        }
        @Override
        protected String[][] doInBackground(String... params) {
            String videoStr;
            if (params.length == 0) {
                return null;
            } HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try{
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
                final String APPID_PARAM = "api_key";

                Uri builtUri= Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath("videos")
                        .appendQueryParameter(APPID_PARAM, MainActivityFragment.api_key)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("Link Video",url.toString());
                // Create the request to MovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                videoStr = buffer.toString();
            }catch (IOException e){
                Log.v("IOException", "Async task Fetch Video");
                return null;
            }
            try {
                return getDataFromJson(videoStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s[][]) {
            super.onPostExecute(s);
            if (s!=null) {
                videos =new String[s.length];
                name=new String[s.length];
                for (int i=0; i<s.length; i++){
                    videos[i]= s[i][0];
                    name[i]=s[i][1];
                    videoAdapter.add(String.valueOf(name[i]));


                }
                videoAdapter.addUrl(videos);
                Log.v("URLS", videos[0]);
                listView.setAdapter(videoAdapter);
            }

        }
    }
    public class FetchReviews  extends AsyncTask<String,Void , String[]>{
        public String[] getDataFromJson (String videoStr) throws JSONException{
            JSONObject jsonObject=new JSONObject(videoStr);
            JSONArray jsonArray=jsonObject.optJSONArray("results");

            String[] resultStrs = new String[jsonArray.length()];
            for(int i=0 ; i<jsonArray.length(); i++){
                childR=jsonArray.getJSONObject(i);
                resultStrs[i]="Author : ";
                resultStrs[i]+=childR.optString("author").toString();
                resultStrs[i]+="\n";
                resultStrs[i]+=childR.optString("content").toString();
                resultStrs[i]+="\n";
            }
            return resultStrs;
        }
        @Override
        protected String[] doInBackground(String... params) {
            String videoStr;
            if (params.length == 0) {
                return null;
            } HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try{
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
                final String APPID_PARAM = "api_key";

                Uri builtUri= Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath("reviews")
                        .appendQueryParameter(APPID_PARAM, MainActivityFragment.api_key)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("Link Video",url.toString());
                // Create the request to MovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                videoStr = buffer.toString();
            }catch (IOException e){
                Log.v("IOException", "Async task Fetch Review");
                return null;
            }
            try {
                return getDataFromJson(videoStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s[]) {
            super.onPostExecute(s);
            if(s!=null) {
                for (int i = 0; i < s.length; i++) {
                    reviewAdapter.add(s[i]);
                }
            }
        }
    }
}
