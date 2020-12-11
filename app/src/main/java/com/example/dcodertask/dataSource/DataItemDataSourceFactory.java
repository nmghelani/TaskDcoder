package com.example.dcodertask.dataSource;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.network.APIService;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class DataItemDataSourceFactory extends DataSource.Factory<Integer, DataItem> {
    private DataItemDataSource dataItemDataSource;
    private APIService apiService;
    private MutableLiveData<DataItemDataSource> mutableLiveData;

    public DataItemDataSourceFactory(APIService apiService) {
        this.apiService = apiService;
        mutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, DataItem> create() {
        dataItemDataSource = new DataItemDataSource(apiService);
        mutableLiveData.postValue(dataItemDataSource);
        return dataItemDataSource;
    }

    public MutableLiveData<DataItemDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
