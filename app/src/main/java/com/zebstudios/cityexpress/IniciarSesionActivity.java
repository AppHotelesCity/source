package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.Map;

public class IniciarSesionActivity extends Activity {

    TextView tituloToolbar;
    ImageView imageViewBack;
    EditText edtUsuario;
    EditText edtPassUsuario;
    Button btnEntrarLogin;
    TextView btnOlvidePass;
    Button btnRegistro;
    ProgressDialog progressDialog;
    SoapObject resultString;
    String xmlF2GO;

    boolean menu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
        menu = bundle.getBoolean("menu");
        }

        tituloToolbar = (TextView) findViewById(R.id.toolbarTitle);
        imageViewBack = (ImageView) findViewById(R.id.back_button);
        edtUsuario = (EditText) findViewById(R.id.edtxtUsuarioLogIn);
        edtPassUsuario = (EditText) findViewById(R.id.edtxtPassLogIn);
        btnEntrarLogin = (Button) findViewById(R.id.btnEntrarLogIn);
        btnOlvidePass = (TextView) findViewById(R.id.btnOlvidePassLogIn);
        btnRegistro = (Button) findViewById(R.id.btnRegistrateLogIn);

        tituloToolbar.setText(getResources().getText(R.string.iniciar_sesion_toolbar));
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });


        btnEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtUsuario.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo de e-mail no puede estar vacío.")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if(edtPassUsuario.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this);
                    builder.setTitle("Hoteles City")
                            .setMessage("El campo de contraseña no puede estar vacío.")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else{
                    LoginUsuario task = new LoginUsuario();
                    task.execute();
                }

            }
        });
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),RegistroActivity.class);
                startActivity(intent);
            }
        });

        btnOlvidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), OlvidePassActivity.class);
                startActivity(intent);
            }
        });
    }


    private class LoginUsuario extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(IniciarSesionActivity.this, "Hoteles City",
                    "Cargando...", true);
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
            progressDialog.dismiss();
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
            Request.addProperty("Username", edtUsuario.getText().toString());//manuel2@denumeris.com
            Request.addProperty("Password", edtPassUsuario.getText().toString()); //Manuel8&

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
               editor.putString("activo", test.getPropertyAsString("Active"));
               editor.putString("nombre",test.getPropertySafelyAsString("Name"));
               editor.putString("apellido",test.getPropertySafelyAsString("LastName"));
               editor.putString("usuario",test.getPropertySafelyAsString("Username"));
               editor.putString("pais",test.getPropertySafelyAsString("Country"));
               editor.putString("genero", test.getPropertySafelyAsString("Gender"));
               editor.putString("cityPremios",test.getPropertySafelyAsString("IsValidCityPremios"));
               editor.putString("person_ID",test.getPropertySafelyAsString("Pers_Id_F2G"));
               loginF2GOUsuario(test.getPropertySafelyAsString("Pers_Id_F2G"));
               editor.putString("phone",test.getPropertySafelyAsString("Phone"));
               editor.putString("UserType_ID",test.getPropertySafelyAsString("UserType_ID"));
               editor.putString("User_Changed", test.getPropertySafelyAsString("User_Changed"));
               editor.putString("User_Created", test.getPropertySafelyAsString("User_Created"));
               editor.putString("User_ID", test.getPropertySafelyAsString("User_ID"));
               editor.commit();


               if(menu){
                   Intent intent = new Intent(this,MainActivity.class);
                   startActivity(intent);
                   finish();
               }else{
                   Intent returnIntent = new Intent();
                   returnIntent.putExtra("result", test.getPropertySafelyAsString("Pers_Id_F2G"));
                   setResult(Activity.RESULT_OK, returnIntent);
                   finish();
               }


           }else{
               AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this);
               builder.setTitle("CityExpress")
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


    public void loginF2GOUsuario(String socio) {

        xmlF2GO= "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://tempuri.org/\">\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <ns1:CityPremiosUsrPwd>\n" +
                "            <ns1:UserWS>UserWS</ns1:UserWS>\n" +
                "            <ns1:PasswordWS>PasswordWS</ns1:PasswordWS>\n" +
                "            <ns1:Pers_Id_F2G>{IDF2GO}</ns1:Pers_Id_F2G>\n" +
                "        </ns1:CityPremiosUsrPwd>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
        xmlF2GO = xmlF2GO.replace("{IDF2GO}",socio); //cambiar por cadena socio.

        StringRequest registro = new StringRequest(Request.Method.POST, "http://wshc.hotelescity.com:9742/wcfMiCityExpress_WCF_Prod/ClienteUnico.svc", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObj = null;
                    JSONObject body = null;
                    JSONObject content;
                    JSONObject cityPremiosResult;
                    JSONObject cityPreciosUsuario;

                    jsonObj = XML.toJSONObject(response);

                    Log.d("JSON Probando", jsonObj.toString());

                    body = new JSONObject(jsonObj.getString("s:Envelope"));

                    content = new JSONObject(body.getString("s:Body"));

                    cityPremiosResult = new JSONObject(content.getString("CityPremiosUsrPwdResponse"));

                    cityPreciosUsuario = new JSONObject(cityPremiosResult.getString("CityPremiosUsrPwdResult"));


                    System.out.println("Usuario->"+  cityPreciosUsuario.getString("a:User")  );
                    System.out.println("Password->"+ cityPreciosUsuario.getString("a:Password")  );
                    SharedPreferences prefs =
                            getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES,getBaseContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("userP2GO", cityPreciosUsuario.getString("a:User"));
                    editor.putString("passP2GO", cityPreciosUsuario.getString("a:Password"));

                    editor.commit();





                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
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
            }
        }) {

            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/xml; charset=utf-8");
                params.put("SOAPAction", "http://tempuri.org/IClienteUnico/CityPremiosUsrPwd");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return  xmlF2GO.toString().getBytes();
            }

        };
        registro.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);

    }
}
