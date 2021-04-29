package com.rasool.ehsanvoice.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rasool.ehsanvoice.Fragments.HomeFragment;
import com.rasool.ehsanvoice.Fragments.SpeakFragment;

public class MyTabAdapter extends FragmentPagerAdapter {

    String[] Titles ={"Categories","Text to Speech",};

    public MyTabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new HomeFragment();
            case 1:
                return new SpeakFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return Titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

}
