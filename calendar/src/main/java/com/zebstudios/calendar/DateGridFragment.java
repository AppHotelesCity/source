package com.zebstudios.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * DateGridFragment contains only 1 gridview with 7 columns to display all the
 * dates within a month.
 * <p/>
 * Client must supply gridAdapter and onItemClickListener before the fragment is
 * attached to avoid complex crash due to fragment life cycles.
 *
 * @author thomasdao
 */
public class DateGridFragment extends Fragment
{
	private GridView gridView;
	private CalendarGridAdapter gridAdapter;
	private AdapterView.OnItemClickListener onItemClickListener;
	private AdapterView.OnItemLongClickListener onItemLongClickListener;

	public AdapterView.OnItemClickListener getOnItemClickListener()
	{
		return onItemClickListener;
	}

	public void setOnItemClickListener( AdapterView.OnItemClickListener onItemClickListener )
	{
		this.onItemClickListener = onItemClickListener;
	}

	public AdapterView.OnItemLongClickListener getOnItemLongClickListener()
	{
		return onItemLongClickListener;
	}

	public void setOnItemLongClickListener( AdapterView.OnItemLongClickListener onItemLongClickListener )
	{
		this.onItemLongClickListener = onItemLongClickListener;
	}

	public CalendarGridAdapter getGridAdapter()
	{
		return gridAdapter;
	}

	public void setGridAdapter( CalendarGridAdapter gridAdapter )
	{
		this.gridAdapter = gridAdapter;
	}

	public GridView getGridView()
	{
		return gridView;
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		gridView = (GridView) inflater.inflate( R.layout.date_grid_fragment,
				container, false );
		// Client normally needs to provide the adapter and onItemClickListener
		// before the fragment is attached to avoid complex crash due to
		// fragment life cycles
		if( gridAdapter != null )
		{
			gridView.setAdapter( gridAdapter );
		}

		if( onItemClickListener != null )
		{
			gridView.setOnItemClickListener( onItemClickListener );
		}
		if( onItemLongClickListener != null )
		{
			gridView.setOnItemLongClickListener( onItemLongClickListener );
		}
		return gridView;
	}

}

