package vn.mcbooks.mcbooks.activity;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import vn.mcbooks.mcbooks.R;

public class ShowImageActivity extends Activity {
    public static final String BUNDLE_IMAGES = "BUNDLE_IMAGES";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ArrayList<String> listImageURL;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_image);

        //get list image url
        if (getIntent().getExtras().getSerializable(BUNDLE_IMAGES) instanceof ArrayList){
            listImageURL = (ArrayList<String>) getIntent().getExtras().getSerializable(BUNDLE_IMAGES);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), listImageURL);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_image, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private String imageURL;
        private int position;

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(String imageURL) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setImageURL(imageURL);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_show_image, container, false);
            SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) rootView.findViewById(R.id.imageViewInSlider);
            DownloadImagesTask downloadImagesTask = new DownloadImagesTask();
            downloadImagesTask.imageView = imageView;
            downloadImagesTask.execute(imageURL);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> listImageURL;


        public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> listImageURL) {
            super(fm);
            this.listImageURL = listImageURL;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(listImageURL.get(position));
        }

        @Override
        public int getCount() {
            return listImageURL.size();
        }

    }
    public static class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {
        public SubsamplingScaleImageView imageView;
        String urlLink;

        @Override
        protected Bitmap doInBackground(String... imageViews) {
            this.urlLink = imageViews[0];
            return download_Image(urlLink);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                imageView.setImage(ImageSource.bitmap(result));
            } catch (Exception e){

            }


        }

        private Bitmap download_Image(String url) {

            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp){
                    return bmp;
                }
            }catch(Exception e){}
            return bmp;
        }
    }
}
