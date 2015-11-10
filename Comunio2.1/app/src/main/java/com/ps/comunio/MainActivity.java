package com.ps.comunio;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    private String user; //Para que la otra actividad pueda acceder al nombre, tiene que ser pública y estática, pero esto puede ocasionar problemas en el momento en que se pueda cerrar sesión y logearse con otro user
    private String contra;
    TextView login;
    TextView pswd;
    Button boton;
    CheckBox chkReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Parte 2
        setContentView(R.layout.activity_login);
        chkReg = (CheckBox)findViewById(R.id.chkBoxRegistrar);
        login = (TextView)findViewById(R.id.txtLogin);
        pswd = (TextView)findViewById(R.id.txtPswd);
        boton = (Button)findViewById(R.id.btnLogin);
        UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);



    }
    /*
    public void sendMessage(View view){
        EditText usuario = (EditText) findViewById(R.id.username);
        String strUsuario = usuario.getText().toString();
        EditText pass = (EditText)findViewById(R.id.editText2);
        String strPass = pass.getText().toString();

        if(strUsuario.equals("Pepito") && strPass.equals("0000")) {
            Intent intent = new Intent(this, Menuss.class);
            //intent.putExtra(EXTRA_MESSAGE, strUsuario);
            setNombre("Pepito");
            startActivity(intent);
        }else{
            Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
        }
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }
    /*public void setNombre(String nombre){
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.setUsuario(nombre);
    }*/
    public void testlogin(View v) throws InterruptedException {
        //Abrimos / Creamos la bd
        UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1); //El 1 indica la versión, si siempre es 1, no volverá a crear la bbdd
        SQLiteDatabase db = usdbh.getWritableDatabase();
        user = login.getText().toString();
        contra = pswd.getText().toString();
        if (chkReg.isChecked()){
            //Hacer registro
            if(db != null){
                //Comprobamos si el nombre de usuario está registrado ya
                Cursor c = db.rawQuery(" SELECT nombre FROM Usuarios WHERE nombre='"+user+"' ",null);
                if(c.moveToFirst()){
                    Toast.makeText(this, "Nombre ya registrado", Toast.LENGTH_SHORT).show();
                }else{
                    //Usuario no registrado
                    db.execSQL("INSERT INTO Usuarios (nombre,contra) VALUES ('" + user + "','" + contra + "')");
                    Toast.makeText(this, "Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
                    db.close();
                    Intent intent = new Intent(this, Menuss.class);
                    intent.putExtra("username",user);
                    startActivity(intent);
                }
            }

        }else { //LOGIN
            Cursor c = db.rawQuery("SELECT contra FROM Usuarios WHERE nombre='"+user+"'",null);
            //Si existe en la bbdd el usuario
            if(c.moveToFirst()){
                contra = c.getString(0);
                if (pswd.getText().toString().equals(contra)) {
                    user = login.getText().toString();
                    db.close();
                    Intent intent = new Intent(this, Menuss.class);
                    intent.putExtra("username",user);
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "Usuario y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Usuario y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }

    public void RegisterOnOff(View v){
        if (chkReg.isChecked()) {
            boton.setText("Registrar");
            //Escribir en base de datos o fichero
        }else
            boton.setText("Aceptar");
    }


}

