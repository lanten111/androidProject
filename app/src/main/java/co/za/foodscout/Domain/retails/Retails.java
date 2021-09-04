package co.za.foodscout.Domain.retails;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Retails {

    @SerializedName("Total Restaurants")
    @Expose
    private int totalRestaurants;
    @SerializedName("Result")
    @Expose
    private List<RetailDetails> result = null;

    public int getTotalRestaurants() {
        return totalRestaurants;
    }

    public void setTotalRestaurants(int totalRestaurants) {
        this.totalRestaurants = totalRestaurants;
    }

    public List<RetailDetails> getResult() {
        return result;
    }

    public void setResult(List<RetailDetails> result) {
        this.result = result;
    }

}
