package co.za.foodscout.Domain.matrix;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Matrix {

@SerializedName("routes")
@Expose
private List<Route> routes = null;
@SerializedName("status")
@Expose
private String status;

public List<Route> getRoutes() {
return routes;
}

public void setRoutes(List<Route> routes) {
this.routes = routes;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}
