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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rczuart on 4/27/2015.
 */
public class PremiosDetailCategoriasFragment extends Fragment
{
	private View _view;
	private ProgressDialogFragment _progress;
	private ArrayList<PremiosExternoClient.entCatePremios> _categories;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) )
			_view = inflater.inflate( R.layout.fragment_premios_categorias_tablet, container, false );
		else
			_view = inflater.inflate( R.layout.fragment_premios_categorias, container, false );

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

		PremiosExternoClient.entProveedor proveedor = new PremiosExternoClient.entProveedor();
		proveedor.setClaveProveedor( "" );
		new ServiceCaller( proveedor ).execute();

		return _view;
	}

	private void dataObtained( PremiosExternoClient.CategoriesResponse response )
	{
		if( response.getResponse() == 0 )
		{
			_categories = response.getCategories();
			Collections.sort( _categories, new PremiosExternoClient.entCatePremios.Comparator() );
			CategoriesListAdapter categoriesListAdapter = new CategoriesListAdapter( getActivity(), _categories );
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
		Bundle bundle = new Bundle();
		bundle.putSerializable( "CATEGORY", _categories.get( position ) );

		PremiosDetailPremiosFragment fragment = new PremiosDetailPremiosFragment();
		fragment.setArguments( bundle );
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
		private PremiosExternoClient.entProveedor _proveedor;
		private PremiosExternoClient.CategoriesResponse _response;

		public ServiceCaller( PremiosExternoClient.entProveedor proveedor )
		{
			_proveedor = proveedor;
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

			_response = client.getCatePreXProv( _proveedor );

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
