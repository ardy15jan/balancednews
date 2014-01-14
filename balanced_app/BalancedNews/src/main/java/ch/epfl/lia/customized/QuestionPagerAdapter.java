package ch.epfl.lia.customized;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import ch.epfl.lia.balancednews.R;
import ch.epfl.lia.constants.Constants;
import ch.epfl.lia.model.Answer;
import ch.epfl.lia.model.Topic;
import ch.epfl.lia.webservice.NewsService;

/**
 * Created by xtang on 13-12-10.
 */
public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Topic> topics;

    public QuestionPagerAdapter(FragmentManager fm) {
        super(fm);
        topics = (ArrayList<Topic>) Constants.topics.clone();
        Collections.sort(topics, TopicComparatorFactory.getTopicComparator());
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0){
            return new QuestionMainFragment();
        }

        if(i == topics.size()+1){
            return new QuestionEndFragment();
        }

        Fragment fragment = new QuestionFragment();
        Bundle args = new Bundle();

        args.putInt(QuestionFragment.TOPIC_ID, topics.get(i-1).topic_id);
        args.putStringArrayList(QuestionFragment.TAGS, topics.get(i-1).tags);
        args.putInt(QuestionFragment.INDEX, i-1);

        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public int getCount() {
        return topics.size()+2;
    }

    private class QuestionMainFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState){

            View rootView=inflater.inflate(R.layout.question_page_main, container, false);
            return rootView;
        }
    }

    private class QuestionEndFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState){

            View rootView=inflater.inflate(R.layout.question_submit, container, false);
            Button submitButton = (Button) rootView.findViewById(R.id.submit_button);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Answer answer:Constants.answers){
                        new NewsService().sendAnswer(Constants.USER_ID, answer.topic_id, answer.understanding, answer.description, new Date());
                        Log.d("test", ""+answer.topic_id);
                    }

                    Constants.topics.clear();
                    Constants.answers.clear();
                    getActivity().finish();
                }
            });


            return rootView;
        }
    }
}

