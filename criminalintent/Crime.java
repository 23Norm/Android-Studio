package com.example.criminalintent;


import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private final String formattedDate;

    private boolean mRequiresPolice;


    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        formattedDate = dateFormat.format(mDate);

    }

    public boolean requiresPolice() {
        return mRequiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
