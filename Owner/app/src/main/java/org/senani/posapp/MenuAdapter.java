package org.senani.posapp;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter {

    private Context mContext;

    ArrayList<String> mData;

    public MenuAdapter(Context mContext) {
        this.mContext = mContext;
        mData=new ArrayList<>();
        mData.add("Good Receive Note");
        mData.add("Daily Balance");
        mData.add("Monthly Balance");
        mData.add("Add Payment");
        mData.add("View Payment");
        mData.add("Add Item");
        mData.add("Update Item");
        mData.add("View Item");
        mData.add("To-Order Item");
        mData.add("Password");
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;

        v = View.inflate(mContext, R.layout.item_list3, null);

        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
        ImageView ic = (ImageView) v.findViewById(R.id.ic_title);
        tvTitle.setText(mData.get(position));

        switch (position){
            case 0:
                ic.setImageResource(R.mipmap.ic_grn);
                break;
            case 1:
                ic.setImageResource(R.mipmap.ic_daily);
                break;
            case 2:
                ic.setImageResource(R.mipmap.ic_monthly);
                break;
            case 3:
                ic.setImageResource(R.mipmap.ic_addpay);
                break;
            case 4:
                ic.setImageResource(R.mipmap.ic_viewpay);
                break;
            case 5:
                ic.setImageResource(R.mipmap.ic_additem);
                break;
            case 6:
                ic.setImageResource(R.mipmap.ic_updateitem);
                break;
            case 7:
                ic.setImageResource(R.mipmap.ic_viewitem);
                break;
            case 8:
                ic.setImageResource(R.mipmap.ic_toorder);
                break;
            case 9:
                ic.setImageResource(R.mipmap.ic_pwd);
                break;
        }

        return v;
    }
}
