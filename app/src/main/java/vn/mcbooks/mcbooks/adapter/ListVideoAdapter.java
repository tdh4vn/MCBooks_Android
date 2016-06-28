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

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.intef.IDownloader;
import vn.mcbooks.mcbooks.intef.IPlayAudio;
import vn.mcbooks.mcbooks.model.Audio;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.model.MediaInBook;
import vn.mcbooks.mcbooks.model.Video;
import vn.mcbooks.mcbooks.network_api.FavoriteServices;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/11/16.
 */
public class ListVideoAdapter extends BaseAdapter implements View.OnClickListener{

    private List<Video> listMedia;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private IDownloader downloader;
    private int indexPlay = 0;

    public List<Video> getListMedia() {
        return listMedia;
    }

    public int getIndexPlay() {
        return indexPlay;
    }

    public void setIndexPlay(int indexPlay) {
        this.indexPlay = indexPlay;
    }

    public ListVideoAdapter(List<Video> listMedia, Context mContext) {
        this.listMedia = listMedia;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setListMedia(List<Video> listMedia) {
        this.listMedia = listMedia;
    }

    @Override
    public int getCount() {
        return listMedia.size();
    }

    @Override
    public Video getItem(int position) {
        return listMedia.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_video, null);
            holder = new ViewHolder();
            holder.content = (RelativeLayout) convertView.findViewById(R.id.contentPanel);
            holder.iconMedia = (ImageView) convertView.findViewById(R.id.iconMedia);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtAudioName);
            holder.txtDuration = (TextView) convertView.findViewById(R.id.txtTime);
            holder.btnDownload = (ImageButton) convertView.findViewById(R.id.downloadMedia);
            holder.btnFavorite = (ImageButton) convertView.findViewById(R.id.favoriteMedia);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.btnFavorite.setOnClickListener(this);

        Video media = this.listMedia.get(position);
        holder.txtName.setText(media.getName());
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        if (ContentManager.getInstance().checkMediaInFavorite(listMedia.get(position).getIdInServer())){
            holder.btnFavorite.setImageResource(R.drawable.favarite_fill);
            holder.stateOfButtonFavorite = 1;
            bundle.putBoolean("ADD", true);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.favorite_stroke);
            holder.stateOfButtonFavorite = 0;
            bundle.putBoolean("ADD", false);
        }
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.stateOfButtonFavorite == 0){
                    holder.stateOfButtonFavorite = 1;
                    FavoriteServices favoriteServices = ServiceFactory.getInstance().createService(FavoriteServices.class);
                    Call<BaseResult> favoriteCall = favoriteServices.addMediaToFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                            listMedia.get(position).getIdInServer());
                    holder.btnFavorite.setImageResource(R.drawable.favarite_fill);
                    favoriteCall.enqueue(new Callback<BaseResult>() {
                        @Override
                        public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                            if (response.body().getCode() == 1){
                                ContentManager.getInstance().reloadListMediaInFavorite();
                            } else {
                                holder.stateOfButtonFavorite = 0;
                                Toast.makeText(mContext,response.body().getMessage(), Toast.LENGTH_LONG).show();
                                holder.btnFavorite.setImageResource(R.drawable.favorite_stroke);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResult> call, Throwable t) {
                            holder.stateOfButtonFavorite = 0;
                            Toast.makeText(mContext, "Thêm video vào yêu thích thất bại!", Toast.LENGTH_LONG).show();
                            holder.btnFavorite.setImageResource(R.drawable.favorite_stroke);
                        }
                    });
                } else if (holder.stateOfButtonFavorite == 1){
                    holder.stateOfButtonFavorite = 0;
                    holder.btnFavorite.setImageResource(R.drawable.favorite_stroke);
                    FavoriteServices favoriteServices = ServiceFactory.getInstance().createService(FavoriteServices.class);
                    Call<BaseResult> favoriteCall = favoriteServices.removeMediaInFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                            listMedia.get(position).getIdInServer());
                    favoriteCall.enqueue(new Callback<BaseResult>() {
                        @Override
                        public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                            if (response.body().getCode() == 1){
                                ContentManager.getInstance().reloadListMediaInFavorite();
                            } else {
                                holder.stateOfButtonFavorite = 1;
                                Toast.makeText(mContext,response.body().getMessage(), Toast.LENGTH_LONG).show();
                                holder.btnFavorite.setImageResource(R.drawable.favarite_fill);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResult> call, Throwable t) {
                            holder.stateOfButtonFavorite = 1;
                            Toast.makeText(mContext, "Thêm video vào yêu thích thất bại!", Toast.LENGTH_LONG).show();
                            holder.btnFavorite.setImageResource(R.drawable.favarite_fill);
                        }
                    });
                }
            }
        });
        holder.btnFavorite.setTag(bundle);
        if (position == indexPlay) {
            holder.content.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected));
            holder.txtDuration.setText("Đang chạy");
        } else {
            holder.content.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.background_light));
            holder.txtDuration.setText("");
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

    void addOrRemoveMediaFavorite(boolean isAdd, final int pos, ImageButton btn){
        FavoriteServices favoriteServices = ServiceFactory.getInstance().createService(FavoriteServices.class);
        Call<BaseResult> favoriteCall;
        if (isAdd){
            favoriteCall = favoriteServices.addMediaToFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                    listMedia.get(pos).getIdInServer());
            Log.d("MEdia ID1", ""+listMedia.get(pos).getIdInServer());
            btn.setImageResource(R.drawable.favarite_fill);
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", pos);
            bundle.putBoolean("ADD", false);
            btn.setTag(bundle);
        } else {
            favoriteCall = favoriteServices.removeMediaInFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                    listMedia.get(pos).getIdInServer());
            Log.d("MEdia ID2", ""+listMedia.get(pos).getIdInServer());
            ContentManager.getInstance().removeMediaInFavorite(listMedia.get(pos).getIdInServer());
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
                Toast.makeText(mContext, "Thêm video vào yêu thích thất bại!", Toast.LENGTH_LONG).show();
            }
        });
    }

    static class ViewHolder {
        RelativeLayout content;
        int stateOfButtonFavorite = 0;
        int position;
        ImageView iconMedia;
        TextView txtName;
        TextView txtDuration;
        ImageButton btnDownload;
        ImageButton btnFavorite;
    }
}
