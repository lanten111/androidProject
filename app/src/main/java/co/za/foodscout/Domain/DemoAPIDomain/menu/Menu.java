package co.za.foodscout.Domain.DemoAPIDomain.menu;


import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Menu {

    @SerializedName("Total Menu")
    @Expose
    private int totalMenu;

    @SerializedName("Result")
    @Expose
    private List<MenuDetails> result = null;

    public int getTotalMenu() {
        return totalMenu;
    }

    public void setTotalMenu(int totalMenu) {
        this.totalMenu = totalMenu;
    }

    public List<MenuDetails> getResult() {
        return result;
    }

    public void setResult(List<MenuDetails> result) {
        this.result = result;
    }
}
