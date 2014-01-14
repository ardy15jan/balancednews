package lia.customized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by xtang on 13-10-22.
 */
public class NewsListSorter {

    public  void negativeFirst(ArrayList<HashMap<String,Object>> newsList){
        Collections.sort(newsList, new NegativeComparator());
    }

    public void positiveFirst(ArrayList<HashMap<String,Object>> newsList){
        Collections.sort(newsList, new PositiveComparator());
    }

    public void shuffle(ArrayList<HashMap<String,Object>> newsList){
        Collections.shuffle(newsList);
    }

    public void relevanceFirst(ArrayList<HashMap<String,Object>> newsList){
        Collections.sort(newsList, new RelevanceComparator());
    }

    private class PositiveComparator implements Comparator<HashMap<String, Object>> {

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

    private class NegativeComparator implements Comparator<HashMap<String, Object>> {

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

    private class RelevanceComparator implements Comparator<HashMap<String, Object>> {

        @Override
        public int compare(HashMap<String, Object> item1, HashMap<String, Object> item2) {
            int opinion1=(Integer) item1.get("itemOpinion");
            int opinion2=(Integer) item2.get("itemOpinion");
            opinion1= Math.abs(opinion1);
            opinion2= Math.abs(opinion2);

            if(opinion1>opinion2)
                return -1;
            else if(opinion1==opinion2)
                return 0;
            else
                return 1;

        }
    }

}
