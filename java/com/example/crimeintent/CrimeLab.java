package com.example.crimeintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static volatile CrimeLab sCrimeLab;

    private final SQLiteDatabase mSQLiteDatabase;

    private CrimeLab(Context context) {
        Context mContext = context.getApplicationContext();
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            synchronized (CrimeLab.class) {
                if (sCrimeLab == null) {
                    sCrimeLab = new CrimeLab(context);
                }
            }
        }

        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(null, null);

        try {
            while (crimeCursorWrapper.moveToNext()) {
                crimes.add(crimeCursorWrapper.getCrime());
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ", new String[] { id.toString() });

        try {
            if (crimeCursorWrapper.getCount() == 0) {
                return null;
            }

            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getCrime();
        } finally {
            crimeCursorWrapper.close();
        }
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        mSQLiteDatabase.insert(CrimeDbSchema.CrimeTable.TABLE_NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);
        mSQLiteDatabase.update(CrimeDbSchema.CrimeTable.TABLE_NAME, contentValues,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ", new String[] { uuidString });
    }

    public CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(CrimeDbSchema.CrimeTable.TABLE_NAME, null, whereClause, whereArgs,
                null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

    public void removeCrime(Crime crime) {
        mSQLiteDatabase.delete(CrimeDbSchema.CrimeTable.TABLE_NAME, CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ",
                new String[] { crime.getId().toString() });
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.REQUIRES_POLICE, crime.isRequiresPolice() ? 1 : 0);

        return contentValues;
    }
}