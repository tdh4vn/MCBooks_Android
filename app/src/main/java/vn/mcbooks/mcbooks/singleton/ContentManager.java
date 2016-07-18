package vn.mcbooks.mcbooks.singleton;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.activity.HomeActivity;
import vn.mcbooks.mcbooks.model.Audio;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.Category;
import vn.mcbooks.mcbooks.model.GetMediaFavoriteResult;
import vn.mcbooks.mcbooks.model.LoginSocialResult;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.model.MediaInBook;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.network_api.FavoriteServices;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * Created by hungtran on 6/11/16.
 */
public class ContentManager implements Serializable{
    private String token;
    private Result result;
    private UserModel user = new UserModel();
    private static ContentManager ourInstance = new ContentManager();

    List<Category> categories = new ArrayList<>();
    public static void setOurInstance(ContentManager ourInstance) {
        ContentManager.ourInstance = ourInstance;
    }

    private ArrayList<Book> listBookFavorite = new ArrayList<>();

    private ArrayList<MediaInBook> listMediaFavorite = new ArrayList<>();

    public ArrayList<Book> getListBookFavorite() {
        return listBookFavorite;
    }

    public void setListBookFavorite(ArrayList<Book> listBookFavorite) {
        this.listBookFavorite = listBookFavorite;
    }

    public static ContentManager getInstance() {
        return ourInstance;
    }

    private ContentManager() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean checkBookInFavorite(String id){
        for (Book book : listBookFavorite){
            if (book.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void addListBookToFavorite(List<Book> books){
        ArrayList<Book> listBookAfterFilter = new ArrayList<>();
        for (Book newBook : books){
            boolean isOldBook = false;
            for (Book oldBook : listBookFavorite){
                if (oldBook.getId().equals(newBook.getId())){
                    isOldBook = true;
                    break;
                }
            }
            if (!isOldBook){
                listBookAfterFilter.add(newBook);
            }
        }
        listBookFavorite.addAll(listBookAfterFilter);
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void removeBookInFavorite(String id){
        for (Iterator<Book> it = listBookFavorite.iterator(); it.hasNext();){
            Book book = it.next();
            if (book.getId().equals(id)){
                it.remove();
                return;
            }
        }
    }

    public void addBookToFavorite(Book book){
        if (!checkBookInFavorite(book.getId())){
            listBookFavorite.add(book);
        }
    }

    public ArrayList<MediaInBook> getListMediaFavorite() {
        return listMediaFavorite;
    }

    public void setListMediaFavorite(ArrayList<MediaInBook> listMediaFavorite) {
        this.listMediaFavorite = listMediaFavorite;
    }

    public boolean checkMediaInFavorite(String id){
        for (MediaInBook media : listMediaFavorite){
            if (media.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void removeMediaInFavorite(String id){
        for (Iterator<MediaInBook> it = listMediaFavorite.iterator(); it.hasNext();){
            MediaInBook mediaInBook = it.next();
            if (mediaInBook.getId().equals(id)){
                it.remove();
                return;
            }
        }
    }
    private int pageMediaFavorite = 1;
    void getMediasInFavorite(){
        FavoriteServices favoriteServices = ServiceFactory.getInstance().createService(FavoriteServices.class);
        getMediaInFavoriteByPage(favoriteServices);
    }

    private void getMediaInFavoriteByPage(final FavoriteServices favoriteServices){
        Call<GetMediaFavoriteResult> getBookResultCall =  favoriteServices.getMediaFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), pageMediaFavorite);
        getBookResultCall.enqueue(new Callback<GetMediaFavoriteResult>() {
            @Override
            public void onResponse(Call<GetMediaFavoriteResult> call, Response<GetMediaFavoriteResult> response) {
                if (response.body().getCode() != 1){
                    Log.d("HungTD", response.body().getMessage());
                } else if (response.body().getResult().size() > 0){
                    pageMediaFavorite++;
                    Log.d("HUngTD2", response.body().getResult().size()+"");
                    listMediaFavorite.addAll(response.body().getResult());
                    if (response.body().getResult().size() == 10){
                        getMediaInFavoriteByPage(favoriteServices);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMediaFavoriteResult> call, Throwable t) {
                Log.d("HungTD", "Có lỗi xảy ra! Vui lòng khởi động lại ứng dụng.");
            }
        });
    }
    public void reloadListMediaInFavorite(){
        pageMediaFavorite=1;
        listMediaFavorite = new ArrayList<>();
        getMediasInFavorite();
    }

    public List<MediaInBook> getListAudioFavorite(){
        ArrayList<MediaInBook> listAudio = new ArrayList<>();
        for (Iterator<MediaInBook> it = listMediaFavorite.iterator(); it.hasNext();){
            MediaInBook mediaInBook = it.next();
            if (mediaInBook.getType() == Media.AUDIO_TYPE){
                listAudio.add(mediaInBook);
            }
        }
        return listAudio;
    }

    public List<MediaInBook> getListVideoFavorite(){
        ArrayList<MediaInBook> listVideo = new ArrayList<>();
        for (Iterator<MediaInBook> it = listMediaFavorite.iterator(); it.hasNext();){
            MediaInBook mediaInBook = it.next();
            if (mediaInBook.getType() == Media.VIDEO_TYPE){
                listVideo.add(mediaInBook);
            }
        }
        return listVideo;
    }
    public void resetContent(){
        listBookFavorite = new ArrayList<>();
        listMediaFavorite = new ArrayList<>();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
