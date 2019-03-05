package org.senani.posappfront;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EasyPaymentActivity extends AppCompatActivity {

    EditText code;
    EditText price;
    EditText itemcode;
    EditText code2;
    Button btnAdd;
    Button btnClose;
    EasyPayment closeInvoice;
    boolean isClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_payment);

        code= (EditText) findViewById(R.id.editcode);
        code2= (EditText) findViewById(R.id.editcode2);
        price = (EditText) findViewById(R.id.editprice);
        itemcode = (EditText) findViewById(R.id.edititemcode);
        btnAdd = (Button) findViewById(R.id.btnadd);
        btnClose = (Button) findViewById(R.id.btnclose);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetOn()) {
                    add();
                }else {
                    Toast.makeText(EasyPaymentActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetOn()) {
                    showDialog();
                }else {
                    Toast.makeText(EasyPaymentActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void add(){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        if(!code.getText().toString().isEmpty() && !price.getText().toString().isEmpty() && !itemcode.getText().toString().isEmpty()) {
            if(Values.items.containsKey(itemcode.getText().toString())) {
                try {
                    final EasyPayment easyPayment = new EasyPayment(dateFormat.format(date),
                            code.getText().toString(),
                            itemcode.getText().toString(),
                            Float.valueOf(price.getText().toString()),false);

                    Values.mDatabase.child("item").child(itemcode.getText().toString()).runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Item i = mutableData.getValue(Item.class);
                            if (i == null) {
                                return Transaction.success(mutableData);
                            }

                            i.setQ(i.getQ()-1);

                            mutableData.setValue(i);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b,
                                               DataSnapshot dataSnapshot) {

                            Values.mDatabase.child("easypayment").child(code.getText().toString()).setValue(easyPayment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EasyPaymentActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                    code.setText("");
                                    price.setText("");
                                    itemcode.setText("");
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(EasyPaymentActivity.this, "Error in entered values", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(EasyPaymentActivity.this, "Error in Item Code", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(EasyPaymentActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void close(){
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");
        final Date date = new Date();

        if(!code2.getText().toString().isEmpty()) {

                try {

                    Values.mDatabase.child("easypayment").child(code2.getText().toString()).runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            EasyPayment i = mutableData.getValue(EasyPayment.class);
                            if (i == null) {
                                return Transaction.success(mutableData);
                            }

                            closeInvoice=i;
                            isClosed=i.isIc();

                            i.setIc(true);


                            mutableData.setValue(i);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b,
                                               DataSnapshot dataSnapshot) {

                            GRNItem grnItem=new GRNItem(closeInvoice.getCi(),closeInvoice.getP(),1,dateFormat.format(date));

                            if(!isClosed) {
                                Values.mDatabase.child("invoice").child(dateFormat2.format(date)).push().setValue(grnItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EasyPaymentActivity.this, "Successfully Closed", Toast.LENGTH_SHORT).show();
                                        code2.setText("");
                                    }
                                });
                            }else {
                                Toast.makeText(EasyPaymentActivity.this, "Already Closed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(EasyPaymentActivity.this, "Error in entered values", Toast.LENGTH_SHORT).show();
                }

        }else {
            Toast.makeText(EasyPaymentActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_righ);
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to close this entry?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                close();

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

}
