package co.za.foodscout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.za.foodscout.Adapters.RetailListAdapter;
import co.za.foodscout.Domain.menu.Menu;
import co.za.foodscout.Domain.menu.MenuDetails;
import co.za.foodscout.Domain.retails.Retails;
import foodscout.R;

public class MenuDetailActivity extends DrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_menu_details);
        getLayoutInflater().inflate(R.layout.activity_menu_details, frameLayout);

        Intent intent = getIntent();
        String menuItermId = intent.getStringExtra("menuItermId");
        ProgressBar progressBar = findViewById(R.id.menuProgressBar);

        TextView menuItemName = findViewById(R.id.menuItemtName);
        TextView menuItemDesc = findViewById(R.id.menuItemtDesc);
        ImageView imageView = findViewById(R.id.menuDetailsImage);

        menuItemName.setText(intent.getStringExtra("menuItermName"));
        menuItemDesc.setText(intent.getStringExtra("menuItermDesc"));
        Picasso.get().load(Uri.parse(intent.getStringExtra("menuItermImage"))).into(imageView);

    }



}