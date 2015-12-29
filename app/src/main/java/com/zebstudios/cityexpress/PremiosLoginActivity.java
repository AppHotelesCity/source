package com.zebstudios.cityexpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Denumeris Interactive on 4/23/2015.
 */
public class PremiosLoginActivity extends ActionBarActivity
{

    private  View view;

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        view = inflater.inflate(R.layout.fragment_premioslogin, container, false);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ActionBarActivity activity = (ActionBarActivity)getActivity();
        activity.getSupportActionBar().setTitle( "Reservaciones" );
        activity.setTheme(R.style.PremiosTheme);

    }*/


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        setTheme( R.style.PremiosTheme );

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_premioslogin );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setIcon( android.R.color.transparent );

        getSupportFragmentManager().addOnBackStackChangedListener( getListener() );

        PremiosLoginFragment fragment = new PremiosLoginFragment();
        getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
    }

    private FragmentManager.OnBackStackChangedListener getListener()
    {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener()
        {
            public void onBackStackChanged()
            {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null)
                {
                    Fragment fragment = manager.findFragmentById( R.id.fragment_container );
                    fragment.onResume();
                }
            }
        };

        return result;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        super.onOptionsItemSelected( item );
        switch( item.getItemId() )
        {
            case android.R.id.home:
                PremiosLoginActivity.this.onBackPressed();
                break;
        }
        return true;
    }


}
