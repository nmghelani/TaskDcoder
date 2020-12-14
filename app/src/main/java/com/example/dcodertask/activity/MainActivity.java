package com.example.dcodertask.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.dcodertask.R;
import com.example.dcodertask.adapter.PagedDataAdapter;
import com.example.dcodertask.databinding.ActivityMainBinding;
import com.example.dcodertask.dialog.FilterDialog;
import com.example.dcodertask.dialog.ProgressDialog;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;
import com.example.dcodertask.utils.Constants;
import com.example.dcodertask.viewModel.PagedDataViewModel;
import com.google.android.material.checkbox.MaterialCheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding mActivityMainBinding;
    private Context mContext;
    private PagedDataViewModel pagedDataViewModel;
    private PagedDataAdapter pagedDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        setTitle(R.string.main_activity_title);
        mContext = MainActivity.this;

        ProgressDialog.getInstance(mContext, false);
        startListening();
        observeFilter();
        initListeners();
    }

    private void startListening() {
        if (pagedDataViewModel == null) {
            pagedDataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PagedDataViewModel.class);
        }
        pagedDataViewModel.startLoading();
        if (!pagedDataViewModel.getLiveDataPagedList().hasActiveObservers()) {
            pagedDataViewModel.getLiveDataPagedList().observe(this, new Observer<PagedList<DataItem>>() {
                @Override
                public void onChanged(PagedList<DataItem> dataItems) {
                    if (pagedDataAdapter == null) {
                        pagedDataAdapter = new PagedDataAdapter(mContext);
                        mActivityMainBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
                        mActivityMainBinding.rvData.setAdapter(pagedDataAdapter);
                    }
                    pagedDataAdapter.submitList(dataItems);
                }
            });
        }
    }

    private void initListeners() {
        mActivityMainBinding.tvClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagedDataViewModel.reload();
            }
        });
    }

    private void observeFilter() {
        pagedDataViewModel.getIsFilterApplied().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean applied) {
                if (applied) {
                    if (mActivityMainBinding.tvClearFilters.getVisibility() == View.GONE) {
                        mActivityMainBinding.tvClearFilters.setVisibility(View.VISIBLE);
                        mActivityMainBinding.tvClearFilters.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_in));
                    }
                } else {
                    if (mActivityMainBinding.tvClearFilters.getVisibility() == View.VISIBLE) {
                        mActivityMainBinding.tvClearFilters.setVisibility(View.GONE);
                        mActivityMainBinding.tvClearFilters.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_out));
                    }
                }
                startListening();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem miFilter = menu.findItem(R.id.action_filter);
        MenuItem miSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) miSearch.getActionView();
        searchView.setQueryHint("Type to search");
        miSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                miFilter.setVisible(false);
                pagedDataViewModel.reload();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                miFilter.setVisible(true);
                pagedDataViewModel.resetFilterVariable();
                startListening();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AppMethods.hideKeyboard(mContext);
                pagedDataViewModel.setFltQuery(query);
                startListening();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                FilterDialog filterDialog = new FilterDialog(mContext) {
                    @Override
                    public void initContent() {
                        SparseArray<String> languages = AppMethods.getLanguages();
                        for (int i = 0; i < languages.size(); i++) {
                            int key = languages.keyAt(i);
                            MaterialCheckBox materialCheckBox = new MaterialCheckBox(mContext);
                            materialCheckBox.setId(key);
                            materialCheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            materialCheckBox.setText(languages.get(key));
                            if (key < 1000) {
                                this.filterBinding.llFilesLanguages.addView(materialCheckBox);
                            } else {
                                filterBinding.llProjectLanguages.addView(materialCheckBox);
                            }
                            if (pagedDataViewModel.getFltLanguageIds() != null && pagedDataViewModel.getFltLanguageIds().contains(key)) {
                                materialCheckBox.setChecked(true);
                            }
                        }

                        if (!AppMethods.isNullOrEmpty(pagedDataViewModel.getFltQuery())) {
                            filterBinding.searchView.setQuery(pagedDataViewModel.getFltQuery(), false);
                        }

                        filterBinding.cbProject.setChecked(pagedDataViewModel.getFltIsProject() == null || pagedDataViewModel.getFltIsProject() == Constants.TRUE);
                        filterBinding.cbFiles.setChecked(pagedDataViewModel.getFltIsProject() == null || pagedDataViewModel.getFltIsProject() == Constants.FALSE);
                    }

                    @Override
                    public void onOkClick() {
                        pagedDataViewModel.getIsFilterApplied().postValue(true);
                        dismiss();
                        pagedDataViewModel.setFltIsProject(null);
                        if (filterBinding.cbProject.isChecked() && !filterBinding.cbFiles.isChecked()) {
                            pagedDataViewModel.setFltIsProject(1);
                        } else if (!filterBinding.cbProject.isChecked() && filterBinding.cbFiles.isChecked()) {
                            pagedDataViewModel.setFltIsProject(0);
                        }

                        pagedDataViewModel.clearFltLanguage();
                        int filesCnt = filterBinding.llFilesLanguages.getChildCount(), projectCnt = filterBinding.llProjectLanguages.getChildCount();
                        for (int fileInx = 0, projectInx = 0; (filesCnt >= projectCnt) ? fileInx < filesCnt : projectInx < projectCnt; fileInx++, projectInx++) {
                            MaterialCheckBox materialCheckBox;
                            if (fileInx < filesCnt) {
                                View view = filterBinding.llFilesLanguages.getChildAt(fileInx);
                                if (view instanceof MaterialCheckBox) {
                                    materialCheckBox = (MaterialCheckBox) view;
                                    if (materialCheckBox.isChecked()) {
                                        pagedDataViewModel.getFltLanguageIds().add(materialCheckBox.getId());
                                    }
                                }
                            }
                            View view = filterBinding.llProjectLanguages.getChildAt(projectInx);
                            if (view instanceof MaterialCheckBox) {
                                materialCheckBox = (MaterialCheckBox) view;
                                if (materialCheckBox.isChecked()) {
                                    pagedDataViewModel.getFltLanguageIds().add(materialCheckBox.getId());
                                }
                            }
                        }

                        pagedDataViewModel.setFltQuery(filterBinding.searchView.getQuery().toString());
                    }
                };

                filterDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}