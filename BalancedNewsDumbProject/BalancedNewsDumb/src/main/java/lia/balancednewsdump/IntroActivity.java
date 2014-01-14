package lia.balancednewsdump;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ch.epfl.lia.balancednewsdumb.R;
import lia.constants.Constants;
import lia.customized.IntroPagerAdapter;


public class IntroActivity extends FragmentActivity {

    private ArrayList<Integer> layoutIDs;

    private ArrayList<Integer> imageIDs;
    private ArrayList<String> texts;

    private IntroPagerAdapter pagerAdapter;
    private ViewPager introPager;
    private ArrayList<ImageView> dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Add to activity holder
        Constants.activityHolder.addActivity(this);

        /*
        SharedPreferences preferences = getSharedPreferences(Constants.PREF, 0);
        if(!preferences.getBoolean(Constants.IS_FIRST_TIME, true)){
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        */

        layoutIDs = new ArrayList<Integer>();
        layoutIDs.add(R.layout.intro_page_main);
        layoutIDs.add(R.layout.intro_page_topic);
        layoutIDs.add(R.layout.intro_page_swipe_navigation);
        //layoutIDs.add(R.layout.intro_page_drawer);
        layoutIDs.add(R.layout.intro_page_login);

        dots = new ArrayList<ImageView>();
        LinearLayout indicators = (LinearLayout) findViewById(R.id.intro_indicator);
        int count = indicators.getChildCount();
        for(int i=0; i<count; i++){
            dots.add((ImageView)indicators.getChildAt(i));
        }
        dots.get(0).setImageResource(R.drawable.dot);


        pagerAdapter=new IntroPagerAdapter(getSupportFragmentManager(), this.layoutIDs);
        introPager=(ViewPager) findViewById(R.id.intro_pager);
        introPager.setAdapter(pagerAdapter);
        introPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

                int size = dots.size();

                for(int k=0; k<size; k++){
                    dots.get(k).setImageResource(R.drawable.dot_unselected);
                }
                dots.get(i).setImageResource(R.drawable.dot);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }
    
}
