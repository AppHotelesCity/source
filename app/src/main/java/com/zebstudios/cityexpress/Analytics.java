package com.zebstudios.cityexpress;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by Denumeris Interactive on 12/10/2014.
 */
public class Analytics extends Application
{
	private static final String PROPERTY_ID = "UA-24713703-16";
	public static int GENERAL_TRACKER = 0;

	public Analytics()
	{
		super();
	}

	HashMap<TrackerName, Tracker> _trackers = new HashMap<TrackerName, Tracker>();

	synchronized Tracker getTracker( TrackerName trackerId )
	{
		if( !_trackers.containsKey( trackerId ) )
		{
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.getLogger().setLogLevel( Logger.LogLevel.WARNING );
			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker( R.xml.global_tracker )
					: analytics.newTracker( R.xml.ecommerce_tracker );
			t.enableAdvertisingIdCollection(true);
			_trackers.put(trackerId, t);
		}
		return _trackers.get(trackerId);
	}


	public void sendAppScreenTrack( String screenName )
	{
		try
		{
			Tracker t = getTracker( TrackerName.APP_TRACKER );
			t.setScreenName( screenName );
			t.send( new HitBuilders.AppViewBuilder().build() );
		}
		catch( Exception e )
		{
			android.util.Log.w( "ANALYTICS", "ERROR SENDING APP SCREEN TRACK: " + e.getMessage() );
		}

	}

	public void sendAppEventTrack( String screenName, String category, String action, String label, long value )
	{
		try
		{
			Tracker t = getTracker( TrackerName.APP_TRACKER );
			t.setScreenName( screenName );

			HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
			builder.setCategory( category );
			builder.setAction( action );
			builder.setLabel( label );
			builder.setValue( value );

			t.send( builder.build() );
		}
		catch( Exception e )
		{
			android.util.Log.w( "ANALYTICS", "ERROR SENDING APP EVENT TRACK: " + e.getMessage() );
		}

	}

	public enum TrackerName
	{
		APP_TRACKER,
		GLOBAL_TRACKER,
		ECOMMERCE_TRACKER,
	}

}
