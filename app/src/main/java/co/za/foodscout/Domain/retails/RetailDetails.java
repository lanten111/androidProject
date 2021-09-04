package co.za.foodscout.Domain.retails;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class RetailDetails {

    @SerializedName("reviews")
    @Expose
    private int reviews;
    @SerializedName("parkinglot")
    @Expose
    private boolean parkinglot;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("averagecost")
    @Expose
    private int averagecost;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("imageId")
    @Expose
    private String imageId;
    @SerializedName("restauranttype")
    @Expose
    private String restauranttype;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("businessname")
    @Expose
    private String businessname;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("menu")
    @Expose
    private String menu;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("__v")
    @Expose
    private int v;
    @SerializedName("foodMenu")
    @Expose
    private List<Object> foodMenu = null;

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public boolean isParkinglot() {
        return parkinglot;
    }

    public void setParkinglot(boolean parkinglot) {
        this.parkinglot = parkinglot;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAveragecost() {
        return averagecost;
    }

    public void setAveragecost(int averagecost) {
        this.averagecost = averagecost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getRestauranttype() {
        return restauranttype;
    }

    public void setRestauranttype(String restauranttype) {
        this.restauranttype = restauranttype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public List<Object> getFoodMenu() {
        return foodMenu;
    }

    public void setFoodMenu(List<Object> foodMenu) {
        this.foodMenu = foodMenu;
    }
}
