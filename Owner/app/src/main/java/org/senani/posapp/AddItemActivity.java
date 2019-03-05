package org.senani.posapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;


public class AddItemActivity extends AppCompatActivity {

    EditText code;
    EditText name;
    EditText limit;
    Button btnAdd;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        code= (EditText) findViewById(R.id.editcode);
        name = (EditText) findViewById(R.id.editname);
        limit = (EditText) findViewById(R.id.editlimit);
        btnAdd = (Button) findViewById(R.id.btnadd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetOn()){
                    add();
                }else {
                    Toast.makeText(AddItemActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

    public void add(){
        if(!code.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !limit.getText().toString().isEmpty()) {
            if(!Values.items.containsKey(code.getText().toString())) {

                dialog = ProgressDialog.show(AddItemActivity.this, "",
                        "Please wait", true);
                dialog.show();

                try {
                    Item item = new Item(code.getText().toString(), Float.valueOf(limit.getText().toString()), name.getText().toString(), 0, 0);

                    Values.mDatabase.child("item").child(code.getText().toString()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            code.setText("");
                            name.setText("");
                            limit.setText("");
                            dialog.dismiss();
                            Toast.makeText(AddItemActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(AddItemActivity.this, "Error in entered values", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(AddItemActivity.this, "Try another code", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(AddItemActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
    }
}
