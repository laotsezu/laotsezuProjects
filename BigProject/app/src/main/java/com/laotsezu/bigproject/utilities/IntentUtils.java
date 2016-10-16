package com.laotsezu.bigproject.utilities;

import android.content.Intent;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class IntentUtils {
    public static boolean isEmpty(Intent intent){
        return intent == null || intent.getExtras() == null;
    }
}
