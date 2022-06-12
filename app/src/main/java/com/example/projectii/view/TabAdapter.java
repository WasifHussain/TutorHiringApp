package com.example.projectii.view;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.projectii.controller.TutorDashboardActivity;
import com.example.projectii.controller.TutorHistoryFragment;
import com.example.projectii.controller.TutorRatingFragment;
import com.example.projectii.controller.TutorSessionFragment;
import com.google.android.material.badge.BadgeDrawable;

public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TutorSessionFragment tutorSessionFragment = new TutorSessionFragment();
                return tutorSessionFragment;
            case 1:
                TutorHistoryFragment tutorHistoryFragment = new TutorHistoryFragment();
                return tutorHistoryFragment;
            case 2:
                TutorRatingFragment tutorRatingFragment = new TutorRatingFragment();
                return tutorRatingFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}