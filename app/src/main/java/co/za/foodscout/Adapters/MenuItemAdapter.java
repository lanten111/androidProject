package co.za.foodscout.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.za.foodscout.Domain.menu.MenuDetails;
import co.za.foodscout.MenuActivity;
import co.za.foodscout.MenuDetailsFragment;
import foodscout.R;


public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder>{
    private List<MenuDetails> menuDetailsList;
    private LayoutInflater mInflater;
    private MenuActivity activity;

   // RecyclerView recyclerView;
    public MenuItemAdapter(MenuActivity activity, List<MenuDetails> menuDetailsList) {
        this.menuDetailsList = menuDetailsList;
        this.mInflater = LayoutInflater.from(activity.getBaseContext());
        this.activity = activity;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.menu_items, parent, false);
        return new ViewHolder(listItem);
    }
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final MenuDetails menuDetails = menuDetailsList.get(position);
        ImageView imageView = holder.imageView;
        holder.menuItemName.setText(menuDetails.getMenuname());
        holder.menuItemDescription.setText(menuDetails.getDescription());
        Picasso.get().load(Uri.parse(menuDetails.getImages().get(1))).into(imageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = activity.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.menuDetailsFragment, R.layout.fragment_menu_details);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


    @Override  
    public int getItemCount() {  
        return menuDetailsList.size();
    }  
  
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView menuItemName;
        TextView menuItemDescription;
        View relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.retailListImage);
            relativeLayout = itemView.findViewById(R.id.menuItermCardView);
            menuItemName = itemView.findViewById(R.id.menuItemtName);
            menuItemDescription = itemView.findViewById(R.id.menuItemtDesc);

        }

    }
}
