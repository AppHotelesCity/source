package com.zebstudios.cityexpress;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by rczuart on 4/27/2015.
 */
public class SquareLayout extends LinearLayout
{
	public SquareLayout( Context context )
	{
		super( context );
	}

	public SquareLayout( Context context, AttributeSet attrs )
	{
		super( context, attrs );
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure( widthMeasureSpec, widthMeasureSpec );
	}
}
