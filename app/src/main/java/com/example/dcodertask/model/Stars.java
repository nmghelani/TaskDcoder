package com.example.dcodertask.model;

import com.google.gson.annotations.SerializedName;

public class Stars {

    @SerializedName("number")
    private final int number;

    public Stars(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}