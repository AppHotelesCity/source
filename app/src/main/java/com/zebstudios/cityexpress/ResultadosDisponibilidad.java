package com.zebstudios.cityexpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ResultadosDisponibilidad extends Activity {

    RecyclerView listaTarjetasHotel;
    Hotel uno,dos;
    ArrayList<Hotel> listaHotel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_disponibilidad);

        listaTarjetasHotel= (RecyclerView)findViewById(R.id.cardListHoteles);
        listaTarjetasHotel.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaTarjetasHotel.setLayoutManager(llm);

        uno = new Hotel();
        uno.set_nombre("Uno");

        dos = new Hotel();
        dos.set_nombre("Dos");

        listaHotel= new ArrayList<>();
        listaHotel.add(uno);
        listaHotel.add(dos);

        HotelAdapter mAdapter = new HotelAdapter(listaHotel);

        listaTarjetasHotel.setAdapter(mAdapter);
    }
}
