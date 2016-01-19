package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.ads.conversiontracking.AdWordsConversionReporter;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

import static com.appsee.Appsee.addEvent;

//import com.urbanairship.UAirship;
//import com.urbanairship.richpush.RichPushInbox;

public class MainActivity extends ActionBarActivity
{
	private Menu messagesAction;
	private ImageView imgMessagesIcon;
	private BadgeView messageCenterBadge;

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
	public boolean onCreateOptionsMenu(Menu menu) {


		Log.e("Main activity " , "Titulo :3 " +getTitle() );

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);

		MenuItem msgItem = menu.findItem(R.id.message_center);
		messagesAction = menu;

		msgItem.setActionView(R.layout.common_messages_indicator);

		if(msgItem.getActionView().findViewById(R.id.imgMessagesIcon) != null)
		{
			imgMessagesIcon = ((ImageView)msgItem.getActionView().findViewById(R.id.imgMessagesIcon));

			imgMessagesIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//((SlidingMenuBase)view.getContext()).onOptionsItemSelected(msgItem);
					Log.e("MainActivity", "Click en item message center");
					Intent intent = new Intent(getBaseContext(), MensajesPushActivity.class);
					startActivity(intent);
					openMessageCenter();
					OcultarBagde();
				}
			});

			/*
			RichPushInbox mensaje = UAirship.shared().getRichPushManager().getRichPushInbox();

			Log.d("Main activity", "Mensajes no leidos " + mensaje.getUnreadCount());
			updateMessagesBadge(mensaje.getUnreadCount());
			*/
		}
		//msgItem.setVisible(false);

		return super.onCreateOptionsMenu(menu);
	}

	public void OcultarBagde(){
		MenuItem msgItem = messagesAction.findItem(R.id.message_center);

		//msgItem.setVisible(false);
	}
	public void MostrarBagde(){
		MenuItem msgItem = messagesAction.findItem(R.id.message_center);
		msgItem.setVisible(true);
	}

	public void onBackPressed() {
		MostrarBagde();
		//final MessagecenterFragment fragment = (MessagecenterFragment) getSupportFragmentManager().findFragmentByTag("tag");

		//super.onBackPressed();
	}


		private void updateMessagesBadge(int badgeCnt)
	{
		if(messagesAction != null)
		{
			ImageView imgMessagesIcon = ((ImageView) messagesAction.getItem(0).getActionView().findViewById(R.id.imgMessagesIcon));


			if(messageCenterBadge == null && badgeCnt > 0)
			{
				messageCenterBadge = new BadgeView(this, imgMessagesIcon);
				messageCenterBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				messageCenterBadge.setBadgeMargin(0);
				messageCenterBadge.setTextSize(12);
				messageCenterBadge.setText(String.valueOf(badgeCnt));
				messageCenterBadge.show();
			}
			else if(messageCenterBadge != null && badgeCnt > 0 )
			{
				messageCenterBadge.setText(String.valueOf(badgeCnt));
				messageCenterBadge.show();
			}
			else if(messageCenterBadge != null && badgeCnt == 0) {
				messageCenterBadge.hide();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.message_center:
				openMessageCenter();
				return true;
			default:
				if( _actionBarDrawerToggle.isDrawerIndicatorEnabled() && _actionBarDrawerToggle.onOptionsItemSelected( item ) )
					return true;
				else if( item.getItemId() == android.R.id.home && getSupportFragmentManager().popBackStackImmediate() )
					return true;
				else
					return super.onOptionsItemSelected( item );
		}
	}
/*
	public boolean onOptionsItemSelected( MenuItem item )
	{
		if( _actionBarDrawerToggle.isDrawerIndicatorEnabled() && _actionBarDrawerToggle.onOptionsItemSelected( item ) )
			return true;
		else if( item.getItemId() == android.R.id.home && getSupportFragmentManager().popBackStackImmediate() )
			return true;
		else
			return super.onOptionsItemSelected( item );
	}

*/

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);

		_drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
		_actionBarDrawerToggle = new ActionBarDrawerToggle( this, _drawerLayout, R.string.app_name, R.string.app_name ) {};
		_drawerLayout.setDrawerListener( _actionBarDrawerToggle );

		getSupportActionBar().setHomeButtonEnabled( true );
		getSupportActionBar().setDisplayHomeAsUpEnabled( true );

		getSupportFragmentManager().addOnBackStackChangedListener( _onBackStackChangedListener );

		PrepareNavDrawerItems();

		android.util.Log.d( "TEST", "DPI: " + getResources().getConfiguration().screenWidthDp + "x" + getResources().getConfiguration().screenHeightDp );

			android.util.Log.d( "TEST", "PHONE" );
			PrincipalFragment fragment = new PrincipalFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();


		Analytics analytics = (Analytics)getApplication();
		analytics.sendAppScreenTrack("MAIN ANDROID");

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
	

	protected void onStart() {
		super.onStart();

		//
		//
		AdWordsConversionReporter.reportWithConversionId(this.getApplicationContext(),"997489850", "dLpzCPjT7lYQuvnR2wM", "1.00", true);

		/*
		AppRater appRater = new AppRater(this);
		//Dias en los que se volvera a lanzar la alerta
		appRater.setDaysBeforePrompt(5);
		//Numero de veces que se tenga que realizar la accion
		appRater.setLaunchesBeforePrompt(3);
		appRater.setPhrases("Califica esta app!", "Si te ha gustado City Express, ¿Te gustaria calificarnos? No tomará más de un minuto. ¡Gracias por tu colaboración!", "Calificar CityExpress", "Recordar mas tarde", "No, gracias");
		appRater.setTargetUri("https://play.google.com/store/apps/details?id=%1$s");
		//appRater.show();
		appRater.demo();

		Log.e("MainFragment", "Lanzar apprater D=");*/




	}

		@Override
	protected void onDestroy()
	{
		getSupportFragmentManager().removeOnBackStackChangedListener(_onBackStackChangedListener);
		super.onDestroy();
	}

	private void setActionBarArrowDependingOnFragmentsBackStack()
	{

		int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();

		_actionBarDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);


		FragmentManager manager = getSupportFragmentManager();
		if (manager != null)
		{
			Fragment fragment = manager.findFragmentById(R.id.fragment_container);
			fragment.onResume();
			Log.e("MainActivity", "FRAGMENT-----> S=S=S=");
		}

		Log.e("MainActivity", "NUMEROOOOO-----> " + backStackEntryCount );

		if( backStackEntryCount == 0 ) {
			_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			MostrarBagde();
		}else {
			OcultarBagde();
			_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}


	}



	@Override
	protected void onPostCreate( Bundle savedInstanceState )
	{
		super.onPostCreate(savedInstanceState);
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
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Cerca de tí", R.drawable.icon_cerca ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Reservaciones", R.drawable.icon_reservaciones ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "Blog", R.drawable.icon_blog ) );
		_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "City Premios", R.drawable.icon_premios_puntos ) );
		_navDrawerItems.add(new NavDrawerListAdapter.ListItem("Log Out", R.drawable.logout));

		//_navDrawerItems.add( new NavDrawerListAdapter.ListItem( "City Premios", R.drawable.icon_premios ) );

		NavDrawerListAdapter navDrawerListAdapter = new NavDrawerListAdapter( this, _navDrawerItems );
		LinearLayout linear = (LinearLayout) findViewById(R.id.linearFondo);
		linear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_drawerLayout.closeDrawers();
				PrincipalFragment fragment = new PrincipalFragment();
				//MainFragment fragment = new MainFragment();
				getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment).commit();
				addEvent("ViewHotelesSmartphone");
			}
		});
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

		//// **** AQUI SE CARGA PANTALLA PREMIOS CITY
		/*ImageView imgPremios = (ImageView)findViewById( R.id.navDrawerPremiosImg );
		imgPremios.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				premiosImageClicked();
			}
		} );
		*/
	}

	private static final int ACTIVITY_NEWS = 1111;
	private static final int ACTIVITY_BLOG = 2222;
	private static final int ACTIVITY_PREMIOS_LOGIN = 3333;
	private static final int ACTIVITY_PREMIOS_DETAIL = 4444;
	private static final int ACTIVITY_MESSAGE_CENTER = 5555;


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
			startActivityForResult(dialog, ACTIVITY_PREMIOS_LOGIN);
		}
	}

	private void DrawerItemSelected( int index )
	{
		_drawerLayout.closeDrawers();



		if( index == 0 )
		{
			if( CompatibilityUtil.isTablet( this ) )
			{
				//MainTabletFragment fragment = new MainTabletFragment();
				PrincipalFragment fragment = new PrincipalFragment();
				getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
				addEvent("ViewHotelesTablet");

			}
			else
			{
				PrincipalFragment fragment = new PrincipalFragment();
				//MainFragment fragment = new MainFragment();
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
			BlogFragment fragment = new BlogFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
			//Intent dialog = new Intent( this, BlogActivity.class );
			//startActivityForResult( dialog, ACTIVITY_BLOG );
			addEvent("MenuBlog-SmartPhone");
		}
		else if( index == 5 )
		{
			//Intent dialog = new Intent( this, BlogActivity.class );
			//startActivityForResult( dialog, ACTIVITY_BLOG );
			premiosImageClicked();
			addEvent("MenuCityPremios-SmartPhone");
		}
		else if(index == 6){
			SharedPreferences loginOut = getSharedPreferences(APIAddress.LOGIN_USUARIO_PREFERENCES, Context.MODE_PRIVATE);
			loginOut.edit().clear().commit();
            PremiosUserLoggedDS db = new PremiosUserLoggedDS( this );
            db.open();
            db.clearUser();
            db.close();
			PrincipalFragment fragment = new PrincipalFragment();
			//MainFragment fragment = new MainFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment).commit();
		}
	}

	public void premiosImageClicked()
	{
		_drawerLayout.closeDrawers();

		PremiosUserLoggedDS db = new PremiosUserLoggedDS( this );
		db.open();
		PremiosUserLoggedDS.PremiosUserLogged user = db.getUserLogged();
		db.close();

		if( user != null )
		{
			Intent dialog = new Intent( this, PremiosDetailActivity.class );
			startActivityForResult(dialog, ACTIVITY_PREMIOS_DETAIL);


			//Intent dialog = new Intent( this, PremiosLoginActivity.class );
			//startActivityForResult( dialog, ACTIVITY_PREMIOS_LOGIN );
		}
		else
		{
			PremiosLoginFragment fragment = new PremiosLoginFragment();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
			//PremiosLoginFragment fragment = new PremiosLoginFragment();
			//getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).commit();
			//Intent dialog = new Intent( this, PremiosDetailActivity.class );
			//startActivityForResult( dialog, ACTIVITY_PREMIOS_DETAIL );

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

	public void messagecenter(View view) {

		/*
		RichPushInbox mensaje = UAirship.shared().getRichPushManager().getRichPushInbox();
		List<RichPushMessage> mensajes = mensaje.getMessages();

		Log.d("Main activity", "Mensajes :3 " + mensaje.getCount() );
		Log.d("Main activity", "Mensajes no leidos " + mensaje.getUnreadCount() );
		Log.d("Main activity", "Mensajes leidos " + mensaje.getReadCount() );

		for(RichPushMessage msj : mensajes) {
			Log.e("Main Activity", "Titulo --> " + msj.getTitle() );
			Log.e("Main Activity", "Expiracion --> " + msj.getMessageId() );
		}
*/
		openMessageCenter();

}

	public void openMessageCenter(){
/*
		MessagecenterFragment fragment = new MessagecenterFragment();
		getSupportActionBar().setTitle("Inbox");
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "tag").addToBackStack(null).commit();
*/
	}
}
