package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConsultarSaldosActivity extends Activity {

    private ListView listViewFolios;
    private ArrayList<Folio> folios;
    private FolioSaldoAdapter folioAdapter;
    TextView txtToolbar;
    ProgressDialog _progress;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_saldos);
        listViewFolios = (ListView) findViewById(R.id.listViewFolios);
        txtToolbar = (TextView) findViewById(R.id.toolbarTitle);
        imageBack = (ImageView) findViewById(R.id.back_button);
        txtToolbar.setText("Balance");

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        folios = new ArrayList<Folio>();


        Bundle bundle = getIntent().getExtras();
        obtenerbalance(bundle.getString("cuarto"), bundle.getString("siglas"), bundle.getString("apellido"));
    }

    public void obtenerbalance(String cuarto, String siglas, String apellido) {
        _progress = ProgressDialog.show(ConsultarSaldosActivity.this, "Cargando...",
                "Espere por favor", true);
        String enviarxml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://tempuri.org/\">\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <ns1:Get_Guest_Balance>\n" +
                "            <ns1:room>{room}</ns1:room>\n" +
                "            <ns1:prop_code>{propcode}</ns1:prop_code>\n" +
                "            <ns1:last_name>{lastname}</ns1:last_name>\n" +
                "        </ns1:Get_Guest_Balance>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        enviarxml = enviarxml.replace("{room}",cuarto); //room
        enviarxml = enviarxml.replace("{propcode}",siglas); //siglas
        enviarxml = enviarxml.replace("{lastname}",apellido); //apellido


        Log.e("ReservacionActivity", "XML a enviar --> " + enviarxml);



        final String finalEnviarxml = enviarxml;
        StringRequest registro = new StringRequest(Request.Method.POST, APIAddress.URL_CHECKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("respuestaSALDOS"+response);

                try {

                    JSONObject jsonObj = null;
                    String body = null;
                    JSONArray listadoTarjetasResult = null;

                    jsonObj = XML.toJSONObject(response);


                    body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("Get_Guest_BalanceResponse").getJSONObject("Get_Guest_BalanceResult").getJSONObject("a:folios").getString("a:Folios");
                    listadoTarjetasResult = new JSONArray(body);
                    obtenerFolios(listadoTarjetasResult);
                } catch (Exception e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }finally {
                    _progress.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                try {
                    _progress.dismiss();
                    NetworkResponse response = error.networkResponse;
                    String datos = new String(response.data);
                    System.out.println("sout" + datos);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }) {

            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/xml; charset=utf-8");
                params.put("SOAPAction", "http://tempuri.org/IWHSReservationEngine/Get_Guest_Balance");
                Log.d("hsdhsdfhuidiuhsd", "clave");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                //return cadena.toString().getBytes();
                final String temp = finalEnviarxml.toString();
                return  temp.toString().getBytes();
            }

        };

        registro.setRetryPolicy(new DefaultRetryPolicy(17000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(registro);



    }

    public void obtenerFolios(JSONArray foliosJSON) throws  Exception{

        for (int i = 0; i < foliosJSON.length(); i++) {
            JSONObject balance = new JSONObject(foliosJSON.get(i).toString());

            folios.add(new Folio(balance.getString("a:folio_code"),balance.getString("a:balance")));
        }
        folioAdapter = new FolioSaldoAdapter(folios,this);
        listViewFolios.setAdapter(folioAdapter);
    }
}
