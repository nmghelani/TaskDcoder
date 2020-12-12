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
import com.example.dcodertask.localDatabase.Project;
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private final Context mContext;
    private List<Project> dataItemList = new ArrayList<>();

    public DataAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void updateList(List<Project> newDataItemList) {
        dataItemList.addAll(newDataItemList);
        notifyDataSetChanged();
    }

    public void setList(List<Project> newDataItemList) {
        dataItemList = newDataItemList;
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

        private final ItemDataBinding mItemDataBinding;

        public DataViewHolder(@NonNull ItemDataBinding mItemDataBinding) {
            super(mItemDataBinding.getRoot());
            this.mItemDataBinding = mItemDataBinding;
        }

        public void bind(Project project) {
            mItemDataBinding.progressBar.setVisibility(View.GONE);
            mItemDataBinding.llContent.setVisibility(View.VISIBLE);

            mItemDataBinding.tvTitle.setText(project.getTitle());
            mItemDataBinding.tvDescription.setText(project.getDescription());
            mItemDataBinding.tvName.setText(project.getFile());
            mItemDataBinding.tvLanguage.setText(AppMethods.getLanguage(project.getLanguageId()));
            mItemDataBinding.tvStar.setText(String.valueOf(project.getNo_of_stars()));
            mItemDataBinding.tvFork.setText(String.valueOf(project.getNo_of_forks()));

            if (project.isProject()) {
                mItemDataBinding.ivType.setImageResource(R.drawable.project);
            } else {
                mItemDataBinding.ivType.setImageResource(R.drawable.file);
            }

            mItemDataBinding.tvDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dgDetails = new Dialog(mContext);
                    DgDetailsBinding detailsBinding = DgDetailsBinding.inflate(LayoutInflater.from(mContext));
                    dgDetails.setContentView(detailsBinding.getRoot());
                    Window window = dgDetails.getWindow();
                    if (window != null) {
                        window.getAttributes().windowAnimations = R.style.BottomDialogAnimation;
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.BOTTOM);
                        window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_details));
                    }
                    dgDetails.show();

                    int radius = mContext.getResources().getDimensionPixelSize(R.dimen._10sdp);
                    if (project.isProject()) {
                        Glide.with(mContext)
                                .load(R.drawable.project)
                                .transform(new CenterCrop(), new GranularRoundedCorners(radius, radius, 0, 0))
                                .into(detailsBinding.ivType);
                        detailsBinding.tvType.setText(mContext.getString(R.string.project_name));
                    } else {
                        Glide.with(mContext)
                                .load(R.drawable.file)
                                .transform(new CenterCrop(), new GranularRoundedCorners(radius, radius, 0, 0))
                                .into(detailsBinding.ivType);
                        detailsBinding.tvType.setText(mContext.getString(R.string.file_name));
                    }

                    detailsBinding.tvTitle.setText(project.getTitle());
                    detailsBinding.tvDescription.setText(project.getDescription());
                    detailsBinding.tvFileName.setText(project.getFile());
                    detailsBinding.tvLanguage.setText(AppMethods.getLanguage(project.getLanguageId()));
                    detailsBinding.tvStar.setText(String.valueOf(project.getNo_of_stars()));
                    detailsBinding.tvFork.setText(String.valueOf(project.getNo_of_forks()));

                    if (project.getTags() != null && !"".equals(project.getTags())) {
                        detailsBinding.tvTags.setText(project.getTags());
                        detailsBinding.tvTags.setVisibility(View.VISIBLE);
                    } else {
                        detailsBinding.tvTags.setVisibility(View.GONE);
                    }

                    detailsBinding.tvCreatedOn.setText(project.getCreatedAt());
                    detailsBinding.tvUsername.setText(project.getUsername());
                    detailsBinding.tvUpdatedOn.setText(project.getUpdatedAt());

                }
            });
        }
    }
}
