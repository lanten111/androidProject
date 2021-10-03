package co.za.foodscout.Domain.Restaurant;

import java.util.List;

public class MenuAddons {

    private boolean isGrouped;
    private String name;
    private List<Addons> addonsList;

    public List<Addons> getAddonsList() {
        return addonsList;
    }

    public void setAddonsList(List<Addons> addonsList) {
        this.addonsList = addonsList;
    }

    public boolean isGrouped() {
        return isGrouped;
    }

    public void setGrouped(boolean grouped) {
        isGrouped = grouped;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MenuAddons{" +
                "isGrouped=" + isGrouped +
                ", name='" + name + '\'' +
                ", addonsList=" + addonsList +
                '}';
    }
}
