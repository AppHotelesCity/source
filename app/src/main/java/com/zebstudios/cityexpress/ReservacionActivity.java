package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservacionActivity extends Activity {

    Hotel _hotel;
    private ArrayList<HabitacionBase> _rooms;
    String cadena;
    String text;

    RadioButton btnMisma;
    EditText txtName;
    EditText txtLast;
    EditText txtEmail;
    EditText txtSocio;
    CheckBox cbAfiliate;
    EditText txtPhone;
    Spinner spinViaje;
    Spinner spinAdultos;
    Spinner spinNinos;

    SegmentedGroup segmentswitch;

    RadioButton btnTarjeta;
    RadioButton btnPaypal;


    ListView listaHabitaciones;
    LinearLayout linearpayTarjeta;
    LinearLayout linearaddTarjeta;
    LinearLayout linearImagenes;

    Button btnAddTarj;

    Button btnVerTarjetas;


    ReservaNumeroHabitacionesAdapter adapter;
    ArrayList<ItemListReserva> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.obtenerListadoTarjetas();

        setContentView(R.layout.activity_reservacion);

        TextView lblHotelName = (TextView) findViewById(R.id.lblHotelName);
        TextView lblArrivalDate = (TextView) findViewById( R.id.dates_arrival_text );
        TextView lblDepartureDate = (TextView) findViewById( R.id.dates_departure_text );
        TextView lblTotal = (TextView) findViewById(R.id.lblTotal);
        TextView lblHotelName2 = (TextView) findViewById( R.id.lblHotelName2 );
        TextView lblArrivalDate2 = (TextView) findViewById( R.id.dates_arrival_text2 );
        TextView lblDepartureDate2 = (TextView) findViewById( R.id.dates_departure_text2 );
        TextView lblTotal2 = (TextView) findViewById(R.id.lblTotal2);

         btnMisma = (RadioButton) findViewById( R.id.btn_rad_misma );
         txtName = (EditText) findViewById( R.id.txtName );
         txtLast = (EditText) findViewById( R.id.txtLast );
         txtEmail = (EditText) findViewById( R.id.txtEmail );
         txtSocio = (EditText) findViewById( R.id.txtSocio );
         cbAfiliate = (CheckBox) findViewById( R.id.cbAfiliate );
         txtPhone = (EditText) findViewById( R.id.txtPhone );
         spinViaje = (Spinner) findViewById( R.id.spinViaje );
         spinAdultos = (Spinner) findViewById( R.id.spinAdultos );
         spinNinos = (Spinner) findViewById( R.id.spinNinos );

        linearpayTarjeta = (LinearLayout) findViewById(R.id.linearAddtarjeta);
        linearaddTarjeta = (LinearLayout) findViewById(R.id.pnlCCPayment);
        linearImagenes = (LinearLayout) findViewById(R.id.linearImagePagos);

        btnAddTarj = (Button) findViewById(R.id.btnAddTarjeta);
        btnVerTarjetas = (Button) findViewById(R.id.verTarjetas);

        segmentswitch = (SegmentedGroup) findViewById(R.id.segmentedPaymentMethod); 
        btnTarjeta = (RadioButton)findViewById(R.id.btn_method_card);
        btnPaypal = (RadioButton) findViewById(R.id.btn_method_paypal);

        listaHabitaciones = (ListView)findViewById(R.id.list_reservations);

        segmentswitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(btnTarjeta.isChecked()){
                    linearaddTarjeta.setVisibility(View.VISIBLE);
                    Toast.makeText(ReservacionActivity.this, "lqwrnflqnrwflkqwnrflkqwmflqwkm", Toast.LENGTH_SHORT).show();
                }else if(btnPaypal.isChecked()){
                    linearaddTarjeta.setVisibility(View.GONE);
                    linearpayTarjeta.setVisibility(View.GONE);
                    linearImagenes.setVisibility(View.GONE);
                    Toast.makeText(ReservacionActivity.this, "z-z-zz-z-z-z-z-z-z-z-", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnVerTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearaddTarjeta.setVisibility(View.VISIBLE);
                linearpayTarjeta.setVisibility(View.GONE);
            }
        });


        btnAddTarj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearaddTarjeta.setVisibility(View.GONE);
                linearpayTarjeta.setVisibility(View.VISIBLE);
            }
        });


        ArrayList<String> viajes = new ArrayList<String>();
        viajes.add( "Viaje de negocios" ); // 001
        viajes.add( "Viaje de placer" ); // 015
        SpinnerAdapter adapterViaje = new ArrayAdapter<String>( ReservacionActivity.this, R.layout.habitaciones_item, R.id.txtOption, viajes );
        Spinner spinViaje = (Spinner) findViewById( R.id.spinViaje );
        spinViaje.setAdapter( adapterViaje );



        _hotel = ResultadosDisponibilidad.listaGeneralHotel.get(0);
        lblHotelName.setText( _hotel.getNombre() );
        lblArrivalDate.setText("12-12-2014");
        lblDepartureDate.setText("13-06-2015");
        lblHotelName2.setText( _hotel.getNombre() );
        lblArrivalDate2.setText("12-12-2014");
        lblDepartureDate2.setText("12-08-2014");


        lblTotal.setText("Total: $678.9");//String.format( "Total: $%,.2f ",1123 ) );
        lblTotal2.setText("Total: $678.9");// String.format( "Total: $%,.2f ", 34345 )  );


      //  NestedListView list = (NestedListView) findViewById( R.id.list_summary );
       // NestedListView list2 = (NestedListView) findViewById( R.id.list_summary2 );


        Button btnReserva2 = (Button) findViewById(R.id.btnReserva);


        /*SummaryListAdapter adapter = new SummaryListAdapter( this, sumary );
        list.setAdapter( adapter );
        list2.setAdapter( adapter );

        list.setAdapter(adapter);
        //list2.setAdapter(adapter);*/


        btnReserva2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("".equals(txtName.getText().toString())){

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo Nombre no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(txtLast.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo Apellido no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(txtEmail.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo Correo Electrónico no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if(!cbAfiliate.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("Hubo un problema al tratar de hacer tu reservación, vuelve a intentarlo")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(txtPhone.getText().toString())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionActivity.this);
                        builder.setTitle("Hoteles City")
                                .setMessage("El campo Teléfono no puede estar vacío")
                                .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                    RecibirDatos();
                }
            }
        });




    }

    public void RecibirDatos(){

            txtName.getText();
            txtLast.getText();
            txtEmail.getText();
            //txtSocio.getText();
            cbAfiliate.isChecked();
            txtPhone.getText();
            spinViaje.getSelectedItemPosition();
            spinAdultos.getSelectedItemPosition();
            spinNinos.getSelectedItemPosition();

        System.out.println(txtName.getText().toString());
        System.out.println(txtLast.getText().toString());
        System.out.println(txtPhone.getText().toString());
       // System.out.println(spinAdultos.getSelectedItem().toString());
       // System.out.println(spinViaje.getSelectedItem().toString());
       // System.out.println(spinNinos.getSelectedItem().toString());


    }

    public void enviarReservacion(){

            cadena = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "    <s:Body>\n" +
                    "        <InsertBookingv3_01 xmlns=\"http://tempuri.org/\">\n" +
                    "            <mdlReservation xmlns:d4p1=\"http://schemas.datacontract.org/2004/07/CityHub\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "                <d4p1:AplicaSMART>\n" +
                    "                    <d4p1:pAplicaSMART>false</d4p1:pAplicaSMART>\n" +
                    "                </d4p1:AplicaSMART>\n" +
                    "                <d4p1:Deposito>\n" +
                    "                    <d4p1:Comprobante>SmartComprobante</d4p1:Comprobante>\n" +
                    "                    <d4p1:Fecha>2015-12-18</d4p1:Fecha>\n" +
                    "                    <d4p1:Monto>854.05</d4p1:Monto>\n" +
                    "                    <d4p1:Notas></d4p1:Notas>\n" +
                    "                    <d4p1:Notas2></d4p1:Notas2>\n" +
                    "                    <d4p1:Tarifa_Fija>0</d4p1:Tarifa_Fija>\n" +
                    "                    <d4p1:TipoMoneda>MXN</d4p1:TipoMoneda>\n" +
                    "                </d4p1:Deposito>\n" +
                    "                <d4p1:Empresa>\n" +
                    "                    <d4p1:IataEmisor />\n" +
                    "                    <d4p1:RfcEmisor>EMISAPP</d4p1:RfcEmisor>\n" +
                    "                </d4p1:Empresa>\n" +
                    "                <d4p1:Estancia>\n" +
                    "                    <d4p1:ChannelRef />\n" +
                    "                    <d4p1:CodigoOperador>APP_MOVIL</d4p1:CodigoOperador>\n" +
                    "                    <d4p1:CodigoOrigen>007</d4p1:CodigoOrigen>\n" +
                    "                    <d4p1:CodigoSegmento>001</d4p1:CodigoSegmento>\n" +
                    "                    <d4p1:FechaEntrada>2015-12-19</d4p1:FechaEntrada>\n" +
                    "                    <d4p1:FormaDePago>VISA</d4p1:FormaDePago>\n" +
                    "                    <d4p1:Hotel>CEAGU</d4p1:Hotel>\n" +
                    "                    <d4p1:NotasFormaPago />\n" +
                    "                    <d4p1:NotasReservacion />\n" +
                    "                    <d4p1:NumeroDeNoches>1</d4p1:NumeroDeNoches>\n" +
                    "                    <d4p1:TipoReservacion>TCRED</d4p1:TipoReservacion>\n" +
                    "                </d4p1:Estancia>\n" +
                    "                <d4p1:Habitacion>\n" +
                    "                    <d4p1:CodigoHabitacion>NSD</d4p1:CodigoHabitacion>\n" +
                    "                    <d4p1:CodigoPromocion></d4p1:CodigoPromocion>\n" +
                    "                    <d4p1:CodigoTarifa>1114</d4p1:CodigoTarifa>\n" +
                    "                    <d4p1:HuespedTitular>\n" +
                    "                        <d4p1:Apellidos>SANDOVAL</d4p1:Apellidos>\n" +
                    "                        <d4p1:Edad>0</d4p1:Edad>\n" +
                    "                        <d4p1:Nombre>FERNANDINHO</d4p1:Nombre>\n" +
                    "                        <d4p1:Acompanantes_Ar></d4p1:Acompanantes_Ar>\n" +
                    "                        <d4p1:CodigoPais>MX</d4p1:CodigoPais>\n" +
                    "                        <d4p1:CorreoElectronico>lala@gegerg.com</d4p1:CorreoElectronico>\n" +
                    "                        <d4p1:Indicaciones />\n" +
                    "                        <d4p1:RwdNumber />\n" +
                    "                        <d4p1:Telefono>12121212</d4p1:Telefono>\n" +
                    "                        <d4p1:TotAcompAdult>0</d4p1:TotAcompAdult>\n" +
                    "                        <d4p1:TotAcompMenor>0</d4p1:TotAcompMenor>\n" +
                    "                    </d4p1:HuespedTitular>\n" +
                    "                    <d4p1:NumeroHabitaciones>1</d4p1:NumeroHabitaciones>\n" +
                    "                </d4p1:Habitacion>\n" +
                    "                <d4p1:Reservante>\n" +
                    "                    <d4p1:Apellidos>SANDOVAL</d4p1:Apellidos>\n" +
                    "                    <d4p1:Edad>0</d4p1:Edad>\n" +
                    "                    <d4p1:Nombre>FERNANDINHO</d4p1:Nombre>\n" +
                    "                    <d4p1:CorreoElectronico>lala@gegerg.com</d4p1:CorreoElectronico>\n" +
                    "                    <d4p1:RwdNumber />\n" +
                    "                    <d4p1:TelefonoReservante>12121212</d4p1:TelefonoReservante>\n" +
                    "                </d4p1:Reservante>\n" +
                    "                <d4p1:SMART>\n" +
                    "                    <d4p1:CVV/>\n" +
                    "                    <d4p1:Clasificacion_Transaccion/>\n" +
                    "                    <d4p1:CuentaId/>\n" +
                    "                    <d4p1:Host/>\n" +
                    "                    <d4p1:PuntoVenta/>\n" +
                    "                    <d4p1:SMART_Moneda/>\n" +
                    "                    <d4p1:SMART_Pais/>\n" +
                    "                    <d4p1:Tipo_Cliente/>\n" +
                    "                </d4p1:SMART>\n" +
                    "                <d4p1:TarjetaCredito>\n" +
                    "                    <d4p1:AnoVencimiento>2024</d4p1:AnoVencimiento>\n" +
                    "                    <d4p1:CodigoSeguridad>123</d4p1:CodigoSeguridad>\n" +
                    "                    <d4p1:MesVencimiento>01</d4p1:MesVencimiento>\n" +
                    "                    <d4p1:NombreTarjeta>HACHAD HJBDJHB</d4p1:NombreTarjeta>\n" +
                    "                    <d4p1:NumeroTarjeta>4111111111111111</d4p1:NumeroTarjeta>\n" +
                    "                </d4p1:TarjetaCredito>\n" +
                    "                <d4p1:TarjetaVirtual>\n" +
                    "                    <d4p1:AnoVencimiento />\n" +
                    "                    <d4p1:CodigoSeguridad />\n" +
                    "                    <d4p1:MesVencimiento />\n" +
                    "                    <d4p1:NombreTarjeta />\n" +
                    "                    <d4p1:NumeroTarjeta />\n" +
                    "                </d4p1:TarjetaVirtual>\n" +
                    "            </mdlReservation>\n" +
                    "        </InsertBookingv3_01>\n" +
                    "    </s:Body>\n" +
                    "</s:Envelope>";


            StringRequest registro = new StringRequest(Request.Method.POST, "http://wshc.hotelescity.com:9742/wsMotor2014/ReservationEngine.svc", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    InputStream stream = null;
                    try {
                        stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                        parseXML(stream);

                    } catch (UnsupportedEncodingException e) {
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
                        if (tagname.equalsIgnoreCase("InsertBookingv3_01Result")) {
                            // create a new instance of employee
                            //employee = new Disponibilidad();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("CodigoError")) {

                        }else if(tagname.equalsIgnoreCase("DescError")){

                        } else if(tagname.equalsIgnoreCase("ErrorMessage")){

                        }else if (tagname.equalsIgnoreCase("NumReservacion")) {
                            // employee.setName(text);
                        } else if (tagname.equalsIgnoreCase("SMART_AutCode")) {
                            //employee.setId(text);
                        } else if (tagname.equalsIgnoreCase("SMART_Error")) {
                            //employee.setDepartment(text);
                        } else if (tagname.equalsIgnoreCase("SMART_NumAfiliacion")) {
                        } else if (tagname.equalsIgnoreCase("SMART_RefSPNum")) {
                        } else if (tagname.equalsIgnoreCase("SMART_Resp_Code")) {
                        } else if (tagname.equalsIgnoreCase("SMART_Resp_Message")) {
                        } else if (tagname.equalsIgnoreCase("TotalRate")) {
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
        }

        //return habitacionBaseList;
    }


    public void obtenerListadoTarjetas(){
        Log.d("ReservacionActivity", "obtener listado tarjetas");

/*
        String sampleXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<mobilegate>"
                +"<timestamp>232423423423</timestamp>"
                + "<txn>" + "Transaction" + "</txn>"
                + "<amt>" + 0 + "</amt>"
                + "</mobilegate>";


        JSONObject jsonObj = null;
        JSONObject mobilegate = null;

        try {
            jsonObj = XML.toJSONObject(sampleXml);
            mobilegate = jsonObj.getJSONObject("mobilegate");

            Log.d("XML", sampleXml);

            Log.d("JSON", jsonObj.toString());


            Log.d("Reservaion", "Valor :3 " + mobilegate.getString("txn") );


        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }

*/
        String enviarxml = "<soapenv:Envelope\n" +
                "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "    xmlns:tem=\"http://tempuri.org/\"\n" +
                "    xmlns:cit=\"http://schemas.datacontract.org/2004/07/CityHub\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <ListadoTarjetas \n" +
                "            xmlns=\"http://tempuri.org/\">\n" +
                "            <IdClienteF2G>{IDF2GO}</IdClienteF2G>\n" +
                "        </ListadoTarjetas >\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        enviarxml = enviarxml.replace("{IDF2GO}","1977012");


        Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);

        System.out.println("XML a enviar --> " + enviarxml);


        final String finalEnviarxml = enviarxml;
        StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.CLIENTE_UNICO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                    try {

                        JSONObject jsonObj = null;
                        JSONObject body = null;
                        JSONObject ListadoTarjetasResult = null;

                        jsonObj = XML.toJSONObject(response);
                        body = jsonObj.getJSONObject("soap:Envelope").getJSONObject("soap:Body");
                        ListadoTarjetasResult = body.getJSONObject("ListadoTarjetasResponse").getJSONObject("ListadoTarjetasResult");

                        Log.d("JSON", ListadoTarjetasResult.toString());

                        JSONArray arreglo = ListadoTarjetasResult.getJSONArray("CardField");

/*
                        for (JSONObject tempsmartcard : arreglo ) {
                            // do some work here on intValue
                            System.out.println("Nombre de tarjeta " + tempsmartcard.getString("") );
                        }
*/

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
                return  temp.toString().getBytes();
            }

        };

        System.out.println("registro->" + registro.toString());
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);



    }

}
