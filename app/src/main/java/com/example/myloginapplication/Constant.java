package com.example.myloginapplication;

import java.util.HashMap;
import java.util.List;

public class Constant {
    public static final String MAIN_URL =  "https://mylocapi.herokuapp.com";
    //others url
    public static final String SIGNUP_URL = MAIN_URL+"/auth/register";
    public static final String LOGIN_URL = MAIN_URL+"/auth/login";
    public static final String SAVE_URL = MAIN_URL+"/history/upload";
    public static final String GET_LOCATIONS = MAIN_URL+"/location";
    public static final String GET_HISTORY = MAIN_URL+"/history/";
    public static final String SEND_HISTORY = MAIN_URL+"/history";
    public static final String GET_ADD_BALANCE = MAIN_URL+"/user/add-to-card/";
    public static final String GET_BALANCE = MAIN_URL+"/user/card/";

    //Keys for server communications

    public static final String KEY_ROLL = "username";
    public static final String KEY_PASSWORD = "password";


    //share preference
    //We will use this to store the user cell number into shared preference
    //This would be used to store the cell of current logged in user
    public static final String ROLL_SHARED_username = "asdasd";
    public static final String ROLL_SHARED_token = "roll";
    public static final String ROLL_SHARED_userid = "iid";

}