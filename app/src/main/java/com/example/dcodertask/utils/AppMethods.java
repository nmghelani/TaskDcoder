package com.example.dcodertask.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.example.dcodertask.R;
import com.example.dcodertask.databinding.DgProgressBinding;

import androidx.core.content.ContextCompat;

public class AppMethods {

    public static SparseArray<String> languages;
    private static Dialog progressDialog;

    static {
        if (languages == null) {
            languages = new SparseArray<>();
            languages.put(4, "Java");
            languages.put(7, "Cpp");
            languages.put(5, "Python2");
            languages.put(6, "C");
            languages.put(1035, "HTML");
            languages.put(1004, "Java");
            languages.put(1022, "Python3");
            languages.put(1007, "CPP");
            languages.put(1002, "VB. NET");
            languages.put(1001, "CSharp");
            languages.put(1005, "Python2");
            languages.put(1021, "NodeJS");
            languages.put(1006, "C Key");
            languages.put(1020, "SCHEME");
            languages.put(24, "Python2");
            languages.put(400, "HTML");
            languages.put(21, "Scala");
        }
    }

    public static String getLanguage(int language_id) {
        return languages.get(language_id);
    }

    public static void showProgressDialog(Context mContext) {
        showProgressDialog(mContext, true);
    }

    public static void showProgressDialog(Context mContext, boolean isCancelable) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                progressDialog = new Dialog(mContext);
                DgProgressBinding progressBinding;
                if (mContext instanceof Activity) {
                    progressBinding = DgProgressBinding.inflate(((Activity) mContext).getLayoutInflater());
                } else {
                    progressBinding = DgProgressBinding.inflate((LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE));
                }
                progressDialog.setContentView(progressBinding.getRoot());
                Window window = progressDialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.transparent));
                    window.setLayout(mContext.getResources().getDimensionPixelSize(R.dimen._120sdp), mContext.getResources().getDimensionPixelSize(R.dimen._120sdp));
                }
                progressDialog.setCancelable(isCancelable);
                Glide.with(mContext)
                        .asGif()
                        .circleCrop()
                        .load(R.drawable.giphy)
                        .into(progressBinding.ivLoader);
                progressDialog.show();
            }
        });
    }

    public static void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
