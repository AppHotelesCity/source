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

/**
 * Created by Denumeris Interactive on 4/24/2015.
 */
public class CountriesDialogFragment extends DialogFragment
{
    private View _view;
    private ProgressDialogFragment _progress;
    private ArrayList<Country> _countries;

    public CountriesDialogFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        _view = inflater.inflate( R.layout.dialog_countries, container, false );

        new GetCountries().execute();

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
        getDialog().setTitle( "Países" );
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if( getDialog() == null )
        {
            ActionBarActivity activity = (ActionBarActivity) getActivity();
            activity.getSupportActionBar().setTitle( "Países" );
        }
    }

    private void countriesObtained()
    {
        android.util.Log.d( "TEST", "TOTAL: " + _countries.size() );

        Collections.sort( _countries, new Country.CountryComparator() );

        CountriesListAdapter countriesListAdapter = new CountriesListAdapter( getActivity(), _countries );
        ListView list = (ListView)_view.findViewById( R.id.result_list );
        list.setAdapter( countriesListAdapter );
        list.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
            {
                ItemSelected( i );
            }
        } );
    }

    public void ItemSelected( int position )
    {
        Country c = _countries.get( position );
        Fragment fragmentObject = getTargetFragment();
        if( fragmentObject instanceof PremiosRegisterFragment )
        {
            PremiosRegisterFragment parent = (PremiosRegisterFragment)getTargetFragment();
            parent.setSelectedCountry( c );
            if( CompatibilityUtil.isTablet( getActivity() ) )
                this.dismiss();
            else
                getFragmentManager().popBackStackImmediate();
        }
    }

    private class GetCountries extends AsyncTask<Void, Void, Integer>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            _progress = ProgressDialogFragment.newInstance();
            _progress.setCancelable( false );
            _progress.show( getFragmentManager(), "Dialog" );
        }

        @Override
        protected Integer doInBackground( Void... arg0 )
        {
            //_states = new ArrayList<State>();

            PremiosExternoClient client = new PremiosExternoClient();
            PremiosExternoClient.CountriesResponse response = client.getCountries();
            _countries = response.getCountries();

            return response.getResponse();
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
                alert.setMessage( "No se han podido obtener el listado de países. Por favor intente nuevamente más tarde." );
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
                countriesObtained();
            }
        }
    }
}
