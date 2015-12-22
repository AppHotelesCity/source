package com.zebstudios.cityexpress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by edwinhernandez on 21/12/15.
 */
public class MiReservacionDetailActivity extends Activity {

    TextView txtNumeroReservacion;
    TextView txtNombreUsuario;
    TextView txtHabitacion;
    TextView txtNombreHotel;
    TextView txtLlegada;
    TextView txtSalida;
    TextView txtTipoHabitacion;
    TextView txtPrecioHabitacion;
    TextView txtNumAdultos;
    TextView txtNumNiños;
    TextView txtPrecioTotal;
    TextView txtLeyendaCambios;
    TextView txtDireccionHotel;
    TextView txtReferenciaHotel;

    GoogleMap _map;
    MapView _mapView;
    Button btnCheckIn;
    Button btnCheckOut;
    Button btnEnviarCorreo;
    Button btnConsultarSaldos;
    Button btnCompartir;
    Button btnComoLlegar;
    Button btnFacturacion;

    ImageView imageCall;
    ImageView imageCorreo;
    ImageView imageChat;
    ImageView imageBack;

    RealmResults<ReservacionBD> datosReservacion;
    Realm realm;
    int numReservacion;
    @Override

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_reservacion_detail_activity);

         txtNumeroReservacion = (TextView) findViewById(R.id.txt_num_reservacion);
         txtNombreUsuario = (TextView) findViewById(R.id.txt_nombre_usuario);
         txtHabitacion = (TextView) findViewById(R.id.txt_habitacion_reservacion); //numero de habitacion
         txtNombreHotel = (TextView) findViewById(R.id.txt_nombre_hotel);
         txtLlegada = (TextView) findViewById(R.id.txt_fecha_llegada);
         txtSalida = (TextView) findViewById(R.id.txt_fecha_salida);
         txtTipoHabitacion = (TextView) findViewById(R.id.txt_tipo_habitacion);
         txtPrecioHabitacion = (TextView) findViewById(R.id.txt_precio_habitacion);
         txtNumAdultos = (TextView) findViewById(R.id.numero_adultos_reserva);
         txtNumNiños = (TextView) findViewById(R.id.numero_niños_reserva);
         txtPrecioTotal = (TextView) findViewById(R.id.txt_precio_total);
         txtLeyendaCambios = (TextView) findViewById(R.id.txt_leyenda_cambios);
         txtDireccionHotel = (TextView) findViewById(R.id.txt_direccion_hotel);
         txtReferenciaHotel = (TextView) findViewById(R.id.txt_referencia_hotel);
        _mapView = (MapView) findViewById( R.id.mapViewReservacion);

         btnCheckIn = (Button) findViewById(R.id.btn_Check_in);
         btnCheckOut = (Button) findViewById(R.id.btn_Check_out);
         btnEnviarCorreo = (Button) findViewById(R.id.btn_enviar_correo);
         btnConsultarSaldos = (Button) findViewById(R.id.btn_consultar_saldos);
         btnCompartir = (Button) findViewById(R.id.btn_compartir);
         btnComoLlegar = (Button) findViewById(R.id.btn_como_llegar);
         btnFacturacion = (Button) findViewById(R.id.btn_facturacion);

         imageCall = (ImageView) findViewById(R.id.btnCall);
         imageCorreo = (ImageView) findViewById(R.id.btnMail);
         imageChat = (ImageView) findViewById(R.id.btnChat);
         imageBack = (ImageView) findViewById(R.id.back_button);

        _mapView.onCreate(savedInstanceState);
        btnCheckIn.setEnabled(false);
        btnCheckOut.setEnabled(false);
        btnConsultarSaldos.setEnabled(false);
        btnFacturacion.setEnabled(false);

        Bundle bundle =  getIntent().getExtras();
        numReservacion = bundle.getInt("numReservacion");

        realm = Realm.getInstance(getBaseContext());

        datosReservacion = realm.where(ReservacionBD.class).equalTo("numReservacion",numReservacion).findAll();

        System.out.println("NombreHotel"+datosReservacion.get(0).getNombreHotel() + "TOTAL->" +datosReservacion.get(0).getHabCosto());

        llenarInformacion();
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnConsultarSaldos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Ya tengo mi reserva en blablablabla");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,  "Compartir en: " ));

            }
        });

        btnComoLlegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnFacturacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + "018002489773";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, 0);

            }
        });

        imageCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void llenarInformacion(){
        SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
        txtNumeroReservacion.setText(""+datosReservacion.get(0).getNumReservacion());
        txtNombreUsuario.setText(datosReservacion.get(0).getNombreUsuario());
        txtHabitacion .setText("");
        txtNombreHotel .setText(datosReservacion.get(0).getNombreHotel());
        txtLlegada .setText(sdf.format(datosReservacion.get(0).getFechaLlegada()));
        txtSalida.setText(sdf.format(datosReservacion.get(0).getFechaSalida()));
        txtTipoHabitacion .setText(datosReservacion.get(0).getDeschabitacion());
        txtPrecioHabitacion.setText("$" + datosReservacion.get(0).getHabCosto() + "M.N");
        txtNumAdultos.setText("" + datosReservacion.get(0).getAdultos());
        txtNumNiños .setText("" + datosReservacion.get(0).getInfantes());
        txtPrecioTotal .setText("Total: $ " + datosReservacion.get(0).getHabCosto() + " M.N");
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
