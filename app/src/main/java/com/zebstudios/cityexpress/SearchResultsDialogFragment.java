package com.zebstudios.cityexpress;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SearchResultsDialogFragment extends DialogFragment
{
	private View _view;
	private ArrayList<Hotel> _results;
	private HotelsResultListAdapter _hotelsResultListAdapter;

	public SearchResultsDialogFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		_view = inflater.inflate( R.layout.fragment_search_results_dialog, container, false );
		_results = (ArrayList<Hotel>)getArguments().getSerializable( "RESULTS" );

		_hotelsResultListAdapter = new HotelsResultListAdapter( getActivity(), _results );
		ListView list = (ListView)_view.findViewById( R.id.result_list );
		list.setAdapter( _hotelsResultListAdapter );
		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
			{
				ItemSelected( i );
			}
		} );

		return _view;
	}

	public void ItemSelected( int position )
	{
		Hotel hotel = _results.get( position );
		Fragment fragmentObject = getTargetFragment();
		if( fragmentObject instanceof CercaFragment )
		{
			CercaFragment parent = (CercaFragment)getTargetFragment();
			parent.hotelSelected( hotel );
			this.dismiss();
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();

		super.onStart();
		if( getDialog() == null )
		{
			return;
		}

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		getDialog().getWindow().setLayout( width / 2, height / 2 );
		getDialog().setTitle( "Hoteles cercanos..." );
	}
}
