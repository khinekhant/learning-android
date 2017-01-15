package com.khinekhant.beatbox;

import android.nfc.Tag;
import android.util.Log;

/**
 * Created by ${KhineKhant} on 0006,09/06/16.
 */
public class Sound {
    private static final String TAG = "filename";
    private String mAssetPath;
    private String mName;
    private Integer mSoundId;



    public Sound(String assetPath) {
        mAssetPath = assetPath;
        //split each string
        String[] components=assetPath.split("/");
        //get each string by specifying the index
        String filename=components[components.length -1];
        Log.i(TAG,"filename after spliting is "+ filename);
        //make that string only to display filename not include type
        mName=filename.replace(".wav","");

    }

    //setter
    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }

    //getter
    public String getName() {
        return mName;
    }
    public String getAssetPath() {
        return mAssetPath;
    }
    public Integer getSoundId() {
        return mSoundId;
    }

}
