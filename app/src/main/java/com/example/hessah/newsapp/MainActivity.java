package com.example.hessah.newsapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    String USGS_URL = "https://content.guardianapis.com/search?";
    NewsAdapter mAdapter;
    ListView mListView;
    TextView mTextView;
    ProgressBar mProgressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list);
        mProgressBar = findViewById(R.id.progressBar);
        mTextView = findViewById(R.id.txtView);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            getSupportLoaderManager().initLoader(0, null , this).forceLoad();
        }else{
            mProgressBar.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(R.string.no_internet);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = mAdapter.getItem(i);
                Uri earthquakeUri = Uri.parse(news.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(webIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Context context = this;
    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String news_type = sharedPrefs.getString(
                getString(R.string.Key_pref),
                getString(R.string.default_Value));

        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //show-tags=contributor&api-key=48ef1edc-1dce-4986-899e-9e4dacb8467d";
        uriBuilder.appendQueryParameter("q", news_type);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "48ef1edc-1dce-4986-899e-9e4dacb8467d");

        return new NewsLoader(context, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {

        mAdapter = new NewsAdapter(this, data);

        if(data != null && !data.isEmpty()) {
            mListView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.GONE);

        }else{
            mTextView.setText(R.string.No_News);
        }
    }

    @Override
    public void onLoaderReset(Loader <ArrayList<News>> loader) {
            mAdapter.clear();
    }


}

