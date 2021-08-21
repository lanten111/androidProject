package co.za.delivernow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import co.za.delivernow.DeliveryDetailsActivity;
import co.za.delivernow.Domain.DeliveryTo;
import co.za.delivernow.R;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private List<DeliveryTo> deliveryToList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
  
   // RecyclerView recyclerView;  
    public MyListAdapter(Context context, List<DeliveryTo> deliveryToList) {
        this.deliveryToList = deliveryToList;
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
        final DeliveryTo delivery = deliveryToList.get(position);
        holder.fromRetail.setText(delivery.getRetailAddress());
        holder.userDestination.setText(delivery.getUserAddress());
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
        return deliveryToList.size();
    }  
  
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fromRetail;
        public TextView userDestination;
        View relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            fromRetail = itemView.findViewById(R.id.deliveriesFromTxt);
            userDestination = itemView.findViewById(R.id.deliveriesToTxt);
            relativeLayout = itemView.findViewById(R.id.deliveriesCardView);
        }

    }
}
