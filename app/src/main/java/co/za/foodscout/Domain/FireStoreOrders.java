package co.za.foodscout.Domain;

import com.google.firebase.firestore.GeoPoint;

public class FireStoreOrders {

    String retailId;
    GeoPoint retailLocation;
    private String order;
    private String orderAddon;
    private String userId;
    String additionalOrderNote;
}
