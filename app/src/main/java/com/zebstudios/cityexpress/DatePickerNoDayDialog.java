package com.zebstudios.cityexpress;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rczuart on 4/28/2015.
 */
public class DatePickerNoDayDialog extends DatePickerDialog
{
	public DatePickerNoDayDialog( Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth )
	{
		super( context, callBack, year, monthOfYear, dayOfMonth );
		updateTitle( year, monthOfYear, dayOfMonth );
		this.hideDay();
	}

	public void onDateChanged( DatePicker view, int year, int month, int day )
	{
		updateTitle( year, month, day );
	}

	private void updateTitle( int year, int month, int day )
	{
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.set( Calendar.YEAR, year );
		mCalendar.set( Calendar.MONTH, month );
		mCalendar.set( Calendar.DAY_OF_MONTH, day );
		setTitle( getFormat().format( mCalendar.getTime() ) );
	}

	/*
	 * the format for dialog tile,and you can override this method
	 */
	public SimpleDateFormat getFormat()
	{
		return new SimpleDateFormat( "MMMM yyyy" );
	}


	private void hideDay()
	{
		try
		{
			Field f[] = this.getClass().getSuperclass().getDeclaredFields();
			for( Field field : f )
			{
				//android.util.Log.d( "TEST", "FIELD: " + field.getName() );
				if( field.getName().equals("mDatePicker") )
				{
					field.setAccessible( true );
					Object pickerTohide = new Object();
					pickerTohide = field.get( this );
					DatePicker picker = (DatePicker)pickerTohide;
					Field f2[] = picker.getClass().getDeclaredFields();

					for( Field field2 : f2 )
					{
						//android.util.Log.d( "TEST", "FIELD2: " + field2.getName() );

						if (field2.getName().equals("mDayPicker") || field2.getName().equals("mDaySpinner") || field2.getName().equals("mCalendarView") )
						{
							field2.setAccessible( true );
							Object viewToHide = new Object();
							viewToHide = field2.get( picker );
							( (View) viewToHide ).setVisibility( View.GONE );
						}
					}
				}
			}
		}
		catch( Exception e )
		{
			android.util.Log.d( "ERROR", e.getMessage() );
		}
	}

}
