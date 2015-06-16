package com.zebstudios.cityexpress;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rczuart on 4/27/2015.
 */
public class PremiosDetailCuentaFragment extends Fragment
{
	private View _view;
	private ProgressDialogFragment _progress;
	private PremiosUserLoggedDS.PremiosUserLogged _user;

	DatePickerNoDayDialog _datePickerDialogFrom;
	Date _dateFrom;
	Boolean _setDateFrom;

	DatePickerNoDayDialog _datePickerDialogTo;
	Date _dateTo;
	Boolean _setDateTo;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) )
			_view = inflater.inflate( R.layout.fragment_premios_cuenta_tablet, container, false );
		else
			_view = inflater.inflate( R.layout.fragment_premios_cuenta, container, false );

		PremiosUserLoggedDS db = new PremiosUserLoggedDS( getActivity() );
		db.open();
		_user = db.getUserLogged();
		db.close();

		setInitialDates();
		setDatePickerDialogFrom();
		setDatePickerDialogTo();

		Button btnFrom = (Button) _view.findViewById( R.id.btnFrom );
		btnFrom.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				_datePickerDialogFrom.show();
			}
		} );
		Button btnTo = (Button) _view.findViewById( R.id.btnTo );
		btnTo.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				_datePickerDialogTo.show();
			}
		} );

		return _view;
	}

	private void datesUpdated()
	{
		if( _dateFrom == null || _dateTo == null )
		{
			android.util.Log.d( "TEST", "HIDE LIST" );
			ListView list = (ListView) _view.findViewById( R.id.listCuenta );
			list.setVisibility( View.GONE );
		}
		else
		{
			android.util.Log.d( "TEST", "UPDATE LIST" );
			new ServiceCaller( _user.getSocio(), _dateFrom, _dateTo ).execute();
		}
	}

	private void dataObtained( PremiosExternoClient.EdoCtaResponse response )
	{
		if( response.getResponse() == 0 )
		{
			ArrayList<PremiosExternoClient.EdoCtaResponse.Record> items = new ArrayList<PremiosExternoClient.EdoCtaResponse.Record>();

			for( int i = 0; i < response.getCRB().size(); i++ )
			{
				addIfValid( items, response.getCRB().get( i ) );
			}
			for( int i = 0; i < response.getPRB().size(); i++ )
			{
				addIfValid( items, response.getPRB().get( i ) );
			}
			for( int i = 0; i < response.getACR().size(); i++ )
			{
				addIfValid( items, response.getACR().get( i ) );
			}
			for( int i = 0; i < response.getRED().size(); i++ )
			{
				addIfValid( items, response.getRED().get( i ) );
			}
			for( int i = 0; i < response.getSWP().size(); i++ )
			{
				addIfValid( items, response.getSWP().get( i ) );
			}
			for( int i = 0; i < response.getMAT().size(); i++ )
			{
				addIfValid( items, response.getMAT().get( i ) );
			}

			Collections.sort( items, new PremiosExternoClient.EdoCtaResponse.Record.Comparator() );

			EdoCuentaListAdapter edoCuentaListAdapter = new EdoCuentaListAdapter( getActivity(), items );
			ListView list = (ListView) _view.findViewById( R.id.listCuenta );
			list.setAdapter( edoCuentaListAdapter );
			list.setVisibility( View.VISIBLE );
		}
		else
		{
			alert( "No se ha podido obtener los datos de tu cuenta. Por favor intenta nuevamente más tarde." );
		}
	}

	private void addIfValid( ArrayList<PremiosExternoClient.EdoCtaResponse.Record> items, PremiosExternoClient.EdoCtaResponse.Record record )
	{
		if( !record.getDescription().equalsIgnoreCase( "NO HUBO MOVIMIENTOS" ) )
		{
			items.add( record );
		}
	}

	private class ServiceCaller extends AsyncTask<Void, Void, Void>
	{
		private int _task;
		private String _socio;
		private Date _inicio;
		private Date _fin;
		private PremiosExternoClient.EdoCtaResponse _response;

		public ServiceCaller( String socio, Date inicio, Date fin )
		{
			_socio = socio;
			_inicio = inicio;
			_fin = fin;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			if( _progress != null )
			{
				_progress.dismiss();
				_progress = null;
			}
			_progress = ProgressDialogFragment.newInstance( "Espere un momento..." );
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		@Override
		protected Void doInBackground( Void... arg0 )
		{
			PremiosExternoClient client = new PremiosExternoClient();

			_response = client.getEdoCta( _socio, _inicio, _fin );

			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			_progress.dismiss();

			dataObtained( _response );
		}
	}

	private void setInitialDates()
	{
		Date now = new Date( System.currentTimeMillis() );
		Calendar calendar = Calendar.getInstance();

		calendar.setTime( now );
		calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
		setToDate( calendar.getTime() );

		calendar.setTime( now );
		calendar.add( Calendar.MONTH, -2 );
		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), 1 );
		setFromDate( calendar.getTime() );
	}

	private Boolean validateLessThanAYear( Date date )
	{
		Date now = new Date( System.currentTimeMillis() );
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( now );
		calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
		Date current = calendar.getTime();

		return getDiffInMonths( current, date ) > 12;
	}

	private void setFromDate( Date date )
	{
		if( validateLessThanAYear( date ) )
		{
			Button btnFrom = (Button) _view.findViewById( R.id.btnFrom );
			btnFrom.setText( "Elegir" );
			_dateFrom = null;
			alert( "Selecciona una fecha no mayor a un año" );
			datesUpdated();
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat( "MMM yyyy" );
		Button btnFrom = (Button) _view.findViewById( R.id.btnFrom );
		btnFrom.setText( Capitalize( sdf.format( date ) ) );
		_dateFrom = date;

		if( _dateTo != null && _dateTo.compareTo( _dateFrom ) < 0 )
		{
			Button btnTo = (Button) _view.findViewById( R.id.btnTo );
			btnTo.setText( "Elegir" );
			_dateTo = null;
		}

		datesUpdated();
		android.util.Log.d( "TEST", "FROM: " + new SimpleDateFormat( "dd MMM yyyy" ).format( _dateFrom ) );
	}

	private void setToDate( Date date )
	{
		if( validateLessThanAYear( date ) )
		{
			Button btnTo = (Button) _view.findViewById( R.id.btnTo );
			btnTo.setText( "Elegir" );
			_dateTo = null;
			alert( "Selecciona una fecha no mayor a un año" );
			datesUpdated();
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat( "MMM yyyy" );
		Button btnTo = (Button) _view.findViewById( R.id.btnTo );
		btnTo.setText( Capitalize( sdf.format( date ) ) );
		_dateTo = date;

		if( _dateFrom != null && _dateFrom.compareTo( _dateTo ) > 0 )
		{
			Button btnFrom = (Button) _view.findViewById( R.id.btnFrom );
			btnFrom.setText( "Elegir" );
			_dateFrom = null;
		}

		datesUpdated();
		android.util.Log.d( "TEST", "TO: " + new SimpleDateFormat( "dd MMM yyyy" ).format( _dateTo ) );
	}

	private void setDatePickerDialogFrom()
	{
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime( _dateFrom );
		_datePickerDialogFrom = new DatePickerNoDayDialog( getActivity(), new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth )
			{
				if( _setDateFrom )
				{
					Calendar newDate = Calendar.getInstance();
					newDate.set( year, monthOfYear, dayOfMonth );
					setFromDate( newDate.getTime() );
				}
			}
		}, newCalendar.get( Calendar.YEAR ), newCalendar.get( Calendar.MONTH ), 1 );
		_datePickerDialogFrom.setButton( DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				_setDateFrom = false;
			}
		} );
		_datePickerDialogFrom.setButton( DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				_setDateFrom = true;
			}
		} );
		_datePickerDialogFrom.setCancelable( false );
	}

	private void setDatePickerDialogTo()
	{
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime( _dateTo );
		_datePickerDialogTo = new DatePickerNoDayDialog( getActivity(), new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth )
			{
				if( _setDateTo )
				{
					Calendar newDate = Calendar.getInstance();
					newDate.set( year, monthOfYear, dayOfMonth );
					newDate.set( year, monthOfYear, newDate.getActualMaximum( Calendar.DAY_OF_MONTH ) );
					setToDate( newDate.getTime() );
				}
			}
		}, newCalendar.get( Calendar.YEAR ), newCalendar.get( Calendar.MONTH ), 1 );
		_datePickerDialogTo.setButton( DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				_setDateTo = false;
			}
		} );
		_datePickerDialogTo.setButton( DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				_setDateTo = true;
			}
		} );
		_datePickerDialogTo.setCancelable( false );
	}

	private String Capitalize( String text )
	{
		if( text.length() > 1 )
		{
			return text.toUpperCase().substring( 0, 1 ) + text.substring( 1 );
		}
		else
		{
			return "";
		}
	}

	private void alert( String message )
	{
		AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
		alert.setTitle( "Atención" );
		alert.setMessage( message );
		alert.setIcon( R.drawable.notification_warning_small );
		alert.setCancelable( false );
		alert.setButton( DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
			}
		} );
		alert.show();
	}

	public static int getDiffInMonths( Date first, Date last )
	{
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(first);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(last);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

		return Math.abs( diffMonth );
	}
}
