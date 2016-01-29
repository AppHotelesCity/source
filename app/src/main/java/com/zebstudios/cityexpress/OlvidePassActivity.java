package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class OlvidePassActivity extends Activity {

    TextView tittleToolbar;
    ImageView imageViewBack;
    EditText edtxtOlvidePass;
    Button btnRecuperarP;
    ProgressDialog progressDialog;
    SoapObject resultString;
    SoapPrimitive test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_pass);
        btnRecuperarP = (Button)findViewById(R.id.btnRecuperarPass);
        edtxtOlvidePass = (EditText)findViewById(R.id.edtxtOlvidePassCorreo);
        tittleToolbar = (TextView)findViewById(R.id.toolbarTitle);
        tittleToolbar.setText(R.string.recuperar_pass);

        imageViewBack = (ImageView)findViewById(R.id.back_button);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btnRecuperarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(edtxtOlvidePass.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(OlvidePassActivity.this);
                    builder.setTitle("Hoteles city")
                            .setMessage("Es necesario ingresar un correo electrónico para recuperar la contraseña")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    ///////////
                    RecuperarPassword task = new RecuperarPassword();
                    task.execute();
                }
            }
        });
    }


    private class RecuperarPassword extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(OlvidePassActivity.this, "Hoteles City",
                    "Cargando...", true);
            System.out.println("OnPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("doInBackground");
            enviarDatosPass();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            System.out.println("OnPostExecute");
            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(OlvidePassActivity.this);
            builder.setTitle("Hoteles city")
                    .setMessage("Se envió la contraseña a tu correo electrónico.")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(OlvidePassActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }



    public void enviarDatosPass() {

        String SOAP_ACTION = "http://tempuri.org/IClienteUnico/PasswordRecovery";
        String METHOD_NAME = "PasswordRecovery";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://wshc.hotelescity.com:9742/wcfMiCityExpress_WCF_Prod/ClienteUnico.svc";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("eMail", "fsandoval@denumeris.com");
            Request.addProperty("UserWS", "UserWS");//manuel2@denumeris.com
            Request.addProperty("PasswordWS", "PasswordWS"); //Manuel8&

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapObject) soapEnvelope.bodyIn;
            System.out.println("->" + resultString.getProperty(0));


            test = (SoapPrimitive) resultString.getProperty(0);

            System.out.println("RESPUESTA ---->" + test);




        } catch (Exception ex) {
            Log.e("Error", "Error: " + ex.getMessage());
        }
    }
}
