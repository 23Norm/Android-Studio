package com.example.crimeintent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CrimeFragment extends Fragment {
        private static final String ARG_CRIME_ID = "crime_id";
        private static final String DIALOG_DATE = "DialogDate";
        private static final String DIALOG_TIME = "DialogTime";
        private static final int REQUEST_DATE = 0;
        private Button mDateButton;
        private Button mTimeButton;
        private Crime mCrime;

        public static CrimeFragment newInstance(UUID crimeId) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_CRIME_ID, crimeId);
            CrimeFragment fragment = new CrimeFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            assert getArguments() != null;
            UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
            mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        }

        @SuppressLint({"MissingInflatedId", "CutPasteId"})
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_crime, container, false);

            EditText mTitleField = (EditText) v.findViewById(R.id.crime_title);
            mTitleField.setText(mCrime.getTitle());
            mTitleField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(
                        CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(
                        CharSequence s, int start, int before, int count) {
                    mCrime.setTitle(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            mDateButton = v.findViewById(R.id.crime_date);
            mDateButton.setOnClickListener(view -> {
                if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    FragmentManager fragmentManager = getFragmentManager();
                    DatePickerFragment datePickerFragment =
                            DatePickerFragment.newInstance(mCrime.getDate());
                    datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                    assert fragmentManager != null;
                    datePickerFragment.show(fragmentManager, DIALOG_DATE);
                } else if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = DatePickerActivity.newIntent(getActivity(), mCrime.getDate());
                    startActivityForResult(intent, REQUEST_DATE);
                }
            });

            mTimeButton = v.findViewById(R.id.crime_time);
            mTimeButton.setOnClickListener(view -> {
                if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    FragmentManager fragmentManager = getFragmentManager();
                    TimePickerFragment timePickerFragment =
                            TimePickerFragment.newInstance(mCrime.getDate());
                    timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                    assert fragmentManager != null;
                    timePickerFragment.show(fragmentManager, DIALOG_TIME);
                } else if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = TimePickerActivity.newIntent(getActivity(), mCrime.getDate());
                    startActivityForResult(intent, REQUEST_DATE);
                }
            });


            Button mDateButton1 = (Button) v.findViewById(R.id.crime_date);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            String dateString = sdf.format(mCrime.getDate());
            mDateButton1.setText(dateString);

            CheckBox mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    mCrime.setSolved(isChecked);
                }
            });
            return v;
        }

    private void updateUI() {
        mDateButton.setText(mCrime.getFormattedDate());
        mTimeButton.setText(mCrime.getFormattedTime());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE && resultCode == Activity.RESULT_OK) {
            Date date = (Date) data.getSerializableExtra(PickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateUI();
        }
    }
}
