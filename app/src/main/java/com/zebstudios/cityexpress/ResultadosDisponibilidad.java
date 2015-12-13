package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultadosDisponibilidad extends FragmentActivity {

    RecyclerView listaTarjetasHotel;
    Hotel hotel;
    HabitacionBase habitacionBase;
    static ArrayList<Hotel> listaHotel;
    static ArrayList<Hotel> listaGeneralHotel;
    static ArrayList<HabitacionBase> habitacionBaseList;
    HotelAdapter hotelAdapter;
    Button btnListas;
    Button btnMapa;
    ImageView imageViewBack;
    private GoogleMap _map;
    private MapView _mapView;
    LatLng hotelPosition;
    Marker marker;
    String cadena;
    String text;
    int contador = 0;
    JSONArray hotelJSON;

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
        habitacionBaseList = new ArrayList<>();
        listaGeneralHotel = new ArrayList<>();
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
        contador=0;
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
            hotelJSON = hoteles;
            for (int i = 0; i < hoteles.length(); i++) {
                JSONObject nuevo = new JSONObject(hoteles.get(i).toString());
                /*System.out.println("---->"+hoteles.get(0).toString());
                System.out.println("--->>" + nuevo.getString("Imagenes"));
                System.out.println("--->>" + nuevo.getString("Hotele"));*/
                listaHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")),new JSONArray(nuevo.getString("Imagenes"))));
            }

            pedirDescripcionHotel();

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



            /*for (int i = 0; i < hoteles.length(); i++) {
                listaHotel.add(new Hotel(hoteles.get(i).toString()));
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void pedirDescripcionHotel() {
        if (contador < listaHotel.size()) {
            System.out.println("SIGLAS->"+listaHotel.get(contador).getSiglas());
            cadena = "<soapenv:Envelope\n" +
                    "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                    "    xmlns:tem=\"http://tempuri.org/\"\n" +
                    "    xmlns:cit=\"http://schemas.datacontract.org/2004/07/CityHub\">\n" +
                    "    <soapenv:Header/>\n" +
                    "    <soapenv:Body>\n" +
                    "        <tem:GetRoomsAvailablePromo>\n" +
                    "            <tem:promoRequestModelv3>\n" +
                    "                <cit:CodigoPromocion></cit:CodigoPromocion>\n" +
                    "                <cit:CodigoTarifa>1114</cit:CodigoTarifa>\n" +
                    "                <cit:FechaInicial>2015-12-16</cit:FechaInicial>\n" +
                    "                <cit:Hotel>" + listaHotel.get(contador).getSiglas() + "</cit:Hotel>\n" +
                    "                <cit:NumeroAdultos>1</cit:NumeroAdultos>\n" +
                    "                <cit:NumeroDeNoches>1</cit:NumeroDeNoches>\n" +
                    "                <cit:NumeroHabitaciones>1</cit:NumeroHabitaciones>\n" +
                    "                <cit:Segmento></cit:Segmento>\n" +
                    "                <cit:TipoHabitacion></cit:TipoHabitacion>\n" +
                    "            </tem:promoRequestModelv3>\n" +
                    "        </tem:GetRoomsAvailablePromo>\n" +
                    "    </soapenv:Body>\n" +
                    "</soapenv:Envelope>";


            StringRequest registro = new StringRequest(Request.Method.POST, "http://wshc.hotelescity.com:9742/wsMotor2014/ReservationEngine.svc", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(contador + "->contador" + response);
                    InputStream stream = null;
                    try {
                        stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                        parseXML(stream);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    obtenerDescripcionHotel(response);
                    contador++;
                    pedirDescripcionHotel();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    NetworkResponse response = error.networkResponse;
                    String datos = new String(response.data);
                    System.out.println("sout" + datos);
                }
            }) {

                public String getBodyContentType() {
                    return "text/xml; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    //params.put("Content-Type", "application/xml; charset=utf-8");
                    params.put("SOAPAction", "http://tempuri.org/IReservationEngine/GetRoomsAvailablePromo");
                    Log.d("hsdhsdfhuidiuhsd", "clave");
                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return cadena.toString().getBytes();
                }

            };
            System.out.println("registro->" + registro.toString());
            registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(registro);
        }else{
            hotelAdapter = new HotelAdapter(listaGeneralHotel);
            listaTarjetasHotel.setAdapter(hotelAdapter);
        }
    }

    public void obtenerDescripcionHotel(String hotel){

    }

    public void parseXML(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        //List<String> listaBase = null;
        try {
            //listaBase = new ArrayList<>();
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("Available")) {
                            // create a new instance of employee
                            //employee = new Disponibilidad();
                        } else if(tagname.equalsIgnoreCase("Disponibilidad")){
                            habitacionBaseList = new ArrayList<>();
                        } else if(tagname.equalsIgnoreCase("HabBase")){
                            habitacionBase = new HabitacionBase();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Available")) {
                            // add employee object to list
                           // employee.setHabitacionBasesList(habitacionBasesList);
                        }else if(tagname.equalsIgnoreCase("Disponibilidad")){
                            JSONObject nuevo = new JSONObject(hotelJSON.get(contador).toString());
                            listaGeneralHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")), new JSONArray(nuevo.getString("Imagenes")),habitacionBaseList));
                            System.out.println("TOTAL->" + listaGeneralHotel.size());
                            habitacionBaseList = new ArrayList<>();
                        } else if(tagname.equalsIgnoreCase("HabBase")){
                            habitacionBaseList.add(habitacionBase);
                        }else if (tagname.equalsIgnoreCase("Descripcion")) {
                           // employee.setName(text);
                            System.out.println(text);
                        } else if (tagname.equalsIgnoreCase("CodigoTarifa")) {
                            //employee.setId(text);
                            System.out.println(text);
                        } else if (tagname.equalsIgnoreCase("Hotel")) {
                            //employee.setDepartment(text);
                            System.out.println(text);
                        } else if (tagname.equalsIgnoreCase("AVAILABILITY")) {
                            habitacionBase.setDisponibilidad(text);
                            System.out.println(text);
                        } else if (tagname.equalsIgnoreCase("CodBase")) {
                            habitacionBase.setCodigoBase(text);
                            System.out.println(text);
                        } else if (tagname.equalsIgnoreCase("DescBase")) {
                            habitacionBase.setDescBase(text);
                            System.out.println(text);
                        } else if (tagname.equalsIgnoreCase("Costo")) {
                            habitacionBase.setCosto(text);
                            System.out.println(text);
                        } else if (tagname.equalsIgnoreCase("Fecha")) {
                            habitacionBase.setFecha(text);
                            System.out.println(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return habitacionBaseList;
    }

}
