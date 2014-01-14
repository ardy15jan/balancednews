package lia.customized;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by xtang on 13-12-10.
 */
public class ActivityHolder {

    private ArrayList<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void finishAll(){
        for(Activity activity: activities){
            activity.finish();
        }
    }
}
