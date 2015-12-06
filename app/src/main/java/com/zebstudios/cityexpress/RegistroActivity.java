package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends Activity {

    TextView tituloToolbar;
    ImageView imageViewBack;
    EditText edtxtNombre;
    EditText edtxtApPaterno;
    EditText edtxtApMaterno;
    EditText edtxtCorreo;
    EditText edtxtPass;
    EditText edtxtTelefono;

    Button btnGenero;
    Button btnTitulo;
    Button btnPais;
    Button btnRegistrar;
    ProgressDialog progressDialog;

    String genero[];
    String paises[];
    ArrayList<Pais> arrayPais;
    SoapObject resultString;
    String paisSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        arrayPais = new ArrayList<>();
        RegistroUsuario task = new RegistroUsuario();
        task.execute();

        tituloToolbar = (TextView) findViewById(R.id.toolbarTitle);
        imageViewBack = (ImageView) findViewById(R.id.back_button);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edtxtNombre = (EditText)findViewById(R.id.edtxtNombre);
        edtxtApPaterno = (EditText)findViewById(R.id.edtxtApPaterno);
        edtxtApMaterno = (EditText)findViewById(R.id.edtxtApMaterno);
        edtxtCorreo = (EditText)findViewById(R.id.edtxtCorreo);
        edtxtPass = (EditText)findViewById(R.id.edtxtPassReg);
        edtxtTelefono = (EditText)findViewById(R.id.edtxtTelefono);

        btnGenero = (Button)findViewById(R.id.btnGenero);
        btnPais = (Button)findViewById(R.id.btnPais);
        btnTitulo = (Button)findViewById(R.id.btnTitulo);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrate);

        genero = getResources().getStringArray(R.array.gender_user);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

       btnGenero.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
               builder.setTitle(getResources().getString(R.string.genero))
                       .setItems(genero, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               btnGenero.setText(genero[which]);
                           }
                       });

               AlertDialog dialog = builder.create();
               dialog.show();
           }
       });

        btnTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                builder.setTitle(getResources().getString(R.string.genero))
                        .setItems(genero, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btnTitulo.setText(genero[which]);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                builder.setTitle(getResources().getString(R.string.pais))
                        .setItems(paises, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                btnPais.setText(paises[(which)]);

                                paisSeleccionado = arrayPais.get(which).getCode();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

       btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });
        
    }

    private class RegistroUsuario extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegistroActivity.this, "Hoteles City",
                    "Cargando...", true);
            System.out.println("OnPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("doInBackground");
            obtenerPaises();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("OnPostExecute");
            progressDialog.dismiss();

        }

    }

    public void obtenerPaises() {
        String SOAP_ACTION = "http://tempuri.org/IClienteUnico/Country";
        String METHOD_NAME = "Country";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://wshc.hotelescity.com:9742/wcfMiCityExpress_WCF_Des/ClienteUnico.svc";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            /*request.addProperty("Country", "");
            Request.addProperty("PasswordService", "PasswordWS");
            Request.addProperty("Username", edtUsuario.getText().toString());//manuel2@denumeris.com
            Request.addProperty("Password", edtPassUsuario.getText().toString()); //Manuel8&
            */
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
           /* resultString = (SoapPrimitive) soapEnvelope.bodyIn;
            System.out.println("->" + resultString.toString());*/

            SoapObject resultado_xml =(SoapObject)soapEnvelope.getResponse();
            int res = resultado_xml.getPropertyCount();

            paises = new String[res-1];
            for (int i = 0; i <res ; i++) {
                if(i==0){
                    continue;
                }
                System.out.println("->"+resultado_xml.getProperty(i));
                SoapObject datosPais = (SoapObject)resultado_xml.getProperty(i) ;
                arrayPais.add(new Pais(datosPais.getPrimitiveProperty("Code").toString(),datosPais.getPrimitiveProperty("Description").toString()));
            }

            for (int i = 0; i < arrayPais.size(); i++) {
                paises[i] = arrayPais.get(i).getDescripcion();
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




        } catch (Exception ex) {
            Log.e("Error", "Error: " + ex.getMessage());
        }
    }

    public void validarCampos(){
        if("".equals(edtxtNombre.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("El campo Nombre es obligatorio")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else if("".equals(edtxtApPaterno.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("El campo Apellido Paterno es obligatorio")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else if("".equals(edtxtApMaterno.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("El campo Apellido Materno es obligatorio")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else if("".equals(edtxtCorreo.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("El campo Correo es obligatorio")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else if("".equals(edtxtPass.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("El campo Contraseña es obligatorio")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else if("".equals(edtxtTelefono.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("El campo Teléfono es obligatorio")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{

            edtxtNombre.getText();
            edtxtApPaterno.getText();
            edtxtApMaterno.getText();
            edtxtCorreo.getText();
            edtxtPass.getText();
            edtxtTelefono.getText();
            btnTitulo.getText();
            btnGenero.getText();

            Toast.makeText(RegistroActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
            System.out.println("----> " + edtxtNombre.getText().toString());
            System.out.println("----> " + edtxtApPaterno.getText().toString());
            System.out.println("----> " + edtxtApMaterno.getText().toString());
            System.out.println("----> " + edtxtCorreo.getText().toString());
            System.out.println("----> " + edtxtPass.getText().toString());
            System.out.println("----> " + edtxtTelefono.getText().toString());
            System.out.println("----> " + btnTitulo.getText().toString());
            System.out.println("----> " + btnGenero.getText().toString());
            System.out.println("----> " + paisSeleccionado);
        }
    }

    public void enviarDatos(){

    }

}
