package com.zebstudios.cityexpress;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.appsee.Appsee.addEvent;
import static com.appsee.Appsee.startScreen;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class HotelReservaResultFragment extends Fragment
{
	private View _view;
	private Reservation _reservation;
	private GoogleMap _map;
	private MapView _mapView;

	public HotelReservaResultFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		Bundle args = getArguments();
		_reservation = (Reservation) args.getSerializable( "RESERVATION" );

		if( CompatibilityUtil.isTablet( getActivity() ) ){
			_view = inflater.inflate( R.layout.fragment_hotel_reserva_result_tablet, container, false );
			startScreen("ViewHotelReservaResult-Tablet");}
		else {
			_view = inflater.inflate( R.layout.fragment_hotel_reserva_result, container, false );
			startScreen("ViewHotelReservaResult-Smartphone");
		}


		NestedListView listReservas = (NestedListView) _view.findViewById( R.id.list_reservations );
		ReservationsSummaryListAdapter reservationsListAdapter = new ReservationsSummaryListAdapter( getActivity(), _reservation.getRooms() );
		listReservas.setAdapter( reservationsListAdapter );

		SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
		TextView lblHotelName = (TextView) _view.findViewById( R.id.lblHotelName );
		TextView lblArrivalDate = (TextView) _view.findViewById( R.id.dates_arrival_text );
		TextView lblDepartureDate = (TextView) _view.findViewById( R.id.dates_departure_text );
		TextView lblTotal = (TextView) _view.findViewById( R.id.lblTotal );

		lblHotelName.setText( _reservation.getHotelName() );
		lblArrivalDate.setText( sdf.format( _reservation.getArrivalDate() ) );
		lblDepartureDate.setText( sdf.format( _reservation.getDepartureDate() ) );

		lblTotal.setText( String.format( "Total: $%,.2f ", getTotalCost() ) + _reservation.getMoneda() );

		ArrayList<SummaryEntry> sumary = new ArrayList<SummaryEntry>();
		for( int i = 0; i < _reservation.getRooms().size(); i++ )
		{
			sumary.add( new SummaryEntry( 0, "Habitación " + ( i + 1 ) ) );
			for( int j = 0; j < _reservation.getRooms().get( i ).getNights().size(); j++ )
			{
				sumary.add( new SummaryEntry( 1, "Noche " + ( j + 1 ) + String.format( " $%,.2f ", _reservation.getRooms().get( i ).getNights().get( j ) ) + _reservation.getMoneda() ) );
			}
		}
		NestedListView listSummary = (NestedListView) _view.findViewById( R.id.list_summary );
		SummaryListAdapter adapter = new SummaryListAdapter( getActivity(), sumary );
		listSummary.setAdapter( adapter );

		TextView lblAddress1 = (TextView) _view.findViewById( R.id.lblAddress1 );
		lblAddress1.setText( _reservation.getHotelAddress() );

		TextView lblAddress2 = (TextView) _view.findViewById( R.id.lblAddress2 );
		lblAddress2.setText( _reservation.getHotelNearest() );

		_mapView = (MapView) _view.findViewById( R.id.mapView );
		_mapView.onCreate( savedInstanceState );
		_mapView.onResume();

		try
		{
			MapsInitializer.initialize( getActivity().getApplicationContext() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		_map = _mapView.getMap();

		if( _map != null )
		{
			LatLng hotelPosition = new LatLng( _reservation.getHotelLatitude(), _reservation.getHotelLongitude() );
			Marker m = _map.addMarker( new MarkerOptions().position( hotelPosition ).title( _reservation.getHotelName() ) );
			_map.moveCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
			m.showInfoWindow();
		}

		Button btnSendEmail = (Button) _view.findViewById( R.id.btnSendEmail );
		btnSendEmail.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				Intent email = new Intent( Intent.ACTION_SEND );
				email.putExtra( Intent.EXTRA_SUBJECT, "Reservación" );
				email.putExtra( Intent.EXTRA_TEXT, getEmailBody() );
				email.setType( "message/rfc822" );
				startActivityForResult( Intent.createChooser( email, "Send Email" ), 1 );
				addEvent("HotelResultEmail-Smartphone");
			}
		} );

		// http://stackoverflow.com/questions/13286358/sharing-to-facebook-twitter-via-share-intent-android
		// TODO: Retringir Share a programas seleccionados?
		Button btnShare = (Button) _view.findViewById( R.id.btnShare );
		btnShare.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				Intent intent = new Intent( Intent.ACTION_SEND );
				intent.putExtra( Intent.EXTRA_TEXT, "Ya tengo mi reserva en " + _reservation.getHotelName() );
				intent.setType( "text/plain" );
				startActivityForResult( Intent.createChooser( intent, "Compartir..." ), 2 );
				addEvent("HotelResultShare-Smartphone");
			}
		} );

		Button btnOpenLocation = (Button) _view.findViewById( R.id.btnOpenLocation );
		btnOpenLocation.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				String uri = "geo:" + _reservation.getHotelLatitude() + "," + _reservation.getHotelLongitude() + "?q=" + _reservation.getHotelLatitude() + "," + _reservation.getHotelLongitude() + "(" + _reservation.getHotelName() + ")";
				startActivityForResult( new Intent( Intent.ACTION_VIEW, Uri.parse( uri ) ), 3 );
				addEvent("HotelResultOpenLocation-Smartphone");
			}
		} );

		if( _reservation.getHotelPhone() != null )
		{
			ImageButton btnCall = (ImageButton) _view.findViewById( R.id.btnCall );
			btnCall.setOnClickListener( new View.OnClickListener()
			{
				@Override
				public void onClick( View view )
				{
					String uri = "tel:" + _reservation.getHotelPhone();
					Intent intent = new Intent( Intent.ACTION_CALL );
					intent.setData( Uri.parse( uri ) );
					startActivityForResult( intent, 0 );
				}
			} );
		}

		if( _reservation.getHotelEmail() != null )
		{
			ImageButton btnEmail = (ImageButton) _view.findViewById( R.id.btnMail );
			btnEmail.setOnClickListener( new View.OnClickListener()
			{
				@Override
				public void onClick( View view )
				{
					String to = _reservation.getHotelEmail();
					Intent email = new Intent( Intent.ACTION_SEND );
					email.putExtra( Intent.EXTRA_EMAIL, new String[]{ to } );
					email.setType( "message/rfc822" );
					startActivityForResult( Intent.createChooser( email, "Send Email" ), 1 );
				}
			} );
		}

		Button btnDelete = (Button) _view.findViewById( R.id.btnDelete );
		Date today = new Date( System.currentTimeMillis() );
		if( _reservation.getDepartureDate().before( today ) )
		{
			btnDelete.setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick( View v )
				{
					confirmDeleteReservation();
					addEvent("HotelResultDelete-Smartphone");
				}
			} );
		}
		else
		{
			btnDelete.setVisibility( View.GONE );
		}

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppEventTrack( "RESERVATIONS ANDROID", "RESERVA DETAIL", "HOTEL", _reservation.getHotelName(), 1 );

		return _view;
	}

	private void confirmDeleteReservation()
	{
		AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
		alert.setTitle( "Atención" );
		alert.setMessage( "¿Estás seguro de que deseas eliminar esta reservación del historial?" );
		alert.setIcon( R.drawable.notification_warning_small );
		alert.setCancelable( false );
		alert.setButton( DialogInterface.BUTTON_POSITIVE, "Eliminar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				deleteReservation();
			}
		} );
		alert.setButton( DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				android.util.Log.d( "TEST", "CANCEL DELETE" );
			}
		} );
		alert.show();
	}

	private void deleteReservation()
	{
		ReservationDS ds = new ReservationDS( getActivity() );
		ds.open();
		ds.delete( _reservation );
		ds.close();

		MainActivity mainActivity = (MainActivity)getActivity();
		mainActivity.showReservations();
	}

	private String getEmailBody()
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );

		String body = "Tu reservación ha sido confirmada con la(s) clave(s): ";
		double total = 0;

		for( int i = 0; i < _reservation.getRooms().size(); i++ )
		{
			body += _reservation.getRooms().get( i ).getReservationNumber() + ", ";
		}
		body = body.substring( 0, body.length() - 2 );

		body += "\n\nHOTEL";
		body += "\n" + _reservation.getHotelName();
		body += "\nDirección: " + _reservation.getHotelAddress();
		if( _reservation.getHotelPhone() != null )
		{
			body += "\nTeléfono: " + _reservation.getHotelPhone();
		}
		body += "\nLlegada: " + sdf.format( _reservation.getArrivalDate() );
		body += "\nCheck in: 15:00";
		body += "\nSalida: " + sdf.format( _reservation.getDepartureDate() );
		body += "\nCheck out: 13:00";

		body += "\n\nHABITACIONES";
		for( int i = 0; i < _reservation.getRooms().size(); i++ )
		{
			body += "\nTitular: " + _reservation.getRooms().get( i ).getReservationTitular() + "\nHabitación: " + _reservation.getRooms().get( i ).getHabDescription() + "\nNoches: " +_reservation.getRooms().get( i ).getNights().size() + "\nTarifa: " + String.format( " $%,.2f ", _reservation.getRooms().get( i ).getNights().get( 0 ) ) + _reservation.getMoneda();
		}

		for( int i = 0; i < _reservation.getRooms().size(); i++ )
		{
			for( int j=0; j<_reservation.getRooms().get( i ).getNights().size(); j++ )
			{
				total += _reservation.getRooms().get( i ).getNights().get( j );
			}
		}

		body += "\n\nTotal: " +String.format( " $%,.2f ", total ) + _reservation.getMoneda();
		body += "Tarifas sujetas a cambios sin previo aviso.\n" +
				"Aplica cargo por persona extra.\n" +
				"Aplican restricciones.";

		body += "\n\nPolíticas de cambios y cancelaciones de reservaciones:";
		body += "\n\n1.- Cualquier cambio o cancelación a su reservación deberá solicitarla al 01 800 248 9397con anticipación mínima de 24 horas antes de la fecha de llegada al hotel proporcionando su (s) clave (s) de confirmación.";
		body += "\n\n2.- En temporada alta cualquier cambio ó cancelación deberá solicitarlo 72 horas antes de la fecha de llegada al hotel y proporcionar su (s) clave (s) de confirmación. Para conocer la información acerca de las fechas de temporada alta, consúltenos al 01 800 248 9397.";
		body += "\n\n3.- De no realizarse el cambio o cancelación en el tiempo y forma antes mencionada, se hará el cargo por el monto de una noche más impuestos por cada habitación a la tarjeta de crédito con la que se garantizó la reservación.";
		body += "\n\n4.- El día de llegada, el hotel pre-verificará el crédito de la tarjeta de crédito con la que se garantiza la reservación. Si dicha tarjeta no es autorizada, y en caso de alta ocupación en el hotel, la reservación no garantizada se cancelará automáticamente.";
		body += "\n\n5.- En estancias de más de una noche, si el huésped no se presenta al hotel, se aplicará el cargo de “No Show” solo por la primera noche de habitación reservada más impuestos a la tarjeta de crédito con la que se garantizó la reservación. El resto de la estancia se cancelará automáticamente.";
		body += "\n\nHabrá cargo extra por persona adicional (mayor a 12 años).";
		body += "\nLa capacidad máxima de personas dependerá del tipo habitación reservado.";
		body += "\nEl costo varía según la marca del hotel.";

		return body;
	}

	private double getTotalCost()
	{
		double total = 0;

		for( int i = 0; i < _reservation.getRooms().size(); i++ )
		{
			for( int j = 0; j < _reservation.getRooms().get( i ).getNights().size(); j++ )
			{
				total += _reservation.getRooms().get( i ).getNights().get( j );
			}
		}

		return total;
	}
}
