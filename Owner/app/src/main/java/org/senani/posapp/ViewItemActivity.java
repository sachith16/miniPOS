package org.senani.posapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewItemActivity extends AppCompatActivity {

    ListView listView;
    ViewItemAdapter viewItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        listView = (ListView) findViewById(R.id.list_view);
        viewItemAdapter= new ViewItemAdapter(getApplicationContext());
        listView.setAdapter(viewItemAdapter);
        viewItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_righ);
    }

}
