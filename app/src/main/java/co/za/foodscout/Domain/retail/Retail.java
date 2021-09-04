package co.za.foodscout.Domain.retail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.za.foodscout.Domain.retails.RetailDetails;

@Generated("jsonschema2pojo")

public class Retail {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private RetailDetails data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RetailDetails getData() {
        return data;
    }

    public void setData(RetailDetails data) {
        this.data = data;
    }
}
