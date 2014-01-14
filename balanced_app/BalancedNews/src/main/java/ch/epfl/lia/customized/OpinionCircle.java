package ch.epfl.lia.customized;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import ch.epfl.lia.Common.FontManager;
import ch.epfl.lia.constants.Constants;

/**
 * Created by xtang on 13-11-27.
 */
public class OpinionCircle extends View {

    private int maxProgress = 100;
    private int positiveProgress = 30;
    private int progressStrokeWidth = 2;

    RectF oval;
    Paint paint;

    public OpinionCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        oval = new RectF();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if(width!=height)
        {
            int min=Math.min(width, height);
            width=min;
            height=min;
        }

        paint.setTypeface(FontManager.getRobotoCondensedLight(getContext()));
        paint.setAntiAlias(true);
        paint.setColor(Constants.RGB_RED_DARK);
        canvas.drawColor(Color.TRANSPARENT);

        paint.setStrokeWidth(progressStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        oval.left = progressStrokeWidth / 2;
        oval.top = progressStrokeWidth / 2;
        oval.right = width - progressStrokeWidth / 2;
        oval.bottom = height - progressStrokeWidth / 2;


        paint.setColor(Color.TRANSPARENT);
        canvas.drawArc(oval, -90, 10, false, paint);
        paint.setColor(Constants.RGB_GREEN_DARK);
        canvas.drawArc(oval, -80, ((float) positiveProgress / maxProgress) * 360-10, false, paint);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawArc(oval, ((float) positiveProgress / maxProgress) * 360-90, 10, false, paint);
        paint.setColor(Constants.RGB_RED_DARK);
        canvas.drawArc(oval, ((float) positiveProgress / maxProgress) * 360-80, 360-(((float) positiveProgress / maxProgress) * 360)-10, false, paint);

        String text = "";
        if(positiveProgress >= 50){
            paint.setColor(Constants.RGB_GREEN_DARK);
            text = positiveProgress + "%";
        }else{
            paint.setColor(Constants.RGB_RED_DARK);
            text = (100-positiveProgress) + "%";
        }

        paint.setStrokeWidth(1);

        int textHeight = (int) (height / 2.5);
        paint.setTextSize(textHeight);
        int textWidth = (int) paint.measureText(text, 0, text.length());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 +textHeight/2, paint);
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setPositiveProgress(int progress) {
        this.positiveProgress = progress;
        this.invalidate();
    }

    //For thread not in UI
    public void setProgressNotInUiThread(int progress) {
        this.positiveProgress = progress;
        this.postInvalidate();
    }

    public int getPositiveProgress(){
        return this.positiveProgress;
    }

    public boolean isPositive(){
        return positiveProgress >= 50;
    }


}
