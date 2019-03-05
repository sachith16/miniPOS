package org.senani.posapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ToOrderActivity extends AppCompatActivity {

    ListView listView;
    ToOrderAdapter toOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_order);

        listView = (ListView) findViewById(R.id.list_view);
        toOrderAdapter= new ToOrderAdapter(getApplicationContext());
        listView.setAdapter(toOrderAdapter);
        toOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_righ);
    }
}
