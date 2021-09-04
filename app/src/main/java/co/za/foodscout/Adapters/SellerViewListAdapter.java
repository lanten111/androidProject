package co.za.foodscout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.DeliveryDetailsActivity;
import co.za.foodscout.Domain.FirestoreDelivery;
import foodscout.R;


public class SellerViewListAdapter extends RecyclerView.Adapter<SellerViewListAdapter.ViewHolder>{
    private List<FirestoreDelivery> firestoreDeliveryList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

   // RecyclerView recyclerView;
    public SellerViewListAdapter(Context context, List<FirestoreDelivery> firestoreDeliveryList) {
        this.firestoreDeliveryList = firestoreDeliveryList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.delivery_list, parent, false);
        return new ViewHolder(listItem);
    }
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final FirestoreDelivery delivery = firestoreDeliveryList.get(position);
        holder.fromRetail.setText("From: "+delivery.getRetailAddress());
        holder.userDestination.setText("To: "+delivery.getUserAddress());
        holder.orderDetails.setText("Order for "+delivery.getUserNames()+"  Contact No: "+delivery.getContactNo());
        if (delivery.isAssigned()){
            holder.deliveryStatus.setText(delivery.getDeliveryStatus() +" by "+delivery.getDriverName());
        } else {
            holder.deliveryStatus.setText(delivery.getDeliveryStatus());
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DeliveryDetailsActivity.class);
                intent.putExtra("deliveryUid", delivery.getId());
                context.startActivity(intent);
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
            userDestination = itemView.findViewById(R.id.deliveriesToTxt);
            orderDetails = itemView.findViewById(R.id.deliveriesOrderDetailTxt);
            relativeLayout = itemView.findViewById(R.id.deliveriesCardView);
            deliveryStatus = itemView.findViewById(R.id.deliveriesStatusTxt);
        }

    }
}
