package com.seawindsolution.meranews.Model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
public class NewsModel implements Serializable{
    private int news_id;
    private String news_id_for_url;
    private String title;
    private String short_content;
    private String meta_description;
    private String content;
    private int image;
    private String tw_image;
    private String published_on;
    @Expose
    private boolean isFavorite;

    public NewsModel(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getNews_id_for_url() {
        return news_id_for_url;
    }

    public void setNews_id_for_url(String news_id_for_url) {
        this.news_id_for_url = news_id_for_url;
    }

    public String getShort_content() {
        return short_content;
    }

    public void setShort_content(String short_content) {
        this.short_content = short_content;
    }

    public String getTw_image() {
        return tw_image;
    }

    public void setTw_image(String tw_image) {
        this.tw_image = tw_image;
    }

    public String getMeta_description() {
        return meta_description;
    }

    public void setMeta_description(String meta_description) {
        this.meta_description = meta_description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublished_on() {
        return published_on;
    }

    public void setPublished_on(String published_on) {
        this.published_on = published_on;
    }
}