package com.zebstudios.cityexpress;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by edwinhernandez on 17/12/15.
 */
public class ReservaNumeroHabitacionesAdapter extends BaseAdapter{

    private Context contextHab;
    private ArrayList<ItemListReserva> data = new ArrayList<>();

    public ReservaNumeroHabitacionesAdapter(Context contextHab, ArrayList<ItemListReserva> data){
        this.contextHab = contextHab;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View fila = convertView;
        HolderView holder = null;
        if (fila == null) {
            LayoutInflater inflater = ((Activity) contextHab).getLayoutInflater();
            fila = inflater.inflate(R.layout.reserva_list_item, parent, false);
            holder = new HolderView();
            holder.txtNumHab = (TextView) fila.findViewById(R.id.txtHabitacion);
            holder.txtPrecio = (TextView) fila.findViewById(R.id.txtNochePrecio);
            fila.setTag(holder);
        } else {
            holder = (HolderView) fila.getTag();
        }

        ItemListReserva item = data.get(position);
        holder.txtNumHab.setText(item.getHabitacion());
        holder.txtPrecio.setText(item.getPrecio());

        return fila;
    }
    public class HolderView {
        TextView txtNumHab, txtPrecio;
    }
}
