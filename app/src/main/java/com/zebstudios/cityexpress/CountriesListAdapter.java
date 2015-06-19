package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Denumeris Interactive on 4/24/2015.
 */
public class CountriesListAdapter extends ArrayAdapter<Country>
{
    private final Context _context;
    private final ArrayList<Country> _items;

    public CountriesListAdapter( Context context, ArrayList<Country> items )
    {
        super( context, R.layout.states_list_item, items );
        _context = context;
        _items = items;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View rowView = convertView;
        if( rowView == null )
        {
            LayoutInflater inflater = LayoutInflater.from( _context );
            rowView = inflater.inflate( R.layout.states_list_item, null );
        }

        Country item = _items.get( position );
        if( item != null )
        {
            TextView tt = (TextView)rowView.findViewById( R.id.lblText );
            if( tt != null )
            {
                tt.setText( item.getName() );
            }

        }

        return rowView;
    }
}
