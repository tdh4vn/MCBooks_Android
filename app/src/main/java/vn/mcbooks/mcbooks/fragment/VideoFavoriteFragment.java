package vn.mcbooks.mcbooks.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.AudioPlayerActivity;
import vn.mcbooks.mcbooks.activity.BaseActivity;
import vn.mcbooks.mcbooks.activity.YoutubePlayerActivity;
import vn.mcbooks.mcbooks.adapter.ListMediaInBookAdapter;
import vn.mcbooks.mcbooks.model.GetBookByIDResult;
import vn.mcbooks.mcbooks.model.MediaInBook;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFavoriteFragment extends Fragment {

    ListView listView;
    public VideoFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_video_favorite, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        if (view != null){
            listView = (ListView) view.findViewById(R.id.listVideoFavorite);
            ListMediaInBookAdapter listMediaInBookAdapter = new ListMediaInBookAdapter(
                    ContentManager.getInstance().getListVideoFavorite(), getActivity());
            listView.setAdapter(listMediaInBookAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        MediaInBook mediaInBook = ContentManager.getInstance().getListVideoFavorite().get(position);
                        GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
                        Call<GetBookByIDResult> call = getBookService.getBookByID(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), mediaInBook.getBook().getId());
                        call.enqueue(new Callback<GetBookByIDResult>() {
                            @Override
                            public void onResponse(Call<GetBookByIDResult> call, Response<GetBookByIDResult> response) {
                                if (response.body().getCode() != 1){
                                    Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(YoutubePlayerActivity.BOOK_KEY, response.body().getResult());
                                    ((BaseActivity)getActivity()).openActivity(YoutubePlayerActivity.class, bundle);
                                }
                            }

                            @Override
                            public void onFailure(Call<GetBookByIDResult> call, Throwable t) {
                                Toast.makeText(getActivity(),"Vui lòng đăng xuất và thử lại!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e){
                        Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG);
                    }

                }
            });
        }

    }

}
