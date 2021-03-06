package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import static com.appsee.Appsee.addEvent;
import static com.appsee.Appsee.startScreen;

/**
 * Created by Denumeris Interactive on 4/23/2015.
 */
public class PremiosLoginFragment extends Fragment
{
    private View _view;
    private ProgressDialogFragment _progress;
    private String usuario;

    private String numSocio;
    private String passSocio;
    private LayoutInflater inflater;
    private ViewGroup container;
    private boolean login;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        this.inflater = inflater;
        this.container = container;
        SharedPreferences prefsUsuario = getActivity().getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
        usuario = prefsUsuario.getString("person_ID", null);
        if(usuario!=null) {
            _view = inflater.inflate(R.layout.fragment_premioslogin, container, false);
            numSocio =prefsUsuario.getString("userP2GO", null); ;//"101W848549" ;
            passSocio =  prefsUsuario.getString("passP2GO", null);;//"City2015" ;
            System.out.println("Socio->" + passSocio);
            doLoginAutomatic(numSocio, passSocio);
            addEvent("CityPremiosLogin");
            return _view;

        }
        else{

                _view = inflater.inflate(R.layout.fragment_premioslogin, container, false);
                startScreen("ViewCityPremios-Smartphone");
            Button btnLogin = (Button) _view.findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doLogin();
                    addEvent("CityPremiosLogin");
                }
            });

            Button btnRegister = (Button) _view.findViewById(R.id.btnCrear);
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();
                    addEvent("CityPremiosRegister");
                }
            });

            Button btnRecupear = (Button) _view.findViewById(R.id.btnOlvide);
            btnRecupear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    olvide();
                    addEvent("CityPremiosReset");
                }
            });

            return _view;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.getSupportActionBar().setTitle("City Premios");
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
        activity.setTheme(R.style.PremiosTheme);
    }

    public void register()
    {
        PremiosRegisterFragment fragment = new PremiosRegisterFragment();
        getFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).addToBackStack("register").commit();
    }

    public void olvide()
    {
        PremiosRecuperarFragment fragment = new PremiosRecuperarFragment();
        getFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).addToBackStack( "olvide" ).commit();
    }

    public void doLogin()
    {

        EditText txtSocio = (EditText) _view.findViewById( R.id.txtSocio );
        EditText txtPassword = (EditText) _view.findViewById( R.id.txtPassword );
        if( txtSocio.getText().length() != 10 )
        {
            alert( "Por favor ingresa un número de socio válido." );
            return;
        }

        if( txtPassword.getText().length() == 0 )
        {
            alert( "Por favor ingresa tu contraseña." );
            return;
        }

        PremiosExternoClient.entSocioCP param = new PremiosExternoClient.entSocioCP();
        param.getAplicacion().setClaveAplicacion( "903" );
        param.getProgramaSocio().setClaveProg("1");
        param.setNoSocioCP(txtSocio.getText().toString());
        param.setPassword(txtPassword.getText().toString());
        new ServiceCaller( SERVICE_LOGIN, param ).execute();
    }


    public void doLoginAutomatic(String numSocio, String passSocio)
    {
        PremiosExternoClient.entSocioCP param = new PremiosExternoClient.entSocioCP();
        param.getAplicacion().setClaveAplicacion( "903" );
        param.getProgramaSocio().setClaveProg("1");
        param.setNoSocioCP(numSocio);
        param.setPassword(passSocio);
        new ServiceCaller( SERVICE_LOGIN, param ).execute();
    }


    public void loginCompleted( PremiosExternoClient.SocioResponse result )
    {
        if( result.getResponse() == 0 )
        {
            if( result.getSocio().getsMensajeAfiliacion().equalsIgnoreCase( "OK" ) )
            {
                login = true;
                PremiosUserLoggedDS.PremiosUserLogged user = new PremiosUserLoggedDS.PremiosUserLogged();
                user.setSocio( result.getSocio().getNoSocioCP() );
                user.setLogged( new Date( System.currentTimeMillis() ) );

                PremiosUserLoggedDS db = new PremiosUserLoggedDS( getActivity() );
                db.open();
                PremiosUserLoggedDS.PremiosUserLogged temp = db.getUserLogged();
                if( temp == null )
                    db.insert( user );
                else
                    db.update( user );
                db.close();

                getActivity().setResult(Activity.RESULT_OK);
                Intent dialog = new Intent( getActivity(), PremiosDetailActivity.class );
                getActivity().startActivity(dialog);
                //getActivity().finish();
            }
            else {
                alert("Socio y/o contraseña inválidos.");
                login=false;
            }
        }
        else
            alert( "No se han podido comprobar tus datos. Por favor intenta nuevamente más tarde." );
    }

    private void alert( String message )
    {
        AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
        alert.setTitle( "Atención" );
        alert.setMessage( message );
        alert.setIcon( R.drawable.notification_warning_small );
        alert.setCancelable( false );
        alert.setButton( DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
            }
        } );
        alert.show();
    }

    private static final int SERVICE_LOGIN = 100;

    private class ServiceCaller extends AsyncTask<Void, Void, Void>
    {
        private int _task;
        private Object _param;
        private Object _response;

        public ServiceCaller( int task, Object param )
        {
            _task = task;
            _param = param;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            _progress = ProgressDialogFragment.newInstance( "Espere un momento..." );
            _progress.setCancelable( false );
            _progress.show( getFragmentManager(), "Dialog" );
        }

        @Override
        protected Void doInBackground( Void... arg0 )
        {
            PremiosExternoClient client = new PremiosExternoClient();

            if( _task == SERVICE_LOGIN )
            {
                PremiosExternoClient.entSocioCP param = (PremiosExternoClient.entSocioCP) _param;
                _response = client.loginSocio( param );
            }

            return null;
        }

        @Override
        protected void onPostExecute( Void arg0 )
        {
            super.onPostExecute( arg0 );
            _progress.dismiss();

            if( _task == SERVICE_LOGIN )
            {
                PremiosExternoClient.SocioResponse result = (PremiosExternoClient.SocioResponse) _response;
                loginCompleted( result );
            }
        }
    }
}
