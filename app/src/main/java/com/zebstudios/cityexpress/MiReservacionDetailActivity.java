package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.appsee.Appsee.addEvent;

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
        btnConsultarSaldos.setEnabled(false);
        btnFacturacion.setEnabled(false);

        Bundle bundle =  getIntent().getExtras();
        numReservacion = bundle.getInt("numReservacion");

        realm = Realm.getInstance(getBaseContext());

        datosReservacion = realm.where(ReservacionBD.class).equalTo("numReservacion",numReservacion).findAll();

        System.out.println("NombreHotel"+datosReservacion.get(0).getNombreHotel() + "TOTAL->" +datosReservacion.get(0).getHabCosto() + "SiglasHotel->" + datosReservacion.get(0).getSiglasHotel());

        llenarInformacion();
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIn();
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut();
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

                Uri uri = Uri.parse("http://facturacion.hotelescity.com:8999/Whs-AutoFact/Default.aspx"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

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
                String to = datosReservacion.get(0).getEmailHotel();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.setType("message/rfc822");
                startActivityForResult(Intent.createChooser(email, "Send Email"), 1);
                addEvent("HotelDetails-SendEmail-Smartphone");
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
        btnCheckIn.setEnabled(datosReservacion.get(0).isCheckIn());
        btnCheckOut.setEnabled(datosReservacion.get(0).isCheckOut());
        if(datosReservacion.get(0).isCheckIn()){
            btnCheckIn.setEnabled(false);
            btnCheckOut.setEnabled(true);
        }
        if(datosReservacion.get(0).isCheckOut()){
            btnCheckOut.setEnabled(false);
            btnCheckIn.setEnabled(false);
        }

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

    public void checkIn(){
        String enviarxml = "<soap:Envelope\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "    xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soap:Body>\n" +
                "        <Ws_CheckIn xmlns=\"http://tempuri.org/\">\n" +
                "            <hotel>{hotel}</hotel>\n" +
                "            <rsrv_code>{codigoreserva}</rsrv_code>\n" +
                "            <sRef>{ref}</sRef>\n" +
                "        </Ws_CheckIn>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";

        enviarxml = enviarxml.replace("{hotel}", datosReservacion.get(0).getSiglasHotel());
        enviarxml = enviarxml.replace("{codigoreserva}", ""+datosReservacion.get(0).getNumReservacion());
        enviarxml = enviarxml.replace("{ref}", "");


        Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);

        System.out.println("XML a enviar --> " + enviarxml);


        final String finalEnviarxml = enviarxml;
        StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.CLIENTE_UNICO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObj = null;
                    JSONObject body = null;
                    JSONObject checkInResult = null;

                    jsonObj = XML.toJSONObject(response);
                    body = jsonObj.getJSONObject("soap:Envelope").getJSONObject("soap:Body");
                    checkInResult = body.getJSONObject("Ws_CheckInResponse").getJSONObject("Ws_CheckInResult");

                    Log.d("JSON", checkInResult.toString());
                    finObtenerCheckIn(checkInResult);

                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                NetworkResponse response = error.networkResponse;
                String datos = new String(response.data);
                System.out.println("sout" + datos);

                //errorObtenerCheckIn("Error al cargar las tarjetas D=");
            }
        }) {

            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/xml; charset=utf-8");
                params.put("SOAPAction", "http://tempuri.org/ListadoTarjetas");
                Log.d("hsdhsdfhuidiuhsd", "clave");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                //return cadena.toString().getBytes();
                final String temp = finalEnviarxml.toString();
                return temp.toString().getBytes();
            }

        };

        System.out.println("registro->" + registro.toString());
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
    }

    public void finObtenerCheckIn(JSONObject checkInResult) throws JSONException{
        if(checkInResult.getInt("a:CodigoError")!=0){
            alert(checkInResult.getString("a:DescError"));
        }else{
            ReservacionBD reservacionBD = new ReservacionBD();
            reservacionBD.setNombreUsuario(datosReservacion.get(0).getNombreUsuario());
            reservacionBD.setApellidoUsuario(datosReservacion.get(0).getApellidoUsuario());
            reservacionBD.setNombreHotel(datosReservacion.get(0).getNombreHotel());
            reservacionBD.setFechaLlegada(datosReservacion.get(0).getFechaLlegada());
            reservacionBD.setFechaSalida(datosReservacion.get(0).getFechaSalida());
            reservacionBD.setDescripcionLugarHotel(datosReservacion.get(0).getDescripcionLugarHotel());
            reservacionBD.setSiglasHotel(datosReservacion.get(0).getSiglasHotel());
            reservacionBD.setDeschabitacion(datosReservacion.get(0).getDeschabitacion());
            reservacionBD.setLatitudHotel(datosReservacion.get(0).getLatitudHotel());
            reservacionBD.setLongitudHotel(datosReservacion.get(0).getLongitudHotel());
            reservacionBD.setHabCosto(datosReservacion.get(0).getHabCosto());
            reservacionBD.setNumReservacion(datosReservacion.get(0).getNumReservacion());
            reservacionBD.setAdultos(datosReservacion.get(0).getAdultos());
            reservacionBD.setInfantes(datosReservacion.get(0).getInfantes());
            reservacionBD.setCodigoHabitacion(datosReservacion.get(0).getCodigoHabitacion());
            reservacionBD.setNumNoches(datosReservacion.get(0).getNumNoches());
            reservacionBD.setNumHabitaciones(datosReservacion.get(0).getNumHabitaciones());
            reservacionBD.setDireccionHotel(datosReservacion.get(0).getDireccionHotel());
            reservacionBD.setCheckIn(true);
            reservacionBD.setCheckOut(false);
            reservacionBD.setConsultarSaldos(false);
            realm= Realm.getInstance(this);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(reservacionBD);
            realm.commitTransaction();

            alert(checkInResult.getString("a:DescError"));
            btnCheckIn.setEnabled(false);
        }

    }
    public void errorObtenerCheckIn(){

    }

    public void checkOut(){
        String enviarxml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://tempuri.org/\">\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <ns1:WS_CheckOUT>\n" +
                "            <ns1:guest_code>{guestcode}</ns1:guest_code>\n" +
                "            <ns1:prop_code>{propcode}</ns1:prop_code>\n" +
                "            <ns1:last_name>{lastname}</ns1:last_name>\n" +
                "        </ns1:WS_CheckOUT>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        enviarxml = enviarxml.replace("{guestcode}","" + datosReservacion.get(0).getNumReservacion());
        enviarxml = enviarxml.replace("{propcode}", "" + datosReservacion.get(0).getSiglasHotel());
        enviarxml = enviarxml.replace("{lastname}", "" + datosReservacion.get(0).getApellidoUsuario());


        Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);

        System.out.println("XML a enviar --> " + enviarxml);


        final String finalEnviarxml = enviarxml;
        StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.CLIENTE_UNICO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObj = null;
                    JSONObject body = null;
                    JSONObject checkOutResult = null;

                    jsonObj = XML.toJSONObject(response);
                    body = jsonObj.getJSONObject("soap:Envelope").getJSONObject("soap:Body");
                    checkOutResult = body.getJSONObject("WS_CheckOUTResponse").getJSONObject("WS_CheckOUTResult");

                    Log.d("JSON", checkOutResult.toString());

                    finObtenerCheckOut(checkOutResult);
                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }

                //

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                NetworkResponse response = error.networkResponse;
                String datos = new String(response.data);
                System.out.println("sout" + datos);

                //errorObtenerCheckIn("Error al cargar las tarjetas D=");
            }
        }) {

            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/xml; charset=utf-8");
                params.put("SOAPAction", "http://tempuri.org/ListadoTarjetas");
                Log.d("hsdhsdfhuidiuhsd", "clave");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                //return cadena.toString().getBytes();
                final String temp = finalEnviarxml.toString();
                return temp.toString().getBytes();
            }

        };

        System.out.println("registro->" + registro.toString());
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
    }

    public void finObtenerCheckOut(JSONObject checkOutResult){

    }

    private void alert( String message )
    {
        AlertDialog alert = new AlertDialog.Builder(this ).create();
        alert.setTitle( "Atención" );
        alert.setMessage( message );
        alert.setIcon( R.drawable.notification_warning_small );
        alert.setCancelable(false);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}
