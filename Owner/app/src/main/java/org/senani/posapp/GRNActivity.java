package org.senani.posapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GRNActivity extends AppCompatActivity {

    EditText code;
    EditText price;
    EditText qty;
    Button btnSave;
    ImageButton btnClear;
    ImageButton btnNext;
    ListView listView;
    GRNAdapter grnAdapter;
    ArrayList<GRNItem> list;
    public boolean success;
    String parent;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn);

        code= (EditText) findViewById(R.id.editcode);
        price = (EditText) findViewById(R.id.editprice);
        qty = (EditText) findViewById(R.id.editqty);
        btnSave = (Button) findViewById(R.id.btnadd);
        btnClear = (ImageButton) findViewById(R.id.btnremove);
        btnNext = (ImageButton) findViewById(R.id.btnnext);

        listView = (ListView) findViewById(R.id.list_view);
        list=new ArrayList<>();
        grnAdapter= new GRNAdapter(getApplicationContext(),list);
        listView.setAdapter(grnAdapter);

        code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(Values.items.containsKey(code.getText().toString())){
                    Toast.makeText(GRNActivity.this, Values.items.get(code.getText().toString()).getN(), Toast.LENGTH_SHORT).show();
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
                            GRNItem grnItem = new GRNItem(code.getText().toString(), dateFormat.format(date), Float.valueOf(price.getText().toString()), Float.valueOf(qty.getText().toString()));
                            list.add(grnItem);
                            grnAdapter.notifyDataSetChanged();
                            code.setText("");
                            price.setText("");
                            qty.setText("");
                        } catch (Exception e) {
                            Toast.makeText(GRNActivity.this, "Error in entered values", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(GRNActivity.this, "Error in Item Code", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(GRNActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
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
                    save();
                }else {
                    Toast.makeText(GRNActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                code.setText(list.get(position).getC());
                price.setText(String.valueOf(list.get(position).getP()));
                qty.setText(String.valueOf(list.get(position).getQ()));
                list.remove(position);
                grnAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_righ);
    }

    public void save(){
        if(list.size()>0){
            dialog = ProgressDialog.show(GRNActivity.this, "",
                    "Please wait", true);
            dialog.show();

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

                        i.setP(grnItem.getP());
                        i.setQ(i.getQ()+grnItem.getQ());

                        mutableData.setValue(i);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        Values.mDatabase.child("grn").child(parent).push().setValue(grnItem).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                success=false;
                            }
                        });

                    }

                });
            }

            dialog.dismiss();

            if(success){
                Toast.makeText(GRNActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                list.clear();
                grnAdapter.notifyDataSetChanged();
            }

        }else {
            Toast.makeText(GRNActivity.this, "List is empty", Toast.LENGTH_SHORT).show();
        }
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

}
