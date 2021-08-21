package co.za.delivernow.Domain;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DeliveryTo implements Serializable {

    String id;
    GeoPoint userDestination;
    String userAddress;
    String userId;
    String retailId;
    String retailName;
    GeoPoint retailLocation;
    String retailAddress;
    Boolean IsDelivered;
    String assigneeId;
    Boolean isAssigned;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAssigned() {
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

    public Boolean getDelivered() {
        return IsDelivered;
    }

    public void setDelivered(Boolean delivered) {
        IsDelivered = delivered;
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
                ", IsDelivered=" + IsDelivered +
                ", assigneeId='" + assigneeId + '\'' +
                ", isAssigned=" + isAssigned +
                '}';
    }
}
