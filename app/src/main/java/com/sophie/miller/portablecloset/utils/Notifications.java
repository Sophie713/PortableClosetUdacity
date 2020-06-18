package com.sophie.miller.portablecloset.utils;

import android.content.Context;
import android.widget.Toast;

public class Notifications {

    public static void makeToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
