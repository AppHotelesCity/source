package com.zebstudios.cityexpress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zebstudios.calendar.CalendarFragment;
import com.zebstudios.calendar.CalendarListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultadosDisponibilidad extends ActionBarActivity {

    ExtensionRecyclerView listaTarjetasHotel;
    Hotel hotel;
    HabitacionBase habitacionBase;
    HabitacionBase habitacionCity;
    static ArrayList<Hotel> listaHotel;
    static ArrayList<Hotel> listaGeneralHotel;
    static ArrayList<HabitacionBase> habitacionBaseList;
    static ArrayList<HabitacionBase> habitacionCityPremiosList;
    TextView textViewFechaLlegada;
    TextView textViewFechaSalida;
    static int numeroNoches;
    HotelAdapter hotelAdapter;
    Button btnListas;
    Button btnMapa;
    ImageView imageViewBack;
    ImageView imageBusqueda;
    String fechaPartida;

    static ProgressDialog progress;

    private GoogleMap _map;
    private MapView _mapView;
    LatLng hotelPosition;
    Marker marker;
    String cadena;
    String text;
    int contador = 0;
    static int totalNoches;
    JSONArray hotelJSON;
    LinearLayout lineaDisponibilidad;

    EditText edtxtHotelDestino;
    TextView txt_salida;
    TextView txt_llegada;
    Button btndisponibilidad;


    static Date _arrivalDate;
    private CalendarFragment _arrivalCalendarFragment;
    private CalendarListener _arrivalCalendarListener;
    static Date _departureDate;
    private CalendarFragment _departureCalendarFragment;
    private CalendarListener _departureCalendarListener;


    RelativeLayout linearAzul;
    LinearLayout linearBotones;
    boolean estadobton = false;
    boolean cityPremios =false;
    final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_disponibilidad);

        edtxtHotelDestino = (EditText) findViewById(R.id.edtxtHotelDestinoResultados);
        txt_salida = (TextView) findViewById(R.id.txtSalida);
        txt_llegada = (TextView) findViewById(R.id.txtLlegada);
        btndisponibilidad = (Button) findViewById(R.id.btnDisponibilidadResultados);
        btnListas = (Button) findViewById(R.id.btnLista);
        btnMapa = (Button) findViewById(R.id.btnMapa);
        listaTarjetasHotel= (ExtensionRecyclerView)findViewById(R.id.cardListHoteles);
        listaTarjetasHotel.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaTarjetasHotel.setLayoutManager(llm);
        lineaDisponibilidad = (LinearLayout)findViewById(R.id.linearBuscarDisponibilidad);
        imageViewBack = (ImageView) findViewById(R.id.back_button);
        imageBusqueda = (ImageView) findViewById(R.id.imgViewMostrarOcultarLinear);
        linearAzul = (RelativeLayout) findViewById(R.id.linearblue);
        linearBotones = (LinearLayout)findViewById(R.id.linearbtns);
        textViewFechaLlegada = (TextView)findViewById(R.id.textViewFechaLlegada);
        textViewFechaSalida = (TextView)findViewById(R.id.textViewFechaSalida);


        PrepareArrivalCalendar();
        PrepareDepartureCalendar();


        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR); //obtenemos el año
        int mes = c.get(Calendar.MONTH); //obtenemos el mes
        String mesT = Integer.toString(mes);
        int dia = c.get(Calendar.DAY_OF_MONTH); // obtemos el día.
        switch (mesT){
            case "0":
                mesT = "ene";
                break;
            case "1":
                mesT = "feb";
                break;
            case "2":
                mesT = "mar";
                break;
            case "3":
                mesT = "abr";
                break;
            case "4":
                mesT = "may";
                break;
            case "5":
                mesT = "jun";
                break;
            case "6":
                mesT = "jul";
                break;
            case "7":
                mesT = "ago";
                break;
            case "8":
                mesT = "sep";
                break;
            case "9":
                mesT = "oct";
                break;
            case "10":
                mesT = "nov";
                break;
            case "11":
                mesT = "dic";
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
        _arrivalDate = c.getTime();
        txt_salida.setText(sdf.format(_arrivalDate));

        _arrivalDate.getTime();
        System.out.println("Arrival->"+ _arrivalDate.getTime());
        Calendar cs = Calendar.getInstance();
        cs.setTime(_arrivalDate);
        cs.add(Calendar.DATE, 1);

        _departureDate = cs.getTime();

        System.out.println("Salida->" + sdf.format(_departureDate));;
        txt_llegada.setText(sdf.format(_departureDate));


        /*txt_salida.setText(dia+" "+mesT+" "+anio);
        txt_llegada.setText(dia+" "+mesT+" "+anio);*/


        btndisponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(edtxtHotelDestino.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResultadosDisponibilidad.this);
                    builder.setTitle("City Express")
                            .setMessage("El campo Destino es obligatorio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                 Intent intent = new Intent(ResultadosDisponibilidad.this, ResultadosDisponibilidad.class);
                    intent.putExtra("busqueda",edtxtHotelDestino.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });




        SimpleDateFormat sdfecha = new SimpleDateFormat( "yyyy-MM-dd" );
        fechaPartida = sdfecha.format(PrincipalFragment._departureDate);
        textViewFechaLlegada.setText("Entrada "+sdf.format(PrincipalFragment._arrivalDate));
        textViewFechaSalida.setText("Salida " + sdf.format(PrincipalFragment._departureDate));
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        totalNoches =Integer.parseInt(""+(PrincipalFragment._arrivalDate.getTime() - PrincipalFragment._departureDate.getTime())/MILLSECS_PER_DAY);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            listaHotel = new ArrayList<>();
            habitacionBaseList = new ArrayList<>();
            listaGeneralHotel = new ArrayList<>();
            habitacionCityPremiosList = new ArrayList<>();
            progress = ProgressDialog.show(ResultadosDisponibilidad.this, "Cargando...",
                    "Espere por favor", true);
            buscarDisponibilidad(bundle.getString("busqueda"));
        }else{
            hotelAdapter = new HotelAdapter(listaGeneralHotel);
            listaTarjetasHotel.setAdapter(hotelAdapter);
        }


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

        btnMapa.setBackgroundResource(R.color.control_border_light);
        imageBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!estadobton){
                    estadobton = true;
                    imageBusqueda.setImageResource(R.drawable.ocultar_buscador);
                    lineaDisponibilidad.setVisibility(View.VISIBLE);
                    linearAzul.setVisibility(View.GONE);
                    linearBotones.setVisibility(View.GONE);
                }else {
                    estadobton = false;
                    imageBusqueda.setImageResource(R.drawable.mostrar_buscador);
                    lineaDisponibilidad.setVisibility(View.GONE);
                    linearAzul.setVisibility(View.VISIBLE);
                    linearBotones.setVisibility(View.VISIBLE);
                }

            }
        });



        btnListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mapView.setVisibility(View.GONE);
                listaTarjetasHotel.setVisibility(View.VISIBLE);
                btnMapa.setBackgroundResource(R.color.control_border_light);
                btnListas.setBackgroundResource(R.color.white);
                /*ListadoHotelesFragment listadoHotelesFragment = new ListadoHotelesFragment();
                listadoHotelesFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_contenedor, listadoHotelesFragment).commit();*/
            }
        });
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMapa.setBackgroundResource(R.color.white);
                btnListas.setBackgroundResource(R.color.control_border_light);
                _mapView.setVisibility(View.VISIBLE);
                listaTarjetasHotel.setVisibility(View.GONE);

               /* ListadoHotelesFragment menuPrincipal = new ListadoHotelesFragment();
                menuPrincipal.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_contenedor, menuPrincipal).commit();*/
            }
        });



    }


    private void PrepareArrivalCalendar()
    {
        _arrivalDate = null;
        _arrivalCalendarListener = new CalendarListener()
        {
            @Override
            public void onSelectDate(Date date, View view )
            {
                _arrivalDate = date;
                if( _departureDate != null && _departureDate.compareTo( _arrivalDate ) <= 0 )
                {
                    //txtLlegada.setText("TXT LLEGADA");
                    _departureDate = null;
                }
                SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
                txt_llegada.setText(sdf.format(date));
                _arrivalCalendarFragment.dismiss();

                Calendar c = Calendar.getInstance();
                c.setTime( date );
                c.add( Calendar.DATE, 1 );

                _departureDate = c.getTime();
                txt_salida.setText(sdf.format(_departureDate));

            }
        };

        Bundle bundle = new Bundle();
        bundle.putString( CalendarFragment.DIALOG_TITLE, "Llegada" );
        _arrivalCalendarFragment = new CalendarFragment();
        _arrivalCalendarFragment.setCalendarListener( _arrivalCalendarListener );
        _arrivalCalendarFragment.setArguments( bundle );

    txt_llegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Date d = cal.getTime();
                if( _arrivalDate != null )
                {
                    _arrivalCalendarFragment.setSelectedDates( _arrivalDate, _arrivalDate );
                }
                _arrivalCalendarFragment.setMinDate(d);
                _arrivalCalendarFragment.show(getSupportFragmentManager(), "TO_CALENDAR_FRAGMENT");
            }
        });
    }


    private void PrepareDepartureCalendar()
    {
        _departureDate = null;
        _departureCalendarListener = new CalendarListener()
        {
            @Override
            public void onSelectDate( Date date, View view )
            {
                _departureDate = date;
                if( _arrivalDate != null && _arrivalDate.compareTo( _departureDate ) >= 0 )
                {

                    //txtSalida.setText("AKLDCMALKSMDCLSKMDSACKLS");
                    _arrivalDate = null;
                }
                SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
                txt_llegada.setText( sdf.format( date ) );
                _departureCalendarFragment.dismiss();


            }
        };

        Bundle bundle = new Bundle();
        bundle.putString( CalendarFragment.DIALOG_TITLE, "Salida" );
        _departureCalendarFragment = new CalendarFragment();
        _departureCalendarFragment.setCalendarListener( _departureCalendarListener );
        _departureCalendarFragment.setArguments( bundle );



     txt_salida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                //if( _arrivalDate != null )
                //	cal.setTime( _arrivalDate );
                cal.add( Calendar.DATE, 1 );
                Date d = cal.getTime();
                if( _departureDate != null )
                {
                    _departureCalendarFragment.setSelectedDates( _departureDate, _departureDate );
                }
                _departureCalendarFragment.setMinDate( d );
                _departureCalendarFragment.show( getSupportFragmentManager(), "TO_CALENDAR_FRAGMENT" );
            }
        });
    }



    public void buscarDisponibilidad(String busqueda){
        //System.out.println("->"+busqueda);

        contador=0;
        StringRequest registro = new StringRequest(Request.Method.GET,"https://www.cityexpress.com/umbraco/api/MobileAppServices/GetHotelsWithServices?SearchTerms="+busqueda, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    obtenerHoteles(new JSONArray(response));
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
            }
        });
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
    }

    public void obtenerHoteles(JSONArray hoteles){

        try{
            hotelJSON = hoteles;
            Hotel hotel;
            for (int i = 0; i < hoteles.length(); i++) {
                JSONObject nuevo = new JSONObject(hoteles.get(i).toString());
                hotel = new Hotel(new JSONObject(nuevo.getString("Hotele")),new JSONArray(nuevo.getString("Imagenes")));
                /*System.out.println("---->"+hoteles.get(0).toString());
                System.out.println("--->>" + nuevo.getString("Imagenes"));
                System.out.println("--->>" + nuevo.getString("Hotele"));*/
                if(hotel.getImagenPrincipal().equalsIgnoreCase("")){

                }else{
                    listaHotel.add(hotel);

                }
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
            //System.out.println("SIGLAS->"+listaHotel.get(contador).getSiglas());
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
                    "                <cit:FechaInicial>"+fechaPartida+"</cit:FechaInicial>\n" +
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
                public void onResponse(String response) { //wsMotor2014 // wsMotor2015_Prod
                    System.out.println(contador + "->contador" + response);
                    InputStream stream = null;
                    try {
                        stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                        parseXML(stream);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    obtenerDescripcionHotel(response);
                   /* try {
                        JSONObject nuevo = new JSONObject(hotelJSON.get(contador).toString());
                        listaGeneralHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")), new JSONArray(nuevo.getString("Imagenes")), habitacionBaseList, habitacionCityPremiosList));
                        System.out.println("TotalHabitaciones->"+listaGeneralHotel.get(0).getArrayHabitaciones().size());

                    }catch(JSONException e){

                    }*/
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
            //System.out.println("registro->" + registro.toString());
            registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(registro);
        }else{
            for (int i = 0; i <listaGeneralHotel.size() ; i++) {
                for (int j = 0; j < listaGeneralHotel.get(i).getArrayHabitaciones().size() ; j++) {
                    if(listaGeneralHotel.get(i).getArrayHabitaciones().get(j).getCodigoBase().equalsIgnoreCase("HCP")){
                        listaGeneralHotel.get(i).getArrayHabitaciones().remove(j);
                    }
                }
            }
            hotelAdapter = new HotelAdapter(listaGeneralHotel);
            listaTarjetasHotel.setAdapter(hotelAdapter);
        }
    }

    public void obtenerDescripcionHotel(String hotel){

        JSONObject jsonObj = null;
        JSONObject mobilegate = null;
        habitacionBase = new HabitacionBase();
        habitacionCity = new HabitacionBase();

        try {


            jsonObj = XML.toJSONObject(hotel);

            JSONObject envelope = new JSONObject(jsonObj.getString("s:Envelope"));

            JSONObject sbody = new JSONObject(envelope.getString("s:Body"));

            JSONObject roomsAvaliables = new JSONObject(sbody.getString("GetRoomsAvailablePromoResponse"));

            JSONObject resultRoomsAvailable = new JSONObject(roomsAvaliables.getString("GetRoomsAvailablePromoResult"));

            JSONObject disponibilidad = new JSONObject(resultRoomsAvailable.getString("a:Disponibilidad"));

            //System.out.println("Disponibilidad->"+disponibilidad.getString("a:Available"));
            //mobilegate = jsonObj.getJSONObject("Costo");

            JSONArray arreglodisponibilidad = new JSONArray(disponibilidad.getString("a:Available"));

            for (int i = 0; i < arreglodisponibilidad.length(); i++) {
                JSONObject descripcion = new JSONObject(arreglodisponibilidad.getString(i));

                if(descripcion.getString("a:CodigoTarifa").equalsIgnoreCase("1115P")){

                    //descripcion.getString("")
                    JSONObject tarifaBase = new JSONObject(descripcion.getString("a:TarifaBase"));
                    JSONArray habitacionBase = new JSONArray(tarifaBase.getString("a:HabBase"));

                    for (int j = 0; j < habitacionBase.length(); j++) {

                        habitacionCity = new HabitacionBase();
                        JSONObject habitacionBaseDisponibilidad = new JSONObject(habitacionBase.get(j).toString());
                        System.out.println("CUATRO1115P->"+habitacionBaseDisponibilidad.getString("a:CodBase"));
                        habitacionCity.setCodigoBase(habitacionBaseDisponibilidad.getString("a:CodBase"));
                        System.out.println("CINCO115P->"+habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject noche = new JSONObject(habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject costoNoche = new JSONObject(noche.getString("a:Noche"));
                        System.out.println("CostoTotal->"+costoNoche.get("a:Costo"));
                        habitacionCity.setCosto(costoNoche.get("a:Costo").toString());
                        habitacionCityPremiosList.add(habitacionCity);
                    }
                }else if(descripcion.getString("a:CodigoTarifa").equalsIgnoreCase("1114")){

                    JSONObject tarifaBase = new JSONObject(descripcion.getString("a:TarifaBase"));
                    JSONArray habitacionBaseJSON = new JSONArray(tarifaBase.getString("a:HabBase"));
                    for (int j = 0; j < habitacionBaseJSON.length(); j++) {
                        habitacionBase = new HabitacionBase();
                        JSONObject habitacionBaseDisponibilidad = new JSONObject(habitacionBaseJSON.get(j).toString());
                        System.out.println("CUATRO->"+habitacionBaseDisponibilidad.getString("a:CodBase"));
                        habitacionBase.setCodigoBase(habitacionBaseDisponibilidad.getString("a:CodBase"));
                        System.out.println("CINCO->"+habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject noche = new JSONObject(habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject costoNoche = new JSONObject(noche.getString("a:Noche"));
                        System.out.println("CostoTotal->"+costoNoche.get("a:Costo"));
                        habitacionBase.setCosto(costoNoche.get("a:Costo").toString());
                        habitacionBaseList.add(habitacionBase);
                    }
                }
            }


        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }
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
            habitacionBaseList = new ArrayList<>();
            habitacionCityPremiosList = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("Available")) {
                        } else if(tagname.equalsIgnoreCase("Disponibilidad")){

                        } else if(tagname.equalsIgnoreCase("HabBase")){
                            habitacionBase = new HabitacionBase();
                            habitacionCity = new HabitacionBase();

                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Available")) {

                            if(cityPremios){

                            }else{
                                JSONObject nuevo = new JSONObject(hotelJSON.get(contador).toString());
                                if(!(contador<listaGeneralHotel.size())){
                                    listaGeneralHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")), new JSONArray(nuevo.getString("Imagenes")), habitacionBaseList,habitacionCityPremiosList));

                                }
                                //listaGeneralHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")), new JSONArray(nuevo.getString("Imagenes")), habitacionBaseList,habitacionCityPremiosList));
                                //listaGeneralHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")), new JSONArray(nuevo.getString("Imagenes")), habitacionBaseList));
                                //System.out.println("TOTAL->" + listaGeneralHotel.size());
                                habitacionBaseList = new ArrayList<>();
                                //habitacionCityPremiosList = new ArrayList<>();
                                // add employee object to list
                                // employee.setHabitacionBasesList(habitacionBasesList);
                            }

                        }else if(tagname.equalsIgnoreCase("CodigoError")){

                        }else if(tagname.equalsIgnoreCase("CodigoTarifa")){
                            if(text.equalsIgnoreCase("1115P")){
                                System.out.println("Hola15");
                                cityPremios = true;
                            }else{
                                cityPremios = false;
                            }
                        }else if(tagname.equalsIgnoreCase("DescError")){
                        }else if(tagname.equalsIgnoreCase("Descripcion")){
                        }else if(tagname.equalsIgnoreCase("Hotel")){
                        }else if(tagname.equalsIgnoreCase("Promociones")){
                        }else if(tagname.equalsIgnoreCase("Disponibilidad")){

                        }else if(tagname.equalsIgnoreCase("TarifaBase")){
                        } else if(tagname.equalsIgnoreCase("HabBase")){

                            if(cityPremios){
                                habitacionCityPremiosList.add(habitacionCity);
                            }else{
                                habitacionBaseList.add(habitacionBase);
                            }
                            System.out.println("HabitacionBase" + habitacionBaseList.size());
                            System.out.println("HabitacionCity" + habitacionCityPremiosList.size());
                        }else if (tagname.equalsIgnoreCase("Descripcion")) {
                        } else if (tagname.equalsIgnoreCase("CodigoTarifa")) {
                        } else if (tagname.equalsIgnoreCase("Hotel")) {
                        } else if (tagname.equalsIgnoreCase("AVAILABILITY")) {
                            habitacionBase.setDisponibilidad(text);
                        } else if (tagname.equalsIgnoreCase("CodBase")) {
                            if(cityPremios){
                                habitacionCity.setCodigoBase(text);
                                System.out.println(text+"zz------");
                            }else{
                                habitacionCity.setCodigoBase("");
                                habitacionBase.setCodigoBase(text);
                            }
                        } else if (tagname.equalsIgnoreCase("DescBase")) {
                            habitacionBase.setDescBase(text);
                        } else if (tagname.equalsIgnoreCase("Costo")) {
                            if(cityPremios){
                                habitacionCity.setCosto(text);
                            }else{
                                habitacionBase.setCosto(text);
                            }
                        } else if (tagname.equalsIgnoreCase("Fecha")) {
                            habitacionBase.setFecha(text);
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

    }

}
