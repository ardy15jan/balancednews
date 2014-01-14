package ch.epfl.lia.balancednews;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.lia.constants.Constants;
import ch.epfl.lia.customized.QuestionPagerAdapter;

public class QuestionActivity extends FragmentActivity {

    private QuestionPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ArrayList<ImageView> dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        pagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.question_pager);
        viewPager.setAdapter(pagerAdapter);

        dots = new ArrayList<ImageView>();
        LinearLayout indicators = (LinearLayout) findViewById(R.id.intro_indicator);
        int count = indicators.getChildCount();
        for(int i=0; i<count; i++){
            dots.add((ImageView)indicators.getChildAt(i));
        }
        dots.get(0).setImageResource(R.drawable.dot);

        viewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
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
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(),"Please complete the questionnaire",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    
}
