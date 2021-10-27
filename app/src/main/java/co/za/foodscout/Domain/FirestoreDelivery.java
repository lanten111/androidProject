package co.za.foodscout.Domain;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import co.za.foodscout.Domain.Enum.DeliveryStatus;

public class FirestoreDelivery {

    String id;
    GeoPoint userLocation;
    String userId;
    String retailId;
    String orderId;
    String retailName;
    GeoPoint retailLocation;
    Boolean isDelivered;
    String assigneeId;
    Boolean isAssigned;
    String userNames;
    String driverName;
    String contactNo;
    DeliveryStatus deliveryStatus;
    Timestamp dateCreated;
    Timestamp dateUpdated;
    Timestamp dateDelivered;
    Boolean isDeliveryPicked;
    int OrderNumber;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoPoint getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(GeoPoint userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRetailId() {
        return retailId;
    }

    public void setRetailId(String retailId) {
        this.retailId = retailId;
    }

    public String getRetailName() {
        return retailName;
    }

    public void setRetailName(String retailName) {
        this.retailName = retailName;
    }

    public GeoPoint getRetailLocation() {
        return retailLocation;
    }

    public void setRetailLocation(GeoPoint retailLocation) {
        this.retailLocation = retailLocation;
    }

    public Boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(Boolean delivered) {
        isDelivered = delivered;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(Boolean assigned) {
        isAssigned = assigned;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Timestamp getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(Timestamp dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public Boolean isDeliveryPicked() {
        return isDeliveryPicked;
    }

    public void setDeliveryPicked(Boolean deliveryPicked) {
        isDeliveryPicked = deliveryPicked;
    }

    public int getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        OrderNumber = orderNumber;
    }
}
