package com.example.dcodertask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.dcodertask.databinding.ItemDataBinding;
import com.example.dcodertask.model.DataItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PagedDataAdapter extends PagedListAdapter<DataItem, PagedDataAdapter.DataViewHolder> {

    private final Context mContext;

    public PagedDataAdapter(Context mContext) {
        super(CALLBACK);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDataBinding mItemDataBinding = ItemDataBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new DataViewHolder(mItemDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private ItemDataBinding mItemDataBinding;

        public DataViewHolder(@NonNull ItemDataBinding mItemDataBinding) {
            super(mItemDataBinding.getRoot());
            this.mItemDataBinding = mItemDataBinding;
        }

        public void bind(DataItem dataItem) {
            mItemDataBinding.tvTitle.setText(dataItem.getTitle());
        }
    }

    public static final DiffUtil.ItemCallback<DataItem> CALLBACK = new DiffUtil.ItemCallback<DataItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull DataItem oldItem, @NonNull DataItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DataItem oldItem, @NonNull DataItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}
