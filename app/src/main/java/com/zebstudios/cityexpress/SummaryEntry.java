package com.zebstudios.cityexpress;

/**
 * Created by Denumeris Interactive on 29/10/2014.
 */
public class SummaryEntry
{
	private int _type;
	private String _text;

	public SummaryEntry( int type, String text )
	{
		_type = type;
		_text = text;
	}

	public int getType()
	{
		return _type;
	}

	public String getText()
	{
		return _text;
	}
}
