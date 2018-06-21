package com.kpcode4u.prasanthkumar.moviesapp.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Prasanth kumar on 21/06/2018.
 */

public class Utility {
    public static byte[] getPictureByteOfArray(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public static Bitmap getBitmapFromByte(byte[] image){
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }
}
