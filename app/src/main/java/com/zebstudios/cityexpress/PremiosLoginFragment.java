package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

/**
 * Created by rczuart on 4/23/2015.
 */
public class PremiosLoginFragment extends Fragment
{
	private View _view;
	private ProgressDialogFragment _progress;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) )
			_view = inflater.inflate( R.layout.fragment_premioslogin_tablet, container, false );
		else
			_view = inflater.inflate( R.layout.fragment_premioslogin, container, false );

		Button btnLogin = (Button) _view.findViewById( R.id.btnLogin );
		btnLogin.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				doLogin();
			}
		} );

		Button btnRegister = (Button) _view.findViewById( R.id.btnCrear );
		btnRegister.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				register();
			}
		} );

		Button btnRecupear = (Button) _view.findViewById( R.id.btnOlvide );
		btnRecupear.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				olvide();
			}
		} );

		return _view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.getSupportActionBar().setTitle( "City Premios" );
	}

	public void register()
	{
		PremiosRegisterFragment fragment = new PremiosRegisterFragment();
		getFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).addToBackStack( "register" ).commit();
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
		param.getProgramaSocio().setClaveProg( "1" );
		param.setNoSocioCP( txtSocio.getText().toString() );
		param.setPassword( txtPassword.getText().toString() );
		new ServiceCaller( SERVICE_LOGIN, param ).execute();
	}

	public void loginCompleted( PremiosExternoClient.SocioResponse result )
	{
		if( result.getResponse() == 0 )
		{
			if( result.getSocio().getsMensajeAfiliacion().equalsIgnoreCase( "OK" ) )
			{
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

				getActivity().setResult( Activity.RESULT_OK );
				getActivity().finish();
			}
			else
				alert( "Socio y/o contraseña inválidos." );
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
