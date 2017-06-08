package com.lynx.warung;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private CircleButton myLocation;
    private CircleButton refresh;
    private CircleButton addWarung;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private Marker myMarker;
    private ProgressDialog mProgressDialog;
    private ArrayList<Restaurant> restaurantList;
    private HashMap<Marker, Integer> markers;
    private CircleButton filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initModules();
        initView();
    }

    private void initModules() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        restaurantList = new ArrayList<>();
        markers = new HashMap<>();
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if(myMarker!=null)
                    myMarker.remove();
                myMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
                mMap.animateCamera(cameraUpdate);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    private void initView() {
        myLocation = (CircleButton) findViewById(R.id.myLocation);
        refresh = (CircleButton) findViewById(R.id.refresh);
        addWarung = (CircleButton) findViewById(R.id.add);
        filter = (CircleButton) findViewById(R.id.filter);
        mProgressDialog = new ProgressDialog(this);
        getMarker();

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMarker();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterRestaurant();
            }
        });

        addWarung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,AddItemActivity.class);
                startActivityForResult(intent,10);
            }
        });
    }

    private void filterRestaurant() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View filterView = inflater.inflate(R.layout.filter_layout,null);
        alert.setView(filterView);
        alert.setTitle("Filter rumah makan");

        final EditText edit_priceRange = (EditText) filterView.findViewById(R.id.edit_priceRange);
        final CheckBox checkBoxAllDay = (CheckBox) filterView.findViewById(R.id.checkboxAllDay);
        final CheckBox checkBox24H = (CheckBox) filterView.findViewById(R.id.checkbox24Hour);
        final CheckBox checkBoxFastFood = (CheckBox) filterView.findViewById(R.id.checkboxFastFood);
        final CheckBox checkBoxSoto = (CheckBox) filterView.findViewById(R.id.checkboxSoto);
        final CheckBox checkBoxPenyetan = (CheckBox) filterView.findViewById(R.id.checkboxPenyetan);
        final CheckBox checkBoxWarteg = (CheckBox) filterView.findViewById(R.id.checkboxWarteg);
        final CheckBox checkBoxCampur = (CheckBox) filterView.findViewById(R.id.checkboxCampur);
        final CheckBox checkBoxTahuTelor = (CheckBox) filterView.findViewById(R.id.checkboxTahuTelur);
        final CheckBox checkBoxNasgor = (CheckBox) filterView.findViewById(R.id.checkboxNasgor);
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer price = 0;
                if(edit_priceRange.getText().toString().equals("")){
                    price = 1000000;
                }
                else {
                    price = Integer.valueOf(edit_priceRange.getText().toString());
                }
                    ArrayList<Integer> selectedCategory = new ArrayList<Integer>();
                    if(checkBoxFastFood.isChecked())
                        selectedCategory.add(1);
                    if(checkBoxSoto.isChecked())
                        selectedCategory.add(2);
                    if(checkBoxPenyetan.isChecked())
                        selectedCategory.add(3);
                    if(checkBoxWarteg.isChecked())
                        selectedCategory.add(4);
                    if(checkBoxCampur.isChecked())
                        selectedCategory.add(5);
                    if(checkBoxTahuTelor.isChecked())
                        selectedCategory.add(6);
                    if(checkBoxNasgor.isChecked())
                        selectedCategory.add(7);
                    loadMarker(price, selectedCategory, checkBoxAllDay.isChecked(), checkBox24H.isChecked());
            }
        });

        alert.show();
    }

    private void getMarker() {
        mProgressDialog.setMessage("Please wait....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        if(Util.isConnectingToInternet(this)){
            StringRequest request = new StringRequest(Request.Method.GET, Api.LIST_RESTAURANT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("warung","Response list restaurant : "+response);
                                JSONObject jsonObject = new JSONObject(response);
                                if(Util.isRequestSucces(jsonObject)){
                                    mProgressDialog.dismiss();
                                    JSONArray jsonArray = jsonObject.getJSONArray(Api.BODY_PARAM_DATA);
                                    for(int i=0;i<jsonArray.length();i++){
                                        restaurantList.add(new Restaurant((JSONObject) jsonArray.get(i)));
                                    }
                                    ArrayList<Integer> category = new ArrayList<>();
                                    category.add(1);category.add(2);category.add(3);category.add(4);category.add(5);category.add(6);category.add(7);
                                    loadMarker(100000,category,false,false);
                                }
                                else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(MapsActivity.this, "Error occured while parsing data", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mProgressDialog.dismiss();
                                Toast.makeText(MapsActivity.this,"Error occured",Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressDialog.dismiss();
                    Toast.makeText(MapsActivity.this,"Error occured, please check your internet connection",Toast.LENGTH_LONG).show();
                }
            });
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);

        } else  { Toast.makeText(this,"Make sure you have internet connection",Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }
    }

    private void loadMarker(Integer price, ArrayList<Integer> selectedCategory, boolean isAllDayOpen, boolean is24HourOpen) {
        if(markers!=null && markers.size() > 0){
            for (Map.Entry<Marker,Integer> markerEntry : markers.entrySet()){
                markerEntry.getKey().remove();
            }
        }
        markers.clear();
        int priceCounts = 0;
        for(int i=0;i<restaurantList.size();i++){
            for(Menu menu : restaurantList.get(i).getMenus()){
                if(Integer.valueOf(menu.getPrice())>price){
                    priceCounts++;
                    Log.d("Warung","price counts : "+priceCounts);
                }
            }
            if(priceCounts>=restaurantList.get(i).getMenus().size()) {
                priceCounts = 0;
                continue;
            }
            boolean isCategoryListed = false;
            for (int j = 0; j < selectedCategory.size(); j++) {
                if(restaurantList.get(i).getType()==selectedCategory.get(j)){
                    isCategoryListed = true;
                    break;
                  }
            }
            Log.d("Warung","category : "+isCategoryListed);
            if(!isCategoryListed)
                continue;

            if(is24HourOpen){
                if(!restaurantList.get(i).getHour_open().equals("24 Jam"))
                    continue;
            }

            if(isAllDayOpen){
                if(!restaurantList.get(i).getDay_open().equals("1,2,3,4,5,6,7"))
                    continue;
            }

            byte[] decodedString = Base64.decode(restaurantList.get(i).getIcon(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(restaurantList.get(i).getLatitude()),Double.valueOf(restaurantList.get(i).getLongitude())))
            .title(restaurantList.get(i).getName())
            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

            markers.put(marker,i);
            priceCounts = 0;
        }

        mMap.setOnMarkerClickListener(this);
    }

    private void getLocation() {
        if(Util.isConnectingToInternet(this)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET
                    },10);
                    return;
                }
            }

            Log.d("SMD","Locatin : getLocation");
            mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);
        } else Toast.makeText(this,"Make sure you have internet connection",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    getLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Warung","Masuk onACtivityResult");
        if(requestCode==10 && resultCode==RESULT_OK){
            Log.d("Warung","Masuk query berhasil");
            getMarker();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //getLocation();
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this,PopUpActivity.class);
        intent.putExtra("restaurant",restaurantList.get(markers.get(marker)));
        startActivity(intent);
        return false;
    }
}
