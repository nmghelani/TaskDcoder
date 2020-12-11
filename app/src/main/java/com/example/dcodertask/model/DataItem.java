package com.example.dcodertask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    private List<String> tagList;

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

    public boolean isProject() {
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

    public List<String> getTagList() {
        return tagList;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataItem)) return false;
        DataItem dataItem = (DataItem) o;
        return isProject == dataItem.isProject &&
                languageId == dataItem.languageId &&
                Objects.equals(forks, dataItem.forks) &&
                Objects.equals(createdAt, dataItem.createdAt) &&
                Objects.equals(file, dataItem.file) &&
                Objects.equals(description, dataItem.description) &&
                Objects.equals(id, dataItem.id) &&
                Objects.equals(stars, dataItem.stars) &&
                Objects.equals(title, dataItem.title) &&
                Objects.equals(tagList, dataItem.tagList) &&
                Objects.equals(updatedAt, dataItem.updatedAt) &&
                Objects.equals(username, dataItem.username);
    }
}