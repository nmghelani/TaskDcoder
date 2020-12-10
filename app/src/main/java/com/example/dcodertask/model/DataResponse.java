package com.example.dcodertask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponse {

    @SerializedName("pages")
    private int pages;

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("sucess")
    private boolean success;

    @SerializedName("message")
    private String message;

    public int getPages() {
        return pages;
    }

    public List<DataItem> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}