package vn.mcbooks.mcbooks.fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.LoginActivity;
import vn.mcbooks.mcbooks.adapter.BookInHomeAdapter;
import vn.mcbooks.mcbooks.exception.SetBookTypeException;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.GetBookResult;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.EndlessRecyclerViewScrollListener;
import vn.mcbooks.mcbooks.utils.StringUtils;

import static android.support.v7.widget.RecyclerView.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreBooksFragment extends BaseFragment{

    public static final String HOT_BOOKS = "hot";
    public static final String NEW_BOOKS = "new";
    public static final String COMING_BOOKS = "coming";
    int pageNumber = 1;

    private GetBookResult listBookResult = new GetBookResult();;

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
        loadBooks(pageNumber);
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void showDialogLoading(){
        progressDialog=new ProgressDialog(getActivity());
    }

    private void loadBooks(int page) {
        GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
        String token = ContentManager.getInstance().getToken();
        Call<GetBookResult> getBookServiceCall;
        if (!bookType.equals("null")){
            getBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), bookType, page);
        } else {
            getBookServiceCall = getBookService.getBooksByCategory(StringUtils.tokenBuild(token), idCategory, page);
        }
        getBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                if (response.body().getCode() != 1){
                    showToast(listBookResult.getMessage(), Toast.LENGTH_LONG);
                } else if (progressDialog != null && response.body().getResult().size() > 0){
                    pageNumber++;
                    progressDialog.dismiss();
                    putDataToListView(response.body().getResult());
                }

            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                showToast("Có lỗi xảy ra! Kiểm tra lại kết nối mạng hoặc liên hệ nhà cung cấp dịch vụ.", Toast.LENGTH_LONG);
            }
        });
    }

    private void putDataToListView(List<Book> books) {
        ((BookInHomeAdapter)listBooks.getAdapter()).getListBook().addAll(books);
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
                loadBooks(pageNumber);
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
    }




}
