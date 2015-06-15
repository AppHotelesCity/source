package com.zebstudios.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by rczuart on 11/09/2014.
 */
public class CalendarGridAdapter extends BaseAdapter
{
	protected ArrayList<DateTime> datetimeList;
	protected int month;
	protected int year;
	protected Context context;
	protected ArrayList<DateTime> disableDates;
	protected ArrayList<DateTime> selectedDates;

	// Use internally, to make the search for date faster instead of using
	// indexOf methods on ArrayList
	protected HashMap<DateTime, Integer> disableDatesMap = new HashMap<DateTime, Integer>();
	protected HashMap<DateTime, Integer> selectedDatesMap = new HashMap<DateTime, Integer>();

	protected DateTime minDateTime;
	protected DateTime maxDateTime;
	protected DateTime today;
	protected int startDayOfWeek;
	protected boolean sixWeeksInCalendar;
	protected Resources resources;

	/**
	 * calendarData belongs to Calendar
	 */
	protected HashMap<String, Object> calendarData;
	/**
	 * extraData belongs to client
	 */
	protected HashMap<String, Object> extraData;

	public void setAdapterDateTime( DateTime dateTime )
	{
		this.month = dateTime.getMonth();
		this.year = dateTime.getYear();
		this.datetimeList = CalendarHelper.getFullWeeks( this.month, this.year,
				startDayOfWeek, sixWeeksInCalendar );
	}

	// GETTERS AND SETTERS
	public ArrayList<DateTime> getDatetimeList()
	{
		return datetimeList;
	}

	public DateTime getMinDateTime()
	{
		return minDateTime;
	}

	public void setMinDateTime( DateTime minDateTime )
	{
		this.minDateTime = minDateTime;
	}

	public DateTime getMaxDateTime()
	{
		return maxDateTime;
	}

	public void setMaxDateTime( DateTime maxDateTime )
	{
		this.maxDateTime = maxDateTime;
	}

	public ArrayList<DateTime> getDisableDates()
	{
		return disableDates;
	}

	public void setDisableDates( ArrayList<DateTime> disableDates )
	{
		this.disableDates = disableDates;
	}

	public ArrayList<DateTime> getSelectedDates()
	{
		return selectedDates;
	}

	public void setSelectedDates( ArrayList<DateTime> selectedDates )
	{
		this.selectedDates = selectedDates;
	}

	public HashMap<String, Object> getCalendarData()
	{
		return calendarData;
	}

	public void setCalendarData( HashMap<String, Object> calendarData )
	{
		this.calendarData = calendarData;

		// Reset parameters
		populateFromCalendarData();
	}

	public HashMap<String, Object> getExtraData()
	{
		return extraData;
	}

	public void setExtraData( HashMap<String, Object> extraData )
	{
		this.extraData = extraData;
	}

	/**
	 * Constructor
	 *
	 * @param context
	 * @param month
	 * @param year
	 * @param calendarData
	 * @param extraData
	 */
	public CalendarGridAdapter( Context context, int month, int year,
								HashMap<String, Object> calendarData,
								HashMap<String, Object> extraData )
	{
		super();
		this.month = month;
		this.year = year;
		this.context = context;
		this.calendarData = calendarData;
		this.extraData = extraData;
		this.resources = context.getResources();

		// Get data from calendarData
		populateFromCalendarData();
	}

	/**
	 * Retrieve internal parameters from calendar data
	 */
	@SuppressWarnings( "unchecked" )
	private void populateFromCalendarData()
	{
		disableDates = (ArrayList<DateTime>) calendarData
				.get( CalendarFragment.DISABLE_DATES );
		if( disableDates != null )
		{
			disableDatesMap.clear();
			for( DateTime dateTime : disableDates )
			{
				disableDatesMap.put( dateTime, 1 );
			}
		}

		selectedDates = (ArrayList<DateTime>) calendarData
				.get( CalendarFragment.SELECTED_DATES );
		if( selectedDates != null )
		{
			selectedDatesMap.clear();
			for( DateTime dateTime : selectedDates )
			{
				selectedDatesMap.put( dateTime, 1 );
			}
		}

		minDateTime = (DateTime) calendarData
				.get( CalendarFragment._MIN_DATE_TIME );
		maxDateTime = (DateTime) calendarData
				.get( CalendarFragment._MAX_DATE_TIME );
		startDayOfWeek = (Integer) calendarData
				.get( CalendarFragment.START_DAY_OF_WEEK );
		sixWeeksInCalendar = (Boolean) calendarData
				.get( CalendarFragment.SIX_WEEKS_IN_CALENDAR );

		this.datetimeList = CalendarHelper.getFullWeeks( this.month, this.year,
				startDayOfWeek, sixWeeksInCalendar );
	}

