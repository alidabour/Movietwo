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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivityFragment extends Fragment {
    final static String api_key = "Your Key";
    String sort_by = "popularity.desc";
    String poster_path[];
    JSONArray jsonArray;
    JSONObject child;
    FetchURL fetchURL;
    View view;
    GridView gridView;
    private ImageAdapter movieAdapter;
    private ImageAdapter imageAdapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                 view =  inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_view);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            startActivity(new Intent(getActivity(),SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
                    SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortType = sharedPreferences.getString(
                    getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popular)
            );
            if(sortType.equals(getString(R.string.pref_sort_popular))){
                sort_by=getString(R.string.pref_sort_popular);
                fetchURL=new FetchURL();
                fetchURL.execute(sort_by);
                gridView.setVisibility(View.VISIBLE);

            }else if (sortType.equals(getString(R.string.pref_sort_highRated))){
                sort_by=getString(R.string.pref_sort_highRated);
                fetchURL=new FetchURL();
                fetchURL.execute(sort_by);
                gridView.setVisibility(View.VISIBLE);

            }
            else //if(sortType.equals("favorites"))
                 {
                JSONObject child;
                     String sharedPreferencesString;
                     sharedPreferencesString=sharedPreferences.getString("path", "");
                     Log.v("SharedP","Hey+" + sharedPreferencesString);

                     if(sharedPreferencesString.length()>1) {
                    Log.v("SharedP",sharedPreferencesString);
                    String paths = "{\"results\":[" + sharedPreferencesString.substring(0, sharedPreferencesString.length() - 1) + "]}";
                    Log.v("S Paths", paths);
                    int total = sharedPreferences.getInt("total", 0);

                    String path_url[] = new String[total];
                    poster_path = new String[total];
                    path_url[0] = "";
                    int j = 0;
                    jsonArray = new JSONArray();
                    Log.v("JsonArray length 1", String.valueOf(jsonArray.length()));
                    Log.v(" path_url.length", String.valueOf(paths.length()));
                    try {
                        JSONObject jsonObject = new JSONObject(paths);
                        jsonArray = jsonObject.optJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.v("JsonArray length", String.valueOf(jsonArray.length()));
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            child = jsonArray.getJSONObject(i);
                            poster_path[i] = "http://image.tmdb.org/t/p/w185/";
                            poster_path[i] += child.optString("poster_path");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    gridView.clearChoices();
                    imageAdapter = new ImageAdapter(getActivity(), poster_path);
                    gridView.setAdapter(null);
                    gridView.setAdapter(imageAdapter);
                }
                     else {
                         gridView.setVisibility(View.INVISIBLE);
                     }
            }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try { ((Callback) getActivity()).onItemSelected(
                        jsonArray.getJSONObject(position).toString()
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(String jsonArrayCB);
    }

    public class FetchURL extends AsyncTask<String, Void, String[]> {
        private String[] getDataFromJson(String movieStr) throws JSONException{
            JSONObject movieJson = new JSONObject(movieStr);
            jsonArray=movieJson.optJSONArray("results");

            String[] resultStrs = new String[jsonArray.length()];
            for (int i=0; i<jsonArray.length(); i++){
                child =jsonArray.getJSONObject(i);
                resultStrs[i]="http://image.tmdb.org/t/p/w185/";
                resultStrs[i]+=child.optString("poster_path").toString();
                Log.v("Poster", resultStrs[i]);

            }
            return resultStrs;
        }
        @Override
        protected String[] doInBackground(String... params){
            String movieStr;
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try{
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORT = "sort_by";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT, params[0])
                        .appendQueryParameter(APPID_PARAM, api_key)
                        .build();
               URL url = new URL(builtUri.toString());
                Log.v("Link",url.toString());
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
                movieStr = buffer.toString();
            }catch (IOException e){
                Log.v("IOException", "Async task FetchURL");
                return null;
            }
            try {
                return getDataFromJson(movieStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s[]) {
            if(s!=null){
            super.onPostExecute(s);
            poster_path=s;
            movieAdapter=new ImageAdapter(getActivity(),poster_path);
            gridView.setAdapter(movieAdapter);

        }}
    }
  /*  public class FetchURL {
        FetchURL(){
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String SORT = "sort_by";
            final String APPID_PARAM = "api_key";
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortType = sharedPreferences.getString(
                    getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popular)
            );
            if(sortType.equals(getString(R.string.pref_sort_popular))){
                sort_by=getString(R.string.pref_sort_popular);
            }else if (sortType.equals(getString(R.string.pref_sort_highRated))){
                sort_by=getString(R.string.pref_sort_highRated);
            }


            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT, sort_by)
                    .appendQueryParameter(APPID_PARAM, api_key)
                    .build();
            url = builtUri.toString();
            Log.v("URL", url);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                jsonArray = response.optJSONArray("results");
                                for (int i=0 ; i<jsonArray.length(); i++) {
                                    child = jsonArray.getJSONObject(i);
                                    poster_path[i]="http://image.tmdb.org/t/p/w185/";
                                    poster_path[i]+=child.optString("poster_path").toString();
                                    Log.v("Poster", poster_path[i]);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //  Log.v("test",test);
                            gridView.setAdapter(movieAdapter);

                        }
                    }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(jsonObjectRequest);
            //jsonObjectRequest.setShouldCache(true);
        }
    }*/
}
