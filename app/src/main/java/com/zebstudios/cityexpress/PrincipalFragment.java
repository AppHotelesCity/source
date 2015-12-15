package com.zebstudios.cityexpress;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class PrincipalFragment extends Fragment implements View.OnClickListener{

    Button btnIniciarSesion;
    Button btnRegistrarme;
    EditText editTextHotelDestino;
    LinearLayout linearSalida;
    LinearLayout linearLlegada;
    EditText editTextPromoCode;
    Button btnDisponibilidad;
    TextView txtLlegada;
    TextView txtSalida;

    SimpleDateFormat formatoFecha;
    SimpleDateFormat formatoFechaJSON;
    DatePickerDialog fechaLlegada;
    DatePickerDialog fechaSalida;
    String fecha;
    String mes;
    boolean usuarioActivo;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        btnIniciarSesion = (Button) view.findViewById(R.id.btnIniciarSesion);
        btnRegistrarme = (Button) view.findViewById(R.id.btnRegistarme);
        editTextHotelDestino = (EditText) view.findViewById(R.id.edtxtHotelDestino);
        linearSalida = (LinearLayout) view.findViewById(R.id.linearSalida);
        linearLlegada = (LinearLayout) view.findViewById(R.id.linearLlegada);
        editTextPromoCode = (EditText) view.findViewById(R.id.editTextPromoCode);
        btnDisponibilidad = (Button) view.findViewById(R.id.btnDisponibilidad);
        txtLlegada = (TextView)view.findViewById(R.id.txt_llegada);
        txtSalida =(TextView)view.findViewById(R.id.txt_salida);

        formatoFechaJSON = new SimpleDateFormat("yyyy-MM-dd", Locale.US);




        txtLlegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaLlegada.show();
            }
        });

        Calendar CalendarLlegada = Calendar.getInstance();
        fechaLlegada = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                switch (monthOfYear){
                    case 0:
                        mes = "ene";
                        break;
                    case 1:
                        mes = "feb";
                        break;
                    case 2:
                        mes = "mar";
                        break;
                    case 3:
                        mes = "abr";
                        break;
                    case 4:
                        mes = "may";
                        break;
                    case 5:
                        mes = "jun";
                        break;
                    case 6:
                        mes = "jul";
                        break;
                    case 7:
                        mes = "ago";
                        break;
                    case 8:
                        mes = "sep";
                        break;
                    case 9:
                        mes = "oct";
                        break;
                    case 10:
                        mes = "nov";
                        break;
                    case 11:
                        mes = "dic";
                        break;
                }
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtLlegada.setText(dayOfMonth +" "+ mes +" "+ year);
                System.out.println(dayOfMonth +" "+ mes +" "+ year);

                if(dayOfMonth < Calendar.DAY_OF_MONTH){
                    Toast.makeText(getActivity(), "adscacd", Toast.LENGTH_SHORT).show();
                }
                fecha = formatoFechaJSON.format(newDate.getTime());
            }

        }, CalendarLlegada.get(Calendar.YEAR), CalendarLlegada.get(Calendar.MONTH), CalendarLlegada.get(Calendar.DAY_OF_MONTH));

        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR); //obtenemos el año
        int mesT = c.get(Calendar.MONTH); //obtenemos el mes
        int dia = c.get(Calendar.DAY_OF_MONTH); // obtemos el día.
        switch (mesT){
            case 0:
                mes = "ene";
                break;
            case 1:
                mes = "feb";
                break;
            case 2:
                mes = "mar";
                break;
            case 3:
                mes = "abr";
                break;
            case 4:
                mes = "may";
                break;
            case 5:
                mes = "jun";
                break;
            case 6:
                mes = "jul";
                break;
            case 7:
                mes = "ago";
                break;
            case 8:
                mes = "sep";
                break;
            case 9:
                mes = "oct";
                break;
            case 10:
                mes = "nov";
                break;
            case 11:
                mes = "dic";
                break;
        }
        txtLlegada.setText(dia+" " + mes +" "+anio); //cambiamos el texto que tiene el TextView por la fecha actual.

        txtSalida.setText(dia+" " + mes +" "+anio);

        txtSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaSalida.show();
            }
        });

        Calendar CalendarSalida = Calendar.getInstance();
        fechaSalida = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                switch (monthOfYear){
                    case 0:
                        mes = "ene";
                        break;
                    case 1:
                        mes = "feb";
                        break;
                    case 2:
                        mes = "mar";
                        break;
                    case 3:
                        mes = "abr";
                        break;
                    case 4:
                        mes = "may";
                        break;
                    case 5:
                        mes = "jun";
                        break;
                    case 6:
                        mes = "jul";
                        break;
                    case 7:
                        mes = "ago";
                        break;
                    case 8:
                        mes = "sep";
                        break;
                    case 9:
                        mes = "oct";
                        break;
                    case 10:
                        mes = "nov";
                        break;
                    case 11:
                        mes = "dic";
                        break;
                }
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtSalida.setText(dayOfMonth +" " + mes + " " + year);
                fecha = formatoFechaJSON.format(newDate.getTime());
            }

        }, CalendarSalida.get(Calendar.YEAR), CalendarSalida.get(Calendar.MONTH), CalendarSalida.get(Calendar.DAY_OF_MONTH));


        SharedPreferences prefsUsuario = getActivity().getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
        usuarioActivo = Boolean.parseBoolean(prefsUsuario.getString("activo",null));
        if(usuarioActivo){
            btnIniciarSesion.setVisibility(View.GONE);
            btnRegistrarme.setVisibility(View.GONE);
        }


        btnIniciarSesion.setOnClickListener(this);
        btnRegistrarme.setOnClickListener(this);
        linearSalida.setOnClickListener(this);
        linearLlegada.setOnClickListener(this);
        btnDisponibilidad.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.btnIniciarSesion:
                intent = new Intent(getActivity(),IniciarSesionActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRegistarme:
                intent = new Intent(getActivity(),RegistroActivity.class);
                startActivity(intent);
                break;
            case R.id.linearSalida:
                break;
            case R.id.linearLlegada:
                break;
            case R.id.btnDisponibilidad:
                if("".equals(editTextHotelDestino.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("City Express")
                            .setMessage("El campo Destino es obligatorio")
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
