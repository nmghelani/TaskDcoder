package com.example.dcodertask.dialog;

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
import com.example.dcodertask.model.DataItem;
import com.example.dcodertask.utils.AppMethods;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class DetailedDialog extends Dialog {

    public DetailedDialog(@NonNull Context mContext, DataItem dataItem) {
        super(mContext);

        DgDetailsBinding detailsBinding = DgDetailsBinding.inflate(LayoutInflater.from(mContext));
        setContentView(detailsBinding.getRoot());
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.BottomDialogAnimation;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_details));
        }

        int radius = mContext.getResources().getDimensionPixelSize(R.dimen._10sdp);
        if (dataItem.isProject()) {
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

        detailsBinding.tvTitle.setText(dataItem.getTitle());
        detailsBinding.tvDescription.setText(dataItem.getDescription());
        detailsBinding.tvFileName.setText(dataItem.getFile());
        detailsBinding.tvLanguage.setText(AppMethods.getLanguage(dataItem.getLanguageId()));
        detailsBinding.tvStar.setText(String.valueOf(dataItem.getStars().getNumber()));
        detailsBinding.tvFork.setText(String.valueOf(dataItem.getForks().getNumber()));

        StringBuilder tagString = null;
        for (String tag : dataItem.getTagList()) {
            if (tagString == null) {
                tagString = new StringBuilder();
                tagString.append(tag);
            } else {
                tagString.append(", ").append(tag);
            }
        }

        if (tagString != null) {
            detailsBinding.tvTags.setText(tagString.toString());
            detailsBinding.tvTags.setVisibility(View.VISIBLE);
        } else {
            detailsBinding.tvTags.setVisibility(View.GONE);
        }

        detailsBinding.tvCreatedOn.setText(dataItem.getCreatedAt());
        detailsBinding.tvUsername.setText(dataItem.getUsername());
        detailsBinding.tvUpdatedOn.setText(dataItem.getUpdatedAt());
    }

}
