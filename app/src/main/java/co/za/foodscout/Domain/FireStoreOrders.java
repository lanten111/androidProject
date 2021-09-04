package co.za.foodscout.Domain;

import com.google.firebase.firestore.GeoPoint;

public class FireStoreOrders {

    String sellerId;
    GeoPoint SellerLocation;
    private String order;
    private String orderAddon;
    private String userId;
    String additionalNote;
}
