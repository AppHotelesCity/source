package com.zebstudios.cityexpress;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class PromotionsFragment extends Fragment
{
	private View _view;
	private ProgressDialog _progressDialog;
	private ArrayList<Promotion> _promotions;

	public PromotionsFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		_view = inflater.inflate( R.layout.fragment_promotions, container, false );

		if( CompatibilityUtil.isTablet( getActivity() ) )
		{
			GridView gridview = (GridView) _view.findViewById( R.id.promosGridView );
			gridview.setNumColumns( 4 );
		}

		new GetPromos().execute();

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppScreenTrack( "PROMOTIONS ANDROID" );

		return _view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity)getActivity();
		activity.getSupportActionBar().setTitle( "Promociones" );
	}

	private void promosObtained()
	{
		GridView gridview = ( GridView )_view.findViewById( R.id.promosGridView );
		PromotionsListAdapter promotionsListAdapter = new PromotionsListAdapter( getActivity(), _promotions );
		gridview.setAdapter( promotionsListAdapter );
		gridview.setOnItemClickListener( new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick( AdapterView<?> adapterView, View view, int index, long l )
			{
				promoSelected( index );
			}
		} );
	}

	private void promoSelected( int index )
	{
		PromotionDetailFragment fragment = new PromotionDetailFragment();
		fragment.setImageURL( _promotions.get( index ).getImageURL() );
		getFragmentManager().beginTransaction().add( R.id.fragment_container, fragment ).addToBackStack( "PromotionDetail" ).commit();
	}

	private class GetPromos extends AsyncTask<Void, Void, Integer>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			_progressDialog = new ProgressDialog( getActivity() );
			_progressDialog.setMessage( "Espere un momento..." );
			_progressDialog.setCancelable( false );
			_progressDialog.show();
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			_promotions = new ArrayList<Promotion>();
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall( APIAddress.HOTELS_API_MOBILE + "/GetPromos", ServiceHandler.GET );

			if( jsonStr != null )
			{
				try
				{
					JSONArray results = new JSONArray( jsonStr );
					for( int i = 0; i < results.length(); i++ )
					{
						JSONObject p = results.getJSONObject( i );
						Promotion promotion = new Promotion( p );
						if( !promotion.isParsedOk() )
							return -3;
						_promotions.add( promotion );
					}
					return 0;
				}
				catch( Exception e )
				{
					android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
					return -2;
				}
			}
			else
				android.util.Log.e( "ServiceHandler", "Couldn't get any data" );

			return -1;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );

			if( _progressDialog.isShowing() )
				_progressDialog.dismiss();

			if( result != 0 )
			{
				AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
				alert.setTitle( "Atención" );
				alert.setMessage( "No se ha podido obtener las promociones en este momento. Por favor intente nuevamente más tarde." );
				alert.setIcon( R.drawable.notification_warning_small );
				alert.setCancelable( false );
				alert.setButton( "OK", new DialogInterface.OnClickListener()
				{
					public void onClick( DialogInterface dialog, int which )
					{
					}
				} );
				alert.show();
			}
			else
			{
				promosObtained();
			}
		}
	}
}
