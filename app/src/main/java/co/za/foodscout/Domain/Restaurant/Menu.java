package co.za.foodscout.Domain.Restaurant;

import java.util.List;

import co.za.foodscout.Domain.Enum.MenuCatagories;

public class Menu {

    String menuItemId;
    String restaurantId;
    String menuItemName;
    String menuItemDescription;
    Double menuItemPrice;
    List<String> menuItemImagesId;
    MenuCatagories menuCatagories;
    List<MenuAddons> addOns;

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

    public List<MenuAddons> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<MenuAddons> addOns) {
        this.addOns = addOns;
    }
}
