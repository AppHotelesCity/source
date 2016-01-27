package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.appsee.Appsee.addEvent;

public class DetalleHotelActivity extends ActionBarActivity{

    private ServicesListAdapter _serviciosListAdapter;
    private ImageCache _imageCache;
    private GoogleMap _map;
    private MapView _mapView;
    private RecyclerView recyclerViewHabitaciones;
    private TextView txtDescripcion;
    private TextView txtServicios;
    private TextView txtMapa;
    private TextView txtTitulo;
    private ListView listView;
    private HabitacionAdapter habitacionAdapter;
    private WeatherReport _weatherReport;
    private LinearLayout linearLayoutDescripcion;
    private String siglas;
    ArrayList<RoomAvailableExtra> _extras;
    static RoomAvailable habitacion;
    static ArrayList<RoomAvailable> roomAvailableArrayList;
    static Hotel hotelActual;
    int posicion;
    ImageView imageViewBack;
    TextView txttitleActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hotel);

        txtDescripcion = (TextView) findViewById(R.id.txtDescripcionHotel);
        txtServicios = (TextView) findViewById(R.id.txtServiciosHotel);
        txtMapa = (TextView) findViewById(R.id.txtMapaHotel);
        _mapView = (MapView) findViewById( R.id.mapViewDetalle );
         listView= (ListView) findViewById(R.id.listServicios);
        txtTitulo = (TextView) findViewById(R.id.textViewTituloDetalleHotel);
        linearLayoutDescripcion = (LinearLayout) findViewById(R.id.linearDescripcion);
        recyclerViewHabitaciones = (RecyclerView) findViewById(R.id.cardListHabitaciones);
        imageViewBack = (ImageView) findViewById(R.id.back_button);
        recyclerViewHabitaciones.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewHabitaciones.setLayoutManager(llm);
        txttitleActionbar = (TextView) findViewById(R.id.toolbarTitle);

        txttitleActionbar.setText(ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getNombre());
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });






        Bundle bundle = getIntent().getExtras();
        posicion = bundle.getInt("posicion");
        System.out.println("PosicionDetalleHote"+posicion);

        hotelActual = ResultadosDisponibilidad.listaGeneralHotel.get(posicion);
        txtTitulo.setText(ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getNombre().toUpperCase());

        roomAvailableArrayList = new ArrayList<RoomAvailable>();
        _extras = new ArrayList<RoomAvailableExtra>();
        //Detalles

        for (int i = 0; i < ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().size(); i++) {
            if(i==0){
                siglas = ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().get(i).getCodigoBase();
            }else{
                siglas += ","+ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().get(i).getCodigoBase();
            }
        }
        pedirDetalleHabitacion(ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getSiglas(),siglas );


        TextView lblAddress = (TextView) findViewById(R.id.lblAddress);
        String address = getAddressString();
        lblAddress.setText( address );

        _imageCache = new ImageCache(this );
        String[] images = new String[ResultadosDisponibilidad.listaGeneralHotel.get(posicion).get_imagenes().length];
        for( int i = 0; i < ResultadosDisponibilidad.listaGeneralHotel.get(posicion).get_imagenes().length; i++ )
            images[i] = ResultadosDisponibilidad.listaGeneralHotel.get(posicion).get_imagenes()[i];

        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter( images );
        viewPager.setAdapter( adapter );

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager( viewPager );

        final float density = getResources().getDisplayMetrics().density;
        //indicator.setBackgroundColor(0xFFCCCCCC);
        indicator.setRadius( 4 * density );
        indicator.setPageColor(0xFF264b89); // 0xFF264b89
        indicator.setFillColor(0xFF264b89);
        indicator.setStrokeColor(0xFF9daeca);
        indicator.setStrokeWidth(1 * density);

        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getDescripcionMaps() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getDescripcionMaps().equalsIgnoreCase( "null" ) )
        {
            TextView lblDescription = (TextView) findViewById(R.id.lblDescription);
            lblDescription.setText(ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getDescripcionMaps());
        }

        ImageButton btnCall = (ImageButton) findViewById( R.id.btnCall );
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getTelefono();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, 0);
                //Appsee
                addEvent("HotelDetails-Call-Smartphone");
            }
        });

        ImageButton btnEmail = (ImageButton) findViewById( R.id.btnMail );
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getEmail();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.setType("message/rfc822");
                startActivityForResult(Intent.createChooser(email, "Send Email"), 1);
                addEvent("HotelDetails-SendEmail-Smartphone");
            }
        });

        ImageButton btnChat = (ImageButton) findViewById( R.id.btnChat );
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btnChat", "Web View Chat");
                Uri chatwv = Uri.parse("http://chat.hotelescity.com/WebAPISamples76/Chat/HtmlChatFrameSet.jsp");
                Intent intent = new Intent(Intent.ACTION_VIEW, chatwv);
                startActivity(intent);
                addEvent("HotelDetails-Chat-Smartphone");
            }
        });

        //Servicios
        _serviciosListAdapter = new ServicesListAdapter(this, ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getServicios() );

        listView.setAdapter(_serviciosListAdapter);

        listView.setDivider(null);
        listView.setDividerHeight(0);
        //Analytics analytics = (Analytics)getApplication();
        //analytics.sendAppEventTrack("HOTEL DETAIL ANDROID", "SERVICES", "HOTEL", _hotel.getNombre(), 1);

        //Mapa


        _mapView.onCreate( savedInstanceState );
        _mapView.onResume();

        try
        {
            MapsInitializer.initialize(getApplicationContext());
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        _map = _mapView.getMap();
        //_map = ((SupportMapFragment) getFragmentManager().findFragmentById( R.id.map )).getMap();

        LatLng hotelPosition =  new LatLng( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getLatitude(), ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getLongitude() );
        Marker m = _map.addMarker( new MarkerOptions().position( hotelPosition ).title( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getNombre() ) );
        //_map.animateCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelPosition, 14));
        m.showInfoWindow();



        txtDescripcion.setBackgroundResource(R.color.white);
        txtServicios.setBackgroundResource(R.color.control_border_light);
        txtMapa.setBackgroundResource(R.color.control_border_light);
        txtDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mapView.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                txtServicios.setBackgroundResource(R.color.control_border_light);
                txtMapa.setBackgroundResource(R.color.control_border_light);
                txtDescripcion.setBackgroundResource(R.color.white);
                linearLayoutDescripcion.setVisibility(View.VISIBLE);

            }
        });
        txtServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDescripcion.setBackgroundResource(R.color.control_border_light);
                txtServicios.setBackgroundResource(R.color.white);
                txtMapa.setBackgroundResource(R.color.control_border_light);
                _mapView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                linearLayoutDescripcion.setVisibility(View.GONE);
            }
        });
        txtMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMapa.setBackgroundResource(R.color.white);
                txtServicios.setBackgroundResource(R.color.control_border_light);
                txtDescripcion.setBackgroundResource(R.color.control_border_light);
                _mapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                linearLayoutDescripcion.setVisibility(View.GONE);
            }
        });

