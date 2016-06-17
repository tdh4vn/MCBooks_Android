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
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.GetBookResult;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
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

    private String bookType;

    private RecyclerView listBooks;

    public MoreBooksFragment() {

    }

    public void setBookType(String bookType) {
        if (bookType.equals(HOT_BOOKS) || bookType.equals(NEW_BOOKS) || bookType.equals(COMING_BOOKS)){
            this.bookType = bookType;
        } else {
            this.bookType = "new";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        
        View rootView = inflater.inflate(R.layout.fragment_more_books, container, false);
        initView(rootView);
        showDialogLoading();
        initListBooks();
        loadBooks(pageNumber);
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
        Log.d("HungTD","abcs");
        progressDialog=new ProgressDialog(getActivity());

    }

    private void loadBooks(int page) {
        GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.KEY_TOKEN, "");

        Call<GetBookResult> getBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), bookType, page);
        getBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                if (progressDialog != null && response.body().getResult().size() > 0){
                    Log.d("HungTD", pageNumber+"");
                    pageNumber++;
                    progressDialog.dismiss();
                    putDataToListView(response.body().getResult());
                }
                if (response.body().getCode() != 1){
                    showToast(listBookResult.getMessage(), Toast.LENGTH_LONG);
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
                Log.d("HungTD", "LoadMore");
                loadBooks(pageNumber);
            }
        });
        listBooks.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), listBooks, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookDetailFragment bookDetailFragment = new BookDetailFragment();
                bookDetailFragment.setmBook(listBookResult.getResult().get(position));
                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }




}
