package com.zebstudios.cityexpress;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

import static com.appsee.Appsee.addEvent;

public class MainActivity extends ActionBarActivity
{
	ActionBarDrawerToggle _actionBarDrawerToggle;
	ArrayList<NavDrawerListAdapter.ListItem> _navDrawerItems;
	DrawerLayout _drawerLayout;
	private FragmentManager.OnBackStackChangedListener _onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener()
	{
		@Override
		public void onBackStackChanged()
		{
			setActionBarArrowDependingOnFragmentsBackStack();
		}
	};

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		Fabric.with(this, new Crashlytics());
		setContentView( R.layout.activity_main );

		_drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
		_actionBarDrawerToggle = new ActionBarDrawerToggle( this, _drawerLayout, R.string.app_name, R.string.app_name ) {};
		_drawerLayout.setDrawerListener( _actionBarDrawerToggle );

		getSupportActionBar().setHomeButtonEnabled( true );
		getSupportActionBar().setDisplayHomeAsUpEnabled( true );

		getSupportFragmentManager().addOnBackStackChangedListener( _onBackStackChangedListener );

		PrepareNavDrawerItems();

		android.util.Log.d( "TEST", "DPI: " + getResources().getConfiguration().screenWidthDp + "x" + getResources().getConfiguration().screenHeightDp );
		if( CompatibilityUtil.isTablet( this ) )
		{
			android.util.Log.d( "TEST", "TABLET" );
			MainTabletFragment fragment = new MainTabletFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
		}
		else
		{
			android.util.Log.d( "TEST", "PHONE" );
			MainFragment fragment = new MainFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
		}

		Analytics analytics = (Analytics)getApplication();
		analytics.sendAppScreenTrack( "MAIN ANDROID" );

		Appsee.start("51b7d802b9ad459185653998504d6813");
		FacebookSdk.sdkInitialize(getApplicationContext());


	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}
	

	@Override
	protected void onDestroy()
	{
		getSupportFragmentManager().removeOnBackStackChangedListener( _onBackStackChangedListener );
		super.onDestroy();
	}

	private void setActionBarArrowDependingOnFragmentsBackStack()
	{
		int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
		_actionBarDrawerToggle.setDrawerIndicatorEnabled( backStackEntryCount == 0 );
		if( backStackEntryCount == 0 )
			_drawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
		else
			_drawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );

		FragmentManager manager = getSupportFragmentManager();
		if (manager != null)
		{
			Fragment fragment = manager.findFragmentById( R.id.fragment_container );
			fragment.onResume();
		}
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		if( _actionBarDrawerToggle.isDrawerIndicatorEnabled() && _actionBarDrawerToggle.onOptionsItemSelected( item ) )
			return true;
		else if( item.getItemId() == android.R.id.home && getSupportFragmentManager().popBackStackImmediate() )
			return true;
		else
			return super.onOptionsItemSelected( item );
	}

	@Override
	protected void onPostCreate( Bundle savedInstanceState )
	{
		super.onPostCreate( savedInstanceState );
		_actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged( Configuration newConfig )
	{
		super.onConfigurationChanged( newConfig );
		_actionBarDrawerToggle.onConfigurationChanged( newConfig );
	}

	private void PrepareNavDrawerItems()
	{
		_navDrawerItems = new ArrayList<NavDrawerListAdapter.ListItem>();
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Hoteles", R.drawable.icon_hoteles ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Promociones", R.drawable.icon_promociones ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Cerca de ti", R.drawable.icon_cerca ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Reservaciones", R.drawable.icon_reservaciones ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Noticias", R.drawable.icon_noticias ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Blog", R.drawable.icon_blog ) );
		//_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "City Premios", R.drawable.icon_premios ) );

		NavDrawerListAdapter navDrawerListAdapter = new NavDrawerListAdapter( this, _navDrawerItems );

		ListView list = (ListView) findViewById( R.id.navDrawerList );
		list.setAdapter( navDrawerListAdapter );
		list.setOnItemClickListener( new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick( AdapterView<?> adapterView, View view, int index, long l )
			{
				DrawerItemSelected( index );
			}
		} );

		ImageView imgPremios = (ImageView)findViewById( R.id.navDrawerPremiosImg );
		imgPremios.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				premiosImageClicked();
			}
		} );
	}

	private static final int ACTIVITY_NEWS = 1111;
	private static final int ACTIVITY_BLOG = 2222;
	private static final int ACTIVITY_PREMIOS_LOGIN = 3333;
	private static final int ACTIVITY_PREMIOS_DETAIL = 4444;

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		if( requestCode == ACTIVITY_PREMIOS_LOGIN && resultCode == RESULT_OK )
		{
			Intent dialog = new Intent( this, PremiosDetailActivity.class );
			startActivityForResult( dialog, ACTIVITY_PREMIOS_DETAIL );
		}
		else if( requestCode == ACTIVITY_PREMIOS_DETAIL && resultCode == RESULT_OK )
		{
			Intent dialog = new Intent( this, PremiosLoginActivity.class );
			startActivityForResult( dialog, ACTIVITY_PREMIOS_LOGIN );
		}
	}

	private void DrawerItemSelected( int index )
	{
		_drawerLayout.closeDrawers();
		if( index == 0 )
		{
			if( CompatibilityUtil.isTablet( this ) )
			{
				MainTabletFragment fragment = new MainTabletFragment();
				getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
				addEvent("ViewHotelesTablet");

			}
			else
			{
				MainFragment fragment = new MainFragment();
				getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment).commit();
				addEvent("ViewHotelesSmartphone");
			}
		}
		else if( index == 1 )
		{
			PromotionsFragment fragment = new PromotionsFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
			addEvent("MenuPromociones-Smartphone");
		}
		else if( index == 2 )
		{
			CercaFragment fragment = new CercaFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
			addEvent("MenuCercaDeTi-Smartphone");
		}
		else if( index == 3 )
		{
			ReservacionesFragment fragment = new ReservacionesFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
			addEvent("MenuReservaciones-Smartphone");
		}
		else if( index == 4 )
		{
			Intent dialog = new Intent( this, NewsActivity.class );
			startActivityForResult( dialog, ACTIVITY_NEWS );
			addEvent("MenuNoticias-Smartphone");
		}
		else if( index == 5 )
		{
			Intent dialog = new Intent( this, BlogActivity.class );
			startActivityForResult( dialog, ACTIVITY_BLOG );
			addEvent("MenuBlog-SmartPhone");
		}
	}

	public void premiosImageClicked()
	{
		_drawerLayout.closeDrawers();

		PremiosUserLoggedDS db = new PremiosUserLoggedDS( this );
		db.open();
		PremiosUserLoggedDS.PremiosUserLogged user = db.getUserLogged();
		db.close();

		if( user == null )
		{
			Intent dialog = new Intent( this, PremiosLoginActivity.class );
			startActivityForResult( dialog, ACTIVITY_PREMIOS_LOGIN );
		}
		else
		{
			Intent dialog = new Intent( this, PremiosDetailActivity.class );
			startActivityForResult( dialog, ACTIVITY_PREMIOS_DETAIL );
		}

		addEvent("MenuCityPremios-Smartphone");
	}

	public void presentReservation( long reservationId )
	{
		Bundle parameters = new Bundle();
		parameters.putLong( "RESERVATION_ID", reservationId );

		ReservacionesFragment fragment = new ReservacionesFragment();
		fragment.setArguments( parameters );
		getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
	}

	public void showReservations()
	{
		getSupportFragmentManager().popBackStackImmediate();
		ReservacionesFragment fragment = new ReservacionesFragment();
		getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
	}
}
