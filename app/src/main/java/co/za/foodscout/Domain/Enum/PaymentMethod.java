package co.za.foodscout.Domain.Enum;

public enum PaymentMethod {
    CashOnDelivery("Cash on Delivery"),
    CardOnDelivery("Card on Delivery"),
    DebitCard("Debit Card"),
    CreditCard("Credit card");

    private final String status;

    PaymentMethod(String status) {
        this.status = status;
    }

    public String getDescription(){
        return this.status;
    }

    public static PaymentMethod findByDescription(String description){
        for(PaymentMethod paymentMethod : values()){
            if( paymentMethod.getDescription().equals(description)){
                return paymentMethod;
            }
        }
        return null;
    }
}
