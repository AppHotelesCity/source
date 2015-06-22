package com.zebstudios.cityexpress;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Denumeris Interactive on 23/10/2014.
 */
public class PromoCodeListAdapter extends ArrayAdapter<PromoCode>
{
    private final Context _context;
    private final ArrayList<PromoCode> _items;

    public PromoCodeListAdapter( Context context, ArrayList<PromoCode> items )
    {
        //super( context, R.layout.states_list_item, items );
        super(context, R.layout.promocode_list_item, items);
        _context = context;
        _items = items;

        Log.e("PromoCodeListAdapter", "Constructor listadapter :3" + items.toString());
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        Log.e("PromoCodeListAdapter", "getview :3");

        View rowView = convertView;
        if( rowView == null )
        {
            LayoutInflater inflater = LayoutInflater.from( _context );
            rowView = inflater.inflate( R.layout.promocode_list_item, null );
        }

        PromoCode item = _items.get( position );
        if( item != null )
        {
            TextView tt = (TextView)rowView.findViewById( R.id.lblText );
            if( tt != null )
            {
                tt.setText( item.gettitulo_promo() );
                Log.e("PromoCodeListAdapter", "Selecciono :3 -->" + item.gettitulo_promo());

            }

        }

        return rowView;
    }
}
