package com.zebstudios.cityexpress;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class PromotionDetailFragment extends Fragment
{
	private View _view;
	private String _imageURL;

	public PromotionDetailFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		// Inflate the layout for this fragment
		_view = inflater.inflate( R.layout.fragment_promotion_detail, container, false );

		ImageView picture = (ImageView)_view.findViewById( R.id.promoImageView );
		ImageCache cache = new ImageCache( getActivity() );
		new ImageLoader( picture, cache ).execute( _imageURL );

		return _view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity)getActivity();
		activity.getSupportActionBar().setTitle( "Promociones" );
	}

	public String getImageURL()
	{
		return _imageURL;
	}

	public void setImageURL( String imageURL )
	{
		_imageURL = imageURL;
	}
}
