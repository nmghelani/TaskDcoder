package com.example.dcodertask.viewModel;

import android.util.Log;

import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.network.APIService;
import com.example.dcodertask.network.RetroInstance;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataViewModel extends ViewModel {

    private static final String TAG = DataViewModel.class.getName();
    private MutableLiveData<DataResponse> mldDataResponse;

    public DataViewModel() {
        mldDataResponse = new MutableLiveData<>();
    }

    public MutableLiveData<DataResponse> getMutableLiveDataResponse() {
        return mldDataResponse;
    }

    public void makeApiCall(int pageId) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<DataResponse> call = apiService.getDataList(pageId);
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                mldDataResponse.postValue(response.body());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                mldDataResponse.postValue(null);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
