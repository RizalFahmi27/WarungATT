package com.lynx.warung;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rizalfahmi on 12/04/17.
 */

public class Menu implements Parcelable{

    private int id;
    private int id_restaurant;
    private String name;
    private String price;

    public Menu(int id, int id_restaurant, String name, String price) {
        this.id = id;
        this.id_restaurant = id_restaurant;
        this.name = name;
        this.price = price;
    }

    protected Menu(Parcel in) {
        id = in.readInt();
        id_restaurant = in.readInt();
        name = in.readString();
        price = in.readString();
    }

    public Menu(JSONObject jsonObject){
        try {
            this.id = jsonObject.getInt("id");
            this.id_restaurant = jsonObject.getInt("id_restaurant");
            this.name = jsonObject.getString("name");
            this.price = jsonObject.getString("price");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setId_restaurant(int id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_restaurant);
        dest.writeString(name);
        dest.writeString(price);
    }
}
