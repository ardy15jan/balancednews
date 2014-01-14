package ch.epfl.lia.customized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by xtang on 13-11-27.
 */
public class TopicListSorter {

    public static void sortByID(ArrayList<HashMap<String,Object>> topicList){

        Collections.sort(topicList, new TopicComparator());
    }

    private static class TopicComparator implements Comparator<HashMap<String, Object>> {

        @Override
        public int compare(HashMap<String, Object> item1, HashMap<String, Object> item2) {
            int id1=(Integer) item1.get("itemID");
            int id2=(Integer) item2.get("itemID");

            if(id1<id2)
                return -1;
            else if(id1==id2)
                return 0;
            else
                return 1;

        }
    }
}
