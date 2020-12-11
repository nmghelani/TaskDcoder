package com.example.dcodertask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.dcodertask.databinding.ItemDataBinding;
import com.example.dcodertask.model.DataItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private final Context mContext;
    private List<DataItem> dataItemList = new ArrayList<>();

    public DataAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void updateList(List<DataItem> newDataItemList) {
        dataItemList.addAll(newDataItemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDataBinding mItemDataBinding = ItemDataBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new DataViewHolder(mItemDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.bind(dataItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private ItemDataBinding mItemDataBinding;

        public DataViewHolder(@NonNull ItemDataBinding mItemDataBinding) {
            super(mItemDataBinding.getRoot());
            this.mItemDataBinding = mItemDataBinding;
        }

        public void bind(DataItem dataItem) {
//            mItemDataBinding.tvTitle.setText(dataItem.getTitle());
        }
    }
}
