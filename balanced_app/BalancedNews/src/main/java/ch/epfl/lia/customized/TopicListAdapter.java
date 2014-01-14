package ch.epfl.lia.customized;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.R.id.*;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.lia.Common.FontManager;
import ch.epfl.lia.balancednews.R;
import ch.epfl.lia.constants.Constants;
import ch.epfl.lia.model.News;

/**
 * Created by xtang on 13-10-15.
 */
public class TopicListAdapter extends SimpleAdapter {
    private List<? extends Map<String, ?>> data;
    private Context mContext;
    private String[] from;
    private int[] to;

    public TopicListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data=data;
        this.mContext=context;
        this.from=from;
        this.to=to;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if (convertView == null) {

            holder=new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.image = (ImageView)convertView.findViewById(R.id.topic_image);
            holder.titlePositive = (TextView)convertView.findViewById(R.id.topic_title_positive);
            holder.titleNegative = (TextView)convertView.findViewById(R.id.topic_title_negative);
            holder.content = (TextView)convertView.findViewById(R.id.topic_content);
            holder.tag1 = (TextView)convertView.findViewById(R.id.tag1);
            holder.tag2 = (TextView)convertView.findViewById(R.id.tag2);
            holder.tag3 = (TextView)convertView.findViewById(R.id.tag3);
            holder.topicNum = (TextView) convertView.findViewById(R.id.topic_num);
            holder.circle = (OpinionCircle) convertView.findViewById(R.id.circle_progressbar);
            holder.opinionLable = (TextView) convertView.findViewById(R.id.opinion_label);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.image.setBackgroundResource((Integer)data.get(position).get("itemImage"));

        holder.titlePositive.setText((String) data.get(position).get("itemTitlePositive"));
        holder.titleNegative.setText((String) data.get(position).get("itemTitleNegative"));
        holder.content.setText((String)data.get(position).get("itemText"));
        holder.tag1.setText(((ArrayList<String>)data.get(position).get("itemTags")).get(0));
        holder.tag2.setText(((ArrayList<String>)data.get(position).get("itemTags")).get(1));
        holder.tag3.setText(((ArrayList<String>)data.get(position).get("itemTags")).get(2));
        holder.topicNum.setText("SUBJECT "+(position+1));
        holder.circle.setPositiveProgress((Integer)data.get(position).get("itemPercentage"));

        if((Integer)data.get(position).get("itemPercentage")< 50){

            setAlignBelow(holder.titlePositive, holder.titleNegative);
            holder.opinionLable.setText("Negative");
            holder.opinionLable.setTextColor(Constants.RGB_RED_DARK);
        }else{

            setAlignBelow(holder.titleNegative, holder.titlePositive);
            holder.opinionLable.setText("Positive");
            holder.opinionLable.setTextColor(Constants.RGB_GREEN_DARK);
        }
        holder.opinionLable.setTypeface(FontManager.getRobotoLight(mContext));

        FontManager.setRobotoLight(mContext,holder.titlePositive);
        FontManager.setRobotoLight(mContext,holder.titleNegative);

        holder.titlePositive.setBackgroundResource(R.drawable.title_background_positive);
        holder.titleNegative.setBackgroundResource(R.drawable.title_background_negative);
        holder.circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpinionCircle circle = (OpinionCircle) v;
                String message;
                if(circle.isPositive())
                    message = circle.getPositiveProgress()+"% articles hold positive opinions";
                else{
                    message = (100-circle.getPositiveProgress())+"% articles hold negative opinions";
                }
                Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }



    private void setAlignBelow(View view, View anchor){
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) anchor.getLayoutParams();
        params1.addRule(RelativeLayout.BELOW, 0);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, anchor.getId());
    }

    class ViewHolder{
        TextView titlePositive;
        TextView titleNegative;
        TextView content;
        ImageView image;
        TextView tag1;
        TextView tag2;
        TextView tag3;
        TextView topicNum;
        TextView opinionLable;
        OpinionCircle circle;

    }
}

