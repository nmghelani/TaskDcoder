package com.example.dcodertask.viewModel;

import android.app.Application;

import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.repository.DataRepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

public class PagedDataViewModel extends AndroidViewModel {
    private final Application application;
    private DataRepository dataRepository;

    public PagedDataViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void startLoading() {
        dataRepository = DataRepository.getInstance(application);
        dataRepository.startLoading();
    }

    public MutableLiveData<Boolean> getIsFilterApplied() {
        return dataRepository.getIsFilterApplied();
    }

    public LiveData<PagedList<DataItem>> getLiveDataPagedList() {
        return dataRepository.getDataItemLiveList();
    }

    public void resetFilterVariable() {
        dataRepository.resetFilterVariable();
    }

    public Integer getFltIsProject() {
        return dataRepository.getFltIsProject();
    }

    public void addFltLanguage(int languageIds) {
        dataRepository.addFltLanguage(languageIds);
    }

    public void clearFltLanguage() {
        dataRepository.clearFltLanguage();
    }

    public void setFltIsProject(Integer fltIsProject) {
        dataRepository.setFltIsProject(fltIsProject);
    }

    public ArrayList<Integer> getFltLanguageIds() {
        return dataRepository.getFltLanguageIds();
    }

    public String getFltQuery() {
        return dataRepository.getFltQuery();
    }

    public void setFltQuery(String fltQuery) {
        dataRepository.setFltQuery(fltQuery);
    }

    public void reload() {
        dataRepository.reload();
    }
}
