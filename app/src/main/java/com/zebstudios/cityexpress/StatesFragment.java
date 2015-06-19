package com.zebstudios.cityexpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class StatesFragment extends DialogFragment
{
	private View _view;
	private ArrayList<State> _states;
	private ProgressDialogFragment _progress;

	public StatesFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		_view = inflater.inflate( R.layout.fragment_states, container, false );

		new GetStates().execute();

		return _view;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if( getDialog() == null )
		{
			return;
		}

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		getDialog().getWindow().setLayout( width / 2, height / 2 );
		getDialog().setTitle( "Ciudades" );
	}

	@Override
	public void onResume()
	{
		super.onResume();

		if( getDialog() == null )
		{
			ActionBarActivity activity = (ActionBarActivity) getActivity();
			activity.getSupportActionBar().setTitle( "Ciudades" );
		}
	}

	private void EstadosObtained()
	{
		android.util.Log.d( "TEST", "RESULTS: " + _states.size() );

		Collections.sort( _states, new State.StateComparator() );

		State c = new State( -1, "Todas las ciudades" );
		_states.add( 0, c );

		StatesListAdapter statesListAdapter = new StatesListAdapter( getActivity(), _states );
		ListView list = (ListView)_view.findViewById( R.id.result_list );
		list.setAdapter( statesListAdapter );
		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
			{
				ItemSelected( i );
			}
		} );
	}

	public void ItemSelected( int position )
	{
		State state = _states.get( position );
		Fragment fragmentObject = getTargetFragment();
		if( fragmentObject instanceof MainFragment )
		{
			MainFragment parent = (MainFragment) getTargetFragment();
			parent.setSelectedState( state );
			getFragmentManager().popBackStackImmediate();
		}
		else if( fragmentObject instanceof MainTabletFragment )
		{
			MainTabletFragment parent = (MainTabletFragment) getTargetFragment();
			parent.setSelectedState( state );
			this.dismiss();
		}
	}

	private class GetStates extends AsyncTask<Void, Void, Integer>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			_progress = ProgressDialogFragment.newInstance();
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		//ESTADOS

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			_states = new ArrayList<State>();

			ServiceHandler sh = new ServiceHandler();
            //String jsonStr = sh.makeServiceCall( APIAddress.HOTELS_API_MOBILE + "/GetStates", ServiceHandler.GET );
            String jsonStr = sh.makeServiceCall( APIAddress.HOTELS_API_MOBILE + "/GetCities", ServiceHandler.GET );

			if( jsonStr != null )
			{
				try
				{
					JSONArray results = new JSONArray( jsonStr );
					for( int i = 0; i < results.length(); i++ )
					{
						JSONObject c = results.getJSONObject( i );
						State state = new State( c );
						if( !state.isParsedOk() )
							return -3;
						_states.add( state );
					}

					return 0;
				}
				catch( Exception e )
				{
					android.util.Log.e( "JSONParser", "Cant parse city: " + e.getMessage() );
					return -2;
				}
			}
			else
				android.util.Log.e( "ServiceHandler", "Couldn't get any city" );

			return -1;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );
			_progress.dismiss();

			if( result != 0 )
			{
				AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
				alert.setTitle( "Atención" );
				alert.setMessage( "No se han podido obtener el listado de ciudades. Por favor intente nuevamente más tarde." );
				alert.setIcon( R.drawable.notification_warning_small );
				alert.setCancelable( false );
				alert.setButton( "OK", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
					}
				} );
				alert.show();
			}
			else
			{
				EstadosObtained();
			}
		}
	}
}
