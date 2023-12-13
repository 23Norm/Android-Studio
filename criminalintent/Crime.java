package com.example.crimeintent;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiresPolice = false;
    private final String formattedDate;



    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        formattedDate = dateFormat.format(mDate);
    }
    public UUID getId() {
        return mId;
    }
    public String getTitle() {
        return mTitle;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
    public String getFormattedTime() {
        String format = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        return simpleDateFormat.format(mDate);
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public void setDate(Date date) {
        mDate = date;
    }
    public Date getDate() {
        return mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }
    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean requiresPolice() {
        return mRequiresPolice;
    }
    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }

}
