package com.item.borrowing.tools;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {
    private final ProgressDialog progressDialog;

    public LoadingDialog(Context context, String mensahe){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(mensahe);
        progressDialog.setCancelable(false);
    }
    public void Show(){
        progressDialog.show();
    }
    public void Close(){
        progressDialog.dismiss();
    }
}
