package co.za.foodscout.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import co.za.foodscout.Domain.Restaurant.Menu;
import foodscout.R;

public class MenuViewPagerAdapter extends RecyclerView.Adapter<MenuViewPagerAdapter.MyViewHolder> {

    private Context context;
    private List<List<Menu>> menuListCatagorized = new ArrayList<>();
    private StorageReference storageReference;

    public MenuViewPagerAdapter(Context context, List<List<Menu>> menuListCatagorized, StorageReference storageReference ) {
        this.context = context;
        this.menuListCatagorized = menuListCatagorized;
        this.storageReference  = storageReference;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_view_pager, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            MenuItemAdapter adapter = new MenuItemAdapter(context, menuListCatagorized.get(position), storageReference);
            holder.recyclerView.setHasFixedSize(false);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerView.setAdapter(adapter);
            holder.recyclerView.setHasFixedSize(false);
    }

    @Override
    public int getItemCount() {
        return menuListCatagorized.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.menuRecycledView);
        }
    }
}