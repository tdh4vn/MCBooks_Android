package vn.mcbooks.mcbooks.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lapism.searchview.SearchView;

import vn.mcbooks.mcbooks.R;

public class SearchActivity extends AppCompatActivity
                implements View.OnClickListener {
    ImageButton btnBack;
    EditText txtSearchBar;
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
        txtSearchBar = (EditText)this.findViewById(R.id.text_search_bar);
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
