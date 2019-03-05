package org.senani.posappfront;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GRNAdapter extends BaseAdapter {

    private Context mContext;

    ArrayList<GRNItem> mData;

    public GRNAdapter(Context mContext,ArrayList<GRNItem> mData) {
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

        v = View.inflate(mContext, R.layout.item_list, null);

        TextView tvCode = (TextView) v.findViewById(R.id.tv_code);
        TextView tvName = (TextView) v.findViewById(R.id.tv_name);
        TextView tvPrice = (TextView) v.findViewById(R.id.tv_price);
        TextView tvQty = (TextView) v.findViewById(R.id.tv_qty);

        tvCode.setText(mData.get(position).getC());
        tvName.setText(Values.items.get(mData.get(position).getC()).getN());
        tvPrice.setText(String.valueOf("Rs "+mData.get(position).getP()));
        tvQty.setText(String.valueOf("Qty "+mData.get(position).getQ()));

        return v;
    }
}
