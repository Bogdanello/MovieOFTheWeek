package com.example.bogdan.movieoftheweek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private final String apiKey = "be9f344b2209aa8b56138a71d6393c6d";
    private final String imageUrl = "https://image.tmdb.org/t/p/w185";
    private List<String> list = new ArrayList<String>();
    //private ArrayAdapter<String> adapter;
    private List<String> imagesUrls = new ArrayList<String>();

    private List<Movie> movieList = new ArrayList<Movie>();
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView)findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/now_playing?api_key=be9f344b2209aa8b56138a71d6393c6d&language=en-US&page=1"
        , null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response)
            {

                try {
                    JSONArray array = response.getJSONArray("results");
                    for(int i=0;i<array.length();i++) {
                        JSONObject obj = array.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(obj.getString("original_title"));
                        movie.setThumbnailUrl(imageUrl + obj.getString("poster_path"));
                        movie.setId(obj.getString("id"));
                        movieList.add(movie);
                        //list.add(array.getJSONObject(i).getString("original_title"));
                        //imagesUrls.add(imageUrl + array.getJSONObject(i).getString("poster_path"));
                    }


                    //adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1, list);


                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "You clicked a movie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MovieActivity.class);

                intent.putExtra("id", movieList.get(position).getId());
                intent.putExtra("image_url", movieList.get(position).getThumbnailUrl());

                startActivity(intent);
            }
        });

    }
}
