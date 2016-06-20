package vn.mcbooks.mcbooks.network_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.mcbooks.mcbooks.model.GetBookByIDResult;
import vn.mcbooks.mcbooks.model.GetBookResult;

/**
 * Created by hungtran on 6/11/16.
 */
public interface GetBookService {
    @GET("api/books/{name}")
    Call<GetBookResult> getBooks(@Header("Authorization") String token, @Path("name") String name, @Query("page") int page);
    @GET("api/categories/{category}/books")
    Call<GetBookResult> getBooksByCategory(@Header("Authorization") String token, @Path("category") String categoryID, @Query("page") int page);
    @GET("api/favorite/books")
    Call<GetBookResult> getBooksInFavorite(@Header("Authorization") String token, @Query("page") int page);
    @GET("api/book/{bookid}")
    Call<GetBookByIDResult> getBookByID(@Header("Authorization") String token, @Path("bookid") String bookid);
    @GET("api/scan")
    Call<GetBookByIDResult> getBookByQRCode(@Header("Authorization") String token, @Query("barcode") String barcode);
}
