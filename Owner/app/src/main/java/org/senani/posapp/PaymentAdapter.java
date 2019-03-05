package org.senani.posapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class PaymentAdapter extends BaseAdapter {

    private Context mContext;

    ArrayList<Payment> mData;

    public PaymentAdapter(Context mContext,ArrayList<Payment> mData) {
        this.mContext = mContext;
        this.mData=mData;
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

        v = View.inflate(mContext, R.layout.item_list2, null);

        TextView tvRsn = (TextView) v.findViewById(R.id.tv_rsn);
        TextView tvPrice = (TextView) v.findViewById(R.id.tv_price);

        tvRsn.setText(mData.get(position).getR());
        tvPrice.setText("Rs "+mData.get(position).getP());

        return v;
    }
}
