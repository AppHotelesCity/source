package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import static com.appsee.Appsee.addEvent;
import static com.appsee.Appsee.startScreen;

/**
 * Created by Denumeris Interactive on 4/27/2015.
 */
public class PremiosDetailPuntosFragment extends Fragment
{
	private View _view;
	private ProgressDialogFragment _progress;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) ){
			_view = inflater.inflate( R.layout.fragment_premios_puntos_tablet, container, false );
			startScreen("ViewCityPremios-MisPuntos-Tablet");
		}
		else{
			_view = inflater.inflate( R.layout.fragment_premios_puntos, container, false );
			startScreen("ViewCityPremios-MisPuntos-Smartphone");
		}

		Button btnLogout = (Button)_view.findViewById( R.id.btnLogout );
		btnLogout.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				initiateLogout();
				addEvent("CityPremiosLogout");
			}
		} );

		PremiosUserLoggedDS db = new PremiosUserLoggedDS( getActivity() );
		db.open();
		PremiosUserLoggedDS.PremiosUserLogged user = db.getUserLogged();
		db.close();

		Calendar c = Calendar.getInstance();
		Date fin = c.getTime();
		c.add( Calendar.DAY_OF_YEAR, -30 );
		Date inicio = c.getTime();

		new ServiceCaller( user.getSocio(), inicio, fin ).execute();

		return _view;
	}

	private void initiateLogout()
	{
		AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
		alert.setTitle( "Atención" );
		alert.setMessage( "Estás seguro que deseas cerrar tu sesión? Podrás acceder nuevamente más tarde si así lo deseas." );
		alert.setIcon( R.drawable.notification_warning_small );
		alert.setCancelable( false );
		alert.setButton( DialogInterface.BUTTON_POSITIVE, "Cerrar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				logout();
			}
		} );
		alert.setButton( DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
			}
		} );
		alert.show();
	}

	private void logout()
	{
		android.util.Log.d( "TEST", "LOGIN OUT" );

		PremiosUserLoggedDS db = new PremiosUserLoggedDS( getActivity() );
		db.open();
		db.clearUser();
		db.close();

		getActivity().setResult( Activity.RESULT_OK );
		getActivity().finish();
	}

	private void dataObtained( PremiosExternoClient.EdoCtaResponse response )
	{
		if( response.getResponse() == 0 )
		{
			int saldo = 0;
			for( int i = 0; i < response.getCRB().size(); i++ )
			{
				saldo += response.getCRB().get( i ).getPoints();
			}
			TextView lblPuntos = (TextView) _view.findViewById( R.id.lblPuntos );
			lblPuntos.setText( String.format( "%,d", saldo ) );

			int porVencer = 0;
			for( int i = 0; i < response.getNMT().size(); i++ )
			{
				porVencer += response.getNMT().get( i ).getPoints();
			}
			TextView lblVencer = (TextView) _view.findViewById( R.id.lblVencer );
			lblVencer.setText( String.format( "%,d", porVencer ) );

			int vencidos = 0;
			for( int i = 0; i < response.getMAT().size(); i++ )
			{
				vencidos += response.getMAT().get( i ).getPoints();
			}
			TextView lblVencidos = (TextView) _view.findViewById( R.id.lblVencidos );
			lblVencidos.setText( String.format( "%,d", vencidos ) );

			int canje = 0;
			for( int i = 0; i < response.getSWP().size(); i++ )
			{
				canje += Math.abs( response.getSWP().get( i ).getPoints() );
			}
			TextView lblCanje = (TextView) _view.findViewById( R.id.lblCanje );
			lblCanje.setText( String.format( "%,d", canje ) );

			Activity parentActivity = getActivity();
			if( parentActivity instanceof PremiosDetailActivity )
			{
				PremiosDetailActivity parent = (PremiosDetailActivity)parentActivity;
				parent.setResumen( response.getRESUMEN().get( 0 ) );
			}
		}
		else
		{
			alert( "No se ha podido obtener los datos de tu cuenta. Por favor intenta nuevamente más tarde." );
		}
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

	private class ServiceCaller extends AsyncTask<Void, Void, Void>
	{
		private int _task;
		private String _socio;
		private Date _inicio;
		private Date _fin;
		private PremiosExternoClient.EdoCtaResponse _response;

		public ServiceCaller( String socio, Date inicio, Date fin )
		{
			_socio = socio;
			_inicio = inicio;
			_fin = fin;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			if( _progress != null )
			{
				_progress.dismiss();
				_progress = null;
			}
			_progress = ProgressDialogFragment.newInstance( "Espere un momento..." );
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		@Override
		protected Void doInBackground( Void... arg0 )
		{
			PremiosExternoClient client = new PremiosExternoClient();

			_response = client.getEdoCta( _socio, _inicio, _fin );

			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			_progress.dismiss();

			dataObtained( _response );
		}
	}
}
