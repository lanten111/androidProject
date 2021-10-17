package co.za.foodscout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.Enum.OrderStatus;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.Utils.Utils;
import co.za.foodscout.activities.delivery.DeliveryDetailsActivity;
import co.za.foodscout.Domain.FirestoreDelivery;
import foodscout.R;


public class DeliveryListAdapter extends RecyclerView.Adapter<DeliveryListAdapter.ViewHolder>{
    private List<FirestoreDelivery> firestoreDeliveryList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseFirestore firestore;
    private FirestoreUser firestoreUser;
  
   // RecyclerView recyclerView;  
    public DeliveryListAdapter(Context context, List<FirestoreDelivery> firestoreDeliveryList, FirebaseFirestore firestore, FirestoreUser firestoreUser) {
        this.firestoreDeliveryList = firestoreDeliveryList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.firestore = firestore;
        this.firestoreUser = firestoreUser;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.delivery_list, parent, false);
        return new ViewHolder(listItem);
    }
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final FirestoreDelivery firestoreDelivery = firestoreDeliveryList.get(position);
        holder.fromRetail.setText("From: "+ Utils.getAddress(firestoreDelivery.getRetailLocation(), context));
        holder.userDestination.setText("To: "+Utils.getAddress(firestoreDelivery.getUserLocation(), context));
        holder.orderDetails.setText("Order for "+firestoreDelivery.getUserNames()+"  Contact No: "+firestoreDelivery.getContactNo());
        if (firestoreDelivery.isAssigned()){
            holder.deliveryStatus.setText(firestoreDelivery.getDeliveryStatus() +" by "+firestoreDelivery.getDriverName());
        } else {
            holder.deliveryStatus.setText(firestoreDelivery.getDeliveryStatus());
        }
        holder.deliveryStatus.setText(OrderStatus.Preparing.getDescription());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDelivery.setDeliveryStatus(OrderStatus.OnRoute.name());
//                firestoreDelivery.setDeliveryPicked(true);
                firestoreDelivery.setAssigned(true);
                firestoreDelivery.setAssigneeId(firestoreUser.getId());
                firestoreDelivery.setDriverName(firestoreUser.getName());
                firestore.collection(Collections.delivery.name()).document(firestoreDelivery.getId()).set(firestoreDelivery).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(context, DeliveryDetailsActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        });
    }


    @Override  
    public int getItemCount() {  
        return firestoreDeliveryList.size();
    }  
  
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fromRetail;
        public TextView userDestination;
        TextView orderDetails;
        TextView deliveryStatus;
        View relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            fromRetail = itemView.findViewById(R.id.deliveriesFromTxt);
            userDestination = itemView.findViewById(R.id.SellerDeliveriesTo);
            orderDetails = itemView.findViewById(R.id.deliveriesOrderDetailTxt);
            relativeLayout = itemView.findViewById(R.id.deliveriesCardView);
            deliveryStatus = itemView.findViewById(R.id.deliveriesStatusTxt);
        }

    }
}
