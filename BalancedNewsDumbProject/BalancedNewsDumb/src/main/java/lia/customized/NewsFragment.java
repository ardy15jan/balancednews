package lia.customized;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import ch.epfl.lia.balancednewsdumb.R;
import lia.balancednewsdump.TopicActivity;
import lia.constants.Constants;
import lia.model.News;
import lia.model.Record;
import lia.webservice.MessageParser;
import lia.webservice.NewsService;

/**
 * Created by xtang on 13-10-23.
 */
public class NewsFragment extends Fragment {
    public static final String NEWS_ID="NEWS_ID";
    public static final String TOPIC_ID="TOPIC_ID";

    private News news;
    private TextView newsTitle;
    private TextView newsContent;

    private ScrollView scrollView;

    private Record record;


    private ArrayList<Integer> newsIDList= TopicActivity.newsIDList;
    private int topicID;
    private int newsID;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.GET_NEWS_COMPLETE:
                    news= MessageParser.toNews((String) msg.obj);
                    newsTitle.setText(news.title);
                    newsContent.setText(Html.fromHtml(news.content));

                    scrollView.smoothScrollTo(0,0);

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

        scrollView=(ScrollView) rootView.findViewById(R.id.news_scroll_content);

        new NewsService(handler).getCompleteNews(topicID,newsID);

        return rootView;
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

}
