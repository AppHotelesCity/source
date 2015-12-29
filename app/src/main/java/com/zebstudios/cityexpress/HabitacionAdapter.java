package com.zebstudios.cityexpress;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 13/12/15.
 */
public class HabitacionAdapter extends RecyclerView.Adapter<HabitacionAdapter.HabitacionViewHolder>{
    ArrayList<Hotel> arrayHoteles;
    ArrayList<HabitacionBase> habitacionBaseArrayList;
    ArrayList<HabitacionBase> habitacionBaseArrayListCity;
    ArrayList<RoomAvailableExtra> habitacionesArrayList;
    int posicion;
    int numHabitaciones;

    public HabitacionAdapter(ArrayList<Hotel> arrayHoteles, ArrayList<HabitacionBase> arrayHabitacion, ArrayList<HabitacionBase> habitacionesCity){
        this.arrayHoteles=arrayHoteles;
        this.habitacionBaseArrayList = arrayHabitacion;
        this.habitacionBaseArrayListCity = habitacionesCity;
    }

    public HabitacionAdapter(ArrayList<Hotel> arrayHoteles, ArrayList<HabitacionBase> arrayHabitacion, ArrayList<HabitacionBase> habitacionesCity,
                             ArrayList<RoomAvailableExtra> habitacionesArrayList, int posicion){
        this.arrayHoteles=arrayHoteles;
        this.habitacionBaseArrayList = arrayHabitacion;
        this.habitacionBaseArrayListCity = habitacionesCity;
        this.habitacionesArrayList = habitacionesArrayList;
        this.posicion = posicion;
    }
    @Override
    public HabitacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tarjetaHotel= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_habitaciones,parent,false);

        return new HabitacionViewHolder(tarjetaHotel);
    }

    @Override
    public void onBindViewHolder(final HabitacionViewHolder holder, final int position) {
        //final Hotel hotel = arrayHoteles.get(0);

        final HabitacionBase habitacionBase= habitacionBaseArrayList.get(position);
        double precioAux = 0;
        String precio = "";
        System.out.println("Tamaño City dentro del adapter de Habitacion" + habitacionBaseArrayListCity.size());
        System.out.println("Tamaño Normal dentro del adapter de Habitacion" + habitacionBaseArrayList.size());
        holder.txtPrecioPremioHabitacion.setText("-");
        Picasso.with(holder.context).load(habitacionesArrayList.get(position).getImagenApp()).into(holder.imageViewHabitacion);
        for (int i = 0; i < habitacionBaseArrayListCity.size(); i++) {
                if(habitacionBaseArrayList.get(position).getCodigoBase().equalsIgnoreCase(habitacionBaseArrayListCity.get(i).getCodigoBase())){
                        holder.txtPrecioPremioHabitacion.setText("" + habitacionBaseArrayListCity.get(i).getCosto());
                }
        }
        holder.txtDescripcionHabitacion.setText(habitacionesArrayList.get(position).getDescripcion());

        holder.txtPrecioDestinoHabitacion.setText(habitacionBaseArrayList.get(position).getCosto());
        holder.cardViewHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(holder.txtPrecioPremioHabitacion.getText().toString().equalsIgnoreCase("-")){

                    if (holder.imgpremiosdestinos) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.context);
                        builder.setTitle("Selecciona el numero de habitaciones")
                                .setItems(holder.num_habitacion, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        numHabitaciones = Integer.parseInt(holder.num_habitacion[which]);
                                        
                                        Intent intent = new Intent(v.getContext(), ReservacionActivity.class);
                                        intent.putExtra("city", false);
                                        intent.putExtra("posicionHotel", posicion);
                                        intent.putExtra("posicionHabitacion", position);
                                        intent.putExtra("numHabitacion", (which+1));
                                        intent.putExtra("descipcionHabitacionJSON",habitacionesArrayList.get(position).getDescripcion());
                                        intent.putExtra("precioDestino", habitacionBaseArrayList.get(position).getCosto());
                                        intent.putExtra("codigoBase", habitacionBaseArrayList.get(position).getCodigoBase());
                                        v.getContext().startActivity(intent);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {

                    }
                }else {


                    if (holder.imgpremiosdestinos) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.context);
                        builder.setTitle("Selecciona el numero de habitaciones")
                                .setItems(holder.num_habitacion, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        numHabitaciones = Integer.parseInt(holder.num_habitacion[which]);


                                        Intent intent = new Intent(v.getContext(), ReservacionActivity.class);
                                        intent.putExtra("city", false);
                                        intent.putExtra("posicionHotel", posicion);
                                        intent.putExtra("posicionHabitacion", position);
                                        intent.putExtra("numHabitacion", (which+1));
                                        intent.putExtra("descipcionHabitacionJSON",habitacionesArrayList.get(position).getDescripcion());
                                        intent.putExtra("precioDestino", habitacionBaseArrayList.get(position).getCosto());
                                        intent.putExtra("codigoBase", habitacionBaseArrayList.get(position).getCodigoBase());
                                        v.getContext().startActivity(intent);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.context);
                        builder.setTitle("Selecciona el numero de habitaciones")
                                .setItems(holder.num_habitacion, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        numHabitaciones = Integer.parseInt(holder.num_habitacion[which]);
                                        Intent intent = new Intent(v.getContext(), ReservacionPremiosActivity.class);
                                        intent.putExtra("city", true);
                                        intent.putExtra("posicionHotel", posicion);
                                        intent.putExtra("posicionHabitacion", position);
                                        intent.putExtra("numHabitacion", (which+1));
                                        intent.putExtra("descipcionHabitacionJSON",habitacionesArrayList.get(position).getDescripcion());
                                        intent.putExtra("precioPremio", habitacionBaseArrayListCity.get(posicion).getCosto());
                                        intent.putExtra("codigoBase", habitacionBaseArrayList.get(position).getCodigoBase());
                                        v.getContext().startActivity(intent);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return habitacionesArrayList.size();
    }

    public static class HabitacionViewHolder extends RecyclerView.ViewHolder{
        protected TextView txtDescripcionHabitacion;
        protected TextView txtPrecioPremioHabitacion;
        protected TextView txtPrecioDestinoHabitacion;
        static String precioHabitacioin;
        static String precioDestino;
        protected String[] num_habitacion;
        protected CardView cardViewHotel;
        protected Button btnReservar;
        protected Context context;
        protected View imgYPremios;
        protected View imgYDestinos;
        protected boolean imgpremiosdestinos;
        protected ImageView imageViewHabitacion;
        protected LinearLayout linearPremios;
        protected LinearLayout linearDestinos;
        public HabitacionViewHolder(View v) {
            super(v);
            txtDescripcionHabitacion =  (TextView) v.findViewById(R.id.textViewDescripcionHabitacion);
            txtPrecioPremioHabitacion = (TextView)  v.findViewById(R.id.textViewPrecioPremioHabitacion);
            txtPrecioDestinoHabitacion = (TextView)  v.findViewById(R.id.textViewPrecioPagoDestinoHotel);
            imgYPremios = (View) v.findViewById(R.id.btnYPremios);
            imgYDestinos = (View) v.findViewById(R.id.btnYDestinos);
            linearPremios = (LinearLayout) v.findViewById(R.id.linearPrecioPremio);
            linearDestinos = (LinearLayout)v.findViewById(R.id.linearPrecioDestino);
            cardViewHotel= (CardView) v.findViewById(R.id.cardViewHabitacion);
            imageViewHabitacion = (ImageView) v.findViewById(R.id.imageViewHabitacion);
            btnReservar = (Button) v.findViewById(R.id.btnReservarAhora);
            context = v.getContext();

            imgYDestinos.setVisibility(View.GONE);
            imgYPremios.setVisibility(View.VISIBLE);

            linearDestinos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgYDestinos.setVisibility(View.VISIBLE);
                    imgYPremios.setVisibility(View.GONE);
                    imgpremiosdestinos = true;
                }
            });

            linearPremios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgYPremios.setVisibility(View.VISIBLE);
                    imgYDestinos.setVisibility(View.GONE);
                    imgpremiosdestinos = false;
                }
            });


            imgYPremios.setPivotX(imgYPremios.getWidth()/2);
            imgYPremios.setPivotY(imgYPremios.getHeight()/2);
            imgYPremios.setRotation(45);

            imgYDestinos.setPivotX(imgYDestinos.getWidth()/2);
            imgYDestinos.setPivotY(imgYDestinos.getHeight()/2);
            imgYDestinos.setRotation(45);

            num_habitacion= v.getResources().getStringArray(R.array.num_habitaciones);

        }
    }

}
