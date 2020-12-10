package com.example.dcodertask.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.dcodertask.adapter.DataAdapter;
import com.example.dcodertask.databinding.ActivityMainBinding;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.model.DataResponse;
import com.example.dcodertask.viewModel.DataViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding mActivityMainBinding;
    private Context mContext;
    private List<DataItem> dataItemList = new ArrayList<>();
    private static int currentPageId = 1, MAX_PAGE = currentPageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        mContext = MainActivity.this;

        DataAdapter mDataAdapter = new DataAdapter(mContext, dataItemList);
        mActivityMainBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
        mActivityMainBinding.rvData.setAdapter(mDataAdapter);

        DataViewModel dataViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext, new ViewModelProvider.NewInstanceFactory()).get(DataViewModel.class);
        dataViewModel.getMutableLiveDataResponse().observe(this, new Observer<DataResponse>() {
            @Override
            public void onChanged(DataResponse dataResponse) {
                if (dataResponse != null) {
                    MAX_PAGE = dataResponse.getPages();
                    dataItemList.addAll(dataResponse.getData());
                    mDataAdapter.notifyDataSetChanged();
                }
            }
        });
        dataViewModel.makeApiCall(currentPageId);
        mActivityMainBinding.rvData.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPageId <= MAX_PAGE) {
                        Log.e(TAG, "onScrollStateChanged: load more");
                        dataViewModel.makeApiCall(++currentPageId);
                    }
                }
            }
        });
    }
}