package com.zebstudios.cityexpress;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class PrincipalFragment extends Fragment implements View.OnClickListener{

    Button btnIniciarSesion;
    Button btnRegistrarme;
    EditText editTextHotelDestino;
    LinearLayout linearSalida;
    LinearLayout linearLlegada;
    EditText editTextPromoCode;
    Button btnDisponibilidad;

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
                break;
        }
    }
}
