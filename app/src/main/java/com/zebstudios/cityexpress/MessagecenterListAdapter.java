/*
package com.zebstudios.cityexpress;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.urbanairship.richpush.RichPushMessage;

import java.util.ArrayList;


public class MessagecenterListAdapter extends ArrayAdapter<RichPushMessage>
{
    private final Context _context;
    private final ArrayList<RichPushMessage> _items;

    public MessagecenterListAdapter( Context context, ArrayList<RichPushMessage> items )
    {
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

        RichPushMessage item = _items.get( position );
        if( item != null )
        {
            TextView tt = (TextView)rowView.findViewById( R.id.lblText );
            if( tt != null )
            {
                tt.setText( item.getTitle() );
                Log.e("PromoCodeListAdapter", "Selecciono :3 -->" + item.getTitle() );

            }

        }

        if ( !item.isRead() ){
            rowView.setBackgroundColor(Color.parseColor("#EF4C41"));

        }

        item.markRead();
        notifyDataSetChanged();

        return rowView;
    }
}
*/