	public void updateToday()
	{
		today = CalendarHelper.convertDateToDateTime( new Date() );
	}

	protected DateTime getToday()
	{
		if( today == null )
		{
			today = CalendarHelper.convertDateToDateTime( new Date() );
		}
		return today;
	}

	@SuppressWarnings( "unchecked" )
	protected void setCustomResources( DateTime dateTime, View backgroundView,
									   TextView textView )
	{
		// Set custom background resource
		HashMap<DateTime, Integer> backgroundForDateTimeMap = (HashMap<DateTime, Integer>) calendarData
				.get( CalendarFragment._BACKGROUND_FOR_DATETIME_MAP );
		if( backgroundForDateTimeMap != null )
		{
			// Get background resource for the dateTime
			Integer backgroundResource = backgroundForDateTimeMap.get( dateTime );

			// Set it
			if( backgroundResource != null )
			{
				backgroundView.setBackgroundResource( backgroundResource.intValue() );
			}
		}

		// Set custom text color
		HashMap<DateTime, Integer> textColorForDateTimeMap = (HashMap<DateTime, Integer>) calendarData
				.get( CalendarFragment._TEXT_COLOR_FOR_DATETIME_MAP );
		if( textColorForDateTimeMap != null )
		{
			// Get textColor for the dateTime
			Integer textColorResource = textColorForDateTimeMap.get( dateTime );

			// Set it
			if( textColorResource != null )
			{
				textView.setTextColor( resources.getColor( textColorResource.intValue() ) );
			}
		}
	}

	/**
	 * Customize colors of text and background based on states of the cell
	 * (disabled, active, selected, etc)
	 * <p/>
	 * To be used only in getView method
	 *
	 * @param position
	 * @param cellView
	 */
	protected void customizeTextView( int position, TextView cellView )
	{
		cellView.setTextColor( Color.BLACK );

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get( position );

		// Set color of the dates in previous / next month
		if( dateTime.getMonth() != month )
		{
			cellView.setTextColor( resources
					.getColor( R.color.calendar_darker_gray ) );
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if( ( minDateTime != null && dateTime.lt( minDateTime ) )
				|| ( maxDateTime != null && dateTime.gt( maxDateTime ) )
				|| ( disableDates != null && disableDatesMap
				.containsKey( dateTime ) ) )
		{

			cellView.setTextColor( CalendarFragment.disabledTextColor );
			if( CalendarFragment.disabledBackgroundDrawable == -1 )
			{
				cellView.setBackgroundResource( R.drawable.disable_cell );
			}
			else
			{
				cellView.setBackgroundResource( CalendarFragment.disabledBackgroundDrawable );
			}

			if( dateTime.equals( getToday() ) )
			{
				cellView.setBackgroundResource( R.drawable.red_border_gray_bg );
			}
		}
		else
		{
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if( selectedDates != null && selectedDatesMap.containsKey( dateTime ) )
		{
			if( CalendarFragment.selectedBackgroundDrawable != -1 )
			{
				cellView.setBackgroundResource( CalendarFragment.selectedBackgroundDrawable );
			}
			else
			{
				cellView.setBackgroundColor( resources
						.getColor( R.color.calendar_sky_blue ) );
			}

			cellView.setTextColor( CalendarFragment.selectedTextColor );
		}
		else
		{
			shouldResetSelectedView = true;
		}

		if( shouldResetDiabledView && shouldResetSelectedView )
		{
			// Customize for today
			if( dateTime.equals( getToday() ) )
			{
				cellView.setBackgroundResource( R.drawable.red_border );
			}
			else
			{
				cellView.setBackgroundResource( R.drawable.cell_bg );
			}
		}

		cellView.setText( "" + dateTime.getDay() );

		// Set custom color if required
		setCustomResources( dateTime, cellView, cellView );
	}

	@Override
	public int getCount()
	{
		return this.datetimeList.size();
	}

	@Override
	public Object getItem( int arg0 )
	{
		return null;
	}

	@Override
	public long getItemId( int arg0 )
	{
		return 0;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		TextView cellView = (TextView) convertView;

		// For reuse
		if( convertView == null )
		{
			cellView = (TextView) inflater.inflate( R.layout.date_cell, null );
		}

		customizeTextView( position, cellView );

		return cellView;
	}
}
