package com.zebstudios.cityexpress;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

import static com.appsee.Appsee.startScreen;


/**
 * Created by Denumeris Interactive on 3/3/2015.
 */
public class MessageCenterActivity extends ActionBarActivity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        startScreen("ViewBlog");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagecenter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(android.R.color.transparent);

        if( CompatibilityUtil.isTablet( this ) )
        {
            ListView list = (ListView) findViewById( R.id.list_rss );
            list.setPadding( 140, 60, 140, 60 );
        }

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        super.onOptionsItemSelected( item );
        switch( item.getItemId() )
        {
            case android.R.id.home:
                MessageCenterActivity.this.onBackPressed();
                break;
        }
        return true;
    }


}
