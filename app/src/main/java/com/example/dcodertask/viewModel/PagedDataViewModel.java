package com.example.dcodertask.viewModel;

import com.example.dcodertask.dataSource.DataItemDataSource;
import com.example.dcodertask.dataSource.DataItemDataSourceFactory;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;
import com.example.dcodertask.repository.DataRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class PagedDataViewModel extends ViewModel {
    private Executor executor;
    private LiveData<PagedList<DataItem>> dataItemLiveList;

    public PagedDataViewModel() {

        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        DataItemDataSourceFactory dataItemDataSourceFactory = new DataItemDataSourceFactory(apiService);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
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
