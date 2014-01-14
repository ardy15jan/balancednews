package ch.epfl.lia.customized;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import ch.epfl.lia.balancednews.QuestionActivity;
import ch.epfl.lia.constants.Constants;

/**
 * Created by xtang on 13-12-10.
 */
public class CountDown extends Thread {

    private Handler handler;
    private int countDownSeconds;
    private Context context;
    private boolean isValid = true;

    public CountDown(Handler handler, int seconds){
        this.handler = handler;
        this.countDownSeconds = seconds;
    }

    public CountDown(Handler handler, int seconds, Context context){
        this.handler = handler;
        this.countDownSeconds = seconds;
        this.context = context;
    }


    @Override
    public void run(){
        Message msg;
        try {

            sleep((countDownSeconds-60) * 1000);
            if(!isValid)
                return;
            msg = new Message();
            msg.what = Constants.SHOW_NOTIFICATION;
            msg.obj = "1 min left";
            handler.sendMessage(msg);

            sleep(50 * 1000);
            if(!isValid)
                return;
            msg = new Message();
            msg.what = Constants.SHOW_NOTIFICATION;
            msg.obj = "10 seconds left";
            handler.sendMessage(msg);

            sleep(10 * 1000);
            if(!isValid)
                return;
            msg = new Message();
            msg.what = Constants.START_QUESTIONAIR;
            handler.sendMessage(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startCountDown(Handler handler, int seconds){
        new CountDown(handler, seconds).start();
    }

    public void kill(){
        this.isValid = false;
    }
}
