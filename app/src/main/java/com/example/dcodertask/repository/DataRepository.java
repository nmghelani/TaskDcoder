package com.example.dcodertask.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.dcodertask.dataSource.DataItemDataSource;
import com.example.dcodertask.localDatabase.DataItemBoundaryCallBack;
import com.example.dcodertask.localDatabase.DataItemDao;
import com.example.dcodertask.localDatabase.ProjectDatabase;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class DataRepository {
    private static DataRepository dataRepository;
    private static DataItemDao dataItemDao;
    private static DataItemBoundaryCallBack boundryCallback;
    private Executor executor;
    private LiveData<PagedList<DataItem>> dataItemLiveList;
    private MutableLiveData<Boolean> isFilterApplied = new MutableLiveData<>(false);
    private Integer fltIsProject;
    private final ArrayList<Integer> fltLanguageIds = new ArrayList<>();
    private String fltQuery;

    public DataRepository() {
    }

    public static DataRepository getInstance(Application application) {
        if (dataRepository == null) {
            ProjectDatabase database = ProjectDatabase.getInstance(application);
            dataRepository = new DataRepository();
            dataItemDao = database.dataItemDao();
            if (boundryCallback == null) {
                boundryCallback = new DataItemBoundaryCallBack(application);
            }
        }
        return dataRepository;
    }

    public DataItemDao getDataItemDao() {
        return dataItemDao;
    }

    public void insert(List<DataItem> dataItemList) {
        for (DataItem dataItem : dataItemList) {
            new InsertDataItemAsyncTask(dataItemDao).execute(dataItem);
        }
    }

    public void insert(DataItem dataItem) {
        new InsertDataItemAsyncTask(dataItemDao).execute(dataItem);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(dataItemDao).execute();
    }

    public void startLoading() {
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setPrefetchDistance(6)
                .setPageSize(DataItemDataSource.MAX_PAGE)
                .build();

        executor = Executors.newFixedThreadPool(5);
        dataItemLiveList = (new LivePagedListBuilder<>(dataRepository.getDataItemDao().getProjects(fltQuery != null ? fltQuery : "", fltIsProject, fltLanguageIds.isEmpty() ? AppMethods.getLanguageIds() : fltLanguageIds), config))
                .setBoundaryCallback(boundryCallback)
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<DataItem>> getDataItemLiveList() {
        return dataItemLiveList;
    }

    public MutableLiveData<Boolean> getIsFilterApplied() {
        return isFilterApplied;
    }

    public void resetFilterVariable() {
        fltIsProject = null;
        fltLanguageIds.clear();
        isFilterApplied.postValue(false);
        fltQuery = null;
    }

    public Integer getFltIsProject() {
        return fltIsProject;
    }

    public void addFltLanguage(int languageIds) {
        fltLanguageIds.add(languageIds);
    }

    public void clearFltLanguage() {
        fltLanguageIds.clear();
    }

    public void setFltIsProject(Integer fltIsProject) {
        this.fltIsProject = fltIsProject;
    }

    public ArrayList<Integer> getFltLanguageIds() {
        return fltLanguageIds;
    }

    public String getFltQuery() {
        return fltQuery;
    }

    public void setFltQuery(String fltQuery) {
        this.fltQuery = fltQuery;
    }

    public void reload() {
        resetFilterVariable();
        startLoading();
    }

    public static class InsertDataItemAsyncTask extends AsyncTask<DataItem, Void, Void> {

        private final DataItemDao dataItemDao;

        public InsertDataItemAsyncTask(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected Void doInBackground(DataItem... dataItems) {
            dataItemDao.insert(dataItems[0]);
            return null;
        }
    }

    public static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private final DataItemDao dataItemDao;

        public DeleteAllAsyncTask(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataItemDao.deleteAllProjects();
            return null;
        }
    }

}
