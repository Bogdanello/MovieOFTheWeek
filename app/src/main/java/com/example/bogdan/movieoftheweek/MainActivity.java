package com.example.bogdan.movieoftheweek;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final String apiKey = "be9f344b2209aa8b56138a71d6393c6d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new RetrieveMovieList().execute();
        //Toast.makeText(, response.body().toString(), Toast.LENGTH_LONG);
    }


    public class RetrieveMovieList extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream inputStream = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    RetrieveMovieList.this.cancel(true);
                }
            });
        }

        @Override
        protected void doInBackground(String... params) {

            String url_select = "http://yoururlhere.com";

            ArrayList<Pair<String, String>> param = new ArrayList<Pair<String, String>>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection

                URL url = new URL("http://some-server");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //conn.getContent();
                //HttpClient httpClient = new DefaultHttpClient();

                //HttpPost httpPost = new HttpPost(url_select);
                //httpPost.setEntity(new UrlEncodedFormEntity(param));
                //HttpResponse httpResponse = httpClient.execute(httpPost);
                //HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                //inputStream = httpEntity.getContent();
                inputStream = new ByteArrayInputStream(conn.getContent());
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }
        } // protected Void doInBackground(String... params)

        protected void onPostExecute(Void v) {
            //parse JSON data
            try {
                JSONArray jArray = new JSONArray(result);
                for(i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String name = jObject.getString("name");
                    String tab1_text = jObject.getString("tab1_text");
                    int active = jObject.getInt("active");

                } // End Loop
                this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        } // protected void onPostExecute(Void v)
    } //class MyAsyncTask extends AsyncTask<String, String, Void>
}
