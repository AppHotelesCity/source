package com.zebstudios.cityexpress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
}
