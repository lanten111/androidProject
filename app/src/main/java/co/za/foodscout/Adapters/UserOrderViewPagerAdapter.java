//package co.za.foodscout.Adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.storage.StorageReference;
//
//import java.util.List;
//
//import co.za.foodscout.Domain.FireStoreOrders;
//import foodscout.R;
//
//public class UserOrderViewPagerAdapter extends RecyclerView.Adapter<UserOrderViewPagerAdapter.MyViewHolder> {
//
//    private Context context;
//    private List<FireStoreOrders> fireStoreOrdersList;
//    private StorageReference storageReference;
//
//    public UserOrderViewPagerAdapter(Context context, List<FireStoreOrders> fireStoreOrders) {
//        this.context = context;
//        this.fireStoreOrdersList = fireStoreOrders;
//        this.storageReference  = storageReference;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.view_pager, parent, false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return fireStoreOrdersList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        RecyclerView recyclerView;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            recyclerView = itemView.findViewById(R.id.menuRecycledView);
//        }
//    }
//}