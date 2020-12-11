package com.example.dcodertask.dataSource;

import android.app.Application;
import android.util.Log;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.network.APIService;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataItemDataSource extends PageKeyedDataSource<Integer, DataItem> {

    private static final String TAG = DataItemDataSource.class.getName();
    private final Application application;
    private APIService apiService;
    public static int MAX_PAGE;

    public DataItemDataSource(Application application, APIService apiService) {
        this.application = application;
        this.apiService = apiService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, DataItem> callback) {

        Log.d(TAG, "loadInitial: ");
        Call<DataResponse> call = apiService.getDataList(1);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                DataResponse dataResponse = response.body();
                if (dataResponse != null && dataResponse.getData() != null) {
                    ArrayList<DataItem> dataItemList = (ArrayList<DataItem>) dataResponse.getData();
                    callback.onResult(dataItemList, null, 2);
                    MAX_PAGE = dataResponse.getPages();
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
        Log.d(TAG, "loadAfter: ");
        if (params.key <= MAX_PAGE) {
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
}
