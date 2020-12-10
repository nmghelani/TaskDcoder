package com.example.dcodertask.network;

import com.example.dcodertask.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("data/{id}")
    Call<DataResponse> getDataList(@Path("id") int pageId);
}
