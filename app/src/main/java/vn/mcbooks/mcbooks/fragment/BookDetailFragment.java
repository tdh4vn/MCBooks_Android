package vn.mcbooks.mcbooks.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.BaseActivity;
import vn.mcbooks.mcbooks.activity.AudioPlayerActivity;
import vn.mcbooks.mcbooks.activity.LoginActivity;
import vn.mcbooks.mcbooks.activity.ShowImageActivity;
import vn.mcbooks.mcbooks.activity.YoutubePlayerActivity;
import vn.mcbooks.mcbooks.adapter.ListReviewAdapter;
import vn.mcbooks.mcbooks.dialog.SaleOffsDialog;
import vn.mcbooks.mcbooks.image_helper.CircleTransform;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.ITabLayoutManager;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.GetRatingResult;
import vn.mcbooks.mcbooks.model.Information;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.model.RatingViewModel;
import vn.mcbooks.mcbooks.model.UserRating;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.network_api.FavoriteServices;
import vn.mcbooks.mcbooks.network_api.RatingServices;
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
    private int pageComment = 1;

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
    private ImageView imgAvatar;
    private RatingBar ratingBar;
    private ListView listRating;
    private ArrayList<RatingViewModel> listRatingViewModel;
    private EditText edtContentReview;
    private Button btnCancle;
    private Button btnSubmit;
    private LinearLayout frmRating;

    //----frm My COmmment
    private RelativeLayout frmMyComment;
    private ImageView imgAvatarMyComment;
    private TextView txtUserNameMyComment;
    private TextView ratingStarMyComment;
    private TextView timeComment;
    private TextView myComment;

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


        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingStarComment);
        listRating = (ListView) view.findViewById(R.id.listReview);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });

        edtContentReview = (EditText)view.findViewById(R.id.edtQuickReview);
        btnCancle = (Button) view.findViewById(R.id.btnCancle);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingServices ratingServices
                        = ServiceFactory.getInstance().createService(RatingServices.class);
                if (!TextUtils.isEmpty(edtContentReview.getText())) {
                    Call<BaseResult> call
                            = ratingServices.ratingBookByID(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), mBook.getId(), (int) ratingBar.getRating(), edtContentReview.getText().toString());
                    call.enqueue(new Callback<BaseResult>() {
                        @Override
                        public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                            if (response.body().getCode() != 1){
                                showToast(response.body().getMessage(), Toast.LENGTH_LONG);
                            } else {
                                showToast("Cảm ơn bạn đã đánh giá!", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResult> call, Throwable t) {
                            showToast("Bạn vui lòng đăng nhập lại hoặc kiểm tra kết nối mạng", Toast.LENGTH_LONG);
                        }
                    });
                }
            }
        });

        frmRating = (LinearLayout) view.findViewById(R.id.frmRating);
        imgAvatarMyComment = (ImageView)view.findViewById(R.id.imgAvatarMyComment);
        frmMyComment = (RelativeLayout) view.findViewById(R.id.frmMyComment);
        txtUserNameMyComment = (TextView) view.findViewById(R.id.txtUserNameMyComment);
        ratingStarMyComment = (TextView) view.findViewById(R.id.ratingStarMyComment);
        timeComment = (TextView) view.findViewById(R.id.timeComment);
        myComment = (TextView) view.findViewById(R.id.yourComment);

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

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, AppCompatActivity.MODE_PRIVATE);
            String urlImg = sharedPreferences.getString(LoginActivity.KEY_AVATAR, "");
            String idUser = sharedPreferences.getString(LoginActivity.KEY_ID, "");
            final String userName = sharedPreferences.getString(LoginActivity.KEY_NAME, "");

            if (urlImg.equals("")) {
                Picasso.with(getActivity()).load("http://mcbooks.vn/images/blogo.png").transform(new CircleTransform()).into(imgAvatar);
            } else {
                Picasso.with(getActivity()).load(urlImg).transform(new CircleTransform()).into(imgAvatar);
            }

            listRatingViewModel = new ArrayList<>();


            if (mBook.getRatings().getMyRating() != null && mBook.getRatings().getMyRating().getComment() != null){
                frmRating.setVisibility(View.GONE);
                if (urlImg.equals("")) {
                    Picasso.with(getActivity()).load("http://mcbooks.vn/images/blogo.png").transform(new CircleTransform()).into(imgAvatarMyComment);
                } else {
                    Picasso.with(getActivity()).load(urlImg).transform(new CircleTransform()).into(imgAvatarMyComment);
                }
                txtUserNameMyComment.setText(userName);
                ratingStarMyComment.setText(StringUtils.ratingLabel((int)((float)mBook.getRatings().getMyRating().getStars())) + " \u2109");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                timeComment.setText(dateFormat.format(mBook.getRatings().getMyRating().getCreateAt()));
                myComment.setText(mBook.getRatings().getMyRating().getComment());
            } else {
                frmMyComment.setVisibility(View.GONE);
            }

            RatingServices ratingServices = ServiceFactory.getInstance().createService(RatingServices.class);
            pageComment = 1;
            Call<GetRatingResult> ratingResultCall = ratingServices.getRatingBookByID(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), mBook.getId(), pageComment);
            ratingResultCall.enqueue(new Callback<GetRatingResult>() {
                @Override
                public void onResponse(Call<GetRatingResult> call, Response<GetRatingResult> response) {
                    if (response.body().getCode() == 1){
                        if (response.body().getResult()!=null || response.body().getResult().size() > 0){
                            for (UserRating userRating : response.body().getResult()){
                                listRatingViewModel.add(
                                        new RatingViewModel(userRating.getId(), userRating.getAssessor().getName(), userRating.getComment(), userRating.getStars(), userRating.getAssessor().getAvatar()));
                            }
                            pageComment++;
                            setAdapterForListComment();
                        }
                    } else {
                        showToast(response.body().getMessage(), Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<GetRatingResult> call, Throwable t) {
                    showToast("Có lỗi xảy ra, vui lòng kiểm tra lại mạng và đăng nhập lại", Toast.LENGTH_LONG);
                }
            });

        }
    }

    private void setAdapterForListComment(){
        ListReviewAdapter listReviewAdapter = new ListReviewAdapter(listRatingViewModel, getActivity());
        listRating.setAdapter(listReviewAdapter);
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
