package com.zebstudios.cityexpress;

import android.support.v4.view.ViewPager;

/**
 * Created by Denumeris Interactive on 23/10/2014.
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener
{

	void setViewPager( ViewPager view );

	void setViewPager( ViewPager view, int initialPosition );

	void setCurrentItem( int item );

	void setOnPageChangeListener( ViewPager.OnPageChangeListener listener );

	void notifyDataSetChanged();
}
