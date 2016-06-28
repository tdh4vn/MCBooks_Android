package vn.mcbooks.mcbooks.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.intef.IDownloader;
import vn.mcbooks.mcbooks.intef.IPlayAudio;
import vn.mcbooks.mcbooks.model.Audio;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.model.MediaInBook;
import vn.mcbooks.mcbooks.network_api.FavoriteServices;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/11/16.
 */
public class ListMediaAdapter extends BaseAdapter implements View.OnClickListener{

    private List<Audio> listMedia;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private IDownloader downloader;
    private int indexPlay = 0;
    private static int TYPE_BUTTON = 2008;
    private static String DELETE = "DELETE";
    private static String DOWNLOAD = "DOWNLOAD";

    public List<Audio> getListMedia() {
        return listMedia;
    }

    public int getIndexPlay() {
        return indexPlay;
    }

    public void setIndexPlay(int indexPlay) {
        this.indexPlay = indexPlay;
    }

    public ListMediaAdapter(List<Audio> listMedia, Context mContext, IDownloader downloader) {
        this.downloader = downloader;
        this.listMedia = listMedia;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setListMedia(List<Audio> listMedia) {
        this.listMedia = listMedia;
    }

    @Override
    public int getCount() {
        return listMedia.size();
    }

    @Override
    public Audio getItem(int position) {
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
            holder.content = (RelativeLayout) convertView.findViewById(R.id.contentPanel);
            holder.iconMedia = (ImageView) convertView.findViewById(R.id.iconMedia);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtAudioName);
            holder.txtDuration = (TextView) convertView.findViewById(R.id.txtTime);
            holder.btnDownload = (ImageButton) convertView.findViewById(R.id.downloadMedia);
            holder.btnFavorite = (ImageButton) convertView.findViewById(R.id.favoriteMedia);
            holder.btnDownload.setOnClickListener(this);
            holder.position = position;
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Audio media = this.listMedia.get(position);
        holder.txtName.setText(media.getName());
        holder.btnDownload.setTag(media);
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        if (ContentManager.getInstance().checkMediaInFavorite(listMedia.get(position).getId())){
            holder.btnFavorite.setImageResource(R.drawable.favarite_fill);
            bundle.putBoolean("ADD", true);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.favorite_stroke);
            bundle.putBoolean("ADD", false);
        }
        holder.btnFavorite.setTag(bundle);
        holder.btnFavorite.setOnClickListener(this);

        if (!media.getLocalURI().equals("null")){
            holder.btnDownload.setImageResource(R.drawable.ic_delete_black_24dp);
        }
        if (position == indexPlay) {
            holder.content.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected));
            holder.txtDuration.setText("");
        } else {
            holder.content.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.background_light));
            holder.txtDuration.setText("");
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.downloadMedia:
                if (((Audio)v.getTag()).getLocalURI().equals("null")){
                    downloader.downloadAudio(((Audio)v.getTag()));
                } else {
                    Audio media = (Audio)v.getTag();
                    File file = new File(media.getLocalURI());
                    if (file.delete()){
                        ((ImageView)v).setImageResource(R.drawable.ic_file_download_blue_24dp);
                        media.setLocalURI("null");
                    }
                }

                break;
            case R.id.favoriteMedia:
                Bundle bundle = (Bundle)v.getTag();
                boolean b = bundle.getBoolean("ADD");
                if (b){
                    addOrRemoveMediaFavorite(false, bundle.getInt("POSITION"), (ImageButton)v);
                } else {
                    addOrRemoveMediaFavorite(true, bundle.getInt("POSITION"), (ImageButton)v);
                }

                break;
            default:
                ((IPlayAudio)downloader).moveToAudio(((ViewHolder)v.getTag()).position);
                break;
        }
    }

    void addOrRemoveMediaFavorite(boolean isAdd, int pos, ImageButton btn){

        FavoriteServices favoriteServices = ServiceFactory.getInstance().createService(FavoriteServices.class);
        Call<BaseResult> favoriteCall;
        if (isAdd){
            Log.d("TRUE", "TRUE");
            favoriteCall = favoriteServices.addMediaToFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                    listMedia.get(pos).getId());
            btn.setImageResource(R.drawable.favarite_fill);
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", pos);
            bundle.putBoolean("ADD", false);
            btn.setTag(bundle);
        } else {
            Log.d("TRUE", "TRUE");
            favoriteCall = favoriteServices.removeMediaInFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                    listMedia.get(pos).getId());
            ContentManager.getInstance().removeMediaInFavorite(listMedia.get(pos).getId());
            btn.setImageResource(R.drawable.favorite_stroke);
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", pos);
            bundle.putBoolean("ADD", true);
            btn.setTag(bundle);
        }
        favoriteCall.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                if (response.body().getCode() == 1){
                    ContentManager.getInstance().reloadListMediaInFavorite();
                } else {
                    Toast.makeText(mContext,response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                Toast.makeText(mContext, "Thêm audio vào yêu thích thất bại!", Toast.LENGTH_LONG).show();
            }
        });
    }

    static class ViewHolder {
        int position;
        RelativeLayout content;
        ImageView iconMedia;
        TextView txtName;
        TextView txtDuration;
        ImageButton btnDownload;
        ImageButton btnFavorite;
    }
}
