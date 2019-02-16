package com.example.hessah.newsapp;

public class News {
    private String mTitle;
    private String mType;
    private String mDate;
    private String mSection;
    private String mAuthor;
    private String mUrl;

    public News(String title, String type, String date, String section,String author, String url) {
        this.mTitle = title;
        this.mType = type;
        this.mDate = date;
        this.mSection = section;
        this.mAuthor = author;
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getType() {
        return mType;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() { return mAuthor;  }

    public String getUrl() {
        return mUrl;
    }
}
