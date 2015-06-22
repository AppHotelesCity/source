package com.zebstudios.cityexpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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


public class PromoCodeFragment extends DialogFragment
{
    private View _view;
    private ArrayList<PromoCode> _promocode;
    private ProgressDialogFragment _progress;

    /*
    public StatesFragment()
    {
        // Required empty public constructor
    }
    */

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        _view = inflater.inflate( R.layout.fragment_promo_code, container, false );

        new GetPromoCode().execute();

        Log.e("PromocodeFragment", "OncreateView :3");

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
            //ActionBarActivity activity = (ActionBarActivity) getActivity();
            //activity.getSupportActionBar().setTitle( "PromoCode" );
        }
    }

    private void EstadosObtained()
    {
        Log.e("TEST", "RESULTS: " + _promocode.size());


        Collections.sort(_promocode, new PromoCode.PromoCodeComparator() );

/*        PromoCode c = new PromoCode("lala", "Sin PromoCode");
        _promocode.add( 0, c );
        */

        PromoCodeListAdapter promoCodeListAdapter = new PromoCodeListAdapter(getActivity(), _promocode);
        ListView list = (ListView) _view.findViewById(R.id.result_list);
        list.setAdapter(promoCodeListAdapter);
        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
        {
            ItemSelected( i );
            Log.d("PromoCodeFragment", "Onitemclick :3 ");
        }
    }

        );


    }

    public void ItemSelected( int position )
    {
        Log.e("PromocodeFragment" , "ItemSelected :3");

        PromoCode promoCode = _promocode.get( position );
        HotelReservaFragment parent = (HotelReservaFragment) getTargetFragment();
        parent.escribirPromocode(promoCode);
        parent.setPromoCodeArray(_promocode);
        //getFragmentManager().popBackStackImmediate();
        this.dismiss();


        /*
        if( fragmentObject instanceof MainFragment )
        {
            MainFragment parent = (MainFragment) getTargetFragment();
            parent.setSelectedPromocode(promoCode);
            getFragmentManager().popBackStackImmediate();
            Log.e("PromocodeFragment", "Selecionar uno");

        }
        else if( fragmentObject instanceof MainTabletFragment )
        {
            MainTabletFragment parent = (MainTabletFragment) getTargetFragment();
            parent.setSelectedPromocode(promoCode);
            this.dismiss();
            Log.e("PromocodeFragment", "Selecionar dos");

        }*/


    }

    private class GetPromoCode extends AsyncTask<Void, Void, Integer>
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
            _promocode = new ArrayList<PromoCode>();

            ServiceHandler sh = new ServiceHandler();
            //String jsonStr = sh.makeServiceCall( APIAddress.HOTELS_API_MOBILE + "/GetStates", ServiceHandler.GET );
            String jsonStr = sh.makeServiceCall( "http://166.78.134.82/umbraco/api/MobileAppServices/GetPromoCodes", ServiceHandler.GET );

            if( jsonStr != null )
            {
                try
                {
                    JSONArray results = new JSONArray( jsonStr );
                    for( int i = 0; i < results.length(); i++ )
                    {
                        JSONObject c = results.getJSONObject( i );
                        /*
                        State state = new State( c );
                        if( !state.isParsedOk() )
                            return -3;
                        //_promocode.add( state );
                        */
                        PromoCode promoCode = new PromoCode(c);
                        if (!promoCode.isParsedOk() )
                            return  -3;
                        _promocode.add(promoCode);

                        Log.e("Promocode Fragment","--> " +results.length() + "Parseando y añadiendo promocode :3 - " + promoCode.gettitulo_promo() );
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
                alert.setMessage( "No se han podido obtener el listado de PromoCode. Por favor intente nuevamente más tarde." );
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
                Log.e("Promocodefragment", "Obtener estados :3");
                EstadosObtained();
            }
        }
    }
}
