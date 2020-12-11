package com.example.dcodertask.viewModel;

import android.app.Application;

import com.example.dcodertask.dataSource.DataItemDataSourceFactory;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class PagedDataViewModel extends AndroidViewModel {
    private Executor executor;
    private LiveData<PagedList<DataItem>> dataItemLiveList;

    public PagedDataViewModel(@NonNull Application application) {
        super(application);
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        DataItemDataSourceFactory dataItemDataSourceFactory = new DataItemDataSourceFactory(application, apiService);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(2)
                .setPrefetchDistance(4)
                .build();

        executor = Executors.newFixedThreadPool(5);
        dataItemLiveList = (new LivePagedListBuilder<>(dataItemDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();

    }

    public LiveData<PagedList<DataItem>> getLiveDataPagedList() {
        return dataItemLiveList;
    }

}
