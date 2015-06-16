package com.zebstudios.cityexpress;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rczuart on 23/10/2014.
 */
public class ProgressDialogFragment extends DialogFragment
{
	private String _label;
	private TextView _textView;

	public static ProgressDialogFragment newInstance()
	{
		return new ProgressDialogFragment();
	}

	public static ProgressDialogFragment newInstance( String label )
	{
		ProgressDialogFragment dialog = new ProgressDialogFragment();
		dialog.setLabel( label );
		return dialog;
	}

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setStyle( STYLE_NO_FRAME, android.R.style.Theme_Translucent );
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate( R.layout.fragment_progress_dialog, container, false );

		_textView = (TextView)view.findViewById( R.id.label );
		_textView.setText( _label );

		return view;
	}

	public void ChageLabel( String text )
	{
		_textView.setText( text );
	}

	public String getLabel()
	{
		return _label;
	}

	public void setLabel( String label )
	{
		_label = label;
	}
}
