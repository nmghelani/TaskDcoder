package com.example.dcodertask.dataSource;

import android.app.Application;
import android.util.Log;

import com.example.dcodertask.activity.MainActivity;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.repository.DataRepository;
import com.example.dcodertask.utils.AppMethods;
import com.example.dcodertask.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataItemDataSource extends PageKeyedDataSource<Integer, DataItem> {

    private static final String TAG = DataItemDataSource.class.getName();
    private final Application application;
    private final String query;
    private final Integer isProject;
    private final List<Integer> languageIds;
    private final APIService apiService;
    public static int MAX_PAGE = 10;
    public int FIRST_LOAD_CNT = 1;
    private final static int PAGE_SIZE = 10;
    private final DataRepository dataRepository;

    public DataItemDataSource(Application application, APIService apiService, String query, Integer isProject, List<Integer> languageIds) {
        this.application = application;
        this.apiService = apiService;
        this.query = query;
        this.isProject = isProject;
        if (languageIds == null || languageIds.isEmpty()) {
            languageIds = AppMethods.getLanguageIds();
        }
        this.languageIds = languageIds;
        dataRepository = new DataRepository(application);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, DataItem> callback) {

        if (MainActivity.isOnline.getValue()) {
            Log.d(TAG, "loadInitial: " + FIRST_LOAD_CNT);
            Call<DataResponse> call = apiService.getDataList(FIRST_LOAD_CNT);
            call.enqueue(new Callback<DataResponse>() {
                @Override
                public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                    DataResponse dataResponse = response.body();
                    if (dataResponse != null && dataResponse.getData() != null) {
                        MAX_PAGE = dataResponse.getPages();
                        ArrayList<DataItem> dataItemList = (ArrayList<DataItem>) dataResponse.getData();
                        Iterator<DataItem> itemIterator = dataItemList.iterator();
                        while (itemIterator.hasNext()) {
                            DataItem dataItem = itemIterator.next();
                            if ((query != null
                                    && !dataItem.getTitle().toLowerCase().contains(query.toLowerCase()) //For search
                                    && !dataItem.getDescription().toLowerCase().contains(query.toLowerCase()) //For search
                                    && !dataItem.getFile().toLowerCase().contains(query.toLowerCase()) //For search
                                    && !dataItem.getUsername().toLowerCase().contains(query.toLowerCase())) //For search
                                    || (isProject != null && ((isProject == Constants.TRUE && !dataItem.isProject()) || (isProject == Constants.FALSE && dataItem.isProject()))) //For project/files
                                    || (languageIds != null && !languageIds.contains(dataItem.getLanguageId())) //For languages
                            ) {
                                itemIterator.remove();
                            }
                        }
                        if (dataItemList.isEmpty()) {
                            FIRST_LOAD_CNT++;
                            loadInitial(params, callback);
                        } else {
                            callback.onResult(dataItemList, FIRST_LOAD_CNT, FIRST_LOAD_CNT + 1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<DataResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    callback.onResult(new ArrayList<>(), FIRST_LOAD_CNT, FIRST_LOAD_CNT + 1);
                }
            });
        } else {
            List<DataItem> dataItemList = dataRepository.getProjectsBySize(query, isProject, languageIds, 1, PAGE_SIZE);
            callback.onResult(dataItemList, null, 2);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, DataItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, DataItem> callback) {
        Log.d(TAG, "loadAfter: " + params.key + " " + params.requestedLoadSize);
        if (MainActivity.isOnline.getValue()) {
            if (params.key <= MAX_PAGE) {
                Call<DataResponse> call = apiService.getDataList(params.key);
                call.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        DataResponse dataResponse = response.body();
                        if (dataResponse != null && dataResponse.getData() != null) {
                            ArrayList<DataItem> dataItemList = (ArrayList<DataItem>) dataResponse.getData();
                            Iterator<DataItem> itemIterator = dataItemList.iterator();
                            while (itemIterator.hasNext()) {
                                DataItem dataItem = itemIterator.next();
                                if ((query != null
                                        && !dataItem.getTitle().toLowerCase().contains(query.toLowerCase()) //For search
                                        && !dataItem.getDescription().toLowerCase().contains(query.toLowerCase()) //For search
                                        && !dataItem.getFile().toLowerCase().contains(query.toLowerCase()) //For search
                                        && !dataItem.getUsername().toLowerCase().contains(query.toLowerCase())) //For search
                                        || (isProject != null && ((isProject == Constants.TRUE && !dataItem.isProject()) || (isProject == Constants.FALSE && dataItem.isProject()))) //For project/files
                                        || (languageIds != null && !languageIds.contains(dataItem.getLanguageId())) //For languages
                                ) {
                                    itemIterator.remove();
                                }
                            }
                            if (dataItemList.isEmpty()) {
                                loadAfter(new LoadParams<>(params.key + 1, params.requestedLoadSize), callback);
                            } else {
                                callback.onResult(dataItemList, params.key + 1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                        callback.onResult(new ArrayList<>(), params.key + 1);
                    }
                });
            }
        } else {
            List<DataItem> dataItemList = dataRepository.getProjectsBySize(query, isProject, languageIds, params.key, PAGE_SIZE);
            callback.onResult(dataItemList, params.key + 1);
        }
    }
}
