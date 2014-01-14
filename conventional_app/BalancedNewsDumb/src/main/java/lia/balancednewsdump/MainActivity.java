package lia.balancednewsdump;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.lia.balancednewsdumb.R;
import lia.Common.DeviceUuidFactory;
import lia.Common.FontManager;
import lia.constants.Constants;
import lia.customized.CountDown;
import lia.customized.TopicListAdapter;
import lia.customized.TopicListSorter;
import lia.model.*;
import lia.webservice.MessageParser;
import lia.webservice.NewsService;


public class MainActivity extends ActionBarActivity {


    private ArrayList<HashMap<String,Object>> topicItems;
    private ArrayList<Double> topicIDs;
    private TopicListAdapter topicListAdapter;

    private HashMap<Integer, Boolean> receivedTopic;
    private boolean doubleBackToExitPressedOnce = false;

    private Record record;
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
                        //addToTopicList(topic.getTopNews().title, index, topic.tags, topic.topic_id, topic.getPositivePercentage());
                        if(!receivedTopic.get(topic.topic_id)){
                            Constants.topics.add(topic);
                            addToTopicList(topic.getTopNews().title, index, topic.tags, topic.topic_id, topic.getPositivePercentage());
                            receivedTopic.put(topic.topic_id, true);
                        }
                    }
                    break;

                case Constants.START_QUESTIONAIR:
                    showQuestionnaire();
                    break;
                case Constants.SHOW_NOTIFICATION:
                    if(!Constants.isShwonQuestionnaire)
                        Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         
        setContentView(R.layout.activity_main);

        //Set isShownQuestionnaireFalse
        Constants.isShwonQuestionnaire = false;

        //Get device id
        DeviceUuidFactory idFactory = new DeviceUuidFactory(getApplicationContext());
        Constants.USER_ID = idFactory.getUserId();

        //Add to activity holder
        Constants.activityHolder.addActivity(this);

        //Start record
        record = new Record(Constants.USER_ID, Record.APP_TYPE, new Date());

        //Disable action bar title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);



        //Get view
        ListView topicList=(ListView) findViewById(R.id.topic_list);
        topicList.setDividerHeight(Constants.LIST_DIVIDER_HEIGHT_LARGE);

        //Topic List
        topicItems=new ArrayList<HashMap<String, Object>>();

        //Listview adapter
        topicListAdapter=new TopicListAdapter(this,
                topicItems,
                R.layout.list_item,
                new String[]{"itemImage", "itemTitle", "itemText", "itemTags"},
                new int[]{R.id.topic_image, R.id.topic_title, R.id.topic_content, R.id.tags});

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
        //new Webservice(handler, CommandBuilder.createGetTopicIDs("Economy"), Constants.GET_TOPIC_IDS).start();
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

        //Start count down
        countDown = new CountDown(handler, Constants.COUNT_DOWN_SECONDS);
        countDown.start();

    }

    private void addToTopicList(String title, String content, ArrayList<String> tags, int topicID, int percentage){
        HashMap<String, Object> map=new HashMap<String, Object>();
        map.put("itemImage", R.drawable.ic_launcher);
        map.put("itemTitle",title);
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
        map.put("choice", Constants.NAVIGATION_IN_MAIN);
        list.add(map);
        return list;
    }


    @Override
    protected void onDestroy(){

        record.setDuration(new Date());
        record.sendRecord(handler);
        countDown.kill();
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

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

    private void showQuestionnaire(){
        if(Constants.isShwonQuestionnaire){
            return;
        }

        Constants.isShwonQuestionnaire = true;

        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        startActivity(intent);
        Constants.activityHolder.finishAll();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case R.id.action_questionnaire:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
