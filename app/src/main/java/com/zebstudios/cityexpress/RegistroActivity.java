package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroActivity extends Activity {

    TextView tituloToolbar;
    ImageView imageViewBack;
    EditText edtxtNombre;
    EditText edtxtApPaterno;
    EditText edtxtApMaterno;
    EditText edtxtCorreo;
    EditText edtxtPass;
    EditText edtxtTelefono;

    Button btnGenero;
    Button btnTitulo;
    Button btnPais;
    Button btnRegistrar;

    //String genero[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        tituloToolbar = (TextView) findViewById(R.id.toolbarTitle);
        imageViewBack = (ImageView) findViewById(R.id.back_button);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edtxtNombre = (EditText)findViewById(R.id.edtxtNombre);
        edtxtApPaterno = (EditText)findViewById(R.id.edtxtApPaterno);
        edtxtApMaterno = (EditText)findViewById(R.id.edtxtApMaterno);
        edtxtCorreo = (EditText)findViewById(R.id.edtxtCorreo);
        edtxtPass = (EditText)findViewById(R.id.edtxtPassReg);
        edtxtTelefono = (EditText)findViewById(R.id.edtxtTelefono);

        btnGenero = (Button)findViewById(R.id.btnGenero);
        btnPais = (Button)findViewById(R.id.btnPais);
        btnTitulo = (Button)findViewById(R.id.btnTitulo);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrate);

       // genero = getResources().getStringArray(R.array.gender_user);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

       /* btnGenero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                builder.setTitle(getResources().getString(R.string.genero))
                        .setItems(genero , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btnGenero.setText(genero[which]);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }); */

       btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if("".equals(edtxtNombre.getText().toString())){

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Wisum")
                            .setMessage("El campo Nombre es obligatorio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(edtxtApPaterno.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Wisum")
                            .setMessage("El campo Apellido Paterno es obligatorio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(edtxtApMaterno.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Wisum")
                            .setMessage("El campo Apellido Materno es obligatorio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(edtxtCorreo.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Wisum")
                            .setMessage("El campo Correo es obligatorio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(edtxtPass.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Wisum")
                            .setMessage("El campo Contraseña es obligatorio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if("".equals(edtxtTelefono.getText().toString())){

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Wisum")
                            .setMessage("El campo Teléfono es obligatorio")
                            .setNeutralButton(R.string.entendido, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{

                    edtxtNombre.getText();
                    edtxtApPaterno.getText();
                    edtxtApMaterno.getText();
                    edtxtCorreo.getText();
                    edtxtPass.getText();
                    edtxtTelefono.getText();

                    Toast.makeText(RegistroActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                    System.out.println("----> " + edtxtNombre.getText().toString());
                    System.out.println("----> " + edtxtApPaterno.getText().toString());
                    System.out.println("----> " + edtxtApMaterno.getText().toString());
                    System.out.println("----> " + edtxtCorreo.getText().toString());
                    System.out.println("----> " + edtxtPass.getText().toString());
                    System.out.println("----> " + edtxtTelefono.getText().toString());
                }
            }
        });
        
    }
}
