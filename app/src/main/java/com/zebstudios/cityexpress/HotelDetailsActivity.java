package com.zebstudios.cityexpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static com.appsee.Appsee.addEvent;

/**
 * Created by Denumeris Interactive on 23/10/2014.
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
		super.onOptionsItemSelected(item);
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
		tab.setCustomView(R.layout.hotel_tab_indicator);
		TextView textView = (TextView)tab.getCustomView().findViewById( R.id.title );
		textView.setText(title);
		ImageView imageView = (ImageView)tab.getCustomView().findViewById( R.id.icon );
		imageView.setImageResource(drawable);

		tab.setTabListener(new TabListener(fragment));
		getSupportActionBar().addTab(tab);
	}

	public void onScanPress(View view) {
		Log.d("escanear ", "€scanear");


		Intent scanIntent = new Intent(this, CardIOActivity.class);

		// customize these values to suit your needs.
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

		// hides the manual entry button
		// if set, developers should provide their own manual entry mechanism in the app
		scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

		// matches the theme of your application
		scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

		// MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
		startActivityForResult(scanIntent, 100);
		addEvent("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		EditText txtCardNumber = (EditText) this.findViewById(R.id.txtCardNumber);
		Spinner spinExpMonth = (Spinner) this.findViewById( R.id.spinExpMonth );
		Spinner spinExpYear = (Spinner) this.findViewById( R.id.spinExpYear );
		EditText txtCardCode = (EditText) this.findViewById(R.id.txtCardCode);

		String resultStr;
		if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
			CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

			// Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
			resultStr = "Card Number: " + scanResult.getFormattedCardNumber() + "\n";
			Log.d("Escanear redacted --> ", scanResult.getRedactedCardNumber());
			Log.d("Escanear formater --> ", scanResult.getFormattedCardNumber());
			//txtCardNumber.setText(scanResult.getRedactedCardNumber());
			txtCardNumber.setText(scanResult.getFormattedCardNumber());

			// Do something with the raw number, e.g.:
			// myService.setCardNumber( scanResult.cardNumber );


			if (scanResult.isExpiryValid()) {
				resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
				//spinExpMonth.setSelection(scanResult.expiryMonth);
				//spinExpYear.setSelection(scanResult.expiryYear);
				Log.d("Escanear --> ", "spinner --> " + scanResult.expiryMonth + " - " + scanResult.expiryYear);


				spinExpMonth.setSelection(scanResult.expiryMonth -0);
				spinExpYear.setSelection(scanResult.expiryYear-2014);
			}

			if (scanResult.cvv != null) {
				// Never log or display a CVV
				resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
				txtCardCode.setText(scanResult.cvv);
			}

			if (scanResult.postalCode != null) {
				resultStr += "Postal Code: " + scanResult.postalCode + "\n";
			}




		} else {
			resultStr = "Scan was canceled.";
		}
		//resultTextView.setText(resultStr);
		Log.d("Escanear --> ", resultStr);

		//gato


	}


	public void pintar(View view) {

		EditText txtCardName = (EditText) view.findViewById( R.id.txtCardName );

	}

	public void getPromocode(View view) {
		//Lanzar vista de promocode
		Log.d("Promocode", "Click para lanzar vista de promocode");
		_hotelReservaFragment.AbrirPormo();
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
			ft.remove(_fragment);
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
