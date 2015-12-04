package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class IniciarSesionActivity extends Activity {

    TextView tituloToolbar;
    ImageView imageViewBack;
    EditText edtUsuario;
    EditText edtPassUsuario;
    Button btnEntrarLogin;
    Button btnOlvidePass;
    Button btnRegistro;

    SoapObject resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        tituloToolbar = (TextView) findViewById(R.id.toolbarTitle);
        imageViewBack = (ImageView) findViewById(R.id.back_button);
        edtUsuario = (EditText) findViewById(R.id.edtxtUsuarioLogIn);
        edtPassUsuario = (EditText) findViewById(R.id.edtxtPassLogIn);
        btnEntrarLogin = (Button) findViewById(R.id.btnEntrarLogIn);
        btnOlvidePass = (Button) findViewById(R.id.btnOlvidePassLogIn);
        btnRegistro = (Button) findViewById(R.id.btnRegistrateLogIn);

        tituloToolbar.setText(getResources().getText(R.string.iniciar_sesion_toolbar));
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btnEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Hacer validaciones si bien.
                LoginUsuario task = new LoginUsuario();
                task.execute();
            }
        });
    }

    private class LoginUsuario extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            System.out.println("OnPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("doInBackground");
            limpiarDatosUsuario();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("OnPostExecute");
        }

    }

    public void limpiarDatosUsuario() {
        String SOAP_ACTION = "http://tempuri.org/IClienteUnico/ValidateUser";
        String METHOD_NAME = "ValidateUser";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://wshc.hotelescity.com:9742/wcfMiCityExpress_WCF_Des/ClienteUnico.svc";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("UserService", "USERWS");
            Request.addProperty("PasswordService", "PasswordWS");
            Request.addProperty("Username", "manuel2@denumeris.com");
            Request.addProperty("Password", "Manuel8&");

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapObject) soapEnvelope.bodyIn;
            System.out.println("->" + resultString.getProperty(0));


            SoapObject test = (SoapObject) resultString.getProperty(0);

           if(Boolean.parseBoolean(test.getPropertySafelyAsString("Active"))){
               SharedPreferences prefs =
                       getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES,getBaseContext().MODE_PRIVATE);
               SharedPreferences.Editor editor = prefs.edit();
               editor.putString("activo",test.getPropertyAsString("Active"));
               editor.putString("nombre",test.getPropertySafelyAsString("Name"));
               editor.putString("apellido",test.getPropertySafelyAsString("LastName"));
               editor.putString("usuario",test.getPropertySafelyAsString("Username"));
               editor.putString("pais",test.getPropertySafelyAsString("Country"));
               editor.putString("genero",test.getPropertySafelyAsString("Gender"));
               editor.putString("cityPremios",test.getPropertySafelyAsString("IsValidCityPremios"));
               editor.putString("person_ID",test.getPropertySafelyAsString("Pers_Id_F2G"));
               editor.putString("phone",test.getPropertySafelyAsString("Phone"));
               editor.putString("UserType_ID",test.getPropertySafelyAsString("UserType_ID"));
               editor.putString("User_Changed",test.getPropertySafelyAsString("User_Changed"));
               editor.putString("User_Created",test.getPropertySafelyAsString("User_Created"));
               editor.putString("User_ID", test.getPropertySafelyAsString("User_ID"));
               editor.commit();

               Intent intent = new Intent(this,MainActivity.class);
               startActivity(intent);

           }else{
               AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this);
               builder.setTitle("Wisum")
                       .setMessage("El usuario o contraseña son incorrectos, favor de verificarlos.")
                       .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       });

               AlertDialog dialog = builder.create();
               dialog.show();
           }

           /* System.out.println("+++->" + test.getPropertySafelyAsString("Active"));
            System.out.println("+++->" + test.getPropertySafelyAsString("Country"));
            System.out.println("+++->"+test.getPropertySafelyAsString("Gender"));
            System.out.println("+++->"+test.getPropertySafelyAsString("IsValidCityPremios"));
            System.out.println("+++->" + test.getPropertySafelyAsString("Name"));
            System.out.println("+++->"+test.getPropertySafelyAsString("LastName"));
            System.out.println("+++->"+test.getPropertySafelyAsString("Pers_Id_F2G"));
            System.out.println("+++->"+test.getPropertySafelyAsString("Phone"));
            System.out.println("+++->"+test.getPropertySafelyAsString("UserType_ID"));
            System.out.println("+++->"+test.getPropertySafelyAsString("User_Changed"));
            System.out.println("+++->"+test.getPropertySafelyAsString("User_Created"));
            System.out.println("+++->"+test.getPropertySafelyAsString("User_ID"));
            System.out.println("+++->"+test.getPropertySafelyAsString("Username"));*/
            //Get the attributes in the array
          /*  String tem = (String) test.getAttribute("Active");
            System.out.println("Active->"+tem);
           // tem = tem + " " + (String) test.getProperty("nname");*/


            System.out.println("" + resultString);



        } catch (Exception ex) {
            Log.e("Error", "Error: " + ex.getMessage());
        }
    }
}
