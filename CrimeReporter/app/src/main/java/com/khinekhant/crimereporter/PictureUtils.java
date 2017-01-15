package com.khinekhant.crimereporter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by ${KhineKhant} on 0004,09/04/16.
 */
public class PictureUtils {


    /*public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return  getScaledBitmap(path, size.x, size.y);
    }*/



    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        //read the dimensions of the image
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        //scale down the photo
        int inSampleSize=1;
        if(srcHeight>destHeight || srcWidth>destWidth){
            if(srcHeight>srcWidth)
                inSampleSize=Math.round(srcHeight/destHeight);
        }else{
            inSampleSize=Math.round(srcWidth/destWidth);
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        //return the scaled photo file
        return BitmapFactory.decodeFile(path,options);

    }


}
