package com.zebstudios.cityexpress;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Denumeris Interactive on 23/10/2014.
 */
public class ServicesListAdapter extends ArrayAdapter<ServicesListAdapter.Servicio>
{
	private final Activity _context;
	private final ArrayList<Servicio> _items;
	private final boolean _isTablet;

	public ServicesListAdapter( Activity context, int[] services )
	{
		super( context, R.layout.services_list_item, new ArrayList<Servicio>() );

		_context = context;

		_items = new ArrayList<Servicio>();
		for( int i=0; i<services.length; i++ )
		{
			Servicio s = new Servicio( services[i] );
			_items.add( s );
			super.add( s );
		}

		_isTablet = CompatibilityUtil.isTablet( context );
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		View rowView = convertView;
		if( rowView == null )
		{
			LayoutInflater inflater = _context.getLayoutInflater();
			if( _isTablet )
				rowView = inflater.inflate( R.layout.services_list_tablet_item, null );
			else
				rowView = inflater.inflate( R.layout.services_list_item, null );
		}

		Servicio item = _items.get( position );
		if( item != null )
		{
			TextView tt = (TextView)rowView.findViewById( R.id.lblText );
			if( tt != null )
			{
				Drawable img = _context.getResources().getDrawable( item.getResourceId() );
				if( _isTablet )
				{
					img.setBounds( 0, 0, 30, 30 );
				}
				else
				{
					img.setBounds( 0, 0, 60, 60 );
				}
				tt.setCompoundDrawables( img, null, null, null );
				tt.setText( item.getText() );
			}

		}

		return rowView;
	}

	public static class Servicio
	{
		private int _type;
		private int _resourceId;
		private String _text;

		public Servicio( int type )
		{
			_type = type;
			_resourceId = R.drawable.serv_01;
			_text = "Servicio " + _type;

			if( _type == 1 )
			{
				_text = "En zona industrial";
			}
			else if( _type == 2 )
			{
				_text = "Max. 5 kms del aeropuerto";
			}
			else if( _type == 3 )
			{
				_text = "Max. 10 kms del aeropuerto";
			}
			else if( _type == 4 )
			{
				_text = "Max. 5 kms de terminal de autobuses";
			}
			else if( _type == 5 )
			{
				_text = "Sobre la carretera federal";
			}
			else if( _type == 6 )
			{
				_text = "En el centro de la ciudad";
			}
			else if( _type == 7 )
			{
				_text = "Aire Acondicionado";
				_resourceId = R.drawable.s_07;
			}
			else if( _type == 8 )
			{
				_text = "Alberca";
				_resourceId = R.drawable.s_08;
			}
			else if( _type == 9 )
			{
				_text = "Cajas de seguridad";
				_resourceId = R.drawable.s_09;
			}
			else if( _type == 10 )
			{
				_text = "Centro de negocios";
				_resourceId = R.drawable.s_10;
			}
			else if( _type == 11 )
			{
				_text = "Cocineta";
				_resourceId = R.drawable.s_11;
			}
			else if( _type == 12 )
			{
				_text = "Desayuno continental";
				_resourceId = R.drawable.s_12;
			}
			else if( _type == 13 )
			{
				_text = "Estacionamiento cortesía";
				_resourceId = R.drawable.s_13;
			}
			else if( _type == 14 )
			{
				_text = "Estacionamiento con costo";
				_resourceId = R.drawable.s_14;
			}
			else if( _type == 15 )
			{
				_text = "Gimnasio";
				_resourceId = R.drawable.s_15;
			}
			else if( _type == 16 )
			{
				_text = "Instalaciones para personas con capacidades diferentes";
				_resourceId = R.drawable.s_16;
			}
			else if( _type == 17 )
			{
				_text = "Internet cortesía";
				_resourceId = R.drawable.s_17;
			}
			else if( _type == 18 )
			{
				_text = "Sala de Juntas";
				_resourceId = R.drawable.s_18;
			}
			else if( _type == 19 )
			{
				_text = "Servicio de tintorería";
				_resourceId = R.drawable.s_19;
			}
			else if( _type == 20 )
			{
				_text = "Servicio de Concierge";
				_resourceId = R.drawable.s_20;
			}
			else if( _type == 21 )
			{
				_text = "Transportación";
				_resourceId = R.drawable.s_21;
			}
			else if( _type == 22 )
			{
				_text = "TV con cable";
				_resourceId = R.drawable.s_22;
			}
			else if( _type == 23 )
			{
				_text = "Room Service";
				_resourceId = R.drawable.s_23;
			}
			else if( _type == 24 )
			{
				_text = "Teléfono acceso a larga distancia";
				_resourceId = R.drawable.s_24;
			}
			else if( _type == 25 )
			{
				_text = "Despertador Automático";
				_resourceId = R.drawable.s_25;
			}
			else if( _type == 26 )
			{
				_text = "Enseres de planchado";
				_resourceId = R.drawable.s_26;
			}
			else if( _type == 27 )
			{
				_text = "Teléfono con buzón de voz";
				_resourceId = R.drawable.s_27;
			}
			else if( _type == 28 )
			{
				_text = "Servicio de lavandería";
				_resourceId = R.drawable.s_28;
			}
			else if( _type == 29 )
			{
				_text = "Comedor";
				_resourceId = R.drawable.s_29;
			}
			else if( _type == 30 )
			{
				_text = "Snack - Bar";
				_resourceId = R.drawable.s_30;
			}
		}

		public int getType()
		{
			return _type;
		}

		public int getResourceId()
		{
			return _resourceId;
		}

		public String getText()
		{
			return _text;
		}
	}
}
