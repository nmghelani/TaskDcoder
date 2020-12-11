package com.example.dcodertask.localDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "project")
public class Project {

    @ColumnInfo(name = "createdAt")
    private String createdAt;

    @ColumnInfo(name = "file")
    private String file;

    @ColumnInfo(name = "isProject")
    private boolean isProject;

    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @PrimaryKey
    private String id;

    @ColumnInfo(name = "language_id")
    private int languageId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "updatedAt")
    private String updatedAt;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "stars")
    private int no_of_stars;

    @ColumnInfo(name = "forks")
    private int no_of_forks;

    @ColumnInfo(name = "tags")
    private String tags;

    public Project(String createdAt, String file, boolean isProject, String description, @NonNull String id, int languageId, String title, String updatedAt, String username, int no_of_stars, int no_of_forks, String tags) {
        this.createdAt = createdAt;
        this.file = file;
        this.isProject = isProject;
        this.description = description;
        this.id = id;
        this.languageId = languageId;
        this.title = title;
        this.updatedAt = updatedAt;
        this.username = username;
        this.no_of_stars = no_of_stars;
        this.no_of_forks = no_of_forks;
        this.tags = tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isProject() {
        return isProject;
    }

    public void setProject(boolean project) {
        isProject = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNo_of_stars() {
        return no_of_stars;
    }

    public void setNo_of_stars(int no_of_stars) {
        this.no_of_stars = no_of_stars;
    }

    public int getNo_of_forks() {
        return no_of_forks;
    }

    public void setNo_of_forks(int no_of_forks) {
        this.no_of_forks = no_of_forks;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}