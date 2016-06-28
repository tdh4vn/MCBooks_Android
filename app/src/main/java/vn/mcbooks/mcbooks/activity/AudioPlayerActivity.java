package vn.mcbooks.mcbooks.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.ListMediaAdapter;
import vn.mcbooks.mcbooks.fragment.BookDetailFragment;
import vn.mcbooks.mcbooks.intef.IDownloader;
import vn.mcbooks.mcbooks.intef.IPlayAudio;
import vn.mcbooks.mcbooks.model.Audio;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.utils.StringUtils;

public class AudioPlayerActivity extends BaseActivity
    implements IDownloader, View.OnClickListener, IPlayAudio{
    private static final String TAG = "AudioPlayer";
    private Book mBook;
    private ArrayList<Audio> listAudio = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private ListView listMedia;
    private TextView txtTileMusic;
    private int index = 0;
    private boolean isInitMediplayer = false;

    private ProgressDialog mProgressDialog;

    //----media player controller
    private TextView txtCurrentTime;
    private TextView txtMaxTime;
    private SeekBar progressMedia;
    private ImageButton btnRepeatOne;
    private boolean isRepeatOne = true;
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


    void checkFile(){
        for (Audio audio : listAudio){
            File file = new File(getFilesDir(), audio.getId()+".mp3");
            if (file.exists()){
                audio.setLocalURI(file.getAbsolutePath());
            }
        }
    }

    private boolean isReadyPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        mBook = (Book) getIntent().getSerializableExtra(BookDetailFragment.BOOK);
        for (Media media : mBook.getMedias()){
            if (media.getType() == Media.AUDIO_TYPE){
                Audio audio = new Audio();
                audio.setId(media.getId());
                audio.setName(media.getName());
                audio.setUrl(APIURL.BaseURL + media.getUrl());
                audio.setLocalURI("null");
                listAudio.add(audio);
            }
        }
        checkFile();
        initView();
        initAudioPlayer();
    }


    private void initAudioPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void initAudioData(){
        try {
            index = 0;
            ((ListMediaAdapter)listMedia.getAdapter()).setIndexPlay(index);
            ((ListMediaAdapter)listMedia.getAdapter()).notifyDataSetChanged();
            if (listAudio.get(index).getLocalURI().equals("null")){
                mediaPlayer.setDataSource(listAudio.get(index).getUrl());
            } else {
                mediaPlayer.setDataSource(listAudio.get(index).getLocalURI());
            }

            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isReadyPlay = true;
                    txtCurrentTime.setText("00:00");
                    txtMaxTime.setText(StringUtils.milliSecondsToTimer((long)mediaPlayer.getDuration()));
                    progressMedia.setProgress(0);
                    progressMedia.setMax(mediaPlayer.getDuration());
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isRepeatOne) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();

                    } else {
                        nextAudio();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nextAudio() {
        try {
            index++;

            if (index >= listAudio.size()){
                index = 0;
            }
            ((ListMediaAdapter)listMedia.getAdapter()).setIndexPlay(index);
            ((ListMediaAdapter)listMedia.getAdapter()).notifyDataSetChanged();
            mediaPlayer.reset();
            //mediaPlayer.release();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            playPauseButton.setImageResource(R.drawable.ic_pause_white_48px);
            progressMedia.setProgress(0);
            if (listAudio.get(index).getLocalURI().equals("null")) {
                mediaPlayer.setDataSource(listAudio.get(index).getUrl());
            } else {
                mediaPlayer.setDataSource(listAudio.get(index).getLocalURI());
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Đường dẫn không đúng!", Toast.LENGTH_LONG).show();
        }
    }
    private void previousAudio() {
        try {
            index--;
            if (index < 0){
                index = listAudio.size() - 1;
            }
            ((ListMediaAdapter)listMedia.getAdapter()).setIndexPlay(index);
            ((ListMediaAdapter)listMedia.getAdapter()).notifyDataSetChanged();
            mediaPlayer.reset();
            //mediaPlayer.release();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            playPauseButton.setImageResource(R.drawable.ic_pause_white_48px);
            progressMedia.setProgress(0);
            if (listAudio.get(index).getLocalURI().equals("null")) {
                mediaPlayer.setDataSource(listAudio.get(index).getUrl());
            } else {
                mediaPlayer.setDataSource(listAudio.get(index).getLocalURI());
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Đường dẫn không đúng!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void moveToAudio(int i){
        try {
            index = i;
            Log.d("Index", ""+i);
            ((ListMediaAdapter)listMedia.getAdapter()).setIndexPlay(index);
            ((ListMediaAdapter)listMedia.getAdapter()).notifyDataSetChanged();
            mediaPlayer.reset();
            //mediaPlayer.release();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            playPauseButton.setImageResource(R.drawable.ic_pause_white_48px);
            progressMedia.setProgress(0);
            if (listAudio.get(index).getLocalURI().equals("null")) {
                Log.d("1", listAudio.get(index).getUrl());
                mediaPlayer.setDataSource(listAudio.get(index).getUrl());
            } else {
                Log.d("2", listAudio.get(index).getLocalURI());
                mediaPlayer.setDataSource(listAudio.get(index).getLocalURI());
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Đường dẫn không đúng!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }
    private void initView() {
        progressMedia = (SeekBar) findViewById(R.id.progress_media);
        if (progressMedia != null){
            progressMedia.setEnabled(false);
        }
        initDialog();
        btnRepeatOne = (ImageButton) findViewById(R.id.repeat);

        btnRepeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRepeatOne){
                    isRepeatOne = true;
                    btnRepeatOne.setImageResource(R.drawable.ic_repeat_white_36px);
                } else {
                    isRepeatOne = false;
                    btnRepeatOne.setImageResource(R.drawable.ic_repeat_grey_24dp);
                }
            }
        });

        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        txtMaxTime = (TextView) findViewById(R.id.txtMaxTime);
        playPauseButton = (ImageButton) findViewById(R.id.btnPlay);
        previousButton = (ImageButton) findViewById(R.id.btnPrevious);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousAudio();
            }
        });
        nextButton = (ImageButton) findViewById(R.id.btnNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAudio();
            }
        });
        txtTileMusic = (TextView) findViewById(R.id.titleMusic);
        listMedia = (ListView) findViewById(R.id.listMedia);
        final ListMediaAdapter mediaAdapter = new ListMediaAdapter(listAudio, this, this);
        listMedia.setAdapter(mediaAdapter);
        listMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToAudio(position);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (isReadyPlay){
                        if (mediaPlayer.isPlaying()){
                            progressMedia.setEnabled(true);
                            playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_48px);
                            mediaPlayer.pause();
                        } else {
                            progressMedia.setEnabled(true);
                            playPauseButton.setImageResource(R.drawable.ic_pause_white_48px);
                            mediaPlayer.start();
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(AudioPlayerActivity.this, "Trình nghe nhạc chưa sẵn sàng!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Đang tải file...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                updateMediaHandler.removeCallbacks(updateSongPerTime);
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
        if (!isInitMediplayer){
            initAudioData();
            initData();
            isInitMediplayer = true;
            progressMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser){
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            AudioPlayerActivity.this.runOnUiThread(updateSongPerTime);
        }

    }

    private void initData() {
        if (mBook != null){
            txtTileMusic.setText(mBook.getName());
        }
    }
    @Override
    public void onBackPressed() {
        updateMediaHandler.removeCallbacks(updateSongPerTime);
        mediaPlayer.stop();
        mediaPlayer.release();
        this.finish();
    }


    @Override
    public void downloadAudio(Audio audio) {
        DownloadFileAsync downloadFileAsync = new DownloadFileAsync();
        downloadFileAsync.audio = audio;
        downloadFileAsync.execute(audio.getUrl());
    }

    private void downloadSuccess(Audio audio){
        for (int i = 0; i < listAudio.size(); i++){
            if (listAudio.get(i).getId().equals(audio.getId())){
                listAudio.get(i).setLocalURI(audio.getLocalURI());
                ((ListMediaAdapter)listMedia.getAdapter()).getListMedia().get(i).setLocalURI(audio.getLocalURI());
                ((ListMediaAdapter)listMedia.getAdapter()).notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        Log.d("onClick", "" + position);
        moveToAudio(position);
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {
        Audio audio;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(audio.getUrl());
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = openFileOutput(audio.getId()+".mp3", MODE_PRIVATE);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            audio.setLocalURI(getFilesDir().getAbsolutePath()+ "/" +audio.getId()+".mp3");
            downloadSuccess(audio);
            mProgressDialog.dismiss();
        }
    }
}
