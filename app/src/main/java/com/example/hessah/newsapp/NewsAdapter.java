package com.example.hessah.newsapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News news = getItem(position);

        TextView txtNewsTitle = listItemView.findViewById(R.id.news_title);
        txtNewsTitle.setText(news.getTitle());

        TextView txtNewsType = listItemView.findViewById(R.id.news_type);
        txtNewsType.setText(news.getType());

        TextView txtNewsDate = listItemView.findViewById(R.id.news_date);
        txtNewsDate.setText(news.getDate());

        TextView txtNewsSection =listItemView.findViewById(R.id.news_section);
        txtNewsSection.setText(news.getSection());

        TextView txtAuthorName =listItemView.findViewById(R.id.author_name);
        txtAuthorName.setText(news.getAuthor());

        return listItemView;
    }
}