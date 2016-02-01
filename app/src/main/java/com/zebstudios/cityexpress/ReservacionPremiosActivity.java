package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.Locale;
import java.util.Map;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
//import io.realm.Realm;

import static com.appsee.Appsee.addEvent;

/**
 * Created by edwinhernandez on 17/12/15.
 */
public class ReservacionPremiosActivity extends Activity implements PayPalCaller.PayPalCallerInterface{


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
    double IVAHabitacion;
    double IVAHabitacion2;
    double subtotalHabitacion;
    double subtotalHabitacion2;
    static ArrayList<GuestData> titulares;
    ArrayList<String> adultos;
    ArrayList<String> ninos;
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
    int numInfantes;
    int numAdultos;
    int numPersonas;
    String codigoBase;
    String siglasHotel;
    String llegada;
    String descripcionHabitacionJSON;
    Date arrival;
    Date departure;
    String precio;
    private int _lastGuestIndex;
    private ProgressDialogFragment _progress;
    TextView lblTotal;
    TextView lblSubTotal;
    TextView lblIVA;
    TextView lblTotal2;
    TextView lblSubTotal2;
    TextView lblIVA2;
    int numHuesped;
    ProgressDialog progress;
    int contador;
    int contadorReservaciones;
    private static final int PAYMENT_METHOD_CARD = 100;
    private static final int PAYMENT_METHOD_PAYPAL = 101;
    private int _paymentMethod;
    private boolean _isShowingWebView = false;
    //Realm realm;

    HabitacionBase habitacionBase;
    HabitacionBase habitacionCity;
    ArrayList<HabitacionBase> habitacionBaseList;
    ArrayList<HabitacionBase> habitacionCityPremiosList;
    ArrayList<Hotel> listaGeneralHotel;
    boolean cityPremios =false;
    ArrayList<String> huespedes;
    static ArrayList<SummaryEntry> sumary;
    static ArrayList<String> preciosList;
    static ArrayList<String> ivaList;
    boolean maxPersonas = false;
    GuestData guess;
    double subtotal;
    double IVA;
    double totalReservacion;

    //DAtosUsuario City;
    String nombreUsuarioCity;
    String apellidoUsuarioCity;
    String correoUsuarioCity;
    String phoneUsuarioCity;

    boolean segunda=false;


    @Override
    protected void onResume() {
        super.onResume();
        if(segunda){
            System.out.println("ONRESUMESEGUNDA");
            segunda=false;
        }else{
            System.out.println("NOPASOSEGUNDA");
            if(personF2GO==null){
                finish();
            }else{
                SharedPreferences prefsUsuario = this.getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
                personF2GO = prefsUsuario.getString("person_ID", null);
                nombreUsuarioCity = prefsUsuario.getString("nombre", null);
                apellidoUsuarioCity = prefsUsuario.getString("apellido", null);
                correoUsuarioCity = prefsUsuario.getString("usuario", null);
                phoneUsuarioCity = prefsUsuario.getString("phone",null);

                txtName.setText(nombreUsuarioCity);
                txtLast.setText(apellidoUsuarioCity);
                txtEmail.setText(correoUsuarioCity);
                txtPhone.setText(phoneUsuarioCity);

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        segunda=true;
        try {
            SharedPreferences prefsUsuario = this.getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
            personF2GO = prefsUsuario.getString("person_ID", null);
            nombreUsuarioCity = prefsUsuario.getString("nombre", null);
            apellidoUsuarioCity = prefsUsuario.getString("apellido", null);
            correoUsuarioCity = prefsUsuario.getString("usuario", null);
            phoneUsuarioCity = prefsUsuario.getString("phone",null);


            obtenerListadoTarjetas();
            setContentView(R.layout.activity_reservacion_city);


        } catch (Exception e) {
            setContentView(R.layout.activity_reservacion_city);
            alert("Problema al cargar las tarjetas");
            numTarjeta = new ArrayList<String>();
            linearaddTarjeta = (LinearLayout) findViewById(R.id.pnlCCPayment);
            linearAgregarTarjeta = (LinearLayout) findViewById(R.id.linearAddtarjeta);
            SpinnerAdapter adapterTajetas = new ArrayAdapter<String>( getBaseContext(), R.layout.habitaciones_item, R.id.txtOption, numTarjeta );
            Spinner spinTarjetas = (Spinner) findViewById(R.id.spinTarjetas);
            linearAgregarTarjeta.setVisibility(View.VISIBLE);
            linearaddTarjeta.setVisibility(View.GONE);
        }

        imageBack = (ImageView) findViewById(R.id.back_button);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        obtenerHostUsuario();
       /* SharedPreferences prefsUsuario = this.getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
        personF2GO = prefsUsuario.getString("person_ID", null);
        nombreUsuarioCity = prefsUsuario.getString("nombre", null);
        apellidoUsuarioCity = prefsUsuario.getString("apellido", null);
        correoUsuarioCity = prefsUsuario.getString("usuario", null);
        phoneUsuarioCity = prefsUsuario.getString("phone",null);*/

        System.out.println("NOMBREUSUARIO" + nombreUsuarioCity + "apellidoUsuario " + apellidoUsuarioCity + "CorreoUsuario" + correoUsuarioCity);
        if(personF2GO==null){
            Intent i = new Intent(this, IniciarSesionActivity.class);
            startActivityForResult(i, 1);
        }

        TextView lblHotelName = (TextView) findViewById(R.id.lblHotelName);
        TextView lblArrivalDate = (TextView) findViewById( R.id.dates_arrival_text );
        TextView lblDepartureDate = (TextView) findViewById( R.id.dates_departure_text );
        lblSubTotal = (TextView) findViewById(R.id.lblSubTotal);
        lblIVA = (TextView) findViewById(R.id.lblIVA);
        lblTotal = (TextView) findViewById(R.id.lblTotal);
        TextView lblHotelName2 = (TextView) findViewById( R.id.lblHotelName2 );
        TextView lblArrivalDate2 = (TextView) findViewById( R.id.dates_arrival_text2 );
        TextView lblDepartureDate2 = (TextView) findViewById( R.id.dates_departure_text2 );
        lblTotal2 = (TextView) findViewById(R.id.lblTotal2);
        lblSubTotal2 = (TextView) findViewById(R.id.lblSubTotal2);
        lblIVA2 = (TextView) findViewById(R.id.lblIVA2);

        linearpayTarjeta = (LinearLayout) findViewById(R.id.linearAddtarjeta);

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
        txtName.setText(nombreUsuarioCity);
        txtLast.setText(apellidoUsuarioCity);
        txtEmail.setText(correoUsuarioCity);
        txtPhone.setText(phoneUsuarioCity);

        numTarjeta = new ArrayList<String>();

        btnGuardarTarjeta = (Button) findViewById(R.id.guardarTarjeta);

        segmentswitch = (SegmentedGroup) findViewById(R.id.segmentedPaymentMethod);
        btnTarjeta = (RadioButton)findViewById(R.id.btn_method_card);
        btnPaypal = (RadioButton) findViewById(R.id.btn_method_paypal);

        btnTarjeta.toggle();

        btnaddTarjeta = (Button) findViewById(R.id.btnAddTarjeta);
        btnTarjetas = (Button) findViewById(R.id.verTarjetas);


        cbAfiliate.setChecked(true);
        totalReservacion = 0;
        Bundle bundle = getIntent().getExtras();
        posicionHot = bundle.getInt("posicionHotel");
        codigoBase = bundle.getString("codigoBase");
        posicionHab = bundle.getInt("posicionHabitacion");
        numHabitacion = bundle.getInt("numHabitacion");
        descripcionHabitacionJSON = bundle.getString("descipcionHabitacionJSON");
        siglasHotel = bundle.getString("siglasHotel");
        arrival = PrincipalFragment._arrivalDate;
        llegada = new SimpleDateFormat("yyyy-MM-dd").format(arrival);

        departure = PrincipalFragment._departureDate;
        numNoches = ResultadosDisponibilidad.totalNoches;
        numNoches = Math.abs(numNoches);
        precioHabitacion = Double.parseDouble(ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getArrayHabitacionesCity().get(posicionHab).getCosto().replace(",", ""));
        subtotalHabitacion = Double.parseDouble(ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getArrayHabitacionesCity().get(posicionHab).getSubTotal().replace(",", ""));
        IVAHabitacion = Double.parseDouble(ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getArrayHabitacionesCity().get(posicionHab).getIVA().replace(",", ""));
        _lastGuestIndex = 0;
        System.out.println("numNoches" + numNoches);
        System.out.println("IVAHABITACION" + IVAHabitacion);
        System.out.println("SIGLASHOTEL"+ bundle.getString("siglasHotel"));
        _hotel = ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot);
        ResultadosDisponibilidad.habitacionBaseList.get(posicionHab);


        precio = bundle.getString("precioPremio");
        precioHabitacion = Double.parseDouble(precio);
        System.out.println("HotelPosicionPrecioListado->" + precioHabitacion);
        System.out.println("PsoicionHotel" + posicionHot);
        System.out.println("PsoiconHabitacion" + posicionHab);
        System.out.println("arrival" + arrival);
        System.out.println("departure" + departure);
        System.out.println("codigoBase" + codigoBase);

        adultos = new ArrayList<String>();
        ninos = new ArrayList<String>();
        adultos.add("0");
        ninos.add("0");
        numInfantes = 0;
        numAdultos = 0;
        obtenerAdultos();
        contador = 0;

        subtotalHabitacion2 = 0;
        IVAHabitacion2 = 0;


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

        /*ArrayList<String> adultos = new ArrayList<String>();
        ArrayList<String> ninos = new ArrayList<String>();*/
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
        /*adultos.add("0");
        adultos.add("1");
        ninos.add("0");
        ninos.add("1");*/

        Spinner spinAdultos = (Spinner) findViewById( R.id.spinAdultos );
        SpinnerAdapter adapterAdultos = new ArrayAdapter<String>( ReservacionPremiosActivity.this, R.layout.habitaciones_item, R.id.txtOption, adultos );
        spinAdultos.setAdapter( adapterAdultos );
        spinAdultos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //changeAdultos();
                numAdultos = position;
                changeAdultos(numAdultos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinNinos = (Spinner) findViewById( R.id.spinNinos );
        SpinnerAdapter adapterNinos = new ArrayAdapter<String>( ReservacionPremiosActivity.this, R.layout.habitaciones_item, R.id.txtOption, ninos );
        spinNinos.setAdapter(adapterNinos);
        spinNinos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numInfantes = position;
                changeInfantes(numInfantes);
                //changeAdultos(numAdultos,numInfantes);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                if ("".equals(txtCardName.getText().toString())) {
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
                } else if (!CCUtils.validCC(txtCardNumb.getText().toString())) {
                    alert("El número de tarjeta es inválido.");
                }
                EditText txtCardCode = (EditText) findViewById(R.id.txtCardCode);
                if (txtCardCode.getText().toString().trim().length() != 3 || !isNumeric(txtCardCode.getText().toString().trim())) {
                    alert("El código de validación de la tarjeta es incorrecto. Ingresa los últimos tres dígitos del número que se encuentra en la parte posterior de la tarjeta.");
                } else if ("".equalsIgnoreCase(fechaVencimiento)) {
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
                } else if ("".equalsIgnoreCase(mesVencimiento)) {
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
                } else {
                    System.out.println("AÑO/MES" + yearVencimiento + mesVencimiento);
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
        lblHotelName.setText(_hotel.getNombre());
        lblArrivalDate.setText(sdfecha.format(arrival));
        lblDepartureDate.setText(sdfecha.format(departure));
        lblHotelName2.setText( _hotel.getNombre() );
        lblArrivalDate2.setText(sdfecha.format(arrival));
        lblDepartureDate2.setText(sdfecha.format(departure));



        lblTotal.setText(String.format( "Total: $%,.2f ", getTotalCost() ) + "M.N");//String.format( "Total: $%,.2f ",1123 ) );
        lblTotal2.setText(String.format( "Total: $%,.2f ", getTotalCost()) + "M.N" );// String.format( "Total: $%,.2f ", 34345 )  );

        lblIVA.setText(String.format("Impuestos: $%,.2f ", getIVACost()) + "M.N");
        lblIVA2.setText(String.format("Impuestos: $%,.2f ", getIVACost()) + "M.N");
        lblSubTotal.setText(String.format("Subtotal: $%,.2f ", getSubTotalCost()) + "M.N");
        lblSubTotal2.setText(String.format("Subtotal: $%,.2f ", getSubTotalCost()) + "M.N");


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
                if( _paymentMethod == PAYMENT_METHOD_CARD ){
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
                                .setMessage("El Código de seguridad de la tarjeta no puede estar vacío")
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
                }else{
                    if (validateCapture()) {
                        for (int i = 0; i < titulares.size(); i++) {
                            changeHuesped(i);
                            System.out.println("Titular " + i + "---->Nombre" + titulares.get(i).getName() + "TAmaño ZisE->"+ titulares.size());
                        }

                        System.out.println("Lo Logre!!!!!");
                        RecibirDatos();
                    }
                }

                //System.out.println("Codigo CVV->" + txtCvvTarjetaLista.getText().toString());

            }
        });


