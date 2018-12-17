package com.example.android.themoviecompanion.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.themoviecompanion.Movie;
import com.example.android.themoviecompanion.MovieRecyclerViewAdapter;
import com.example.android.themoviecompanion.R;
import com.example.android.themoviecompanion.Utils.HTTPConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity2 extends AppCompatActivity {
    // Declares the RecyclerView object, as well as an Adapter and Layout Manager
    // to handle list updating and row layout respectively.
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerAdapapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private List<Movie> movieList;
    private RequestQueue queue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        queue = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView) findViewById(R.id.displayRV);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        movieList = new ArrayList<>();

        /*final int capacity = 100;
        // fills a dummy array for testing
        ArrayList<String> myDataset = new ArrayList<>(100);
        for (int i = 0; i < capacity; i++){
            myDataset.add(Integer.toString(i+1));
        }*/

        movieList = getMovies("Search");
        // sets the adapter to display the contents on the RecyclerView.
        movieRecyclerAdapapter = new MovieRecyclerViewAdapter(this,movieList);
        recyclerView.setAdapter(movieRecyclerAdapapter);
        movieRecyclerAdapapter.notifyDataSetChanged();



    }

    public List<Movie> getMovies(String searchTerm) {
        movieList.clear();
        String url = "https://api.themoviedb.org/3/search/movie?api_key=eeab5da6854350c8bf390f554ae7f997&query=Batman";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray moviesArray = response.getJSONArray("results");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        // Creates a new movie item, and then rips the appropriate JSON Data
                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("title"));
                        movie.setYear("Year Released: " + movieObj.getString("release_date")
                        .substring(0,4));
                        movie.setPoster(HTTPConstants.baseImageURL + movieObj.getString("poster_path"));
                        movie.setOverview(movieObj.getString("overview"));


                        Log.d("Movies: ", movie.getTitle());
                        Log.d("Poster URL", movie.getPosterPath());
                        movieList.add(movie);


                    }
                    /**
                     * Very important!! Otherwise, we wont see anything being displayed.
                     */
                    movieRecyclerAdapapter.notifyDataSetChanged();//Important!!


                }catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception p) {
                    p.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

        return movieList;

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}
