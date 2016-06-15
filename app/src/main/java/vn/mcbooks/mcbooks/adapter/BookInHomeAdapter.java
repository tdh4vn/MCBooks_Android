package vn.mcbooks.mcbooks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import vn.mcbooks.mcbooks.R;

import java.util.List;

import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/11/16.
 */
public class BookInHomeAdapter extends RecyclerView.Adapter<BookInHomeAdapter.ViewHolder>{
    //------data
    /*
    //Danh sach Book duoc hien thi
    //Yeu cau duoc truyen vao sau luc khoi tao Recycler view dapter
     */
    private List<Book> listBook;

    //---ref

    private Context mContext;
    private View.OnClickListener onClickListener;

    //--getter/setter


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Book> getListBook() {
        return listBook;
    }

    public void setListBook(List<Book> listBook) {
        this.listBook = listBook;
    }

    //----method overided
    @Override
    public BookInHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_book, parent, false);
        if (onClickListener != null){
            itemView.setOnClickListener(onClickListener);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookInHomeAdapter.ViewHolder holder, int position) {
        Picasso.with(mContext).load(APIURL.BASE_IMAGE_URL + listBook.get(position).getImage() + "&width=150").into(holder.iconBook);
        holder.titleBook.setText(listBook.get(position).getName());
        holder.txtPrice.setText(StringUtils.convertToCurencyUnitStyle(String.valueOf(listBook.get(position).getPrice()))+"đ");
        holder.ratingBar.setText(String.valueOf(listBook.get(position).getRatings().getAvgStar()));
    }

    @Override
    public int getItemCount() {
        return listBook.size();
    }


    //TODO
    //Inner Class
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconBook;
        TextView titleBook;
        TextView ratingBar;
        TextView txtPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            iconBook = (ImageView)itemView.findViewById(R.id.iconBook);
            titleBook = (TextView)itemView.findViewById(R.id.nameBook);
            ratingBar = (TextView) itemView.findViewById(R.id.ratingStar);
            txtPrice = (TextView)itemView.findViewById(R.id.txtPriceBook);
        }
    }
}


