package com.lynx.warung;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rizalfahmi on 12/04/17.
 */

public class Restaurant implements Parcelable {
    private int id;
    private String name;
    private String picture;
    private String latitude;
    private String longitude;
    private String icon;
    private String day_open;
    private String hour_open;
    private int type;
    private ArrayList<Menu> menus;

    public Restaurant(int id, String name, String picture, String latitude, String longitude,
                      String icon, String day_open, String hour_open, int type, ArrayList<Menu> menus) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        this.day_open = day_open;
        this.hour_open = hour_open;
        this.type = type;
        this.menus = menus;
    }

    public Restaurant(JSONObject jsonObject){
        try {
            this.id = jsonObject.getInt(Api.PARAM_ID);
            this.name = jsonObject.getString(Api.PARAM_NAME);
            this.picture = jsonObject.getString(Api.PARAM_PICTURE);
            this.latitude = jsonObject.getString(Api.PARAM_LATITUDE);
            this.longitude = jsonObject.getString(Api.PARAM_LONGITUDE);
            this.icon = jsonObject.getString(Api.PARAM_ICON);
            this.day_open = jsonObject.getString(Api.PARAM_DAY_OPEN);
            this.hour_open = jsonObject.getString(Api.PARAM_HOUR_OPEN);
            this.type = jsonObject.getInt(Api.PARAM_TYPE);
            menus = new ArrayList<>();
            JSONArray arrayMenu = jsonObject.getJSONArray("menu");
            for (int i=0;i<arrayMenu.length();i++){
                JSONObject objectMenu = arrayMenu.getJSONObject(i);
                menus.add(new Menu(objectMenu));
                Log.d("Warung","Menu from json : " +menus.get(i).getName());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Restaurant(Parcel in) {
        id = in.readInt();
        name = in.readString();
        picture = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        icon = in.readString();
        day_open = in.readString();
        hour_open = in.readString();
        type = in.readInt();
        menus = new ArrayList<>();
        in.readTypedList(menus,Menu.CREATOR);
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDay_open(String day_open) {
        this.day_open = day_open;
    }

    public void setHour_open(String hour_open) {
        this.hour_open = hour_open;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getIcon() {
        return icon;
    }

    public String getDay_open() {
        return day_open;
    }

    public String getHour_open() {
        return hour_open;
    }

    public int getType() {
        return type;
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(picture);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(icon);
        dest.writeString(day_open);
        dest.writeString(hour_open);
        dest.writeInt(type);
        dest.writeTypedList(menus);
    }
}
