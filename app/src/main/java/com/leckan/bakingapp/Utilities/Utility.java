package com.leckan.bakingapp.Utilities;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Thinkpad on 8/6/2017.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    int noOfColumns = (int) (dpWidth / 300);
    return noOfColumns;
}
}
