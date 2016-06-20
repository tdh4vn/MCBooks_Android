package vn.mcbooks.mcbooks.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.BaseActivity;
import vn.mcbooks.mcbooks.activity.AudioPlayerActivity;
import vn.mcbooks.mcbooks.activity.ShowImageActivity;
import vn.mcbooks.mcbooks.activity.YoutubePlayerActivity;
import vn.mcbooks.mcbooks.dialog.SaleOffsDialog;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.ITabLayoutManager;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.Information;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.network_api.FavoriteServices;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/11/16.
 */
public class BookDetailFragment extends BaseFragment {
    public static final String BOOK = "BOOK";
    //---------data
    private Book mBook;
    boolean isLoadMore = false;

    //------------view
    private ImageView iconBook;
    private Button btnReadPreview;
    private TextView txtBookName;
    private TextView txtShortDetail;
    private TextView txtRatingStar;
    private TextView txtPrice;
    private Button btnMedia;
    private Button btnYoutube;
    private Button btnBuy;
    private ImageButton btnSaleOff;
    private Button btnReadMore;
    private ImageButton btnFavorite;
    private TextView txtDescription;


    public void setmBook(Book mBook) {
        this.mBook = mBook;
    }

    public static BookDetailFragment create(Book mBook, String title){
        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        bookDetailFragment.titles = title;
        bookDetailFragment.mBook = mBook;
        return bookDetailFragment;
    }


    public static BookDetailFragment create(Book mBook){
        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        bookDetailFragment.mBook = mBook;
        return bookDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        initView(view);

        if (!titles.equals("")){
            configToolBar();
        } else {
            titles = mBook.getCategories().get(0).getName();
            configToolBar();
        }
        return view;
    }

    private void initView(View view){
        btnReadMore = (Button) view.findViewById(R.id.btnReadMore);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        iconBook = (ImageView)view.findViewById(R.id.iconBook);
        btnReadPreview = (Button)view.findViewById(R.id.btnRead);
        txtBookName = (TextView)view.findViewById(R.id.bookName);
        txtShortDetail = (TextView)view.findViewById(R.id.shortDetail);
        txtRatingStar = (TextView)view.findViewById(R.id.ratingStar);
        txtPrice = (TextView)view.findViewById(R.id.price);
        btnMedia = (Button)view.findViewById(R.id.btnAudio);
        btnYoutube = (Button) view.findViewById(R.id.btnVideo);
        btnBuy = (Button)view.findViewById(R.id.btnBuy);
        btnSaleOff = (ImageButton)view.findViewById(R.id.btnSaleOffs);
        btnFavorite = (ImageButton)view.findViewById(R.id.favorite);
        mBook.setFavorite(ContentManager.getInstance().checkBookInFavorite(mBook.getId()));
        txtDescription.setText(mBook.getInformation().getDescription().substring(0, 150) + "...");
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteServices favoriteServices = ServiceFactory.getInstance().createService(FavoriteServices.class);
                Call<BaseResult> favoriteCall;
                if (!mBook.isFavorite()){
                    favoriteCall = favoriteServices.addBookToFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                            mBook.getId());
                    mBook.setFavorite(true);
                    btnFavorite.setImageResource(R.drawable.favarite_fill);
                } else {
                    favoriteCall = favoriteServices.removeBookInFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                            mBook.getId());
                    ContentManager.getInstance().removeBookInFavorite(mBook.getId());
                    mBook.setFavorite(false);
                    btnFavorite.setImageResource(R.drawable.favorite_stroke);
                }
                favoriteCall.enqueue(new Callback<BaseResult>() {
                    @Override
                    public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                        if (response.body().getCode() == 1){
                            ContentManager.getInstance().addBookToFavorite(mBook);
                        } else {
                            showToast(response.body().getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                    @Override
                    public void onFailure(Call<BaseResult> call, Throwable t) {
                        showToast("Thêm sách vào yêu thích thất bại!", Toast.LENGTH_LONG);
                    }
                });
            }
        });
        if (mBook.getSaleOffs().size() > 0){
            btnSaleOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaleOffsDialog saleOffsDialog = new SaleOffsDialog();
                    saleOffsDialog.setSaleOffs(mBook.getSaleOffs());
                    ((IOpenFragment)getActivity()).openDialogFragment(saleOffsDialog);
                }
            });

        } else {
            btnSaleOff.setVisibility(View.INVISIBLE);
        }
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoadMore){
                    isLoadMore = true;
                    txtDescription.setText(mBook.getInformation().getDescription());
                    btnReadMore.setText("Thu gọn");
                } else {
                    isLoadMore = false;
                    txtDescription.setText(mBook.getInformation().getDescription().substring(0, 150) + "...");
                    btnReadMore.setText("Xem thêm");
                }
            }
        });

        iconBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ShowImageActivity.BUNDLE_IMAGES, buildListImageURL());
                ((BaseActivity)getActivity()).openActivity(ShowImageActivity.class, bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBook != null){
            Picasso.with(getContext()).load(APIURL.BASE_IMAGE_URL + mBook.getImage() + "&width=200").into(iconBook);
            txtBookName.setText(mBook.getName());
            txtShortDetail.setText(getShortDetail(mBook.getInformation()));
            txtRatingStar.setText(String.valueOf(mBook.getRatings().getAvgStar()));
            txtPrice.setText(StringUtils.convertToCurencyUnitStyle(String.valueOf(mBook.getPrice()))+"đ");
            if (!mBook.isFavorite()){
                btnFavorite.setImageResource(R.drawable.favorite_stroke);
            } else {
                btnFavorite.setImageResource(R.drawable.favarite_fill);
            }
            btnReadPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPdfFragment viewPdfFragment = new ViewPdfFragment();
                    viewPdfFragment.setPdfURL(mBook.getPreview());
                    ((IOpenFragment)getActivity()).openFragment(viewPdfFragment, true);
                }
            });
            btnMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AudioPlayerActivity.class);
                    intent.putExtra(BOOK, mBook);
                    startActivity(intent);
                }
            });
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBook.getBuyUrl().equals("") || mBook.getBuyUrl() == null){
                        showToast("Chưa có thông tin nơi bán!", Toast.LENGTH_LONG);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri. parse(mBook.getBuyUrl()));
                        startActivity(browserIntent);
                    }
                }
            });
            btnYoutube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(YoutubePlayerActivity.BOOK_KEY, mBook);
                    ((BaseActivity)getActivity()).openActivity(YoutubePlayerActivity.class, bundle);
                }
            });
            if (countMediaOnList(mBook.getMedias(), Media.AUDIO_TYPE) == 0){
                btnMedia.setEnabled(false);
                btnMedia.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
            }
            if (countMediaOnList(mBook.getMedias(), Media.VIDEO_TYPE) == 0){
                btnYoutube.setEnabled(false);
                btnYoutube.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
            }

        }
    }

    private ArrayList<String> buildListImageURL(){
        ArrayList<String> rs = new ArrayList<>();
        rs.add(APIURL.BaseURL + mBook.getImage());
        return rs;
    }

    private int countMediaOnList(List<Media> mediaList, int mediaType){
        int countMedia = 0;
        for (Media media : mediaList){
            if (media.getType() == mediaType){
                countMedia++;
            }
        }
        return  countMedia;
    }


    public static String getShortDetail(Information information){
        String rs;
        rs = "NXB: " + information.getPublisher() + "\n" + "Tác giả: " + information.getAuthor();
        return rs;

    }
}
