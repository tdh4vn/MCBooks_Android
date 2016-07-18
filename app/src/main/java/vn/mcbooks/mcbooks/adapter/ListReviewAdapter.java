package vn.mcbooks.mcbooks.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.model.RatingViewModel;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.utils.AwesomeTextView;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/21/16.
 */
public class ListReviewAdapter extends BaseAdapter {
    private ArrayList<RatingViewModel> listRating;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public ListReviewAdapter(ArrayList<RatingViewModel> listRating, Context mContext) {
        this.listRating = listRating;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<RatingViewModel> getListRating() {
        return listRating;
    }

    public void setListRating(ArrayList<RatingViewModel> listRating) {
        this.listRating = listRating;
    }

    @Override
    public int getCount() {
        return listRating.size();
    }

    @Override
    public RatingViewModel getItem(int position) {
        return listRating.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_comment, null);
            holder = new ViewHolder();
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
            holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUsername);
            holder.txtRaingStars = (AwesomeTextView) convertView.findViewById(R.id.ratingStar);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            holder.timeRate = (TextView) convertView.findViewById(R.id.timeRating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //TODO
        Picasso.with(mContext).load(listRating.get(position).getAvatar()).into(holder.imgAvatar);
        holder.txtUsername.setText(listRating.get(position).getName());
        Log.d("Nhan Xinh Nhat", listRating.get(position).getName());
        holder.txtComment.setText(listRating.get(position).getComment());
        holder.timeRate.setText(new SimpleDateFormat("dd/MM/yyyy").format(listRating.get(position).getTimeRating()));
        holder.txtRaingStars.setText(StringUtils.ratingLabel(listRating.get(position).getStars(), mContext));
        return convertView;
    }

    public class ViewHolder{
        public TextView timeRate;
        public ImageView imgAvatar;
        public TextView txtUsername;
        public TextView txtRaingStars;
        public TextView txtComment;
    }
}
