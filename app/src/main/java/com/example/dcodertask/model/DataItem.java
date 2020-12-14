package com.example.dcodertask.model;

import com.example.dcodertask.localDatabase.DataConverter;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "DataItem")
public class DataItem {

    @TypeConverters(DataConverter.class)
    @SerializedName("forks")
    private ValueObject forks;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("file")
    private String file;

    @SerializedName("is_project")
    private boolean isProject;

    @SerializedName("description")
    private String description;

    @SerializedName("_id")
    @NonNull
    @PrimaryKey
    private String id;

    @SerializedName("language_id")
    private int languageId;

    @TypeConverters(DataConverter.class)
    @SerializedName("stars")
    private ValueObject stars;

    @SerializedName("title")
    private String title;

    @TypeConverters(DataConverter.class)
    @SerializedName("tags")
    private List<String> tagList;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("username")
    private String username;

    public void setForks(ValueObject forks) {
        this.forks = forks;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setProject(boolean project) {
        isProject = project;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public void setStars(ValueObject stars) {
        this.stars = stars;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ValueObject getForks() {
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

    public ValueObject getStars() {
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
                id.equals(dataItem.id) &&
                Objects.equals(stars, dataItem.stars) &&
                Objects.equals(title, dataItem.title) &&
                Objects.equals(tagList, dataItem.tagList) &&
                Objects.equals(updatedAt, dataItem.updatedAt) &&
                Objects.equals(username, dataItem.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forks, createdAt, file, isProject, description, id, languageId, stars, title, tagList, updatedAt, username);
    }

    public String getTags() {
        StringBuilder tagString = null;
        for (String tag : tagList) {
            if (tagString == null) {
                tagString = new StringBuilder();
                tagString.append(tag);
            } else {
                tagString.append(", ").append(tag);
            }
        }
        if (tagString == null) {
            tagString = new StringBuilder();
        }
        return tagString.toString();
    }
}