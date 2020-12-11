package com.example.dcodertask.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.dcodertask.R;
import com.example.dcodertask.adapter.PagedDataAdapter;
import com.example.dcodertask.databinding.ActivityMainBinding;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.viewModel.PagedDataViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding mActivityMainBinding;
    private Context mContext;
    //    private DataViewModel dataViewModel;
    private PagedDataViewModel pagedDataViewModel;
    //    private static int currentPageId = 1, MAX_PAGE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        mContext = MainActivity.this;

        //region WITHOUT PAGING
        /*DataAdapter mDataAdapter = new DataAdapter(mContext);
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
        }, 4000, 4000);*/
        //endregion

        //region WITH PAGING
        //For AndroidViewModel ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        //For ViewModel new ViewModelProvider.NewInstanceFactory()
        pagedDataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PagedDataViewModel.class);
        pagedDataViewModel.getLiveDataPagedList().observe(this, new Observer<PagedList<DataItem>>() {
            @Override
            public void onChanged(PagedList<DataItem> dataItems) {
                PagedDataAdapter pagedDataAdapter = new PagedDataAdapter(mContext);
                pagedDataAdapter.submitList(dataItems);
                mActivityMainBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
                mActivityMainBinding.rvData.setAdapter(pagedDataAdapter);
            }
        });
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }
}