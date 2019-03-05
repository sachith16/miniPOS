package org.senani.posapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddPaymentActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    EditText dateText;
    EditText rsn;
    EditText price;
    Button btnAdd;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        dateText= (EditText) findViewById(R.id.editdate);
        rsn = (EditText) findViewById(R.id.editrsn);
        price = (EditText) findViewById(R.id.editprice);
        btnAdd = (Button) findViewById(R.id.btnadd);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        dateText.setText(dateFormat.format(date));

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.valueOf(dateText.getText().toString().substring(0,4));
                int month = Integer.valueOf(dateText.getText().toString().substring(5,7))-1;
                int day = Integer.valueOf(dateText.getText().toString().substring(8,10));

                DatePickerDialog dialog = new DatePickerDialog(
                        AddPaymentActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date;

                if(month<10) {
                    if(day<10) {
                        date = year + "-0" + month + "-0" + day;
                    }else {
                        date = year + "-0" + month + "-" + day;
                    }
                }else {
                    if(day<10) {
                        date = year + "-" + month + "-0" + day;
                    }else {
                        date = year + "-" + month + "-" + day;
                    }
                }
                dateText.setText(date);
            }
        };

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetOn()){
                    save();
                }else {
                    Toast.makeText(AddPaymentActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void save(){
        if(!rsn.getText().toString().isEmpty() && !price.getText().toString().isEmpty()) {
            dialog = ProgressDialog.show(AddPaymentActivity.this, "",
                    "Please wait", true);
            dialog.show();

            try {
                Payment payment = new Payment(dateText.getText().toString(),Float.valueOf(price.getText().toString()),rsn.getText().toString());
                Values.mDatabase.child("payment").child(dateText.getText().toString().substring(0,7)).push().setValue(payment).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                rsn.setText("");
                                price.setText("");
                                dialog.dismiss();
                                Toast.makeText(AddPaymentActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            }
                        });
            }catch (Exception e){
                dialog.dismiss();
                Toast.makeText(AddPaymentActivity.this, "Error in entered values", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(AddPaymentActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
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
}
