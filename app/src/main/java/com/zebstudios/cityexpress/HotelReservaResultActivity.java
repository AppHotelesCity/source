package com.zebstudios.cityexpress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

//import io.realm.Realm;
//import io.realm.RealmResults;

import static com.appsee.Appsee.addEvent;

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
    TextView txtPrecioSubtotal;
    TextView txtPrecioIva;
    TextView txtDireccionHotel;
    TextView txtReferenciaHotel;
    GoogleMap _map;
    MapView _mapView;
    Button btnEnviarMail;
    Button btnCompartir;
    Button btnAbrirUbicacion;
    ReservacionBD datosReservacion;
   // RealmResults<ReservacionBD> datosReservacion;
   // Realm realm;
    int numReservacion;
    int numHabitaciones;
    int numNoches;
    double precioHabitacion;
    double total;
    double subtotal;
    double iva;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hotel_reserva_result);

        imageBack = (ImageView) findViewById(R.id.back_button);
        txtNumeroReservacion = (TextView) findViewById(R.id.txt_numero_reservacion);
        txtNombreUsuario = (TextView) findViewById(R.id.txt_nombre_usuario);
        listHabitaciones = (ListView) findViewById(R.id.list_reservations);
        txtNombreHotel = (TextView) findViewById(R.id.lblHotelName);
        txtLlegada = (TextView) findViewById(R.id.dates_arrival_text);
        txtSalida = (TextView) findViewById(R.id.dates_departure_text);
        txtPrecioTotal = (TextView) findViewById(R.id.lblTotal);
        txtPrecioSubtotal = (TextView) findViewById(R.id.lblSubTotal);
        txtPrecioIva = (TextView) findViewById(R.id.lblIVA);
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
        subtotal = bundle.getDouble("subtotal");
        iva = bundle.getDouble("iva");

        ArrayList<SummaryEntry> sumary = new ArrayList<SummaryEntry>();

        for (int i = 0; i < numHabitaciones; i++) {
            sumary.add(new SummaryEntry(0, "Habitación " + (i + 1)));
            for (int j = 0; j < Math.abs(numNoches); j++) {
                sumary.add(new SummaryEntry(1, "Noche " + (j + 1) + " $" + ReservacionActivity.preciosList.get(i)));
            }
        }

        NestedListView list = (NestedListView) findViewById(R.id.list_reservations);
        SummaryListAdapter adapter = new SummaryListAdapter(this, sumary);
        list.setAdapter(adapter);


        //realm = Realm.getInstance(getBaseContext());
        ReservacionBDD ds = new ReservacionBDD( this );
        ds.open();
        datosReservacion =ds.getReservante(numReservacion);
        ds.close();

        System.out.println("NombreHotel" + datosReservacion.getNombreHotel() + "TOTAL->" + ReservacionActivity.titulares.get(0).getPrecio());
        System.out.println("NombreHotel"+datosReservacion.getNombreHotel() + "TOTAL->" +ReservacionActivity.titulares.get(1).getPrecio());


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
                String to = datosReservacion.getEmailHotel();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.setType("message/rfc822");
                startActivityForResult(Intent.createChooser(email, "Send Email"), 1);
                addEvent("HotelDetails-SendEmail-Smartphone");
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

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" +datosReservacion.getLongitudHotel()+ "&daddr="+datosReservacion.getLatitudHotel()));
                startActivity(intent);
                
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
        txtNumeroReservacion.setText(""+datosReservacion.getNumReservacion());
        txtNombreUsuario.setText(datosReservacion.getNombreUsuario());
        txtNombreHotel.setText(datosReservacion.getNombreHotel());
        txtLlegada.setText(sdf.format(datosReservacion.getFechaLlegada()));
        txtSalida.setText(sdf.format(datosReservacion.getFechaSalida()));

        txtPrecioSubtotal .setText(String.format("Subtotal: $%,.2f ", subtotal) + " M.N");
        txtPrecioIva .setText(String.format("Impuestos: $%,.2f ", iva) + " M.N");
        txtPrecioTotal .setText(String.format("Total: $%,.2f ", total) + " M.N");
        txtDireccionHotel .setText("" + datosReservacion.getDireccionHotel());
        txtReferenciaHotel.setText("" + datosReservacion.getDescripcionLugarHotel());
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

        LatLng hotelPosition =  new LatLng( datosReservacion.getLatitudHotel(), datosReservacion.getLongitudHotel() );
        Marker m = _map.addMarker( new MarkerOptions().position( hotelPosition ).title(datosReservacion.getNombreHotel()) );
        //_map.animateCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelPosition, 14));
        m.showInfoWindow();

    }
}
