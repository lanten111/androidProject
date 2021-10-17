package co.za.foodscout.Domain.Enum;

public enum OrderStatus {

    Preparing("Order bing prepared"),
    OnRoute("Delivery On route"),
    Arrived("Driver has arrived"),
    Ready("Ready for Pickup"),
    New("New order, Click for more details");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

   public String getDescription(){
        return this.status;
   }
}
