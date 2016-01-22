package com.zebstudios.cityexpress;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 21/01/16.
 */
public class FolioSaldoAdapter extends BaseAdapter {
    ArrayList<Folio> folioArrayList;
    Activity activity;

    public FolioSaldoAdapter(ArrayList<Folio> folioArrayList, Activity activity) {
        this.folioArrayList = folioArrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return folioArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return folioArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        TextView textViewFolio;
        TextView textViewBalance;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final Folio saldo = folioArrayList.get(position);
        convertView = layoutInflater.inflate(R.layout.list_view_folio,
                parent,false);

        viewHolder.textViewFolio = (TextView)
                convertView.findViewById(R.id.textViewFolio);
        viewHolder.textViewBalance = (TextView)
                convertView.findViewById(R.id.textViewBalance);

        viewHolder.textViewFolio.setText("Folio : "+saldo.getFolio());
        viewHolder.textViewBalance.setText("Balance: $"+saldo.getBalance());

        return convertView;
    }
}
