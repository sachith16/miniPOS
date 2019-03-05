package org.senani.posapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class UpdateItemActivity extends AppCompatActivity {

    EditText code;
    EditText name;
    EditText limit;
    Button btnAdd;
    ImageButton btnSearch;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        code= (EditText) findViewById(R.id.editcode);
        name = (EditText) findViewById(R.id.editname);
        limit = (EditText) findViewById(R.id.editlimit);
        btnAdd = (Button) findViewById(R.id.btnadd);
        btnSearch = (ImageButton) findViewById(R.id.btnsearch);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetOn()){
                    update();
                }else {
                    Toast.makeText(UpdateItemActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Values.items.containsKey(code.getText().toString())){
                    name.setText(Values.items.get(code.getText().toString()).getN());
                    limit.setText(String.valueOf(Values.items.get(code.getText().toString()).getL()));
                }else {
                    Toast.makeText(UpdateItemActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                }
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

    public void update(){
        if(!code.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !limit.getText().toString().isEmpty()) {
            if(Values.items.containsKey(code.getText().toString())) {

                dialog = ProgressDialog.show(UpdateItemActivity.this, "",
                        "Please wait", true);
                dialog.show();

                Values.mDatabase.child("item").child(code.getText().toString()).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Item i = mutableData.getValue(Item.class);
                        if (i == null) {
                            return Transaction.success(mutableData);
                        }

                        i.setN(name.getText().toString());
                        try {
                            i.setL(Float.valueOf(limit.getText().toString()));
                        } catch (Exception e) {
                            dialog.dismiss();
                            Toast.makeText(UpdateItemActivity.this, "Error in entered values", Toast.LENGTH_SHORT).show();
                        }

                        // Set value and report transaction success
                        mutableData.setValue(i);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        dialog.dismiss();
                        Toast.makeText(UpdateItemActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        code.setText("");
                        name.setText("");
                        limit.setText("");
                    }
                });
            }else {
                Toast.makeText(UpdateItemActivity.this, "Code doesn't available", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(UpdateItemActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
    }
}
