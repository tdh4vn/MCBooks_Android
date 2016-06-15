package vn.mcbooks.mcbooks.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.BookInHomeAdapter;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.model.LoginSocialResult;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.singleton.ListBooksSingleton;


public class HomeFragment extends BaseFragment {
    public static final String NAME = HomeFragment.class.toString();

    //-----------Data
    private Result dataLoginResult;

    //-----------RecyclerView
    private RecyclerView recyclerViewHotBook;
    private RecyclerView recyclerViewNewBook;
    private RecyclerView recyclerViewComingBook;

    public void setDataLoginResult(Result dataLoginResult) {
        this.dataLoginResult = dataLoginResult;
    }

    public HomeFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view){
        recyclerViewHotBook = (RecyclerView)view.findViewById(R.id.listBookHotSeller);
        recyclerViewNewBook = (RecyclerView)view.findViewById(R.id.listBooksNew);
        recyclerViewComingBook = (RecyclerView)view.findViewById(R.id.listBooksInRelease);
    }

    private void initData(){
        //-----------------init for list hot book
        LinearLayoutManager layoutManagerForListHotBook
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHotBook.setLayoutManager(layoutManagerForListHotBook);
        BookInHomeAdapter adapterForHotBookList
                = new BookInHomeAdapter();
        if (dataLoginResult == null){
            dataLoginResult = ListBooksSingleton.getInstance().getResult();
        }
        adapterForHotBookList.setmContext(getContext());
        adapterForHotBookList.setListBook(dataLoginResult.getHotBooks());
        recyclerViewHotBook.setAdapter(adapterForHotBookList);
        recyclerViewHotBook.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewHotBook, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookDetailFragment bookDetailFragment = new BookDetailFragment();
                bookDetailFragment.setmBook(dataLoginResult.getHotBooks().get(position));
                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        //-----------------init for list new book
        LinearLayoutManager layoutManagerForListNewBook
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewBook.setLayoutManager(layoutManagerForListNewBook);
        BookInHomeAdapter adapterForNewBookList
                = new BookInHomeAdapter();
        adapterForNewBookList.setmContext(getContext());
        adapterForNewBookList.setListBook(dataLoginResult.getNewBooks());
        recyclerViewNewBook.setAdapter(adapterForNewBookList);
        recyclerViewNewBook.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewNewBook, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookDetailFragment bookDetailFragment = new BookDetailFragment();
                bookDetailFragment.setmBook(dataLoginResult.getNewBooks().get(position));
                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        //------------------init for list coming book
        LinearLayoutManager layoutManagerForListComingBook
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewComingBook.setLayoutManager(layoutManagerForListComingBook);
        BookInHomeAdapter adapterForComingBookList
                = new BookInHomeAdapter();
        adapterForComingBookList.setmContext(getContext());
        adapterForComingBookList.setListBook(dataLoginResult.getComingBooks());
        recyclerViewComingBook.setAdapter(adapterForComingBookList);
        recyclerViewComingBook.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewComingBook, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookDetailFragment bookDetailFragment = new BookDetailFragment();
                bookDetailFragment.setmBook(dataLoginResult.getComingBooks().get(position));
                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("DATA", dataLoginResult);
        super.onSaveInstanceState(outState);
    }

}
