package vn.mcbooks.mcbooks.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.intef.IDownloader;
import vn.mcbooks.mcbooks.intef.IPlayAudio;
import vn.mcbooks.mcbooks.model.Audio;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.MediaInBook;
import vn.mcbooks.mcbooks.network_api.FavoriteServices;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/18/16.
 */
public class ListMediaInBookAdapter extends BaseAdapter{

    private List<MediaInBook> listMedia;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private int indexPlay = 0;

    public List<MediaInBook> getListMedia() {
        return listMedia;
    }

    public int getIndexPlay() {
        return indexPlay;
    }

    public void setIndexPlay(int indexPlay) {
        this.indexPlay = indexPlay;
    }

    public ListMediaInBookAdapter(List<MediaInBook> listMedia, Context mContext) {
        this.listMedia = listMedia;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setListMedia(List<MediaInBook> listMedia) {
        this.listMedia = listMedia;
    }

    @Override
    public int getCount() {
        return listMedia.size();
    }

    @Override
    public MediaInBook getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.item_media_in_book, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtAudioName);
            holder.txtBookName = (TextView) convertView.findViewById(R.id.txtBookName);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MediaInBook media = this.listMedia.get(position);
        holder.txtName.setText(media.getName());
        holder.txtBookName.setText(media.getBook().getName());
        return convertView;
    }


    static class ViewHolder {
        int position;
        TextView txtName;
        TextView txtBookName;
    }
}


