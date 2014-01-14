package lia.customized;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lia.Common.FontManager;
import ch.epfl.lia.balancednewsdumb.R;

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
            holder.title = (TextView)convertView.findViewById(R.id.topic_title);
            holder.content = (TextView)convertView.findViewById(R.id.topic_content);
            holder.tag1 = (TextView)convertView.findViewById(R.id.tag1);
            holder.tag2 = (TextView)convertView.findViewById(R.id.tag2);
            holder.tag3 = (TextView)convertView.findViewById(R.id.tag3);
            holder.topicNum = (TextView) convertView.findViewById(R.id.topic_num);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.image.setBackgroundResource((Integer)data.get(position).get("itemImage"));
        holder.title.setText((String)data.get(position).get("itemTitle"));
        holder.content.setText((String)data.get(position).get("itemText"));
        holder.tag1.setText(((ArrayList<String>)data.get(position).get("itemTags")).get(0));
        holder.tag2.setText(((ArrayList<String>)data.get(position).get("itemTags")).get(1));
        holder.tag3.setText(((ArrayList<String>)data.get(position).get("itemTags")).get(2));
        holder.topicNum.setText("SUBJECT "+(position+1));

        FontManager.setRobotoLight(mContext,holder.title);



        return convertView;
    }

    class ViewHolder{
        TextView title;
        TextView content;
        ImageView image;
        TextView tag1;
        TextView tag2;
        TextView tag3;
        TextView topicNum;

    }
}

