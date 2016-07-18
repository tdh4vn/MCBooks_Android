package vn.mcbooks.mcbooks.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.network_api.APIURL;


public class GiftFragment extends BaseFragment {

    private WebView webGift;

    public GiftFragment() {
        // Required empty public constructor
    }


    public static GiftFragment newInstance() {
        GiftFragment fragment = new GiftFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gift, container, false);
        initView(v);
        return v;
    }

    void initView(View v){
        webGift = (WebView)v.findViewById(R.id.webGift);
        webGift.getSettings().setJavaScriptEnabled(true);
        webGift.loadUrl(APIURL.GIFT_URL);
    }
}
