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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.za.foodscout.Domain.Restaurant.Menu;
import co.za.foodscout.activities.menu.MenuDetailActivity;
import foodscout.R;


public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder>{
    private List<Menu> menuList;
    private LayoutInflater mInflater;
    private Context context;
    private StorageReference storageReference;

   // RecyclerView recyclerView;
    public MenuItemAdapter(Context context, List<Menu> menuList, StorageReference storageReference) {
        this.menuList = menuList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.storageReference = storageReference;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.menu_items, parent, false);
        return new ViewHolder(listItem);
    }
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final Menu menu = menuList.get(position);
        ImageView imageView = holder.imageView;
        holder.menuItemName.setText(menu.getMenuItemName());
        holder.menuItemDescription.setText(menu.getMenuItemDescription());
        storageReference.child(menu.getMenuItemImagesId().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MenuDetailActivity.class);
                intent.putExtra("restaurantId", menu.getRestaurantId());
                intent.putExtra("menuItermName", menu.getMenuItemName());
                intent.putExtra("menuItermDesc", menu.getMenuItemDescription());
                intent.putExtra("menuItermId", menu.getMenuItemId());
                context.startActivity(intent);
            }
        });
    }


    @Override  
    public int getItemCount() {  
        return menuList.size();
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
