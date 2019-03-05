package org.senani.posapp;

import android.net.ConnectivityManager;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sachith on 8/22/18.
 */

public class Values {

    public static DatabaseReference mDatabase;
    public static HashMap<String,Item> items;
    public static ArrayList<Item> itemsList;
    public static ConnectivityManager cm;
}
