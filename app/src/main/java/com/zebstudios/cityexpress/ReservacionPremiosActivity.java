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
import android.widget.ImageView;
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

import io.realm.Realm;

/**
 * Created by edwinhernandez on 17/12/15.
 */
public class ReservacionPremiosActivity extends Activity {

    ArrayList<SmartCard> tarjetas_list;
    ArrayList<String> numTarjeta;
    EditText txtCardNumb;
    EditText txtCardName;
    EditText txtCvv;
    EditText txtCvvTarjetaLista;
    LinearLayout linearAgregarTarjeta;
    Button btnaddTarjeta;
    Button btnTarjetas;
    Button btnGuardarTarjeta;
    String fechaVencimiento;
    int yearVencimiento;
    String mesVencimiento;
    String personF2GO;
    String hostUsuario;
    String tarjetaID;
    String cvvTarjetaLista;
    Hotel _hotel;
    private ArrayList<HabitacionBase> _rooms;
    String cadena;
    String text;
    ArrayList<HabitacionBase> habitacionBaseArrayList;
    RadioButton btnMisma;
    RadioButton btnOtros;
    EditText txtName;
    EditText txtLast;
    EditText txtEmail;
    EditText txtSocio;
    EditText txtAlias;
    CheckBox cbAfiliate;
    EditText txtPhone;
    Spinner spinViaje;
    Spinner spinAdultos;
    Spinner spinNinos;
    ImageView imageBack;
    double precioHabitacion;
    ArrayList<GuestData> titulares;
    SegmentedGroup segmentswitch;
    RadioButton btnTarjeta;
    RadioButton btnPaypal;
    ListView listaHabitaciones;
    LinearLayout linearpayTarjeta;
    LinearLayout linearaddTarjeta;
    LinearLayout linearImagenes;
    ReservaNumeroHabitacionesAdapter adapter;
    ArrayList<ItemListReserva> data = new ArrayList<>();
    int posicion;
    int posicionHot;
    int posicionHab;
    int numHabitacion;
    int numNoches;
    String codigoBase;
    String descripcionHabitacionJSON;
    Date arrival;
    Date departure;
    String precio;
    private int _lastGuestIndex;

    int contador;

    Realm realm;

    //DAtosUsuario City;
    String nombreUsuarioCity;
    String apellidoUsuarioCity;
    String correoUsuarioCity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        numTarjeta = new ArrayList<String>();
        this.obtenerListadoTarjetas();

        obtenerHostUsuario();
        SharedPreferences prefsUsuario = this.getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
        personF2GO = prefsUsuario.getString("person_ID", null);
        nombreUsuarioCity = prefsUsuario.getString("nombre", null);
        apellidoUsuarioCity = prefsUsuario.getString("apellido", null);
        correoUsuarioCity = prefsUsuario.getString("usuario", null);

        System.out.println("NOMBREUSUARIO"+ nombreUsuarioCity + "apellidoUsuario "+ apellidoUsuarioCity + "CorreoUsuario" + correoUsuarioCity);
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


        txtCardName = (EditText) findViewById(R.id.txtCardName);
        txtCardNumb = (EditText) findViewById(R.id.txtCardNumber);
        txtAlias = (EditText) findViewById(R.id.txtAlias);
        txtCvv = (EditText) findViewById(R.id.txtCardCode);
        txtCvvTarjetaLista = (EditText) findViewById(R.id.txtcvv);
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
        posicionHab = bundle.getInt("posicionHabitacion");
        numHabitacion = bundle.getInt("numHabitacion");
        descripcionHabitacionJSON = bundle.getString("descipcionHabitacionJSON");

