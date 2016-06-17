package vn.mcbooks.mcbooks.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.BaseActivity;
import vn.mcbooks.mcbooks.activity.AudioPlayerActivity;
import vn.mcbooks.mcbooks.activity.ShowImageActivity;
import vn.mcbooks.mcbooks.activity.YoutubePlayerActivity;
import vn.mcbooks.mcbooks.dialog.SaleOffsDialog;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.Information;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/11/16.
 */
public class BookDetailFragment extends BaseFragment {
    private String des1 = "Tiếng Trung là ngôn ngữ có nhiều người nói nhất trên thế giới, ở Việt Nam đây cũng là một ngôn ngữ có số lượng người học đông đảo và được đưa vào giảng dạy ở bậc phổ thông. Tuy nhiên khó khăn lớn nhất đối với người học tiếng Trung đó là làm quen với chữ cái và từ vựng. Bởi Việt Nam chúng ta sử dụng hệ thống chữ cái khác hắn so với Hán tự của Trung Quốc. Chính vì vậy nhu cầu học chữ và từ vựng tiếng Trung là rất lớn.\n" +
            "\nĐể đáp ứng nhu cầu có công cụ đối chiếu 2 ngôn ngữ Trung - Việt, MCBooks - chuyến sách ngoại ngữ -  đã dày công biên soạn bộ Sổ tay từ vựng tiếng Trung với 3 trình độ A-B-C (từ dễ tới khó) nhằm giúp bạn đọc dễ học và dễ phân chia theo trình độ học của bản thân.";
    private String des2 = "Tiếng Trung là ngôn ngữ có nhiều người nói nhất trên thế giới, ở Việt Nam đây cũng là một ngôn ngữ có số lượng người học đông đảo và được đưa vào giảng dạy ở bậc phổ thông. Tuy nhiên khó khăn lớn nhất đối với người học tiếng Trung đó là làm quen với chữ cái và từ vựng. Bởi Việt Nam chúng ta sử dụng hệ thống chữ cái khác hắn so với Hán tự của Trung Quốc. Chính vì vậy nhu cầu học chữ và từ vựng tiếng Trung là rất lớn...";


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
    private TextView txtDescription;


    public void setmBook(Book mBook) {
        this.mBook = mBook;
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
                    txtDescription.setText(des1);
                } else {
                    isLoadMore = false;
                    txtDescription.setText(des2);
                }
            }
        });
        btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).openActivity(YoutubePlayerActivity.class, new Bundle());
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
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBook.getBuyUrl()));
                        startActivity(browserIntent);
                    }
                }
            });
        }
    }

    private ArrayList<String> buildListImageURL(){
        ArrayList<String> rs = new ArrayList<>();
        rs.add(APIURL.BaseURL + mBook.getImage());
        return rs;
    }


    public static String getShortDetail(Information information){
        String rs;
        rs = "NXB: " + information.getPublisher() + "\n" + "Tác giả: " + information.getAuthor();
        return rs;

    }
}
