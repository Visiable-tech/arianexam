package com.onlinetalentsearchexam.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void showToast(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
