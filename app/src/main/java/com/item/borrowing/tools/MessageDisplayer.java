package com.item.borrowing.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MessageDisplayer {

    private AlertDialog.Builder builder;
    private Context context;
    private Intent intent;

    public MessageDisplayer(Context context, String title, String message, boolean canClose) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(canClose);
    }

    public MessageDisplayer(Context context, String title, String message, boolean canClose, Intent intent) {
        this.context = context;
        this.intent = intent;
        builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(canClose);
    }

    public void showAndGo() {

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (intent != null) {
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void show() {

        builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
        });
        builder.show();
    }
}
