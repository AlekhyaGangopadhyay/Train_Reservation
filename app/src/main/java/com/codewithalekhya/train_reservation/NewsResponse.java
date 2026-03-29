package com.codewithalekhya.train_reservation;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NewsResponse {
    @SerializedName("articles")
    public List<Article> articles;

    public static class Article {
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
    }
}