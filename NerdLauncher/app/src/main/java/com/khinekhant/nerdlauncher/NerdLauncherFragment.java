package com.khinekhant.nerdlauncher;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NerdLauncherFragment extends Fragment {

    private static final String TAG = "NerdLauncherFragment";
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.fragment_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));

        setUpAdapter();
        return view;
    }


    //getting the list of activities that match the intent
    public void setUpAdapter(){
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
       PackageManager packageManager=getActivity().getPackageManager();
        //retrieve all activities that can perform given intent
        List<ResolveInfo> activitiesList=packageManager.queryIntentActivities(intent,0);
        //sort the resolveInfo objects return from pm in alphabetical order by label
        Collections.sort(activitiesList, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
       PackageManager pm =getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString());

            }
        });

        //setting up the adapter
mRecyclerView.setAdapter(new LabelAdapter(activitiesList));
        Log.i(TAG,"Number of activities that match are "+ activitiesList.size());
    }

    private class LabelsHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        private ResolveInfo mResolveInfo;
        private TextView mLabelTextView;
        private ImageView mIconImageView;
        public LabelsHolder(View itemView) {
            super(itemView);
            mLabelTextView=(TextView)itemView.findViewById(R.id.app_label_textView);
            mIconImageView=(ImageView)itemView.findViewById(R.id.app_icon_imageView);
          itemView.setOnClickListener(this);
        }

        //get the sorted resolveInfo objects and mind the loadLabel callback
        public void bindActivity(ResolveInfo resolveInfo){
            mResolveInfo=resolveInfo;
            PackageManager pm=getActivity().getPackageManager();
            String appName=mResolveInfo.loadLabel(pm).toString();
         Drawable appIcon= mResolveInfo.loadIcon(pm);
           // mLabelTextView.setText(appName);
            mIconImageView.setImageDrawable(appIcon);
        }

        @Override
        //make a explicit intent to start an activity the user pressed from the list
        public void onClick(View view) {
            ActivityInfo activityInfo=mResolveInfo.activityInfo;

            Intent intent=new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName,
                            activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
    }



    private class LabelAdapter extends RecyclerView.Adapter<LabelsHolder>{

private List<ResolveInfo> mResolveInfos;

        public LabelAdapter(List<ResolveInfo> resolveInfos) {
            mResolveInfos=resolveInfos;
        }

        @Override
        public LabelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View view=inflater.inflate(R.layout.app_label_and_icon,parent,false);
            return new LabelsHolder(view);
        }

        @Override
        public void onBindViewHolder(LabelsHolder holder, int position) {
ResolveInfo resolveInfo=mResolveInfos.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mResolveInfos.size();
        }
    }

}
