package com.example.dcodertask.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.dcodertask.localDatabase.DataItemDao;
import com.example.dcodertask.localDatabase.Project;
import com.example.dcodertask.localDatabase.ProjectDatabase;
import com.example.dcodertask.model.DataItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DataRepository {
    private DataItemDao dataItemDao;
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

    public List<Project> getProjectsByTitle(String titleQuery) {
        try {
            return new GetProjectByTitle(dataItemDao).execute(titleQuery).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Project> getProjects(int isProject, List<Integer> languages) {
        try {
            return new GetProjects(dataItemDao).execute(Collections.singletonList(isProject), languages).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public LiveData<List<Project>> getLiveDataItemList() {
        return mldDataItemList;
    }

    public static class InsertDataItemAsyncTask extends AsyncTask<DataItem, Void, Void> {

        private DataItemDao dataItemDao;

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

        private DataItemDao dataItemDao;

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

        private DataItemDao dataItemDao;

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

        private DataItemDao dataItemDao;

        public DeleteAllAsyncTask(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataItemDao.deleteAllProjects();
            return null;
        }
    }

    public static class GetProjectByTitle extends AsyncTask<String, Void, List<Project>> {

        private DataItemDao dataItemDao;

        public GetProjectByTitle(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected List<Project> doInBackground(String... strings) {
            return dataItemDao.getProjectsByTitle(strings[0]);
        }
    }

    public static class GetProjects extends AsyncTask<List<Integer>, Void, List<Project>> {

        private DataItemDao dataItemDao;

        public GetProjects(DataItemDao dataItemDao) {
            this.dataItemDao = dataItemDao;
        }

        @Override
        protected List<Project> doInBackground(List<Integer>... integers) {
            return dataItemDao.getProjects(integers[0].get(0), integers[1]);
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
