package vn.mcbooks.mcbooks.dialog;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.AudioPlayerActivity;
import vn.mcbooks.mcbooks.activity.BaseActivity;
import vn.mcbooks.mcbooks.activity.YoutubePlayerActivity;
import vn.mcbooks.mcbooks.eventbus.SetIsReadyQRCodeEvent;
import vn.mcbooks.mcbooks.fragment.BookDetailFragment;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShortReviewBookDialog extends DialogFragment implements View.OnClickListener{
    private String barcode;

    Book mBook;
    ImageView iconBook;
    TextView titlesBook;
    TextView detailBook;
    Button btnAudio;
    Button btnVideo;

    public static ShortReviewBookDialog create(Book book){
        ShortReviewBookDialog shortReviewBookDialog = new ShortReviewBookDialog();
        shortReviewBookDialog.mBook = book;
        return shortReviewBookDialog;
    }

    public ShortReviewBookDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_short_review_book_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iconBook = (ImageView) view.findViewById(R.id.iconBook);
        titlesBook = (TextView) view.findViewById(R.id.bookName);
        detailBook = (TextView) view.findViewById(R.id.shortDetail);
        btnAudio = (Button) view.findViewById(R.id.btnAudio);
        btnVideo = (Button) view.findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        if (countMediaOnList(mBook.getMedias(), Media.AUDIO_TYPE) == 0){
            btnAudio.setEnabled(false);
            btnAudio.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
        }
        if (countMediaOnList(mBook.getMedias(), Media.VIDEO_TYPE) == 0){
            btnVideo.setEnabled(false);
            btnVideo.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
        }
        showDataToView();
    }

    private void showDataToView() {
        Picasso.with(getActivity()).load(APIURL.BaseURL + mBook.getImage()).into(iconBook);
        titlesBook.setText(mBook.getName());
        detailBook.setText(StringUtils.getShortDetail(mBook.getInformation()));
        getDialog().setTitle("Kết quả");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnAudio:
                Intent intent = new Intent(getActivity(), AudioPlayerActivity.class);
                intent.putExtra(BookDetailFragment.BOOK, mBook);
                startActivity(intent);
                break;
            case R.id.btnVideo:
                Bundle bundle = new Bundle();
                bundle.putSerializable(YoutubePlayerActivity.BOOK_KEY, mBook);
                ((BaseActivity)getActivity()).openActivity(YoutubePlayerActivity.class, bundle);
                break;
            default:
                break;
        }
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

    @Override
    public void onCancel(DialogInterface dialog) {
        EventBus.getDefault().post(new SetIsReadyQRCodeEvent(false));
        Log.d("ABDS","asdasdas");
        super.onCancel(dialog);
    }
}
