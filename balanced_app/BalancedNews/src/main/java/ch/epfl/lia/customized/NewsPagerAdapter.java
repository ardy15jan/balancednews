package ch.epfl.lia.customized;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by xtang on 13-10-23.
 */
public class NewsPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Integer> newsIDList;
    private int topicID;


    public NewsPagerAdapter(FragmentManager fm, ArrayList<Integer> newsIDList, int topicID){
        super(fm);
        this.newsIDList=newsIDList;
        this.topicID=topicID;
    }

    @Override
    public Fragment getItem(int i){


        Fragment fragment=new NewsFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(NewsFragment.TOPIC_ID, topicID);
        args.putInt(NewsFragment.NEWS_ID, newsIDList.get(i));
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getCount(){
        return newsIDList.size();
    }


}
