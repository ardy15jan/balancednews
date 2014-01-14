package ch.epfl.lia.customized;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.R.id.*;

import java.util.List;
import java.util.Map;

import ch.epfl.lia.Common.FontManager;
import ch.epfl.lia.balancednews.R;

/**
 * Created by xtang on 13-10-15.
 */
public class NewsListAdapter extends SimpleAdapter {
    private List<? extends Map<String, ?>> data;
    private Context mContext;
    private String[] from;
    private int[] to;

    public NewsListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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
            convertView = mInflater.inflate(R.layout.news_list_item, null);
            holder.image = (ImageView)convertView.findViewById(R.id.news_item_image);
            holder.title = (TextView)convertView.findViewById(R.id.news_item_title);
            holder.content = (TextView)convertView.findViewById(R.id.news_item_content);


            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.image.setBackgroundResource((Integer)data.get(position).get("itemImage"));
        holder.title.setText((String)data.get(position).get("itemTitle"));
        holder.content.setText(Html.fromHtml((String)data.get(position).get("itemText")));


        FontManager.setRobotoLight(mContext,holder.title);
        FontManager.setRobotoMedium(mContext,holder.content);

        int opinion=(Integer) data.get(position).get("itemOpinion");
        if(opinion<0){
            holder.title.setBackgroundResource(R.drawable.title_background_negative);
        }else{
            holder.title.setBackgroundResource(R.drawable.title_background_positive);
        }

        return convertView;
    }

    class ViewHolder{
        TextView title;
        TextView content;
        ImageView image;

    }
}


