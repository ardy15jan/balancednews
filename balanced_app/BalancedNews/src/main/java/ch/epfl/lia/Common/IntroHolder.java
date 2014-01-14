package ch.epfl.lia.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.lia.balancednews.NewsActivity;
import ch.epfl.lia.balancednews.R;
import ch.epfl.lia.constants.Constants;

/**
 * Created by xtang on 13-12-18.
 */
public class IntroHolder {
    private int counter;
    private ArrayList<Intro> holder = new ArrayList<Intro>();
    private Activity activity;
    private static final int RESERVED_ID = 2013;
    public static final int CENTER_HORIZONTAL = RelativeLayout.CENTER_HORIZONTAL;
    public static final int CENTER_IN_PARENT = RelativeLayout.CENTER_IN_PARENT;

    public IntroHolder(Activity activity){
        this.activity = activity;
    }

    public void addIntro(int x, int y, CharSequence text){
        holder.add(new Intro(x, y, text));
    }

    public void addIntro(int x, int y, CharSequence text, int width){
        holder.add(new Intro(x, y, text, width));
    }

    public void addIntro(CharSequence text, int choice){
        holder.add(new Intro(text, choice));
    }

    public void next(){
        if(counter<holder.size()){
            Intro intro = holder.get(counter++);
            ViewGroup rootView = (ViewGroup)((ViewGroup) activity.getWindow().getDecorView());


            View oldView = rootView.findViewById(RESERVED_ID);
            if(oldView != null){
                rootView.removeView(oldView);
            }

            RelativeLayout relativeLayout = new RelativeLayout(activity);
            relativeLayout.setBackgroundColor(Color.argb(120,0,0,0));
            relativeLayout.setId(RESERVED_ID);

            RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = intro.x;
            params.topMargin = intro.y;
            params.rightMargin = 10;
            if(intro.choice != 0){
                params.addRule(intro.choice);
            }

            TextView textView = new TextView(activity);
            textView.setText(intro.text);
            textView.setBackgroundResource(R.drawable.tag_background);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18);
            textView.setPadding(8, 0, 8, 0);
            textView.setId(RESERVED_ID);
            if(intro.width > 0)
                textView.setWidth(intro.width);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            relativeLayout.addView(textView,params);

            RelativeLayout.LayoutParams buttonParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            buttonParams.bottomMargin = 60;
            Button nextButton = new Button(activity);
            nextButton.setBackgroundResource(R.drawable.tag_background);
            nextButton.setText("Got it!");
            nextButton.setTextSize(20);
            nextButton.setPadding(15,0,15,0);
            nextButton.setTextColor(Color.WHITE);
            nextButton.setWidth(300);
            nextButton.setHeight(60);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NewsActivity.introHolder != null){
                        NewsActivity.introHolder.next();
                    }
                }
            });
            relativeLayout.addView(nextButton, buttonParams);


            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            rootView.addView(relativeLayout);
        }else{

            ViewGroup rootView = (ViewGroup)((ViewGroup) activity.getWindow().getDecorView());
            View oldView = rootView.findViewById(RESERVED_ID);
            if(oldView != null){
                rootView.removeView(oldView);
            }
        }
    }



    private class Intro{
        CharSequence text;
        int x;
        int y;
        int width;
        int choice;

        public Intro(int x, int y, CharSequence text){
            this.x = x;
            this.y = y;
            this.text = text;

        }

        public Intro(int x, int y, CharSequence text, int width){
            this.x = x;
            this.y = y;
            this.text = text;
            this.width = width;
        }

        public Intro(CharSequence text, int choice){
            this.text = text;
            this.choice = choice;
        }
    }

    /*
    private class IntroView extends View{


        public IntroView(Context context){
            super(context);
        }

        public void onDraw(Canvas canvas){
            super.onDraw(canvas);

            switch(highlightType){
                case HIGHLIGHT_BOTTOM:
                    drawLine(canvas);
                    drawInfo(canvas);
                    break;
            }

        }


        private void drawLine(Canvas canvas){
            int[] positions = new int[2];
            target.getLocationOnScreen(positions);
            int x = positions[0];
            int y = positions[1];

            int width = target.getWidth();
            int height = target.getHeight();

            int width = 0;
            int height = 0;

            Paint linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setColor(Color.BLUE);
            linePaint.setStrokeWidth(2);

            canvas.drawLine(x, y+height, x+width, y+height, linePaint);
        }

        private void drawInfo(Canvas canvas){
            int[] positions = new int[2];
            target.getLocationOnScreen(positions);
            int x = positions[0];
            int y = positions[1];

            int width = target.getWidth();
            int height = target.getHeight();


            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLUE);
            textPaint.setTextSize(textSize);
            textPaint.setAntiAlias(true);

            canvas.drawText(text, x, y+height+textSize , textPaint);

            RectF oval = new RectF(x,y+height+textSize, x+)
        }
    }
    */

}
