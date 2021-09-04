package co.za.foodscout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.za.foodscout.Domain.retails.RetailDetails;
import co.za.foodscout.MenuActivity;
import foodscout.R;


public class RetailListAdapter extends RecyclerView.Adapter<RetailListAdapter.ViewHolder>{
    private List<RetailDetails> retailList;
    private LayoutInflater mInflater;
    private Context context;

   // RecyclerView recyclerView;
    public RetailListAdapter(Context context, List<RetailDetails> retailList) {
        this.retailList = retailList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.retail_list, parent, false);
        return new ViewHolder(listItem);
    }
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final RetailDetails retailDetails = retailList.get(position);
//        holder.retailDistance.setText("3KM");
        holder.retailDeliveryTime.setText("30Mins 3KM away");
        holder.retailName.setText(retailDetails.getBusinessname());
        holder.retailAddress.setText(retailDetails.getAddress());
        holder.retailRating.setText(String.valueOf(retailDetails.getReviews()));
        ImageView imageView = holder.imageView;
        Picasso.get().load(Uri.parse(retailDetails.getImage())).into(imageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("retailId", retailDetails.getId());
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
        TextView retailRating;
        TextView retailAddress;
        View relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.retailListImage);
            retailName = itemView.findViewById(R.id.retailListName);
            retailDeliveryTime = itemView.findViewById(R.id.retailListDeliveryTime);
//            retailDistance = itemView.findViewById(R.id.retailListDistance);
            retailRating = itemView.findViewById(R.id.retailListRating);
            retailAddress = itemView.findViewById(R.id.retailListAddress);
            relativeLayout = itemView.findViewById(R.id.retailCardView);

        }

    }
}
