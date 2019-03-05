package org.senani.posappfront;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText code;
    EditText price;
    EditText qty;
    EditText editPwd;
    Button btnSave;
    Button btnEasy;
    ImageButton btnClear;
    ImageButton btnNext;
    ImageButton btnPwd;
    ListView listView;
    GRNAdapter grnAdapter;
    ArrayList<GRNItem> list;
    public boolean success;
    String parent;
    RelativeLayout splash;
    RelativeLayout contentLayout;
    TextView tvTotal;
    float total=0;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);       
        
        code= (EditText) findViewById(R.id.editcode);
        price = (EditText) findViewById(R.id.editprice);
        qty = (EditText) findViewById(R.id.editqty);
        editPwd = (EditText) findViewById(R.id.editPwd);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        btnSave = (Button) findViewById(R.id.btnadd);
        btnEasy = (Button) findViewById(R.id.btneasy);
        btnClear = (ImageButton) findViewById(R.id.btnremove);
        btnNext = (ImageButton) findViewById(R.id.btnnext);
        btnPwd = (ImageButton) findViewById(R.id.btnPwd);
        splash = (RelativeLayout) findViewById(R.id.splash);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);

        Values.mDatabase = FirebaseDatabase.getInstance().getReference();
        Values.items=new HashMap<>();
        Values.itemsList=new ArrayList<>();
        Values.cm =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        listView = (ListView) findViewById(R.id.list_view);
        list=new ArrayList<>();
        grnAdapter= new GRNAdapter(getApplicationContext(),list);
        listView.setAdapter(grnAdapter);

        if(!isInternetOn()){
            showDialogInternet();
        }

        code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(Values.items.containsKey(code.getText().toString())){
                    Toast.makeText(MainActivity.this, Values.items.get(code.getText().toString()).getN()+" - "+Values.items.get(code.getText().toString()).getP(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!code.getText().toString().isEmpty() && !price.getText().toString().isEmpty() && !qty.getText().toString().isEmpty()) {
                    if(Values.items.containsKey(code.getText().toString())) {
                        try {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            GRNItem grnItem = new GRNItem(code.getText().toString(), Float.valueOf(price.getText().toString()), Float.valueOf(qty.getText().toString()),dateFormat.format(date));
                            list.add(grnItem);
                            grnAdapter.notifyDataSetChanged();
                            code.setText("");
                            price.setText("");
                            qty.setText("");
                            total+=(grnItem.getP()*grnItem.getQ());
                            tvTotal.setText(String.valueOf(total));
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Error in entered values", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Error in Item Code", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!code.getText().toString().isEmpty() || !price.getText().toString().isEmpty() || !qty.getText().toString().isEmpty()) {
                    showDialog();
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetOn()){
                    dialog = ProgressDialog.show(MainActivity.this, "",
                            "Please wait", true);
                    dialog.show();
                    save();
                }else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EasyPaymentActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                code.setText(list.get(position).getC());
                price.setText(String.valueOf(list.get(position).getP()));
                qty.setText(String.valueOf(list.get(position).getQ()));
                total-=(list.get(position).getP()*list.get(position).getQ());
                tvTotal.setText(String.valueOf(total));
                list.remove(position);
                grnAdapter.notifyDataSetChanged();
            }
        });

        btnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Values.mDatabase.child("user").child("pwd").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue().toString().equals(editPwd.getText().toString())){
                            dialog = ProgressDialog.show(MainActivity.this, "",
                                    "Please wait", true);
                            dialog.show();
                            load();
                        }else {
                            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    

    public void save(){
        if(list.size()>0){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = new Date();
            parent = dateFormat.format(date);

            success=true;
            for(final GRNItem grnItem : list){

                Values.mDatabase.child("item").child(grnItem.getC()).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Item i = mutableData.getValue(Item.class);
                        if (i == null) {
                            return Transaction.success(mutableData);
                        }

                        i.setQ(i.getQ()-grnItem.getQ());

                        mutableData.setValue(i);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {

                        Values.mDatabase.child("invoice").child(parent).push().setValue(grnItem).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                success=false;
                            }
                        });

                    }
                });
            }

            if(success){
                Toast.makeText(MainActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                list.clear();
                grnAdapter.notifyDataSetChanged();
                dialog.dismiss();
                total=0;
                tvTotal.setText(String.valueOf(total));
            }

        }else {
            Toast.makeText(MainActivity.this, "List is empty", Toast.LENGTH_SHORT).show();
        }
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
                    contentLayout.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to clear this entry?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                code.setText("");
                price.setText("");
                qty.setText("");

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showDialogInternet(){
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
