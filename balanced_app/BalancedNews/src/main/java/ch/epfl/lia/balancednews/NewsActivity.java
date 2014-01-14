package ch.epfl.lia.balancednews;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


import ch.epfl.lia.Common.IntroHolder;
import ch.epfl.lia.constants.Constants;

import ch.epfl.lia.customized.NewsPagerAdapter;
import ch.epfl.lia.model.News;
import ch.epfl.lia.model.Topic;
import ch.epfl.lia.webservice.MessageParser;
import ch.epfl.lia.webservice.NewsService;


public class NewsActivity extends ActionBarActivity {

    private News news;
    private TextView newsTitle;
    private TextView newsContent;
    private TextView newsOpinion;
    private ScrollView scrollView;

    private ImageButton leftButton;
    private ImageButton rightButton;

    private ListView drawerListLeft;
    private ListView drawerListRight;
    private DrawerLayout mDrawerLayout;
    private ArrayList<HashMap<String,Object>> leftNewsItems=new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String,Object>> rightNewsItems=new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter leftAdapter;
    private SimpleAdapter rightAdapter;

    private ArrayList<Integer> newsIDList=TopicActivity.newsIDList;
    private int topicID;
    private int newsID;

    private NewsPagerAdapter pagerAdapter;
    private ViewPager newsPager;
    public static IntroHolder introHolder;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case Constants.GET_TOPIC:
                    Topic topic=MessageParser.toTopic((String)msg.obj);
                    //newsIDList=topic.newsList;

                    updateDrawer();
                    break;

                case Constants.GET_NEWS:

                    News news=MessageParser.toNews((String)msg.obj);
                    if(news!=null)
                        addToDrawer(news);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //Show intro
        if(Constants.showIntroNews){
            //showIntro();
        }
        /*
        introHolder = new IntroHolder(NewsActivity.this);
        introHolder.addIntro(1231,231,"Hello world");
        introHolder.addIntro(731,231,"Hello world");
        introHolder.next();
        */

        //Add to activity holder
        Constants.activityHolder.addActivity(this);

        //Get passed values
        Intent intent=getIntent();
        topicID=intent.getIntExtra("TOPIC_ID",0);
        newsID=intent.getIntExtra("NEWS_ID",0);

        //Disable action bar title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get views

        mDrawerLayout=(DrawerLayout) findViewById(R.id.news_drawer_layout);
        drawerListLeft=(ListView) findViewById(R.id.news_drawer_left);
        drawerListRight=(ListView) findViewById(R.id.news_drawer_right);
        drawerListLeft.setDividerHeight(Constants.LIST_DIVIDER_HEIGHT_SMALL);
        drawerListRight.setDividerHeight(Constants.LIST_DIVIDER_HEIGHT_SMALL);

        leftButton=(ImageButton) findViewById(R.id.button_left_drawer);
        rightButton=(ImageButton) findViewById(R.id.button_right_drawer);




        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(drawerListLeft);

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(drawerListRight);
            }
        });

        //Add adapter to listviews
        leftAdapter=new SimpleAdapter(this,
                leftNewsItems,
                R.layout.drawer_list_item,
                new String[]{"itemTitle", "itemImage"},
                new int[]{R.id.drawer_list_item_title, R.id.drawer_list_item_image});
        rightAdapter=new SimpleAdapter(this,
                rightNewsItems,
                R.layout.drawer_list_item,
                new String[]{"itemTitle", "itemImage"},
                new int[]{R.id.drawer_list_item_title, R.id.drawer_list_item_image});

        drawerListLeft.setAdapter(leftAdapter);
        drawerListRight.setAdapter(rightAdapter);

        //Set on item click listener
        drawerListLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int index=(Integer) leftNewsItems.get(i).get("itemIndex");
                newsPager.setCurrentItem(index);
                mDrawerLayout.closeDrawers();
            }
        });

        drawerListRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int index=(Integer) rightNewsItems.get(i).get("itemIndex");
                newsPager.setCurrentItem(index);
                mDrawerLayout.closeDrawers();
            }
        });

        //Get news

        pagerAdapter=new NewsPagerAdapter(getSupportFragmentManager(), this.newsIDList, this.topicID);
        newsPager=(ViewPager) findViewById(R.id.news_pager);
        newsPager.setAdapter(pagerAdapter);

        int initialIndex=newsIDList.indexOf(newsID);
        newsPager.setCurrentItem(initialIndex);


        //Get news IDs
        new NewsService(handler).getTopic(topicID);


        /*
        //Show intro
        if(Constants.showIntroNews){
            //showIntro();
        }
        IntroHolder test = new IntroHolder(getApplicationContext(), (RelativeLayout) findViewById(R.id.intro_news_view)
                ,(Button) findViewById(R.id.next_opinion), IntroHolder.HIGHLIGHT_BOTTOM, "Click me");
        test.addIntro();
        */
    }

    private void setNewsOpinion(int opinionValue){
        if(opinionValue<0){
            newsOpinion.setText(Math.abs(opinionValue)+"");
            newsOpinion.setBackgroundColor(Color.parseColor(Constants.NEGATIVE_COLOR));
        }else{
            newsOpinion.setText(Math.abs(opinionValue)+"");
            newsOpinion.setBackgroundColor(Color.parseColor(Constants.POSITIVE_COLOR));
        }
    }

    private void updateDrawer(){
        for(int i=0;i<newsIDList.size();i++){
            Integer id=newsIDList.get(i).intValue();

            new NewsService(handler).getNews(topicID, id);
        }
    }

    private void addToDrawer(News news){
        int opinionValue=news.opinionValue;
        if(opinionValue>0){
            HashMap<String, Object> map=new HashMap<String, Object>();
            map.put("itemImage", R.drawable.ic_launcher);
            map.put("itemTitle",news.title);
            map.put("itemID",news.id);
            map.put("itemIndex",newsIDList.indexOf(news.id));
            leftNewsItems.add(map);
            leftAdapter.notifyDataSetChanged();
        }else{
            HashMap<String, Object> map=new HashMap<String, Object>();
            map.put("itemImage", R.drawable.ic_launcher);
            map.put("itemTitle",news.title);
            map.put("itemID",news.id);
            map.put("itemIndex",newsIDList.indexOf(news.id));
            rightNewsItems.add(map);
            rightAdapter.notifyDataSetChanged();
        }

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


    private void showIntro(){
        RelativeLayout introView = (RelativeLayout) findViewById(R.id.intro_news_view);

        Constants.showIntroNews = true;

        TextView introNewsText = (TextView) findViewById(R.id.intro_opiniontag_text);
        SpannableString text = new SpannableString(introNewsText.getText());
        ImageSpan leftButton = new ImageSpan(getApplicationContext(), R.drawable.ic_action_previous_item_light);
        ImageSpan rightButton = new ImageSpan(getApplicationContext(), R.drawable.ic_action_next_item_light);
        text.setSpan(leftButton, 5,6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        text.setSpan(rightButton, 8, 9, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        introNewsText.setText(text);

        introView.setVisibility(View.VISIBLE);
        introView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((Button) findViewById(R.id.dismiss_intro_news)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RelativeLayout) findViewById(R.id.intro_news_view)).setVisibility(View.GONE);
                Constants.showIntroNews = false;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(Constants.showIntroNews){
                return true;
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            case R.id.action_help_news:
                showIntro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    
}
