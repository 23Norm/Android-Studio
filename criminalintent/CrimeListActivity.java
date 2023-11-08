package com.example.criminalintent;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected CrimeListFragment createFragment() {
        return new CrimeListFragment();
    }

}