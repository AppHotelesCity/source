package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends Activity {

    TextView tituloToolbar;
    ImageView imageViewBack;
    ImageView imageConditions;
    EditText edtxtNombre;
    EditText edtxtApPaterno;
    EditText edtxtApMaterno;
    EditText edtxtCorreo;
    EditText edtxtPass;
    EditText edtxtTelefono;
    ProgressDialog progress;

    Button btnGenero;
    Button btnTitulo;
    Button btnPais;
    Button btnRegistrar;
    ProgressDialog progressDialog;

    String gender;
    TextView txtTermis;
    String respuesta;

    String genero[];
    String titulo[];
    String paises[];
    ArrayList<Pais> arrayPais;
    SoapObject resultString;
    String paisSeleccionado;
    boolean banderaCondiciones = false;
    String cadena;


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
        imageConditions = (ImageView)findViewById(R.id.img_condiciones);

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
        titulo = getResources().getStringArray(R.array.titulo_user);
        txtTermis = (TextView) findViewById(R.id.txtTerminos);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tituloToolbar.setText("Registro");
       btnGenero.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
               builder.setTitle(getResources().getString(R.string.genero))
                       .setItems(genero, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               btnGenero.setText(genero[which]);
                               if ("Masculino".equals(btnGenero.getText().toString())) {
                                   gender = "H";
                               } else if ("Femenino".equals(btnGenero.getText().toString())) {
                                   gender = "F";
                               }
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
                builder.setTitle(getResources().getString(R.string.titulo))
                        .setItems(titulo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btnTitulo.setText(titulo[which]);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnPais.setText("México");
        paisSeleccionado = "MX";
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
               if( validarCampos()){
                   enviarRegistro();
               }

            }
        });



        imageConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageConditions.setImageResource(R.drawable.checked);
                banderaCondiciones = true;
            }
        });

        txtTermis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "https://www.cityexpress.com/es/terminos-y-condiciones-de-uso/";
                Intent intent = null;
                intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });

    }


    public void enviarRegistro(){
        progress = ProgressDialog.show(RegistroActivity.this, "Cargando...",
                "Espere por favor", true);
        System.out.println("CADENARESGIRTRO"+cadena);
        StringRequest registro = new StringRequest(Request.Method.POST,APIAddress.URL_INICIO_SESION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("RESPUESTA OK -------> "+response);
                JSONObject jsonObj = null;
                try {
                    jsonObj = XML.toJSONObject(response);
                    Log.d("JSON", jsonObj.toString());
                    JSONObject a = new JSONObject(jsonObj.getString("s:Envelope"));
                    JSONObject b = new JSONObject(a.getString("s:Body"));
                    JSONObject c = new JSONObject(b.getString("InsertUserResponse"));
                   // JSONObject respuesta = new JSONObject(c.getString("InsertUserResult"));
                    respuesta =c.getString("InsertUserResult").replace("|","") ;

                    System.out.println("RESPUESTASERVICIO"+ respuesta);
                    alert(respuesta);
                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }finally {
                    progress.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                error.printStackTrace();
                NetworkResponse response = error.networkResponse;
                String datos = new String(response.data);
                System.out.println("sout ERROR ------> " + datos);
            }
        }){

            public String getBodyContentType(){
                return "text/xml; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String>  params = new HashMap<String,String>();
                //params.put("Content-Type", "text/xml; charset=utf-8");
                params.put("SOAPAction", "http://tempuri.org/IClienteUnico/InsertUser");
                Log.d("hsdhsdfhuidiuhsd","clave");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return cadena.toString().getBytes();
            }

        };
        System.out.println("registro->"+registro.toString());
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);
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

    public boolean validarCampos(){
        if("".equals(edtxtNombre.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("El campo Nombre no puede estar vacío")
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
                    .setMessage("El campo Apellido Paterno no puede estar vacío")
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
                    .setMessage("El campo Apellido Materno no puede estar vacío")
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
                    .setMessage("El campo Correo no puede estar vacío")
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
                    .setMessage("El campo Contraseña no puede estar vacío")
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
                    .setMessage("El campo Teléfono no puede estar vacío")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else if(!banderaCondiciones) {

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("Debes aceptar los términos y condiciones")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }else if("Género".equals(btnGenero.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("Selecciona tu Género")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }else if("Título".equals(btnTitulo.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("Selecciona tu título")
                    .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }else if("País".equals(btnPais.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("City Express")
                    .setMessage("Selecciona tu país")
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

                System.out.println("----> " + edtxtNombre.getText().toString());
                System.out.println("----> " + edtxtApPaterno.getText().toString());
                System.out.println("----> " + edtxtApMaterno.getText().toString());
                System.out.println("----> " + edtxtCorreo.getText().toString());
                System.out.println("----> " + edtxtPass.getText().toString());
                System.out.println("----> " + edtxtTelefono.getText().toString());
                System.out.println("----> " + btnTitulo.getText().toString());
                System.out.println("----> " + btnGenero.getText().toString());
                System.out.println("----> " + paisSeleccionado);
            cadena = "<soapenv:Envelope\n" +
                    "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                    "    xmlns:tem=\"http://tempuri.org/\"\n" +
                    "    xmlns:cit=\"http://schemas.datacontract.org/2004/07/CityHub\">\n" +
                    "    <soapenv:Header/>\n" +
                    "    <soapenv:Body>\n" +
                    "        <InsertUser\n" +
                    "            xmlns=\"http://tempuri.org/\">\n" +
                    "            <UserInfo\n" +
                    "                xmlns:d4p1=\"http://schemas.datacontract.org/2004/07/CityExpress.ClienteUnico.Entidades\"\n" +
                    "                xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "                <d4p1:Active>true</d4p1:Active>\n" +
                    "                <d4p1:Changed>2015-06-03T14:47:00</d4p1:Changed>\n" +
                    "                <d4p1:CityAccess_Pwd i:nil=\"true\" />\n" +
                    "                <d4p1:CityAccess_Usr i:nil=\"true\" />\n" +
                    "                <d4p1:CityPremios_Pwd i:nil=\"true\" />\n" +
                    "                <d4p1:CityPremios_Usr i:nil=\"true\" />\n" +
                    "                <d4p1:Country>MX</d4p1:Country>\n" +
                    "                <d4p1:Created>2015-06-03T14:47:00</d4p1:Created>\n" +
                    "                <d4p1:Gender>"+ gender +"</d4p1:Gender>\n" +
                    "                <d4p1:IsValidCityPremios>true</d4p1:IsValidCityPremios>\n" +
                    "                <d4p1:LastName>"+ edtxtApPaterno.getText().toString() +"</d4p1:LastName>\n" +
                    "                <d4p1:Name>" + edtxtNombre.getText().toString() +"</d4p1:Name>\n" +
                    "                <d4p1:Password>" + edtxtPass.getText().toString() +"</d4p1:Password>\n" +
                    "                <d4p1:Pers_Id_F2G>0</d4p1:Pers_Id_F2G>\n" +
                    "                <d4p1:Phone>" + edtxtTelefono.getText().toString() +"</d4p1:Phone>\n" +
                    "                <d4p1:SecondLastName>"+ edtxtApMaterno.getText().toString() +"</d4p1:SecondLastName>\n" +
                    "                <d4p1:System_ID i:nil=\"true\" />\n" +
                    "                <d4p1:UserType_ID>1</d4p1:UserType_ID>\n" +
                    "                <d4p1:User_Changed>0</d4p1:User_Changed>\n" +
                    "                <d4p1:User_Created>0</d4p1:User_Created>\n" +
                    "                <d4p1:User_ID>0</d4p1:User_ID>\n" +
                    "                <d4p1:Username>"+ edtxtCorreo.getText().toString() +"</d4p1:Username>\n" +
                    "            </UserInfo>\n" +
                    "            <AfilCityPrem>0</AfilCityPrem>\n" +
                    "            <Title>Mr</Title>\n" +
                    "           <Enabled>1</Enabled>\n"+
                    "        </InsertUser >\n" +
                    "    </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            return true;
            }
        return false;
        }

    private void alert( String message )
    {
        AlertDialog alert = new AlertDialog.Builder(this ).create();
        alert.setTitle( "Atención" );
        alert.setMessage(message);
        alert.setIcon(R.drawable.notification_warning_small);
        alert.setCancelable(false);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (respuesta.contains("Error")) {

                } else {
                    finish();
                }
            }
        });
        alert.show();
    }

}
