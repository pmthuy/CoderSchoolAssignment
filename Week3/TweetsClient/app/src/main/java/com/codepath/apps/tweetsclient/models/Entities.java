package com.codepath.apps.tweetsclient.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by thuypm on 3/28/2016.
 */
public class Entities {
    @SerializedName("media")
    private List<Media> media;

    @SerializedName("urls")
    private List<URLs> urls;

    @SerializedName("hashtags")
    private List<HashTag> hashTags;

    public List<Media> getMedia() {
        return media;
    }

    public List<URLs> getUrls() {
        return urls;
    }

    public List<HashTag> getHashTags() {
        return hashTags;
    }

    public class Media {
        @SerializedName("media_url")
        private String url;

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "Media{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }

    public class URLs {
        @SerializedName("display_url")
        private String displayUrl;

        @SerializedName("url")
        private String url;

        public String getDisplayUrl() {
            return displayUrl;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "URLs{" +
                    "displayUrl='" + displayUrl + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class HashTag {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return "HashTag{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }
}
