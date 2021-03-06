package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

//import io.realm.Realm;
//import io.realm.RealmResults;

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
    TextView txtPrecioSubtotal;
    TextView txtPrecioIva;
    TextView txtLeyendaCambios;
    TextView txtDireccionHotel;
    TextView txtReferenciaHotel;
    TextView txtToolbar;

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
    ReservacionBD datosReservacion;
    ProgressDialog progress;

    int numReservacion;
    @Override

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_reservacion_detail_activity);

         txtNumeroReservacion = (TextView) findViewById(R.id.txt_num_reservacion);
         txtNombreUsuario = (TextView) findViewById(R.id.txt_nombre_usuario);
         txtHabitacion = (TextView) findViewById(R.id.txt_habitacion_reservacion);
         txtNombreHotel = (TextView) findViewById(R.id.txt_nombre_hotel);
         txtLlegada = (TextView) findViewById(R.id.txt_fecha_llegada);
         txtSalida = (TextView) findViewById(R.id.txt_fecha_salida);
         txtTipoHabitacion = (TextView) findViewById(R.id.txt_tipo_habitacion);
         txtPrecioHabitacion = (TextView) findViewById(R.id.txt_precio_habitacion);
         txtNumAdultos = (TextView) findViewById(R.id.numero_adultos_reserva);
         txtNumNiños = (TextView) findViewById(R.id.numero_niños_reserva);
         txtPrecioTotal = (TextView) findViewById(R.id.txt_precio_total);
         txtPrecioSubtotal = (TextView) findViewById(R.id.txt_precio_subtotal);
         txtPrecioIva = (TextView) findViewById(R.id.txt_precio_iva);
         txtLeyendaCambios = (TextView) findViewById(R.id.txt_leyenda_cambios);
         txtDireccionHotel = (TextView) findViewById(R.id.txt_direccion_hotel);
         txtReferenciaHotel = (TextView) findViewById(R.id.txt_referencia_hotel);
        _mapView = (MapView) findViewById( R.id.mapViewReservacion);
        txtToolbar = (TextView) findViewById(R.id.toolbarTitle);

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
        txtToolbar.setText("Mis reservaciones");

        Bundle bundle =  getIntent().getExtras();
        numReservacion = bundle.getInt("numReservacion");

        ReservacionBDD ds = new ReservacionBDD( this );
        ds.open();
        datosReservacion =ds.getReservante(numReservacion);
        ds.close();

        System.out.println("NombreHotel" + datosReservacion.getNombreHotel() + "TOTAL->" + datosReservacion.getHabCosto() + "SiglasHotel->" + datosReservacion.getSiglasHotel());

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
                String to = datosReservacion.getEmailHotel();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.setType("message/rfc822");
                email.putExtra(Intent.EXTRA_SUBJECT, "Detalles de mi reservación");
                email.putExtra(Intent.EXTRA_TEXT,""+datosReservacion.getNombreHotel()+" "+datosReservacion.getDireccionHotel()+ "Comprobante de reservación"
                        + " Tu reservación ha sido confirmada con la clave "+ datosReservacion.getNumReservacion()
                        + " "+ datosReservacion.getNombreUsuario() + " " + datosReservacion.getApellidoUsuario()+"\n"
                        + "\nHOTEL"
                        + "\n\n" + datosReservacion.getDireccionHotel()
                        + "\n\nPaís:MX"
                        + "\n\nTélefono: 014491492900"
                        + "\n\nFax: "
                        + "\n\nLlegada Check in: "+ datosReservacion.getFechaLlegada()
                        + "\n\nSalida Check out " + datosReservacion.getFechaSalida()
                        + "\n\nCuartos: 1"
                        + "\n\nNombre: "+ datosReservacion.getNombreUsuario() + " "+ datosReservacion.getApellidoUsuario()
                        + "\n\nTipo de Habitación: " + datosReservacion.getDeschabitacion()
                        + "\n\nAcompañantes: " + (datosReservacion.getAdultos() + datosReservacion.getInfantes()
                        + "\n\nTarifa por Cuarto: " + datosReservacion.getHabCosto()
                        + "\n\nTotal a pagar: " + total()
                        + "\n\nTarifas sujetas a cambios sin previo aviso.\n\n" +
                        "Aplica cargo por persona extra.\n\n" +
                        "Aplican restricciones.\n\n" +
                        "Políticas de cambios y cancelaciones de reservaciones:\n\n" +
                        "1.- Cualquier cambio o cancelación a su reservación deberá solicitarla al 01 800 248 9397 con anticipación mínima de 24 horas antes de la fecha de llegada al hotel proporcionando su (s) clave (s) de confirmación.\n" +
                        "2.- En temporada alta cualquier cambio ó cancelación deberá solicitarlo 72 horas antes de la fecha de llegada al hotel y proporcionar su (s) clave (s) de confirmación. Para conocer la información acerca de las fechas de temporada alta, consúltenos al 01 800 248 9397.\n" +
                        "3.- De no realizarse el cambio o cancelación en el tiempo y forma antes mencionada, se hará el cargo por el monto de una noche más impuestos por cada habitación a la tarjeta de crédito con la que se garantizó la reservación.\n" +
                        "4.- El día de llegada, el hotel pre-verificará el crédito de la tarjeta de crédito con la que se garantiza la reservación. Si dicha tarjeta no es autorizada, y en caso de alta ocupación en el hotel, la reservación no garantizada se cancelará automáticamente.\n" +
                        "5.- En estancias de más de una noche, si el huésped no se presenta al hotel, se aplicará el cargo de “No Show” solo por la primera noche de habitación reservada más impuestos a la tarjeta de crédito con la que se garantizó la reservación. El resto de la estancia se cancelará automáticamente.Habrá cargo extra por persona adicional (mayor a 12 años).\n" +
                        "\nLa capacidad máxima de personas dependerá del tipo habitación reservado.\n" +
                        "\nEl costo varía según la marca del hotel.\n" +
                        "\nPara mayor información también contáctenos a través de nuestro"));
                startActivityForResult(Intent.createChooser(email, "Send Email"), 1);
                addEvent("HotelDetails-SendEmail-Smartphone");
            }
        });

            btnConsultarSaldos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ConsultarSaldosActivity.class);
                    intent.putExtra("cuarto", datosReservacion.getNumHabitacionAsigado());
                    intent.putExtra("siglas", datosReservacion.getSiglasHotel());
                    intent.putExtra("apellido", datosReservacion.getApellidoUsuario());
                    startActivity(intent);
                }
            });
        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Ya tengo mi reserva en " + datosReservacion.getNombreHotel());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,  "Compartir en: " ));

            }
        });

        btnComoLlegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + datosReservacion.getLatitudHotel() + "," + datosReservacion.getLongitudHotel() + " (" + datosReservacion.getNombreHotel() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(geoUri));
                startActivity(intent);
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
                String to = datosReservacion.getEmailHotel();
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
                Log.e("btnChat", "Web View Chat");
                Uri chatwv = Uri.parse("http://chat.hotelescity.com/WebAPISamples76/Chat/HtmlChatFrameSet.jsp");
                Intent intent = new Intent(Intent.ACTION_VIEW, chatwv);
                startActivity(intent);
                addEvent("HotelDetails-Chat-Smartphone");
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
        txtNumeroReservacion.setText("" + datosReservacion.getNumReservacion());
        txtNombreUsuario.setText(datosReservacion.getNombreUsuario() + " " + datosReservacion.getApellidoUsuario());
        txtHabitacion .setText("");
        txtNombreHotel .setText(datosReservacion.getNombreHotel());
        txtLlegada .setText(sdf.format(datosReservacion.getFechaLlegada()));
        txtSalida.setText(sdf.format(datosReservacion.getFechaSalida()));
        txtHabitacion.setText(datosReservacion.getNumHabitacionAsigado());
        txtTipoHabitacion .setText(datosReservacion.getDeschabitacion());
        txtPrecioHabitacion.setText("$" + datosReservacion.getHabCosto() + "M.N");
        txtNumAdultos.setText("" + (datosReservacion.getAdultos()));
        txtNumNiños .setText("" + datosReservacion.getInfantes());
        txtPrecioTotal .setText(String.format("Total: $%,.2f ", total())+ " M.N");
        txtPrecioSubtotal .setText(String.format("Subtotal: $%,.2f ", Double.parseDouble(datosReservacion.getHabCosto().replace(",", ""))) + " M.N");
        txtPrecioIva .setText(String.format("Impuestos: $%,.2f ", Double.parseDouble(datosReservacion.getIva().replace(",",""))) + " M.N");
        txtDireccionHotel .setText("" + datosReservacion.getDireccionHotel());
        txtReferenciaHotel.setText("" + datosReservacion.getDescripcionLugarHotel());
        btnCheckIn.setEnabled(datosReservacion.isCheckIn());
        btnCheckOut.setEnabled(datosReservacion.isCheckOut());
        if(datosReservacion.isCityPremios()){
            txtLeyendaCambios.setVisibility(View.VISIBLE);
        }else{
            txtLeyendaCambios.setVisibility(View.GONE);
        }

        if(datosReservacion.isCheckIn()){
            btnCheckOut.setEnabled(false);
            btnFacturacion.setEnabled(false);
        }else{
            btnCheckOut.setEnabled(true);
            btnConsultarSaldos.setEnabled(true);
            txtHabitacion .setText(", Habitación " + datosReservacion.getNumHabitacionAsigado());
        }
        if(!datosReservacion.isCheckOut() && !datosReservacion.isCheckIn()){
            btnFacturacion.setEnabled(true);
            btnCheckOut.setEnabled(false);
            btnConsultarSaldos.setEnabled(false);
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

        LatLng hotelPosition =  new LatLng( datosReservacion.getLatitudHotel(), datosReservacion.getLongitudHotel() );
        Marker m = _map.addMarker( new MarkerOptions().position(hotelPosition).title(datosReservacion.getNombreHotel()) );
        //_map.animateCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelPosition, 14));
        m.showInfoWindow();

    }

    public double total(){
        return (Double.parseDouble(datosReservacion.getHabCosto().replace(",","")) + Double.parseDouble(datosReservacion.getIva().replace(",","")));
    }

    public void checkIn(){
        progress = ProgressDialog.show(MiReservacionDetailActivity.this, "Cargando...",
                "Espere por favor", true);
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

        enviarxml = enviarxml.replace("{hotel}", datosReservacion.getSiglasHotel());
        enviarxml = enviarxml.replace("{codigoreserva}", ""+datosReservacion.getNumReservacion());
        enviarxml = enviarxml.replace("{ref}", "");


        Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);

        System.out.println("XML a enviar --> " + enviarxml);


        final String finalEnviarxml = enviarxml;
        StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.URL_CHECKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("RESPUESTACHECKIN"+ response);
                try {

                    JSONObject jsonObj = null;
                    JSONObject body = null;
                    JSONObject checkInResult = null;

                    jsonObj = XML.toJSONObject(response);
                    body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body");
                    checkInResult = body.getJSONObject("Ws_CheckInResponse").getJSONObject("Ws_CheckInResult");

                    Log.d("JSON", checkInResult.toString());
                    finObtenerCheckIn(checkInResult);

                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }finally {
                    progress.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
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
                params.put("SOAPAction", "http://tempuri.org/IWHSReservationEngine/Ws_CheckIn");
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
            final ReservacionBD reservacionBD = new ReservacionBD();
            reservacionBD.setNombreUsuario(datosReservacion.getNombreUsuario());
            reservacionBD.setApellidoUsuario(datosReservacion.getApellidoUsuario());
            reservacionBD.setNombreHotel(datosReservacion.getNombreHotel());
            reservacionBD.setFechaLlegada(datosReservacion.getFechaLlegada());
            reservacionBD.setFechaSalida(datosReservacion.getFechaSalida());
            reservacionBD.setDescripcionLugarHotel(datosReservacion.getDescripcionLugarHotel());
            reservacionBD.setSiglasHotel(datosReservacion.getSiglasHotel());
            reservacionBD.setDeschabitacion(datosReservacion.getDeschabitacion());
            reservacionBD.setLatitudHotel(datosReservacion.getLatitudHotel());
            reservacionBD.setLongitudHotel(datosReservacion.getLongitudHotel());
            reservacionBD.setHabCosto(datosReservacion.getHabCosto());
            reservacionBD.setTotal(datosReservacion.getTotal());
            reservacionBD.setSubtotal(datosReservacion.getSubtotal());
            reservacionBD.setIva(datosReservacion.getIva());
            reservacionBD.setNumReservacion(datosReservacion.getNumReservacion());
            reservacionBD.setAdultos(checkInResult.getInt("a:totaladults"));
            reservacionBD.setNumHabitacionAsigado(checkInResult.getString("a:room"));
            reservacionBD.setInfantes(datosReservacion.getInfantes());
            reservacionBD.setCodigoHabitacion(datosReservacion.getCodigoHabitacion());
            reservacionBD.setNumNoches(datosReservacion.getNumNoches());
            reservacionBD.setNumHabitaciones(datosReservacion.getNumHabitaciones());
            reservacionBD.setDireccionHotel(datosReservacion.getDireccionHotel());
            reservacionBD.setCheckIn(false);
            reservacionBD.setCheckOut(true);
            reservacionBD.setConsultarSaldos(true);

            ReservacionBDD ds = new ReservacionBDD( this );
            ds.open();
            ds.update(reservacionBD, "" + numReservacion);
            ds.close();

            txtHabitacion.setText(", Habitación " + checkInResult.getString("a:room"));
            alert("Check in realizado correctamente. El número de tu habitación es " + checkInResult.getString("a:room") + ". La fecha de check-out es " + checkInResult.getString("a:dateout") + " antes de la 1:00 PM");
            btnCheckIn.setEnabled(false);
            btnCheckOut.setEnabled(true);
            btnConsultarSaldos.setEnabled(true);
            btnConsultarSaldos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ConsultarSaldosActivity.class);
                    intent.putExtra("cuarto", reservacionBD.getNumHabitacionAsigado());
                    intent.putExtra("siglas", datosReservacion.getSiglasHotel());
                    intent.putExtra("apellido", datosReservacion.getApellidoUsuario());
                    startActivity(intent);
                }
            });
        }

    }
    public void errorObtenerCheckIn(){

    }

    public void checkOut(){
        progress = ProgressDialog.show(MiReservacionDetailActivity.this, "Cargando...",
                "Espere por favor", true);
        String enviarxml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://tempuri.org/\">\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <ns1:WS_CheckOUT>\n" +
                "            <ns1:guest_code>{guestcode}</ns1:guest_code>\n" +
                "            <ns1:prop_code>{propcode}</ns1:prop_code>\n" +
                "            <ns1:last_name>{lastname}</ns1:last_name>\n" +
                "        </ns1:WS_CheckOUT>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        enviarxml = enviarxml.replace("{guestcode}","" + datosReservacion.getNumReservacion());
        enviarxml = enviarxml.replace("{propcode}", "" + datosReservacion.getSiglasHotel());
        enviarxml = enviarxml.replace("{lastname}", "" + datosReservacion.getApellidoUsuario());


        Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);

        System.out.println("XML a enviar --> " + enviarxml);


        final String finalEnviarxml = enviarxml;
        StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.URL_CHECKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Check Out Response"+response);
                try {
                    progress.dismiss();
                    JSONObject jsonObj = null;
                    JSONObject body = null;
                    JSONObject checkOutResult = null;

                    jsonObj = XML.toJSONObject(response);
                    body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body");
                    checkOutResult = body.getJSONObject("WS_CheckOUTResponse");

                    Log.d("JSON", checkOutResult.toString());

                    try {
                        finObtenerCheckOut(checkOutResult);
                    }catch(Exception e){
                        alert("Ocurrio un problema intenta mas tarde");
                    }

                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
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
                params.put("SOAPAction", "http://tempuri.org/IWHSReservationEngine/WS_CheckOUT");
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

    public void finObtenerCheckOut(JSONObject checkOutResult) throws Exception{
        //if(checkOutResult.getInt("a:CodigoError")!=0){
        if(checkOutResult.getString("WS_CheckOUTResult").contains("incorrecto")){
            //alert(checkOutResult.getString("a:DescError"));
            alert(checkOutResult.getString("WS_CheckOUTResult"));
        } else if(checkOutResult.getString("WS_CheckOUTResult").equalsIgnoreCase("OK")){


            ReservacionBD reservacionBD = new ReservacionBD();
            reservacionBD.setNombreUsuario(datosReservacion.getNombreUsuario());
            reservacionBD.setApellidoUsuario(datosReservacion.getApellidoUsuario());
            reservacionBD.setNombreHotel(datosReservacion.getNombreHotel());
            reservacionBD.setFechaLlegada(datosReservacion.getFechaLlegada());
            reservacionBD.setFechaSalida(datosReservacion.getFechaSalida());
            reservacionBD.setDescripcionLugarHotel(datosReservacion.getDescripcionLugarHotel());
            reservacionBD.setSiglasHotel(datosReservacion.getSiglasHotel());
            reservacionBD.setDeschabitacion(datosReservacion.getDeschabitacion());
            reservacionBD.setLatitudHotel(datosReservacion.getLatitudHotel());
            reservacionBD.setLongitudHotel(datosReservacion.getLongitudHotel());
            reservacionBD.setHabCosto(datosReservacion.getHabCosto());
            reservacionBD.setTotal(datosReservacion.getTotal());
            reservacionBD.setSubtotal(datosReservacion.getSubtotal());
            reservacionBD.setIva(datosReservacion.getIva());
            reservacionBD.setNumReservacion(datosReservacion.getNumReservacion());
            reservacionBD.setAdultos(datosReservacion.getAdultos());
            reservacionBD.setNumHabitacionAsigado(datosReservacion.getNumHabitacionAsigado());
            reservacionBD.setInfantes(datosReservacion.getInfantes());
            reservacionBD.setCodigoHabitacion(datosReservacion.getCodigoHabitacion());
            reservacionBD.setNumNoches(datosReservacion.getNumNoches());
            reservacionBD.setNumHabitaciones(datosReservacion.getNumHabitaciones());
            reservacionBD.setDireccionHotel(datosReservacion.getDireccionHotel());
            reservacionBD.setCheckIn(false);
            reservacionBD.setCheckOut(false);
            reservacionBD.setConsultarSaldos(false);

            ReservacionBDD ds = new ReservacionBDD( this );
            ds.open();
            ds.update(reservacionBD, "" + numReservacion);
            ds.close();
            btnCheckOut.setEnabled(false);
            btnConsultarSaldos.setEnabled(false);
            btnFacturacion.setEnabled(true);
            alert("Check out Exitoso");
        }else{
            alert(checkOutResult.getString("WS_CheckOUTResult"));
        }
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
