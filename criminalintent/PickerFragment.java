package com.example.crimeintent;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;

public abstract class PickerFragment extends DialogFragment {

    protected static final String ARG_DATE = "date";
    protected Calendar mCalendar;

    public static final String EXTRA_DATE = "com.star.criminal-intent.date";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        mCalendar = Calendar.getInstance();
        assert date != null;
        mCalendar.setTime(date);
    }
    protected abstract void setDate();
    protected abstract Date getDate();

    protected void sendResult(Date date) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        if (getTargetFragment() == null) {
            getActivity().setResult(android.app.Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            getTargetFragment().onActivityResult(getTargetRequestCode(), android.app.Activity.RESULT_OK, intent);
            dismiss();
        }
    }
}