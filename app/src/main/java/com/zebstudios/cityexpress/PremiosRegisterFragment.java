package com.zebstudios.cityexpress;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Denumeris Interactive on 4/24/2015.
 */
public class PremiosRegisterFragment extends Fragment
{
	private final int FRAGMENT_LIST_COUNTRIES = 100;

	private View _view;
	DatePickerDialog _datePickerDialog;
	Date _birthday;
	Boolean _setBirthday;
	private ProgressDialogFragment _progress;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if( CompatibilityUtil.isTablet( getActivity() ) )
			_view = inflater.inflate( R.layout.fragment_premiosregister_tablet, container, false );
		else
			_view = inflater.inflate( R.layout.fragment_premiosregister, container, false );

		this.setGeneroSpin();
		this.setDatePickerDialog();
		EditText txtBirthday = (EditText) _view.findViewById( R.id.txtBirthday );
		txtBirthday.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				_datePickerDialog.show();
			}
		} );

		EditText txtCountry = (EditText) _view.findViewById( R.id.txtCountry );
		txtCountry.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				listCountries();
			}
		} );

		Button btnCrear = (Button) _view.findViewById( R.id.btnCrear );
		btnCrear.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				createAccount();
			}
		} );

		return _view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.getSupportActionBar().setTitle( "Crear cuenta" );
	}

	public void createAccount()
	{
		if( validate() )
		{
			EditText txtName = (EditText)_view.findViewById( R.id.txtName );
			EditText txtPaterno = (EditText)_view.findViewById( R.id.txtPaterno );
			EditText txtMaterno = (EditText)_view.findViewById( R.id.txtMaterno );
			Spinner spinGenero = (Spinner) _view.findViewById( R.id.spinGenero );
			EditText txtCountry = (EditText)_view.findViewById( R.id.txtCountry );
			EditText txtPhone = (EditText)_view.findViewById( R.id.txtPhone );
			EditText txtEmail = (EditText)_view.findViewById( R.id.txtEmail );

			PremiosExternoClient.entSocioCP param = new PremiosExternoClient.entSocioCP();
			param.getAplicacion().setClaveAplicacion( "903" );
			param.getProgramaSocio().setClaveProg( "1" );
			param.getEsfuerzoSocio().setClaveEsfuerzo( "A" );
			param.getOrigenSocio().setClaveOrigen( "01" );
			param.setUsuarioAltaSocio( "APP" );

			param.setNombreSocio( txtName.getText().toString() );
			param.setApPaternoSocio( txtPaterno.getText().toString() );
			param.setApMaternoSocio( txtMaterno.getText().toString() );
			param.setSexoSocio( spinGenero.getSelectedItemPosition() == 0 ? "HOMBRE" : "MUJER" );
			param.setPaisSocio( txtCountry.getText().toString() );
			param.setTelefonoSocio( txtPhone.getText().toString() );
			param.setFechaNacSocio( _birthday );
			param.setCorreoEmailSocio( txtEmail.getText().toString() );

			new ServiceCaller( param ).execute();
		}
	}

	private void afiliacionComplete( PremiosExternoClient.SocioResponse response )
	{
		android.util.Log.d( "TEST", "RESPONSE: " + response.getResponse() + " MSG: " + response.getSocio().getsMensajeAfiliacion() );
		if( response.getResponse() == 0 )
		{
			if( response.getSocio().getsMensajeAfiliacion().equalsIgnoreCase( "OK" ) )
			{
				alertAndWaitForResponse( "Cuenta creada exitosamente. Tu contraseña se envío a tu correo electrónico." );
			}
			else
			{
				alert( "No se ha podido crear tu cuenta. Intenta nuevamente más tarde. Error: " + response.getSocio().getsMensajeAfiliacion() );
			}
		}
		else
			alert( "No se ha podido crear tu cuenta. Intenta nuevamente más tarde." );
	}

	private void userInformed()
	{
		//getFragmentManager().beginTransaction().remove( PremiosRegisterFragment.this ).commit();
		//PremiosLoginFragment fragment = new PremiosLoginFragment();
		//getFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).addToBackStack( "login" ).commit();
		getActivity().onBackPressed();
	}

	private Boolean validate()
	{
		if( !validateRequired( R.id.txtName ) )
		{
			alert( "Por favor, ingresa tu nombre." );
			return false;
		}
		if( !validateRequired( R.id.txtPaterno ) )
		{
			alert( "Por favor, ingresa tu apellido paterno." );
			return false;
		}
		if( !validateRequired( R.id.txtMaterno ) )
		{
			alert( "Por favor, ingresa tu apellido materno." );
			return false;
		}

		Spinner spinGenero = (Spinner) _view.findViewById( R.id.spinGenero );
		if( spinGenero.getSelectedItemPosition() == 2 )
		{
			alert( "Por favor, selecciona tu género." );
			return false;
		}

		if( _birthday == null )
		{
			alert( "Por favor, ingresa tu fecha de nacimiento." );
			return false;
		}

		if( !validateRequired( R.id.txtCountry ) )
		{
			alert( "Por favor, selecciona tu país." );
			return false;
		}

		if( !validateRequired( R.id.txtPhone ) )
		{
			alert( "Por favor, ingresa tu teléfono." );
			return false;
		}

		if( !validateRequired( R.id.txtEmail ) )
		{
			alert( "Por favor, ingresa tu correo electrónico." );
			return false;
		}

		EditText txtEmail = (EditText) _view.findViewById( R.id.txtEmail );
		if( !isEmailValid( txtEmail.getText() ) )
		{
			alert( "Por favor, ingresa un correo electrónico válido." );
			return false;
		}

		return true;
	}

	private boolean isEmailValid( CharSequence email )
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher( email ).matches();
	}

	private Boolean validateRequired( int id )
	{
		EditText field = (EditText) _view.findViewById( id );
		if( field.getText().toString().trim().length() == 0 )
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private void listCountries()
	{
		CountriesDialogFragment fragment = new CountriesDialogFragment();
		fragment.setTargetFragment( this, FRAGMENT_LIST_COUNTRIES );
		if( CompatibilityUtil.isTablet( getActivity() ) )
		{
			fragment.show( getFragmentManager(), "dialog" );
		}
		else
		{
			getFragmentManager().beginTransaction().add( R.id.fragment_container, fragment ).addToBackStack( "list_countries" ).commit();
		}
	}

	public void setSelectedCountry( Country c )
	{
		EditText txtCountry = (EditText) _view.findViewById( R.id.txtCountry );
		txtCountry.setText( c.getName() );
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

	private void alertAndWaitForResponse( String message )
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
				userInformed();
			}
		} );
		alert.show();
	}

	private void setDatePickerDialog()
	{
		_birthday = new Date( System.currentTimeMillis() );
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime( _birthday );
		_datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth )
			{
				if( _setBirthday )
				{
					Calendar newDate = Calendar.getInstance();
					newDate.set( year, monthOfYear, dayOfMonth );
					_birthday = newDate.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
					EditText txtBirthday = (EditText) _view.findViewById( R.id.txtBirthday );
					txtBirthday.setText( sdf.format( newDate.getTime() ) );
				}
			}
		}, newCalendar.get( Calendar.YEAR ), newCalendar.get( Calendar.MONTH ), newCalendar.get( Calendar.DAY_OF_MONTH ) );
		_datePickerDialog.setButton( DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				_setBirthday = false;
			}
		} );
		_datePickerDialog.setButton( DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				_setBirthday = true;
			}
		} );
		_datePickerDialog.setCancelable( false );
	}

	private void setGeneroSpin()
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_dropdown_item )
		{

			@Override
			public View getView( int position, View convertView, ViewGroup parent )
			{

				View v = super.getView( position, convertView, parent );

				TextView field = (TextView) v.findViewById( android.R.id.text1 );
				if( position == getCount() )
				{
					field.setText( "" );
					field.setHint( getItem( getCount() ) ); //"Hint to be displayed"
				}

				field.setHintTextColor( getResources().getColor( R.color.control_border_light ) );
				field.setTextSize( 18 );

				return v;
			}

			@Override
			public int getCount()
			{
				return super.getCount() - 1; // you dont display last item. It is used as hint.
			}

		};

		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		adapter.add( "Hombre" );
		adapter.add( "Mujer" );
		adapter.add( "Género" );

		Spinner spinner = (Spinner) _view.findViewById( R.id.spinGenero );
		spinner.setAdapter( adapter );
		spinner.setSelection( adapter.getCount() ); //display hint
	}

	private class ServiceCaller extends AsyncTask<Void, Void, Void>
	{
		private int _task;
		private PremiosExternoClient.entSocioCP _param;
		private PremiosExternoClient.SocioResponse _response;

		public ServiceCaller( PremiosExternoClient.entSocioCP param )
		{
			_param = param;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			_progress = ProgressDialogFragment.newInstance( "Espere un momento..." );
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		@Override
		protected Void doInBackground( Void... arg0 )
		{
			PremiosExternoClient client = new PremiosExternoClient();

			_response = client.afiliacionSocio( _param );

			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			_progress.dismiss();

			afiliacionComplete( _response );
		}
	}
}
