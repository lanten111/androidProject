package co.za.foodscout.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import co.za.foodscout.Domain.Enum.Collections;
import co.za.foodscout.Domain.FireStoreCart;
import co.za.foodscout.Domain.Restaurant.Addons;
import co.za.foodscout.Domain.Restaurant.MenuAddons;
import foodscout.R;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.ViewHolder>{

    private final LayoutInflater mInflater;
    private final Context context;
    private final List<FireStoreCart> fireStoreCartList;
    private final FirebaseFirestore firestore;

    public OrderViewAdapter(Context context, List<FireStoreCart> fireStoreCartList, FirebaseFirestore firestore){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.fireStoreCartList = fireStoreCartList;
        this.firestore = firestore;
    }

    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem= mInflater.inflate(R.layout.order_summary_list, parent, false);
        return new ViewHolder(listItem);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
         final FireStoreCart fireStoreCart = fireStoreCartList.get(position);
        TableLayout tableLayout = new TableLayout(context);
        TableRow tableRow0 = new TableRow(context);
        TextView orderName = new TextView(context);
        TextView orderPrice = new TextView(context);
        ImageView deleteItemBtn = new ImageView(context);

        deleteItemBtn.setBackgroundResource(R.drawable.delete);

        orderPrice.setTypeface(null, Typeface.BOLD);
        orderName.setTypeface(null, Typeface.BOLD);
        orderName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(40) });
        orderName.setSingleLine(false);
        orderPrice.setTextSize(1, 13);
        orderName.setTextSize(1, 13);

        deleteItemBtn.setId(position);
        orderName.setId(position+500);
        orderPrice.setId(position+600);
        orderName.setText(position + 1+"  -  "+fireStoreCart.getItemName());
        orderPrice.setText("  R "+fireStoreCart.getItemPrice()+"     ");

        tableLayout.setId(position+400);
        tableRow0.setId(position+300);
        tableRow0.addView(orderName);
        tableRow0.addView(orderPrice);
        tableRow0.addView(deleteItemBtn);
        tableLayout.addView(tableRow0);
        for (MenuAddons menuAddons: fireStoreCart.getMenuAddonsList()){
            int textViewId = 100;
            int rowId = 200;
            for(Addons addons: menuAddons.getAddonsList()){
                TableRow tableRow1 = new TableRow(context);
                TextView textView = new TextView(context);
                tableRow1.setId(rowId);
                textView.setId(textViewId);
                textView.setText("    " +menuAddons.getName()+": "+addons.getAddon());
                tableRow1.addView(textView);
                tableLayout.addView(tableRow1);
                textViewId++;
                rowId++;
            }
        }
        holder.frameLayout.addView(tableLayout);

        deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FireStoreCart fireStoreCart1 = fireStoreCartList.get(deleteItemBtn.getId());
                firestore.collection(Collections.cart.name()).document(fireStoreCart1.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        fireStoreCartList.remove(deleteItemBtn.getId());
                        notifyItemRemoved(deleteItemBtn.getId());
                        Toast.makeText(context, "item removed", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return fireStoreCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout frameLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.orderDetailLayout);

        }

    }

}
