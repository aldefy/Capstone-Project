package techgravy.nextstop.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

public class PagerAdapter extends BasePagerAdapter {
    private List<Fragment> fragments;


    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getFragmentItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void updateFragmentItem(int position, Fragment fragment) {

    }
}
