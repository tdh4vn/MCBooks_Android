package vn.mcbooks.mcbooks.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import vn.mcbooks.mcbooks.adapter.BookInHomeAdapter;
import vn.mcbooks.mcbooks.adapter.ListMediaInBookAdapter;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.ITabLayoutManager;
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.model.GetBookByIDResult;
import vn.mcbooks.mcbooks.model.GetBookResult;
import vn.mcbooks.mcbooks.model.MediaInBook;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFavoriteFragment extends Fragment {
    public static final String MEDIA = "MEDIAA";
    ListView listViewMedia;
    ListMediaInBookAdapter listMediaInBookAdapter;


    public AudioFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_audio_favorite, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        listMediaInBookAdapter.setListMedia(ContentManager.getInstance().getListAudioFavorite());
        listMediaInBookAdapter.notifyDataSetChanged();
    }

    void initView(View rootView){
        if (rootView != null){
            listViewMedia = (ListView) rootView.findViewById(R.id.listMediaFavorite);
            listMediaInBookAdapter = new ListMediaInBookAdapter(
                    ContentManager.getInstance().getListAudioFavorite(), getActivity());
            listViewMedia.setAdapter(listMediaInBookAdapter);
            listViewMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                MediaInBook mediaInBook = ContentManager.getInstance().getListAudioFavorite().get(position);
                GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
                Call<GetBookByIDResult> call = getBookService.getBookByID(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), mediaInBook.getBook().getId());
                call.enqueue(new Callback<GetBookByIDResult>() {
                    @Override
                    public void onResponse(Call<GetBookByIDResult> call, Response<GetBookByIDResult> response) {
                        if (response.body().getCode() != 1){
                            Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("TG",response.body().getResult().getId());
                            Intent intent = new Intent(getActivity(), AudioPlayerActivity.class);
                            intent.putExtra(BookDetailFragment.BOOK, response.body().getResult());
                            intent.putExtra(MEDIA, listMediaInBookAdapter.getItem(position).getId());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetBookByIDResult> call, Throwable t) {
                        Toast.makeText(getActivity(),"Vui lòng đăng xuất và thử lại!", Toast.LENGTH_LONG).show();
                    }
                });
                }
            });
        }
    }
}
