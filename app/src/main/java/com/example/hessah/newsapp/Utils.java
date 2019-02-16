package com.example.hessah.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    public static ArrayList<News> fetchWeatherData(URL requestUrl){
         String jsonResponse = null;
        try
        {
            jsonResponse = makehttpRequest(requestUrl);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error in making http request",e);
        }
        ArrayList<News> result = extractNews(jsonResponse);
        return result;
    }
    private static URL createUrl(String stringUrl){
        URL url = null;
        try
        {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error in Creating URL",e);
        }
        return url;
    }

    private static String makehttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error in connection!! Bad Response ");
            }

        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;

    }
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static ArrayList<News> extractNews(String NewsJSON){
        if (TextUtils.isEmpty(NewsJSON)) {
            return null;
        }
        ArrayList<News>  newsData = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(NewsJSON);
            JSONObject response = reader.getJSONObject("response");
            JSONArray resultArray = response.getJSONArray("results");
            for(int i=0; i< resultArray.length(); i++) {
                JSONObject news = resultArray.getJSONObject(i);
                String title = news.getString("webTitle");
                String type = news.getString("type");
                String date = news.getString("webPublicationDate");
                String section = news.getString("sectionName");
                JSONArray tags = news.getJSONArray("tags");
                final int tagArrayLength = tags.length();
                String Author = "", firstName= "" , lastName = "" ;
                if(tagArrayLength > 0) {
                    for (int x = 0; x < tagArrayLength; x++) {
                        JSONObject tag = tags.getJSONObject(x);
                        firstName = tag.getString("firstName");
                        lastName = tag.getString("lastName");
                        Author = firstName + " " + lastName;
                    }
                }
                String url =  news.getString("webUrl");
                News n = new News(title, type, date, section, Author, url);
                newsData.add(n);
            }
        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the news JSON results", e);
        }

        return newsData;
    }

}
