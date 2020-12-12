package com.example.dcodertask.viewModel;

import android.app.Application;

import com.example.dcodertask.localDatabase.Project;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.repository.DataRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DataViewModel extends AndroidViewModel {
    private final DataRepository dataRepository;
    private final LiveData<List<Project>> liveDataList;

    public DataViewModel(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        liveDataList = dataRepository.getLiveDataItemList();
    }

    public LiveData<List<Project>> getLiveDataList() {
        return liveDataList;
    }

    public void insert(DataItem dataItem) {
        dataRepository.insert(dataItem);
    }

    public void update(DataItem dataItem) {
        dataRepository.update(dataItem);
    }

    public void delete(DataItem dataItem) {
        dataRepository.delete(dataItem);
    }

    public void deleteAll() {
        dataRepository.deleteAll();
    }

    public List<DataItem> getProjects(String query, Integer isProject, List<Integer> languages) {
        return dataRepository.getProjects(query, isProject, languages);
    }

}
