package com.zebstudios.cityexpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultadosDisponibilidad extends FragmentActivity {

    RecyclerView listaTarjetasHotel;
    Hotel uno,dos;
    static ArrayList<Hotel> listaHotel;
    HotelAdapter hotelAdapter;
    Button btnListas;
    Button btnMapa;
    ImageView imageViewBack;
    private GoogleMap _map;
    private MapView _mapView;
    LatLng hotelPosition;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_disponibilidad);
        btnListas = (Button) findViewById(R.id.btnLista);
        btnMapa = (Button) findViewById(R.id.btnMapa);

        imageViewBack = (ImageView) findViewById(R.id.back_button);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listaHotel = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();

        listaTarjetasHotel= (RecyclerView)findViewById(R.id.cardListHoteles);
        listaTarjetasHotel.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaTarjetasHotel.setLayoutManager(llm);
        buscarDisponibilidad(bundle.getString("busqueda"));

        _mapView = (MapView) findViewById( R.id.mapView );
        _mapView.onCreate(savedInstanceState);
        _mapView.onResume();

        try
        {
            MapsInitializer.initialize(getBaseContext());
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        _map = _mapView.getMap();

        /*if (findViewById(R.id.fragment_contenedor) != null) {

            if (savedInstanceState != null) {
                return;
            }

            ListadoHotelesFragment listadoHotelesFragment = new ListadoHotelesFragment();

            listadoHotelesFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_contenedor, listadoHotelesFragment).commit();
        }*/

        btnListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mapView.setVisibility(View.GONE);
                listaTarjetasHotel.setVisibility(View.VISIBLE);
                /*ListadoHotelesFragment listadoHotelesFragment = new ListadoHotelesFragment();
                listadoHotelesFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_contenedor, listadoHotelesFragment).commit();*/
            }
        });
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 _mapView.setVisibility(View.VISIBLE);
                listaTarjetasHotel.setVisibility(View.GONE);

               /* ListadoHotelesFragment menuPrincipal = new ListadoHotelesFragment();
                menuPrincipal.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_contenedor, menuPrincipal).commit();*/
            }
        });

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
                JSONObject nuevo = new JSONObject(hoteles.get(i).toString());
                /*System.out.println("---->"+hoteles.get(0).toString());
                System.out.println("--->>" + nuevo.getString("Imagenes"));
                System.out.println("--->>" + nuevo.getString("Hotele"));*/
                listaHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")),new JSONArray(nuevo.getString("Imagenes"))));
            }
            hotelAdapter = new HotelAdapter(listaHotel);

            listaTarjetasHotel.setAdapter(hotelAdapter);


            if( _map != null )
            {
                for (int i = 0; i < listaHotel.size(); i++) {
                    hotelPosition = new LatLng(listaHotel.get(i).getLatitude(), listaHotel.get(i).getLongitude() );
                    marker = _map.addMarker( new MarkerOptions().position( hotelPosition ).title( listaHotel.get(i).getNombre() ) );
                }
                _map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                    }
                });
                _map.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelPosition, 10));
                _map.getUiSettings().setMapToolbarEnabled(false);
            }


/*            for (int i = 0; i < hoteles.length(); i++) {
                listaHotel.add(new Hotel(hoteles.get(i).toString()));
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
