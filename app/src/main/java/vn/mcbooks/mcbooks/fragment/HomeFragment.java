package vn.mcbooks.mcbooks.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.github.barteksc.pdfviewer.ScrollBar;

import org.greenrobot.eventbus.EventBus;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.AdPageAdapter;
import vn.mcbooks.mcbooks.adapter.BookInHomeAdapter;
import vn.mcbooks.mcbooks.eventbus.SetBottomBarPosition;
import vn.mcbooks.mcbooks.intef.IBottomNavigationController;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.IReloadData;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.listener.RecyclerItemClickListener;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.singleton.ContentManager;


public class HomeFragment extends BaseFragment
        implements IReloadData.ILoadDataCompleteCallBack,
                    View.OnClickListener{


    public static final String NAME = HomeFragment.class.toString();

    private SwipeRefreshLayout swipeRefreshLayout;
    private IReloadData iReloadData;
    private SliderLayout sliderAd;

    //-----------Data


    private int[] saveState = new int[2];
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
    //private ViewPager pagerForAd;

    private ScrollView scrollView;

    public void setDataLoginResult(Result dataLoginResult) {
        this.dataLoginResult = dataLoginResult;
    }

    public void setiReloadData(IReloadData iReloadData) {
        this.iReloadData = iReloadData;
    }

    public IReloadData getiReloadData() {
        return iReloadData;
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

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new SetBottomBarPosition(0));
        IToolBarController toolBarController = (IToolBarController)getActivity();
        toolBarController.setVisibilityForTitles(View.GONE);
        toolBarController.changeTitles("");
        toolBarController.setVisibilityForLogo(View.VISIBLE);
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
        //pagerForAd = (ViewPager)view.findViewById(R.id.listAd);
        sliderAd = (SliderLayout) view.findViewById(R.id.listAd);
        createSlider();
        scrollView = (ScrollView) view.findViewById(R.id.scrollViewHome);


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



    private void createSlider(){
        DefaultSliderView sliderView1 = new DefaultSliderView(getActivity());
        sliderView1.image("http://mcbooks.vn/images/banner/ad864885451f28d7cb9a79626cf830a4.png");
        DefaultSliderView sliderView2 = new DefaultSliderView(getActivity());
        sliderView2.image("http://mcbooks.vn/images/banner/ea2380194f5fe09187a3497dedf1b099.png");
        DefaultSliderView sliderView3 = new DefaultSliderView(getActivity());
        sliderView3.image("http://mcbooks.vn/images/banner/10efab4cb2ca77b1f5c4a70d9ee8399d.png");
        sliderAd.addSlider(sliderView1);
        sliderAd.addSlider(sliderView2);
        sliderAd.addSlider(sliderView3);
        sliderAd.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderAd.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderAd.setCustomAnimation(new DescriptionAnimation());
        sliderAd.setDuration(4000);
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
                BookDetailFragment bookDetailFragment
                        = BookDetailFragment.create(dataLoginResult.getHotBooks().get(position), getString(R.string.hot_books_titles));
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
                BookDetailFragment bookDetailFragment
                        = BookDetailFragment.create(dataLoginResult.getNewBooks().get(position), getString(R.string.news_book_titles));
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
                BookDetailFragment bookDetailFragment
                        = BookDetailFragment.create(dataLoginResult.getComingBooks().get(position), getString(R.string.coming_books));
                ((IOpenFragment)getActivity()).openFragment(bookDetailFragment, true);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (scrollView != null){
            scrollView.setScrollX(saveState[0]);
            scrollView.setScrollY(saveState[1]);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState[0] = scrollView.getScrollX();
        saveState[1] = scrollView.getScrollY();
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
                moreBooksHotFragment.titles = getString(R.string.hot_books_titles);
                moreBooksHotFragment.setBookType(MoreBooksFragment.HOT_BOOKS);
                ((IOpenFragment)getActivity()).openFragment(moreBooksHotFragment, true);
                break;
            case R.id.btnBookNew:
                MoreBooksFragment moreBooksNewFragment = new MoreBooksFragment();
                moreBooksNewFragment.titles = getString(R.string.news_book_titles);
                moreBooksNewFragment.setBookType(MoreBooksFragment.NEW_BOOKS);
                ((IOpenFragment)getActivity()).openFragment(moreBooksNewFragment, true);
                break;
            case R.id.btnBooksInRelease:
                MoreBooksFragment moreBooksComingFragment = new MoreBooksFragment();
                moreBooksComingFragment.titles = getString(R.string.coming_books);
                moreBooksComingFragment.setBookType(MoreBooksFragment.COMING_BOOKS);
                ((IOpenFragment)getActivity()).openFragment(moreBooksComingFragment, true);
                break;
            default:
                break;
        }
    }
}
