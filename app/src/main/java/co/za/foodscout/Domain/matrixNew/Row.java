package co.za.foodscout.Domain.matrixNew;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Row {

@SerializedName("elements")
@Expose
private List<Element> elements = null;

public List<Element> getElements() {
return elements;
}

public void setElements(List<Element> elements) {
this.elements = elements;
}

}