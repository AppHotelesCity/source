package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
        protected Button imgYPremios;
        protected Button imgYDestinos;
        protected LinearLayout linearPremios;
        protected LinearLayout linearDestinos;
        public HabitacionViewHolder(View v) {
            super(v);
            txtDescripcionHabitacion =  (TextView) v.findViewById(R.id.textViewDescripcionHabitacion);
            txtPrecioPremioHabitacion = (TextView)  v.findViewById(R.id.textViewPrecioPremioHabitacion);
            txtPrecioDestinoHabitacion = (TextView)  v.findViewById(R.id.textViewPrecioPagoDestinoHotel);
            imgYPremios = (Button) v.findViewById(R.id.btnYPremios);
            imgYDestinos = (Button) v.findViewById(R.id.btnYDestinos);
            linearPremios = (LinearLayout) v.findViewById(R.id.linearPrecioPremio);
            linearDestinos = (LinearLayout)v.findViewById(R.id.linearPrecioDestino);
            cardViewHotel= (CardView) v.findViewById(R.id.cardViewHabitacion);
            btnReservar = (Button) v.findViewById(R.id.btnReservarAhora);
            context = v.getContext();

            imgYDestinos.setVisibility(View.GONE);
            imgYPremios.setVisibility(View.VISIBLE);

            linearDestinos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgYDestinos.setVisibility(View.VISIBLE);
                    imgYPremios.setVisibility(View.GONE);
                }
            });

            linearPremios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgYPremios.setVisibility(View.VISIBLE);
                    imgYDestinos.setVisibility(View.GONE);
                }
            });


            imgYPremios.setPivotX(imgYPremios.getWidth()/2);
            imgYPremios.setPivotY(imgYPremios.getHeight()/2);
            imgYPremios.setRotation(45);

            imgYDestinos.setPivotX(imgYDestinos.getWidth()/2);
            imgYDestinos.setPivotY(imgYDestinos.getHeight()/2);
            imgYDestinos.setRotation(45);

        }
    }

}
