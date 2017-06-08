package com.lynx.warung;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rizal Fahmi on 19-Feb-17.
 */

public class Util {

    public static final int REQUEST_SUCCESS = 200;

    public static boolean isConnectingToInternet(Context context){
        boolean isConnectedToWifi = false;
        boolean isConnectedToMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null){
            if(netInfo.isAvailable()){
                if(netInfo.isConnected()){
                    if(netInfo.getType() == ConnectivityManager.TYPE_WIFI){
                        isConnectedToWifi = true;
                    }else if(netInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                        isConnectedToMobile = true;
                    }
                }
            }
        }

        return isConnectedToMobile || isConnectedToWifi;
    }



    public static boolean isRequestSucces(JSONObject response) {
        try {
            if (response.getInt(Api.BODY_PARAM_CODE) == REQUEST_SUCCESS)
                return true;
            else return false;
        }
        catch (JSONException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static String getCategoryText(int category){
        String text = "";
        switch (category){
            case 1:
                text = "Fast Food";
                break;
            case 2:
                text = "Soto";
                break;
            case 3:
                text = "Penyetan";
                break;
            case 4:
                text = "Warteg";
                break;
            case 5:
                text = "Campur";
                break;
            case 6:
                text = "Tahu tek";
                break;
            case 7:
                text = "Nasi goreng";
                break;
        }
        return text;
    }

    public static String getDayText(int[] day){
        String dayString = "";
        for (int i=0;i<day.length;i++){
            Log.d("Warung","day int : "+day[i]);
            switch (day[i]){
                case 1:
                    dayString = dayString + "Senin, ";
                    break;
                case 2:
                    dayString = dayString + "Selasa, ";
                    break;
                case 3:
                    dayString = dayString + "Rabu, ";
                    break;
                case 4:
                    dayString = dayString + "Kamis, ";
                    break;
                case 5:
                    dayString = dayString + "Jumat, ";
                    break;
                case 6:
                    dayString = dayString + "Sabtu, ";
                    break;
                case 7:
                    dayString = dayString + "Ahad, ";
                    break;
                default: dayString = "";
                    break;
            }
        }
        return dayString.substring(0,dayString.length()-2);
    }


}
