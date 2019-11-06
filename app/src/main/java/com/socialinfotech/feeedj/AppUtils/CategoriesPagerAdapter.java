package com.socialinfotech.feeedj.AppUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.socialinfotech.feeedj.ExploreActivities.CategoryFeedsFragment;
import com.socialinfotech.feeedj.ExploreActivities.CategoryFollowFragment;

/**
 * Created by fi8er1 on 10/01/2018.
 */

public class CategoriesPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CategoriesPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CategoryFollowFragment();
            case 1:
                return new CategoryFeedsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
