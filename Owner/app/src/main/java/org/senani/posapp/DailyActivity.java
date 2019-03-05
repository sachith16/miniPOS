package org.senani.posapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    EditText dateText;

    ListView listView;
    ArrayList<GRNItem> list;
    GRNAdapter grnAdapter;
    TextView tvTotal;
    float total;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        dateText= (EditText) findViewById(R.id.editDate);
        tvTotal= (TextView) findViewById(R.id.tv_total);
        listView = (ListView) findViewById(R.id.list_view);
        list=new ArrayList<>();
        total=0;
        grnAdapter= new GRNAdapter(getApplicationContext(),list);
        listView.setAdapter(grnAdapter);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        dateText.setText(dateFormat.format(date));
        load();

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.valueOf(dateText.getText().toString().substring(0,4));
                int month = Integer.valueOf(dateText.getText().toString().substring(5,7))-1;
                int day = Integer.valueOf(dateText.getText().toString().substring(8,10));

                DatePickerDialog dialog = new DatePickerDialog(
                        DailyActivity.this,
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
                load();
            }
        };
    }

    public void load(){
        list.clear();

        dialog = ProgressDialog.show(DailyActivity.this, "",
                "Please wait", true);
        dialog.show();

        Values.mDatabase.child("invoice").child(dateText.getText().toString().substring(0,7)).orderByChild("d").equalTo(dateText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total=0;

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    GRNItem item = messageSnapshot.getValue(GRNItem.class);
                    total+=item.getQ()*item.getP();
                    list.add(item);

                }
                grnAdapter.notifyDataSetChanged();
                tvTotal.setText(String.valueOf(total));
                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                grnAdapter.notifyDataSetChanged();
                dialog.dismiss();
                tvTotal.setText("");
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_righ);
    }

}
