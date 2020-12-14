package com.example.dcodertask.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;

import com.example.dcodertask.R;
import com.example.dcodertask.databinding.DgFilterBinding;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public abstract class FilterDialog extends Dialog {

    protected final DgFilterBinding filterBinding;

    public FilterDialog(@NonNull Context mContext) {
        super(mContext);

        filterBinding = DgFilterBinding.inflate(LayoutInflater.from(mContext));
        setContentView(filterBinding.getRoot());
        designDialog(mContext);

        setCheckedChangeListener();
        initContent();

        filterBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick();
            }
        });
    }

    private void setCheckedChangeListener() {
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
        filterBinding.cbFiles.setOnCheckedChangeListener(onCheckedChangeListener);
        filterBinding.cbProject.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public abstract void initContent();

    public abstract void onOkClick();

    private void designDialog(Context mContext) {
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.BottomDialogAnimation;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_details));
        }
    }

}
