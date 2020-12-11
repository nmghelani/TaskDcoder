package com.example.dcodertask.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.dcodertask.R;
import com.example.dcodertask.adapter.DataAdapter;
import com.example.dcodertask.adapter.PagedDataAdapter;
import com.example.dcodertask.databinding.ActivityMainBinding;
import com.example.dcodertask.databinding.DgFilterBinding;
import com.example.dcodertask.localDatabase.Project;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;
import com.example.dcodertask.viewModel.DataViewModel;
import com.example.dcodertask.viewModel.PagedDataViewModel;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding mActivityMainBinding;
    private Context mContext;
    private DataViewModel dataViewModel;
    private PagedDataViewModel pagedDataViewModel;
    private PagedList<DataItem> dataItemList;
    private List<Project> projectList;
    private DataAdapter dataAdapter;
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

        dataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(DataViewModel.class);
        if (AppMethods.isNetworkEnabled(mContext)) {
            pagedDataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PagedDataViewModel.class);
            pagedDataViewModel.getLiveDataPagedList().observe(this, new Observer<PagedList<DataItem>>() {
                @Override
                public void onChanged(PagedList<DataItem> dataItems) {
                    dataItemList = dataItems;
                    PagedDataAdapter pagedDataAdapter = new PagedDataAdapter(mContext, dataViewModel);
                    pagedDataAdapter.submitList(dataItemList);
                    mActivityMainBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
                    mActivityMainBinding.rvData.setAdapter(pagedDataAdapter);
                }
            });
        } else {
            dataAdapter = new DataAdapter(mContext);
            mActivityMainBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
            mActivityMainBinding.rvData.setAdapter(dataAdapter);

            dataViewModel.getLiveDataList().observe(this, new Observer<List<Project>>() {
                @Override
                public void onChanged(List<Project> dataItemList) {
                    projectList = dataItemList;
                    if (dataAdapter != null) {
                        dataAdapter.updateList(projectList);
                    }
                }
            });
        }
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (dataAdapter != null) {
                    Log.d(TAG, "onQueryTextSubmit: ");
                    projectList = dataViewModel.getProjectsByTitle(query);
                    dataAdapter.submitList(projectList);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (dataAdapter != null && "".equals(newText)) {
                    Log.d(TAG, "onQueryTextChange: ");
                    projectList = dataViewModel.getProjectsByTitle("");
                    dataAdapter.submitList(projectList);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                Dialog dgFilter = new Dialog(mContext);
                DgFilterBinding filterBinding = DgFilterBinding.inflate(LayoutInflater.from(mContext));
                dgFilter.setContentView(filterBinding.getRoot());
                Window window = dgFilter.getWindow();
                if (window != null) {
                    window.getAttributes().windowAnimations = R.style.BottomDialogAnimation;
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);
                    window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_details));
                }
                SparseArray<String> languages = AppMethods.getLanguages();
                for (int i = 0; i < languages.size(); i++) {
                    int key = languages.keyAt(i);
                    MaterialCheckBox materialCheckBox = new MaterialCheckBox(mContext);
                    materialCheckBox.setId(key);
                    materialCheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    materialCheckBox.setText(languages.get(key));
                    if (key < 1000) {
                        filterBinding.llFilesLanguages.addView(materialCheckBox);
                    } else {
                        filterBinding.llProjectLanguages.addView(materialCheckBox);
                    }
                }
                filterBinding.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dgFilter.dismiss();
                        int isProject = 2;
                        if (filterBinding.cbProject.isChecked() && !filterBinding.cbFiles.isChecked()) {
                            isProject = 1;
                        } else if (!filterBinding.cbProject.isChecked() && filterBinding.cbFiles.isChecked()) {
                            isProject = 0;
                        }
                        List<Integer> languages = new ArrayList<>();
                        for (int i = 0; i < filterBinding.llFilesLanguages.getChildCount(); i++) {
                            View view = filterBinding.llFilesLanguages.getChildAt(i);
                            MaterialCheckBox materialCheckBox;
                            if (view instanceof MaterialCheckBox && (materialCheckBox = (MaterialCheckBox) view).isChecked()) {
                                languages.add(materialCheckBox.getId());
                            }
                        }
                        for (int i = 0; i < filterBinding.llProjectLanguages.getChildCount(); i++) {
                            View view = filterBinding.llProjectLanguages.getChildAt(i);
                            MaterialCheckBox materialCheckBox;
                            if (view instanceof MaterialCheckBox && (materialCheckBox = (MaterialCheckBox) view).isChecked()) {
                                languages.add(materialCheckBox.getId());
                            }
                        }
                        projectList = dataViewModel.getProjects(isProject, languages);
                        Log.d(TAG, "onClick: " + projectList);
                        dataAdapter.submitList(projectList);
                    }
                });
                dgFilter.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}