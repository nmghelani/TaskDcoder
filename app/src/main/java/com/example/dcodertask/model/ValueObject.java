package com.example.dcodertask.model;

import com.google.gson.annotations.SerializedName;

public class ValueObject {

    @SerializedName("number")
    private final int number;

    public ValueObject(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}