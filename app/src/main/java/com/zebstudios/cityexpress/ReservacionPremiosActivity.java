package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edwinhernandez on 17/12/15.
 */
public class ReservacionPremiosActivity extends Activity {

    Hotel _hotel;
    private ArrayList<HabitacionBase> _rooms;
    ArrayList<SmartCard> tarjetas_list;
    ArrayList<String> numTarjeta;
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

    EditText txtCardNumb;
    EditText txtCardName;
    EditText txtCvv;

    double precioHabitacion;
    SegmentedGroup segmentswitch;

    RadioButton btnTarjeta;
    RadioButton btnPaypal;


    LinearLayout linearaddTarjeta;
    LinearLayout linearAgregarTarjeta;


    Button btnaddTarjeta;
    Button btnTarjetas;
    Button btnGuardarTarjeta;


    String fechaVencimiento;
    String mesVencimiento;

    ReservaNumeroHabitacionesAdapter adapter;
    ArrayList<ItemListReserva> data = new ArrayList<>();

    int posicion;
    int posicionHot;
    int posicionHab;
    int numHabitacion;
    int numNoches;
    String codigoBase;
    Date arrival;
    Date departure;
    String precio;

    String personF2GO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        numTarjeta = new ArrayList<String>();
        this.obtenerListadoTarjetas();


        SharedPreferences prefsUsuario = this.getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
        personF2GO = prefsUsuario.getString("person_ID", null);

        if(personF2GO==null){
            Intent i = new Intent(this, IniciarSesionActivity.class);
            startActivityForResult(i, 1);
        }

        setContentView(R.layout.activity_reservacion_city);

        TextView lblHotelName = (TextView) findViewById(R.id.lblHotelName);
        TextView lblArrivalDate = (TextView) findViewById( R.id.dates_arrival_text );
        TextView lblDepartureDate = (TextView) findViewById( R.id.dates_departure_text );
        TextView lblTotal = (TextView) findViewById(R.id.lblTotal);
        TextView lblHotelName2 = (TextView) findViewById( R.id.lblHotelName2 );
        TextView lblArrivalDate2 = (TextView) findViewById( R.id.dates_arrival_text2 );
        TextView lblDepartureDate2 = (TextView) findViewById( R.id.dates_departure_text2 );
        TextView lblTotal2 = (TextView) findViewById(R.id.lblTotal2);

        txtCardName = (EditText) findViewById(R.id.txtCardNumber);
        txtCardNumb = (EditText) findViewById(R.id.txtCardName);
        txtCvv = (EditText) findViewById(R.id.txtCardCode);

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

        linearaddTarjeta = (LinearLayout) findViewById(R.id.pnlCCPayment);
        linearAgregarTarjeta = (LinearLayout) findViewById(R.id.linearAddtarjeta);

        btnGuardarTarjeta = (Button) findViewById(R.id.guardarTarjeta);

        segmentswitch = (SegmentedGroup) findViewById(R.id.segmentedPaymentMethod);
        btnTarjeta = (RadioButton)findViewById(R.id.btn_method_card);
        btnPaypal = (RadioButton) findViewById(R.id.btn_method_paypal);


        btnaddTarjeta = (Button) findViewById(R.id.btnAddTarjeta);
        btnTarjetas = (Button) findViewById(R.id.verTarjetas);



        Bundle bundle = getIntent().getExtras();
        posicionHot = bundle.getInt("posicionHotel");
        codigoBase = bundle.getString("codigoBase");
        posicionHab= bundle.getInt("posicionHabitacion");
        numHabitacion = bundle.getInt("numHabitacion");
        arrival = PrincipalFragment._arrivalDate;
        departure = PrincipalFragment._departureDate;
        numNoches = ResultadosDisponibilidad.totalNoches;
        precioHabitacion = Double.parseDouble(ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getArrayHabitaciones().get(posicionHab).getCosto().replace(",", ""));

