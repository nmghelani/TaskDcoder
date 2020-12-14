package com.example.dcodertask.dataSource;

import android.app.Application;
import android.util.Log;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;
import com.example.dcodertask.repository.DataRepository;
import com.example.dcodertask.utils.AppMethods;
import com.example.dcodertask.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataItemDataSource extends PageKeyedDataSource<Integer, DataItem> {

    private static final String TAG = DataItemDataSource.class.getName();
    private final Application application;
    private final String query;
    private final Integer isProject;
    private final List<Integer> languageIds;
    private final APIService apiService;
    public static int MAX_PAGE = 10;
    private final static int PAGE_SIZE = 10;
    private final DataRepository dataRepository;

    public DataItemDataSource(Application application, String query, Integer isProject, List<Integer> languageIds) {
        this.application = application;
        this.query = query;
        this.isProject = isProject;
        if (languageIds == null || languageIds.isEmpty()) {
            languageIds = AppMethods.getLanguageIds();
        }
        this.languageIds = languageIds;
        dataRepository = DataRepository.getInstance(application);
        apiService = RetroInstance.getRetroClient().create(APIService.class);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, DataItem> callback) {

        Call<DataResponse> call = apiService.getDataList(1);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                DataResponse dataResponse = response.body();
                if (dataResponse != null && dataResponse.getData() != null) {
                    MAX_PAGE = dataResponse.getPages();
                    ArrayList<DataItem> dataItemList = (ArrayList<DataItem>) dataResponse.getData();
                    dataRepository.insert(dataItemList);
                    callback.onResult(dataItemList, 1, 2);
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, DataItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, DataItem> callback) {
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
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }
}
