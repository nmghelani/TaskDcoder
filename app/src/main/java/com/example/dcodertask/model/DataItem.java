package com.example.dcodertask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItem {

    @SerializedName("forks")
    private Forks forks;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("file")
    private String file;

    @SerializedName("is_project")
    private boolean isProject;

    @SerializedName("description")
    private String description;

    @SerializedName("_id")
    private String id;

    @SerializedName("language_id")
    private int languageId;

    @SerializedName("stars")
    private Stars stars;

    @SerializedName("title")
    private String title;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("username")
    private String username;

    public Forks getForks() {
        return forks;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFile() {
        return file;
    }

    public boolean isIsProject() {
        return isProject;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public int getLanguageId() {
        return languageId;
    }

    public Stars getStars() {
        return stars;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }
}