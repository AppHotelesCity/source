package com.zebstudios.cityexpress;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zebstudios.calendar.CalendarFragment;
import com.zebstudios.calendar.CalendarListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PrincipalFragment extends Fragment implements View.OnClickListener{

    private View view;
    Button btnIniciarSesion;
    Button btnRegistrarme;
    EditText editTextHotelDestino;
    LinearLayout linearSalida;
    LinearLayout linearLlegada;
    EditText editTextPromoCode;
    Button btnDisponibilidad;
    TextView txtLlegada;
    TextView txtSalida;
    TextView textNombreUsuario;
    LinearLayout linearIngresaDatos;
    LinearLayout linearLayoutSinLog;
    LinearLayout linearUsuario;
    SimpleDateFormat formatoFechaJSON;
    String fecha;
    boolean usuarioActivo;

    static ProgressDialog progress;


    static Date _arrivalDate;
    private CalendarFragment _arrivalCalendarFragment;
    private CalendarListener _arrivalCalendarListener;
    static Date _departureDate;
    private CalendarFragment _departureCalendarFragment;
    private CalendarListener _departureCalendarListener;


    public static PrincipalFragment newInstance(String param1, String param2) {
        PrincipalFragment fragment = new PrincipalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PrincipalFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   @Override
    public void onResume(){
        super.onResume();
       SharedPreferences prefsUsuario = getActivity().getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
       usuarioActivo = Boolean.parseBoolean(prefsUsuario.getString("activo",null));
       if(usuarioActivo){
           btnIniciarSesion.setVisibility(View.GONE);
           btnRegistrarme.setVisibility(View.GONE);
           linearLayoutSinLog.setVisibility(View.GONE);
           linearUsuario.setVisibility(View.VISIBLE);
           textNombreUsuario.setText(prefsUsuario.getString("nombre",null) +" "+ prefsUsuario.getString("apellido",null));
       }

       ActionBarActivity activity = (ActionBarActivity) getActivity();
       activity.getSupportActionBar().setTitle("Principal");
       activity.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.blue_button));


   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_principal, container, false);

        btnIniciarSesion = (Button) view.findViewById(R.id.btnIniciarSesion);
        btnRegistrarme = (Button) view.findViewById(R.id.btnRegistarme);
        editTextHotelDestino = (EditText) view.findViewById(R.id.edtxtHotelDestino);
        linearSalida = (LinearLayout) view.findViewById(R.id.linearSalida);
        linearLlegada = (LinearLayout) view.findViewById(R.id.linearLlegada);
        editTextPromoCode = (EditText) view.findViewById(R.id.editTextPromoCode);
        btnDisponibilidad = (Button) view.findViewById(R.id.btnDisponibilidad);
        txtLlegada = (TextView)view.findViewById(R.id.txt_llegada);
        txtSalida =(TextView)view.findViewById(R.id.txt_salida);
        textNombreUsuario = (TextView) view.findViewById(R.id.textNombreUsuario);

        linearUsuario = (LinearLayout) view.findViewById(R.id.linearUsario);
        linearIngresaDatos = (LinearLayout) view.findViewById(R.id.linearIngresaDatos);

        linearLayoutSinLog = (LinearLayout) view.findViewById(R.id.linearRegistro);

        formatoFechaJSON = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        PrepareArrivalCalendar();
        PrepareDepartureCalendar();


        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR); //obtenemos el año
        int mes = c.get(Calendar.MONTH); //obtenemos el mes
        String mesT = Integer.toString(mes);
        int dia = c.get(Calendar.DAY_OF_MONTH); // obtemos el día.
        switch (mesT){
            case "0":
                mesT = "ene";
                break;
            case "1":
                mesT = "feb";
                break;
            case "2":
                mesT = "mar";
                break;
            case "3":
                mesT = "abr";
                break;
            case "4":
                mesT = "may";
                break;
            case "5":
                mesT = "jun";
                break;
            case "6":
                mesT = "jul";
                break;
            case "7":
                mesT = "ago";
                break;
            case "8":
                mesT = "sep";
                break;
            case "9":
                mesT = "oct";
                break;
            case "10":
                mesT = "nov";
                break;
            case "11":
                mesT = "dic";
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
        _arrivalDate = c.getTime();
        txtLlegada.setText(sdf.format(_arrivalDate));

        _arrivalDate.getTime();
        System.out.println("Arrival->"+ _arrivalDate.getTime());
        Calendar cs = Calendar.getInstance();
        cs.setTime(_arrivalDate);
        cs.add(Calendar.DATE, 1);

        _departureDate = cs.getTime();

        System.out.println("Salida->"+sdf.format(_departureDate));;
        txtSalida.setText(sdf.format(_departureDate));

        SharedPreferences prefsUsuario = getActivity().getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
        usuarioActivo = Boolean.parseBoolean(prefsUsuario.getString("activo",null));
        if(usuarioActivo){
            btnIniciarSesion.setVisibility(View.GONE);
            btnRegistrarme.setVisibility(View.GONE);
            linearLayoutSinLog.setVisibility(View.GONE);
            linearUsuario.setVisibility(View.VISIBLE);
            textNombreUsuario.setText(prefsUsuario.getString("nombre",null) +" "+ prefsUsuario.getString("apellido",null));
        }


        btnIniciarSesion.setOnClickListener(this);
        btnRegistrarme.setOnClickListener(this);
        linearSalida.setOnClickListener(this);
        linearLlegada.setOnClickListener(this);
        btnDisponibilidad.setOnClickListener(this);
        return view;
    }

                    /*********  AQUI SE LLENA CALENDARIO SALIDA ********/
    private void PrepareDepartureCalendar()
    {
        _departureDate = null;
        _departureCalendarListener = new CalendarListener()
        {
            @Override
            public void onSelectDate( Date date, View view )
            {
                _departureDate = date;
                if( _arrivalDate != null && _arrivalDate.compareTo( _departureDate ) >= 0 )
                {

                    //txtSalida.setText("AKLDCMALKSMDCLSKMDSACKLS");
                    _arrivalDate = null;
                }
                SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
                txtSalida.setText( sdf.format( date ) );
                _departureCalendarFragment.dismiss();


            }
        };

        Bundle bundle = new Bundle();
        bundle.putString( CalendarFragment.DIALOG_TITLE, "Llegada" );
        _departureCalendarFragment = new CalendarFragment();
        _departureCalendarFragment.setCalendarListener(_departureCalendarListener);
        _departureCalendarFragment.setArguments(bundle);



        txtSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                //if( _arrivalDate != null )
                //	cal.setTime( _arrivalDate );
                cal.add( Calendar.DATE, 1 );
                Date d = cal.getTime();
                if( _departureDate != null )
                {
                    _departureCalendarFragment.setSelectedDates( _departureDate, _departureDate );
                }
                _departureCalendarFragment.setMinDate( d );
                _departureCalendarFragment.show( getFragmentManager(), "TO_CALENDAR_FRAGMENT" );
            }
        });
    }

    /****** AQUI SE LLENA CALENDARIO LLEGADA ******/

    private void PrepareArrivalCalendar()
    {
        _arrivalDate = null;
        _arrivalCalendarListener = new CalendarListener()
        {
            @Override
            public void onSelectDate( Date date, View view )
            {
                _arrivalDate = date;
                if( _departureDate != null && _departureDate.compareTo( _arrivalDate ) <= 0 )
                {
                    //txtLlegada.setText("TXT LLEGADA");
                    _departureDate = null;
                }
                SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
                txtLlegada.setText(sdf.format(date));
                _arrivalCalendarFragment.dismiss();

                Calendar c = Calendar.getInstance();
                c.setTime( date );
                c.add( Calendar.DATE, 1 );

                _departureDate = c.getTime();
                txtSalida.setText(sdf.format(_departureDate));

            }
        };

        Bundle bundle = new Bundle();
        bundle.putString( CalendarFragment.DIALOG_TITLE, "Salida" );
        _arrivalCalendarFragment = new CalendarFragment();
        _arrivalCalendarFragment.setCalendarListener( _arrivalCalendarListener );
        _arrivalCalendarFragment.setArguments( bundle );

        txtLlegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Date d = cal.getTime();
                if( _arrivalDate != null )
                {
                    _arrivalCalendarFragment.setSelectedDates( _arrivalDate, _arrivalDate );
                }
                _arrivalCalendarFragment.setMinDate(d);
                _arrivalCalendarFragment.show(getFragmentManager(), "FROM_CALENDAR_FRAGMENT");
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.btnIniciarSesion:
                intent = new Intent(getActivity(),IniciarSesionActivity.class);
                intent.putExtra("menu",true);
                startActivity(intent);
                break;
            case R.id.btnRegistarme:
                intent = new Intent(getActivity(),RegistroActivity.class);
                startActivity(intent);
                break;
            case R.id.linearSalida:
                Calendar cale = Calendar.getInstance();
                Date day = cale.getTime();
                if( _arrivalDate != null )
                {
                    _arrivalCalendarFragment.setSelectedDates( _arrivalDate, _arrivalDate );
                }
                _arrivalCalendarFragment.setMinDate(day);
                _arrivalCalendarFragment.show(getFragmentManager(), "FROM_CALENDAR_FRAGMENT");
                break;

            case R.id.linearLlegada:
                Calendar cal = Calendar.getInstance();
                //if( _arrivalDate != null )
                //	cal.setTime( _arrivalDate );
                cal.add(Calendar.DATE, 1);
                Date d = cal.getTime();
                if( _departureDate != null )
                {
                    _departureCalendarFragment.setSelectedDates( _departureDate, _departureDate );
                }
                _departureCalendarFragment.setMinDate(d);
                _departureCalendarFragment.show( getFragmentManager(), "TO_CALENDAR_FRAGMENT" );

            break;
            case R.id.btnDisponibilidad:


                if("".equals(editTextHotelDestino.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("City Express")
                            .setMessage("Ingrese un texto en la búsqueda.")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{

                    intent = new Intent(getActivity(),ResultadosDisponibilidad.class);
                    intent.putExtra("busqueda",editTextHotelDestino.getText().toString());
                    startActivity(intent);

                }
                break;
        }
    }
}