        arrival = PrincipalFragment._arrivalDate;
        departure = PrincipalFragment._departureDate;
        numNoches = ResultadosDisponibilidad.totalNoches;
        numNoches = Math.abs(numNoches);
        precioHabitacion = Double.parseDouble(ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getArrayHabitaciones().get(posicionHab).getCosto().replace(",", ""));
        _lastGuestIndex = 0;
        System.out.println("numNoches" + numNoches);
        _hotel = ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot);
        ResultadosDisponibilidad.habitacionBaseList.get(posicionHab);


        if (bundle.getBoolean("city")) {
            precio = bundle.getString("precioPremio");
            precioHabitacion = Double.parseDouble(precio);
        } else {
            precio = bundle.getString("precioDestino");
        }
        System.out.println("HotelPosicionPrecioListado->" + precioHabitacion);
        System.out.println("PsoicionHotel" + posicionHot);
        System.out.println("PsoiconHabitacion" + posicionHab);
        System.out.println("arrival" + arrival);
        System.out.println("departure" + departure);
        System.out.println("codigoBase" + codigoBase);


        contador = 0;


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
        titulares = new ArrayList<>();

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
                    fechaVencimiento=years.get((position + 1));
                    yearVencimiento = (Integer.parseInt(fechaVencimiento)-2000);

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
                            .setMessage("Por favor ingresa el nombre del titular de la tarjeta de crédito.")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if( !CCUtils.validCC( txtCardNumb.getText().toString() ) )
                {
                    alert("El número de tarjeta es inválido.");
                }EditText txtCardCode = (EditText) findViewById( R.id.txtCardCode );
                if( txtCardCode.getText().toString().trim().length() != 3 || !isNumeric( txtCardCode.getText().toString().trim() ) )
                {
                    alert("El código de validación de la tarjeta es incorrecto. Ingresa los últimos tres dígitos del número que se encuentra en la parte posterior de la tarjeta.");
                }else if("".equalsIgnoreCase(fechaVencimiento)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("Selecciona el año de expiración de la tarjeta de crédito.")
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
                            .setMessage("Selecciona el mes de expiración de la tarjeta de crédito.")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


                else{
                    System.out.println("AÑO/MES" + yearVencimiento+mesVencimiento);
                    txtCardName.getText();
                    txtCardNumb.getText();
                    txtCvv.getText();
                    enviarTarjeta(txtCardNumb.getText().toString(), txtCardName.getText().toString(), yearVencimiento + mesVencimiento, txtAlias.getText().toString(), personF2GO);
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
                saveCurrentGuest();
                System.out.println("Codigo CVV->" + txtCvvTarjetaLista.getText().toString());
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
                }else if("".equalsIgnoreCase(txtCvvTarjetaLista.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El Codigo de seguridad de la tarjeta no puedo estar vacio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                else{
                    if (validateCapture()) {
                        for (int i = 0; i < titulares.size(); i++) {
                            changeHuesped(i);
                            System.out.println("Titular " + i + "---->Nombre" + titulares.get(i).getName() + "TAmaño ZisE->"+ titulares.size());
                        }

                        System.out.println("Lo Logre!!!!!");
                        RecibirDatos();
                    }
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
            titulares.add(new GuestData());
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
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
                changeHuesped(position);
            }

            @Override
            public void onNothingSelected( AdapterView<?> parent ) {
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
                handleCaptureOptionHandle();
            }
        } );


        System.out.println("Siglas de hotel->"+_hotel.getSiglas());

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

        cvvTarjetaLista =  txtCvvTarjetaLista.getText().toString();
        System.out.println(txtName.getText().toString());
        System.out.println(txtLast.getText().toString());
        System.out.println(txtPhone.getText().toString());
        // System.out.println(spinAdultos.getSelectedItem().toString());
        // System.out.println(spinViaje.getSelectedItem().toString());
        // System.out.println(spinNinos.getSelectedItem().toString());
        enviarReservacion();


    }

    public void enviarReservacion(){
        Log.d("ReservacionActivity", "Enviar Tarjeta ");

        for (int i = 0; i < titulares.size(); i++) {
            String enviarxml = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "    <s:Body>\n" +
                    "        <InsertBookingv3_01 xmlns=\"http://tempuri.org/\">\n" +
                    "            <mdlReservation xmlns:d4p1=\"http://schemas.datacontract.org/2004/07/CityHub\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "                <d4p1:AplicaSMART>\n" +
                    "                    <d4p1:pAplicaSMART>{smart}</d4p1:pAplicaSMART>\n" +
                    "                </d4p1:AplicaSMART>\n" +
                    "                <d4p1:Deposito>\n" +
                    "                    <d4p1:Comprobante>SmartComprobante</d4p1:Comprobante>\n" +
                    "                    <d4p1:Fecha>{depositofecha}</d4p1:Fecha>\n" +
                    "                    <d4p1:Monto>{depositomonto}</d4p1:Monto>\n" +
                    "                    <d4p1:Notas>Nota1</d4p1:Notas>\n" +
                    "                    <d4p1:Notas2>Nota2</d4p1:Notas2>\n" +
                    "                    <d4p1:Tarifa_Fija>0</d4p1:Tarifa_Fija>\n" +
                    "                    <d4p1:TipoMoneda>{depositotipomoneda}</d4p1:TipoMoneda>\n" +
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
                    "                    <d4p1:FechaEntrada>{estanciaentrada}</d4p1:FechaEntrada>\n" +
                    "                    <d4p1:FormaDePago>{formapago}</d4p1:FormaDePago>\n" +
                    "                    <d4p1:Hotel>{codigohotel}</d4p1:Hotel>\n" +
                    "                    <d4p1:NotasFormaPago />\n" +
                    "                    <d4p1:NotasReservacion />\n" +
                    "                    <d4p1:NumeroDeNoches>{numeronoches}</d4p1:NumeroDeNoches>\n" +
                    "                    <d4p1:TipoReservacion>DEPO</d4p1:TipoReservacion>\n" +
                    "                </d4p1:Estancia>\n" +
                    "                <d4p1:Habitacion>\n" +
                    "                    <d4p1:CodigoHabitacion>{codigohabitacion}</d4p1:CodigoHabitacion>\n" +
                    "                    <d4p1:CodigoPromocion>{codigopromocion}</d4p1:CodigoPromocion>\n" +
                    "                    <d4p1:CodigoTarifa>{codigotarifa}</d4p1:CodigoTarifa>\n" +
                    "                    <d4p1:HuespedTitular>\n" +
                    "                        <d4p1:Apellidos>{huespedapellidos}</d4p1:Apellidos>\n" +
                    "                        <d4p1:Edad>32</d4p1:Edad>\n" +
                    "                        <d4p1:Nombre>{huespednombre}</d4p1:Nombre>\n" +
                    "                        <d4p1:Acompanantes_Ar>{acompanantes}</d4p1:Acompanantes_Ar>\n" +
                    "                        <d4p1:CodigoPais>{codigopais}</d4p1:CodigoPais>\n" +
                    "                        <d4p1:CorreoElectronico>{correoelectronico}</d4p1:CorreoElectronico>\n" +
                    "                        <d4p1:Indicaciones />\n" +
                    "                        <d4p1:RwdNumber />\n" +
                    "                        <d4p1:Telefono>{telefono}</d4p1:Telefono>\n" +
                    "                        <d4p1:TotAcompAdult>{totalacompadultos}</d4p1:TotAcompAdult>\n" +
                    "                        <d4p1:TotAcompMenor>{totalacompmenores}</d4p1:TotAcompMenor>\n" +
                    "                    </d4p1:HuespedTitular>\n" +
                    "                    <d4p1:NumeroHabitaciones>1</d4p1:NumeroHabitaciones>\n" +
                    "                </d4p1:Habitacion>\n" +
                    "                <d4p1:Reservante>\n" +
                    "                    <d4p1:Apellidos>{reservanteapellidos}</d4p1:Apellidos>\n" +
                    "                    <d4p1:Edad>0</d4p1:Edad>\n" +
                    "                    <d4p1:Nombre>{reservantenombre}</d4p1:Nombre>\n" +
                    "                    <d4p1:CorreoElectronico>{reservantecorreoelectronico}</d4p1:CorreoElectronico>\n" +
                    "                    <d4p1:RwdNumber />\n" +
                    "                    <d4p1:TelefonoReservante>{reservantetelefono}</d4p1:TelefonoReservante>\n" +
                    "                </d4p1:Reservante>\n" +
                    "                <d4p1:SMART>\n" +
                    "                    <d4p1:CVV>{smartcvv}</d4p1:CVV>\n" +
                    "                    <d4p1:Clasificacion_Transaccion>{smartclasificacion}</d4p1:Clasificacion_Transaccion>\n" +
                    "                    <d4p1:CuentaId>{smartcuentaid}</d4p1:CuentaId>\n" +
                    "                    <d4p1:Host>{smarthost}</d4p1:Host>\n" +
                    "                    <d4p1:PuntoVenta>2</d4p1:PuntoVenta>\n" +
                    "                    <d4p1:SMART_Moneda>{smartmoneda}</d4p1:SMART_Moneda>\n" +
                    "                    <d4p1:SMART_Pais>{smartpais}</d4p1:SMART_Pais>\n" +
                    "                    <d4p1:Tipo_Cliente>C</d4p1:Tipo_Cliente>\n" +
                    "                </d4p1:SMART>" +
                    "                <d4p1:TarjetaCredito>\n" +
                    "                    <d4p1:AnoVencimiento />\n" +
                    "                    <d4p1:CodigoSeguridad />\n" +
                    "                    <d4p1:MesVencimiento />\n" +
                    "                    <d4p1:NombreTarjeta />\n" +
                    "                    <d4p1:NumeroTarjeta />\n" +
                    "                </d4p1:TarjetaCredito>" +
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


            String llegada = new SimpleDateFormat("yyyy-MM-dd").format(arrival);

            enviarxml = enviarxml.replace("{smart}", "true");
            enviarxml = enviarxml.replace("{codigohabitacion}", codigoBase);
            enviarxml = enviarxml.replace("{codigotarifa}", "1114");
            enviarxml = enviarxml.replace("{codigopromocion}", "");


            enviarxml = enviarxml.replace("{correoelectronico}", titulares.get(i).getEmail());
            enviarxml = enviarxml.replace("{telefono}", titulares.get(i).getPhone());
            enviarxml = enviarxml.replace("{totalacompadultos}", "1");
            enviarxml = enviarxml.replace("{totalacompmenores}", "0");
            enviarxml = enviarxml.replace("{codigopais}", "MEX");
            enviarxml = enviarxml.replace("{acompanantes}", "");
            enviarxml = enviarxml.replace("{huespednombre}", titulares.get(i).getName());
            enviarxml = enviarxml.replace("{huespedapellidos}", titulares.get(i).getName());


            enviarxml = enviarxml.replace("{codigohotel}", _hotel.getSiglas());
            enviarxml = enviarxml.replace("{estanciaentrada}", llegada);
            enviarxml = enviarxml.replace("{numeronoches}", "" + numNoches);

            enviarxml = enviarxml.replace("{reservantenombre}", titulares.get(i).getName());
            enviarxml = enviarxml.replace("{reservanteapellidos}", titulares.get(i).getLastName());
            enviarxml = enviarxml.replace("{reservantecorreoelectronico}", titulares.get(i).getEmail());
            enviarxml = enviarxml.replace("{reservantetelefono}", titulares.get(i).getPhone());


            enviarxml = enviarxml.replace("{depositomonto}", precio); //Total?
            enviarxml = enviarxml.replace("{depositotipomoneda}", "MXN");
            enviarxml = enviarxml.replace("{depositofecha}", llegada);
            enviarxml = enviarxml.replace("{formapago}", "SMART");


        /*EditText txtCardName = (EditText) findViewById( R.id.txtCardName );
        EditText txtCardNumber = (EditText) findViewById( R.id.txtCardNumber );
        Spinner spinExpMonth = (Spinner) findViewById( R.id.spinExpMonth );
        Spinner spinExpYear = (Spinner) findViewById( R.id.spinExpYear );
        EditText txtCardCode = (EditText) findViewById( R.id.txtCardCode );*/


            enviarxml = enviarxml.replace("{smartcvv}", cvvTarjetaLista);
            enviarxml = enviarxml.replace("{smartclasificacion}", "16|||||||"+hostUsuario+"|"+nombreUsuarioCity+"|"+llegada+"|"+numNoches+"|1||3||BLVD. JOSE MARIA CHAVEZ |NO. 1919|COL. SAN PEDRO|AGUASCALIENTES|MX|20280|014491492900|0|||||||||||||||"+nombreUsuarioCity+"|"+apellidoUsuarioCity+"|||||||||||"+correoUsuarioCity+"|||");
            enviarxml = enviarxml.replace("{smartcuentaid}", tarjetaID);
            enviarxml = enviarxml.replace("{smarthost}", hostUsuario);
            enviarxml = enviarxml.replace("{smartmoneda}", "484");
            enviarxml = enviarxml.replace("{smartpais}", "MX");

            Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);

            System.out.println("XML a enviar --> " + enviarxml);


            final String finalEnviarxml = enviarxml;
            StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.URL_RESERVACIONES, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {

                        JSONObject jsonObj = null;
                        JSONObject body = null;
                        JSONObject InsertBookingv3_01Response = null;

                        jsonObj = XML.toJSONObject(response);

                        Log.d("JSON alta tarjeta", jsonObj.toString());


                        body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body");
                        InsertBookingv3_01Response = body.getJSONObject("InsertBookingv3_01Response").getJSONObject("InsertBookingv3_01Result");

                        if (InsertBookingv3_01Response.has("a:NumReservacion")) {

                            FinenviarReservacion("" + InsertBookingv3_01Response.getInt("a:NumReservacion"));
                            return;

                        }


                        //AltaTarjetaResult

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        e.printStackTrace();
                    }

                    FinenviarReservacion("");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    NetworkResponse response = error.networkResponse;
                    String datos = new String(response.data);
                    System.out.println("sout" + datos);
                    //ErrorenviarTarjeta("error");
                    ErrorenviarReservacion();
                }
            }) {

                public String getBodyContentType() {
                    return "text/xml; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    //params.put("Content-Type", "application/xml; charset=utf-8");
                    params.put("SOAPAction", "http://tempuri.org/IReservationEngine/InsertBookingv3_01");
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

    }


    public void FinenviarReservacion(String reservacion){
        System.out.println("Reservacion OK " + reservacion);

        ReservacionBD reservacionBD = new ReservacionBD();
        reservacionBD.setNombreUsuario(titulares.get(contador).getName());
        reservacionBD.setApellidoUsuario(titulares.get(contador).getLastName());
        reservacionBD.setNombreHotel(_hotel.getNombre());
        reservacionBD.setFechaLlegada(arrival);
        reservacionBD.setFechaSalida(departure);
        reservacionBD.setEmailHotel(_hotel.getEmail());
        reservacionBD.setNumHabitacionAsigado("");
        reservacionBD.setDescripcionLugarHotel(_hotel.getLugaresCercanos());
        reservacionBD.setSiglasHotel(_hotel.getSiglas());
        reservacionBD.setDeschabitacion(descripcionHabitacionJSON);
        reservacionBD.setLatitudHotel(_hotel.getLatitude());
        reservacionBD.setLongitudHotel(_hotel.getLongitude());
        reservacionBD.setHabCosto(""+precioHabitacion);
        reservacionBD.setNumReservacion(Integer.parseInt(reservacion));
        reservacionBD.setAdultos(1);
        reservacionBD.setInfantes(1);
        reservacionBD.setCodigoHabitacion(codigoBase);
        reservacionBD.setNumNoches(numNoches);
        reservacionBD.setNumHabitaciones(numHabitacion);
        reservacionBD.setDireccionHotel(getAddressString());
        reservacionBD.setCheckIn(false);
        reservacionBD.setCheckOut(false);
        reservacionBD.setConsultarSaldos(false);
        System.out.println("DIreccionHotel" + getAddressString());
        System.out.println("numNohe" + numNoches);
        System.out.println("descHabitacion"+ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getArrayHabitaciones().get(posicionHab).getDescBase());

        realm= Realm.getInstance(this);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(reservacionBD);
        realm.commitTransaction();
        contador++;
        if(contador==titulares.size()){
            Intent intent = new Intent(ReservacionPremiosActivity.this, HotelReservaResultActivity.class);
            intent.putExtra("numReservacion", reservacion);
            intent.putExtra("numHabitaciones", numHabitacion);
            intent.putExtra("precioHabitacion", precioHabitacion);
            intent.putExtra("numNoches",numNoches);
            intent.putExtra("total",getTotalCost());
            startActivity(intent);
        }
        System.out.println("Reservacion OK " + reservacion);


       /* AlertDialog.Builder builder = new AlertDialog.Builder(ReservacionPremiosActivity.this);
        builder.setTitle("Hoteles City")
                .setMessage("El numero de reservacion es " +reservacion)
                .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();*/
    }

    public void ErrorenviarReservacion(){

        System.out.println("Reservacion fallo D=");
    }


    public String obtenerClasificacion(GuestData titular){
        StringBuilder clasificacion = new StringBuilder();
        //Impuestos
        clasificacion.append("16");
        clasificacion.append("|");
        //CodigoCupon
        clasificacion.append("|");
        //MontoCUpon
        clasificacion.append("|");
        //CodigoLealtad
        clasificacion.append("|");
        //CodigoPromocion
        clasificacion.append("|");
        //MontoPromocion
        clasificacion.append("|");
        //DireccionIP
        clasificacion.append(hostUsuario);
        clasificacion.append("|");
        //NombreCliente
        clasificacion.append(titular.getName());
        clasificacion.append("|");
        //FechaIngreso
        SimpleDateFormat sdfecha = new SimpleDateFormat( "dd-MM-yyyy" );
        clasificacion.append(sdfecha.format(arrival));
        clasificacion.append("|");
        //NumNoches
        clasificacion.append(""+numNoches);
        clasificacion.append("|");
        //CantCuartos
        clasificacion.append("1");
        clasificacion.append("|");
        //NumeroCuarto
        clasificacion.append("|");
        //MedioReservacion
        clasificacion.append("3");
        clasificacion.append("|");
        //NombreAgenciaTercero
        clasificacion.append("|");
        //DATOSHOTEL
        //CAlle **obtener calle del hotel
        clasificacion.append("|");
        //Numero **validarNumero
        clasificacion.append("|");
        //coloniaHOtel
        clasificacion.append(_hotel.getColonia());
        clasificacion.append("|");
        //Estado/Municipio
        clasificacion.append(_hotel.getEstado());
        clasificacion.append("|");
        //Pais
        clasificacion.append(_hotel.getPais());
        clasificacion.append("|");
        //CP
        clasificacion.append(""+_hotel.getCp());
        clasificacion.append("|");
        //Telefono
        clasificacion.append(_hotel.getTelefono());
        clasificacion.append("|");
        //DatosFacturacion
        //TipoPersona
        clasificacion.append("0");
        clasificacion.append("|");
        //RFC
        clasificacion.append("|");
        //Nombre
        clasificacion.append("|");
        //apellidoPAterno
        clasificacion.append("|");
        //apellidoMateno
        clasificacion.append("|");
        //calle
        clasificacion.append("|");
        //NumeroExterioe
        clasificacion.append("|");
        //NumeroInterior
        clasificacion.append("|");
        //DelegacionMunicipio
        clasificacion.append("|");
        //Ciudad
        clasificacion.append("|");
        //Estado
        clasificacion.append("|");
        //CodigoPostal
        clasificacion.append("|");
        //Pais
        clasificacion.append("|");
        //Email
        clasificacion.append("|");
        //Telefono
        clasificacion.append("|");

        //DAtos del Cliente
        //Nombre
        clasificacion.append(titular.getName());
        clasificacion.append("|");
        //Apellido Paterno
        clasificacion.append(titular.getLastName());
        clasificacion.append("|");
        //Apellido Materno
        clasificacion.append("|");
        //Calle
        clasificacion.append("|");
        //NumeroExterior
        clasificacion.append("|");
        //NumeroInterior
        clasificacion.append("|");
        //Delegacion/Municipio
        clasificacion.append("|");
        //Ciudad
        clasificacion.append("|");
        //Estado
        clasificacion.append("|");
        //CodigoPostal
        clasificacion.append("|");
        //Pais
        clasificacion.append("|");
        //Telefono
        clasificacion.append("|");
        //Email
        clasificacion.append("lucky.strike88@hotmail.com"); //titular.get(0).getEmail();
        clasificacion.append("|");
        //Membresia
        clasificacion.append("|");
        //Fecha de Alta Membresia
        clasificacion.append("|");
        //Nivel de membresia
        clasificacion.append("|");

        return clasificacion.toString();
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

        enviarxml = enviarxml.replace("{IDF2GO}",personF2GO);


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
                            tarjetaID = tarjetas_list.get(position).IdCard;
                            System.out.println("TarjetaSelecccionada " + tarjetas_list.get(position).IdCard + "-> " + tarjetas_list.get(position).Alias + "-->"+tarjetas_list.get(position).OwnerName);
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
                        FinenviarTarjeta( AltaTarjetaResponse.getString("AltaTarjetaResult") );
                        return;
                    }


                    Log.d("JSON alta tarjeta", jsonObj.toString());

                    //AltaTarjetaResult

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


    public void FinenviarTarjeta( String mensaje){
        alert("Tarjeta agregada con éxito");
        obtenerListadoTarjetas();
        linearaddTarjeta.setVisibility(View.VISIBLE);
        linearAgregarTarjeta.setVisibility(View.GONE);
        linearaddTarjeta.setVisibility(View.GONE);
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

    public void obtenerHostUsuario(){
        StringRequest registro = new StringRequest(Request.Method.GET,"http://ipinfo.io/json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try{
                    JSONObject ip = new JSONObject(response);
                    hostUsuario = ip.getString("ip");
                    System.out.println("HostUsuairo->"+hostUsuario);
                }catch(JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                NetworkResponse response = error.networkResponse;
                String datos = new String(response.data);
                System.out.println("HostERROR" + datos);
            }
        }){
        };
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
    }

    private void clearCaptureFocus()
    {
        RadioButton btnMisma = (RadioButton) findViewById( R.id.btn_rad_misma );
        RadioButton btnOtras = (RadioButton) findViewById(R.id.btn_rad_otros);
        EditText txtName = (EditText) findViewById(R.id.txtName);
        EditText txtLast = (EditText) findViewById(R.id.txtLast);
        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        EditText txtSocio = (EditText) findViewById( R.id.txtSocio );
        CheckBox cbAfiliate = (CheckBox) findViewById(R.id.cbAfiliate);
        EditText txtPhone = (EditText)  findViewById(R.id.txtPhone);
        Spinner spinViaje = (Spinner)  findViewById(R.id.spinViaje);
        Spinner spinAdultos = (Spinner) findViewById( R.id.spinAdultos );
        Spinner spinNinos = (Spinner) findViewById( R.id.spinNinos );

        btnMisma.clearFocus();
        btnOtras.clearFocus();
        txtName.clearFocus();
        txtLast.clearFocus();
        txtEmail.clearFocus();
        txtSocio.clearFocus();
        cbAfiliate.clearFocus();
        txtPhone.clearFocus();
        spinViaje.clearFocus();
        spinAdultos.clearFocus();
        spinNinos.clearFocus();
    }

    private void changeHuesped( int index )
    {
        clearCaptureFocus();
        saveCurrentGuest();
        _lastGuestIndex = index;
        setGuestData();

        if( index == 0 )
        {
            setEnableSegmentedGroup( false );
        }
        else
        {
            setEnableSegmentedGroup( true );
        }
    }

    private void setEnableSegmentedGroup( boolean enabled )
    {
        RadioButton btnMisma = (RadioButton) findViewById(R.id.btn_rad_misma);
        RadioButton btnOtras = (RadioButton) findViewById(R.id.btn_rad_otros);
        btnMisma.setEnabled( enabled );
        btnOtras.setEnabled(enabled);
    }

    private boolean validateGuest( int index )
    {
        try {
            GuestData g = titulares.get(index);

            String title = "Huesped titular - habitación " + (index + 1);

            if (g.getName().trim().length() == 0 || g.getName().isEmpty()) {
                alert("Por favor ingresa el nombre del " + title + ".");
                return false;
            }
            if (g.getLastName().trim().length() == 0) {
                alert("Por favor ingresa el apellido del " + title + ".");
                return false;
            }
            if (g.getEmail().trim().length() == 0) {
                alert("Por favor ingresa el correo electrónico del " + title + ".");
                return false;
            }
            if (!isEmailValid(g.getEmail())) {
                alert("El correo electrónico del " + title + " es incorrecto.");
                return false;
            }
            if (g.getSocio().trim().length() != 0 && (g.getSocio().trim().length() != 10 || !isAlphaNumeric(g.getSocio().trim()))) {
                alert("El número de socio City Premios del " + title + " es incorrecto.");
                return false;
            }
            if (g.getPhone().trim().length() == 0) {
                // TODO: Validar más adecuadamente el teléfono
                alert("Por favor ingresa el teléfono del " + title + ".");
                return false;
            }
        }catch(Exception e){
            alert("Falta registrar al Huesped titular - Habitación " + (index+1));
            return false;
        }


        return true;
    }

    private boolean validateCapture()
    {
        for( int i = 0; i < titulares.size(); i++ )
        {
            if( !validateGuest( i ) )
            {
                return false;
            }
        }

      /*  EditText txtCardName = (EditText) findViewById( R.id.txtCardName );
        if( txtCardName.getText().toString().trim().length() == 0 )
        {
            alert( "Por favor ingresa el nombre del titular de la tarjeta de crédito." );
            return false;
        }

        EditText txtCardNumber = (EditText) findViewById( R.id.txtCardNumber );
        if( !CCUtils.validCC( txtCardNumber.getText().toString() ) )
        {
            alert( "El número de tarjeta es inválido." );
            return false;
        }

        Spinner spinExpMonth = (Spinner) findViewById( R.id.spinExpMonth );
        if( spinExpMonth.getSelectedItemPosition() == 0 )
        {
            alert( "Selecciona el mes de expiración de la tarjeta de crédito." );
            return false;
        }

        Spinner spinExpYear = (Spinner) findViewById( R.id.spinExpYear );
        if( spinExpYear.getSelectedItemPosition() == 0 )
        {
            alert( "Selecciona el año de expiración de la tarjeta de crédito." );
            return false;
        }

        EditText txtCardCode = (EditText) findViewById( R.id.txtCardCode );
        if( txtCardCode.getText().toString().trim().length() != 3 || !isNumeric( txtCardCode.getText().toString().trim() ) )
        {
            alert( "El código de validación de la tarjeta es incorrecto. Ingresa los últimos tres dígitos del número que se encuentra en la parte posterior de la tarjeta." );
            return false;
        }*/

        return true;
    }

    private boolean isAlphaNumeric( String s )
    {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    private boolean isNumeric( String s )
    {
        String pattern = "^[0-9]*$";
        return s.matches(pattern);
    }

    private boolean isEmailValid( CharSequence email )
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher( email ).matches();
    }

    private void alert( String message )
    {
        AlertDialog alert = new AlertDialog.Builder(this ).create();
        alert.setTitle( "Atención" );
        alert.setMessage( message );
        alert.setIcon( R.drawable.notification_warning_small );
        alert.setCancelable( false );
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }


    private void saveCurrentGuest()
    {
        RadioButton btnMisma = (RadioButton) findViewById(R.id.btn_rad_misma);
        EditText txtName = (EditText) findViewById(R.id.txtName);
        EditText txtLast = (EditText) findViewById(R.id.txtLast);
        EditText txtEmail = (EditText) findViewById( R.id.txtEmail );
        EditText txtSocio = (EditText) findViewById(R.id.txtSocio);
        CheckBox cbAfiliate = (CheckBox) findViewById(R.id.cbAfiliate);
        EditText txtPhone = (EditText) findViewById(R.id.txtPhone);
        Spinner spinViaje = (Spinner) findViewById(R.id.spinViaje);
        Spinner spinAdultos = (Spinner) findViewById(R.id.spinAdultos);
        Spinner spinNinos = (Spinner) findViewById(R.id.spinNinos);

        GuestData d = titulares.get( _lastGuestIndex );
        d.setDataOption(btnMisma.isChecked() ? 0 : 1);
        if( _lastGuestIndex == 0 || ( _lastGuestIndex != 0 && !btnMisma.isChecked() ) )
        {
            d.setName( txtName.getText().toString() );
            d.setLastName( txtLast.getText().toString() );
            d.setEmail( txtEmail.getText().toString() );
            d.setSocio( txtSocio.getText().toString() );
            d.setAfiliate( cbAfiliate.isChecked() );
            d.setPhone( txtPhone.getText().toString() );
            d.setViaje( spinViaje.getSelectedItemPosition() );
        }
        if(_lastGuestIndex>0 && btnMisma.isChecked()){
            System.out.println("ENTREACA");
            d.setName( txtName.getText().toString() );
            d.setLastName( txtLast.getText().toString() );
            d.setEmail( txtEmail.getText().toString() );
            d.setSocio( txtSocio.getText().toString() );
            d.setAfiliate( cbAfiliate.isChecked() );
            d.setPhone( txtPhone.getText().toString() );
            d.setViaje( spinViaje.getSelectedItemPosition() );
        }
        d.setAdultos( spinAdultos.getSelectedItemPosition() );
        d.setNinos(spinNinos.getSelectedItemPosition());
    }

    private void setGuestData()
    {
        Log.d( "TEST", "Setting Guest Data" );
        RadioButton btnMisma = (RadioButton) findViewById( R.id.btn_rad_misma );
        RadioButton btnOtras = (RadioButton) findViewById( R.id.btn_rad_otros );
        EditText txtName = (EditText) findViewById( R.id.txtName );
        EditText txtLast = (EditText) findViewById( R.id.txtLast );
        EditText txtEmail = (EditText) findViewById( R.id.txtEmail );
        EditText txtSocio = (EditText) findViewById( R.id.txtSocio );
        CheckBox cbAfiliate = (CheckBox) findViewById( R.id.cbAfiliate );
        EditText txtPhone = (EditText) findViewById( R.id.txtPhone );
        Spinner spinViaje = (Spinner) findViewById( R.id.spinViaje );
        Spinner spinAdultos = (Spinner) findViewById(R.id.spinAdultos);
        Spinner spinNinos = (Spinner) findViewById(R.id.spinNinos);

        GuestData d;

        d = titulares.get( _lastGuestIndex );


        prepareNinosSpinner( d.getAdultos() + 1 ); // Acompañantes + Titular
        spinAdultos.setSelection( d.getAdultos() );
        spinNinos.setSelection( d.getNinos() );

        if( _lastGuestIndex == 0 )
        {
            d = titulares.get( 0 );
            txtName.setEnabled( true );
            txtLast.setEnabled( true );
            txtEmail.setEnabled( true );
            txtSocio.setEnabled( true );
            cbAfiliate.setEnabled( true );
            txtPhone.setEnabled( true );
            spinViaje.setEnabled( true );
        }
        else
        {
            d = titulares.get( _lastGuestIndex );
            if( d.getDataOption() == 0 )
            {
                btnMisma.setChecked( true );
            }
            else
            {
                btnOtras.setChecked( true );
            }

            if( d.getDataOption() == 0 )
            {
                d = titulares.get( 0 );
                txtName.setEnabled( false );
                txtLast.setEnabled( false );
                txtEmail.setEnabled( false );
                txtSocio.setEnabled( false );
                cbAfiliate.setEnabled( false );
                txtPhone.setEnabled( false );
                spinViaje.setEnabled( false );
            }
            else
            {
                txtName.setEnabled( true );
                txtLast.setEnabled( true );
                txtEmail.setEnabled( true );
                txtSocio.setEnabled( true );
                cbAfiliate.setEnabled( true );
                txtPhone.setEnabled( true );
                spinViaje.setEnabled( true );
            }
        }

        txtName.setText( d.getName() );
        txtLast.setText( d.getLastName() );
        txtEmail.setText( d.getEmail() );
        txtSocio.setText( d.getSocio() );
        cbAfiliate.setChecked(d.isAfiliate());
        txtPhone.setText(d.getPhone());
        spinViaje.setSelection(d.getViaje());
    }

    private void handleCaptureOptionHandle()
    {
        if( _lastGuestIndex != 0 )
        {
            RadioButton btnMisma = (RadioButton) findViewById( R.id.btn_rad_misma );
            GuestData d = titulares.get( _lastGuestIndex );
            d.setDataOption(btnMisma.isChecked() ? 0 : 1);
            setGuestData();
        }
    }

    private void prepareNinosSpinner( int totalAdultos )
    {
        // Actualizamos el combo de niños, porque no pueden haber más personas
        // que el máximo de adultos.
        Spinner spinNinos = (Spinner) findViewById( R.id.spinNinos );
        int maxNinos = 2 - totalAdultos; //Macximo de adultos - total de adultos

        ArrayList<String> ninos = new ArrayList<String>();
        for( int i = 0; i <= maxNinos; i++ )
        {
            ninos.add( "" + i );
        }

        SpinnerAdapter adapterNinos = new ArrayAdapter<String>(this, R.layout.habitaciones_item, R.id.txtOption, ninos );
        spinNinos.setAdapter(adapterNinos);
    }

    private class GuestData
    {
        private int _dataOption;
        private String _name;
        private String _lastName;
        private String _email;
        private String _socio;
        private boolean _afiliate;
        private String _phone;
        private int _viaje;
        private int _adultos;
        private int _ninos;

        public GuestData()
        {
        }

        public GuestData(int _dataOption, String _name, String _lastName, String _email, String _socio, boolean _afiliate, String _phone, int _viaje, int _adultos, int _ninos) {
            this._dataOption = _dataOption;
            this._name = _name;
            this._lastName = _lastName;
            this._email = _email;
            this._socio = _socio;
            this._afiliate = _afiliate;
            this._phone = _phone;
            this._viaje = _viaje;
            this._adultos = _adultos;
            this._ninos = _ninos;
        }

        public GuestData(String _name, String _lastName, String _email, String _socio, boolean _afiliate, String _phone, int _viaje, int _adultos, int _ninos) {
            this._name = _name;
            this._lastName = _lastName;
            this._email = _email;
            this._socio = _socio;
            this._afiliate = _afiliate;
            this._phone = _phone;
            this._viaje = _viaje;
            this._adultos = _adultos;
            this._ninos = _ninos;
        }

        public int getDataOption()
        {
            return _dataOption;
        }

        public void setDataOption( int dataOption )
        {
            _dataOption = dataOption;
        }

        public String getName()
        {
            return _name;
        }

        public void setName( String name )
        {
            _name = name;
        }

        public String getLastName()
        {
            return _lastName;
        }

        public void setLastName( String lastName )
        {
            _lastName = lastName;
        }

        public String getEmail()
        {
            return _email;
        }

        public void setEmail( String email )
        {
            _email = email;
        }

        public String getSocio()
        {
            return _socio;
        }

        public void setSocio( String socio )
        {
            _socio = socio;
        }

        public boolean isAfiliate()
        {
            return _afiliate;
        }

        public void setAfiliate( boolean afiliate )
        {
            _afiliate = afiliate;
        }

        public String getPhone()
        {
            return _phone;
        }

        public void setPhone( String phone )
        {
            _phone = phone;
        }

        public int getViaje()
        {
            return _viaje;
        }

        public void setViaje( int viaje )
        {
            _viaje = viaje;
        }

        public int getAdultos()
        {
            return _adultos;
        }

        public void setAdultos( int adultos )
        {
            _adultos = adultos;
        }

        public int getNinos()
        {
            return _ninos;
        }

        public void setNinos( int ninos )
        {
            _ninos = ninos;
        }
    }

    private String getAddressString()
    {
        String address = "";
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getDireccion() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getDireccion().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getDireccion() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getColonia() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getColonia().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getColonia() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCp() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCp().equalsIgnoreCase( "null" ) )
        {
            address += "CP: " + ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCp() + "\n";
        }
        if( ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCiudad() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCiudad().equalsIgnoreCase( "null" ) && ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getEstado() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getEstado().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCiudad() + ". " + ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getEstado();
        }
        else if( ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCiudad() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCiudad().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getCiudad();
        }
        else if( ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getEstado() != null && !ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getEstado().equalsIgnoreCase( "null" ) )
        {
            address += ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getEstado();
        }
        if( address.endsWith( "\n" ) )
        {
            address = address.substring( 0, address.length() - 1 );
        }
        return address;
    }


}
