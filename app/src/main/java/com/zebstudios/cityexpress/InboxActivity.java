package com.zebstudios.cityexpress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InboxActivity extends Activity {

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
