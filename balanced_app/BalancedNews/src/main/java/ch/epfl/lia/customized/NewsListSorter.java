package ch.epfl.lia.customized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by xtang on 13-10-22.
 */
public class NewsListSorter {

    public static void negativeFirst(ArrayList<HashMap<String,Object>> newsList){

        Collections.sort(newsList, new NegativeComparator());
    }

    public static void positiveFirst(ArrayList<HashMap<String,Object>> newsList){
        Collections.sort(newsList, new PositiveComparator());
    }

    public static void shuffle(ArrayList<HashMap<String,Object>> newsList){
        //Collections.shuffle(newsList);
        ArrayList<HashMap<String, Object>> positive=new ArrayList<HashMap<String, Object>>();
        ArrayList<HashMap<String, Object>> negative=new ArrayList<HashMap<String, Object>>();

        for(HashMap<String, Object> item:newsList){
            if((Integer)item.get("itemOpinion")<0){
                negative.add(item);
            }else{
                positive.add(item);
            }
        }

        NewsListSorter.positiveFirst(positive);
        NewsListSorter.negativeFirst(negative);

        newsList.clear();

        int i=0;
        while(i<positive.size()&&i<negative.size()){
            newsList.add(positive.get(i));
            newsList.add(negative.get(i));
            i++;
        }

        if(i<positive.size()){
            for(;i<positive.size();i++){
                newsList.add(positive.get(i));
            }
        }

        if(i<negative.size()){
            for(;i<negative.size();i++){
                newsList.add(negative.get(i));
            }
        }

    }

    public static void relevanceFirst(ArrayList<HashMap<String,Object>> newsList){
        Collections.sort(newsList, new RelevanceComparator());
    }

    private static class PositiveComparator implements Comparator<HashMap<String, Object>>{

        @Override
        public int compare(HashMap<String, Object> item1, HashMap<String, Object> item2) {
            int opinion1=(Integer) item1.get("itemOpinion");
            int opinion2=(Integer) item2.get("itemOpinion");

            if(opinion1>opinion2)
                return -1;
            else if(opinion1==opinion2)
                return 0;
            else
                return 1;

        }
    }

    private static class NegativeComparator implements Comparator<HashMap<String, Object>>{

        @Override
        public int compare(HashMap<String, Object> item1, HashMap<String, Object> item2) {
            int opinion1=(Integer) item1.get("itemOpinion");
            int opinion2=(Integer) item2.get("itemOpinion");

            if(opinion1>opinion2)
                return 1;
            else if(opinion1==opinion2)
                return 0;
            else
                return -1;

        }
    }

    private static class RelevanceComparator implements Comparator<HashMap<String, Object>>{

        @Override
        public int compare(HashMap<String, Object> item1, HashMap<String, Object> item2) {
            int opinion1=(Integer) item1.get("itemOpinion");
            int opinion2=(Integer) item2.get("itemOpinion");
            opinion1=Math.abs(opinion1);
            opinion2=Math.abs(opinion2);

            if(opinion1>opinion2)
                return -1;
            else if(opinion1==opinion2)
                return 0;
            else
                return 1;

        }
    }

}
