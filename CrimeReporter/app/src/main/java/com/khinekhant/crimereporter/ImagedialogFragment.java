package com.khinekhant.crimereporter;



import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagedialogFragment extends DialogFragment {
    private static final String PHOTOFILE= "get photofile from crimefragemnt";
    private ImageView mZoomInImageView;



public static ImagedialogFragment newInstance(Context context, File photoFile){
    Bundle args=new Bundle();
    args.putSerializable(PHOTOFILE,photoFile);

    ImagedialogFragment mZoomInDialog=new ImagedialogFragment();
    mZoomInDialog.setArguments(args);

    return mZoomInDialog;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    File mPhotoFileToZoomIn=(File)getArguments().getSerializable(PHOTOFILE);

        View view= inflater.inflate(R.layout.fragment_image_dialog_, container, false);
        mZoomInImageView=(ImageView)  view.findViewById(R.id.zoom_in_imageView);

        Point setSize = new Point();
        setSize.set(mZoomInImageView.getWidth(), mZoomInImageView.getHeight());
        Bitmap suspectImageBitmap=PictureUtils.getScaledBitmap(mPhotoFileToZoomIn.getPath(),setSize.x,setSize.y);
        mZoomInImageView.setImageBitmap(suspectImageBitmap);
        return  view;
    }



}
