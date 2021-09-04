package co.za.foodscout.Domain;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class FirestoreDelivery {

    String id;
    GeoPoint userDestination;
    String userAddress;
    String userId;
    String retailId;
    String retailName;
    GeoPoint retailLocation;
    String retailAddress;
    Boolean isDelivered;
    String assigneeId;
    Boolean isAssigned;
    String userNames;
    String driverName;
    String contactNo;
    String deliveryStatus;
    Timestamp dateCreated;
    Timestamp dateUpdated;
    Timestamp dateDelivered;
    Boolean isDeliveryPicked;

    public Boolean isDeliveryPicked() {
        return isDeliveryPicked;
    }

    public void setDeliveryPicked(Boolean deliveryPicked) {
        isDeliveryPicked = deliveryPicked;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Timestamp getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(Timestamp dateDelivered) {
        this.dateDelivered = dateDelivered;
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(Boolean assigned) {
        isAssigned = assigned;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getRetailAddress() {
        return retailAddress;
    }

    public void setRetailAddress(String retailAddress) {
        this.retailAddress = retailAddress;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(Boolean delivered) {
        isDelivered = delivered;
    }

    public GeoPoint getUserDestination() {
        return userDestination;
    }

    public void setUserDestination(GeoPoint userDestination) {
        this.userDestination = userDestination;
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

    @Override
    public String toString() {
        return "DeliveryTo{" +
                "id='" + id + '\'' +
                ", userDestination=" + userDestination +
                ", userAddress='" + userAddress + '\'' +
                ", userId='" + userId + '\'' +
                ", retailId='" + retailId + '\'' +
                ", retailName='" + retailName + '\'' +
                ", retailLocation=" + retailLocation +
                ", retailAddress='" + retailAddress + '\'' +
                ", IsDelivered=" + isDelivered +
                ", assigneeId='" + assigneeId + '\'' +
                ", isAssigned=" + isAssigned +
                '}';
    }
}
