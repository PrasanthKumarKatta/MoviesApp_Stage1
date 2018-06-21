package com.kpcode4u.prasanthkumar.moviesapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.kpcode4u.prasanthkumar.moviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.kpcode4u.prasanthkumar.moviesapp.MainActivity.TAG;

/**
 * Created by Prasanth kumar on 10/06/2018.
 */

public class FavoriteDBHelper extends SQLiteOpenHelper {

   private static final String DATABSE_NAME = "favorite.db";
   private static final int DATABSE_VERSION = 1;
   private static final String LOGTAG = "FAVORITE";

   public  static final String GET_FAVORITE_QUERY = "SELECT * FROM " + FavoriteContract.FavoriteEntry.TABLE_NAME;
   public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME;

   SQLiteOpenHelper dbHandler;
   SQLiteDatabase db;
   Context context;

    public FavoriteDBHelper(Context context) {
        super(context, DATABSE_NAME, null,DATABSE_VERSION);
        this.context = context;

    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        db = dbHandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG,"Database Closed");
        dbHandler.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                        FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                        FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_IMG + " TEXT NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL );";

                       /* FavoriteContract.FavoriteEntry.COLUMN_POSTER_blob + " blob not null, " +*/
                        /*FavoriteContract.FavoriteEntry.COLUMN_RELEASEDATE + "TEXT NOT NULL, " +*/

       try {
           db.execSQL(SQL_CREATE_FAVORITE_TABLE);
           this.onCreate(db);
           Toast.makeText(context, "Table created", Toast.LENGTH_SHORT).show();
       }catch (SQLException e){
           Log.d(TAG,e.getMessage());
       }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_QUERY);
        this.onCreate(db);
    }

    public void addFavorite(Movie movie){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie.getId());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.getOriginal_title());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_IMG,movie.getBackdrop_path());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, movie.getOverview());

        db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null,values);
        db.close();

    }

    public void deleteFavorite(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + "=" + id, null );

    }

    public List<Movie> getAllFaavorite(){
        String[] clumns = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_TITLE,
                FavoriteContract.FavoriteEntry.COLUMN_USERRATING,
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_IMG,
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS
                };

        String sortOrder = FavoriteContract.FavoriteEntry._ID + "ASC";
        List<Movie> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();



     /*   Cursor cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                clumns,
                null,
                null,
                null,
                null,
                sortOrder);
      */
     Cursor cursor = db.rawQuery(GET_FAVORITE_QUERY,null);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH)));
              //  movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_IMG)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS)));

                favoriteList.add(movie);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoriteList;
    }

    public  boolean search(int id){
        SQLiteDatabase db = getReadableDatabase();
        int num = db.rawQuery(GET_FAVORITE_QUERY + "where movieid="+ id,null).getCount();
        if (num == 0){
            return false;
        }else {
            return true;
        }

    }

}