        System.out.println("numNoches"+numNoches);
        _hotel = ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot);
        ResultadosDisponibilidad.habitacionBaseList.get(posicionHab);

        System.out.println("HotelPosicionPrecioListado->"+precioHabitacion);
        if(bundle.getBoolean("city")){
            precio = bundle.getString("precioPremio");
        }else{
            precio =  bundle.getString("precioDestino");
        }

        System.out.println("PsoicionHotel"+posicionHot);
        System.out.println("PsoiconHabitacion"+posicionHab);
        System.out.println("arrival"+arrival);
        System.out.println("departure"+departure);
        System.out.println("codigoBase"+codigoBase);


        tarjetas_list = new ArrayList<>();

        segmentswitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(btnTarjeta.isChecked()){
                    linearaddTarjeta.setVisibility(View.VISIBLE);
                }else if(btnPaypal.isChecked()){
                    linearAgregarTarjeta.setVisibility(View.GONE);
                    linearaddTarjeta.setVisibility(View.GONE);
                }

            }
        });


        ArrayList<String> viajes = new ArrayList<String>();
        viajes.add( "Viaje de negocios" ); // 001
        viajes.add( "Viaje de placer" ); // 015
        SpinnerAdapter adapterViaje = new ArrayAdapter<String>( ReservacionPremiosActivity.this, R.layout.habitaciones_item, R.id.txtOption, viajes );
        Spinner spinViaje = (Spinner) findViewById( R.id.spinViaje );
        spinViaje.setAdapter( adapterViaje );


        final ArrayList<String> months = new ArrayList<String>();
        months.add( "Mes" );
        for( int i = 1; i < 13; i++ )
        {
            if( i < 10 )
            {
                months.add( "0" + i );
            }
            else
            {
                months.add( "" + i );
            }
        }

        SpinnerAdapter adapterMonths = new ArrayAdapter<String>( ReservacionPremiosActivity.this , R.layout.habitaciones_item, R.id.txtOption, months );
        Spinner spinMonths = (Spinner) findViewById( R.id.spinExpMonth );
        spinMonths.setAdapter(adapterMonths);
        spinMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(fechaVencimiento==""){
                    mesVencimiento = "";
                }
                mesVencimiento = months.get((position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // TODO: Cuántos años en el select de años de la tarjeta?
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        final ArrayList<String> years = new ArrayList<String>();
        years.add( "Año" );
        for( int i = 0; i < 15; i++ )
        {
            years.add( "" + ( year + i ) );
        }
        SpinnerAdapter adapterYears = new ArrayAdapter<String>(ReservacionPremiosActivity.this , R.layout.habitaciones_item, R.id.txtOption, years );
        final Spinner spinYears = (Spinner) findViewById(R.id.spinExpYear);
        spinYears.setAdapter(adapterYears);
        spinYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    fechaVencimiento = "";
                }else{
                    fechaVencimiento=years.get((position+1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Log.d( "TEST", "MAX: " + _room.getMaxAdultos() );

        ArrayList<String> adultos = new ArrayList<String>();
        ArrayList<String> ninos = new ArrayList<String>();
/*
        for( int i = 0; i <= _room.getMaxAdultos() - 1; i++ )
        {
            adultos.add( "" + i );
        }
        for( int i = 0; i <= _room.getMaxAdultos() - 1; i++ )
        {
            ninos.add( "" + i );
        }
*/
        adultos.add("0");
        adultos.add("1");
        ninos.add("0");
        ninos.add("1");

        Spinner spinAdultos = (Spinner) findViewById( R.id.spinAdultos );
        SpinnerAdapter adapterAdultos = new ArrayAdapter<String>( ReservacionPremiosActivity.this, R.layout.habitaciones_item, R.id.txtOption, adultos );
        spinAdultos.setAdapter( adapterAdultos );
        spinAdultos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //changeAdultos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinNinos = (Spinner) findViewById( R.id.spinNinos );
        SpinnerAdapter adapterNinos = new ArrayAdapter<String>( ReservacionPremiosActivity.this, R.layout.habitaciones_item, R.id.txtOption, ninos );
        spinNinos.setAdapter(adapterNinos);

        btnaddTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearAgregarTarjeta.setVisibility(View.VISIBLE);
                linearaddTarjeta.setVisibility(View.GONE);
            }
        });

        btnTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearaddTarjeta.setVisibility(View.VISIBLE);
                linearAgregarTarjeta.setVisibility(View.GONE);
            }
        });

        btnGuardarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(txtCardName.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo Nombre no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(txtCardNumb.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo Número de tarjeta no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(txtCvv.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo CVV no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equalsIgnoreCase(fechaVencimiento)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El año no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equalsIgnoreCase(mesVencimiento)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El mes no puede estar vacío")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


                else{
                    txtCardName.getText();
                    txtCardNumb.getText();
                    txtCvv.getText();
                    enviarTarjeta(txtCardNumb.getText().toString(),txtCardName.getText().toString(),fechaVencimiento,"pruabs2",personF2GO);
                    Toast.makeText(ReservacionPremiosActivity.this, "Tarjeta Guardada", Toast.LENGTH_SHORT).show();
                }
            }
        });



        SimpleDateFormat sdfecha = new SimpleDateFormat( "dd-MM-yyyy" );
        sdfecha.format(departure);


        _hotel = ResultadosDisponibilidad.listaGeneralHotel.get(0);
        lblHotelName.setText( _hotel.getNombre() );
        lblArrivalDate.setText(sdfecha.format(arrival));
        lblDepartureDate.setText(sdfecha.format(departure));
        lblHotelName2.setText( _hotel.getNombre() );
        lblArrivalDate2.setText(sdfecha.format(arrival));
        lblDepartureDate2.setText(sdfecha.format(departure));



        lblTotal.setText(String.format( "Total: $%,.2f ", getTotalCost() ) + "MXN");//String.format( "Total: $%,.2f ",1123 ) );
        lblTotal2.setText(String.format( "Total: $%,.2f ", getTotalCost()) + "MXN" );// String.format( "Total: $%,.2f ", 34345 )  );



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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
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


        ArrayList<String> huespedes = new ArrayList<String>();
        ArrayList<SummaryEntry> sumary = new ArrayList<SummaryEntry>();

        for( int i = 0; i < numHabitacion; i++ )
        {
            huespedes.add( "Huesped titular - habitación " + ( i + 1 ) );
            sumary.add( new SummaryEntry( 0, "Habitación " + ( i + 1 ) ) );
            for( int j = 0; j < Math.abs(numNoches); j++ )
            {
                sumary.add( new SummaryEntry( 1, "Noche " + ( j + 1 ) + " $"+precioHabitacion));
            }

        }

        NestedListView list = (NestedListView) findViewById(R.id.list_summary);
        NestedListView list2 = (NestedListView) findViewById( R.id.list_summary2 );
        SummaryListAdapter adapter = new SummaryListAdapter( this, sumary );
        list.setAdapter( adapter );
        list2.setAdapter( adapter );

        SpinnerAdapter adapterHuespedes = new ArrayAdapter<String>( this, R.layout.habitaciones_item, R.id.txtOption, huespedes );
        Spinner spinHuespedes = (Spinner) findViewById(R.id.spinHuespedes);
        spinHuespedes.setAdapter( adapterHuespedes );
        spinHuespedes.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
            {

            }

            @Override
            public void onNothingSelected( AdapterView<?> parent )
            {
                // FATAL: No debería ocurrir
            }
        } );

        RadioButton btnMisma = (RadioButton) findViewById(R.id.btn_rad_misma);
        RadioButton btnOtras = (RadioButton) findViewById(R.id.btn_rad_otros);
        btnMisma.setChecked( true );
        btnMisma.setEnabled( false );
        btnOtras.setEnabled( false );
        SegmentedGroup segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented);
        segmentedGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( RadioGroup group, int checkedId )
            {

            }
        } );




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(resultCode == Activity.RESULT_OK){
                personF2GO=data.getStringExtra("result");
            }
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

                    for (int i = 0; i < arreglo.length(); i++) {
                        // do some work here on intValue
                        JSONObject tempsmartcard = arreglo.getJSONObject(i);
                        //System.out.println("Nombre de tarjeta " + tempsmartcard.getString("OwnerName") + " - " + tempsmartcard.getString("Alias") );

                        SmartCard temp = new SmartCard();
                        temp.loadinfo(tempsmartcard);
                        System.out.println("Nombre de tarjeta " + temp.Alias + " - " + temp.IdCard + " - " + temp.OwnerName);

                        numTarjeta.add(temp.CardNumber);
                        tarjetas_list.add(temp);

                    }
                    SpinnerAdapter adapterTajetas = new ArrayAdapter<String>( getBaseContext(), R.layout.habitaciones_item, R.id.txtOption, numTarjeta );
                    Spinner spinTarjetas = (Spinner) findViewById(R.id.spinTarjetas);
                    spinTarjetas.setAdapter( adapterTajetas );
                    spinTarjetas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // FATAL: No debería ocurrir
                        }
                    });

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

    private double getTotalCost()
    {
        double total = 0;
        for( int i = 0; i < Math.abs(numNoches) ; i++ )
        {
            total += precioHabitacion;
        }

        return (total*numHabitacion);
    }


    public void enviarTarjeta(String tarjeta, String nombre, String fechaexpiracion, String alias, String idF2GO){
        Log.d("ReservacionActivity", "Enviar Tarjeta ");


        String enviarxml = "<soapenv:Envelope\n" +
                "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "    xmlns:tem=\"http://tempuri.org/\"\n" +
                "    xmlns:cit=\"http://schemas.datacontract.org/2004/07/CityHub\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <AltaTarjeta\n" +
                "            xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "            xmlns=\"http://tempuri.org/\">\n" +
                "            <NumeroCuenta>{NumeroCuenta}</NumeroCuenta>\n" +
                "            <NombreCliente>{NombreCliente}</NombreCliente>\n" +
                "            <FechaExpiracion>{FechaExpiracion}</FechaExpiracion>\n" +
                "            <AliasCuenta>{AliasCuenta}</AliasCuenta>\n" +
                "            <IDCliente>{IDCliente}</IDCliente>\n" +
                "        </AltaTarjeta>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        enviarxml = enviarxml.replace("{NumeroCuenta}",tarjeta);
        enviarxml = enviarxml.replace("{NombreCliente}",nombre);
        enviarxml = enviarxml.replace("{FechaExpiracion}",fechaexpiracion);
        enviarxml = enviarxml.replace("{AliasCuenta}",alias);
        enviarxml = enviarxml.replace("{IDCliente}",idF2GO);


        Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);

        System.out.println("XML a enviar --> " + enviarxml);


        final String finalEnviarxml = enviarxml;
        StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.CLIENTE_UNICO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                try {

                    JSONObject jsonObj = null;
                    JSONObject body = null;
                    JSONObject AltaTarjetaResponse = null;

                    jsonObj = XML.toJSONObject(response);


                    body = jsonObj.getJSONObject("soap:Envelope").getJSONObject("soap:Body");
                    AltaTarjetaResponse = body.getJSONObject("AltaTarjetaResponse");

                    if ( AltaTarjetaResponse.has("AltaTarjetaResult") ){
                        ErrorenviarTarjeta( AltaTarjetaResponse.getString("AltaTarjetaResult") );
                        return;
                    }


                    Log.d("JSON alta tarjeta", jsonObj.toString());

                    //AltaTarjetaResult

                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }

                FinenviarTarjeta();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                NetworkResponse response = error.networkResponse;
                String datos = new String(response.data);
                System.out.println("sout" + datos);
                ErrorenviarTarjeta("error");
            }
        }) {

            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/xml; charset=utf-8");
                params.put("SOAPAction", "http://tempuri.org/AltaTarjeta");
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


    public void FinenviarTarjeta(){
        System.out.println("Tarjeta enviada con exito :D");
    }

    public void ErrorenviarTarjeta(String mensaje){
        System.out.println("ERROR AL AÑADIR TARJETA  " + mensaje );
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
        builder.setTitle("City Express")
                .setMessage(mensaje)
                .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
