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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rczuart on 4/28/2015.
 */
public class PremiosDetailDetailFragment extends Fragment
{
	private View _view;
	private ProgressDialogFragment _progress;
	private PremiosExternoClient.Prze _premio;
	private ImageCache _imageCache;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) )
			_view = inflater.inflate( R.layout.fragment_premios_detail_tablet, container, false );
		else
			_view = inflater.inflate( R.layout.fragment_premios_detail, container, false );
		_premio = (PremiosExternoClient.Prze)getArguments().getSerializable( "PREMIO" );

		_imageCache = new ImageCache( getActivity() );

		TextView lblDisponibles = (TextView)_view.findViewById( R.id.lblDisponibles );
		Activity parentActivity = getActivity();
		if( parentActivity instanceof PremiosDetailActivity )
		{
			PremiosDetailActivity parent = (PremiosDetailActivity)parentActivity;
			PremiosExternoClient.EdoCtaResponse.ResRecord resumen = parent.getResumen();
			if( resumen != null )
				lblDisponibles.setText( String.format( "%,d", resumen.getSaldoAlCorte() ) + " puntos." );
			else
				lblDisponibles.setText( "" );
		}
		else
			lblDisponibles.setText( "" );

		TextView lblBack = (TextView) _view.findViewById( R.id.lblBack );
		lblBack.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				stepBack();
			}
		} );

		ImageView imgBack = (ImageView)_view.findViewById( R.id.imgBack );
		imgBack.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				stepBack();
			}
		} );

		//android.util.Log.d( "TEST", "IMG0: " + _premio.getPrze_URL00() );
		//android.util.Log.d( "TEST", "IMG1: " + _premio.getPrze_URL01() );

		ImageView imgPremio = (ImageView)_view.findViewById( R.id.imgPremio );
		imgPremio.setImageDrawable( null );
		if( _premio.getPrze_URL00().startsWith( "http" ) || _premio.getPrze_URL00().startsWith( "HTTP" ) )
			new ImageLoader( imgPremio, _imageCache ).execute( _premio.getPrze_URL00() );
		else if( _premio.getPrze_URL01().startsWith( "http" ) || _premio.getPrze_URL01().startsWith( "HTTP" ) )
			new ImageLoader( imgPremio, _imageCache ).execute( _premio.getPrze_URL01() );


		TextView lblPuntos = (TextView)_view.findViewById( R.id.lblPuntos );
		lblPuntos.setText( String.format( "%,d", _premio.getPrze_Pts().intValue() ) );

		TextView lblName = (TextView)_view.findViewById( R.id.lblName );
		lblName.setText( _premio.getPrze_Dscr() );

		TextView lblDescription = (TextView)_view.findViewById( R.id.lblDescription );
		lblDescription.setText( _premio.getPrze_Shrt_Name() );

		ArrayList<String> cantidades = new ArrayList<String>();
		for( Double i=_premio.getPrze_Low_Qtty(); i<= _premio.getPrze_Hgh_Qtty(); i++ )
			cantidades.add( "" + i.intValue() );
		Spinner spinTotal = (Spinner)_view.findViewById( R.id.spinTotal );
		SpinnerAdapter adapterCantidades = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, cantidades );
		spinTotal.setAdapter( adapterCantidades );

		Button btnCanjear = (Button)_view.findViewById( R.id.btnCanjear );
		btnCanjear.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				validateCanje();
			}
		} );

		return _view;
	}

	private void stepBack()
	{
		getFragmentManager().beginTransaction().remove( PremiosDetailDetailFragment.this ).commit();
	}

	private void validateCanje()
	{
		Spinner spinTotal = (Spinner)_view.findViewById( R.id.spinTotal );
		int total = Integer.parseInt( (String)spinTotal.getSelectedItem() );

		int cost = total * _premio.getPrze_Amt().intValue();

		Activity parentActivity = getActivity();
		if( parentActivity instanceof PremiosDetailActivity )
		{
			PremiosDetailActivity parent = (PremiosDetailActivity)parentActivity;
			PremiosExternoClient.EdoCtaResponse.ResRecord resumen = parent.getResumen();
			if( resumen.getSaldoAlCorte() < cost )
			{
				alert( "No tienes los puntos suficientes disponibles para hacer el canje." );
				return;
			}
		}

		confirmCanje();
	}

	private void doCanje()
	{
		PremiosUserLoggedDS db = new PremiosUserLoggedDS( getActivity() );
		db.open();
		PremiosUserLoggedDS.PremiosUserLogged user = db.getUserLogged();
		db.close();

		Date now = new Date( System.currentTimeMillis() );
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
		Spinner spinTotal = (Spinner)_view.findViewById( R.id.spinTotal );
		int total = Integer.parseInt( (String)spinTotal.getSelectedItem() );

		PremiosExternoClient.entRedencion redencion = new PremiosExternoClient.entRedencion();
		redencion.setentSwap_Fcbm( 0 );
		redencion.setentSwap_Id( (long) 0 );
		redencion.setentSwap_Swst( 1 );
		redencion.setentSwap_CtTy( "MEX" );
		redencion.setentSwap_Src( "APP" );
		redencion.setentSwap_Add_Usr( "APP" );
		redencion.setentSwap_Cmnt( "App Android" );

		redencion.setentSwap_BsAs( _premio.getPrze_BsAs() );
		redencion.setentSwap_FrCs( user.getSocio() );
		redencion.setentSwap_Last_Dte( sdf.format( now ) );
		redencion.setentSwap_Prze( _premio.getPrze_Id() );
		redencion.setentSwap_Qtty( total );
		redencion.setentSwap_Amt( _premio.getPrze_Amt().intValue() * total );
		redencion.setentSwap_Pts( _premio.getPrze_Pts().intValue() * total );
		redencion.setentSwap_Val( _premio.getPrze_Value().intValue() * total );

		new ServiceCaller( redencion ).execute();
	}

	private void premioRedimed( PremiosExternoClient.CanjePremioResponse response )
	{
		if( response.getResponse() == 0 )
		{
			if( response.getRedencion().getsMensajeRedencion().equalsIgnoreCase( "OK" ) )
			{
				displayInitialTab();
			}
			else
			{
				android.util.Log.d( "TEST", "ERROR: " +  response.getRedencion().getsMensajeRedencion() );
				alert( "No se ha podido realizar el canje de este premio en este momento. Por favor intenta nuevamente más tarde." );
			}
		}
		else
			alert( "No se ha podido realizar el canje de este premio. Por favor intenta nuevamente más tarde." );
	}

	private void displayInitialTab()
	{
		Activity parentActivity = getActivity();
		if( parentActivity instanceof PremiosDetailActivity )
		{
			PremiosDetailActivity parent = (PremiosDetailActivity)parentActivity;
			parent.showInitialTab();
		}
	}

	private class ServiceCaller extends AsyncTask<Void, Void, Void>
	{
		private PremiosExternoClient.entRedencion _redencion;
		private PremiosExternoClient.CanjePremioResponse _response;

		public ServiceCaller( PremiosExternoClient.entRedencion redencion )
		{
			_redencion = redencion;
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

			_response = client.solCanjePremio( _redencion );

			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			_progress.dismiss();

			premioRedimed( _response );
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

	private void confirmCanje()
	{
		AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
		alert.setTitle( "Atención" );
		alert.setMessage( "Estás seguro de que deseas canjear este premio?" );
		alert.setIcon( R.drawable.notification_warning_small );
		alert.setCancelable( false );
		alert.setButton( DialogInterface.BUTTON_POSITIVE, "Canjear", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				doCanje();
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
}
