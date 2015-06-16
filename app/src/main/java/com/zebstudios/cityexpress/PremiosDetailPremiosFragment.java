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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rczuart on 4/28/2015.
 */
public class PremiosDetailPremiosFragment extends Fragment
{
	private View _view;
	private ProgressDialogFragment _progress;
	private ArrayList<PremiosExternoClient.Prze> _premios;
	private PremiosExternoClient.entCatePremios _category;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) )
			_view = inflater.inflate( R.layout.fragment_premios_premios_tablet, container, false );
		else
			_view = inflater.inflate( R.layout.fragment_premios_premios, container, false );
		_category = (PremiosExternoClient.entCatePremios)getArguments().getSerializable( "CATEGORY" );

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

		PremiosExternoClient.entProveedor proveedor = new PremiosExternoClient.entProveedor();
		proveedor.setClaveProveedor( "" );
		new ServiceCaller( proveedor, _category ).execute();

		return _view;
	}

	private void stepBack()
	{
		getFragmentManager().beginTransaction().remove( PremiosDetailPremiosFragment.this ).commit();
	}

	private void dataObtained( PremiosExternoClient.PremiosResponse response )
	{
		if( response.getResponse() == 0 )
		{
			_premios = response.getPremios();
			Collections.sort( _premios, new PremiosExternoClient.Prze.Comparator() );
			PremiosListAdapter categoriesListAdapter = new PremiosListAdapter( getActivity(), _premios );
			ListView list = (ListView)_view.findViewById( R.id.list_categorias );
			list.setAdapter( categoriesListAdapter );
			list.setOnItemClickListener( new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
				{
					ItemSelected( i );
				}
			} );
		}
		else
			alert( "No se han podido obtener los premios disponibles. Por favor intenta más tarde." );
	}

	public void ItemSelected( int position )
	{
		Bundle params = new Bundle();
		params.putSerializable( "PREMIO", _premios.get( position ) );

		PremiosDetailDetailFragment fragment = new PremiosDetailDetailFragment();
		fragment.setArguments( params );
		getFragmentManager().beginTransaction().add( R.id.fragment_container, fragment ).commit();
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
		private PremiosExternoClient.entCatePremios _category;
		private PremiosExternoClient.entProveedor _proveedor;
		private PremiosExternoClient.PremiosResponse _response;

		public ServiceCaller( PremiosExternoClient.entProveedor proveedor, PremiosExternoClient.entCatePremios category )
		{
			_proveedor = proveedor;
			_category = category;
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

			_response = client.getPreXProv( _proveedor, _category );

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
