package com.zebstudios.cityexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by edwinhernandez on 20/12/15.
 */
public class HotelReservaResultActivity extends Activity {

    ImageView imageBack;
    TextView txtNumeroReservacion;
    TextView txtNombreUsuario;
    ListView listHabitaciones;
    TextView txtNombreHotel;
    TextView txtSalida;
    TextView txtLlegada;
    TextView txtPrecioTotal;
    TextView txtDireccionHotel;
    TextView txtReferenciaHotel;
    GoogleMap _map;
    MapView _mapView;
    Button btnEnviarMail;
    Button btnCompartir;
    Button btnAbrirUbicacion;

    RealmResults<ReservacionBD> datosReservacion;
    Realm realm;
    int numReservacion;
    int numHabitaciones;
    int numNoches;
    double precioHabitacion;
    double total;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hotel_reserva_result);

        imageBack = (ImageView) findViewById(R.id.back_button);
        txtNumeroReservacion = (TextView) findViewById(R.id.txt_numero_reservacion);
        txtNombreUsuario = (TextView) findViewById(R.id.txt_nombre_usuario);
        listHabitaciones = (ListView) findViewById(R.id.list_reservations);
        txtNombreHotel = (TextView) findViewById(R.id.lblHotelName);
        txtSalida = (TextView) findViewById(R.id.dates_arrival_text);
        txtLlegada = (TextView) findViewById(R.id.dates_departure_text);
        txtPrecioTotal = (TextView) findViewById(R.id.lblTotal);
        txtDireccionHotel = (TextView) findViewById(R.id.lblAddress1);
        txtReferenciaHotel = (TextView) findViewById(R.id.lblAddress2);
        _mapView = (MapView) findViewById( R.id.mapViewReservaResult);
        btnEnviarMail = (Button) findViewById(R.id.btnSendEmail);
        btnCompartir = (Button) findViewById(R.id.btnShare);
        btnAbrirUbicacion = (Button) findViewById(R.id.btnOpenLocation);
        _mapView.onCreate(savedInstanceState);
        Bundle bundle =  getIntent().getExtras();
        numReservacion = Integer.parseInt(bundle.getString("numReservacion"));
        numHabitaciones = bundle.getInt("numHabitaciones");
        precioHabitacion = bundle.getDouble("precioHabitacion");
        numNoches = bundle.getInt("numNoches");
        total = bundle.getDouble("total");

        ArrayList<SummaryEntry> sumary = new ArrayList<SummaryEntry>();

        for (int i = 0; i < numHabitaciones; i++) {
            sumary.add(new SummaryEntry(0, "Habitación " + (i + 1)));
            for (int j = 0; j < Math.abs(numNoches); j++) {
                sumary.add(new SummaryEntry(1, "Noche " + (j + 1) + " $" + precioHabitacion));
            }
        }

        NestedListView list = (NestedListView) findViewById(R.id.list_reservations);
        SummaryListAdapter adapter = new SummaryListAdapter(this, sumary);
        list.setAdapter(adapter);


        realm = Realm.getInstance(getBaseContext());

        datosReservacion = realm.where(ReservacionBD.class).equalTo("numReservacion",numReservacion).findAll();

        System.out.println("NombreHotel"+datosReservacion.get(0).getNombreHotel() + "TOTAL->" +datosReservacion.get(0).getHabCosto());


        llenarInformacion();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelReservaResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnEnviarMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Ya tengo mi reserva en...");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,  "Compartir en: " ));

            }
        });

        btnAbrirUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HotelReservaResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void llenarInformacion(){
        SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
        txtNumeroReservacion.setText(""+datosReservacion.get(0).getNumReservacion());
        txtNombreUsuario.setText(datosReservacion.get(0).getNombreUsuario());
        txtNombreHotel.setText(datosReservacion.get(0).getNombreHotel());
        txtLlegada.setText(sdf.format(datosReservacion.get(0).getFechaLlegada()));
        txtSalida.setText(sdf.format(datosReservacion.get(0).getFechaSalida()));

        txtPrecioTotal .setText("Total: $ " + total + " M.N");
        txtDireccionHotel .setText("" + datosReservacion.get(0).getDireccionHotel());
        txtReferenciaHotel.setText("" + datosReservacion.get(0).getDescripcionLugarHotel());
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

        LatLng hotelPosition =  new LatLng( datosReservacion.get(0).getLatitudHotel(), datosReservacion.get(0).getLongitudHotel() );
        Marker m = _map.addMarker( new MarkerOptions().position( hotelPosition ).title(datosReservacion.get(0).getNombreHotel()) );
        //_map.animateCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelPosition, 14));
        m.showInfoWindow();

    }
}
