package com.khinekhant.beatbox;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BeatBoxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeatBoxFragment extends Fragment {

    private RecyclerView mBeatBoxRecyclerView;
    private BeatBox mBeatBox;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * @return A new instance of fragment BeatBoxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeatBoxFragment newInstance() {
        BeatBoxFragment fragment = new BeatBoxFragment();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        mBeatBox=new BeatBox(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_beat_box, container, false);
        mBeatBoxRecyclerView=(RecyclerView)view.findViewById(R.id.beat_box_recycler_view);
        mBeatBoxRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mBeatBoxRecyclerView.setAdapter(new BeatBoxAdapter(mBeatBox.getSounds()));
        return view;

    }
    private class BeatBoxHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button mButton;
        private Sound mSound;


        public BeatBoxHolder(LayoutInflater inflater,ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_sound,container,false));
            mButton=(Button) itemView.findViewById(R.id.list_item_sound_button);
            mButton.setOnClickListener(this);

        }
        public void bindSound(Sound sound){
            mSound =sound;
            mButton.setText(mSound.getName());
        }

        @Override
        public void onClick(View view) {
            mBeatBox.play(mSound);
        }
    }

    private class BeatBoxAdapter extends RecyclerView.Adapter<BeatBoxHolder>{
        private List<Sound> soundList;

        public BeatBoxAdapter(List<Sound> soundList) {
            this.soundList = soundList;
        }

        @Override
        public BeatBoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     LayoutInflater inflater=LayoutInflater.from(getActivity());
            return new BeatBoxHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(BeatBoxHolder holder, int position) {
Sound sound=soundList.get(position);
            holder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return soundList.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }
}
