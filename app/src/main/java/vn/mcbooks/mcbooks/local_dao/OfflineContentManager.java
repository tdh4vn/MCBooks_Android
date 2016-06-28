package vn.mcbooks.mcbooks.local_dao;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import vn.mcbooks.mcbooks.local_dao.model.AudioRealm;
import vn.mcbooks.mcbooks.local_dao.model.BookRealm;
import vn.mcbooks.mcbooks.model.Audio;
import vn.mcbooks.mcbooks.model.Book;
import vn.mcbooks.mcbooks.model.Media;
import vn.mcbooks.mcbooks.network_api.APIURL;

/**
 * Created by hungtran on 6/23/16.
 */
public class OfflineContentManager {
    private Context mContext;
    private Realm realm;
    private RealmList<BookRealm> listBook;
    private RealmList<AudioRealm> listAudio;

    private static OfflineContentManager sharePointer = new OfflineContentManager();

    private static OfflineContentManager getInstance(Context mContext){
        sharePointer.mContext = mContext;
        return sharePointer;
    }

    public OfflineContentManager() {
        realm = Realm.getDefaultInstance();
        listBook = new RealmList<>();
        listAudio = new RealmList<>();
        listBook.addAll(realm.where(BookRealm.class).findAll());
        listAudio.addAll(realm.where(AudioRealm.class).findAll());
    }

    public RealmList<BookRealm> getListBook() {
        return listBook;
    }

    public RealmList<AudioRealm> getListAudio() {
        return listAudio;
    }

    public void addBookToLocal(BookRealm bookRealm){
        listBook.add(bookRealm);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(listBook);
            }
        });
    }

    private RealmList<AudioRealm> convertToRealmList(List<Media> listMedias){
        RealmList<AudioRealm> audios = new RealmList<>();
        for (Media media : listMedias){
            AudioRealm audioRealm = realm.createObject(AudioRealm.class);
            audioRealm.setName(media.getName());
            audioRealm.setId(media.getId());
            audioRealm.setLocalURI(media.getUrl());
            audioRealm.setUrl(media.getUrl());
            audios.add(audioRealm);
        }
        return audios;
        //TODO
    }

    public void addBookToLocal(Book book){
        BookRealm bookRealm = realm.createObject(BookRealm.class);
        bookRealm.setId(book.getId());
        bookRealm.setAuthor(book.getInformation().getAuthor());
        bookRealm.setPublisher(book.getInformation().getPublisher());
        bookRealm.setName(book.getName());
        DownloadFileAsync downloadFileAsync = new DownloadFileAsync();
        downloadFileAsync.book = book;
        downloadFileAsync.bookRealm = bookRealm;
        downloadFileAsync.execute("");
        listBook.add(bookRealm);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(listBook);
            }
        });
    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {
        Book book;
        BookRealm bookRealm;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(APIURL.BaseURL + book.getImage());
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = mContext.openFileOutput(book.getId()+".png", AppCompatActivity.MODE_PRIVATE);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
        }

        @Override
        protected void onPostExecute(String unused) {
            bookRealm.setImage(mContext.getFilesDir().getAbsolutePath()+ "/" +bookRealm.getId()+".png");
        }
    }
}
