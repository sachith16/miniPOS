package org.senani.posappfront;

import android.net.ConnectivityManager;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Values {

    public static DatabaseReference mDatabase;
    public static HashMap<String,Item> items;
    public static ArrayList<Item> itemsList;
    public static ConnectivityManager cm;
}
