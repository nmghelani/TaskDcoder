package com.example.dcodertask.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dcodertask.localDatabase.DataItemDao;
import com.example.dcodertask.localDatabase.Project;
import com.example.dcodertask.localDatabase.ProjectDatabase;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class DataRepository {
    private final DataItemDao dataItemDao;
    private LiveData<List<Project>> mldDataItemList = new MutableLiveData<>();

    public DataRepository(Application application) {
        /*APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<DataResponse> data = apiService.getDataList(1);
        data.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                mldDataItemList.postValue(response.body().getData());
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });*/
        ProjectDatabase database = ProjectDatabase.getInstance(application);
        dataItemDao = database.dataItemDao();
        mldDataItemList = dataItemDao.getAllProjects();
    }

    public void insert(DataItem dataItem) {
        new InsertDataItemAsyncTask(dataItemDao).execute(dataItem);
    }

    public void update(DataItem dataItem) {
        new UpdateDataItemAsyncTask(dataItemDao).execute(dataItem);
    }

    public void delete(DataItem dataItem) {
        new DeleteDataItemAsyncTask(dataItemDao).execute(dataItem);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(dataItemDao).execute();
    }

    public List<DataItem> getProjects(String query, Integer isProject, List<Integer> languageIds) {
        try {
            return new GetProjects(dataItemDao).execute(query, isProject, languageIds).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<DataItem> getProjectsBySize(String query, Integer isProject, List<Integer> languageIds, int page, int size) {
        try {
            return new GetProjectsBySize(dataItemDao).execute(query, isProject, languageIds, page, size).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public LiveData<List<Project>> getLiveDataItemList() {
        return mldDataItemList;
    }

    public static class InsertDataItemAsyncTask extends AsyncTask<DataItem, Void, Void> {

        private final DataItemDao dataItemDao;

        public InsertDataItemAsyncTask(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected Void doInBackground(DataItem... dataItems) {
            dataItemDao.insert(getProject(dataItems[0]));
            return null;
        }
    }

    public static class UpdateDataItemAsyncTask extends AsyncTask<DataItem, Void, Void> {

        private final DataItemDao dataItemDao;

        public UpdateDataItemAsyncTask(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected Void doInBackground(DataItem... dataItems) {
            dataItemDao.update(getProject(dataItems[0]));
            return null;
        }
    }

    public static class DeleteDataItemAsyncTask extends AsyncTask<DataItem, Void, Void> {

        private final DataItemDao dataItemDao;

        public DeleteDataItemAsyncTask(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected Void doInBackground(DataItem... dataItems) {
            dataItemDao.delete(getProject(dataItems[0]));
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

    public static class GetProjects extends AsyncTask<Object, Void, List<DataItem>> {

        private final DataItemDao dataItemDao;

        public GetProjects(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected List<DataItem> doInBackground(Object... objects) {
            String query = objects[0] != null ? (String) objects[0] : "";
            Integer isProject = (Integer) objects[1];
            List<Integer> languageIds = (List<Integer>) objects[2];
            List<Project> projectList = dataItemDao.getProjects(query, isProject, languageIds != null && !languageIds.isEmpty() ? languageIds : AppMethods.getLanguageIds());
            List<DataItem> dataItemList = new ArrayList<>();
            for (Project project : projectList) {
                dataItemList.add(new DataItem(project));
            }
            return dataItemList;
        }
    }

    public static class GetProjectsBySize extends AsyncTask<Object, Void, List<DataItem>> {

        private final String TAG = this.getClass().getName();
        private final DataItemDao dataItemDao;

        public GetProjectsBySize(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected List<DataItem> doInBackground(Object... objects) {
            String query = objects[0] != null ? (String) objects[0] : "";
            Integer isProject = (Integer) objects[1];
            List<Integer> languageIds = (List<Integer>) objects[2];
            int page = (int) objects[3];
            int size = (int) objects[4];
            List<Project> projectList = dataItemDao.getProjectsBySize(query, isProject, languageIds != null && !languageIds.isEmpty() ? languageIds : AppMethods.getLanguageIds(), page, size);
            List<DataItem> dataItemList = new ArrayList<>();
            for (Project project : projectList) {
                dataItemList.add(new DataItem(project));
            }
            return dataItemList;
        }
    }

    public static Project getProject(DataItem dataItem) {
        StringBuilder tagString = null;
        for (String tag : dataItem.getTagList()) {
            if (tagString == null) {
                tagString = new StringBuilder();
                tagString.append(tag);
            } else {
                tagString.append(", ").append(tag);
            }
        }
        return new Project(
                dataItem.getCreatedAt(),
                dataItem.getFile(),
                dataItem.isProject(),
                dataItem.getDescription(),
                dataItem.getId(),
                dataItem.getLanguageId(),
                dataItem.getTitle(),
                dataItem.getUpdatedAt(),
                dataItem.getUsername(),
                dataItem.getStars().getNumber(),
                dataItem.getForks().getNumber(),
                tagString.toString()
        );
    }

}
