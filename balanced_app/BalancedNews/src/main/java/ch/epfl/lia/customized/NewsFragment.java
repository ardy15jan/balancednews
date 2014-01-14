package ch.epfl.lia.customized;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ch.epfl.lia.Common.IntroHolder;
import ch.epfl.lia.balancednews.NewsActivity;
import ch.epfl.lia.balancednews.R;
import ch.epfl.lia.balancednews.TopicActivity;
import ch.epfl.lia.constants.Constants;
import ch.epfl.lia.model.News;
import ch.epfl.lia.model.Record;
import ch.epfl.lia.text.PhraseLocator;
import ch.epfl.lia.webservice.MessageParser;
import ch.epfl.lia.webservice.NewsService;

/**
 * Created by xtang on 13-10-23.
 */
public class NewsFragment extends Fragment {
    public static final String NEWS_ID="NEWS_ID";
    public static final String TOPIC_ID="TOPIC_ID";
    public static final String IS_INITIAL = "INITIAL";
    private static final String BUTTON_TEXT_WEB = "Read on web";
    private static final String BUTTON_TEXT_PLAIN_TEXT = "Read text";

    private News news;
    private TextView newsTitle;
    private TextView newsContent;
    private TextView newsOpinion;
    private ScrollView scrollView;
    private Button webButton;
    private Button nextOpinionButton;
    private Button prevOpinionButton;
    private WebView webContent;
    private boolean isReadOnWeb = false;
    private PhraseLocator phraseLocator;

    private ArrayList<HashMap<String,Object>> leftNewsItems=new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String,Object>> rightNewsItems=new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter leftAdapter;
    private SimpleAdapter rightAdapter;

    private ArrayList<Integer> newsIDList= TopicActivity.newsIDList;
    private int topicID;
    private int newsID;
    private String newsURL;
    private int currentOpinionIndex = -1;

    private Record record;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.GET_NEWS_COMPLETE:
                    news= MessageParser.toNews((String) msg.obj);
                    newsTitle.setText(news.title);
                    newsContent.setText(Html.fromHtml(news.content));

                    phraseLocator = new PhraseLocator(news.phrases, news.content);
                    phraseLocator.fillLocation();

