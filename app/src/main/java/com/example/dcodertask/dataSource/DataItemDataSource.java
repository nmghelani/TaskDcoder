package com.example.dcodertask.dataSource;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataItemDataSource extends PageKeyedDataSource<Integer, DataItem> {

    private APIService apiService;

    public DataItemDataSource(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, DataItem> callback) {

        Call<DataResponse> call = apiService.getDataList(1);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                DataResponse dataResponse = response.body();
                if (dataResponse != null && dataResponse.getData() != null) {
                    ArrayList<DataItem> dataItemList = (ArrayList<DataItem>) dataResponse.getData();
                    callback.onResult(dataItemList, null, 2);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, DataItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, DataItem> callback) {

        Call<DataResponse> call = apiService.getDataList(params.key);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                DataResponse dataResponse = response.body();
                if (dataResponse != null && dataResponse.getData() != null) {
                    ArrayList<DataItem> dataItemList = (ArrayList<DataItem>) dataResponse.getData();
                    callback.onResult(dataItemList, params.key + 1);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });

    }
}
