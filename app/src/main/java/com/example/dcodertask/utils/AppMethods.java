package com.example.dcodertask.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.example.dcodertask.R;
import com.example.dcodertask.databinding.DgProgressBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class AppMethods {

    private static final String TAG = AppMethods.class.getName();
    private static SparseArray<String> languages;
    private static Dialog progressDialog;

    static {
        if (languages == null) {
            languages = new SparseArray<>();
            languages.put(4, "Java");
            languages.put(7, "C++");
            languages.put(5, "Python2");
            languages.put(6, "C");
            languages.put(1035, "HTML");
            languages.put(1004, "Java");
            languages.put(1022, "Python3");
            languages.put(1007, "C++");
            languages.put(1002, "VB. NET");
            languages.put(1001, "CSharp");
            languages.put(1005, "Python2");
            languages.put(1021, "NodeJS");
            languages.put(1006, "C Key");
            languages.put(1020, "SCHEME");
            languages.put(24, "Python3");
            languages.put(400, "HTML");
            languages.put(21, "Scala");
        }
    }

    public static SparseArray<String> getLanguages() {
        return languages;
    }

    public static List<Integer> getLanguageIds() {
        ArrayList<Integer> languageIds = new ArrayList<>();
        for (int i = 0; i < languages.size(); i++) {
            languageIds.add(languages.keyAt(i));
        }
        return languageIds;
    }

    public static String getLanguage(int language_id) {
        return languages.get(language_id);
    }

    public static boolean isNetworkEnabled(Context mContext) {
        if (mContext == null)
            return false;
        try {
            ConnectivityManager connectManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectManager.getActiveNetworkInfo();
            if (netInfo != null) {
                if (netInfo.isConnected()) {
                    Log.e("true", "isNetworkEnabled: ");
                    return true;
                }
            }
            Log.e("false", "isNetworkEnabled: ");
        } catch (Exception ex) {
            Log.e("false", "isNetworkEnabled: " + ex.getMessage());
        }
        return false;
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

    public static boolean hideKeyboard(Context mContext) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm == null)
                return false;
            View view = ((Activity) mContext).getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return true;
            }
        } catch (Exception ex) {
            Log.e(TAG, "hideKeyboard: " + ex.getMessage());
        }
        return false;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.equals("");
    }
}
