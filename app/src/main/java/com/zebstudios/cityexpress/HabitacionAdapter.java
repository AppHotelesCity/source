package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 13/12/15.
 */
public class HabitacionAdapter extends RecyclerView.Adapter<HabitacionAdapter.HabitacionViewHolder>{
    ArrayList<Hotel> arrayHoteles;
    ArrayList<HabitacionBase> habitacionBaseArrayList;
    ArrayList<HabitacionBase> habitacionBaseArrayListCity;

    public HabitacionAdapter(ArrayList<Hotel> arrayHoteles, ArrayList<HabitacionBase> arrayHabitacion, ArrayList<HabitacionBase> habitacionesCity){
        this.arrayHoteles=arrayHoteles;
        this.habitacionBaseArrayList = arrayHabitacion;
        this.habitacionBaseArrayListCity = habitacionesCity;
    }
    @Override
    public HabitacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tarjetaHotel= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_habitaciones,parent,false);
        return new HabitacionViewHolder(tarjetaHotel);
    }

    @Override
    public void onBindViewHolder(final HabitacionViewHolder holder, int position) {
        final Hotel hotel = arrayHoteles.get(position);
        final HabitacionBase habitacionBase= habitacionBaseArrayList.get(position);
        double precioAux = 0;
        String precio = "";
        System.out.println("Tamaño dentro del adapter de Habitacion" + habitacionBaseArrayListCity.size());
        holder.txtPrecioPremioHabitacion.setText("-");
        for (int i = 0; i < habitacionBaseArrayListCity.size(); i++) {
                if(habitacionBaseArrayList.get(position).getCodigoBase().equalsIgnoreCase(habitacionBaseArrayListCity.get(i).getCodigoBase())){
                        holder.txtPrecioPremioHabitacion.setText("" + habitacionBaseArrayListCity.get(i).getCosto());
                }
        }
        holder.txtDescripcionHabitacion.setText(habitacionBaseArrayList.get(position).getDescBase());

        holder.txtPrecioDestinoHabitacion.setText(habitacionBaseArrayList.get(position).getCosto());
        holder.cardViewHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReservacionActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habitacionBaseArrayList.size();
    }

    public static class HabitacionViewHolder extends RecyclerView.ViewHolder{
        protected TextView txtDescripcionHabitacion;
        protected TextView txtPrecioPremioHabitacion;
        protected TextView txtPrecioDestinoHabitacion;
        protected CardView cardViewHotel;
        protected Button btnReservar;
        protected Context context;
        public HabitacionViewHolder(View v) {
            super(v);
            txtDescripcionHabitacion =  (TextView) v.findViewById(R.id.textViewDescripcionHabitacion);
            txtPrecioPremioHabitacion = (TextView)  v.findViewById(R.id.textViewPrecioPremioHabitacion);
            txtPrecioDestinoHabitacion = (TextView)  v.findViewById(R.id.textViewPrecioPagoDestinoHotel);
            cardViewHotel= (CardView) v.findViewById(R.id.cardViewHabitacion);
            btnReservar = (Button) v.findViewById(R.id.btnReservarAhora);
            context = v.getContext();
        }
    }

}
