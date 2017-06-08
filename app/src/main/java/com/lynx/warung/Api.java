package com.lynx.warung;

/**
 * Created by rizalfahmi on 12/04/17.
 */

public class Api {

//    public static final String BASE_URL = "http://192.168.1.154/warung/web/api/";
    public static final String BASE_URL = "http://192.168.43.156/warung/web/api/";
    public static final String LIST_RESTAURANT = BASE_URL + "restaurant/list-restaurant";
    public static final String ADD_RESTAURANT= BASE_URL + "restaurant/add-restaurant";

    public static final String BODY_PARAM_CODE = "code";
    public static final String BODY_PARAM_DATA = "data";

    public static final int CODE_SUCCESS = 200;

    public static final String PARAM_ID = "id";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_LATITUDE = "latitude";
    public static final String PARAM_LONGITUDE = "longitude";
    public static final String PARAM_PICTURE = "picture";
    public static final String PARAM_DAY_OPEN = "day_open";
    public static final String PARAM_HOUR_OPEN = "hour_open";
    public static final String PARAM_ICON = "icon";



}
