package com.lynx.warung;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PopUpActivity extends AppCompatActivity {

    private TextView category;
    private TextView name;
    private TextView hour;
    private TextView day;
    private ImageView image;
    private Restaurant restaurant;
    private TextView address;
    private RecyclerView recyclerMenu;
    private final String PICTURE_BASE_URL = "http://192.168.43.156/warung/Upload/Picture/";
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

    }

    private void initView() {
        geocoder = new Geocoder(this, Locale.getDefault());
        category = (TextView) findViewById(R.id.text_category);
        name = (TextView) findViewById(R.id.text_restaurantName);
        hour = (TextView) findViewById(R.id.text_openHour);
        day = (TextView) findViewById(R.id.text_openDay);
        address = (TextView) findViewById(R.id.text_address);
        image = (ImageView) findViewById(R.id.image_restaurant);
        recyclerMenu = (RecyclerView) findViewById(R.id.list_menu);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PopUpActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerMenu.setLayoutManager(linearLayoutManager);
        restaurant = getIntent().getParcelableExtra("restaurant");
//        getSupportActionBar().setTitle(restaurant.getName());
        recyclerMenu.setNestedScrollingEnabled(false);
        if(restaurant!=null){

            try {
                List<Address> addresses = geocoder.getFromLocation(Double.valueOf(restaurant.getLatitude()),
                        Double.valueOf(restaurant.getLongitude()),1);
                if(addresses.size() > 0){
                    String addressLine = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getAddressLine(1);
                    String country = addresses.get(0).getAddressLine(2);
                    address.setText(addressLine+", "+city+", "+country);
                }
                else address.setText("-");
            } catch (IOException e) {
                e.printStackTrace();
                address.setText("-");
            }

            category.setText(Util.getCategoryText(restaurant.getType()));
            name.setText(restaurant.getName());
            hour.setText(restaurant.getHour_open());
            String[] dayArray = restaurant.getDay_open().split(",");
            int[] dayInt = new int[dayArray.length];
            for (int i=0;i<dayArray.length;i++){
                dayInt[i] = Integer.valueOf(dayArray[i]);
            }
            day.setText(Util.getDayText(dayInt));
            Picasso.Builder picasso = new Picasso.Builder(this);
            picasso.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                    Log.d("Warung","Picasso error : "+exception);
                }
            });
            Log.d("Warung","Picasso url : "+PICTURE_BASE_URL+restaurant.getPicture());
            picasso.build()
                    .load(PICTURE_BASE_URL+restaurant.getPicture())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(image);

        }
        Log.d("Warung","Menu size : "+restaurant.getMenus().size());
        Adapter adapter = new Adapter(this,restaurant.getMenus());
        recyclerMenu.setAdapter(adapter);

    }

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder>{

        private ArrayList<Menu> menus;
        private Context context;

        public Adapter(Context context, ArrayList<Menu> menus) {
            this.menus = menus;
            this.context = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.menu_layout,parent,false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.price.setText("Rp."+menus.get(position).getPrice());
            holder.menuName.setText(menus.get(position).getName());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return menus.size();
        }


        class Holder extends RecyclerView.ViewHolder{
            private TextView menuName;
            private TextView price;

            public Holder(View view) {
                super(view);
                menuName = (TextView) view.findViewById(R.id.text_menuName);
                price = (TextView) view.findViewById(R.id.text_menuPrice);
            }
        }

    }

}
