package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import io.card.payment.CardIOActivity;

import static com.appsee.Appsee.addEvent;

/**
 * Created by Denumeris Interactive on 24/11/2014.
 */
public class TabletHotelDetailsActivity extends ActionBarActivity
{
	private Hotel _hotel;
	private ImageCache _imageCache;
	private TabletHotelDetailsFragment _tabletHotelDetailFragment;
	private TabletHotelReservaFragment _tabletHotelReservaFragment;

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
		setContentView( R.layout.activity_tablet_hoteldetails );

		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setIcon( android.R.color.transparent );
		getSupportActionBar().setTitle( _hotel.getNombre() );

		_imageCache = new ImageCache( this );
		//_imageCache.ClearCache();

		String[] images = new String[_hotel.getImagenesExtra().length];
		for( int i = 0; i < _hotel.getImagenesExtra().length; i++ )
		{
			images[i] = _hotel.getImagenesExtra()[i];
		}

		ViewPager viewPager = (ViewPager)findViewById( R.id.view_pager );
		ImagePagerAdapter adapter = new ImagePagerAdapter( images );
		viewPager.setAdapter( adapter );

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById( R.id.indicator );
		indicator.setViewPager( viewPager );
		indicator.setSnap( true );

		final float density = getResources().getDisplayMetrics().density;
		//indicator.setBackgroundColor(0xFFCCCCCC);
		indicator.setRadius( 2 * density );
		indicator.setPageColor( 0xFF264b89 ); // 0xFF264b89
		indicator.setFillColor( 0xFF264b89 );
		indicator.setStrokeColor( 0xFF9daeca );
		indicator.setStrokeWidth( 1 * density );

		_tabletHotelDetailFragment = new TabletHotelDetailsFragment();
		_tabletHotelReservaFragment = new TabletHotelReservaFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable( "HOTEL", _hotel );
		_tabletHotelDetailFragment.setArguments( bundle );
		_tabletHotelReservaFragment.setArguments( bundle );

		getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, _tabletHotelDetailFragment ).commit();

		RadioButton btnNoticias = (RadioButton)findViewById( R.id.btnInfo );
		btnNoticias.setChecked( true );
		SegmentedGroup segmentedGroup = (SegmentedGroup)findViewById( R.id.segmented );
		segmentedGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged( RadioGroup group, int checkedId )
			{
				handleOptions();
			}
		} );
	}

	@Override
	public void onResume()
	{
		super.onResume();

		//http://stackoverflow.com/questions/13419416/dynamically-set-viewpager-height
		/*final ViewPager viewPager = (ViewPager)findViewById( R.id.view_pager );
		ViewTreeObserver viewTreeObserver = viewPager.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);

						int viewPagerWidth = viewPager.getWidth();
						float viewPagerHeight = (float) (viewPagerWidth * (  388.0f / 640.0f ) );

						layoutParams.width = viewPagerWidth;
						layoutParams.height = (int) viewPagerHeight;

						viewPager.setLayoutParams(layoutParams);
						viewPager.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});*/
	}

	private void handleOptions()
	{
		RadioButton btnInfo = (RadioButton)findViewById( R.id.btnInfo );
		if( btnInfo.isChecked() )
		{
			android.util.Log.d( "TEST", "Details" );
			Fragment fragment = getSupportFragmentManager().findFragmentByTag( "Reservation2" );
			if(fragment != null)
				getSupportFragmentManager().beginTransaction().remove(fragment).commit();
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, _tabletHotelDetailFragment ).commit();

		}
		else
		{
			android.util.Log.d( "TEST", "Reserva" );
			getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, _tabletHotelReservaFragment ).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		super.onOptionsItemSelected( item );
		switch( item.getItemId() )
		{
			case android.R.id.home:
				TabletHotelDetailsActivity.this.onBackPressed();
				break;
		}
		return true;
	}

	public void presentReservation( long reservationId )
	{
		Intent intent = getIntent();
		intent.putExtra( "RESERVATION_ID", reservationId );
		setResult(RESULT_OK, intent);
		//finish();
	}

	public void getPromocode(View view) {
		//Lanzar vista de promocode
		Log.d("Promocode", "Click para lanzar vista de promocode");
		//_tabletHotelDetailFragment.AbrirPormo();
		_tabletHotelReservaFragment.AbrirPormo();
		//_hotelReservaFragment

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



	// https://github.com/chiuki/android-swipe-image-viewer
	private class ImagePagerAdapter extends PagerAdapter
	{
		private String[] _images;

		public ImagePagerAdapter( String[] images )
		{
			_images = images;
		}

		@Override
		public int getCount()
		{
			return _images.length;
		}


		@Override
		public boolean isViewFromObject( View view, Object object )
		{
			return view == object;
		}


		@Override
		public Object instantiateItem( ViewGroup container, int position )
		{
			Context context = TabletHotelDetailsActivity.this;
			ImageView imageView = new ImageView( context );
			int padding = 0;
			imageView.setPadding( padding, padding, padding, padding );
			imageView.setScaleType( ImageView.ScaleType.CENTER_CROP );
			new ImageLoader( imageView, _imageCache ).execute( _images[position] );
			//( (ViewPager) container ).addView( imageView, 0 );
			container.addView( imageView, 0 );
			return imageView;
		}

		@Override
		public void destroyItem( ViewGroup container, int position, Object object )
		{
			container.removeView((ImageView) object);
		}

		@Override public float getPageWidth(int position) { return(0.5f); }
	}
}
