package com.example.dcodertask.model;

import com.google.gson.annotations.SerializedName;

public class Forks {

    @SerializedName("number")
    private int number;

    public Forks(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}