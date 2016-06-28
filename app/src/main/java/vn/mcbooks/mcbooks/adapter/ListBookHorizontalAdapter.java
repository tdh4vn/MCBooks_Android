package vn.mcbooks.mcbooks.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.RatingViewModel;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/22/16.
 */
public class ListBookHorizontalAdapter extends BaseAdapter{
    private ArrayList<Book> listBook;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public ListBookHorizontalAdapter(ArrayList<Book> listBook, Context mContext) {
        this.listBook = listBook;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<Book> getListBook() {
        return listBook;
    }

    public void setListBook(ArrayList<Book> listBook) {
        this.listBook = listBook;
    }

    @Override
    public int getCount() {
        return listBook.size();
    }

    @Override
    public Book getItem(int position) {
        return listBook.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_book_listview, null);
            holder = new ViewHolder();
            holder.iconBook = (ImageView) convertView.findViewById(R.id.iconBook);
            holder.txtBookName = (TextView) convertView.findViewById(R.id.txtBookName);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPriceBook);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingStar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(APIURL.BaseURL + listBook.get(position).getImage()).into(holder.iconBook);
        holder.txtBookName.setText(listBook.get(position).getName());
        holder.txtPrice.setText(StringUtils.convertToCurencyUnitStyle(String.valueOf(listBook.get(position).getPrice())) + "đ");
        holder.ratingBar.setRating(listBook.get(position).getRatings().getAvgStar());
        return convertView;
    }

    public class ViewHolder{
        public ImageView iconBook;
        public TextView txtBookName;
        public TextView txtPrice;
        public RatingBar ratingBar;
    }
}
