package com.huayhuas.vmortopedia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    EditText txtCorreo, txtPasword;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleBtn= findViewById(R.id.googleBtn);
        btnIngresar = findViewById(R.id.btnIngresar);
        txtCorreo = findViewById((R.id.txtCorreo));
        txtPasword = findViewById((R.id.txtPasword));

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    public void iniciarSesion(){
        String usuario = txtCorreo.getText().toString().trim();
        String password = txtPasword.getText().toString().trim();
        if(usuario.equalsIgnoreCase("kevin.huayhuas@gmail.com") && password.equalsIgnoreCase("hola2022")){
            Intent intent = new Intent(MainActivity.this,ActivityPrincipal.class);
            startActivity(intent);
            Toast.makeText(this, "Bienvenido Kevin", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Verifique el Correo o Contraseña " + usuario +" - "+password, Toast.LENGTH_SHORT).show();
        }

    }

    //metodos
    public void signIn(){
        Intent signIntent = gsc.getSignInIntent();
        startActivityForResult(signIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    task.getResult(ApiException.class);
                    navigateToSecondActivity();
                }catch (ApiException e){
                    Toast.makeText(this, "\n" + "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(MainActivity.this,ActivityPrincipal.class);
        startActivity(intent);
    }
}