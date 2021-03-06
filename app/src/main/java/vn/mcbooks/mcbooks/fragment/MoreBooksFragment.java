package vn.mcbooks.mcbooks.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.BookInHomeAdapter;
import vn.mcbooks.mcbooks.adapter.ListBookHorizontalAdapter;
import vn.mcbooks.mcbooks.eventbus.ListOrGridEventBus;
import vn.mcbooks.mcbooks.eventbus.SetBottomBarPosition;
import vn.mcbooks.mcbooks.eventbus.SetIsReadyQRCodeEvent;
import vn.mcbooks.mcbooks.eventbus.ShowHideViewTypeMenuEventBus;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.GetBookResult;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.EndlessRecyclerViewScrollListener;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreBooksFragment extends BaseFragment{

    public static final String HOT_BOOKS = "hot";
    public static final String NEW_BOOKS = "new";
    public static final String COMING_BOOKS = "coming";
    int pageNumberRecycler = 1;
    private boolean islistViewLoadMore = false;

    private GetBookResult listBookResult = new GetBookResult();

    ProgressDialog progressDialog;

    private String bookType = "null";

    private String idCategory = "null";

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.setBookType("null");
        this.idCategory = idCategory;
    }

    private RecyclerView listBooks;
    private ListView listviewBooks;


    public void setBookType(String bookType) {
        if (bookType.equals(HOT_BOOKS) || bookType.equals(NEW_BOOKS) || bookType.equals(COMING_BOOKS)){
            this.setIdCategory("null");
            this.bookType = bookType;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more_books, container, false);
        initView(rootView);
        showDialogLoading();
        initListBooks();
        initListViewBooks();
        loadBooks(pageNumberRecycler);
        if (this.titles.equals("")){
            IToolBarController toolBarController = (IToolBarController)getActivity();
            toolBarController.setVisibilityForTitles(View.GONE);
            toolBarController.changeTitles("");
            toolBarController.setVisibilityForLogo(View.VISIBLE);
        } else {
            configToolBar();
        }
        return rootView;
    }

    private void initView(View rootView) {
        listBooks = (RecyclerView) rootView.findViewById(R.id.listbooks);
        listviewBooks = (ListView) rootView.findViewById(R.id.listViewBooks);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new ShowHideViewTypeMenuEventBus(true));

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new SetBottomBarPosition(0, false));
    }

    void showDialogLoading(){
        progressDialog=new ProgressDialog(getActivity());
    }

    private void loadBooks(int page) {
        Log.d("abdd","ádsds");
        GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
        String token = ContentManager.getInstance().getToken();
        Call<GetBookResult> getBookServiceCall;
        if (!bookType.equals("null")){
            getBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), bookType, pageNumberRecycler);
        } else {
            getBookServiceCall = getBookService.getBooksByCategory(StringUtils.tokenBuild(token), idCategory, pageNumberRecycler);
        }
        getBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                if (response.body().getCode() != 1){
                    showToast(listBookResult.getMessage(), Toast.LENGTH_LONG);
                } else if (progressDialog != null && response.body().getResult().size() > 0){
                    pageNumberRecycler++;
                    progressDialog.dismiss();
                    islistViewLoadMore = false;
                    putDataToListViewRecycler(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                showToast("Có lỗi xảy ra! Kiểm tra lại kết nối mạng hoặc liên hệ nhà cung cấp dịch vụ.", Toast.LENGTH_LONG);
            }
        });
    }


    private void putDataToListViewRecycler(List<Book> books) {
        List<Book> listBook = ((BookInHomeAdapter)listBooks.getAdapter()).getListBook();
        for (Book newBook : books){
            boolean isExists = true;
            for (Book oldBook : listBook){
                if (newBook.getId().equals(oldBook.getId())) {
                    isExists = false;
                    break;
                }
            }
            if (isExists){
                listBook.add(newBook);
            }
        }
        ((BookInHomeAdapter)listBooks.getAdapter()).setListBook(listBook);
        ((ListBookHorizontalAdapter)listviewBooks.getAdapter()).setListBook((ArrayList<Book>) listBook);
        listviewBooks.invalidate();
        listBooks.getAdapter().notifyDataSetChanged();

    }

    private void initListBooks() {
        BookInHomeAdapter bookInHomeAdapter = new BookInHomeAdapter();
        bookInHomeAdapter.setListBook(listBookResult.getResult());
        bookInHomeAdapter.setmContext(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        listBooks.setLayoutManager(gridLayoutManager);
        listBooks.setAdapter(bookInHomeAdapter);
        listBooks.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("LoadMore", "Re");
                loadBooks(pageNumberRecycler);
            }
        });
        listBooks.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), listBooks, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookDetailFragment bookDetailFragment
                        = BookDetailFragment.create(listBookResult.getResult().get(position));
                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        listviewBooks.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if((lastInScreen == totalItemCount) && !(islistViewLoadMore)){
                    Log.d("LoadMore", "List");
                    islistViewLoadMore = true;
                    loadBooks(pageNumberRecycler);
                }
            }
        });
    }

    private void initListViewBooks(){
        ListBookHorizontalAdapter listBookHorizontalAdapter = new ListBookHorizontalAdapter((ArrayList<Book>) listBookResult.getResult(), getActivity());
        listviewBooks.setAdapter(listBookHorizontalAdapter);
        listviewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDetailFragment bookDetailFragment
                        = BookDetailFragment.create(listBookResult.getResult().get(position));
                FragmentManager ft = getActivity().getSupportFragmentManager();
                ft.beginTransaction().add(R.id.container, bookDetailFragment).commit();
            }
        });
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(ListOrGridEventBus event){
        if (event.getViewType() == ListOrGridEventBus.GRID){
            listBooks.setVisibility(View.VISIBLE);
            listviewBooks.setVisibility(View.GONE);
        } else {
            if (event.getViewType() == ListOrGridEventBus.LIST){
                listviewBooks.setVisibility(View.VISIBLE);
                listBooks.setVisibility(View.GONE);
            }
        }
    }
}
