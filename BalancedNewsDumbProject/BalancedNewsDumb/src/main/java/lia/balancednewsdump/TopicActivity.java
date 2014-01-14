package lia.balancednewsdump;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ch.epfl.lia.balancednewsdumb.R;
import lia.constants.Constants;
import lia.customized.NewsListAdapter;
import lia.customized.NewsListSorter;
import lia.model.News;
import lia.model.Record;
import lia.model.Topic;
import lia.webservice.MessageParser;
import lia.webservice.NewsService;

public class TopicActivity extends ActionBarActivity {

    private ArrayList<HashMap<String,Object>> newsItems=new ArrayList<HashMap<String, Object>>();
    private NewsListAdapter newsListAdapter;
    public static ArrayList<Integer> newsIDList;
    private int topicID;
    private ListView newsList;
    private android.support.v7.widget.PopupMenu popupMenu;

    private Record record;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case Constants.GET_TOPIC:
                    Topic topic=MessageParser.toTopic((String)msg.obj);
                    newsIDList=topic.newsList;
                    updateNewsList();
                    break;
                case Constants.GET_NEWS:
                    News news=MessageParser.toNews((String)msg.obj);

                    if(news!=null)
                        addToNewsList(news.title,news.content,news.id,news.opinionValue);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        //Add to activity holder
        Constants.activityHolder.addActivity(this);

        //Start record
        record = new Record(Constants.USER_ID.toString(), Record.TOPIC_TYPE, new Date());

        //Get passed value from intent
        Intent intent=getIntent();
        topicID=intent.getIntExtra("TOPIC_ID",0);

        //Disable action bar title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        //Get list vew
        newsList=(ListView) findViewById(R.id.news_list);
        newsList.setDividerHeight(Constants.LIST_DIVIDER_HEIGHT_MEDIUM);

        //ListView adapter
        newsListAdapter=new NewsListAdapter(this,
                newsItems,
                R.layout.news_list_item,
                new String[]{"itemImage", "itemTitle", "itemText","itemOpinion"},
                new int[]{R.id.news_item_image, R.id.news_item_title, R.id.news_item_content});

        newsList.setAdapter(newsListAdapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int newsID=(Integer)newsItems.get(i).get("itemID");

                Intent intent=new Intent(TopicActivity.this, NewsActivity.class);
                intent.putExtra("TOPIC_ID",topicID);
                intent.putExtra("NEWS_ID",newsID);
                startActivity(intent);
            }
        });


        //new Webservice(handler, CommandBuilder.createGetTopic(topicID), Constants.GET_TOPIC).start();
        new NewsService(handler).getTopic(topicID);
    }

    private void addToNewsList(String title, String content, int newsID, int opinion){
        HashMap<String, Object> map=new HashMap<String, Object>();
        map.put("itemImage", R.drawable.ic_launcher);
        map.put("itemTitle",title);
        map.put("itemText",content);
        map.put("itemID",newsID);
        map.put("itemOpinion", opinion);

        newsItems.add(map);
        new NewsListSorter().relevanceFirst(newsItems);
        newsListAdapter.notifyDataSetChanged();

    }

    private void updateNewsList(){

        for(int i=0;i<newsIDList.size();i++){
            Integer id=newsIDList.get(i).intValue();
            //new Webservice(handler,CommandBuilder.createGetNews(id),Constants.GET_NEWS).start();
            new NewsService(handler).getNews(topicID,id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case android.R.id.home:
                TopicActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortingList(View v){
        popupMenu=new android.support.v7.widget.PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.sort, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener( new android.support.v7.widget.PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                switch(item.getItemId()){
                    case R.id.sort_positive_first:
                        new NewsListSorter().positiveFirst(newsItems);
                        newsListAdapter.notifyDataSetChanged();
                        updateNewsIDListOrder();
                        //newsList.smoothScrollToPosition(0);
                        return true;

                    case R.id.sort_negative_first:
                        new NewsListSorter().negativeFirst(newsItems);
                        newsListAdapter.notifyDataSetChanged();
                        updateNewsIDListOrder();
                        //newsList.smoothScrollToPosition(0);
                        return true;

                    case R.id.sort_shuffle:
                        new NewsListSorter().shuffle(newsItems);
                        newsListAdapter.notifyDataSetChanged();
                        updateNewsIDListOrder();
                        //newsList.smoothScrollToPosition(0);
                        return true;

                    default:
                        return false;
                }
            }

            }
        );
        popupMenu.show();
    }

    @Override
    protected void onDestroy(){
        record.setDuration(new Date());
        record.setTopicID(topicID);
        record.sendRecord(handler);
        super.onDestroy();
    }

    private void updateNewsIDListOrder(){
        newsIDList.clear();
        for(HashMap<String, Object> item:newsItems){
            newsIDList.add((Integer)item.get("itemID"));
        }
    }
    
}
