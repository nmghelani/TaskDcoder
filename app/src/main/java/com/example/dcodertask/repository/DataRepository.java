package com.example.dcodertask.repository;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {
    private MutableLiveData<List<DataItem>> mldDataItemList = new MutableLiveData<>();

    public DataRepository() {
    }

    public void loadData(int page) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<DataResponse> data = apiService.getDataList(page);
        data.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                mldDataItemList.postValue(response.body().getData());
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    public LiveData<List<DataItem>> getLiveDataItemList() {
        return mldDataItemList;
    }
}
