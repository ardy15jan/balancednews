package ch.epfl.lia.customized;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import ch.epfl.lia.balancednews.MainActivity;
import ch.epfl.lia.balancednews.R;
import ch.epfl.lia.balancednews.TopicActivity;

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
