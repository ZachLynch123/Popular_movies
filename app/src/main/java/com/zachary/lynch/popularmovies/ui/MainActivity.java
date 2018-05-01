package com.zachary.lynch.popularmovies.ui;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextView;
    private MovieData[] mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTextView = findViewById(R.id.text);
        // check if the network is available
        ApiKey apiKey = new ApiKey();
        String movieUrl = "https://api.themoviedb.org/3/discover/movie?api_key=" +
                "5065b430c0db30e31daa59f500647254" + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
        if (isNetworkAvailable()){
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().
                    url(movieUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText("Failed");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        String jsonData = response.body().string();
                        if (response.isSuccessful()){
                            Log.v(TAG, "From JSON" + jsonData);
                        }
                    } catch (IOException e){
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }
            });

        } else {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
            mTextView.setText("Different Issue");
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }
}