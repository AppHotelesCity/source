package com.zebstudios.cityexpress;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ListadoHotelesFragment extends Fragment {

    /*RecyclerView listaTarjetasHotel;
    Hotel uno,dos;
    ArrayList<Hotel> listaHotel;*/

    public static ListadoHotelesFragment newInstance(String param1, String param2) {
        ListadoHotelesFragment fragment = new ListadoHotelesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ListadoHotelesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listado_hoteles, container, false);
      /*  System.out.println("Entre al fragment");

        listaTarjetasHotel= (RecyclerView)view.findViewById(R.id.cardListHoteles);
        listaTarjetasHotel.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaTarjetasHotel.setLayoutManager(llm);

       /* uno = new Hotel();
        uno.set_nombre("Uno");

        dos = new Hotel();
        dos.set_nombre("Dos");

        listaHotel= new ArrayList<>();
        listaHotel.add(uno);
        listaHotel.add(dos);
        listaHotel.add(uno);
        listaHotel.add(dos);

        HotelAdapter mAdapter = new HotelAdapter(ResultadosDisponibilidad.listaHotel);

        listaTarjetasHotel.setAdapter(mAdapter);*/

        return view;
    }


}
