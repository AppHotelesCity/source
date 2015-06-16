package com.zebstudios.calendar;

import android.view.View;

import java.util.Date;

/**
 * Created by rczuart on 11/09/2014.
 */
public abstract class CalendarListener
{
	/**
	 * Inform client user has clicked on a date
	 * @param date
	 * @param view
	 */
	public abstract void onSelectDate(Date date, View view);


	/**
	 * Inform client user has long clicked on a date
	 * @param date
	 * @param view
	 */
	public void onLongClickDate(Date date, View view) {
		// Do nothing
	}


	/**
	 * Inform client that calendar has changed month
	 * @param month
	 * @param year
	 */
	public void onChangeMonth(int month, int year) {
		// Do nothing
	};


	/**
	 * Inform client that CaldroidFragment view has been created and views are
	 * no longer null. Useful for customization of button and text views
	 */
	public void onCalendarViewCreated() {
		// Do nothing
	}
}
