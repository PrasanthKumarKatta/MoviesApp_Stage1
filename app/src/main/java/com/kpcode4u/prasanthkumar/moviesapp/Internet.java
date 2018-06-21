package com.kpcode4u.prasanthkumar.moviesapp.ConnectToInternet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Prasanth kumar on 14/06/2018.
 */

public class Internet {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo!= null && networkInfo.isConnected()&& networkInfo.isConnectedOrConnecting();
    }
}
