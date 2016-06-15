package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungtran on 6/1/16.
 */
public class Book implements Serializable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("information")
    @Expose
    private Information information;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    @SerializedName("medias")
    @Expose
    private List<Media> medias = new ArrayList<Media>();
    @SerializedName("sale_offs")
    @Expose
    private List<String> saleOffs = new ArrayList<String>();
    @SerializedName("hot")
    @Expose
    private Boolean hot;
    @SerializedName("new")
    @Expose
    private Boolean _new;
    @SerializedName("coming")
    @Expose
    private Boolean coming;
    @SerializedName("ratings")
    @Expose
    private Ratings ratings;
    @SerializedName("buy_url")
    @Expose
    private String buyUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }


    public List<String> getSaleOffs() {
        return saleOffs;
    }


    public void setSaleOffs(List<String> saleOffs) {
        this.saleOffs = saleOffs;
    }


    public Boolean getHot() {
        return hot;
    }


    public void setHot(Boolean hot) {
        this.hot = hot;
    }


    public Boolean getNew() {
        return _new;
    }

    public void setNew(Boolean _new) {
        this._new = _new;
    }

    public Boolean getComing() {
        return coming;
    }

    public void setComing(Boolean coming) {
        this.coming = coming;
    }

    public Ratings getRatings() {
        return ratings;
    }

    public void setRatings(Ratings ratings) {
        this.ratings = ratings;
    }

    public String getBuyUrl() {
        return buyUrl;
    }

    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    public int getNumberAudiosFile(){
        int number = 0;
        if (medias != null){
            for (Media media : medias){
                if (media.getType() == Media.AUDIO_TYPE){
                    number++;
                }
            }
        }
        return number;
    }

    public int getNumberVideosFile(){
        int number = 0;
        if (medias != null){
            for (Media media : medias){
                if (media.getType() == Media.VIDEO_TYPE){
                    number++;
                }
            }
        }
        return number;
    }
}
