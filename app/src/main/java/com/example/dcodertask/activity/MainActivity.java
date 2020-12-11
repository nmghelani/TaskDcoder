package com.example.dcodertask.activity;

import android.content.Context;
import android.os.Bundle;

import com.example.dcodertask.adapter.DataAdapter;
import com.example.dcodertask.databinding.ActivityMainBinding;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.viewModel.DataViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding mActivityMainBinding;
    private Context mContext;
    private DataViewModel dataViewModel;
    private static int currentPageId = 1, MAX_PAGE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        mContext = MainActivity.this;

        DataAdapter mDataAdapter = new DataAdapter(mContext);
        mActivityMainBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
        mActivityMainBinding.rvData.setAdapter(mDataAdapter);

        dataViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DataViewModel.class);
        dataViewModel.getLiveDataList().observe(this, new Observer<List<DataItem>>() {
            @Override
            public void onChanged(List<DataItem> dataItemList) {
                mDataAdapter.updateList(dataItemList);
            }
        });
        dataViewModel.loadData(currentPageId);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentPageId < MAX_PAGE) {
                    dataViewModel.loadData(++currentPageId);
                    return;
                }
                this.cancel();
            }
        }, 4000, 4000);
    }
}