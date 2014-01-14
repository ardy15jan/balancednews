package lia.constants;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lia.customized.ActivityHolder;
import lia.model.Answer;
import lia.model.Topic;

/**
 * Created by xtang on 13-10-8.
 */
public class Constants {

    public static final String URL="http://128.179.163.163:8084/BalancedNewsWebservice/BalancedNewsService?";

    public static final int GET_TOPIC_IDS=0;
    public static final int GET_TOPIC=1;
    public static final int GET_NEWS=2;
    public static final int GET_OPINION=3;
    public static final int GET_NEWS_COMPLETE=4;
    public static final int INSERT_RECORD=5;
    public static final int START_QUESTIONAIR = 6;
    public static final int SEND_ANSWER = 7;
    public static final int SHOW_NOTIFICATION = 8;

    public static final int LIST_DIVIDER_HEIGHT_LARGE=18;
    public static final int LIST_DIVIDER_HEIGHT_MEDIUM=12;
    public static final int LIST_DIVIDER_HEIGHT_SMALL=8;

    public static final String DEFAULT_CATEGORY="Economy";
    public static final String NAVIGATION_IN_MAIN = "Subject Today";

    public static final int RETRY_NUMBER=5;

    public static final String POSITIVE_COLOR="#99CC00";
    public static final String NEGATIVE_COLOR="#CC0000";

    public static UUID UUID;
    public static String USER_ID = "-1";

    public static final int VERSION = "dummy".hashCode();

    public static final int RGB_GREEN_LIGHT = Color.rgb(153, 204, 0);
    public static final int RGB_RED_LIGHT = Color.rgb(255, 68, 68);

    public static final int RGB_GREEN_DARK = Color.rgb(143, 212, 0);
    public static final int RGB_RED_DARK = Color.rgb(204, 0, 0);


    public static final String IS_FIRST_TIME = "FIRST_TIME";
    public static final String PREF = "PREF";

    public static ActivityHolder activityHolder = new ActivityHolder();

    public static boolean showIntroMain = true;
    public static boolean showIntroTopic = true;
    public static boolean showIntroNews = true;

    public static ArrayList<Topic> topics = new ArrayList<Topic>();
    public static Set<Answer> answers = new HashSet<Answer>();

    public static int COUNT_DOWN_SECONDS = (int) (10 * 60);

    public static boolean isShwonQuestionnaire = false;
}
