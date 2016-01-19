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
 * Created by Denumeris Interactive on 4/27/2015.
 */
public class PremiosDetailActivity extends ActionBarActivity
{
	private PremiosDetailPuntosFragment _premiosPuntosFragment;
	private PremiosDetailCategoriasFragment _premiosPremiosFragment;
	private PremiosDetailCuentaFragment _premiosCuentaFragment;
	private PremiosExternoClient.EdoCtaResponse.ResRecord _resumen;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		setTheme( R.style.PremiosTheme );

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_premiosdetail );

		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setIcon( android.R.color.transparent );

		getSupportActionBar().setNavigationMode( android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS );

		_premiosPuntosFragment = new PremiosDetailPuntosFragment();
		_premiosPremiosFragment = new PremiosDetailCategoriasFragment();
		_premiosCuentaFragment = new PremiosDetailCuentaFragment();

		addTab( "Mis puntos", R.drawable.icon_premios_puntos, _premiosPuntosFragment );
		addTab( "Premios", R.drawable.icon_premios_premios, _premiosPremiosFragment );
		addTab( "Edo. de cuenta", R.drawable.icon_premios_cuenta, _premiosCuentaFragment );
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

	public void showInitialTab()
	{
		getSupportActionBar().setSelectedNavigationItem(0);
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

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		super.onOptionsItemSelected( item );
		switch( item.getItemId() )
		{
			case android.R.id.home:
				//PremiosDetailActivity.this.onBackPressed();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
		}
		return true;
	}

	public void setResumen( PremiosExternoClient.EdoCtaResponse.ResRecord resumen )
	{
		_resumen = resumen;
	}

	public PremiosExternoClient.EdoCtaResponse.ResRecord getResumen()
	{
		return _resumen;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
