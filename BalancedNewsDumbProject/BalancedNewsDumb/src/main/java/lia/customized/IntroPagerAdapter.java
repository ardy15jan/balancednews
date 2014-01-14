package lia.customized;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by xtang on 13-12-2.
 */

public class IntroPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Integer> layoutIDs;


    public IntroPagerAdapter(FragmentManager fm, ArrayList<Integer> layoutIDs){
        super(fm);
        this.layoutIDs = layoutIDs;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=new IntroFragment();
        Bundle args = new Bundle();

        args.putInt(IntroFragment.LAYOUT_ID, layoutIDs.get(i));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {

        return layoutIDs.size();
    }
}
