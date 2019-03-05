package com.lantingBletooth.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lantingBletooth.R;

/**
 * Created by wym on 2017/12/20.
 */

public class ProgressDialog {
    public Dialog mDialog;
    private AnimationDrawable animationDrawable = null;

    public ProgressDialog(Context context, String message) {
        try {
            Looper.prepare();
        } catch (Exception e) {
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_view, null);

        TextView text = (TextView) view.findViewById(R.id.progress_message);
        text.setText(message);
        ImageView loadingImage = (ImageView) view.findViewById(R.id.progress_view);
        loadingImage.setImageResource(R.drawable.loading_animation);
        animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
        animationDrawable.setOneShot(false);
        animationDrawable.start();

        mDialog = new Dialog(context, R.style.AlertDialogStyle);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public ProgressDialog(Context context, int imageRes, String message) {
        try {
            Looper.prepare();
        } catch (Exception e) {
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_view, null);

        TextView text = (TextView) view.findViewById(R.id.progress_message);
        text.setText(message);
        ImageView loadingImage = (ImageView) view.findViewById(R.id.progress_view);
        loadingImage.setImageResource(imageRes);
        mDialog = new Dialog(context, R.style.AlertDialogStyle);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public ProgressDialog show() {
        mDialog.show();
        return this;
    }

    public void show(int dismissMillis) {
        mDialog.show();
        dismiss(dismissMillis);
    }

    public ProgressDialog setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        mDialog.setCancelable(cancel);
        return this;
    }

    public void dismiss() {
        try {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
                if (animationDrawable != null) animationDrawable.stop();
            }
        } catch (Exception e) {
        }
    }

    public void dismiss(int delayMillis) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dismiss();
            }
        }, delayMillis);
    }
}
