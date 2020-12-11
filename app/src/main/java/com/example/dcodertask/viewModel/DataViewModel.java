package com.example.dcodertask.viewModel;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.repository.DataRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private DataRepository dataRepository;
    private LiveData<List<DataItem>> liveDataList;

    public DataViewModel() {
        dataRepository = new DataRepository();
        liveDataList = dataRepository.getLiveDataItemList();
    }

    public LiveData<List<DataItem>> getLiveDataList() {
        return liveDataList;
    }

}