        //ArrayList<String> huespedes = new ArrayList<String>();
        //ArrayList<SummaryEntry> sumary = new ArrayList<SummaryEntry>();
        huespedes = new ArrayList<String>();
        sumary = new ArrayList<SummaryEntry>();
        preciosList = new ArrayList<String>();
        ivaList = new ArrayList<String>();

        for( int i = 0; i < numHabitacion; i++ )
        {
            huespedes.add( "Huésped titular - habitación " + ( i + 1 ) );
            sumary.add( new SummaryEntry( 0, "Habitación " + ( i + 1 ) ) );
            for( int j = 0; j < Math.abs(numNoches); j++ )
            {
                sumary.add( new SummaryEntry( 1, "Noche " + ( j + 1 ) + " $" + subtotalHabitacion + " M.N"));//precioHabitacion));
                preciosList.add(""+subtotalHabitacion);
                ivaList.add(""+IVAHabitacion);
            }
            titulares.add(new GuestData());
        }
        numHuesped = 0;
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
                numHuesped = position;
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

        //Paypal
        SegmentedGroup segmentedPayment = (SegmentedGroup) findViewById(R.id.segmentedPaymentMethod);
        segmentedPayment.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( RadioGroup group, int checkedId )
            {
                handleChangePaymentMethod();
            }
        } );

        //LinearLayout pnlPaymentMethod = (LinearLayout) findViewById(R.id.pnlCCPayment);
        /*RadioButton btnCardMethod = (RadioButton) findViewById(R.id.btn_method_card);
        RadioButton btnPaypalMethod = (RadioButton) findViewById(R.id.btn_method_paypal);

            _paymentMethod = PAYMENT_METHOD_PAYPAL;
            btnPaypalMethod.setChecked( true );
            btnCardMethod.setEnabled( false );
            btnReserva2.setText( "Ingresar a PayPal" );*/
        //pnlPaymentMethod.setVisibility( View.GONE );
        WebView webView = (WebView) findViewById( R.id.webView );
        webView.setWebViewClient(new PayPalWebViewClient());

        Analytics analytics = (Analytics) getApplication();
        analytics.sendAppEventTrack("HOTEL DETAIL ANDROID", "RESERVA 2", "HOTEL", _hotel.getNombre(), 1);

    }

    //Cambiar la tarifa de adultos.
    public void changeAdultos(final int numAdultos){
        int totalAdultos = numAdultos + numInfantes;
        if(numAdultos<=1){
            ninos.clear();
            int max=numPersonas-numAdultos;
            for (int i = 0; i < max; i++) {
                ninos.add(""+i);
            }
            SpinnerAdapter adapterNinos = new ArrayAdapter<String>(ReservacionPremiosActivity.this, R.layout.habitaciones_item, R.id.txtOption, ninos);
            spinNinos.setAdapter(adapterNinos);
            if(maxPersonas){
                System.out.println("Cambiar tarifa");
                System.out.println("NumeroDEAdultos->" + adultos.get(posicion));
                maxPersonas=false;
                cambiarTarifa();
            }
        }
        else if(numAdultos>1  ){
            maxPersonas = true;
            System.out.println("Cambiar tarifa");
            System.out.println("NumeroDEAdultos->" + adultos.get(posicion));
            ninos.clear();
            int max=numPersonas-numAdultos;
            for (int i = 0; i < max; i++) {
                ninos.add(""+i);
            }
            SpinnerAdapter adapterNinos = new ArrayAdapter<String>(ReservacionPremiosActivity.this, R.layout.habitaciones_item, R.id.txtOption, ninos);
            spinNinos.setAdapter(adapterNinos);
            cambiarTarifa();

        }
        else if(numInfantes>1 && totalAdultos<numPersonas){
            System.out.println("Cambiar tarifa");
            System.out.println("numero de infantes" + ninos.get(posicion));
            ninos.get(posicion);
            cambiarTarifa();
        }
    }

    public void changeInfantes(int numInfantes){
        if(numAdultos>0 && numInfantes>0){
            cambiarTarifa();
        }
        /*else if(numAdultos==0){
            if(numInfantes<=1) {
                adultos.clear();
                int max = numPersonas - numInfantes;
                for (int i = 0; i < max; i++) {
                    adultos.add("" + i);
                }
                SpinnerAdapter adapterNinos = new ArrayAdapter<String>(ReservacionActivity.this, R.layout.habitaciones_item, R.id.txtOption, adultos);
                spinAdultos.setAdapter(adapterNinos);
                if (maxPersonas) {
                    System.out.println("Cambiar tarifa");
                    System.out.println("NumeroDEAdultos->" + ninos.get(posicion));
                    maxPersonas = false;
                    cambiarTarifa();
                }
            }
           else if(numInfantes>1 ){
                System.out.println("jhdfsjlhdsfhljksdf"+numInfantes);
                maxPersonas = true;
                System.out.println("Cambiar tarifa");
                System.out.println("NumeroDEAdultos->" + ninos.get(posicion));
                adultos.clear();
                int max=(numPersonas+numAdultos)-numInfantes;
                for (int i = 0; i < max; i++) {
                    adultos.add(""+i);
                }
                SpinnerAdapter adapterNinos = new ArrayAdapter<String>(ReservacionActivity.this, R.layout.habitaciones_item, R.id.txtOption, adultos);
                spinAdultos.setAdapter(adapterNinos);
                //cambiarTarifa();

            }
        }*/
    }

    public void cambiarTarifa(){
        //System.out.println("SIGLAS->"+listaHotel.get(contador).getSiglas());
        cadena = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <s:Body>\n" +
                "    <GetRoomsAvailablePromoImpuestos xmlns=\"http://tempuri.org/\">\n" +
                "      <promoRequestModelv3I xmlns:d4p1=\"http://schemas.datacontract.org/2004/07/CityHub\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "        <d4p1:CodigoPromocion />\n" +
                "        <d4p1:CodigoTarifa>1114</d4p1:CodigoTarifa>\n" + //1115P
                "        <d4p1:FechaInicial>"+llegada+"</d4p1:FechaInicial>\n" +
                "        <d4p1:Hotel>"+ siglasHotel +"</d4p1:Hotel>\n" +
                "        <d4p1:NumeroAdultos>"+((numAdultos+numInfantes)+1)+"</d4p1:NumeroAdultos>\n" +
                "        <d4p1:NumeroDeNoches>"+numNoches+"</d4p1:NumeroDeNoches>\n" +
                "        <d4p1:NumeroHabitaciones>1</d4p1:NumeroHabitaciones>\n" +
                "        <d4p1:Segmento>001</d4p1:Segmento>\n" +
                "        <d4p1:TipoHabitacion />\n" +
                "      </promoRequestModelv3I>\n" +
                "    </GetRoomsAvailablePromoImpuestos>\n" +
                "  </s:Body>\n" +
                "</s:Envelope>";

        System.out.println("XMLHOTELES"+cadena);
        progress = ProgressDialog.show(ReservacionPremiosActivity.this, "Cargando...",
                "Espere por favor", true);
        StringRequest registro = new StringRequest(Request.Method.POST,APIAddress.URL_RESERVACIONES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { //wsMotor2014 // wsMotor2015_Prod

                System.out.println("IMPUESTOS->"+response);
                InputStream stream = null;
                try {
                    stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                    parseXMLImpuestos(stream);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    obtenerDescripcionHotelImpuestos(response);
                    progress.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                    /*NetworkResponse response = error.networkResponse;
                    String datos = new String(response.data);
                    System.out.println("sout" + datos);*/
                progress.dismiss();
                alert("Ha ocurrido un error de conexión");
            }
        }) {

            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/xml; charset=utf-8");
                params.put("SOAPAction", "http://tempuri.org/IReservationEngine/GetRoomsAvailablePromoImpuestos");
                Log.d("hsdhsdfhuidiuhsd", "clave");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return cadena.toString().getBytes();
            }

        };
        //System.out.println("registro->" + registro.toString());
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
           /* for (int i = 0; i <listaGeneralHotel.size() ; i++) {
                for (int j = 0; j < listaGeneralHotel.get(i).getArrayHabitaciones().size() ; j++) {
                    if(listaGeneralHotel.get(i).getArrayHabitaciones().get(j).getCodigoBase().equalsIgnoreCase("HCP")){
                        listaGeneralHotel.get(i).getArrayHabitaciones().remove(j);
                    }
                }
            }*/


    }


    public void parseXMLImpuestos(InputStream is) throws Exception {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        //List<String> listaBase = null;
        try {
            //listaBase = new ArrayList<>();
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);
            habitacionBaseList = new ArrayList<>();
            habitacionCityPremiosList = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("AvailableI")) {
                        } else if(tagname.equalsIgnoreCase("Disponibilidad")){

                        } else if(tagname.equalsIgnoreCase("HabBaseI")){
                            habitacionBase = new HabitacionBase();
                            habitacionCity = new HabitacionBase();

                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Available")) {

                            if(cityPremios){

                            }else{
                                //listaGeneralHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")), new JSONArray(nuevo.getString("Imagenes")), habitacionBaseList,habitacionCityPremiosList));
                                //listaGeneralHotel.add(new Hotel(new JSONObject(nuevo.getString("Hotele")), new JSONArray(nuevo.getString("Imagenes")), habitacionBaseList));
                                //System.out.println("TOTAL->" + listaGeneralHotel.size());
                                habitacionBaseList = new ArrayList<>();
                                //habitacionCityPremiosList = new ArrayList<>();
                                // add employee object to list
                                // employee.setHabitacionBasesList(habitacionBasesList);
                            }

                        }else if(tagname.equalsIgnoreCase("CodigoError")){

                        }else if(tagname.equalsIgnoreCase("CodigoTarifa")){
                            if(text.equalsIgnoreCase("1114")){ //1115P
                                System.out.println("Hola15");
                                cityPremios = true;
                            }else{
                                cityPremios = false;
                            }
                        }else if(tagname.equalsIgnoreCase("DescError")){
                        }else if(tagname.equalsIgnoreCase("Descripcion")){
                        }else if(tagname.equalsIgnoreCase("Hotel")){
                        }else if(tagname.equalsIgnoreCase("Promociones")){
                        }else if(tagname.equalsIgnoreCase("Disponibilidad")){

                        }else if(tagname.equalsIgnoreCase("TarifaBase")){
                        } else if(tagname.equalsIgnoreCase("HabBase")){

                            if(cityPremios){
                                habitacionCityPremiosList.add(habitacionCity);
                            }else{
                                habitacionBaseList.add(habitacionBase);
                            }
                            System.out.println("HabitacionBase" + habitacionBaseList.size());
                            System.out.println("HabitacionCity" + habitacionCityPremiosList.size());
                        }else if (tagname.equalsIgnoreCase("Descripcion")) {
                        } else if (tagname.equalsIgnoreCase("CodigoTarifa")) {
                        } else if (tagname.equalsIgnoreCase("Hotel")) {
                        } else if (tagname.equalsIgnoreCase("PromocionesI")) {
                        } else if (tagname.equalsIgnoreCase("AVAILABILITY")) {
                            habitacionBase.setDisponibilidad(text);
                        } else if (tagname.equalsIgnoreCase("CodBase")) {
                            if(cityPremios){
                                habitacionCity.setCodigoBase(text);
                                System.out.println(text+"zz------");
                            }else{
                                habitacionCity.setCodigoBase("");
                                habitacionBase.setCodigoBase(text);
                            }
                        } else if (tagname.equalsIgnoreCase("DescBase")) {
                            habitacionBase.setDescBase(text);
                        } else if (tagname.equalsIgnoreCase("Costo")) {
                            if (cityPremios) {
                                habitacionCity.setCosto(text);
                            } else {
                                habitacionBase.setCosto(text);
                            }

                        } else if (tagname.equalsIgnoreCase("IVA")) {
                            if(cityPremios){
                                habitacionCity.setIVA(text);
                            }else{
                                habitacionBase.setIVA(text);
                            }
                            System.out.println("IVA->"+text);
                        } else if (tagname.equalsIgnoreCase("SubTotal")) {
                            if(cityPremios){
                                habitacionCity.setSubTotal(text);
                            }else{
                                habitacionBase.setSubTotal(text);
                            }
                            System.out.println("SUBTOTAL->"+text);
                        } else if (tagname.equalsIgnoreCase("Fecha")) {
                            habitacionBase.setFecha(text);
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
    }
    public void obtenerDescripcionHotelImpuestos(String hotel){

        JSONObject jsonObj = null;
        JSONObject mobilegate = null;
        habitacionBase = new HabitacionBase();
        habitacionCity = new HabitacionBase();

        try {


            jsonObj = XML.toJSONObject(hotel);

            JSONObject envelope = new JSONObject(jsonObj.getString("s:Envelope"));

            JSONObject sbody = new JSONObject(envelope.getString("s:Body"));

            JSONObject roomsAvaliables = new JSONObject(sbody.getString("GetRoomsAvailablePromoImpuestosResponse"));

            JSONObject resultRoomsAvailable = new JSONObject(roomsAvaliables.getString("GetRoomsAvailablePromoImpuestosResult"));

            JSONObject disponibilidad = new JSONObject(resultRoomsAvailable.getString("a:Disponibilidad"));

            //System.out.println("Disponibilidad->"+disponibilidad.getString("a:Available"));
            //mobilegate = jsonObj.getJSONObject("Costo");

            JSONArray arreglodisponibilidad = new JSONArray(disponibilidad.getString("a:AvailableI"));

            for (int i = 0; i < arreglodisponibilidad.length(); i++) {
                JSONObject descripcion = new JSONObject(arreglodisponibilidad.getString(i));

                if(descripcion.getString("a:CodigoTarifa").equalsIgnoreCase("1115P")){

                    //descripcion.getString("")
                   /* JSONObject tarifaBase = new JSONObject(descripcion.getString("a:TarifaBaseI"));
                    JSONArray habitacionBase = new JSONArray(tarifaBase.getString("a:HabBaseI"));

                   /* for (int j = 0; j < habitacionBase.length(); j++) {

                        habitacionCity = new HabitacionBase();
                        JSONObject habitacionBaseDisponibilidad = new JSONObject(habitacionBase.get(j).toString());
                        System.out.println("CUATRO1115P->"+habitacionBaseDisponibilidad.getString("a:CodBase"));
                        habitacionCity.setCodigoBase(habitacionBaseDisponibilidad.getString("a:CodBase"));
                        System.out.println("CINCO115P->"+habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject noche = new JSONObject(habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject costoNoche = new JSONObject(noche.getString("a:NocheI"));
                        System.out.println("CostoTotal->" + costoNoche.get("a:Costo"));
                        habitacionCity.setCosto(costoNoche.get("a:Costo").toString());
                        habitacionCity.setIVA(costoNoche.get("a:IVA").toString());
                        habitacionCity.setSubTotal(costoNoche.get("a:SubTotal").toString());
                        habitacionCityPremiosList.add(habitacionCity);
                    }*/
                    JSONObject tarifaBase = new JSONObject(descripcion.getString("a:TarifaBaseI"));
                    JSONArray habitacionBaseJSON = new JSONArray(tarifaBase.getString("a:HabBaseI"));

                    for (int j = 0; j < habitacionBaseJSON.length(); j++) {
                        habitacionBase = new HabitacionBase();
                        JSONObject habitacionBaseDisponibilidad = new JSONObject(habitacionBaseJSON.get(j).toString());
                        System.out.println("CUATRO->"+habitacionBaseDisponibilidad.getString("a:CodBase"));
                        habitacionBase.setCodigoBase(habitacionBaseDisponibilidad.getString("a:CodBase"));
                        System.out.println("CINCO->"+habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject noche = new JSONObject(habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject costoNoche = new JSONObject(noche.getString("a:NocheI"));
                        System.out.println("CostoTotal->" + costoNoche.get("a:Costo"));
                        habitacionBase.setCosto(costoNoche.get("a:Costo").toString());
                        habitacionBase.setIVA(costoNoche.get("a:IVA").toString());
                        habitacionBase.setSubTotal(costoNoche.get("a:SubTotal").toString());
                        System.out.println("COSTOSUBTOTAL" + costoNoche.get("a:SubTotal").toString());
                        habitacionBaseList.add(habitacionBase);

                        if(habitacionBase.getCodigoBase().equalsIgnoreCase(codigoBase)){

                            precioHabitacion = Double.parseDouble(habitacionBase.getCosto().replace(",",""));
                            IVAHabitacion2 = Double.parseDouble(habitacionBase.getIVA().replace(",", ""));
                            subtotalHabitacion2 = Double.parseDouble(habitacionBase.getSubTotal().replace(",", ""));
                            System.out.println("IVAHABITACION2" + IVAHabitacion2);
                            sumary.clear();
                            preciosList.clear();
                            ivaList.clear();
                            subtotal = 0;
                            IVA = 0;
                            for (int k = 0; k < numHabitacion; k++) {
                                if(k==numHuesped){
                                    sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                                    for (int l = 0; l < Math.abs(numNoches); l++) {
                                        sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + subtotalHabitacion2 + " M.N"));//precioHabitacion));
                                        preciosList.add(subtotalHabitacion2+"");
                                        subtotal += subtotalHabitacion2;
                                        IVA += IVAHabitacion2;
                                        ivaList.add(""+IVAHabitacion2);
                                    }
                                }else{
                                    sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                                    for (int l = 0; l < Math.abs(numNoches); l++) {
                                        sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + guess.getPrecio() + " M.N"));//precioHabitacion))//// ;
                                        subtotal += guess.getPrecio();
                                        preciosList.add(subtotal+"");
                                        IVA += guess.getIva();
                                        ivaList.add(""+guess.getIva());
                                    }
                                }

                            }

                            NestedListView list = (NestedListView) findViewById(R.id.list_summary);
                            NestedListView list2 = (NestedListView) findViewById(R.id.list_summary2);
                            SummaryListAdapter adapter = new SummaryListAdapter(this, sumary);
                            list.setAdapter(adapter);
                            list2.setAdapter(adapter);
                            lblTotal.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");//String.format( "Total: $%,.2f ",1123 ) );
                            lblTotal2.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");// String.format( "Total: $%,.2f ", 34345 )  );
                            lblIVA.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                            lblIVA2.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                            lblSubTotal.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");
                            lblSubTotal2.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");
                        }
                    }
                }else if(descripcion.getString("a:CodigoTarifa").equalsIgnoreCase("1114")){

                    JSONObject tarifaBase = new JSONObject(descripcion.getString("a:TarifaBaseI"));
                    JSONArray habitacionBaseJSON = new JSONArray(tarifaBase.getString("a:HabBaseI"));

                    for (int j = 0; j < habitacionBaseJSON.length(); j++) {
                        habitacionBase = new HabitacionBase();
                        JSONObject habitacionBaseDisponibilidad = new JSONObject(habitacionBaseJSON.get(j).toString());
                        System.out.println("CUATRO->"+habitacionBaseDisponibilidad.getString("a:CodBase"));
                        habitacionBase.setCodigoBase(habitacionBaseDisponibilidad.getString("a:CodBase"));
                        System.out.println("CINCO->"+habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject noche = new JSONObject(habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject costoNoche = new JSONObject(noche.getString("a:NocheI"));
                        System.out.println("CostoTotal->" + costoNoche.get("a:Costo"));
                        habitacionBase.setCosto(costoNoche.get("a:Costo").toString());
                        habitacionBase.setIVA(costoNoche.get("a:IVA").toString());
                        habitacionBase.setSubTotal(costoNoche.get("a:SubTotal").toString());
                        System.out.println("COSTOSUBTOTAL" + costoNoche.get("a:SubTotal").toString());
                        habitacionBaseList.add(habitacionBase);

                        if(habitacionBase.getCodigoBase().equalsIgnoreCase(codigoBase)){

                            precioHabitacion = Double.parseDouble(habitacionBase.getCosto().replace(",",""));
                            IVAHabitacion2 = Double.parseDouble(habitacionBase.getIVA().replace(",", ""));
                            subtotalHabitacion2 = Double.parseDouble(habitacionBase.getSubTotal().replace(",", ""));
                            System.out.println("IVAHABITACION2" + IVAHabitacion2);
                            sumary.clear();
                            preciosList.clear();
                            ivaList.clear();
                            subtotal = 0;
                            IVA = 0;
                            for (int k = 0; k < numHabitacion; k++) {
                                if(k==numHuesped){
                                    sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                                    for (int l = 0; l < Math.abs(numNoches); l++) {
                                        sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + subtotalHabitacion2 + " M.N"));//precioHabitacion));
                                        preciosList.add(subtotalHabitacion2+"");
                                        subtotal += subtotalHabitacion2;
                                        IVA += IVAHabitacion2;
                                        ivaList.add(""+IVAHabitacion2);
                                    }
                                }else{
                                    sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                                    for (int l = 0; l < Math.abs(numNoches); l++) {
                                        sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + guess.getPrecio() + " M.N"));//precioHabitacion))//// ;
                                        subtotal += guess.getPrecio();
                                        preciosList.add(subtotal+"");
                                        IVA += guess.getIva();
                                        ivaList.add(""+guess.getIva());
                                    }
                                }

                            }

                            NestedListView list = (NestedListView) findViewById(R.id.list_summary);
                            NestedListView list2 = (NestedListView) findViewById(R.id.list_summary2);
                            SummaryListAdapter adapter = new SummaryListAdapter(this, sumary);
                            list.setAdapter(adapter);
                            list2.setAdapter(adapter);
                            lblTotal.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");//String.format( "Total: $%,.2f ",1123 ) );
                            lblTotal2.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");// String.format( "Total: $%,.2f ", 34345 )  );
                            lblIVA.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                            lblIVA2.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                            lblSubTotal.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");
                            lblSubTotal2.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");
                        }
                    }
                }
            }


        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();

            try {
                jsonObj = XML.toJSONObject(hotel);
                JSONObject envelope = new JSONObject(jsonObj.getString("s:Envelope"));

                JSONObject sbody = new JSONObject(envelope.getString("s:Body"));

                JSONObject roomsAvaliables = new JSONObject(sbody.getString("GetRoomsAvailablePromoImpuestosResponse"));

                JSONObject resultRoomsAvailable = new JSONObject(roomsAvaliables.getString("GetRoomsAvailablePromoImpuestosResult"));

                JSONObject disponibilidad = new JSONObject(resultRoomsAvailable.getString("a:Disponibilidad"));

                //System.out.println("Disponibilidad->"+disponibilidad.getString("a:Available"));
                //mobilegate = jsonObj.getJSONObject("Costo");

                JSONArray arreglodisponibilidad = new JSONArray(disponibilidad.getString("a:AvailableI"));

                for (int i = 0; i < arreglodisponibilidad.length(); i++) {
                    JSONObject descripcion = new JSONObject(arreglodisponibilidad.getString(i));

                    if(descripcion.getString("a:CodigoTarifa").equalsIgnoreCase("1115P")){

                        //descripcion.getString("")
                        JSONObject tarifaBase = new JSONObject(descripcion.getString("a:TarifaBaseI"));
                        JSONObject habitacionBaseDisponibilidad = new JSONObject(tarifaBase.getString("a:HabBaseI"));


                        habitacionCity = new HabitacionBase();
                        System.out.println("CUATRO1115P->"+habitacionBaseDisponibilidad.getString("a:CodBase"));
                        habitacionCity.setCodigoBase(habitacionBaseDisponibilidad.getString("a:CodBase"));
                        System.out.println("CINCO115P->"+habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject noche = new JSONObject(habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject costoNoche = new JSONObject(noche.getString("a:NocheI"));
                        System.out.println("CostoTotal->" + costoNoche.get("a:Costo"));
                        habitacionCity.setCosto(costoNoche.get("a:Costo").toString());
                        habitacionCity.setIVA(costoNoche.get("a:IVA").toString());
                        habitacionCity.setSubTotal(costoNoche.get("a:SubTotal").toString());
                        habitacionCityPremiosList.add(habitacionCity);

                    }else if(descripcion.getString("a:CodigoTarifa").equalsIgnoreCase("1114")){

                        JSONObject tarifaBase = new JSONObject(descripcion.getString("a:TarifaBaseI"));
                        JSONObject habitacionBaseDisponibilidad = new JSONObject(tarifaBase.getString("a:HabBaseI"));

                        habitacionBase = new HabitacionBase();
                        System.out.println("CUATRO->"+habitacionBaseDisponibilidad.getString("a:CodBase"));
                        habitacionBase.setCodigoBase(habitacionBaseDisponibilidad.getString("a:CodBase"));
                        System.out.println("CINCO->"+habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject noche = new JSONObject(habitacionBaseDisponibilidad.getString("a:Noches"));
                        JSONObject costoNoche = new JSONObject(noche.getString("a:NocheI"));
                        System.out.println("CostoTotal->" + costoNoche.get("a:Costo"));
                        habitacionBase.setCosto(costoNoche.get("a:Costo").toString());
                        habitacionBase.setIVA(costoNoche.get("a:IVA").toString());
                        habitacionBase.setSubTotal(costoNoche.get("a:SubTotal").toString());
                        System.out.println("COSTOSUBTOTAL" + costoNoche.get("a:SubTotal").toString());
                        habitacionBaseList.add(habitacionBase);

                        if(habitacionBase.getCodigoBase().equalsIgnoreCase(codigoBase)){

                            precioHabitacion = Double.parseDouble(habitacionBase.getCosto().replace(",",""));
                            IVAHabitacion2 = Double.parseDouble(habitacionBase.getIVA().replace(",", ""));
                            subtotalHabitacion2 = Double.parseDouble(habitacionBase.getSubTotal().replace(",", ""));
                            System.out.println("IVAHABITACION2" + IVAHabitacion2);
                            sumary.clear();
                            preciosList.clear();
                            ivaList.clear();
                            subtotal = 0;
                            IVA = 0;
                            for (int k = 0; k < numHabitacion; k++) {
                                if(k==numHuesped){
                                    sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                                    for (int l = 0; l < Math.abs(numNoches); l++) {
                                        sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + subtotalHabitacion2 + " M.N"));//precioHabitacion));
                                        preciosList.add(subtotalHabitacion2+"");
                                        subtotal += subtotalHabitacion2;
                                        IVA += IVAHabitacion2;
                                        ivaList.add(""+IVAHabitacion2);
                                    }
                                }else{
                                    sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                                    for (int l = 0; l < Math.abs(numNoches); l++) {
                                        sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + guess.getPrecio() + " M.N"));//precioHabitacion))//// ;
                                        subtotal += guess.getPrecio();
                                        preciosList.add(subtotal+"");
                                        IVA += guess.getIva();
                                        ivaList.add(""+guess.getIva());
                                    }
                                }

                            }

                            NestedListView list = (NestedListView) findViewById(R.id.list_summary);
                            NestedListView list2 = (NestedListView) findViewById(R.id.list_summary2);
                            SummaryListAdapter adapter = new SummaryListAdapter(this, sumary);
                            list.setAdapter(adapter);
                            list2.setAdapter(adapter);
                            lblTotal.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");//String.format( "Total: $%,.2f ",1123 ) );
                            lblTotal2.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");// String.format( "Total: $%,.2f ", 34345 )  );
                            lblIVA.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                            lblIVA2.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                            lblSubTotal.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");
                            lblSubTotal2.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");

                        }
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }
    }

    public void obtenerAdultos(){
        progress = ProgressDialog.show(ReservacionPremiosActivity.this, "Cargando...",
                "Espere por favor", true);
        String url ="https://www.cityexpress.com/umbraco/api/MobileAppServices/GetHabitaciones?HotelCode="+siglasHotel+"&RoomCodes="+codigoBase;

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonStr) {
                        System.out.println("JSONHABITACION->" + jsonStr);
                        try {
                            JSONArray habitacion = new JSONArray(jsonStr);
                            JSONObject habi = new JSONObject(habitacion.get(0).toString());
                            numPersonas = Integer.parseInt(habi.getString("Num_de_Personas"));
                            for (int i = 1; i < numPersonas; i++) {
                                adultos.add(""+i);
                                ninos.add(""+i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                    }
                }
        ) {
            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }
			/*@Override
			public Map<String, String> getHeaders()
			{
				Map<String, String>  params = new HashMap<>();
				params.put("Content-Type", "utf-8");
				return params;
			}*/
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(postRequest);

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
        //enviarReservacion();
        if( _paymentMethod == PAYMENT_METHOD_PAYPAL )
        {
            if( validateCapture() )
            {

                // Iniciar PayPal Payment
                _payPalPayment = getPayPalPayment();
                new PayPalCaller( this, PayPalCaller.INITIATE_PAYMENT, _payPalPayment, this, _progress ).execute();
            }
        }else {
            contadorReservaciones = 0;
            enviarReservacion();
        }


    }

    public void enviarReservacion(){
        Log.d("ReservacionActivity", "Enviar Tarjeta ");
        progress = ProgressDialog.show(ReservacionPremiosActivity.this, "Cargando...",
                "Espere por favor", true);
        if(contadorReservaciones < titulares.size()){
            totalReservacion=0;
            if(IVAHabitacion2 == 0 && subtotal == 0){
                totalReservacion = Double.parseDouble(preciosList.get(contadorReservaciones))+ Double.parseDouble(ivaList.get(contadorReservaciones));

            }else{
                totalReservacion = Double.parseDouble(preciosList.get(contadorReservaciones)) + Double.parseDouble(ivaList.get(contadorReservaciones));
            }


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


            llegada = new SimpleDateFormat("yyyy-MM-dd").format(arrival);

            enviarxml = enviarxml.replace("{smart}", "true");
            enviarxml = enviarxml.replace("{codigohabitacion}", codigoBase);
            enviarxml = enviarxml.replace("{codigotarifa}", "1114"); // 1115P
            enviarxml = enviarxml.replace("{codigopromocion}", "");


            enviarxml = enviarxml.replace("{correoelectronico}", titulares.get(contadorReservaciones).getEmail());
            enviarxml = enviarxml.replace("{telefono}", titulares.get(contadorReservaciones).getPhone());
            /*enviarxml = enviarxml.replace("{totalacompadultos}", "1");
            enviarxml = enviarxml.replace("{totalacompmenores}", "0");*/
            enviarxml = enviarxml.replace("{totalacompadultos}", ""+(numAdultos));
            enviarxml = enviarxml.replace("{totalacompmenores}", "" + (numInfantes));
            enviarxml = enviarxml.replace("{codigopais}", "MEX");
            enviarxml = enviarxml.replace("{acompanantes}", "");
            enviarxml = enviarxml.replace("{huespednombre}", titulares.get(contadorReservaciones).getName());
            enviarxml = enviarxml.replace("{huespedapellidos}", titulares.get(contadorReservaciones).getName());


            enviarxml = enviarxml.replace("{codigohotel}", _hotel.getSiglas());
            enviarxml = enviarxml.replace("{estanciaentrada}", llegada);
            enviarxml = enviarxml.replace("{numeronoches}", "" + numNoches);

            enviarxml = enviarxml.replace("{reservantenombre}", titulares.get(contadorReservaciones).getName());
            enviarxml = enviarxml.replace("{reservanteapellidos}", titulares.get(contadorReservaciones).getLastName());
            enviarxml = enviarxml.replace("{reservantecorreoelectronico}", titulares.get(contadorReservaciones).getEmail());
            enviarxml = enviarxml.replace("{reservantetelefono}", titulares.get(contadorReservaciones).getPhone());


            //enviarxml = enviarxml.replace("{depositomonto}", precio); //Total?
            enviarxml = enviarxml.replace("{depositomonto}", ""+totalReservacion);//preciosList.get(contadorReservaciones));
            enviarxml = enviarxml.replace("{depositotipomoneda}", "MXN");
            enviarxml = enviarxml.replace("{depositofecha}", llegada);
            enviarxml = enviarxml.replace("{formapago}", "SMART");


        /*EditText txtCardName = (EditText) findViewById( R.id.txtCardName );
        EditText txtCardNumber = (EditText) findViewById( R.id.txtCardNumber );
        Spinner spinExpMonth = (Spinner) findViewById( R.id.spinExpMonth );
        Spinner spinExpYear = (Spinner) findViewById( R.id.spinExpYear );
        EditText txtCardCode = (EditText) findViewById( R.id.txtCardCode );*/


            enviarxml = enviarxml.replace("{smartcvv}", cvvTarjetaLista);
            enviarxml = enviarxml.replace("{smartclasificacion}", "16|||||||"+hostUsuario+"|"+nombreUsuarioCity+"|"+llegada+"|"+numNoches+"|1||3||BLVD. JOSE MARIA CHAVEZ |NO. 1919|COL. SAN PEDRO|"+_hotel.getEstado()+"|MX|"+_hotel.getCp()+"|014491492900|0|||||||||||||||"+nombreUsuarioCity+"|"+apellidoUsuarioCity+"|||||||||||"+correoUsuarioCity+"|||");
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

                        }else{
                            alert("Hubo un problema al tratar de hacer tu reservacion, vuelve a intentarlo");
                        }


                        //AltaTarjetaResult

                    } catch (JSONException e) {
                        alert("Hubo un problema al tratar de hacer tu reservacion, vuelve a intentarlo");
                        progress.dismiss();
                        Log.e("JSON exception", e.getMessage());
                        e.printStackTrace();
                    }finally {
                        progress.dismiss();

                    }

                    //FinenviarReservacion("");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
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
            registro.setRetryPolicy(new DefaultRetryPolicy(17000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(registro);


        }

    }


    public void FinenviarReservacion(String reservacion){
        if(reservacion != "") {
            System.out.println("Reservacion OK " + reservacion);

            ReservacionBD reservacionBD = new ReservacionBD();
            reservacionBD.setNombreUsuario(titulares.get(contador).getName());
            reservacionBD.setEmailUsuario(titulares.get(contador).getEmail());
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
            //reservacionBD.setHabCosto("" + precioHabitacion);
            reservacionBD.setHabCosto("" + titulares.get(contador).getPrecio());
            reservacionBD.setNumReservacion(Integer.parseInt(reservacion));
        /*reservacionBD.setAdultos(1);
        reservacionBD.setInfantes(1);*/
            reservacionBD.setAdultos(titulares.get(contador).getAdultos());
            reservacionBD.setInfantes(titulares.get(contador).getNinos());
            reservacionBD.setCodigoHabitacion(codigoBase);
        /*reservacionBD.setTotal("" + precioHabitacion);
        reservacionBD.setSubtotal("" + subtotalHabitacion);
        reservacionBD.setIva("" + IVAHabitacion);*/
            if (getTotalCostAlt() == 0) {
                reservacionBD.setTotal("" + precioHabitacion);
            } else {
                reservacionBD.setTotal("" + getTotalCostAlt());
            }
            if (subtotal == 0) {
                reservacionBD.setSubtotal("" + subtotalHabitacion);
            } else {
                reservacionBD.setSubtotal("" + subtotal);
            }
            if (IVA == 0) {
                reservacionBD.setIva("" + titulares.get(contador).getIva());
            } else {
                reservacionBD.setIva("" + titulares.get(contador).getIva());
            }
            reservacionBD.setNumNoches(numNoches);
            reservacionBD.setNumHabitaciones(numHabitacion);
            reservacionBD.setDireccionHotel(getAddressString());
            reservacionBD.setCheckIn(true);
            reservacionBD.setCheckOut(false);
            reservacionBD.setConsultarSaldos(false);
            reservacionBD.setCityPremios(true);
            System.out.println("DIreccionHotel" + getAddressString());
            System.out.println("numNohe" + numNoches);
            System.out.println("descHabitacion" + ResultadosDisponibilidad.listaGeneralHotel.get(posicionHot).getArrayHabitaciones().get(posicionHab).getDescBase());

        /*realm= Realm.getInstance(this);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(reservacionBD);
        realm.commitTransaction();*/

            ReservacionBDD ds = new ReservacionBDD(this);
            ds.open();
            ds.insert(reservacionBD);
            ds.close();
            new EmailSender(reservacionBD).execute();
            contador++;
            if (contador == titulares.size()) {
                Intent intent = new Intent(ReservacionPremiosActivity.this, HotelReservaResultActivity.class);
                intent.putExtra("numReservacion", reservacion);
                intent.putExtra("numHabitaciones", numHabitacion);
                intent.putExtra("precioHabitacion", precioHabitacion);
                intent.putExtra("numNoches", numNoches);
            /*intent.putExtra("total",getTotalCost());
            intent.putExtra("subtotal",getSubTotalCost());
            intent.putExtra("iva",getIVACost());*/
                if (getTotalCostAlt() == 0) {
                    intent.putExtra("total", getTotalCost());
                } else {
                    intent.putExtra("total", getTotalCostAlt());
                }
                if (subtotal == 0) {
                    intent.putExtra("subtotal", getSubTotalCost());
                } else {
                    intent.putExtra("subtotal", subtotal);
                }
                if (IVA == 0) {
                    intent.putExtra("iva", getIVACost());
                } else {
                    intent.putExtra("iva", IVA);
                }
                startActivity(intent);
            }
            System.out.println("Reservacion OK " + reservacion);
            contadorReservaciones++;
            enviarReservacion();

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
        }else{
            alert("Hubo un problema al tratar de hacer tu reservacion, vuelve a intentarlo");
        }
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


    public void obtenerListadoTarjetas() {
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

        enviarxml = enviarxml.replace("{IDF2GO}",""+personF2GO);


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
                try {
                    NetworkResponse response = error.networkResponse;
                    String datos = new String(response.data);
                    System.out.println("sout" + datos);
                }catch (Exception e){
                  e.printStackTrace();
                }

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
        registro.setRetryPolicy(new DefaultRetryPolicy(17000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                } catch (Exception e) {
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
        registro.setRetryPolicy(new DefaultRetryPolicy(17000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
    }


    public void FinenviarTarjeta( String mensaje) throws  Exception{
        alert("Tarjeta agregada con éxito");
        obtenerListadoTarjetas();
        linearaddTarjeta.setVisibility(View.VISIBLE);
        linearAgregarTarjeta.setVisibility(View.GONE);
        linearaddTarjeta.setVisibility(View.GONE);
        System.out.println("Tarjeta enviada con exito :D");
    }

    public void ErrorenviarTarjeta(String mensaje){
        System.out.println("ERROR AL AÑADIR TARJETA  " + mensaje);
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

    public void onScanPress(View view) {
        Log.d("escanear ", "€scanear");


        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, 100);
        addEvent("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            personF2GO=data.getStringExtra("result");
        }

        EditText txtCardNumber = (EditText) this.findViewById(R.id.txtCardNumber);
        Spinner spinExpMonth = (Spinner) this.findViewById(R.id.spinExpMonth);
        Spinner spinExpYear = (Spinner) this.findViewById(R.id.spinExpYear);
        EditText txtCardCode = (EditText) this.findViewById(R.id.txtCardCode);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.getFormattedCardNumber() + "\n";
            Log.d("Escanear redacted --> ", scanResult.getRedactedCardNumber());
            Log.d("Escanear formater --> ", scanResult.getFormattedCardNumber());
            //txtCardNumber.setText(scanResult.getRedactedCardNumber());
            txtCardNumber.setText(scanResult.getFormattedCardNumber());

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );


            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                //spinExpMonth.setSelection(scanResult.expiryMonth);
                //spinExpYear.setSelection(scanResult.expiryYear);
                Log.d("Escanear --> ", "spinner --> " + scanResult.expiryMonth + " - " + scanResult.expiryYear);


                spinExpMonth.setSelection(scanResult.expiryMonth - 0);
                spinExpYear.setSelection(scanResult.expiryYear - 2014);
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                txtCardCode.setText(scanResult.cvv);
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }




        } else {
            resultStr = "Scan was canceled.";
        }
        //resultTextView.setText(resultStr);
        Log.d("Escanear --> ", resultStr);

        //gato


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
        cbAfiliate.setChecked(true);
    }

    private void changeHuesped( int index )
    {
        clearCaptureFocus();
        saveCurrentGuest();
        _lastGuestIndex = index;
        if(index!=0){
            setGuestData();
        }

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

            String title = "Huésped titular - habitación " + (index + 1);

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
            alert("Falta registrar al Huésped titular - Habitación " + (index+1));
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
    /*    if( _paymentMethod == PAYMENT_METHOD_CARD )
        {

            EditText txtCardName = (EditText) findViewById(R.id.txtCardName);
            if( txtCardName.getText().toString().trim().length() == 0 )
            {
                alert( "Por favor ingresa el nombre del titular de la tarjeta de crédito." );
                return false;
            }

            EditText txtCardNumber = (EditText) findViewById(R.id.txtCardNumber);
            if( !CCUtils.validCC( txtCardNumber.getText().toString() ) )
            {
                alert( "El número de tarjeta es inválido." );
                return false;
            }

            Spinner spinExpMonth = (Spinner) findViewById(R.id.spinExpMonth);
            if( spinExpMonth.getSelectedItemPosition() == 0 )
            {
                alert( "Selecciona el mes de expiración de la tarjeta de crédito." );
                return false;
            }

            Spinner spinExpYear = (Spinner) findViewById(R.id.spinExpYear);
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
            }
        }
      /*  if(_paymentMethod == PAYMENT_METHOD_PAYPAL){

        }
            /*EditText txtCardName = (EditText) findViewById( R.id.txtCardName );


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

    private double getTotalCostAlt()
    {
        double total = 0;
        for( int i = 0; i < Math.abs(numNoches) ; i++ )
        {
            total += IVA+subtotal;
        }

        return total;
    }

    private double getSubTotalCost(){
        double total = 0;
        for( int i = 0; i < Math.abs(numNoches) ; i++ )
        {
            //total += subtotalHabitacion;
            if(subtotalHabitacion2==0){
                total += subtotalHabitacion;
            }else{
                total += subtotalHabitacion2;
            }
        }

        return (total*numHabitacion);
    }

    private double getIVACost(){
        double total = 0;
        for( int i = 0; i < Math.abs(numNoches) ; i++ )
        {
            //total += IVAHabitacion;
            if(IVAHabitacion2==0){
                total += IVAHabitacion;
            }else{
                total += IVAHabitacion2;
            }
        }

        return (total*numHabitacion);
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
            /*d.setName( txtName.getText().toString() );
            d.setLastName( txtLast.getText().toString() );
            d.setEmail( txtEmail.getText().toString() );
            d.setSocio( txtSocio.getText().toString() );
            d.setAfiliate( cbAfiliate.isChecked() );
            d.setPhone( txtPhone.getText().toString() );
            d.setViaje( spinViaje.getSelectedItemPosition() );*/
            d.setName(txtName.getText().toString());
            d.setLastName(txtLast.getText().toString());
            d.setEmail(txtEmail.getText().toString());
            d.setSocio(txtSocio.getText().toString());
            d.setAfiliate(cbAfiliate.isChecked());
            d.setPhone(txtPhone.getText().toString());
            d.setViaje(spinViaje.getSelectedItemPosition());
            if(subtotalHabitacion2 == 0){
                d.setPrecio(subtotalHabitacion);
            }else{
                d.setPrecio(subtotalHabitacion2);
            }
            if(IVAHabitacion2 == 0){
                d.setIva(IVAHabitacion);
            }else{
                d.setIva(IVAHabitacion2);
            }
            d.setAdultos(spinAdultos.getSelectedItemPosition());
            d.setNinos(spinNinos.getSelectedItemPosition());
        }
        if(_lastGuestIndex>0 && btnMisma.isChecked()){
            System.out.println("ENTREACA");
            d.setName( txtName.getText().toString());
            d.setLastName( txtLast.getText().toString());
            d.setEmail( txtEmail.getText().toString());
            d.setSocio(txtSocio.getText().toString());
            d.setAfiliate(cbAfiliate.isChecked());
            d.setPhone( txtPhone.getText().toString() );
            d.setViaje( spinViaje.getSelectedItemPosition() );
            if(subtotalHabitacion2 == 0){
                d.setPrecio(subtotalHabitacion);
            } else {
                d.setPrecio(subtotalHabitacion2);
            }
            d.setAdultos(spinAdultos.getSelectedItemPosition());
            d.setNinos(spinNinos.getSelectedItemPosition());
        }
        //d.setAdultos(spinAdultos.getSelectedItemPosition());
        //d.setNinos(spinNinos.getSelectedItemPosition());
        numAdultos=0;
        numInfantes = 0;
        //spinAdultos.setSelection(0);
        //spinNinos.setSelection(0);
        guess=d;
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


        //prepareNinosSpinner( d.getAdultos() + 1 ); // Acompañantes + Titular
        //spinAdultos.setSelection( d.getAdultos() );
        //spinNinos.setSelection( d.getNinos() );

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
            spinAdultos.setEnabled(true);
            spinNinos.setEnabled(true);
        }
        else
        {
            d = titulares.get( _lastGuestIndex );
            if( d.getDataOption() == 0 )
            {
                btnMisma.setChecked( false );
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
                //spinAdultos.setEnabled(false);
                //spinNinos.setEnabled(false);
                sumary.clear();
                preciosList.clear();
                ivaList.clear();
                subtotal =0;
                IVA = 0;
                for (int k = 0; k < numHabitacion; k++) {
                    if(k==numHuesped){
                        sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                        for (int l = 0; l < Math.abs(numNoches); l++) {
                            sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + d.getPrecio() + " M.N"));//precioHabitacion));
                            preciosList.add(""+d.getPrecio());
                            subtotal+=d.getPrecio();
                            IVA += d.getIva();
                            ivaList.add(""+d.getIva());
                        }
                    }else{
                        sumary.add(new SummaryEntry(0, "Habitación " + (k + 1)));
                        for (int l = 0; l < Math.abs(numNoches); l++) {
                            sumary.add(new SummaryEntry(1, "Noche " + (l + 1) + " $" + d.getPrecio() + " M.N" ));//precioHabitacion));
                            subtotal+=d.getPrecio();
                            preciosList.add(d.getPrecio()+"");
                            IVA +=d.getIva();
                            ivaList.add(""+d.getIva());
                        }
                    }

                }

                NestedListView list = (NestedListView) findViewById(R.id.list_summary);
                NestedListView list2 = (NestedListView) findViewById(R.id.list_summary2);
                SummaryListAdapter adapter = new SummaryListAdapter(this, sumary);
                list.setAdapter(adapter);
                list2.setAdapter(adapter);
                lblSubTotal.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");
                lblSubTotal2.setText(String.format("Subtotal: $%,.2f ", subtotal) + "M.N");
                lblIVA.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                lblIVA2.setText(String.format("Impuestos: $%,.2f ", IVA) + "M.N");
                lblTotal.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");//String.format( "Total: $%,.2f ",1123 ) );
                lblTotal2.setText(String.format("Total: $%,.2f ", getTotalCostAlt()) + "M.N");// Str
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
                spinAdultos.setEnabled(true);
                spinNinos.setEnabled(true);
            }
        }

        txtName.setText(d.getName());
        txtLast.setText(d.getLastName());
        txtEmail.setText( d.getEmail() );
        txtSocio.setText(d.getSocio());
        cbAfiliate.setChecked(d.isAfiliate());
        txtPhone.setText(d.getPhone());
        spinViaje.setSelection(d.getViaje());
        spinAdultos.setSelection(d.getAdultos());
        spinNinos.setSelection(d.getNinos());
    }

    /**
     * PAyPAL
     */
    private void handleChangePaymentMethod()
    {
        RadioButton btnCardMethod = (RadioButton) findViewById(R.id.btn_method_card);
        Button btnReserva = (Button) findViewById(R.id.btnReserva);
        if( btnCardMethod.isChecked() )
        {
            btnReserva.setText( "Reserva" );
            _paymentMethod = PAYMENT_METHOD_CARD;
            linearaddTarjeta.setVisibility(View.VISIBLE);
        }
        else
        {
            btnReserva.setText( "Ingresar a PayPal" );
            _paymentMethod = PAYMENT_METHOD_PAYPAL;
            linearpayTarjeta.setVisibility(View.GONE);
            linearAgregarTarjeta.setVisibility(View.GONE);
            linearaddTarjeta.setVisibility(View.GONE);
        }
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

    public class GuestData
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
        private double precio;
        private double iva;

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

        public double getIva() {
            return iva;
        }

        public void setIva(double iva) {
            this.iva = iva;
        }

        public double getPrecio() {
            return precio;
        }

        public void setPrecio(double precio) {
            this.precio = precio;
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

    private class EmailSender extends AsyncTask<Void, Void, Void>
    {
        private ReservacionBD _results;

        public EmailSender( ReservacionBD results )
        {
            _results = results;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            /*_progress = ProgressDialogFragment.newInstance( "Enviando respuesta..." );
            _progress.setCancelable( false );
            _progress.show( getSupportFragmentManager(), "Dialog" );*/
        }

        @Override
        protected Void doInBackground( Void... arg0 )
        {
            ///////////////////////
            String para = "";
            ArrayList<String> ccs = new ArrayList<String>();
            ArrayList<String> afiliates = new ArrayList<String>();
            String codigos = "";
            String reservantes = "";
            double total = 0;

            ReservacionBD huesped = _results;
            String correo = huesped.getEmailUsuario();
            //String reservante = "[" + huesped.getNombreUsuario() + " " + huesped.getApellidoUsuario() + "^" + descripcionHabitacionJSON + "^" + "1" + "^" + numNoches + "^" + precioHabitacion + "]";
            String reservante = "[" + huesped.getNombreUsuario() + " " + huesped.getApellidoUsuario() + "^" + descripcionHabitacionJSON + "^" + ""+numAdultos + "^" + numNoches + "^" + totalReservacion + "]";
            reservante = reservante.replaceAll( ",", " " );

               /* total += Double.parseDouble(_results.getHabCosto());
                //if( i == 0 )
               // {
                    para = huesped.getEmailUsuario();
                    codigos += _results.getNumReservacion();
                    reservantes += reservante;
                //}
               /* else
                {
                    if( !para.equalsIgnoreCase( correo ) && !isInList( correo, ccs ) )
                    {
                        ccs.add( correo );
                    }
                    codigos += "," + _results.getNumReservacion();
                    reservantes += "," + reservante;
                }*/


           /* for( int i = 0; i < titulares.size(); i++ )
            {
                GuestData g = titulares.get( i );
                if( i == 0 )
                {
                    if( g.isAfiliate() )
                    {
                        afiliates.add( g.getEmail() );
                    }
                }
                else if( g.isAfiliate() && g.getDataOption() != 0 )
                {
                    if( !isInList( g.getEmail(), afiliates ) )
                    {
                        afiliates.add( g.getEmail() );
                    }
                }
            }*/

            /*String cctext = "";
            for( int i = 0; i < ccs.size(); i++ )
            {
                if( i == 0 )
                {
                    cctext = ccs.get( i );
                }
                else
                {
                    cctext += "," + ccs.get( i );
                }
            }

            String afiliatestext = "";
            for( int i = 0; i < afiliates.size(); i++ )
            {
                if( i == 0 )
                {
                    afiliatestext = afiliates.get( i );
                }
                else
                {
                    afiliatestext += "," + afiliates.get( i );
                }
            }*/

            SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add( new BasicNameValuePair( "Para", _results.getEmailUsuario() ) );
            params.add( new BasicNameValuePair( "CC", "" ) );
            params.add( new BasicNameValuePair( "CodigosReservacion", _results.getNumReservacion()+"" ) );
            params.add( new BasicNameValuePair( "CodigoHotel", _hotel.getSiglas() ) );
            params.add( new BasicNameValuePair( "FInicioFfinal", sdf.format( arrival ) + "," + sdf.format( departure ) ) );
            params.add( new BasicNameValuePair( "NumHabitaciones", numHabitacion + "" ) );
            params.add( new BasicNameValuePair( "idioma", "es" ) );
            params.add( new BasicNameValuePair( "DatosReservantes", reservante ) );
            params.add( new BasicNameValuePair( "CostoTotal", _results.getTotal()));//String.format( Locale.US, "%.2f", total ) ) );
            params.add( new BasicNameValuePair( "subTotal", ""+_results.getSubtotal() ) );
            params.add( new BasicNameValuePair( "IVA", ""+ _results.getIva() ) );
            params.add( new BasicNameValuePair( "cuentasAfiliacion", "" ) );

            ServiceHandler handler = new ServiceHandler();
            String response = handler.makeServiceCall( APIAddress.HOTELS_API_MOBILE + "/EnvioCorreosIVA", ServiceHandler.GET, params );
            Log.d( "TEST", "EMAIL: " + response );

            return null;
        }

        @Override
        protected void onPostExecute( Void arg0 ) {
            super.onPostExecute(arg0 );
            //_progress.dismiss();
        }

        private boolean isInList( String text, ArrayList<String> list )
        {
            for( int i = 0; i < list.size(); i++ )
            {
                if( list.get( i ).equalsIgnoreCase( text ) )
                {
                    return true;
                }
            }

            return false;
        }
    }
    /**
     *
     */
    PayPalCaller.PayPalPayment _payPalPayment;
    PayPalCaller.PayPalSECResponse _payPalSECResponse;
    PayPalCaller.PayPalReturnResponse _payPalReturnResponse;
    PayPalCaller.PayPalDECParameters _payPalDECParameters;
    PayPalCaller.PayPalDECResponse _payPalDECResponse;
    PayPalCaller.PayPalRTParameters _payPalRTParameters;

    private PayPalCaller.PayPalPayment getPayPalPayment()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );

        double total = getTotalCost();

        if(getTotalCostAlt()==0){
            total = getTotalCost();
        }else{
            total = getTotalCostAlt();
        }
        PayPalCaller.PayPalPayment payment = new PayPalCaller.PayPalPayment();
        payment.setUser( _hotel.getMerchantUserName() );
        payment.setPassword( _hotel.getMerchantPassword() );
        payment.setSignature( _hotel.getSignature() );
        payment.setReturnURL( "http://www.zebstudiospayment-return.com" );
        payment.setCancelURL( "http://www.zebstudiospayment-cancel.com" );
        payment.setItemName( "Reservante: " + titulares.get( 0 ).getName() + " " + titulares.get( 0 ).getLastName() );
        payment.setItemSKU( sdf.format( arrival ) + " a " + sdf.format( departure ) );
        payment.setItemDesc( numHabitacion + " habitaciones en: " + descripcionHabitacionJSON );
        payment.setItemAmount( String.format( Locale.US, "%.2f", total ) );
        payment.setAmount( String.format( Locale.US, "%.2f", total ) );
        payment.setCurrencyCode( "MXN" );

        if( _hotel.getIdMarca() == 4 )
        {
            payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress-plus.png" );
            payment.setCartBorderColor( "221c35" );
        }
        else if( _hotel.getIdMarca() == 3 )
        {
            payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress-suites.png" );
            payment.setCartBorderColor( "8b2131" );
        }
        else if( _hotel.getIdMarca() == 2 )
        {
            payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress-junior.png" );
            payment.setCartBorderColor( "004d41" );
        }
        else
        {
            payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress.png" );
            payment.setCartBorderColor( "071689" );
        }

        return payment;
    }

    public void onPayPalPaymentInitiated( PayPalCaller.PayPalSECResponse response )
    {
        _payPalSECResponse = response;
        android.util.Log.d( "TEST", "PAYMENT INITIATED" );
        if( _payPalSECResponse.getACK().equalsIgnoreCase( "SUCCESS" ) )
        {
            android.util.Log.d( "TEST", "TOKEN: " + _payPalSECResponse.getToken() );

            _isShowingWebView = true;

            _progress = ProgressDialogFragment.newInstance();
            _progress.setCancelable( false );
            LinearLayout pnlCapture = (LinearLayout) findViewById(R.id.pnlCapture);
            WebView webView = (WebView) findViewById(R.id.webView);
            pnlCapture.setVisibility( View.GONE );
            webView.clearView();
            //TODO: Utilizar liga de acuerdo a si es producción o sandbox
            webView.loadUrl( APIAddress.PAYPAL_MERCHANT_WEB + "?cmd=_express-checkout&token=" + _payPalSECResponse.getToken() );
            webView.setVisibility( View.VISIBLE );
        }
        else
        {
            android.util.Log.d( "TEST", "ERROR: " + _payPalSECResponse.getLongMessage() );
            alert( "PayPal no disponible. Por favor intente nuevamente más tarde." );
        }
    }

    private void onPayPalPaymentCancelled()
    {
        LinearLayout pnlCapture = (LinearLayout) findViewById(R.id.pnlCapture);
        WebView webView = (WebView) findViewById(R.id.webView);
        pnlCapture.setVisibility( View.VISIBLE );
        webView.setVisibility( View.GONE );
    }

    private void onPayPalPaymentReturned( String queryString )
    {
        android.util.Log.d( "TEST", "PAYPAL RETURN: " + queryString );

        LinearLayout pnlCapture = (LinearLayout) findViewById(R.id.pnlCapture);
        WebView webView = (WebView) findViewById(R.id.webView);
        pnlCapture.setVisibility( View.VISIBLE );
        webView.setVisibility( View.GONE );
        _isShowingWebView = false;

        _payPalReturnResponse = new PayPalCaller.PayPalReturnResponse( queryString );
        if( _payPalReturnResponse.getToken() != null && _payPalReturnResponse.getPayerId() != null )
        {
            if( _payPalReturnResponse.getToken().equalsIgnoreCase( _payPalSECResponse.getToken() ) )
            {
                _payPalDECParameters = new PayPalCaller.PayPalDECParameters();
                _payPalDECParameters.setUser( _hotel.getMerchantUserName() );
                _payPalDECParameters.setPassword( _hotel.getMerchantPassword() );
                _payPalDECParameters.setSignature( _hotel.getSignature() );
                _payPalDECParameters.setToken( _payPalReturnResponse.getToken() );
                _payPalDECParameters.setPayerId( _payPalReturnResponse.getPayerId() );
                _payPalDECParameters.setAmount( _payPalPayment.getAmount() );
                _payPalDECParameters.setCurrency( _payPalPayment.getCurrencyCode() );

                new PayPalCaller( this, PayPalCaller.DO_EXPRESS_CHECKOUT, _payPalDECParameters,this, _progress ).execute();
            }
            else
            {
                alert( "Datos de PayPal corruptos. Por favor intente nuevamente más tarde." );
            }
        }
        else
        {
            alert( "Error de comunicación con PayPal. Por favor intente nuevamente más tarde." );
        }
    }

    public void onPayPalDoExpressCheckOut( PayPalCaller.PayPalDECResponse response )
    {
        android.util.Log.d( "TEST", "EXPRESS CHECKOUT DONE" );
        _payPalDECResponse = response;
        if( _payPalDECResponse.getACK().equalsIgnoreCase( "Success" ) )
        {
            if( _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Pending" )  )
            {
                android.util.Log.d( "TEST", "PENDING LESS THAN 48" );
                _payPalRTParameters = new PayPalCaller.PayPalRTParameters();
                _payPalRTParameters.setUser( _hotel.getMerchantUserName() );
                _payPalRTParameters.setPassword( _hotel.getMerchantPassword() );
                _payPalRTParameters.setSignature( _hotel.getSignature() );
                _payPalRTParameters.setPayerId( _payPalReturnResponse.getPayerId() );
                _payPalRTParameters.setCurrency( _payPalDECResponse.getCourrencyCode() );
                _payPalRTParameters.setTransactionId( _payPalDECResponse.getTransactionId() );

                new PayPalCaller( this, PayPalCaller.REFUND_TRANSACTION, _payPalRTParameters, this, _progress ).execute();
            }
            else if( _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Completed" ) || _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Processed" ) || _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Pending" ) )
            {
                android.util.Log.d( "TEST", "OK: " + _payPalDECResponse.getPaymentStatus() );
                initiatePayPalReservation();
            }
            else
            {
                android.util.Log.d( "TEST", "ELSE: " + _payPalDECResponse.getPaymentStatus() );
                alert( "No se ha podido realizar su pago con PayPal. Por favor intente nuevamente más tarde." );
            }
        }
        else
        {
            alert( "Error de comunicación con PayPal. Por favor intente nuevamente más tarde." );
            android.util.Log.d( "PAYPAL", "ERROR: " + _payPalDECResponse.getLongMessage() );
        }
    }

    public void onPayPalRefundTransaction( PayPalCaller.PayPalRTResponse response )
    {
        android.util.Log.d( "TEST", "REFUND TRANSACTION DONE" );
        if( response.getACK().equalsIgnoreCase( "Success" ) )
        {
            alert( "No se ha podido realizar su pago con PayPal. Fecha de reservación menor a 48 horas." );
        }
        else
        {
            //TODO: Dar un mensaje más descriptivo de este error
            alert( "No se ha podido realizar su pago con PayPal. Fecha de reservación menor a 48 horas." );
        }
    }

    private void initiatePayPalReservation()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        long timeOne = arrival.getTime();
        long timeTwo = departure.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = ( timeTwo - timeOne ) / oneDay;

        ArrayList<ReservationEngineClient.ReservationModelv3_01> reservations = new ArrayList<ReservationEngineClient.ReservationModelv3_01>();
        for( int i = 0; i < _rooms.size(); i++ )
        {
            HabitacionBase room = _rooms.get( i );
            GuestData d = titulares.get( i );
            GuestData o = titulares.get( i );
            if( i != 0 && d.getDataOption() == 0 )
            {
                o = titulares.get( 0 );
            }

            ReservationEngineClient.ReservationModelv3_01 reserva = new ReservationEngineClient.ReservationModelv3_01();
            reserva.getEmpresa().setRfcEmisor( "EMISAPP" );

            reserva.getEstancia().setCodigoOperador( "APP_Movil" );
            reserva.getEstancia().setCodigoOrigen( "007" );
            reserva.getEstancia().setFechaEntrada( arrival );
            reserva.getEstancia().setHotel( _hotel.getSiglas() );
            reserva.getEstancia().setNumeroDeNoches( (int) delta );
            reserva.getEstancia().setTipoReservacion( "DEPO" );
            reserva.getEstancia().setFormaDePago( "PAYPAL" );
            reserva.getEstancia().setCodigoSegmento( o.getViaje() == 0 ? "001" : "015" );

            reserva.getHabitacion().setCodigoHabitacion( room.getCodigoBase() );
            reserva.getHabitacion().setCodigoPromocion( "" );
            reserva.getHabitacion().setCodigoTarifa( room.getCosto() );
            reserva.getHabitacion().setNumeroHabitaciones( 1 );

            reserva.getHabitacion().getHuespedTitular().setNombre( o.getName() );
            reserva.getHabitacion().getHuespedTitular().setApellidos( o.getLastName() );
            reserva.getHabitacion().getHuespedTitular().setCorreoElectronico( o.getEmail() );
            reserva.getHabitacion().getHuespedTitular().setTelefono( o.getPhone() );
            reserva.getHabitacion().getHuespedTitular().setRwdNumber( o.getSocio() );
            reserva.getHabitacion().getHuespedTitular().setTotAcompAdult( d.getAdultos() );
            reserva.getHabitacion().getHuespedTitular().setTotAcompMenor( d.getNinos() );
            reserva.getHabitacion().getHuespedTitular().setCodigoPais( "MEX" );
            // TODO: Cuál es el código de país que debe llevar el huesped titular

            reserva.getDeposito().setComprobante( _payPalDECResponse.getTransactionId() );
            reserva.getDeposito().setFecha( sdf.format( cal.getTime() ) );
            reserva.getDeposito().setMonto( Float.parseFloat( _payPalDECResponse.getAmount() ) );
            reserva.getDeposito().setTipoMoneda( _payPalDECResponse.getCourrencyCode() );
            reserva.getDeposito().setNotas( "PAGO PAYPAL. DEPOSITO POR ESTANCIA COMPLETA" );
            if( _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "PENDING" ) )
            {
                reserva.getDeposito().setNotas2( "PENDING" );
            }
            else
            {
                reserva.getDeposito().setNotas2( "OK" );
            }

            reservations.add( reserva );
        }

    }

    private class PayPalWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted( WebView view, String url, Bitmap favicon )
        {
            /*if( _progress != null && !_progress.isVisible() )
            {
                _progress.setCancelable( false );
                _progress.show( this, "DIALOG" );
            }*/
        }

        @Override
        public void onPageFinished( WebView view, String url )
        {
            /*if( _progress != null && _progress.isVisible() )
            {
                _progress.dismiss();
            }*/
        }

        @Override
        public boolean shouldOverrideUrlLoading( WebView view, String url )
        {
            android.util.Log.d( "WEBVIEW", "CALLING: " + url );
            if( url.startsWith( "http://www.zebstudiospayment-return.com" ) )
            {
               /* if( _progress != null && _progress.isVisible() )
                {
                    _progress.dismiss();
                }*/
                android.util.Log.d( "WEBVIEW", "ACEPTED" );
                onPayPalPaymentReturned( url.replace( "http://www.zebstudiospayment-return.com/?", "" ) );
                return true;
            }
            else if( url.startsWith( "http://www.zebstudiospayment-cancel.com" ) )
            {
               /* if( _progress != null && _progress.isVisible() )
                {
                    _progress.dismiss();
                }*/
                android.util.Log.d( "WEBVIEW", "CANCELLED" );
                onPayPalPaymentCancelled();
                return true;
            }
            else
                return false;
        }
    }
    /**
     *
     */


}
