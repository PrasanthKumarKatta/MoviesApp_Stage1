package com.kpcode4u.prasanthkumar.moviesapp.data;

import android.provider.BaseColumns;

/**
 * Created by Prasanth kumar on 10/06/2018.
 */

public class FavoriteContract {

    public static final class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USERRATING = "userrating";
        public static final String COLUMN_RELEASEDATE = "releasedate";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";
        public static final String COLUMN_BACKDROP_IMG = "backdrop";



    }
}
