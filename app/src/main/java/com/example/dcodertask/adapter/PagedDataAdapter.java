package com.example.dcodertask.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.dcodertask.R;
import com.example.dcodertask.databinding.DgDetailsBinding;
import com.example.dcodertask.databinding.ItemDataBinding;
import com.example.dcodertask.dialog.DetailedDialog;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PagedDataAdapter extends PagedListAdapter<DataItem, PagedDataAdapter.DataViewHolder> {

    private static final String TAG = PagedDataAdapter.class.getName();
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
        DataItem dataItem = getItem(position);
        if (dataItem == null) {
            holder.showProgress();
        } else {
            holder.bind(getItem(position));
        }
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private final ItemDataBinding mItemDataBinding;

        public DataViewHolder(@NonNull ItemDataBinding mItemDataBinding) {
            super(mItemDataBinding.getRoot());
            this.mItemDataBinding = mItemDataBinding;
        }

        public void showProgress() {
            mItemDataBinding.progressBar.setVisibility(View.VISIBLE);
            mItemDataBinding.llContent.setVisibility(View.GONE);
        }

        public void bind(DataItem dataItem) {

            mItemDataBinding.progressBar.setVisibility(View.GONE);
            mItemDataBinding.llContent.setVisibility(View.VISIBLE);

            mItemDataBinding.tvTitle.setText(dataItem.getTitle());
            mItemDataBinding.tvDescription.setText(dataItem.getDescription());
            mItemDataBinding.tvName.setText(dataItem.getFile());
            mItemDataBinding.tvLanguage.setText(AppMethods.getLanguage(dataItem.getLanguageId()));
            mItemDataBinding.tvStar.setText(String.valueOf(dataItem.getStars().getNumber()));
            mItemDataBinding.tvFork.setText(String.valueOf(dataItem.getForks().getNumber()));

            if (dataItem.isProject()) {
                mItemDataBinding.ivType.setImageResource(R.drawable.project);
            } else {
                mItemDataBinding.ivType.setImageResource(R.drawable.file);
            }

            mItemDataBinding.tvDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailedDialog detailedDialog = new DetailedDialog(mContext, dataItem);
                    detailedDialog.show();
                }
            });
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
