package com.zebstudios.cityexpress;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Denumeris Interactive on 22/10/2014.
 */
public class PromotionsImageView extends ImageView
{
	public PromotionsImageView( Context context )
	{
		super( context );
	}

	public PromotionsImageView( Context context, AttributeSet attrs )
	{
		super( context, attrs );
	}

	public PromotionsImageView( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );

		double px =  getMeasuredWidth() / 680.0;
		setMeasuredDimension( getMeasuredWidth(), (int)Math.round( px * 868 ) ); //Snap to width
	}
}
