package vn.mcbooks.mcbooks.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.ListBookHorizontalAdapter;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.SearchResult;
import vn.mcbooks.mcbooks.network_api.SearchServices;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

public class SearchActivity extends AppCompatActivity
                implements View.OnClickListener {
    ImageButton btnBack;
    EditText txtSearchBar;
    ListView listView;
    boolean isReadySearch = true;
    SearchServices searchServices;
    ProgressBar progressBarLoading;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }
    private void initView(){
        btnBack = (ImageButton)this.findViewById(R.id.back_button);
        if(btnBack != null){
            btnBack.setOnClickListener(this);
        }
        progressBarLoading = (ProgressBar) this.findViewById(R.id.progress_loading);
        txtSearchBar = (EditText)this.findViewById(R.id.text_search_bar);
        listView = (ListView) this.findViewById(R.id.listResult);
        searchServices = ServiceFactory.getInstance().createService(SearchServices.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("BOOK", (Book)listView.getAdapter().getItem(position));
                setResult(AppCompatActivity.RESULT_OK, returnIntent);
                SearchActivity.this.finish();
            }
        });
        txtSearchBar.requestFocus();
        txtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            private Timer timer=new Timer();
            private final long DELAY = 500; // milliseconds
            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (isReadySearch){
                                    if (searchServices  == null){
                                        searchServices = ServiceFactory.getInstance().createService(SearchServices.class);
                                    } else {
//                                        if (progressBarLoading.getVisibility() == View.GONE){
//                                            progressBarLoading.setVisibility(View.VISIBLE);
//                                        }
//                                        SearchActivity.this.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
                                        Call<SearchResult> getBookResultCall;
                                        if (txtSearchBar.getText().toString().equals("") || txtSearchBar.getText().toString() == null ){
                                            getBookResultCall
                                                    = searchServices.searchBook(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), page, "");
                                        } else {
                                            getBookResultCall
                                                    = searchServices.searchBook(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), page, txtSearchBar.getText().toString());
                                        }
                                        getBookResultCall.enqueue(new Callback<SearchResult>() {
                                            @Override
                                            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                                                if (progressBarLoading.getVisibility() == View.VISIBLE){
                                                    progressBarLoading.setVisibility(View.GONE);
                                                }
                                                if (response.body().getCode() == 1){
                                                    ListBookHorizontalAdapter listBookSearchResultAdapter
                                                            = new ListBookHorizontalAdapter((ArrayList<Book>) response.body().getResult().getBooks(), SearchActivity.this);
                                                    listView.setAdapter(listBookSearchResultAdapter);
                                                } else {
                                                    Toast.makeText(SearchActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<SearchResult> call, Throwable t) {
//                                                        if (progressBarLoading.getVisibility() == View.VISIBLE){
//                                                            progressBarLoading.setVisibility(View.GONE);
//                                                        }
                                                //Toast.makeText(SearchActivity.this, "Có lỗi xảy ra! Vui lòng kiểm tra kết nối mạng hoặc đăng nhập lại", Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
                                            }
                                        });
                                    }
                                }//end if
                            }
                        },
                        DELAY
                );

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
