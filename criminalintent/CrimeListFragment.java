package com.example.criminalintent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private static final int VIEW_TYPE_BUTTON = 1;
    private static final int VIEW_TYPE = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;
        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final ImageView mSolvedImageView;
        private Button mRequiresPolice;

        public CrimeHolder(View layoutInflater, int viewType) {

            super(layoutInflater);
            layoutInflater.setOnClickListener(this);

            mTitleTextView = layoutInflater.findViewById(R.id.crime_title);
            mDateTextView = layoutInflater.findViewById(R.id.crime_date);
            mSolvedImageView = layoutInflater.findViewById(R.id.crime_solved);

            if(viewType == VIEW_TYPE){
                mRequiresPolice = null;
            } else {
                mRequiresPolice = layoutInflater.findViewById(R.id.police_contact_button);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getFormattedDate());
            mRequiresPolice = itemView.findViewById(R.id.police_contact_button);

            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);

            if(mRequiresPolice != null && crime.isSolved()) mRequiresPolice.setVisibility(View.GONE);

        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private final List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            View view;

            if(viewType == VIEW_TYPE_BUTTON){
                view = layoutInflater.inflate(R.layout.fragment_crime_police, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            }
            return new CrimeHolder(view, viewType);
        }

        @Override
        public int getItemViewType(int position) {
            boolean requiresPolice = mCrimes.get(position).requiresPolice();
            return requiresPolice ? VIEW_TYPE_BUTTON : VIEW_TYPE;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
