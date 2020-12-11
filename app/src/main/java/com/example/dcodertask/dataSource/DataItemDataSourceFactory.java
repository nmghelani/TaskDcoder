package com.example.dcodertask.dataSource;

import android.app.Application;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.network.APIService;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class DataItemDataSourceFactory extends DataSource.Factory<Integer, DataItem> {
    private final Application application;
    private DataItemDataSource dataItemDataSource;
    private APIService apiService;
    private MutableLiveData<DataItemDataSource> mutableLiveData;

    public DataItemDataSourceFactory(Application application, APIService apiService) {
        this.application = application;
        this.apiService = apiService;
        mutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, DataItem> create() {
        dataItemDataSource = new DataItemDataSource(application, apiService);
        mutableLiveData.postValue(dataItemDataSource);
        return dataItemDataSource;
    }

    public MutableLiveData<DataItemDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
