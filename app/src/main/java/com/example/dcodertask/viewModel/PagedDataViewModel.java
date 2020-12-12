package com.example.dcodertask.viewModel;

import android.app.Application;

import com.example.dcodertask.dataSource.DataItemDataSource;
import com.example.dcodertask.dataSource.DataItemDataSourceFactory;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;
import com.example.dcodertask.utils.AppMethods;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class PagedDataViewModel extends AndroidViewModel {
    private final Application application;
    private Executor executor;
    private LiveData<PagedList<DataItem>> dataItemLiveList;

    public PagedDataViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void startLoading(String query, Integer isProject, List<Integer> languageIds) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setPrefetchDistance(6)
                .setPageSize(DataItemDataSource.MAX_PAGE)
                .build();

        DataItemDataSourceFactory dataItemDataSourceFactory = new DataItemDataSourceFactory(application, apiService, query, isProject, languageIds);

        executor = Executors.newFixedThreadPool(5);
        dataItemLiveList = (new LivePagedListBuilder<>(dataItemDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<DataItem>> getLiveDataPagedList() {
        return dataItemLiveList;
    }

}
