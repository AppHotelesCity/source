package com.zebstudios.cityexpress;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.urbanairship.UAirship;
import com.urbanairship.richpush.RichPushInbox;
import com.urbanairship.richpush.RichPushMessage;

import java.util.ArrayList;
import java.util.List;


public class MessagecenterFragment extends DialogFragment
{
    private View _view;
    private ArrayList<RichPushMessage> _mensaje;
    private ProgressDialogFragment _progress;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        _view = inflater.inflate( R.layout.fragment_message_center, container, false );
        Log.e("Messagecenter", "OncreateView :3");
        RichPushInbox mensaje = UAirship.shared().getRichPushManager().getRichPushInbox();
        List<RichPushMessage> mensajes = mensaje.getMessages();

        _mensaje = new ArrayList<RichPushMessage>();
        for(RichPushMessage msj : mensajes) {
            _mensaje.add(msj);
        }

        EstadosObtained();


        return _view;
    }


    private void EstadosObtained()
    {
        Log.e("TEST", "RESULTS: " + _mensaje.size());
        
        MessagecenterListAdapter messagecenterlistadap = new MessagecenterListAdapter(getActivity(), _mensaje);
        ListView list = (ListView) _view.findViewById(R.id.result_list);
        list.setAdapter(messagecenterlistadap);
        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
            {
                ItemSelected( i );
                Log.d("PromoCodeFragment", "Onitemclick :3 ");
            }
        });

    }

    @Override
    public void onStart()
    {
        super.onStart();
        if( getDialog() == null )
        {
            return;
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        getDialog().getWindow().setLayout( width / 2, height / 2 );
        getDialog().setTitle("Inbox");
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if( getDialog() == null )
        {
            //ActionBarActivity activity = (ActionBarActivity) getActivity();
            //activity.getSupportActionBar().setTitle( "PromoCode" );
        }
    }

    public void ItemSelected( int position )
    {



        RichPushMessage msj = _mensaje.get(position);
        Log.e("Messagecenter", "Tocaste --> " + msj.getTitle());
        MessageFragment fragment = new MessageFragment();
        msj.markRead();
        fragment = fragment.newInstance(msj.getMessageId());
        getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();
		//getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "tag").addToBackStack(null).commit();

        //getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "tag").addToBackStack(null).commit();

        //getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();

        MainActivity main = (MainActivity) getActivity();
        main.OcultarBagde();

    }


}
