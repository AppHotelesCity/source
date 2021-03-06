package com.zebstudios.cityexpress;

import android.app.Application;

import com.adform.adformtrackingsdk.AdformTrackingSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
//import com.urbanairship.AirshipConfigOptions;
//import com.urbanairship.UAirship;

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

	public void onCreate() {
		super.onCreate();

		//Configuracion Urban Airship
		//La clase que recibe y manipula las notificacione spush es IntentReceiver
/*
		AirshipConfigOptions options = new AirshipConfigOptions();
		//options.developmentAppKey = "edLry_oVTlSBkX6O04Nzsg";
		//options.developmentAppSecret = "yXBxRhAQQzqxXUp52Yh_lg";
		options.productionAppKey = "Y3RK3BIPQVm03yvVycVX6A";
		options.productionAppSecret = "0B3vc3-0RryAZWB3tF9uXw";
		options.gcmSender = "844586752934";
		options.inProduction = true;

		UAirship.takeOff(this, options);
		UAirship.shared().getPushManager().setUserNotificationsEnabled(true);

		String channelId = UAirship.shared().getPushManager().getChannelId();
		com.urbanairship.Logger.info("My Application Channel ID: " + channelId);

*/
		AdformTrackingSdk.startTracking(this,424115);

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
