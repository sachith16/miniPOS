package org.senani.posapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthlyActivity extends AppCompatActivity {

    EditText dateText;
    float monthlysale;
    float monthlyprofit;
    float monthlypayment;
    float monthlynet;
    boolean success;

    TextView tvmonthlysale;
    TextView  tvmonthlyprofit;
    TextView tvmonthlypayment;
    TextView tvmonthlynet;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        dateText= (EditText) findViewById(R.id.editDate);

        tvmonthlysale= (TextView) findViewById(R.id.tvmonthlysale);
        tvmonthlyprofit= (TextView) findViewById(R.id.tvmonthlyprofit);
        tvmonthlypayment= (TextView) findViewById(R.id.tvmonthlypayments);
        tvmonthlynet= (TextView) findViewById(R.id.tvmonthlynetprofit);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(MonthlyActivity.this, new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year, int month) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

                        dateText.setText(dateFormat.format(calendar.getTime()));
                        load();
                    }
                });
                yearMonthPickerDialog.show();
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        dateText.setText(dateFormat.format(date));
        load();
    }

    public void load(){
        monthlysale=0;
        monthlyprofit=0;
        monthlypayment=0;
        monthlynet=0;
        success=true;

        dialog = ProgressDialog.show(MonthlyActivity.this, "",
                "Calculating", true);
        dialog.show();

        Values.mDatabase.child("invoice").child(dateText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    GRNItem item = messageSnapshot.getValue(GRNItem.class);
                    monthlysale+=item.getP()*item.getQ();
                    monthlyprofit+=(item.getP()*item.getQ())-(Values.items.get(item.getC()).getP()*item.getQ());
                }

                Values.mDatabase.child("payment").child(dateText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            Payment payment = messageSnapshot.getValue(Payment.class);
                            monthlypayment+=payment.getP();
                        }

                        monthlynet=monthlyprofit-monthlypayment;

                        if(success){
                            tvmonthlysale.setText(String.valueOf(monthlysale));
                            tvmonthlyprofit.setText(String.valueOf(monthlyprofit));
                            tvmonthlypayment.setText(String.valueOf(monthlypayment));
                            tvmonthlynet.setText(String.valueOf(monthlynet));
                        }else {
                            Toast.makeText(MonthlyActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        success=false;
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                success=false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_righ);
    }
}