/*
        habitacion = new RoomAvailable();
        habitacion.setMaxAdultos(_extras.get(0).getNumPersonas());
        habitacion.setCode(ResultadosDisponibilidad.listaGeneralHotel.get(0).getArrayHabitaciones().get(0).getCodigoBase());
        habitacion.setTitle(ResultadosDisponibilidad.listaGeneralHotel.get(0).getNombre());
        habitacion.setMoneda("MXN");
        habitacion.setDescription(_extras.get(0).getDescripcion());
        habitacion.setImagen(_extras.get(0).getImagenApp());
        habitacion.setPromoCode("");
        habitacion.setTotal(Double.parseDouble(ResultadosDisponibilidad.listaGeneralHotel.get(0).getArrayHabitaciones().get(0).getCosto()));

        roomAvailableArrayList.add(habitacion);*/

        new GetWeather().execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private String getAddressString()
    {
        String address = "";
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getDireccion() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getDireccion().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getDireccion() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getColonia() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getColonia().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getColonia() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCp() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCp().equalsIgnoreCase( "null" ) )
        {
            address += "CP: " + ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCp() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCiudad() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCiudad().equalsIgnoreCase( "null" ) && ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getEstado() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getEstado().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCiudad() + ". " + ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getEstado();
        }
        else if( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCiudad() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCiudad().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getCiudad();
        }
        else if( ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getEstado() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getEstado().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getEstado();
        }
        if( address.endsWith( "\n" ) )
        {
            address = address.substring( 0, address.length() - 1 );
        }
        return address;
    }

    private class ImagePagerAdapter extends PagerAdapter
    {
        private String[] _images;

        public ImagePagerAdapter( String[] images )
        {
            _images = images;
        }

        @Override
        public int getCount()
        {
            return _images.length;
        }


        @Override
        public boolean isViewFromObject( View view, Object object )
        {
            return view == object;
        }


        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            Context context = getBaseContext();
            ImageView imageView = new ImageView( context );
            int padding = 0;
            imageView.setPadding( padding, padding, padding, padding );
            imageView.setScaleType( ImageView.ScaleType.FIT_XY );
            new ImageLoader( imageView, _imageCache ).execute( _images[position] );
            //( (ViewPager) container ).addView( imageView, 0 );
            container.addView( imageView, 0 );
            return imageView;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView((ImageView) object);
        }
    }


    public void weatherObtained()
    {
        LinearLayout panel = (LinearLayout) findViewById( R.id.tblWeather );
        panel.setVisibility( View.VISIBLE );

        if( _weatherReport.getWeathers().length > 0 )
        {
            ImageView imgWeather = (ImageView) findViewById( R.id.imgWeather );
            imgWeather.setImageResource( GetResourceForWeatherIcon( _weatherReport.getWeathers()[0].getIcon() ) );

            TextView lblDesc = (TextView) findViewById(R.id.lblWDesc);
            lblDesc.setText( _weatherReport.getWeathers()[0].getDescription() );
        }

        TextView lblTemp = (TextView) findViewById(R.id.lblWTemp);
        lblTemp.setText(String.format("%.1f", _weatherReport.getTemperature()).replace(",", ".") + "°");
    }

    private int GetResourceForWeatherIcon( String icon )
    {
        if( icon.equalsIgnoreCase("01d") ) return R.drawable.w_01d;
        else if( icon.equalsIgnoreCase( "01n" ) ) return R.drawable.w_01n;
        else if( icon.equalsIgnoreCase( "02d" ) ) return R.drawable.w_02d;
        else if( icon.equalsIgnoreCase( "02n" ) ) return R.drawable.w_02n;
        else if( icon.equalsIgnoreCase( "03d" ) ) return R.drawable.w_03d;
        else if( icon.equalsIgnoreCase( "03n" ) ) return R.drawable.w_03n;
        else if( icon.equalsIgnoreCase( "04d" ) ) return R.drawable.w_04d;
        else if( icon.equalsIgnoreCase( "04n" ) ) return R.drawable.w_04n;
        else if( icon.equalsIgnoreCase( "09d" ) ) return R.drawable.w_09d;
        else if( icon.equalsIgnoreCase( "09n" ) ) return R.drawable.w_09n;
        else if( icon.equalsIgnoreCase( "10d" ) ) return R.drawable.w_10d;
        else if( icon.equalsIgnoreCase( "10n" ) ) return R.drawable.w_10n;
        else if( icon.equalsIgnoreCase( "11d" ) ) return R.drawable.w_11d;
        else if( icon.equalsIgnoreCase( "11n" ) ) return R.drawable.w_11n;
        else if( icon.equalsIgnoreCase( "13d" ) ) return R.drawable.w_13d;
        else if( icon.equalsIgnoreCase( "13n" ) ) return R.drawable.w_13n;
        else if( icon.equalsIgnoreCase( "50d" ) ) return R.drawable.w_50d;
        else if( icon.equalsIgnoreCase( "50n" ) ) return R.drawable.w_50n;
        else return R.drawable.w_01d;
    }


    private class GetWeather extends AsyncTask<Void, Void, Integer>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground( Void... arg0 )
        {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall( "http://api.openweathermap.org/data/2.5/weather?lat=" + ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getLatitude() + "&lon=" + ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getLongitude() + "&APPID=2e9600ef77ddc382c98ac7ccef1ecd2c&lang=es&units=metric", ServiceHandler.GET );
            if( jsonStr != null )
            {
                try
                {
                    JSONObject json = new JSONObject( jsonStr );
                    _weatherReport = new WeatherReport( json );
                    if( !_weatherReport.isParsedOk() )
                        return -3;

                    return 0;
                }
                catch( Exception e )
                {
                    android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
                    return -2;
                }
            }
            else android.util.Log.e( "ServiceHandler", "Couldn't get any data" );

            return -1;
        }

        @Override
        protected void onPostExecute( Integer result )
        {
            super.onPostExecute(result);

            if( result == 0 ) weatherObtained();
        }
    }

    public void pedirDetalleHabitacion(String siglasHotel, String siglasHabitacion){
        StringRequest registro = new StringRequest(Request.Method.GET,"https://www.cityexpress.com/umbraco/api/MobileAppServices/GetHabitaciones?HotelCode="+siglasHotel+"&RoomCodes="+siglasHabitacion, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("JSONRespuesta->"+response);
                    obtenerHabitacion(new JSONArray(response));
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

    public void obtenerHabitacion(JSONArray results){
        try {
            for (int i = 0; i < results.length(); i++) {
                JSONObject h = results.getJSONObject(i);
                RoomAvailableExtra r = new RoomAvailableExtra(h);
                if (!r.isParsedOk()) {

                }
                _extras.add(r);
            }

            habitacionAdapter = new HabitacionAdapter(ResultadosDisponibilidad.listaGeneralHotel, ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones(),ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitacionesCity(),_extras,posicion);
            recyclerViewHabitaciones.setAdapter(habitacionAdapter);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int density = metrics.densityDpi;

            if (density == DisplayMetrics.DENSITY_XXHIGH) {
                int viewHeight = 620 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().size();
                recyclerViewHabitaciones.getLayoutParams().height = viewHeight;

                int altura = 120 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getServicios().length;
                listView.getLayoutParams().height = altura;
                System.out.println("AltoXX");
            }
            if (density == DisplayMetrics.DENSITY_XXXHIGH) {
                int viewHeight = 750 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().size();
                recyclerViewHabitaciones.getLayoutParams().height = viewHeight;
                int altura = 150 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getServicios().length;
                listView.getLayoutParams().height = altura;
                System.out.println("Alto XXX");
            }
            if (density == DisplayMetrics.DENSITY_HIGH) {
                int viewHeight = 320 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().size();
                recyclerViewHabitaciones.getLayoutParams().height = viewHeight;
                int altura = 80 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getServicios().length;
                listView.getLayoutParams().height = altura;
                System.out.println("Alto");
            }
            if (density == DisplayMetrics.DENSITY_XHIGH) {
                int viewHeight = 450 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().size();
                recyclerViewHabitaciones.getLayoutParams().height = viewHeight;
                int altura = 95 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getServicios().length;
                listView.getLayoutParams().height = altura;
                System.out.println("AltoX");
            }
            else if (density == DisplayMetrics.DENSITY_MEDIUM) {
                int viewHeight = 300 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().size();
                recyclerViewHabitaciones.getLayoutParams().height = viewHeight;
                int altura = 80 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getServicios().length;
                listView.getLayoutParams().height = altura;
                System.out.println("Medio");
            }
            else if (density == DisplayMetrics.DENSITY_LOW) {
                int viewHeight = 280 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getArrayHabitaciones().size();
                recyclerViewHabitaciones.getLayoutParams().height = viewHeight;
                int altura = 60 * ResultadosDisponibilidad.listaGeneralHotel.get(posicion).getServicios().length;
                listView.getLayoutParams().height = altura;
                System.out.println("Bajo");
            }

        }catch(Exception e){

        }
    }



}
