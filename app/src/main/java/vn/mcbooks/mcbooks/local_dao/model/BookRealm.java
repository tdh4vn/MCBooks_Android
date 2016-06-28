package vn.mcbooks.mcbooks.local_dao.model;

import java.util.ArrayList;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import vn.mcbooks.mcbooks.model.Information;
import vn.mcbooks.mcbooks.model.Media;

/**
 * Created by hungtran on 6/23/16.
 */
public class BookRealm extends RealmObject{
    @PrimaryKey
    private String id;
    private String image;
    private String name;
    private String publisher;
    private String author;
    private RealmList<AudioRealm> medias;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public RealmList<AudioRealm> getMedias() {
        return medias;
    }

    public void setMedias(RealmList<AudioRealm> medias) {
        this.medias = medias;
    }
}
