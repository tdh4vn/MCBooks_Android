package vn.mcbooks.mcbooks.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import vn.mcbooks.mcbooks.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageViewFragment extends Fragment {


    public int imageSRC;

    public ImageViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_view, container, false);
        ImageView imgView = (ImageView)view.findViewById(R.id.imageView);
        imgView.setImageResource(imageSRC);
        return view;
    }

}
