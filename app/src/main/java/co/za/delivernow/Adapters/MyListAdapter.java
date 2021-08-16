package co.za.delivernow.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;
import android.widget.RelativeLayout;  
import android.widget.TextView;  
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import co.za.delivernow.Domain.DeliveryTo;
import co.za.delivernow.R;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private List<DeliveryTo> deliveryToList = new ArrayList<>();
    private LayoutInflater mInflater;
  
   // RecyclerView recyclerView;  
    public MyListAdapter(Context context, List<DeliveryTo> deliveryToList) {
        this.deliveryToList = deliveryToList;
        this.mInflater = LayoutInflater.from(context);
    }  
    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.delivery_list, parent, false);
        return new ViewHolder(listItem);
    }  
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final DeliveryTo delivery = deliveryToList.get(position);
        holder.fromRetail.setText(deliveryToList.get(position).getRetailAddress());
        holder.userDestination.setText(deliveryToList.get(position).getUserAddress());
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+delivery.getRetailAddress(),Toast.LENGTH_LONG).show();
//            }
//        });
    }  
  
  
    @Override  
    public int getItemCount() {  
        return deliveryToList.size();
    }  
  
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fromRetail;
        public TextView userDestination;
        public RelativeLayout relativeLayout;  
        public ViewHolder(View itemView) {  
            super(itemView);  
            this.fromRetail = itemView.findViewById(R.id.deliveriesFromTxt);
            this.userDestination = itemView.findViewById(R.id.deliveriesToTxt);
        }  
    }

}  