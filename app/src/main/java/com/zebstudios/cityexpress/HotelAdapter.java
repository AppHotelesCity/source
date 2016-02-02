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
    ArrayList<HabitacionBase> habitacionCityArrayList;
    double precioAux = 0;

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
    public void onBindViewHolder(final HotelViewHolder holder, final int position) {
        final Hotel hotel = arrayHoteles.get(position);


        System.out.println("--->" + hotel.getImagenPrincipal());
        if(hotel.getImagenPrincipal().equalsIgnoreCase("")){

        }else{
            String precio = "";
            String precio2 = "";
            holder.txtNombreHotel.setText(hotel.getNombre());
            holder.txtDescripcionHotel.setText(hotel.getLugaresCercanos());
            habitacionBaseArrayList = hotel.getArrayHabitaciones();
            habitacionCityArrayList = hotel.getArrayHabitacionesCity();
            System.out.println(habitacionCityArrayList.size() + "hotelNombre>>**->" + hotel.getNombre());
            if(habitacionCityArrayList.size()>0){
                for (int i = 0; i < habitacionCityArrayList.size(); i++) {

                    if(habitacionCityArrayList.get(i).getCosto().equalsIgnoreCase("")||habitacionCityArrayList.get(i).getSubTotal().equalsIgnoreCase("")) {
                    }else{
                        if (precioAux < Double.parseDouble(habitacionCityArrayList.get(i).getSubTotal().replace(",", ""))) { //getCosto()
                            precioAux = Double.parseDouble(habitacionCityArrayList.get(i).getSubTotal().replace(",", "")); //getCosto()
                            precio = habitacionCityArrayList.get(i).getSubTotal(); //getCosto
                            System.out.println("Precio1"+precio);
                        } else{
                            precioAux = Double.parseDouble(habitacionCityArrayList.get(i).getSubTotal().replace(",", "")); //getCosto()
                            precio2 = habitacionCityArrayList.get(i).getSubTotal(); //getCosto()
                            System.out.println("Precio2"+precio2);
                        }
                    }
                    System.out.println("habitacionesCosto->" + habitacionCityArrayList.get(i).getSubTotal()); //getCosto
                }
                if(precio2.equalsIgnoreCase("")){
                    holder.txtPrecioHotel.setText(String.format("$%,.0f ", Double.parseDouble(habitacionCityArrayList.get(0).getSubTotal().replace(",", "")))); //getCosto
                }else{
                    holder.txtPrecioHotel.setText(String.format("$%,.0f ",Double.parseDouble(precio2.replace(",", ""))));
                }
            }

            for (int i = 0; i < habitacionBaseArrayList.size(); i++) {

                if(habitacionBaseArrayList.get(i).getCosto().equalsIgnoreCase("")||habitacionBaseArrayList.get(i).getSubTotal().equalsIgnoreCase("")) {
                }else{
                    if (precioAux < Double.parseDouble(habitacionBaseArrayList.get(i).getSubTotal().replace(",", ""))) { //getCosto()
                        precioAux = Double.parseDouble(habitacionBaseArrayList.get(i).getSubTotal().replace(",", "")); //getCosto()
                        precio = habitacionBaseArrayList.get(i).getSubTotal(); //getCosto
                        System.out.println("Precio1"+precio);
                    } else{
                        precioAux = Double.parseDouble(habitacionBaseArrayList.get(i).getSubTotal().replace(",", "")); //getCosto()
                        precio2 = habitacionBaseArrayList.get(i).getSubTotal(); //getCosto()
                        System.out.println("Precio2"+precio2);
                    }
                }
                System.out.println("habitacionesCosto->" + habitacionBaseArrayList.get(i).getSubTotal()); //getCosto
            }
            if(precio2.equalsIgnoreCase("")){
                holder.txtPrecioHotel.setText(String.format("$%,.0f ", Double.parseDouble(habitacionBaseArrayList.get(0).getSubTotal().replace(",", "")))); //getCosto
            }else{
                holder.txtPrecioHotel.setText(String.format("$%,.0f ",Double.parseDouble(precio2.replace(",", ""))));
            }

            holder.cardViewHotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.context, DetalleHotelActivity.class);
                    intent.putExtra("posicion", position);
                    holder.context.startActivity(intent);
                    System.out.println("POSICION"+position);
                }
            });
            Picasso.with(holder.context).load(hotel.get_imagenes()[0]).into(holder.imageViewHotel);
        }

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
