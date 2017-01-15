package com.khinekhant.crimereporter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {


private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private boolean mSubTitleVisibility;
    private ImageButton mImageButtonForPlain;
    private TextView mTextViewForPlain;
    private LinearLayout layout;

private Callbacks mCallbacks;


    /**
     *interface for hosting activity to implement
     */
public interface Callbacks{
    void onCrimeSelected(Crime crime);
}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks=(Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        layout =(LinearLayout) view.findViewById(R.id.add_new_crime_for_plain_layout);
        mImageButtonForPlain=(ImageButton) view.findViewById(R.id.add_new_crime_for_plain);
        mTextViewForPlain=(TextView) view.findViewById(R.id.inform_user_for_plain);
        mTextViewForPlain.setText(R.string.inform_text);
        mImageButtonForPlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crime crime = new Crime();
                Intent intent = CrimeViewPagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                CrimeLab.getInstance(getActivity()).addCrime(crime);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInterface();
    }

    public void updateUserInterface() {
        List<Crime> crimesToDisaply = CrimeLab.getInstance(getActivity()).getCrimes();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimesToDisaply);
            mRecyclerView.setAdapter(mCrimeAdapter);
        } else {
           mCrimeAdapter.setCrimelist(crimesToDisaply);
            mCrimeAdapter.notifyDataSetChanged();
        }

        if(crimesToDisaply.isEmpty()){
            layout.setVisibility(View.VISIBLE);
            mImageButtonForPlain.setVisibility(View.VISIBLE);
            mTextViewForPlain.setVisibility(View.VISIBLE);

        }else {
            layout.setVisibility(View.INVISIBLE);
            mTextViewForPlain.setVisibility(View.INVISIBLE);
            mImageButtonForPlain.setVisibility(View.INVISIBLE);
        }

        setsubOnToolBar();
    }
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mDate;
        private CheckBox mIsCrimeSolve;

        private Crime mCrime;
        public ViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

            mTitle=(TextView) itemView.findViewById(R.id.crime_title_separate_layout);
            mDate=(TextView) itemView.findViewById(R.id.crime_date_separate_layout);
            mIsCrimeSolve=(CheckBox) itemView.findViewById(R.id.crime_solved_seperate_layout);



        }

        public void WiringupViewswithData(Crime crime){
            mCrime=crime;
            mTitle.setText(mCrime.getTitle());
            mDate.setText(mCrime.getDate().toString());
            mIsCrimeSolve.setChecked(mCrime.isCrimeSolved());

        }

        @Override
        public void onClick(View view) {
            /*Intent intent=CrimeViewPagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
            */
            //call callbacks interface method that is implemented by hosting Activity
            mCallbacks.onCrimeSelected(mCrime);

        }
    }




    private class CrimeAdapter extends RecyclerView.Adapter<ViewHolder>{
        List<Crime> mCrimeList;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimeList = crimes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.seperate_layout_for_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Crime crime=mCrimeList.get(position);
            holder.WiringupViewswithData(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }

       public void setCrimelist(List<Crime> crimes){
            mCrimeList=crimes;
       }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        //get actionitem to show total crime number
       MenuItem mMenuItem=menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubTitleVisibility) {
            mMenuItem.setTitle(R.string.hide_subtitle);
        }else {
            mMenuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
               // Intent intent = CrimeViewPagerActivity.newIntent(getActivity(), crime.getId());
               // startActivity(intent);
                //call callbacks interface method that is implemented by hosting Activity
                mCallbacks.onCrimeSelected(crime);
                updateUserInterface();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubTitleVisibility=!mSubTitleVisibility;
                getActivity().invalidateOptionsMenu();
                setsubOnToolBar();
                return true;
default:
    return super.onOptionsItemSelected(item);
        }
    }


    public void setsubOnToolBar(){
       int sizeOfCrimeList= CrimeLab.getInstance(getActivity()).getCrimes().size();
        String subtitle=getResources().getQuantityString(R.plurals.sub_title,sizeOfCrimeList,sizeOfCrimeList);

        if(!mSubTitleVisibility){
            subtitle=null;
        }

        //cast crimelistactivity into appcompactactivity in order to use toolbar
        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }
}
