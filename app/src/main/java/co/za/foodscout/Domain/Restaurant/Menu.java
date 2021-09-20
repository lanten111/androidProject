package co.za.foodscout.Domain.Restaurant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.za.foodscout.Domain.MenuCatagories;

public class Menu {

    String menuItemId;
    String restaurantId;
    String menuItemName;
    String menuItemDescription;
    Double menuItemPrice;
    List<String> menuItemImagesId;
    MenuCatagories menuCatagories;
    Map<String, HashMap<String, Double>> addOns;

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getMenuItemDescription() {
        return menuItemDescription;
    }

    public void setMenuItemDescription(String menuItemDescription) {
        this.menuItemDescription = menuItemDescription;
    }

    public Double getMenuItemPrice() {
        return menuItemPrice;
    }

    public void setMenuItemPrice(Double menuItemPrice) {
        this.menuItemPrice = menuItemPrice;
    }

    public List<String> getMenuItemImagesId() {
        return menuItemImagesId;
    }

    public void setMenuItemImagesId(List<String> menuItemImagesId) {
        this.menuItemImagesId = menuItemImagesId;
    }

    public MenuCatagories getMenuCatagories() {
        return menuCatagories;
    }

    public void setMenuCatagories(MenuCatagories menuCatagories) {
        this.menuCatagories = menuCatagories;
    }

    public Map<String, HashMap<String, Double>> getAddOns() {
        return addOns;
    }

    public void setAddOns(Map<String, HashMap<String, Double>> addOns) {
        this.addOns = addOns;
    }
}
