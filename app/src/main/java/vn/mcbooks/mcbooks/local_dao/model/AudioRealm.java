package vn.mcbooks.mcbooks.local_dao.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hungtran on 6/23/16.
 */
public class AudioRealm extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String url;
    private String localURI;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalURI() {
        return localURI;
    }

    public void setLocalURI(String localURI) {
        this.localURI = localURI;
    }
}
