package ch.epfl.lia.balancednews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ch.epfl.lia.Common.DeviceUuidFactory;
import ch.epfl.lia.Common.FontManager;
import ch.epfl.lia.constants.Constants;
import ch.epfl.lia.customized.CountDown;
import ch.epfl.lia.customized.TopicListAdapter;
import ch.epfl.lia.customized.TopicListSorter;
import ch.epfl.lia.webservice.MessageParser;
import ch.epfl.lia.webservice.NewsService;
import ch.epfl.lia.model.*;


public class MainActivity extends ActionBarActivity {


    private ArrayList<HashMap<String,Object>> topicItems;
    private ArrayList<Double> topicIDs;
    private TopicListAdapter topicListAdapter;
    private Record record;
    private Toast quitToast;
    private boolean doubleBackToExitPressedOnce = false;
    private ListView topicList;
    private HashMap<Integer, Boolean> receivedTopic;
    private CountDown countDown;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.GET_TOPIC_IDS:
                    topicIDs=MessageParser.toTopicIDs((String)msg.obj);
                    updateTopicList();
                    break;
                case Constants.GET_TOPIC:
                    Topic topic=MessageParser.toTopic((String)msg.obj);
                    //addToTopicList(topic.topStory.title, topic.topStory.content);
                    if(topic.isValid()){
                        String index=topic.newsList.size()<10?topic.newsList.size()+" ":topic.newsList.size()+"";
                        if(!receivedTopic.get(topic.topic_id)){
                            Constants.topics.add(topic);
                            addToTopicList(topic.positiveNews.title, topic.negativeNews.title, index, topic.tags, topic.topic_id, topic.getPositivePercentage());
                            receivedTopic.put(topic.topic_id, true);
                        }
                    }
                    if(topicList.getChildAt(0) != null && Constants.showIntroMain){
                        showIntro();
                    }
                    break;
                case Constants.START_QUESTIONAIR:
                    showQuestionnaire();
                    break;
                case Constants.SHOW_NOTIFICATION:
                    if(!Constants.isShwonQuestionnaire)
                        Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Set isShownQuestionnaireFalse
        Constants.isShwonQuestionnaire = false;

        setContentView(R.layout.activity_main);
        Constants.showIntroMain = true;

        //Add to activity holder
        Constants.activityHolder.addActivity(this);

        //Get device id
        DeviceUuidFactory idFactory = new DeviceUuidFactory(getApplicationContext());
        Constants.USER_ID = idFactory.getUserId();

        //Start record
        record = new Record(Constants.USER_ID, Record.APP_TYPE, new Date());

        //Disable action bar title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        //Get view
        topicList=(ListView) findViewById(R.id.topic_list);
        topicList.setDividerHeight(Constants.LIST_DIVIDER_HEIGHT_LARGE);

        //Topic List
        topicItems=new ArrayList<HashMap<String, Object>>();

        //Listview adapter
        topicListAdapter=new TopicListAdapter(this,
                topicItems,
                R.layout.list_item,
                new String[]{"itemImage", "itemTitlePositive", "itemTitleNegative",  "itemText", "itemTags"},
                new int[]{R.id.topic_image, R.id.topic_title_positive, R.id.topic_title_negative, R.id.topic_content, R.id.tags});

        topicList.setAdapter(topicListAdapter);
        clearTopicList();

        //Add onItemClick event
        topicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int topicID=(Integer) topicItems.get(i).get("itemID");
                Intent intent=new Intent(MainActivity.this, TopicActivity.class);
                intent.putExtra("TOPIC_ID",topicID);
                startActivity(intent);
            }
        });


        //Get topic list
        new NewsService(handler).getTopicIDs(Constants.DEFAULT_CATEGORY);

        //Action bar navigation

        SimpleAdapter mSpinnerAdapter=new SimpleAdapter(this,getDropdownMenuItem(),R.layout.dropdown_menu_item,new String[]{"choice"},new int[]{R.id.dropdown_menu_choice});

        actionBar.setListNavigationCallbacks(mSpinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                return false;
            }
        });

        //Set font
        ViewGroup godfatherView = (ViewGroup)this.getWindow().getDecorView();
        FontManager.setRobotoFont(this, godfatherView);

        //Set introduction view
        RelativeLayout introView = (RelativeLayout) findViewById(R.id.intro_main_view);
        introView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Start count down
        countDown = new CountDown(handler, Constants.COUNT_DOWN_SECONDS);
        countDown.start();
        //CountDown.startCountDown(handler , Constants.COUNT_DOWN_SECONDS);

    }

    private void addToTopicList(String titlePositive, String titleNegative, String content, ArrayList<String> tags, int topicID, int percentage){
        HashMap<String, Object> map=new HashMap<String, Object>();
        map.put("itemImage", R.drawable.ic_launcher);
        map.put("itemTitlePositive",titlePositive);
        map.put("itemTitleNegative", titleNegative);
        map.put("itemText",content);
        map.put("itemTags", tags);
        map.put("itemID",topicID);
        map.put("itemPercentage", percentage);
        topicItems.add(map);
        TopicListSorter.sortByID(topicItems);
        topicListAdapter.notifyDataSetChanged();
    }

    private void clearTopicList(){
        topicItems.clear();
        topicListAdapter.notifyDataSetChanged();
    }

    private void updateTopicList(){

        if(receivedTopic == null)
            receivedTopic = new HashMap<Integer, Boolean>(topicIDs.size());


        for(int i=0;i<topicIDs.size();i++){
            Integer id=topicIDs.get(i).intValue();
            receivedTopic.put(id, false);
            new NewsService(handler).getTopic(id);
        }

        Constants.topics.clear();
    }


    private List<Map<String,Object>> getDropdownMenuItem(){
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("choice",Constants.NAVIGATION_IN_MAIN);
        list.add(map);
        return list;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case R.id.action_help_main:
                showIntro();
                return true;
            case R.id.action_questionnaire:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy(){

        record.setDuration(new Date());
        record.sendRecord(handler);
        Constants.reset();
        countDown.kill();
        NewsActivity.introHolder = null;
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(Constants.showIntroMain){

                return true;
            }

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();

                return true;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;

                }
            }, 2000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showIntro(){
        Constants.showIntroMain = true;
        RelativeLayout introMainView = (RelativeLayout) findViewById(R.id.intro_main_view);
        introMainView.setVisibility(View.VISIBLE);
        View topicItem = topicList.getChildAt(0);
        View subjectPolarity = topicItem.findViewById(R.id.subject_polarity);
        View tags = topicItem.findViewById(R.id.tags);

        ImageView introCircle = (ImageView) findViewById(R.id.intro_circle_image);
        introCircle.setImageBitmap(getBitmapFromView(subjectPolarity));

        ImageView introTags = (ImageView) findViewById(R.id.intro_tags_image);
        introTags.setImageBitmap(getBitmapFromView(tags));

        ((Button) findViewById(R.id.dismiss_intro_main)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RelativeLayout) findViewById(R.id.intro_main_view)).setVisibility(View.GONE);
                Constants.showIntroMain = false;
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure to skip to questionnaire?")
                .setTitle("Skip to questionnaire")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showQuestionnaire();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showQuestionnaire(){
        if(Constants.isShwonQuestionnaire == true){
            return;
        }

        Constants.isShwonQuestionnaire = true;

        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        startActivity(intent);
        Constants.activityHolder.finishAll();
    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}
