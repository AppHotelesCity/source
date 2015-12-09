package com.zebstudios.cityexpress;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 07/12/15.
 */
public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder>{
    ArrayList<Hotel> arrayHoteles;

    public HotelAdapter(ArrayList<Hotel> arrayHoteles){
        this.arrayHoteles=arrayHoteles;
    }
    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tarjetaHotel= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_hoteles,parent,false);
        return new HotelViewHolder(tarjetaHotel);
    }

    @Override
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        final Hotel hotel = arrayHoteles.get(position);
        holder.txtNombreHotel.setText(hotel.getNombre());
        holder.txtDescripcionHotel.setText(hotel.getLugaresCercanos());
        holder.txtPrecioHotel.setText("$789");

        holder.cardViewHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayHoteles.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder{
    protected TextView txtNombreHotel;
    protected TextView txtDescripcionHotel;
    protected TextView txtPrecioHotel;
    protected CardView cardViewHotel;
    public HotelViewHolder(View v) {
        super(v);
        txtNombreHotel =  (TextView) v.findViewById(R.id.textViewHotel);
        txtDescripcionHotel = (TextView)  v.findViewById(R.id.textViewDescripcionHotel);
        txtPrecioHotel = (TextView)  v.findViewById(R.id.textViewPrecioHotel);
        cardViewHotel= (CardView) v.findViewById(R.id.cardViewHotel);
    }
}

}
