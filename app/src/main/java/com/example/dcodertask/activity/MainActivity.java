package com.example.dcodertask.activity;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dcodertask.R;
import com.example.dcodertask.adapter.PagedDataAdapter;
import com.example.dcodertask.databinding.ActivityMainBinding;
import com.example.dcodertask.databinding.DgFilterBinding;
import com.example.dcodertask.localDatabase.Project;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;
import com.example.dcodertask.utils.Constants;
import com.example.dcodertask.viewModel.DataViewModel;
import com.example.dcodertask.viewModel.PagedDataViewModel;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
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
    private PagedDataAdapter pagedDataAdapter;
    public static MutableLiveData<Boolean> isFilterApplied = new MutableLiveData<>(false), isOnline = new MutableLiveData<>(false);
    private Integer fltIsProject;
    private ArrayList<Integer> fltLanguageIds = new ArrayList<>();
    private String fltQuery;
    //    private static int currentPageId = 1, MAX_PAGE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        setTitle(R.string.main_activity_title);
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

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                isOnline.postValue(true);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                isOnline.postValue(false);
            }
        };
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }

        dataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(DataViewModel.class);

        isOnline.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean online) {
                reload();
            }
        });
        //endregion

        isFilterApplied.observe(this, new Observer<Boolean>() {
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
            }
        });

        mActivityMainBinding.tvClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFilterApplied.postValue(false);
                reload();
            }
        });
    }

    private void observeList() {
        observeList(null, null, null);
    }

    private void observeList(String query, Integer isProject, List<Integer> languageIds) {
        if (pagedDataViewModel == null) {
            pagedDataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PagedDataViewModel.class);
        }
        pagedDataViewModel.startLoading(query, isProject, languageIds);
        if (!pagedDataViewModel.getLiveDataPagedList().hasActiveObservers()) {
            pagedDataViewModel.getLiveDataPagedList().observe(this, new Observer<PagedList<DataItem>>() {
                @Override
                public void onChanged(PagedList<DataItem> dataItems) {
                    dataItemList = dataItems;
                    pagedDataAdapter = new PagedDataAdapter(mContext, dataViewModel);
                    pagedDataAdapter.submitList(dataItemList);
                    mActivityMainBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
                    mActivityMainBinding.rvData.setAdapter(pagedDataAdapter);
                }
            });
        }
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
                mActivityMainBinding.tvClearFilters.performClick();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                miFilter.setVisible(true);
                reload();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AppMethods.hideKeyboard(mContext);
                observeList(query, null, null);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void reload() {
        observeList();

        fltIsProject = null;
        fltLanguageIds.clear();
        isFilterApplied.postValue(false);
        fltQuery = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                Dialog dgFilter = new Dialog(mContext);
                DgFilterBinding filterBinding = DgFilterBinding.inflate(LayoutInflater.from(mContext));
                dgFilter.setContentView(filterBinding.getRoot());
                Window window = dgFilter.getWindow();
                if (window != null) {
                    window.getAttributes().windowAnimations = R.style.BottomDialogAnimation;
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                    if (fltLanguageIds != null && fltLanguageIds.contains(key)) {
                        materialCheckBox.setChecked(true);
                    }
                }

                CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (filterBinding.cbProject.isChecked() && !filterBinding.cbFiles.isChecked()) {
                            filterBinding.llProjectLanguages.setVisibility(View.VISIBLE);
                            filterBinding.llFilesLanguages.setVisibility(View.GONE);
                        } else if (!filterBinding.cbProject.isChecked() && filterBinding.cbFiles.isChecked()) {
                            filterBinding.llProjectLanguages.setVisibility(View.GONE);
                            filterBinding.llFilesLanguages.setVisibility(View.VISIBLE);
                        } else {
                            filterBinding.llProjectLanguages.setVisibility(View.VISIBLE);
                            filterBinding.llFilesLanguages.setVisibility(View.VISIBLE);
                        }
                    }
                };

                if (!AppMethods.isNullOrEmpty(fltQuery)) {
                    filterBinding.searchView.setQuery(fltQuery, false);
                }

                filterBinding.cbFiles.setOnCheckedChangeListener(onCheckedChangeListener);
                filterBinding.cbProject.setOnCheckedChangeListener(onCheckedChangeListener);

                filterBinding.cbProject.setChecked(fltIsProject == null || fltIsProject == Constants.TRUE);
                filterBinding.cbFiles.setChecked(fltIsProject == null || fltIsProject == Constants.FALSE);

                filterBinding.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isFilterApplied.postValue(true);
                        dgFilter.dismiss();
                        fltIsProject = null;
                        if (filterBinding.cbProject.isChecked() && !filterBinding.cbFiles.isChecked()) {
                            fltIsProject = 1;
                        } else if (!filterBinding.cbProject.isChecked() && filterBinding.cbFiles.isChecked()) {
                            fltIsProject = 0;
                        }

                        fltLanguageIds.clear();
                        int filesCnt = filterBinding.llFilesLanguages.getChildCount(), projectCnt = filterBinding.llProjectLanguages.getChildCount();
                        for (int fileInx = 0, projectInx = 0; (filesCnt >= projectCnt) ? fileInx < filesCnt : projectInx < projectCnt; fileInx++, projectInx++) {
                            MaterialCheckBox materialCheckBox;
                            if (fileInx < filesCnt) {
                                View view = filterBinding.llFilesLanguages.getChildAt(fileInx);
                                if (view instanceof MaterialCheckBox) {
                                    materialCheckBox = (MaterialCheckBox) view;
                                    if (materialCheckBox.isChecked()) {
                                        fltLanguageIds.add(materialCheckBox.getId());
                                    }
                                }
                            }
                            View view = filterBinding.llProjectLanguages.getChildAt(projectInx);
                            if (view instanceof MaterialCheckBox) {
                                materialCheckBox = (MaterialCheckBox) view;
                                if (materialCheckBox.isChecked()) {
                                    fltLanguageIds.add(materialCheckBox.getId());
                                }
                            }
                        }

                        fltQuery = filterBinding.searchView.getQuery().toString();
                        observeList(fltQuery, fltIsProject, fltLanguageIds);
                    }
                });
                dgFilter.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}