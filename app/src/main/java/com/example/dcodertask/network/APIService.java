package com.example.dcodertask.network;

import com.example.dcodertask.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("data/{page}")
    Call<DataResponse> getDataList(@Path("page") int page);
}
