package ch.epfl.lia.customized;

import java.util.Comparator;

import ch.epfl.lia.model.Topic;

/**
 * Created by xtang on 13-12-10.
 */
public class TopicComparatorFactory {

    public static  TopicIDComparator getTopicComparator(){
        return new TopicIDComparator();
    }

    public static class TopicIDComparator implements Comparator<Topic>{

        @Override
        public int compare(Topic topic1, Topic topic2) {
            if(topic1.topic_id < topic2.topic_id)
                return -1;
            else if(topic1.topic_id == topic2.topic_id)
                return 0;
            else
                return 1;

        }
    }

}
