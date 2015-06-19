package com.zebstudios.cityexpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Denumeris Interactive on 4/26/2015.
 */
public class PremiosRecuperarFragment extends Fragment
{
	private View _view;
	private ProgressDialogFragment _progress;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) )
			_view = inflater.inflate( R.layout.fragment_premiosrecuperar_tablet, container, false );
		else
			_view = inflater.inflate( R.layout.fragment_premiosrecuperar, container, false );

		Button btnCrear = (Button) _view.findViewById( R.id.btnRecuperar );
		btnCrear.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				recuperarPass();
			}
		} );

		return _view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.getSupportActionBar().setTitle( "Recuperar contraseña" );
	}

	private void recuperarPass()
	{
		EditText txtSocio = (EditText)_view.findViewById( R.id.txtSocio );
		if( txtSocio.getText().toString().trim().length() != 0 && ( txtSocio.getText().toString().trim().length() != 10 || !isAlphaNumeric( txtSocio.getText().toString().trim() ) ) )
		if( !isEmailValid( txtSocio.getText() ) )
		{
			alert( "Por favor, ingresa un número de socio válido." );
			return;
		}

		PremiosExternoClient.entSocioCP param = new PremiosExternoClient.entSocioCP();
		param.getAplicacion().setClaveAplicacion( "903" );
		param.getProgramaSocio().setClaveProg( "1" );

		param.setNoSocioCP( txtSocio.getText().toString() );

		new ServiceCaller( param ).execute();
	}

	private void recuperarComplete( PremiosExternoClient.SendEmailResponse result )
	{
		if( result.getResponse() == 0 )
		{
			if( result.isSent() )
				alertAndWaitForResponse( "Tu contraseña se ha enviado a tu correo." );
			else
				alert( "No se ha podido enviar tu contraseña. Intenta nuevamente más tarde." );
		}
		else
			alert( "No se ha podido enviar tu contraseña. Intenta nuevamente más tarde." );
	}

	private void userInformed()
	{
		//getFragmentManager().beginTransaction().remove( PremiosRecuperarFragment.this ).commit();
		//PremiosLoginFragment fragment = new PremiosLoginFragment();
		//getFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).addToBackStack( "login" ).commit();
		getActivity().onBackPressed();
	}

	private boolean isAlphaNumeric( String s )
	{
		String pattern = "^[a-zA-Z0-9]*$";
		if( s.matches( pattern ) )
		{
			return true;
		}
		return false;
	}

	private boolean isEmailValid( CharSequence email )
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher( email ).matches();
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

	private void alertAndWaitForResponse( String message )
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
				userInformed();
			}
		} );
		alert.show();
	}

	private class ServiceCaller extends AsyncTask<Void, Void, Void>
	{
		private int _task;
		private PremiosExternoClient.entSocioCP _param;
		private PremiosExternoClient.SendEmailResponse _response;

		public ServiceCaller( PremiosExternoClient.entSocioCP param )
		{
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

			_response = client.sendTCPEmail( _param );

			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			_progress.dismiss();

			recuperarComplete( _response );
		}
	}
}
