package com.lynx.warung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView picture;
    String imgBase64 = "";
    Bitmap bitmapPicture;
    private LinearLayout addMenu;
    private LayoutInflater inflater;
    private LinearLayout containerMenu;
    private CheckBox checkBox24Hour;
    private EditText editTextOpenTime;
    private EditText editTextRestaurantName;
    private RadioGroup radioGroupType;
    private Button buttonSubmit;
    private CheckBox checkBoxSenin;
    private CheckBox checkBoxSelasa;
    private CheckBox checkBoxRabu;
    private CheckBox checkBoxKamis;
    private CheckBox checkBoxJumat;
    private CheckBox checkBoxSabtu;
    private CheckBox checkBoxMinggu;
    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        initView();

    }

    private void initView() {
        picture = (ImageView) findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryConfig config = new GalleryConfig.Build()
                        .limitPickPhoto(1)
                        .singlePhoto(true)
                        .hintOfPick("Silahkan pilih gambar")
                        .build();
                GalleryActivity.openActivity(AddItemActivity.this,100,config);
            }
        });

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        checkBoxSenin = (CheckBox) findViewById(R.id.checkboxSenin);
        checkBoxSelasa = (CheckBox) findViewById(R.id.checkboxSelasa);
        checkBoxRabu = (CheckBox) findViewById(R.id.checkboxRabu);
        checkBoxKamis = (CheckBox) findViewById(R.id.checkboxKamis);
        checkBoxJumat = (CheckBox) findViewById(R.id.checkboxJumat);
        checkBoxSabtu = (CheckBox) findViewById(R.id.checkboxSabtu);
        checkBoxMinggu = (CheckBox) findViewById(R.id.checkboxMinggu);

        radioGroupType = (RadioGroup) findViewById(R.id.radioGroupType);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        editTextRestaurantName = (EditText) findViewById(R.id.editTextName);

        checkBox24Hour = (CheckBox) findViewById(R.id.checkbox24Hour);
        editTextOpenTime = (EditText) findViewById(R.id.editTextOpenHour);

        checkBox24Hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextOpenTime.setEnabled(false);
                }
                else {
                    editTextOpenTime.setEnabled(true);
                }
            }
        });

        containerMenu = (LinearLayout) findViewById(R.id.container_menu);

        addMenu = (LinearLayout) findViewById(R.id.menu_adder);
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextRestaurantName.getText().toString();
                String openHour = "";
                String openDay = "";
                int type = 0;

                if(checkBox24Hour.isChecked())
                    openHour = "24 Jam";
                else openHour = editTextOpenTime.getText().toString();

                switch (radioGroupType.getCheckedRadioButtonId()){
                    case R.id.radioButtonFastFood:
                        type = 1;
                        break;
                    case R.id.radioButtonSoto:
                        type = 2;
                        break;
                    case R.id.radioButtonPenyetan:
                        type = 3;
                        break;
                    case R.id.radioButtonWarteg:
                        type = 4;
                        break;
                    case R.id.radioButtonCampur:
                        type = 5;
                        break;
                    case R.id.radioButtonTahuTelur:
                        type = 6;
                        break;
                    case R.id.radioButtonNasiGoreng:
                        type = 7;
                        break;
                    default:
                        type = 0;
                        break;
                }

                if(checkBoxSenin.isChecked())
                    openDay+="1,";
                if(checkBoxSelasa.isChecked())
                    openDay+="2,";
                if(checkBoxRabu.isChecked())
                    openDay+="3,";
                if(checkBoxKamis.isChecked())
                    openDay+="4,";
                if(checkBoxJumat.isChecked())
                    openDay+="5,";
                if(checkBoxSabtu.isChecked())
                    openDay+="6,";
                if(checkBoxMinggu.isChecked())
                    openDay+="7,";
                if(!openDay.equals(""))
                    openDay = openDay.substring(0,openDay.length()-1);

                String latitude = String.valueOf(googleMap.getCameraPosition().target.latitude);
                String longitude = String.valueOf(googleMap.getCameraPosition().target.longitude);

                if(!name.equals("") && !openDay.equals("") && !openHour.equals("") &&!imgBase64.equals("")){
                    JSONObject params = new JSONObject();
                    try {
                        params.put("name",name);
                        params.put("type",type);
                        params.put("latitude",latitude);
                        params.put("longitude",longitude);
                        params.put("day_open",openDay);
                        params.put("hour_open",openHour);

                        JSONArray menuArray = new JSONArray();

                        for(int i=0;i<containerMenu.getChildCount();i++){
                            JSONObject menus = new JSONObject();
                            menus.put("name",((EditText)containerMenu.getChildAt(i).findViewById(R.id.edit_menuName)).getText().toString());
                            menus.put("price",((EditText)containerMenu.getChildAt(i).findViewById(R.id.edit_menuPrice)).getText().toString());
                            menuArray.put(menus);
                        }

                        params.put("menu",menuArray);
                        params.put("picture",imgBase64);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("Warung","Json body : "+params);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Api.ADD_RESTAURANT, params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.getInt("code")==200) {
                                            Toast.makeText(getApplicationContext(), "Rumah makan berhasil ditambahkan", Toast.LENGTH_LONG).show();
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                        else Toast.makeText(AddItemActivity.this,"Query Failed",Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(AddItemActivity.this,"Invalid response format",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Warung","Error volley add restaurant : "+error);
                            Toast.makeText(getApplicationContext(),"Connection Error or invalid json body",Toast.LENGTH_LONG).show();
                        }
                    });

                    Volley.newRequestQueue(AddItemActivity.this).add(request);
                }
                else {
                    Toast.makeText(AddItemActivity.this,"Silhkan periksa input Anda sebelumnya melakukan submot data",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void addMenu() {
        CardView view = (CardView) inflater.inflate(R.layout.add_menu_layout,null);

        containerMenu.addView(view);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==100){
            String imgPath = ((List<String>)data.getSerializableExtra(GalleryActivity.PHOTOS)).get(0);
            bitmapPicture = BitmapFactory.decodeFile(imgPath);
            imgBase64 = getStringImage(bitmapPicture);
            Log.d("LOG","Image Base64 : "+imgBase64);
            Uri uri = Uri.fromFile(new File(imgPath));
            Picasso.with(this)
                    .load(uri)
                    .into(picture);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
