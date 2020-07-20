package com.app.codesmakers.ui.onboarding.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.codesmakers.utils.session.SessionManager;

/**
 * Created by nmillward on 7/12/16.
 */
public class BoardingAdapter extends FragmentPagerAdapter {

    public BoardingAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        return BoardingFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return SessionManager.getInstance().getAppHowToUseConfigurations().size();
    }
}
