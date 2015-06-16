package com.zebstudios.cityexpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rczuart on 23/10/2014.
 */
public class HotelDetailsActivity extends ActionBarActivity
{
	private Hotel _hotel;
	private HotelDetailsFragment _hotelDetailFragment;
	private HotelServicesFragment _hotelServicesFragment;
	private HotelMapFragment _hotelMapFragment;
	private HotelReservaFragment _hotelReservaFragment;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		Intent intent = getIntent();
		_hotel = (Hotel) intent.getSerializableExtra( "HOTEL" );

		if( _hotel.getIdMarca() == 1 )
			setTheme( R.style.MainTheme );
		else if( _hotel.getIdMarca() == 4 )
			setTheme( R.style.PlusTheme );
		else if( _hotel.getIdMarca() == 3 )
			setTheme( R.style.SuitesTheme );
		else if( _hotel.getIdMarca() == 2 )
			setTheme( R.style.JuniorTheme );
		else
			setTheme( R.style.MainTheme );

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_hoteldetails );

		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setIcon( android.R.color.transparent );
		getSupportActionBar().setTitle( _hotel.getNombre() );

		getSupportActionBar().setNavigationMode( android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS );

		_hotelDetailFragment = new HotelDetailsFragment();
		_hotelServicesFragment = new HotelServicesFragment();
		_hotelMapFragment = new HotelMapFragment();
		_hotelReservaFragment = new HotelReservaFragment();

		Bundle bundle = new Bundle();
		bundle.putSerializable( "HOTEL", _hotel );
		_hotelDetailFragment.setArguments( bundle );
		_hotelServicesFragment.setArguments( bundle );
		_hotelMapFragment.setArguments( bundle );
		_hotelReservaFragment.setArguments( bundle );

		addTab( "Descripción", R.drawable.tab_desc, _hotelDetailFragment );
		addTab( "Servicios", R.drawable.tab_serv, _hotelServicesFragment );
		addTab( "Mapa", R.drawable.tab_map, _hotelMapFragment );
		addTab( "Reservar", R.drawable.tab_res, _hotelReservaFragment );
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		super.onOptionsItemSelected( item );
		switch( item.getItemId() )
		{
			case android.R.id.home:
				HotelDetailsActivity.this.onBackPressed();
				break;
		}
		return true;
	}

	private void addTab( String title, int drawable, Fragment fragment )
	{
		android.support.v7.app.ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setCustomView( R.layout.hotel_tab_indicator );
		TextView textView = (TextView)tab.getCustomView().findViewById( R.id.title );
		textView.setText( title );
		ImageView imageView = (ImageView)tab.getCustomView().findViewById( R.id.icon );
		imageView.setImageResource( drawable );

		tab.setTabListener( new TabListener( fragment ) );
		getSupportActionBar().addTab( tab );
	}

	public class TabListener implements android.support.v7.app.ActionBar.TabListener
	{
		Fragment _fragment;
		public TabListener( Fragment fragment )
		{
			_fragment = fragment;
		}

		public void onTabSelected( android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft)
		{
			ft.replace( R.id.fragment_container, _fragment );
		}

		public void onTabUnselected( android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft)
		{
			ft.remove( _fragment );
		}

		public void onTabReselected( android.support.v7.app.ActionBar.Tab tab, FragmentTransaction ft)
		{
			// Nada
		}
	}

	public void presentReservation( long reservationId )
	{
		Intent intent = getIntent();
		intent.putExtra( "RESERVATION_ID", reservationId );
		setResult( RESULT_OK, intent );
		finish();
	}

	private Bundle _reserva2FragmentArgs;

	public void saveReserva2FragmentArgs( Bundle args )
	{
		_reserva2FragmentArgs = args;
	}

	public Bundle getReserva2FragmentArgs()
	{
		return _reserva2FragmentArgs;
	}
}
