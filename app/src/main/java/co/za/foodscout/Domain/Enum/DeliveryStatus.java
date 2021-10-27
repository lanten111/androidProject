package co.za.foodscout.Domain.Enum;

public enum DeliveryStatus {

    Preparing("Order bing prepared"),
    OnRoute("Delivery On route"),
    Arrived("Driver has arrived"),
    Ready("Ready for Pickup"),
    New("New order, Click for more details"),
    Completed("Order Complete");

    private final String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

   public String getDescription(){
        return this.status;
   }
}
