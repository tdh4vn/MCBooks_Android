package vn.mcbooks.mcbooks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.ListVideoAdapter;
import vn.mcbooks.mcbooks.constant.AppConstant;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.model.Video;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public static final String BOOK_KEY = "BOOK";
    private int index = 0;
    private boolean isFullScreen = false;
    private ListView listVideo;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private Book mBook;
    private List<Video> videoList = new ArrayList<>();

    private YouTubePlayerView youTubePlayerView;
    ImageButton btnBack;
    YouTubePlayer youTubePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(AppConstant.DEVELOPER_KEY, this);
        TextView txtTitle = (TextView)findViewById(R.id.titleYoutube);

        btnBack = (ImageButton)findViewById(R.id.btn_home);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listVideo = (ListView)findViewById(R.id.listVideos);
        mBook = (Book)getIntent().getExtras().getSerializable(BOOK_KEY);
        txtTitle.setText(mBook.getName());
        createListVideo();
        ListVideoAdapter listVideoAdapter = new ListVideoAdapter(videoList, this);
        listVideo.setAdapter(listVideoAdapter);
        listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openVideo(videoList.get(position).getId());
                index = position;
                ((ListVideoAdapter)listVideo.getAdapter()).setIndexPlay(index);
                ((ListVideoAdapter)listVideo.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    void createListVideo(){
        for (Media media : mBook.getMedias()){
            if (media.getType() == Media.VIDEO_TYPE){
                Video video = new Video();
                video.setIdInServer(media.getId());
                video.setUrl(media.getUrl());
                video.setName(media.getName());
                Log.d("HUNG",video.getUrl().split("v=").length + "");
                String id = video.getUrl().split("v=")[1];
                Log.d("HungTD2222", id);
                video.setId(id);
                videoList.add(video);
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

            this.youTubePlayer = youTubePlayer;
            this.youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean b) {
                    isFullScreen = b;
                    Log.d("ABC",  String.valueOf(isFullScreen));
                }
            });
            openVideo(videoList.get(index).getId());

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

        Log.d("ABC22",  String.valueOf(isFullScreen));
        if (isFullScreen){
            Log.d("ACBD", "Full Screen");
            youTubePlayer.setFullscreen(false);
        } else {
            Log.d("ACBD", "Full Screen2");
            super.onBackPressed();
        }
        //super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(AppConstant.DEVELOPER_KEY, this);
        }
    }
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private void openVideo(String id){
        youTubePlayer.cueVideo(id);
    }
}
