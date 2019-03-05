package org.senani.posapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ImageView splash;
    MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Values.mDatabase = FirebaseDatabase.getInstance().getReference();
        Values.items=new HashMap<>();
        Values.itemsList=new ArrayList<>();
        Values.cm =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if(!isInternetOn()){
            showDialog();
        }

        listView= (ListView) findViewById(R.id.list_view);
        splash = (ImageView) findViewById(R.id.splash);
        menuAdapter = new MenuAdapter(getApplicationContext());
        listView.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getApplicationContext(),GRNActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),DailyActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),MonthlyActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),AddPaymentActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(),PaymentActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 5:
                        startActivity(new Intent(getApplicationContext(),AddItemActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 6:
                        startActivity(new Intent(getApplicationContext(),UpdateItemActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 7:
                        startActivity(new Intent(getApplicationContext(),ViewItemActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 8:
                        startActivity(new Intent(getApplicationContext(),ToOrderActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 9:
                        startActivity(new Intent(getApplicationContext(),PwdActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                }
            }
        });

        load();

    }

    public void load(){

        Values.mDatabase.child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Values.items.clear();
                Values.itemsList.clear();

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    try {
                        Item item = messageSnapshot.getValue(Item.class);
                        Values.items.put(item.getC(),item);
                        Values.itemsList.add(item);
                    }catch (Exception e){

                    }
                }

                if(splash.getVisibility()==View.VISIBLE) {
                    splash.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_righ);
    }

    public final boolean isInternetOn() {

        try {
            // Check for network connections
            if (Values.cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    Values.cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    Values.cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    Values.cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                return true;

            } else if (
                    Values.cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            Values.cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                return false;
            }
            return true;
        }catch (Exception e){
            return true;
        }
    }

    public void showDialog(){
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle("");
            alertDialog.setMessage("Internet not available, Check your internet connectivity and try again");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialog.show();
        } catch (Exception e) {}
    }
}
