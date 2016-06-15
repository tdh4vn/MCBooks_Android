package vn.mcbooks.mcbooks.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.IOException;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.ListMediaAdapter;
import vn.mcbooks.mcbooks.fragment.BookDetailFragment;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.utils.PlayerControl;
import vn.mcbooks.mcbooks.utils.StringUtils;

public class MediaPlayerActivity extends BaseActivity {
    private static final String URL = "http://download.a2.nixcdn.com/30dbd0e79fe0450a1517731023e96d23/57610e8e/NhacCuaTui154/TinhYeuHoaGio-TruongTheVinh_353sn.mp3";
    private static final String TAG = "AudioPlayer";
    private Book mBook;
    private PlayerControl mPlayerControl;
    private MediaPlayer mediaPlayer;
    private ListView listMedia;
    private TextView txtTileMusic;
    private int numberMedia;

    //----media player controller
    private TextView txtCurrentTime;
    private TextView txtMaxTime;
    private SeekBar progressMedia;
    private ImageButton playPauseButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private Handler updateMediaHandler = new Handler();

    //----update loop

    private Runnable updateSongPerTime = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            txtCurrentTime.setText(StringUtils.milliSecondsToTimer((long)currentPosition));
            progressMedia.setProgress(currentPosition);
            updateMediaHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        mBook = (Book) getIntent().getSerializableExtra(BookDetailFragment.BOOK);
        numberMedia = mBook.getNumberAudiosFile();
        initView();
        initAudioPlayer();
        initAudioData();
    }


    private void initAudioPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayerControl = new PlayerControl(mediaPlayer);
    }

    private void initAudioData(){
        try {
            mediaPlayer.setDataSource(URL);
            mediaPlayer.prepare();
            txtMaxTime.setText(StringUtils.milliSecondsToTimer((long)mediaPlayer.getDuration()));
            progressMedia.setMax(mediaPlayer.getDuration());
            progressMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser){
                        mPlayerControl.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            MediaPlayerActivity.this.runOnUiThread(updateSongPerTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    private void initView() {
        progressMedia = (SeekBar) findViewById(R.id.progress_media);
        if (progressMedia != null){
            progressMedia.setEnabled(false);
        }
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        txtMaxTime = (TextView) findViewById(R.id.txtMaxTime);
        playPauseButton = (ImageButton) findViewById(R.id.btnPlay);
        previousButton = (ImageButton) findViewById(R.id.btnPrevious);
        nextButton = (ImageButton) findViewById(R.id.btnNext);
        txtTileMusic = (TextView) findViewById(R.id.titleMusic);
        listMedia = (ListView) findViewById(R.id.listMedia);
        final ListMediaAdapter mediaAdapter = new ListMediaAdapter(mBook.getMedias(), this);
        listMedia.setAdapter(mediaAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (mPlayerControl.isPlaying()){
                        progressMedia.setEnabled(true);
                        playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_48px);
                        mPlayerControl.pause();
                    } else {
                        progressMedia.setEnabled(true);
                        playPauseButton.setImageResource(R.drawable.ic_pause_white_48px);
                        mPlayerControl.start();
                    }
                } catch (Exception e){
                    Toast.makeText(MediaPlayerActivity.this, "Trình nghe nhạc chưa sẵn sàng!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mediaPlayer.stop();
                mediaPlayer.release();
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (mBook != null){
            txtTileMusic.setText(mBook.getName());

        }
    }
    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        mediaPlayer.release();
        this.finish();
    }
}
