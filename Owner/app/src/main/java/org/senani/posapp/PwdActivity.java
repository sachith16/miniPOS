package org.senani.posapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

public class PwdActivity extends AppCompatActivity {

    EditText pwd;
    Button btnAdd;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        pwd = (EditText) findViewById(R.id.editpwd);
        btnAdd = (Button) findViewById(R.id.btnadd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pwd.getText().toString().isEmpty()){
                    if(isInternetOn()){
                        add();
                    }else {
                        Toast.makeText(PwdActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PwdActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void add(){
        dialog = ProgressDialog.show(PwdActivity.this, "",
                "Please wait", true);
        dialog.show();

        Values.mDatabase.child("user").child("pwd").setValue(pwd.getText().toString()).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pwd.setText("");
                        dialog.dismiss();
                        dialog.dismiss();
                        Toast.makeText(PwdActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    }
                });
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
