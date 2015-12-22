package com.zebstudios.cityexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by edwinhernandez on 20/12/15.
 */
public class HotelReservaResultActivity extends Activity {

    ImageView imageBack;
    TextView txtNumeroReservacion;
    TextView txtNombreUsuario;
    ListView listHabitaciones;
    TextView txtNombreHotel;
    TextView txtSalida;
    TextView txtLlegada;
    TextView txtPrecioTotal;
    TextView txtDireccionHotel;
    TextView txtReferenfiaHotel;

    Button btnEnviarMail;
    Button btnCompartir;
    Button btnAbrirUbicacion;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hotel_reserva_result);

        imageBack = (ImageView) findViewById(R.id.back_button);
        txtNumeroReservacion = (TextView) findViewById(R.id.txt_numero_reservacion);
        txtNombreUsuario = (TextView) findViewById(R.id.txt_nombre_usuario);
        listHabitaciones = (ListView) findViewById(R.id.list_reservations);
        txtNombreHotel = (TextView) findViewById(R.id.lblHotelName);
        txtSalida = (TextView) findViewById(R.id.dates_arrival_text);
        txtLlegada = (TextView) findViewById(R.id.dates_departure_text);
        txtPrecioTotal = (TextView) findViewById(R.id.lblTotal);
        txtDireccionHotel = (TextView) findViewById(R.id.lblAddress1);
        txtReferenfiaHotel = (TextView) findViewById(R.id.lblAddress2);

        btnEnviarMail = (Button) findViewById(R.id.btnSendEmail);
        btnCompartir = (Button) findViewById(R.id.btnShare);
        btnAbrirUbicacion = (Button) findViewById(R.id.btnOpenLocation);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelReservaResultActivity.this, PrincipalFragment.class);
                startActivity(intent);
                finish();
            }
        });

        btnEnviarMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Ya tengo mi reserva en...");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,  "Compartir en: " ));

            }
        });

        btnAbrirUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
