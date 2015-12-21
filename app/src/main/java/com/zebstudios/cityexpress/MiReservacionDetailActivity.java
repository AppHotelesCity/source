package com.zebstudios.cityexpress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
         txtLeyendaCambios = (TextView) findViewById(R.id.txt_leyenda_cambios);
         txtDireccionHotel = (TextView) findViewById(R.id.txt_direccion_hotel);
         txtReferenciaHotel = (TextView) findViewById(R.id.txt_referencia_hotel);

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

        btnCheckIn.setEnabled(false);
        btnCheckOut.setEnabled(false);
        btnConsultarSaldos.setEnabled(false);
        btnFacturacion.setEnabled(false);

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
