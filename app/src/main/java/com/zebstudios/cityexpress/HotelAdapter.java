package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 07/12/15.
 */
public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder>{
    ArrayList<Hotel> arrayHoteles;
    ArrayList<HabitacionBase> habitacionBaseArrayList;

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
    public void onBindViewHolder(final HotelViewHolder holder, int position) {
        final Hotel hotel = arrayHoteles.get(position);
        double precioAux = 0;
        String precio = "";
        holder.txtNombreHotel.setText(hotel.getNombre());
        holder.txtDescripcionHotel.setText(hotel.getLugaresCercanos());
        habitacionBaseArrayList = hotel.getArrayHabitaciones();
        Picasso.with(holder.context).load(hotel.getImagenPrincipal()).into(holder.imageViewHotel);
        System.out.println(habitacionBaseArrayList.size() + "hotelNombre>>**->" + hotel.getNombre());
        for (int i = 0; i < habitacionBaseArrayList.size(); i++) {
            if(precioAux<Double.parseDouble(habitacionBaseArrayList.get(i).getCosto().replace(",",""))){
                precioAux = Double.parseDouble(habitacionBaseArrayList.get(i).getCosto().replace(",",""));
                precio = habitacionBaseArrayList.get(i).getCosto();
            }else{
                precioAux = Double.parseDouble(habitacionBaseArrayList.get(i).getCosto().replace(",",""));
                precio = habitacionBaseArrayList.get(i).getCosto();
            }
            System.out.println("habitacioneDescBase->" + habitacionBaseArrayList.get(i).getDescBase());
            System.out.println("habitacionesCosto->" + habitacionBaseArrayList.get(i).getCosto());
        }
        holder.txtPrecioHotel.setText("$ "+precio);

        holder.cardViewHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(holder.context,DetalleHotelActivity.class);
                holder.context.startActivity(intent);*/

                Intent dialog = new Intent(holder.context, DetalleHotelActivity.class);
                //dialog.putExtra("HOTEL", hotel);
                holder.context.startActivity(dialog);
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
        protected ImageView imageViewHotel;
        protected Context context;
    public HotelViewHolder(View v) {
        super(v);
        txtNombreHotel =  (TextView) v.findViewById(R.id.textViewHotel);
        txtDescripcionHotel = (TextView)  v.findViewById(R.id.textViewDescripcionHotel);
        txtPrecioHotel = (TextView)  v.findViewById(R.id.textViewPrecioHotel);
        cardViewHotel= (CardView) v.findViewById(R.id.cardViewHotel);
        imageViewHotel = (ImageView) v.findViewById(R.id.imageViewHotel);
        context = v.getContext();

    }
}

}
