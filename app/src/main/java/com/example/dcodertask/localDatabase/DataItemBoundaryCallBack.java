package com.example.dcodertask.localDatabase;

import android.app.Application;
import android.util.Log;

import com.example.dcodertask.dialog.ProgressDialog;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;
import com.example.dcodertask.repository.DataRepository;
import com.example.dcodertask.utils.AppMethods;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataItemBoundaryCallBack extends PagedList.BoundaryCallback<DataItem> {

    private final String TAG = this.getClass().getName();
    private final APIService apiService;
    private final Application application;
    private DataRepository dataRepository;
    private static int MAX_PAGE = 10;
    private int page = 1;

    public DataItemBoundaryCallBack(Application application) {
        this.application = application;
        dataRepository = DataRepository.getInstance(application);
        apiService = RetroInstance.getRetroClient().create(APIService.class);
    }

    @Override
    public void onZeroItemsLoaded() {
        super.onZeroItemsLoaded();
        Log.d(TAG, "onZeroItemsLoaded: ");
        fetchData();
    }

    @Override
    public void onItemAtFrontLoaded(@NonNull DataItem itemAtFront) {
        Log.d(TAG, "onItemAtFrontLoaded: ");
        super.onItemAtFrontLoaded(itemAtFront);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull DataItem itemAtEnd) {
        super.onItemAtEndLoaded(itemAtEnd);
        Log.d(TAG, "onItemAtEndLoaded: ");
        if (page <= MAX_PAGE) {
            fetchData();
        }
    }

    private void fetchData() {
        Log.d(TAG, "fetchData: ");
        ProgressDialog.getInstance(application).show();
        Call<DataResponse> call = apiService.getDataList(page++);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                ProgressDialog.getInstance(application).hide();
                if (page == 2) {
                    dataRepository.deleteAll();
                }
                DataResponse dataResponse = response.body();
                if (dataResponse != null) {
                    MAX_PAGE = dataResponse.getPages();
                    dataRepository.insert(dataResponse.getData());
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                ProgressDialog.getInstance(application).hide();
            }
        });
    }
}
