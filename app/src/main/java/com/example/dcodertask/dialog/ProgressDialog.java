package com.example.dcodertask.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.example.dcodertask.R;
import com.example.dcodertask.databinding.DgProgressBinding;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class ProgressDialog extends Dialog {

    private static ProgressDialog progressDialog;

    public ProgressDialog(@NonNull Context mContext, boolean isCancelable) {
        super(mContext);
        DgProgressBinding progressBinding;
        if (mContext instanceof Activity) {
            progressBinding = DgProgressBinding.inflate(((Activity) mContext).getLayoutInflater());
        } else {
            progressBinding = DgProgressBinding.inflate((LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE));
        }
        setContentView(progressBinding.getRoot());
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.transparent));
            window.setLayout(mContext.getResources().getDimensionPixelSize(R.dimen._120sdp), mContext.getResources().getDimensionPixelSize(R.dimen._120sdp));
        }
        setCancelable(isCancelable);
        Glide.with(mContext)
                .asGif()
                .circleCrop()
                .load(R.drawable.giphy)
                .into(progressBinding.ivLoader);
    }

    public static ProgressDialog getInstance(Context mContext) {
        return getInstance(mContext, true);
    }

    public static ProgressDialog getInstance(Context mContext, boolean isCancelable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext, isCancelable);
        }
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    @Override
    public void show() {
        if (progressDialog.isShowing())
            return;
        super.show();
    }
}
