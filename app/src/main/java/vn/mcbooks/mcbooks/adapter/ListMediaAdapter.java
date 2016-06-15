package vn.mcbooks.mcbooks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.model.Media;

/**
 * Created by hungtran on 6/11/16.
 */
public class ListMediaAdapter extends BaseAdapter{

    private List<Media> listMedia;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public ListMediaAdapter(List<Media> listMedia, Context mContext) {
        this.listMedia = listMedia;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setListMedia(List<Media> listMedia) {
        this.listMedia = listMedia;
    }

    @Override
    public int getCount() {
        return listMedia.size();
    }

    @Override
    public Media getItem(int position) {
        return listMedia.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_media, null);
            holder = new ViewHolder();
            holder.iconMedia = (ImageView) convertView.findViewById(R.id.iconMedia);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtAudioName);
            holder.txtDuration = (TextView) convertView.findViewById(R.id.txtTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Media media = this.listMedia.get(position);

        holder.txtName.setText(media.getName());

        holder.txtDuration.setText("--:--");
        return convertView;
    }
    static class ViewHolder {
        ImageView iconMedia;
        TextView txtName;
        TextView txtDuration;
    }
}
