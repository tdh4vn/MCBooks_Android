package vn.mcbooks.mcbooks.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.BookInHomeAdapter;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.IReloadData;
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.singleton.ContentManager;


public class HomeFragment extends BaseFragment
        implements IReloadData.ILoadDataCompleteCallBack,
                    View.OnClickListener{
    public static final String NAME = HomeFragment.class.toString();

    private SwipeRefreshLayout swipeRefreshLayout;
    private IReloadData iReloadData;

    //-----------Data
    private Result dataLoginResult;

    //-----------RecyclerView
    private RecyclerView recyclerViewHotBook;
    private RecyclerView recyclerViewNewBook;
    private RecyclerView recyclerViewComingBook;
    private BookInHomeAdapter adapterForComingBookList;
    private BookInHomeAdapter adapterForNewBookList;
    private BookInHomeAdapter adapterForHotBookList;
    private Button btnMoreHotBook;
    private Button btnMoreNewBook;
    private Button btnMoreComingBook;
    public void setDataLoginResult(Result dataLoginResult) {
        this.dataLoginResult = dataLoginResult;
    }

    public void setiReloadData(IReloadData iReloadData) {
        this.iReloadData = iReloadData;
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
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_reload_data);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (iReloadData != null) {
                    iReloadData.reloadData();
                }
            }
        });
        recyclerViewHotBook = (RecyclerView)view.findViewById(R.id.listBookHotSeller);
        recyclerViewNewBook = (RecyclerView)view.findViewById(R.id.listBooksNew);
        recyclerViewComingBook = (RecyclerView)view.findViewById(R.id.listBooksInRelease);
        btnMoreHotBook = (Button) view.findViewById(R.id.btnBookHotSeller);
        btnMoreHotBook.setOnClickListener(this);
        btnMoreNewBook = (Button) view.findViewById(R.id.btnBookNew);
        btnMoreNewBook.setOnClickListener(this);
        btnMoreComingBook = (Button) view.findViewById(R.id.btnBooksInRelease);
        btnMoreComingBook.setOnClickListener(this);

    }

    private void initData(){
        //-----------------init for list hot book
        LinearLayoutManager layoutManagerForListHotBook
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHotBook.setLayoutManager(layoutManagerForListHotBook);
        adapterForHotBookList
                = new BookInHomeAdapter();
        if (dataLoginResult == null){
            dataLoginResult = ContentManager.getInstance().getResult();
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
        adapterForNewBookList
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
        adapterForComingBookList
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

    @Override
    public void reloadDataComplete() {
        dataLoginResult = ContentManager.getInstance().getResult();
        adapterForHotBookList.setListBook(dataLoginResult.getHotBooks());
        adapterForHotBookList.notifyDataSetChanged();
        adapterForComingBookList.setListBook(dataLoginResult.getComingBooks());
        adapterForComingBookList.notifyDataSetChanged();
        adapterForNewBookList.setListBook(dataLoginResult.getNewBooks());
        adapterForNewBookList.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBookHotSeller:
                MoreBooksFragment moreBooksHotFragment = new MoreBooksFragment();
                moreBooksHotFragment.setBookType(MoreBooksFragment.HOT_BOOKS);
                ((IOpenFragment)getActivity()).openFragment(moreBooksHotFragment, true);
                break;
            case R.id.btnBookNew:
                MoreBooksFragment moreBooksNewFragment = new MoreBooksFragment();
                moreBooksNewFragment.setBookType(MoreBooksFragment.NEW_BOOKS);
                ((IOpenFragment)getActivity()).openFragment(moreBooksNewFragment, true);
                break;
            case R.id.btnBooksInRelease:
                MoreBooksFragment moreBooksComingFragment = new MoreBooksFragment();
                moreBooksComingFragment.setBookType(MoreBooksFragment.COMING_BOOKS);
                ((IOpenFragment)getActivity()).openFragment(moreBooksComingFragment, true);
                break;
            default:
                break;
        }
    }
}
