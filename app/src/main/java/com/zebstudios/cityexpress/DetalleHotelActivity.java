package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import static com.appsee.Appsee.addEvent;

public class DetalleHotelActivity extends ActionBarActivity {

    private ServicesListAdapter _serviciosListAdapter;
    private ImageCache _imageCache;
    private GoogleMap _map;
    private MapView _mapView;
    private RecyclerView recyclerViewHabitaciones;
    private TextView txtDescripcion;
    private TextView txtServicios;
    private TextView txtMapa;
    private ListView listView;
    private WeatherReport _weatherReport;
    private LinearLayout linearLayoutDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hotel);

        txtDescripcion = (TextView) findViewById(R.id.txtDescripcionHotel);
        txtServicios = (TextView) findViewById(R.id.txtServiciosHotel);
        txtMapa = (TextView) findViewById(R.id.txtMapaHotel);
        _mapView = (MapView) findViewById( R.id.mapViewDetalle );
         listView= (ListView) findViewById(R.id.listServicios);
        linearLayoutDescripcion = (LinearLayout) findViewById(R.id.linearDescripcion);

        //Detalles

        TextView lblAddress = (TextView) findViewById(R.id.lblAddress);
        String address = getAddressString();
        lblAddress.setText( address );

        _imageCache = new ImageCache(this );
        String[] images = new String[ResultadosDisponibilidad.listaGeneralHotel.get(0).get_imagenes().length];
        for( int i = 0; i < ResultadosDisponibilidad.listaGeneralHotel.get(0).get_imagenes().length; i++ )
            images[i] = ResultadosDisponibilidad.listaGeneralHotel.get(0).get_imagenes()[i];

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

        if( ResultadosDisponibilidad.listaGeneralHotel.get(0).getDescripcionMaps() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getDescripcionMaps().equalsIgnoreCase( "null" ) )
        {
            TextView lblDescription = (TextView) findViewById(R.id.lblDescription);
            lblDescription.setText(ResultadosDisponibilidad.listaGeneralHotel.get(0).getDescripcionMaps());
        }

        ImageButton btnCall = (ImageButton) findViewById( R.id.btnCall );
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + ResultadosDisponibilidad.listaGeneralHotel.get(0).getTelefono();
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
                String to = ResultadosDisponibilidad.listaGeneralHotel.get(0).getEmail();
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
        _serviciosListAdapter = new ServicesListAdapter(this, ResultadosDisponibilidad.listaGeneralHotel.get(0).getServicios() );

        listView.setAdapter( _serviciosListAdapter );

        listView.setDivider( null );
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

        LatLng hotelPosition =  new LatLng( ResultadosDisponibilidad.listaGeneralHotel.get(0).getLatitude(), ResultadosDisponibilidad.listaGeneralHotel.get(0).getLongitude() );
        Marker m = _map.addMarker( new MarkerOptions().position( hotelPosition ).title( ResultadosDisponibilidad.listaGeneralHotel.get(0).getNombre() ) );
        //_map.animateCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
        _map.moveCamera( CameraUpdateFactory.newLatLngZoom(hotelPosition, 14) );
        m.showInfoWindow();


        txtDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mapView.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                linearLayoutDescripcion.setVisibility(View.VISIBLE);

            }
        });
        txtServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mapView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                linearLayoutDescripcion.setVisibility(View.GONE);
            }
        });
        txtMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                linearLayoutDescripcion.setVisibility(View.GONE);
            }
        });

        new GetWeather().execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private String getAddressString()
    {
        String address = "";
        if( ResultadosDisponibilidad.listaGeneralHotel.get(0).getDireccion() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getDireccion().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(0).getDireccion() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(0).getColonia() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getColonia().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(0).getColonia() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(0).getCp() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getCp().equalsIgnoreCase( "null" ) )
        {
            address += "CP: " + ResultadosDisponibilidad.listaGeneralHotel.get(0).getCp() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(0).getCiudad() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getCiudad().equalsIgnoreCase( "null" ) && ResultadosDisponibilidad.listaGeneralHotel.get(0).getEstado() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getEstado().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(0).getCiudad() + ". " + ResultadosDisponibilidad.listaGeneralHotel.get(0).getEstado();
        }
        else if( ResultadosDisponibilidad.listaGeneralHotel.get(0).getCiudad() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getCiudad().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(0).getCiudad();
        }
        else if( ResultadosDisponibilidad.listaGeneralHotel.get(0).getEstado() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(0).getEstado().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(0).getEstado();
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
        lblTemp.setText( String.format( "%.2f", _weatherReport.getTemperature() ) + "°C" );
    }

    private int GetResourceForWeatherIcon( String icon )
    {
        if( icon.equalsIgnoreCase( "01d" ) ) return R.drawable.w_01d;
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
            String jsonStr = sh.makeServiceCall( "http://api.openweathermap.org/data/2.5/weather?lat=" + ResultadosDisponibilidad.listaGeneralHotel.get(1).getLatitude() + "&lon=" + ResultadosDisponibilidad.listaGeneralHotel.get(1).getLongitude() + "&lang=ES&units=metric", ServiceHandler.GET );

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
}