                    setNewsOpinion(news.opinionValue);
                    scrollView.smoothScrollTo(0,0);
                    newsURL = news.url;
                    displayOpinionButton();
                    initializeIntro();

                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.news_page, container, false);

        //Get passed values
        Bundle args = getArguments();
        topicID=args.getInt(NewsFragment.TOPIC_ID);
        newsID=args.getInt(NewsFragment.NEWS_ID);

        newsTitle=(TextView) rootView.findViewById(R.id.news_title);
        newsContent=(TextView) rootView.findViewById(R.id.news_content);
        newsOpinion=(TextView) rootView.findViewById(R.id.news_opinion);
        scrollView=(ScrollView) rootView.findViewById(R.id.news_scroll_content);
        webButton = (Button) rootView.findViewById(R.id.button_web_content);
        nextOpinionButton = (Button) rootView.findViewById(R.id.next_opinion);
        prevOpinionButton = (Button) rootView.findViewById(R.id.previous_opinion);
        webContent = (WebView) rootView.findViewById(R.id.web_content);

        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReadOnWeb = !isReadOnWeb;

                if(isReadOnWeb){
                    newsTitle.setVisibility(View.GONE);
                    newsContent.setVisibility(View.GONE);
                    webButton.setText(BUTTON_TEXT_PLAIN_TEXT);
                    webContent.setVisibility(View.VISIBLE);
                    webContent.loadUrl(newsURL);
                }else{
                    newsTitle.setVisibility(View.VISIBLE);
                    newsContent.setVisibility(View.VISIBLE);
                    webButton.setText(BUTTON_TEXT_WEB);
                    webContent.setVisibility(View.GONE);
                }
            }
        });

        prevOpinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentOpinionIndex > 0){
                    currentOpinionIndex --;
                    scrollToPhrase(news.phrases.get(currentOpinionIndex));
                }else if(currentOpinionIndex == 0){

                    currentOpinionIndex = -1;
                    if(news.opinionValue<0){
                        newsOpinion.setText("- "+Math.abs(news.opinionValue)+"");
                    }else{
                        newsOpinion.setText("+ "+Math.abs(news.opinionValue)+"");
                    }

                    scrollView.smoothScrollTo(0,0);
                }
                displayOpinionButton();
            }
        });

        nextOpinionButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentOpinionIndex < (news.phrases.size() - 1)){
                    currentOpinionIndex ++;
                    scrollToPhrase(news.phrases.get(currentOpinionIndex));
                }
                displayOpinionButton();

            }
        });



        webContent.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        newsOpinion.setClickable(true);
        newsOpinion.setFocusable(true);
        newsOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);

            }
        });

        new NewsService(handler).getCompleteNews(topicID,newsID);



        return rootView;
    }


    private void setNewsOpinion(int opinionValue){
        if(opinionValue<0){
            newsOpinion.setText("- "+Math.abs(opinionValue)+"");
            newsOpinion.setBackgroundColor(Color.parseColor(Constants.NEGATIVE_COLOR));
        }else{
            newsOpinion.setText("+ "+Math.abs(opinionValue)+"");
            newsOpinion.setBackgroundColor(Color.parseColor(Constants.POSITIVE_COLOR));
        }
    }

    private void showPopupMenu(View view){
        PopupMenu popup;
        popup = new PopupMenu(getActivity(), view);

        int counter = 0;
        for(News.OpinionPhrase phrase: news.phrases){
                popup.getMenu().add(0, counter, Menu.NONE, phrase.phrase);
                counter ++;

        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                scrollToPhrase(news.phrases.get(item.getItemId()));
                return true;
            }
        });
        popup.show();

    }

    private void displayOpinionButton(){

        prevOpinionButton.setVisibility(View.VISIBLE);
        nextOpinionButton.setVisibility(View.VISIBLE);

        if(currentOpinionIndex == -1)
            prevOpinionButton.setVisibility(View.INVISIBLE);


        if(currentOpinionIndex == news.phrases.size() -1)
            nextOpinionButton.setVisibility(View.INVISIBLE);


    }

    private void scrollToPhrase(News.OpinionPhrase phrase){
        int position = phrase.index;
        int contentLine = newsContent.getLayout().getLineForOffset(position);
        int y = (int) ( (contentLine-10) * (newsContent.getLineHeight() - 0.8 ) + newsTitle.getHeight() + 20);

        scrollView.smoothScrollTo(0, y);
        int end = phrase.sentence.length() + position - 1;
        String newContent = null;
        //Log.d("test", " "+position+" "+end+" "+news.phrases.get(item.getItemId()).sentence);
        if(phrase.isPositive)
            newContent = news.content.substring(0,position)+ "<font color='green'>"+phrase.sentence+"</font>" + news.content.substring(end, news.content.length());
        else
            newContent = news.content.substring(0,position) + "<font color='red'>"+phrase.sentence+"</font>" + news.content.substring(end, news.content.length());
        newsContent.setText(Html.fromHtml(newContent));

        newsOpinion.setText(phrase.phrase);
        currentOpinionIndex = news.phrases.indexOf(phrase);
        displayOpinionButton();
    }


    @Override
    public void setUserVisibleHint (boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            record = new Record(Constants.USER_ID.toString(), Record.NEWS_TYPE, new Date());
        }else{
            if(record != null){
                record.setNewsID(newsID);
                record.setTopicID(topicID);
                record.setDuration(new Date());
                record.sendRecord(handler);

                record = null;
            }
        }
    }


    @Override
    public void onStop(){
        if(record != null){
            record.setNewsID(newsID);
            record.setTopicID(topicID);
            record.setDuration(new Date());
            record.sendRecord(handler);
            record = null;
        }

        super.onStop();
    }

    private void initializeIntro(){

        if(NewsActivity.introHolder == null){
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight =  display.getHeight();

            NewsActivity.introHolder = new IntroHolder(getActivity());

            int[] nextButtonLocation = getLocations(nextOpinionButton);
           // int[] prevButtonLocation = getLocations(prevOpinionButton);

            View decorView = getActivity().getWindow().getDecorView();
            View buttonLeftDrawer = decorView.findViewById(R.id.button_left_drawer);
            View buttonRightDrawer = decorView.findViewById(R.id.button_right_drawer);
            int[] leftButtonLocations = getLocations(buttonLeftDrawer);
            int[] rightButtonLocations = getLocations(buttonRightDrawer);


            SpannableString text = new SpannableString("Click or to navigate articles by opinion phrase");
            ImageSpan prevButton = new ImageSpan(getActivity(), R.drawable.ic_action_previous_item_light);
            ImageSpan nextButton = new ImageSpan(getActivity(), R.drawable.ic_action_next_item_light);
            text.setSpan(prevButton, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            text.setSpan(nextButton, 8, 9, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            SpannableString text2 = new SpannableString(" \n Slide to navigate between news");
            ImageSpan slideGesture = new ImageSpan(getActivity(), R.drawable.slide_gesture);

            text2.setSpan(slideGesture,0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


            NewsActivity.introHolder.addIntro(screenWidth-500 , nextOpinionButton.getHeight()+nextButtonLocation[1]+10, text);
            NewsActivity.introHolder.addIntro(leftButtonLocations[0]+10, leftButtonLocations[1]-100,"Click to see positive news list",200);
            NewsActivity.introHolder.addIntro(rightButtonLocations[0]-180, leftButtonLocations[1]-100,"Click to see negative news list",200);
            NewsActivity.introHolder.addIntro(text2, RelativeLayout.CENTER_IN_PARENT);

            NewsActivity.introHolder.next();
        }


    }

    private int[] getLocations(View v){
        int[] locations = new int[2];
        v.getLocationOnScreen(locations);
        return locations;
    }

}
