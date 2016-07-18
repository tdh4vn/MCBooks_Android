package vn.mcbooks.mcbooks.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.LoginActivity;
import vn.mcbooks.mcbooks.adapter.BookInHomeAdapter;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.EndlessRecyclerViewScrollListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteBooksFragment extends Fragment {
    RecyclerView recyclerViewBook;
    BookInHomeAdapter bookInHomeAdapter;

    public FavoriteBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void updateUI(){
        bookInHomeAdapter.setListBook(ContentManager.getInstance().getListBookFavorite());
        bookInHomeAdapter.setmContext(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewBook.setLayoutManager(gridLayoutManager);
        recyclerViewBook.setAdapter(bookInHomeAdapter);
        recyclerViewBook.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewBook, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("Tag", ""+position);
                BookDetailFragment bookDetailFragment
                        = BookDetailFragment.create(ContentManager.getInstance().getListBookFavorite().get(position), getString(R.string.favarite_books));
                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
//                BookDetailFragment bookDetailFragment = new BookDetailFragment();
//                bookDetailFragment.setmBook(ContentManager.getInstance().getListBookFavorite().get(position));
//                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite_books, container, false);
        initView(rootView);
        updateUI();
        return rootView;
    }

    void initView(View rootView){
        recyclerViewBook = (RecyclerView)rootView.findViewById(R.id.listBooksFavorite);
        bookInHomeAdapter = new BookInHomeAdapter();
    }

}
