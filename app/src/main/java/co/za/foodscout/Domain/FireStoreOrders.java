package co.za.foodscout.Domain;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import co.za.foodscout.Domain.Enum.PaymentMethod;

public class FireStoreOrders {

    String id;
    String retailId;
    String retailName;
    GeoPoint retailLocation;
    int orderNumber;
    String userId;
    String additionalOrderNote;
    PaymentMethod PaymentMethod;
    Boolean complete;
    List<FireStoreCart> cartList;
    Boolean isPaid;
    Double totalPrice;
    Timestamp dateCreated;
    Timestamp dateUpdated;
    Boolean orderReady;

    public Boolean isOrderReady() {
        return orderReady;
    }

    public void setOrderReady(Boolean orderReady) {
        this.orderReady = orderReady;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getRetailName() {
        return retailName;
    }

    public void setRetailName(String retailName) {
        this.retailName = retailName;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public List<FireStoreCart> getCartList() {
        return cartList;
    }

    public void setCartList(List<FireStoreCart> cartList) {
        this.cartList = cartList;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailId() {
        return retailId;
    }

    public void setRetailId(String retailId) {
        this.retailId = retailId;
    }

    public GeoPoint getRetailLocation() {
        return retailLocation;
    }

    public void setRetailLocation(GeoPoint retailLocation) {
        this.retailLocation = retailLocation;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdditionalOrderNote() {
        return additionalOrderNote;
    }

    public void setAdditionalOrderNote(String additionalOrderNote) {
        this.additionalOrderNote = additionalOrderNote;
    }

    public co.za.foodscout.Domain.Enum.PaymentMethod getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(co.za.foodscout.Domain.Enum.PaymentMethod paymentMethod) {
        PaymentMethod = paymentMethod;
    }
}
