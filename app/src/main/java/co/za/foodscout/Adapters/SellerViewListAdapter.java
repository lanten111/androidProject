package co.za.foodscout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.FireStoreOrders;
import co.za.foodscout.Domain.FirestoreUser;
import co.za.foodscout.activities.seller.SellerDetailsActivity;
import foodscout.R;


public class SellerViewListAdapter extends RecyclerView.Adapter<SellerViewListAdapter.ViewHolder>{
    private List<FireStoreOrders> fireStoreOrdersList;
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseFirestore firestore;

    public SellerViewListAdapter(Context context, List<FireStoreOrders> firestoreDeliveryList, FirebaseFirestore firestore) {
        this.fireStoreOrdersList = firestoreDeliveryList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.firestore = firestore;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.seller_view_list, parent, false);
        return new ViewHolder(listItem);
    }
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final FireStoreOrders orders = fireStoreOrdersList.get(position);
        firestore.collection(Collections.user.name()).document(orders.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                holder.orderNumber.setText("Order#"+orders.getOrderNumber());
                holder.user.setText("Order by "+firestoreUser.getName());
                for (FireStoreCart cart: orders.getCartList()){
                    TextView textView = new TextView(context);
                    textView.setText(" - " + cart.getItemName());
                    holder.layout.addView(textView);
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SellerDetailsActivity.class);
                intent.putExtra("orderId", orders.getId());
                context.startActivity(intent);
            }
        });
    }


    @Override  
    public int getItemCount() {  
        return fireStoreOrdersList.size();
    }  
  
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        TextView orderNumber;
        TextView orderName;
        MaterialCardView cardView;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.SellerUserName);
            orderNumber = itemView.findViewById(R.id.SellerOrderNumber);
            orderName = itemView.findViewById(R.id.SellerOrderContact);
            layout = itemView.findViewById(R.id.sellerOrderListLayout);
            cardView = itemView.findViewById(R.id.sellerCardView);
        }

    }
}
