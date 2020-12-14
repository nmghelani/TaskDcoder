package com.example.dcodertask.dataSource;

import android.app.Application;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class DataItemDataSourceFactory extends DataSource.Factory<Integer, DataItem> {
    private final Application application;
    private final String query;
    private final Integer isProject;
    private final List<Integer> languageIds;
    private DataItemDataSource dataItemDataSource;
    private final MutableLiveData<DataItemDataSource> mutableLiveData;

    public DataItemDataSourceFactory(Application application, String query, Integer isProject, List<Integer> languageIds) {
        this.application = application;
        this.query = query;
        this.isProject = isProject;
        this.languageIds = languageIds;
        mutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, DataItem> create() {
        dataItemDataSource = new DataItemDataSource(application, query, isProject, languageIds);
        mutableLiveData.postValue(dataItemDataSource);
        return dataItemDataSource;
    }

    public MutableLiveData<DataItemDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
