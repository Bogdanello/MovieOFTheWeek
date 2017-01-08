package com.example.bogdan.movieoftheweek;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bogdan on 1/8/2017.
 */

public class MovieActivity extends Activity{

    private final String apiKey = "be9f344b2209aa8b56138a71d6393c6d";
    private final String baseUrl = "https://api.themoviedb.org/3/movie/";
    //private final String imageUrl = "https://image.tmdb.org/t/p/w185";
    private String imageUrl;

    private Movie movie = new Movie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);
        final ImageView poster = (ImageView)findViewById(R.id.imageView);
        final TextView description = (TextView)findViewById(R.id.description);
        String newId;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newId = null;
            } else {
                newId = extras.getString("id");
                imageUrl = extras.getString("image_url");
            }
        }
        else
            newId = null;
        String movieUrl = baseUrl + newId + "?api_key=" + apiKey + "&language=en-US";
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, movieUrl
                , null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response)
            {

                try {
                    movie.setThumbnailUrl(imageUrl + response.getString("poster_path"));
                    movie.setDescription(response.getString("overview"));
                    description.setText(movie.getDescription());
                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        ImageRequest request = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        poster.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        AppController.getInstance().addToRequestQueue(movieReq);
        AppController.getInstance().addToRequestQueue(request);
        //Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_LONG).show();
    }

}
