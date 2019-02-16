package com.example.hessah.newsapp;


import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {
    URL mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        try{
            this.mUrl = new URL(url);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        return Utils.fetchWeatherData(mUrl);
    }
}