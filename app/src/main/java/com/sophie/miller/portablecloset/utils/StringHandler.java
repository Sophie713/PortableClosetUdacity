package com.sophie.miller.portablecloset.utils;

import android.widget.TextView;

public class StringHandler {
    /**
     * gets string from a view or returns ""
     * avoids null pointer exception
     *
     * @param v view to get string from
     * @return
     */
    public static String getText(TextView v) {
        CharSequence text = "";
        if (v != null)
            text = v.getText();
        if (text != null) {
            return text.toString();
        } else {
            return "";
        }
    }
}
