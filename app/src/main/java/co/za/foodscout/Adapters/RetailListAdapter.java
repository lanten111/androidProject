package co.za.foodscout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.za.foodscout.Domain.DeliveryTime;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Domain.Restaurant.Restaurant;
import co.za.foodscout.Domain.matrixNew.DurationMatrix;
import co.za.foodscout.Utils.Utils;
import co.za.foodscout.activities.menu.MenuActivity;
import foodscout.R;


public class RetailListAdapter extends RecyclerView.Adapter<RetailListAdapter.ViewHolder>{
    private List<Restaurant> retailList;
    private LayoutInflater mInflater;
    private Context context;
    private StorageReference storageReference;
    private FirestoreUser firestoreUser;
    private String key;

   // RecyclerView recyclerView;
    public RetailListAdapter(Context context, List<Restaurant> retailList, StorageReference storageReference, FirestoreUser firestoreUser, String key) {
        this.retailList = retailList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.storageReference = storageReference;
        this.firestoreUser = firestoreUser;
        this.key = key;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.retail_list, parent, false);
        return new ViewHolder(listItem);
    }
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final Restaurant restaurant = retailList.get(position);
        getDeliveryTime(Utils.getLatLong(firestoreUser.getLocation()), Utils.getLatLong(restaurant.getLocation()),holder.retailDeliveryTime, key );
        holder.retailName.setText(restaurant.getName());
        holder.retailAddress.setText(Utils.getAddress(restaurant.getLocation(), context));
        holder.retailRating.setRating(restaurant.getRating());
        ImageView imageView = holder.imageView;
        storageReference.child(restaurant.getMainImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("restaurantId", restaurant.getId());
                intent.putExtra("restaurantDetails", holder.retailDeliveryTime.getText());
                context.startActivity(intent);
            }
        });
    }


    @Override  
    public int getItemCount() {  
        return retailList.size();
    }  
  
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView retailName;
        TextView retailDeliveryTime;
        TextView retailDistance;
        RatingBar retailRating;
        TextView retailAddress;
        View relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.retailListImage);
            retailName = itemView.findViewById(R.id.retailListName);
            retailDeliveryTime = itemView.findViewById(R.id.retailListDeliveryTime);
//            retailDistance = itemView.findViewById(R.id.retailListDistance);
            retailRating = itemView.findViewById(R.id.retailRestaurantRatingBar);
            retailAddress = itemView.findViewById(R.id.retailListAddress);
            relativeLayout = itemView.findViewById(R.id.retailCardView);

        }

    }


    private void getDeliveryTime(LatLng origin, LatLng dest, TextView retailDeliveryTime, String gKey) {

        DeliveryTime deliveryTime = new DeliveryTime();
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        String key = "key=" + gKey;
        String parameters = str_origin + "&" + str_dest+ "&" + key;
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
                        retailDeliveryTime.setText(" Estimated Delivery Time: "+ deliveryTime.getETA() + " " + deliveryTime.getDistance()+ " away");
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
