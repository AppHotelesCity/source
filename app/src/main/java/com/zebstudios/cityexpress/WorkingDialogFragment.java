package com.zebstudios.cityexpress;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Denumeris Interactive on 23/10/2014.
 */
public class WorkingDialogFragment extends DialogFragment
{
    private int _icon;
    private String _label;
    private TextView _textView;

    public static WorkingDialogFragment newInstance()
    {
        return new WorkingDialogFragment();
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
        View view = inflater.inflate( R.layout.fragment_working_dialog, container, false );

        ImageView imageView = (ImageView)view.findViewById( R.id.imageView );
        imageView.setImageResource( _icon );
        _textView = (TextView)view.findViewById( R.id.label );
        _textView.setText( _label );

        return view;
    }

    public void ChageLabel( String text )
    {
        _textView.setText( text );
    }

    public int getIcon()
    {
        return _icon;
    }

    public void setIcon( int icon )
    {
        _icon = icon;
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

