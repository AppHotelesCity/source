package com.zebstudios.cityexpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultadosDisponibilidad extends Activity {

    RecyclerView listaTarjetasHotel;
    Hotel uno,dos;
    ArrayList<Hotel> listaHotel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_disponibilidad);
        Bundle bundle = getIntent().getExtras();
        buscarDisponibilidad(bundle.getString("busqueda"));

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
    public void buscarDisponibilidad(String busqueda){
        System.out.println("->"+busqueda);

        StringRequest registro = new StringRequest(Request.Method.GET,"https://www.cityexpress.com/umbraco/api/MobileAppServices/GetHotelsWithServices?SearchTerms="+busqueda, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    obtenerHoteles(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
    }

    public void obtenerHoteles(JSONArray hoteles){
        try{
            for (int i = 0; i < hoteles.length(); i++) {
                    System.out.println(hoteles.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
