package com.test.numbers.main;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AlertDialog;

import com.test.numbers.R;


public class Controller {

    public static Mylistner mylistner;

    public interface Mylistner {
        void func();
    }

    private static ProgressDialog progressDialog;
    public static final String LIST_DATA_URL = "http://dev.tapptic.com/test/json.php";
    public static final String DETAIL_DATA_URL = "http://dev.tapptic.com/test/json.php?name=";

    public static void showLoader(Context con, String msg) {
        progressDialog = new ProgressDialog(con);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public static void stopLoader() {
        if (progressDialog != null) {

            progressDialog.cancel();
            progressDialog = null;

        }
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void show_network_info_dialog(Context context) {
        mylistner = (Mylistner) context;

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setMessage("Sorry you don't have active internet connection.Turn on data and try again!")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mylistner.func();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}

