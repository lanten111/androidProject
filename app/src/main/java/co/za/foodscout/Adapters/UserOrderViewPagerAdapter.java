package co.za.foodscout.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.List;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.DeliveryTime;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.FireStoreOrders;
import co.za.foodscout.Domain.FirestoreDelivery;
import co.za.foodscout.Domain.matrixNew.DurationMatrix;
import co.za.foodscout.Utils.Utils;
import foodscout.R;

public class UserOrderViewPagerAdapter extends RecyclerView.Adapter<UserOrderViewPagerAdapter.MyViewHolder> {

    private Context context;
    LatLng mOrigin;
    LatLng mDestination;
    private List<FirestoreDelivery> fireStoreOrdersList;
    private FirebaseFirestore firestore;
    CircularProgressIndicator circularProgressIndicator;
    LinearLayout linearLayout;

    public UserOrderViewPagerAdapter(Context context, List<FirestoreDelivery> firestoreDeliveryList, FirebaseFirestore firestore, LinearLayout linearLayout, CircularProgressIndicator circularProgressIndicator) {
        this.context = context;
        this.fireStoreOrdersList = firestoreDeliveryList;
        this.firestore = firestore;
        this.circularProgressIndicator = circularProgressIndicator;
        this.linearLayout = linearLayout;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_order_view_pager, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FirestoreDelivery firestoreDelivery = fireStoreOrdersList.get(position);
        firestore.collection(Collections.order.name()).document(firestoreDelivery.getOrderId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FireStoreOrders fireStoreOrders =  documentSnapshot.toObject(FireStoreOrders.class);
                mOrigin = Utils.getLatLong(firestoreDelivery.getRetailLocation());
                mDestination = Utils.getLatLong(firestoreDelivery.getUserLocation());
                for (FireStoreCart cart: fireStoreOrders.getCartList()){
                    TextView orderName = new TextView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 15, 0, 0);
                    orderName.setLayoutParams(layoutParams);
                    orderName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.food, 0 , 0 ,0);
                    orderName.setText(" "+cart.getItemName());
                    holder.retailNameLayout.addView(orderName);
                }
                holder.retailName.setText(fireStoreOrders.getRetailName());
                holder.orderFrom.setText("  From: " + Utils.getAddress(firestoreDelivery.getRetailLocation(), context));
                holder.orderTo.setText("TO: " + Utils.getAddress(firestoreDelivery.getUserLocation(), context));
                getDeliveryTime(mOrigin, mDestination, holder.deliveryTime);
                holder.orderHead.setText("Order Status: "+ firestoreDelivery.getDeliveryStatus().getDescription());
                holder.totalPrice.setText(Html.fromHtml("Paid amount:" + "<font color=#f57f17>" +" R"+fireStoreOrders.getTotalPrice().toString()));
                linearLayout.setVisibility(View.VISIBLE);
                circularProgressIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fireStoreOrdersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView retailName;
        TextView orderFrom;
        TextView orderTo;
        TextView orderHead;
        TextView deliveryTime;
        TextView totalPrice;
        LinearLayout retailNameLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            retailName = itemView.findViewById(R.id.retailName);
            orderFrom = itemView.findViewById(R.id.orderFromTxt);
            orderTo = itemView.findViewById(R.id.orderToTxt);
            orderHead = itemView.findViewById(R.id.orderHeadTxt);
            deliveryTime = itemView.findViewById(R.id.deliveryTime);
            retailNameLayout = itemView.findViewById(R.id.retailNameLayout);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }


    private void getDeliveryTime(LatLng origin, LatLng dest, TextView deliveryT) {

        DeliveryTime deliveryTime = new DeliveryTime();
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        String key = "key=" + context.getString(R.string.google_maps_key);
        String parameters = str_origin + "&" + str_dest + "&" + key;
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?" + parameters;

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        DurationMatrix durationMatrix = gson.fromJson(response, DurationMatrix.class);
                        deliveryTime.setDistance(durationMatrix.getRows().get(0).getElements().get(0).getDistance().getText());
                        deliveryTime.setETA(durationMatrix.getRows().get(0).getElements().get(0).getDuration().getText());
                        deliveryT.setText(" Estimated Delivery Time " + deliveryTime.getETA() + " Distance " + deliveryTime.getDistance());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

}