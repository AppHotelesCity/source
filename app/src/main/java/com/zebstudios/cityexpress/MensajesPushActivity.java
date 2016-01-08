package com.zebstudios.cityexpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DanyCarreto on 07/01/16.
 */
public class MensajesPushActivity extends ActionBarActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inbox);

            TextView txtTitulo = (TextView) findViewById(R.id.toolbarTitle);
            txtTitulo.setText("Inbox");
            ImageView imgView = (ImageView) findViewById(R.id.back_button);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


}
