package com.zebstudios.cityexpress;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by Denumeris Interactive on 25/11/2014.
 */
public class NestedGridView extends GridView //implements View.OnTouchListener, AbsListView.OnScrollListener
{

	public NestedGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NestedGridView(Context context) {
		super(context);
	}

	public NestedGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int heightSpec;

		if (getLayoutParams().height == LayoutParams.WRAP_CONTENT)
		{
			heightSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST );
		}
		else
		{
			// Any other height should be respected as is.
			heightSpec = heightMeasureSpec;
		}

		super.onMeasure(widthMeasureSpec, heightSpec);

	}

	/*private int listViewTouchAction;
	private static final int MAXIMUM_LIST_ITEMS_VIEWABLE = 30;

	public NestedGridView( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		listViewTouchAction = -1;
		setOnScrollListener( this );
		setOnTouchListener( this );
	}

	@Override
	public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount )
	{
		if( getAdapter() != null && getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE )
		{
			if( listViewTouchAction == MotionEvent.ACTION_MOVE )
			{
				scrollBy( 0, -1 );
			}
		}
	}

	@Override
	public void onScrollStateChanged( AbsListView view, int scrollState )
	{
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );

		int newHeight = 0;
		final int heightMode = MeasureSpec.getMode( heightMeasureSpec );
		int heightSize = MeasureSpec.getSize( heightMeasureSpec );
		int columns = getNumColumns();
		int columnWith = widthMeasureSpec / columns;
		if( heightMode != MeasureSpec.EXACTLY )
		{
			ListAdapter listAdapter = getAdapter();
			if( listAdapter != null && !listAdapter.isEmpty() )
			{
				int listPosition = 0;
				int maxHeight;
				int measured;
				for( listPosition = 0; listPosition < listAdapter.getCount() && listPosition < MAXIMUM_LIST_ITEMS_VIEWABLE; listPosition += columns )
				{
					maxHeight = 0;
					for( int j=0; j<columns; j++ )
					{
						if( listPosition + j < listAdapter.getCount() )
						{
							View listItem = listAdapter.getView( listPosition + j, null, this );
							measured = getViewHeight( listItem, widthMeasureSpec, heightMeasureSpec );
							if( measured > maxHeight )
							{
								maxHeight = measured;
							}
						}
					}
					android.util.Log.d( "TEST", "MAXHEIGHT: " + maxHeight );
					newHeight += maxHeight;
				}
				//newHeight += getDividerHeight() * listPosition;
			}
			if( ( heightMode == MeasureSpec.AT_MOST ) && ( newHeight > heightSize ) )
			{
				if( newHeight > heightSize )
				{
					newHeight = heightSize;
				}
			}
		}
		else
		{
			newHeight = getMeasuredHeight();
		}
		setMeasuredDimension( getMeasuredWidth(), newHeight );
	}

	private int getViewHeight( View listItem, int widthMeasureSpec, int heightMeasureSpec )
	{
		if( listItem instanceof ViewGroup )
		{
			listItem.setLayoutParams( new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
		}
		listItem.measure( widthMeasureSpec, heightMeasureSpec );
		return listItem.getMeasuredHeight();
	}

	@Override
	public boolean onTouch( View v, MotionEvent event )
	{
		if( getAdapter() != null && getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE )
		{
			if( listViewTouchAction == MotionEvent.ACTION_MOVE )
			{
				scrollBy( 0, 1 );
			}
		}
		return false;
	}*/
}
