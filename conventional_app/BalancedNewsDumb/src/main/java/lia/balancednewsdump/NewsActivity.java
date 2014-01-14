package lia.balancednewsdump;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import ch.epfl.lia.balancednewsdumb.R;
import lia.constants.Constants;
import lia.customized.NewsPagerAdapter;


public class NewsActivity extends ActionBarActivity {

    private ArrayList<Integer> newsIDList=TopicActivity.newsIDList;
    private int topicID;
    private int newsID;

    private NewsPagerAdapter pagerAdapter;
    private ViewPager newsPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //Get passed values
        Intent intent=getIntent();
        topicID=intent.getIntExtra("TOPIC_ID",0);
        newsID=intent.getIntExtra("NEWS_ID",0);

        //Disable action bar title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Add to activity holder
        Constants.activityHolder.addActivity(this);

        //Get news
        //new NewsService(handler).getCompleteNews(topicID,newsID);

        pagerAdapter=new NewsPagerAdapter(getSupportFragmentManager(), this.newsIDList, this.topicID);
        newsPager=(ViewPager) findViewById(R.id.news_pager);
        newsPager.setAdapter(pagerAdapter);

        int initialIndex=newsIDList.indexOf(newsID);
        newsPager.setCurrentItem(initialIndex);
    }

    private void readNext(){

        int currentIndex=newsPager.getCurrentItem();
        if(currentIndex<newsIDList.size()-1){
            newsPager.setCurrentItem(currentIndex+1);
        }

    }

    private void readPrevious(){

        int currentIndex=newsPager.getCurrentItem();
        if(currentIndex>0){
            newsPager.setCurrentItem(currentIndex-1);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_next_news:
                readNext();
                return true;
            case R.id.action_previous_news:
                readPrevious();
                return true;
            case android.R.id.home:
                NewsActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    
}
